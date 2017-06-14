package com.fullbloom.fbllibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fullbloom.fbllibrary.R;
import com.fullbloom.fbllibrary.util.DisplayUtil;


/**
 * 万能的textView
 * Created by allen on 2016/10/14.
 */

public class AutioRelativeLayout extends RelativeLayout {

    private Context mContext;
    private int defaultBgColor = 0xFFFFFFFF;//默认背景颜色
    private int defaultLineColor = 0xFFE8E8E8;//线的默认颜色
    //private int defaultLineColor = 0xFF535353;//文字默认颜色
    private int defaultLinePadding = 0;//线的左右边距

    private ImageView leftIconIV;//左边图标
    private ImageView rightIconIV;//右边图标
    private CheckBox rightCheckBox;//右边checkbox
    private Drawable rightCheckBoxBg;//checkBox的背景

    private TextView leftTV;//左边textView
    private TextView centerTV;//中间textView
    private TextView rightTV;//右边textView

    private TextView leftTopTV;//左上的textView
    private TextView leftBottomTV;//左下的textView
    private TextView leftBottomTV2;//左下第二个textView


    private Drawable leftIconRes;//左边图标资源
    private Drawable rightIconRes;//右边图标资源
    private int sBackgroundRes;//背景资源
    private String leftTextString;//左边显示的文字
    private String centerTextString;//中间显示的文字
    private String rightTextString;//右边显示的文字
    private String leftTopTextString;//左上显示的文字
    private String leftBottomTextString;//左下显示的文字
    private String leftBottomTextString2;//左下第二个显示的文字


    private int defaultPadding = 0;//默认边距

    private int topLineMargin;//上边线的左右边距
    private int bottomLineMargin,sLineLeftMargin,sLineRightMargin;//下边线的左右边距
    private int bothLineMargin;//两条线的左右边距

    private int leftIconMarginLeft;//左边图标的左边距

    private int leftTVMarginLeft;//左边文字的左边距

    private int leftTopMarginLeft;//左上文字的左边距
    private int leftBottomMarginLeft;//左下文字的左边距
    private int leftBottomMarginLeft2;//左下第二个文字的左边距

    private int rightTVMarginRight;//右边文字的右边距
    private int rightIconMarginRight;//右边图标的右边距
    private int rightCheckBoxMarginRight;//右边checkBox的右边距
    private boolean showCheckBox;//是否显示右边选择框
    private boolean isChecked;//是否默认选中

    private int defaultSize = 0,defaultWidthSize = 0;//默认字体大小

    private int leftTVSize;//左边文字字体大小
    private int leftTopTVSize;//左上文字字体大小
    private int leftBottomTVSize;//左下文字字体大小
    private int leftBottomTVSize2;//左下第二个文字字体大小
    private int rightTVSize;//右边文字字体大小
    private int centerTVSize;//中间文字字体大小


    private int defaultColor = 0xFF373737;//文字默认颜色

    private int backgroundColor;//背景颜色
    private int leftTVColor;//左边文字颜色
    private int leftTopTVColor;//左上文字颜色
    private int leftBottomTVColor;//左下文字颜色
    private int leftBottomTVColor2;//左下第二个文字颜色
    private int rightTVColor;//右边文字颜色
    private int centerTVColor;//中间文字颜色

    private static final int NONE = 0;
    private static final int TOP = 1;
    private static final int BOTTOM = 2;
    private static final int BOTH = 3;
    private static final int DEFAULT = BOTTOM;

    private boolean useRipple;
    private int sLeftIconSizeWidth,sRightIconSizeHeight,sRightIconSizeWidth,sLeftIconSizeHeight;

    private int lineType;
    private LayoutParams centerBaseLineParams, topLineParams, bottomLineParams, leftImgParams, leftTextParams, centerTextParams, leftTopTextParams, leftBottomParams,
            leftBottomParams2, rightTextParams, rightImgParams, rightCheckBoxParams;

