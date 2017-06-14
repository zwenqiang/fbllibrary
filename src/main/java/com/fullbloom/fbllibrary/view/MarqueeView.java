package com.fullbloom.fbllibrary.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;


import com.fullbloom.fbllibrary.R;
import com.fullbloom.fbllibrary.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GaoXP on 2016/7/1.
 */
public class MarqueeView extends ViewFlipper {
    private Context mContext;
    private List<String> notices;
    private boolean isSetAnimDuration = false;
    private OnItemClickListener onItemClickListener;

    private int interval = 2000;
    private int animDuration = 2000;
    private int textSize = 12;
    private int textColor = 0xffffffff;

    private boolean singleLine = false;
    private int gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
    private static final int TEXT_GRAVITY_LEFT = 0, TEXT_GRAVITY_CENTER = 1, TEXT_GRAVITY_RIGHT = 2;

    private AnimMode mAnimMode = AnimMode.TOPORBOTTOM;
    private ScrollNumMode mScrollNumMode = ScrollNumMode.ONE;
    public enum AnimMode{
        LEFTORRIHTH, TOPORBOTTOM
    }
    public enum ScrollNumMode{
        ONE,MORE
    }

    private Animation animIn;
    private Animation animOut;
    public MarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        if (notices == null) {
            notices = new ArrayList<>();
        }

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MarqueeViewStyle, defStyleAttr, 0);
        interval = typedArray.getInteger(R.styleable.MarqueeViewStyle_mvInterval, interval);
        isSetAnimDuration = typedArray.hasValue(R.styleable.MarqueeViewStyle_mvAnimDuration);
        singleLine = typedArray.getBoolean(R.styleable.MarqueeViewStyle_mvSingleLine, false);
        animDuration = typedArray.getInteger(R.styleable.MarqueeViewStyle_mvAnimDuration, animDuration);
        if (typedArray.hasValue(R.styleable.MarqueeViewStyle_mvTextSize)) {
            textSize = (int) typedArray.getDimension(R.styleable.MarqueeViewStyle_mvTextSize, textSize);
            textSize = DisplayUtil.px2sp(mContext, textSize);
        }
        textColor = typedArray.getColor(R.styleable.MarqueeViewStyle_mvTextColor, textColor);
        int gravityType = typedArray.getInt(R.styleable.MarqueeViewStyle_mvGravity, TEXT_GRAVITY_LEFT);
        switch (gravityType) {
            case TEXT_GRAVITY_CENTER:
                gravity = Gravity.CENTER;
                break;
            case TEXT_GRAVITY_RIGHT:
                gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                break;
        }
        typedArray.recycle();

        setFlipInterval(interval);
    }

    // 根据公告字符串启动轮播
    public void startWithText(final String notice) {
        if (TextUtils.isEmpty(notice)) return;
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                startWithFixedWidth(notice, getWidth());
            }
        });
    }

    // 根据公告字符串列表启动轮播
    public void startWithList(List<String> notices,AnimMode animMode,ScrollNumMode scrollNumMode) {
        mAnimMode = animMode;
        mScrollNumMode = scrollNumMode;
        setNotices(notices);
        start();
    }

    // 根据宽度和公告字符串启动轮播
    private void startWithFixedWidth(String notice, int width) {
        int noticeLength = notice.length();
        int dpW = DisplayUtil.px2dip(mContext, width);
        int limit = dpW / textSize;
        if (dpW == 0) {
            throw new RuntimeException("Please set MarqueeView width !");
        }

        if (noticeLength <= limit) {
            notices.add(notice);
        } else {
            int size = noticeLength / limit + (noticeLength % limit != 0 ? 1 : 0);
            for (int i = 0; i < size; i++) {
                int startIndex = i * limit;
                int endIndex = ((i + 1) * limit >= noticeLength ? noticeLength : (i + 1) * limit);
                notices.add(notice.substring(startIndex, endIndex));
            }
        }
        start();
    }

    // 启动轮播
    public boolean start() {
        if (notices == null || notices.size() == 0) return false;
        removeAllViews();
        //动画滚动方向
        if(mAnimMode == AnimMode.LEFTORRIHTH){
            animIn = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_left);
            animOut = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_right);
        }else {
            animIn = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_in);
            animOut = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_out);
        }
        if (isSetAnimDuration) animIn.setDuration(animDuration);
        setInAnimation(animIn);
        if (isSetAnimDuration) animOut.setDuration(animDuration);
        setOutAnimation(animOut);
        // 单个滚动或多个滚动
        if(mScrollNumMode == ScrollNumMode.MORE){
            moreScroll();
        }else {
            oneScroll();
        }
        if (notices.size() > 1) {
            startFlipping();
        }
        return true;
    }
    /**
     * 作者：zwq
     * 日期: 2016/10/14 14:17
     * 描述: 单个TextView
     */
    private void oneScroll(){
//          单个TextVew
        for (int i = 0; i < notices.size(); i++) {
            final TextView textView = createTextView(notices.get(i), i);
            final int finalI = i;
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(finalI, textView);
                    }
                }
            });
            addView(textView);
        }
    }
    /**
     * 作者：zwq
     * 日期: 2016/10/14 14:18
     * 描述: 多个TextView
     */
    private void moreScroll(){
        //多个TextView
        List<LinearLayout> layoutList = createLinearLayout(notices);
        for (final LinearLayout ll  : layoutList) {
            final int position = (int) ll.getTag();
            ll.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(position, ll);
                    }
                }
            });
            addView(ll);
        }
    }

    // 创建ViewFlipper下的TextView
    private TextView createTextView(String text, int position) {
        TextView tv = new TextView(mContext);
        tv.setGravity(gravity);
        tv.setText(text);
        tv.setTextColor(textColor);
        tv.setSingleLine(true);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setTextSize(textSize);
        tv.setSingleLine(singleLine);
        tv.setTag(position);
        tv.setPadding(0,0,DisplayUtil.dip2px(getContext(),10),0);
        return tv;
    }

    // 创建ViewFlipper下的TextView
    private  List<LinearLayout> createLinearLayout(List<String> lists) {
        List<LinearLayout> llList = new ArrayList<>();
        LinearLayout linearLayout = null;
        //TODO 如果每次都展示两条 打开注释
//        if(lists.size() % 2 != 0){
//            lists.add(lists.size() -2,lists.get(lists.size() -2));
//        }
        for (int i = 0; i < lists.size(); i++) {
            if(i%2 == 0){
                List<String> subList = null;
                if(i+2 >= lists.size()){
                    subList = lists.subList(i, lists.size());
                }else {
                    subList = lists.subList(i, i+2);
                }
                linearLayout = new LinearLayout(getContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams lp =
                        new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                linearLayout.setLayoutParams(lp);
                for (String str : subList) {

                    SpannableStringBuilder builder = new SpannableStringBuilder(str);

                    RelativeLayout.LayoutParams tvlp =
                            new RelativeLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                    TextView tv = new TextView(mContext);
                    tv.setGravity(gravity);
                    tv.setSingleLine(true);
                    tv.setEllipsize(TextUtils.TruncateAt.END);
                    int start = str.length() -3;
                    int end = str.length();
                    builder.setSpan(new ForegroundColorSpan(Color.parseColor("#D0011B")),start,end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    tv.setText(builder);
                    tv.setTextColor(textColor);
                    tv.setTextSize(textSize);
                    tv.setSingleLine(singleLine);
                    tvlp.addRule(RelativeLayout.RIGHT_OF,R.id.lunboiv);
                    tvlp.addRule(RelativeLayout.CENTER_VERTICAL);
                    tv.setPadding(0,DisplayUtil.dip2px(getContext(),1),DisplayUtil.dip2px(getContext(),10),0);
                    tv.setLayoutParams(tvlp);

                    RelativeLayout innerll = new RelativeLayout(getContext());
                    RelativeLayout.LayoutParams innerlllp =
                            new RelativeLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                    innerlllp.addRule(RelativeLayout.CENTER_VERTICAL);
                   // innerlllp.height = DisplayUtil.dip2px(getContext(),CommonUtils.changeTextSize(getContext(),40));
                    innerll.setLayoutParams(innerlllp);

                    innerll.addView(tv);

                    linearLayout.setTag(i);
                    linearLayout.addView(innerll);
                }
                llList.add(linearLayout);
            }
        }
        Log.i("zwq","size -- >" + llList.size());
        return llList;
    }
    public int getPosition() {
        return (int) getCurrentView().getTag();
    }

    public List<String> getNotices() {
        return notices;
    }

    public void setNotices(List<String> notices) {
        this.notices = notices;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View textView);
    }

}
