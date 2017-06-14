package com.fullbloom.fbllibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fullbloom.fbllibrary.R;


/**
 * 作者：zwq
 * 日期: 2016/5/20 17:33
 * 描述: 通用dialog
 */
public class CommentDialog extends Dialog implements View.OnClickListener{
    private Context mContext;
    private TextView mTitle;
    private TextView mContent;
    private static TextView mCancle;
    private TextView mOk;
    private View mMy_dialog_linearlines;
    private LinearLayout out_ll;
    private Display display;

    public CommentDialog(Context context) {
        this(context, R.style.MyDialogStyle);
        mContext = context;
        initView();
    }

    public CommentDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        initView();
    }

    protected CommentDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
        initView();
    }

    private void initView() {
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        View view =  View.inflate(mContext, R.layout.my_dialog,null);
        setContentView(view);
        out_ll = (LinearLayout) view.findViewById(R.id.out_ll);
        // 调整dialog背景大小
        out_ll.setLayoutParams(new FrameLayout.LayoutParams((int) (display.getWidth() * 0.85), FrameLayout.LayoutParams.WRAP_CONTENT));
        mTitle = (TextView) view.findViewById(R.id.my_dialog_title);
        mContent = (TextView) view.findViewById(R.id.my_dialog_context);
        mCancle = (TextView) view.findViewById(R.id.my_dialog_cancle);
        mOk = (TextView) view.findViewById(R.id.my_dialog_ok);
        mMy_dialog_linearlines = view.findViewById(R.id.my_dialog_linearlines);
        mCancle.setOnClickListener(this);
        mOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.my_dialog_cancle) {
            dismiss();

        } else if (i == R.id.my_dialog_ok) {
            mOnClicks.onClicks(v);
            dismiss();

        }
    }
    public interface OnClicks{
       public void onClicks(View okView);
    }
    private OnClicks mOnClicks;
    public void setOnClicks(OnClicks clicks){
        if(clicks != null) mOnClicks = clicks;
    }
    /**
     * 作者：zwq
     * 日期: 2016/5/10 11:24
     * 描述: 设置顶部提示语
     */
    public void setTitle(String text){
        mTitle.setText(text);
    }
    /**
     * 作者：zwq
     * 日期: 2016/5/10 11:24
     * 描述: 设置提示 内容
     */
    public void setContent(String text){
        mContent.setText(text);
    }
    /**
     * 作者：zwq
     * 日期: 2016/5/10 11:25
     * 描述: 取消按钮文本设置
     */
    public void setCancle(String text){
        mCancle.setText(text);
    }
    /**
     * 作者：zwq
     * 日期: 2016/5/10 11:26
     * 描述: 取消按钮文本设置
     */
    public void setOkText(String text){
        mOk.setText(text);
    }
    /**
     * 作者：zwq
     * 日期: 2016/5/10 11:28
     * 描述: 设置竖线是否隐藏  显示
     */
    public void setLinearLiesVisible(boolean isVisible){
        if(isVisible){
            mMy_dialog_linearlines.setVisibility(View.VISIBLE);
        }else {
            mMy_dialog_linearlines.setVisibility(View.GONE);
        }
    }
    /**
     * 作者：zwq
     * 日期: 2016/5/10 11:33
     * 描述: 取消按钮显示隐藏
     */
    public void setCancleVisible(boolean isVisible){
        if(isVisible){
            mCancle.setVisibility(View.VISIBLE);
            mMy_dialog_linearlines.setVisibility(View.VISIBLE);
        }else {
            mCancle.setVisibility(View.GONE);
            mMy_dialog_linearlines.setVisibility(View.GONE);
        }
    }
    /**
     * 作者：zwq
     * 日期: 2016/5/10 11:34
     * 描述: 顶部提示 文本 显示隐藏
     */
    public void setTitleVisible(boolean isVisible){
        if(isVisible){
            mTitle.setVisibility(View.VISIBLE);
        }else {
            mTitle.setVisibility(View.GONE);
        }
    }
    /**
     * 作者：zwq
     * 日期: 2016/5/10 11:37
     * 描述: 顶部标题文字颜色
     * textSize = 0 时时默认值
     */
    public void setTitleColor(String textColor, int textSize){
        mTitle.setTextColor(Color.parseColor(textColor));
        if( 0 != textSize){
            mTitle.setTextSize(textSize);
        }
    }
    /**
     * 作者：zwq
     * 日期: 2016/5/10 11:37
     * 描述: 内容 文字颜色
     * textSize = 0 时时默认值
     */
    public void setContentColor(String textColor, int textSize){
        mContent.setTextColor(Color.parseColor(textColor));
        if( 0 != textSize){
            mContent.setTextSize(textSize);
        }
    }
    /**
     * 作者：zwq
     * 日期: 2016/5/10 11:37
     * 描述: 取消 文字颜色
     * textSize = 0 时时默认值
     */
    public void setCancleColor(String textColor, int textSize){
        mCancle.setTextColor(Color.parseColor(textColor));
        if( 0 != textSize){
            mCancle.setTextSize(textSize);
        }
    }
    /**
     * 作者：zwq
     * 日期: 2016/5/10 11:37
     * 描述: 确定 文字颜色
     * textSize = 0 时时默认值
     */
    public void setOkColor(String textColor, int textSize){
        mOk.setTextColor(Color.parseColor(textColor));
        if( 0 != textSize){
            mOk.setTextSize(textSize);
        }
    }
    public static TextView getCancle(){
        return mCancle;
    }
    public static class Builder{

        CommentDialog commentDialog = null;
        public Builder(Context mContext){
            commentDialog = new CommentDialog(mContext);
        }
        public Builder setTitle(String txt){
            commentDialog.setTitle(txt);
            return this;
        }
        public Builder setContent(String txt){
            commentDialog.setContent(txt);
            return this;
        }
        public Builder setOkText(String txt){
            commentDialog.setOkText(txt);
            return this;
        }
        public Builder setCancle(String txt){
            commentDialog.setCancle(txt);
            return this;
        }
        /**
         * 作者：zwq
         * 时间: 2017/3/14 9:50
         * 描述: textSize 默认传0
         */
        public Builder setCancleColor(String textColor, int textSize){
            commentDialog.setCancleColor(textColor,textSize);
            return this;
        }
        public Builder setCancleColor(String textColor){
            commentDialog.setCancleColor(textColor,0);
            return this;
        }
        public Builder setOkColor(String textColor, int textSize){
            commentDialog.setOkColor(textColor,textSize);
            return this;
        }
        public Builder setOkColor(String textColor){
            commentDialog.setOkColor(textColor,0);
            return this;
        }
        public Builder setCancleVisible(boolean isVisible){
            commentDialog.setCancleVisible(isVisible);
            return this;
        }
        public Builder setTitleVisible(boolean isVisible){
            commentDialog.setTitleVisible(isVisible);
            return this;
        }
        public Builder setOkClicks(OnClicks clicks){
            commentDialog.setOnClicks(clicks);
            return this;
        }
        public Builder setCancleClicks(View.OnClickListener listener){
            getCancle().setOnClickListener(listener);
            return this;
        }
        public CommentDialog build(){
            return commentDialog;
        }
        public CommentDialog show(){
            commentDialog.show();
            return commentDialog;
        }
    }
}