    private OnSuperTextViewClickListener onSuperTextViewClickListener;


    public AutioRelativeLayout(Context context) {
        super(context);
    }

    public AutioRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        defaultLinePadding = dip2px(context, 16);
        defaultPadding = dip2px(context, 16);
        defaultSize = sp2px(context, 14);
        defaultWidthSize = sp2px(context, 0);
        getAttr(attrs);

        initLayout();

    }

    /**
     * 获取自定义属性值
     *
     * @param attrs
     */
    private void getAttr(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.AutioRelativeLayout);

        ////////设置文字或者图片资源////////
        leftIconRes = typedArray.getDrawable(R.styleable.AutioRelativeLayout_sLeftIconRes);
        rightIconRes = typedArray.getDrawable(R.styleable.AutioRelativeLayout_sRightIconRes);
        rightCheckBoxBg = typedArray.getDrawable(R.styleable.AutioRelativeLayout_sRightCheckBoxRes);

        leftTextString = typedArray.getString(R.styleable.AutioRelativeLayout_sLeftTextString);
        centerTextString = typedArray.getString(R.styleable.AutioRelativeLayout_sCenterTextString);
        rightTextString = typedArray.getString(R.styleable.AutioRelativeLayout_sRightTextString);

        leftTopTextString = typedArray.getString(R.styleable.AutioRelativeLayout_sLeftTopTextString);
        leftBottomTextString = typedArray.getString(R.styleable.AutioRelativeLayout_sLeftBottomTextString);
        leftBottomTextString2 = typedArray.getString(R.styleable.AutioRelativeLayout_sLeftBottomTextString2);

        showCheckBox = typedArray.getBoolean(R.styleable.AutioRelativeLayout_sRightCheckBoxShow, false);
        isChecked = typedArray.getBoolean(R.styleable.AutioRelativeLayout_sIsChecked, false);
        useRipple = typedArray.getBoolean(R.styleable.AutioRelativeLayout_sUseRipple, false);

        lineType = typedArray.getInt(R.styleable.AutioRelativeLayout_sLineShow, DEFAULT);

        /////////设置view的边距////////
        topLineMargin = typedArray.getDimensionPixelSize(R.styleable.AutioRelativeLayout_sTopLineMargin, defaultLinePadding);
        bottomLineMargin = typedArray.getDimensionPixelSize(R.styleable.AutioRelativeLayout_sBottomLineMargin, defaultLinePadding);
        bothLineMargin = typedArray.getDimensionPixelSize(R.styleable.AutioRelativeLayout_sBothLineMargin, defaultLinePadding);

        leftIconMarginLeft = typedArray.getDimensionPixelSize(R.styleable.AutioRelativeLayout_sLeftIconMarginLeft, defaultPadding);
        leftTVMarginLeft = typedArray.getDimensionPixelSize(R.styleable.AutioRelativeLayout_sLeftTextMarginLeft, defaultPadding);

        leftTopMarginLeft = typedArray.getDimensionPixelSize(R.styleable.AutioRelativeLayout_sLeftTopTextMarginLeft, defaultPadding);
        leftBottomMarginLeft = typedArray.getDimensionPixelSize(R.styleable.AutioRelativeLayout_sLeftBottomTextMarginLeft, defaultPadding);
        leftBottomMarginLeft2 = typedArray.getDimensionPixelSize(R.styleable.AutioRelativeLayout_sLeftBottomTextMarginLeft2, defaultPadding);
        rightTVMarginRight = typedArray.getDimensionPixelSize(R.styleable.AutioRelativeLayout_sRightTextMarginRight, defaultPadding);
        rightIconMarginRight = typedArray.getDimensionPixelSize(R.styleable.AutioRelativeLayout_sRightIconMarginRight, defaultPadding);
        rightCheckBoxMarginRight = typedArray.getDimensionPixelSize(R.styleable.AutioRelativeLayout_sRightCheckBoxMarginRight, defaultPadding);


        sLeftIconSizeWidth = typedArray.getDimensionPixelSize(R.styleable.AutioRelativeLayout_sLeftIconSizeWidth,defaultWidthSize);
        sLeftIconSizeHeight = typedArray.getDimensionPixelSize(R.styleable.AutioRelativeLayout_sLeftIconSizeHeight,defaultWidthSize);
        sRightIconSizeWidth = typedArray.getDimensionPixelSize(R.styleable.AutioRelativeLayout_sRightIconSizeWidth,defaultWidthSize);
        sRightIconSizeHeight = typedArray.getDimensionPixelSize(R.styleable.AutioRelativeLayout_sRightIconSizeHeight,defaultWidthSize);

        //////设置字体颜色////////
        backgroundColor = typedArray.getColor(R.styleable.AutioRelativeLayout_sBackgroundColor, defaultBgColor);
        sBackgroundRes = typedArray.getResourceId(R.styleable.AutioRelativeLayout_sBackgroundRes,R.drawable.selector_white_buttons);
        leftTVColor = typedArray.getColor(R.styleable.AutioRelativeLayout_sLeftTextColor, defaultColor);
        leftTopTVColor = typedArray.getColor(R.styleable.AutioRelativeLayout_sLeftTopTextColor, defaultColor);
        leftBottomTVColor = typedArray.getColor(R.styleable.AutioRelativeLayout_sLeftBottomTextColor, defaultColor);
        leftBottomTVColor2 = typedArray.getColor(R.styleable.AutioRelativeLayout_sLeftBottomTextColor2, defaultColor);
        rightTVColor = typedArray.getColor(R.styleable.AutioRelativeLayout_sRightTextColor, defaultColor);
        centerTVColor = typedArray.getColor(R.styleable.AutioRelativeLayout_sCenterTextColor, defaultColor);
        //////设置字体大小////////
        leftTVSize = typedArray.getDimensionPixelSize(R.styleable.AutioRelativeLayout_sLeftTextSize, defaultSize);
        leftTopTVSize = typedArray.getDimensionPixelSize(R.styleable.AutioRelativeLayout_sLeftTopTextSize, defaultSize);
        leftBottomTVSize = typedArray.getDimensionPixelSize(R.styleable.AutioRelativeLayout_sLeftBottomTextSize, defaultSize);
        leftBottomTVSize2 = typedArray.getDimensionPixelSize(R.styleable.AutioRelativeLayout_sLeftBottomTextSize2, defaultSize);
        rightTVSize = typedArray.getDimensionPixelSize(R.styleable.AutioRelativeLayout_sRightTextSize, defaultSize);
        centerTVSize = typedArray.getDimensionPixelSize(R.styleable.AutioRelativeLayout_sCenterTextSize, defaultSize);

        typedArray.recycle();
    }

    /**
     * 初始化布局
     */
    private void initLayout() {

        initSuperTextView();
        initCenterBaseLine();

        if (leftIconRes != null) {
            initLeftIcon();
        }
        if (leftTopTextString != null) {
            initLeftTopText();
        }
        if (leftBottomTextString != null) {
            initLeftBottomText();
        }
        if (leftBottomTextString2 != null) {
            initLeftBottomText2();
        }
        if (leftTextString != null) {
            initLeftText();
        }
        if (centerTextString != null) {
            initCenterText();
        }
        if (rightIconRes != null) {
            initRightIcon();
        }
        if (rightTextString != null) {
            initRightText();
        }
        if (showCheckBox) {
            initRightCheckBox();
        }

        switch (lineType) {
            case NONE:
                break;
            case TOP:
                initTopLine(topLineMargin);
                break;
            case BOTTOM:
                initBottomLine(bottomLineMargin);
                break;
            case BOTH:
                initTopLine(bothLineMargin);
                initBottomLine(bothLineMargin);
                break;
        }
    }


    /**
     * 初始化上边的线
     */
    private void initTopLine(int lineMargin) {
        View topLine = new View(mContext);
        topLineParams = new LayoutParams(LayoutParams.MATCH_PARENT, dip2px(mContext, 1));
        topLineParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, TRUE);
        topLineParams.setMargins(lineMargin, 0, lineMargin, 0);
        topLine.setLayoutParams(topLineParams);
        topLine.setBackgroundColor(defaultLineColor);
        addView(topLine);
    }

    /**
     * 初始化下边的线
     */
    private void initBottomLine(int lineMargin) {
        View bottomLine = new View(mContext);
        bottomLineParams = new LayoutParams(LayoutParams.MATCH_PARENT, dip2px(mContext, 1));
        bottomLineParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, TRUE);
        bottomLineParams.setMargins(lineMargin, 0, lineMargin, 0);
        bottomLine.setLayoutParams(bottomLineParams);
        bottomLine.setBackgroundColor(defaultLineColor);

        addView(bottomLine);
    }

    /**
     * 初始化SuperTextView
     */
    private void initSuperTextView() {

        this.setBackgroundColor(backgroundColor);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSuperTextViewClickListener != null) {
                    onSuperTextViewClickListener.onSuperTextViewClick(view);
                }
            }
        });

        if (useRipple) {
            this.setBackgroundResource(R.drawable.selector_white_buttons);
        }
    }


    /**
     * 为了设置上下两排文字居中对齐显示而需要设置的基准线
     */
    private void initCenterBaseLine() {
        View view = new View(mContext);
        centerBaseLineParams = new LayoutParams(LayoutParams.MATCH_PARENT, dip2px(mContext, 10));
        centerBaseLineParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        view.setId(R.id.sCenterBaseLineId);
        view.setLayoutParams(centerBaseLineParams);
        addView(view);
    }


    /**
     * 初始化左边图标
     */
    private void initLeftIcon() {
        leftIconIV = new ImageView(mContext);
        leftImgParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        leftImgParams.addRule(ALIGN_PARENT_LEFT, TRUE);
        leftImgParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        if(sLeftIconSizeWidth != 0){
            leftImgParams.width = sLeftIconSizeWidth;
        }
        if(sLeftIconSizeHeight != 0){
            leftImgParams.height = sLeftIconSizeHeight;
        }
        setMargin(leftImgParams, leftIconMarginLeft, 0, 0, 0);
        leftIconIV.setScaleType(ImageView.ScaleType.CENTER);
        leftIconIV.setId(R.id.sLeftIconId);
        leftIconIV.setLayoutParams(leftImgParams);
        leftIconIV.setImageDrawable(leftIconRes);
        leftIconIV.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(leftIconIV);
    }

    /**
     * 初始化右边图标
     */
    private void initRightIcon() {
        rightIconIV = new ImageView(mContext);

        rightImgParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if(sRightIconSizeWidth != 0){
            rightImgParams.width = sRightIconSizeWidth;
        }
        if(sRightIconSizeHeight != 0){
            rightImgParams.height = sRightIconSizeHeight;
        }
        rightImgParams.addRule(ALIGN_PARENT_RIGHT, TRUE);
        rightImgParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);

        setMargin(rightImgParams, 0, 0, rightIconMarginRight, 0);
        rightIconIV.setLayoutParams(rightImgParams);
        rightIconIV.setImageDrawable(rightIconRes);

        addView(rightIconIV);
    }
    /**
     * 初始化左边文字
     */
    private void initLeftText() {
        leftTV = new TextView(mContext);
        leftTextParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        leftTextParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        leftTextParams.addRule(RelativeLayout.RIGHT_OF, R.id.sLeftIconId);
        setMargin(leftTextParams, leftTVMarginLeft, 0, 0, 0);
        leftTV.setLayoutParams(leftTextParams);
        leftTV.setText(leftTextString);
        setTextColor(leftTV, leftTVColor);
        setTextSize(leftTV, leftTVSize);
        addView(leftTV);
    }

    /**
     * 初始化左上文字
     */
    private void initLeftTopText() {
        leftTopTV = new TextView(mContext);
        leftTopTextParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        leftTopTextParams.addRule(RelativeLayout.ABOVE, R.id.sCenterBaseLineId);
        leftTopTextParams.addRule(RelativeLayout.RIGHT_OF, R.id.sLeftIconId);
        setMargin(leftTopTextParams, leftTopMarginLeft, 0, 0, 0);
        leftTopTV.setLayoutParams(leftTopTextParams);
        leftTopTV.setText(leftTopTextString);
        setTextColor(leftTopTV, leftTopTVColor);
        setTextSize(leftTopTV, leftTopTVSize);
        leftTopTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSuperTextViewClickListener != null) {
                    onSuperTextViewClickListener.onLeftTopClick();
                }
            }
        });
        addView(leftTopTV);
    }

    /**
     * 初始化左下文字
     */
    private void initLeftBottomText() {
        leftBottomTV = new TextView(mContext);
        leftBottomParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        leftBottomParams.addRule(RelativeLayout.BELOW, R.id.sCenterBaseLineId);
        leftBottomParams.addRule(RelativeLayout.RIGHT_OF, R.id.sLeftIconId);
        setMargin(leftBottomParams, leftBottomMarginLeft, 0, 0, 0);
        leftBottomTV.setLayoutParams(leftBottomParams);
        leftBottomTV.setId(R.id.sLeftBottomTextId);
        leftBottomTV.setText(leftBottomTextString);
        setTextColor(leftBottomTV, leftBottomTVColor);
        setTextSize(leftBottomTV, leftBottomTVSize);
        leftBottomTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSuperTextViewClickListener != null) {
                    onSuperTextViewClickListener.onLeftBottomClick();
                }
            }
        });
        addView(leftBottomTV);
    }

    /**
     * 初始化左下第二个文字
     */
    private void initLeftBottomText2() {
        leftBottomTV2 = new TextView(mContext);
        leftBottomParams2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        leftBottomParams2.addRule(RelativeLayout.BELOW, R.id.sCenterBaseLineId);
        leftBottomParams2.addRule(RelativeLayout.RIGHT_OF, R.id.sLeftBottomTextId);
        setMargin(leftBottomParams2, leftBottomMarginLeft2, 0, 0, 0);
        leftBottomTV2.setLayoutParams(leftBottomParams2);
        leftBottomTV2.setText(leftBottomTextString2);
        setTextColor(leftBottomTV2, leftBottomTVColor2);
        setTextSize(leftBottomTV2, leftBottomTVSize2);
        leftBottomTV2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSuperTextViewClickListener != null) {
                    onSuperTextViewClickListener.onLeftBottomClick2();
                }
            }
        });
        addView(leftBottomTV2);
    }

    /**
     * 初始化中间文字
     */
    private void initCenterText() {
        centerTV = new TextView(mContext);
        centerTextParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        centerTextParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        centerTV.setLayoutParams(centerTextParams);
        centerTV.setText(centerTextString);
        setTextColor(centerTV, centerTVColor);
        setTextSize(centerTV, centerTVSize);
        addView(centerTV);
    }

    /**
     * 初始化右边文字
     */
    private void initRightText() {
        rightTV = new TextView(mContext);
        rightTextParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rightTextParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        rightTextParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
        setMargin(rightTextParams, DisplayUtil.dip2px(mContext,100), 0, rightTVMarginRight, 0);
        rightTV.setLayoutParams(rightTextParams);
        rightTV.setText(rightTextString);
        setTextColor(rightTV, rightTVColor);
        setTextSize(rightTV, rightTVSize);
        rightTV.setSingleLine();
        rightTV.setEllipsize(TextUtils.TruncateAt.END);
        addView(rightTV);
    }


    /**
     * 初始化右边选择框
     */
    private void initRightCheckBox() {
        rightCheckBox = new CheckBox(mContext);

        rightCheckBoxParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        rightCheckBoxParams.addRule(ALIGN_PARENT_RIGHT, TRUE);
        rightCheckBoxParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        setMargin(rightCheckBoxParams, 0, 0, rightCheckBoxMarginRight, 0);
        rightCheckBox.setLayoutParams(rightCheckBoxParams);
        if (rightCheckBoxBg != null) {
            rightCheckBox.setGravity(CENTER_IN_PARENT);
            rightCheckBox.setButtonDrawable(rightCheckBoxBg);
        }
        rightCheckBox.setChecked(isChecked);
        addView(rightCheckBox);
    }

    private void setMargin(LayoutParams params, int left, int top, int right, int bottom) {
        params.setMargins(left, top, right, bottom);
    }

    /**
     * 设置view的边距
     *
     * @param view   view对象
     * @param left   左边边距
     * @param top    上边边距
     * @param right  右边边距
     * @param bottom 下边边距
     */
    private void setPadding(View view, int left, int top, int right, int bottom) {
        view.setPadding(left, top, right, bottom);
    }

    /**
     * 设置文字的字体大小
     *
     * @param textView textView对象
     * @param size     文字大小
     */
    private void setTextSize(TextView textView, int size) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    /**
     * 设置文字的颜色
     *
     * @param textView textView对象
     * @param color    文字颜色
     */
    private void setTextColor(TextView textView, int color) {
        textView.setTextColor(color);
    }

    //////////对外公布的方法///////////////

    /**
     * 设置左边图标
     *
     * @param leftIcon 左边图标
     * @return 返回对象
     */
    public AutioRelativeLayout setLeftIcon(Drawable leftIcon) {
        leftIconRes = leftIcon;
        if (leftIconIV == null) {
            initLeftIcon();
        } else {
            leftIconIV.setImageDrawable(leftIcon);
        }
        return this;
    }

    /**
     * 设置右边图标
     *
     * @param rightIcon 右边图标
     * @return 返回对象
     */
    public AutioRelativeLayout setRightIcon(Drawable rightIcon) {
        rightIconRes = rightIcon;
        if (rightIconIV == null) {
            initRightIcon();
        } else {
            rightIconIV.setImageDrawable(rightIcon);
        }
        return this;
    }

    /**
     * 设置左边显示文字
     *
     * @param leftString 左边文字
     * @return 返回对象
     */
    public AutioRelativeLayout setLeftString(String leftString) {
        leftTextString = leftString;
        if (leftTV == null) {
            initLeftText();
        } else {
            leftTV.setText(leftString);
        }
        return this;
    }

    /**
     * 设置左上显示的文字
     *
     * @param leftTopString 左上文字
     * @return 返回对象
     */
    public AutioRelativeLayout setLeftTopString(String leftTopString) {
        leftTopTextString = leftTopString;
        if (leftTopTV == null) {
            initLeftTopText();
        } else {
            leftTopTV.setText(leftTopString);
        }
        return this;
    }

    /**
     * 设置左下显示的文字
     *
     * @param leftBottomString 左下第一个文字
     * @return 返回对象
     */
    public AutioRelativeLayout setLeftBottomString(String leftBottomString) {
        leftBottomTextString = leftBottomString;
        if (leftBottomTV == null) {
            initLeftBottomText();
        } else {
            leftBottomTV.setText(leftBottomString);
        }
        return this;
    }

    /**
     * 设置左下第二个显示的文字
     *
     * @param leftBottomString2 左下第二个文字
     * @return 返回对象
     */
    public AutioRelativeLayout setLeftBottomString2(String leftBottomString2) {
        leftBottomTextString2 = leftBottomString2;
        if (leftBottomTV2 == null) {
            initLeftBottomText2();
        } else {
            leftBottomTV2.setText(leftBottomString2);
        }
        return this;
    }

    /**
     * 设置右边显示的文字
     *
     * @param rightString 右边文字
     * @return 返回对象
     */
    public AutioRelativeLayout setRightString(String rightString) {
        rightTextString = rightString;
        if (rightTV == null) {
            initRightText();
        } else {
            rightTV.setText(rightString);
        }
        return this;
    }
    public String getRightTextString(){
        if(rightTV != null){
            return rightTV.getText().toString().trim();
        }
        return null;
    }

    /**
     * @param checked 是否选中
     * @return 返回值
     */
    public AutioRelativeLayout setCbChecked(boolean checked) {
        isChecked = checked;
        if (rightCheckBox == null) {
            initRightCheckBox();
        } else {
            rightCheckBox.setChecked(checked);
        }
        return this;
    }

    /**
     * 设置checkbox的背景图
     *
     * @param drawable drawable对象
     * @return 返回对象
     */
    public AutioRelativeLayout setCbBackground(Drawable drawable) {
        rightCheckBoxBg = drawable;
        if (rightCheckBox == null) {
            initRightCheckBox();
        } else {
            rightCheckBox.setBackgroundDrawable(drawable);
        }
        return this;
    }

    /**
     * 获取checkbox状态
     *
     * @return 返回选择框当前选中状态
     */
    public boolean getCbisChecked() {
        boolean isChecked = false;
        if (rightCheckBox != null) {
            isChecked = rightCheckBox.isChecked();
        }
        return isChecked;
    }

    /**
     * 设置左边文字的颜色
     *
     * @param textColor 文字颜色值
     * @return 返回对象
     */
    public AutioRelativeLayout setLeftTVColor(int textColor) {
        leftTVColor = textColor;
        if (leftTV == null) {
            initLeftText();
        } else {
            leftTV.setTextColor(textColor);
        }
        return this;
    }

    /**
     * 设置右边文字的颜色
     *
     * @param textColor 文字颜色值
     * @return 返回对象
     */
    public AutioRelativeLayout setRightTVColor(int textColor) {
        rightTVColor = textColor;
        if (rightTV == null) {
            initRightText();
        } else {
            rightTV.setTextColor(textColor);
        }
        return this;
    }

    /**
     * 设置左上边文字的颜色
     *
     * @param textColor 文字颜色值
     * @return 返回对象
     */
    public AutioRelativeLayout setLeftTopTVColor(int textColor) {
        leftTopTVColor = textColor;
        if (leftTopTV == null) {
            initLeftTopText();
        } else {
            leftTopTV.setTextColor(textColor);
        }
        return this;
    }

    /**
     * 设置左下边文字的颜色
     *
     * @param textColor 文字颜色值
     * @return 返回对象
     */
    public AutioRelativeLayout setLeftBottomTVColor(int textColor) {
        leftBottomTVColor = textColor;
        if (leftBottomTV == null) {
            initLeftBottomText();
        } else {
            leftBottomTV.setTextColor(textColor);
        }
        return this;
    }

    /**
     * 设置左下第二个文字的颜色
     *
     * @param textColor 文字颜色值
     * @return 返回对象
     */
    public AutioRelativeLayout setLeftBottomTVColor2(int textColor) {
        leftBottomTVColor2 = textColor;
        if (leftBottomTV2 == null) {
            initLeftBottomText2();
        } else {
            leftBottomTV2.setTextColor(textColor);
        }
        return this;
    }

    //////////设置View的点击事件/////////////////

    /**
     * 点击事件
     *
     * @param listener listener对象
     * @return 返回对象
     */
    public AutioRelativeLayout setOnSuperTextViewClickListener(OnSuperTextViewClickListener listener) {
        onSuperTextViewClickListener = listener;
        return this;
    }

    /**
     * 点击事件接口
     */
    public static class OnSuperTextViewClickListener {
        public void onSuperTextViewClick(View v) {
        }

        public void onLeftTopClick() {
        }

        public void onLeftBottomClick() {
        }

        public void onLeftBottomClick2() {
        }

    }

    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }
}
