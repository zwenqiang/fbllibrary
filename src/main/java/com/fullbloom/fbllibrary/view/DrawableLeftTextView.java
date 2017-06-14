package com.fullbloom.fbllibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.TextView;

import com.fullbloom.fbllibrary.R;


/**
 * Created by zwq on 2017/4/19.
 */

public class DrawableLeftTextView extends TextView {

    private int lW;
    private int lH;
    private int rH;
    private int rW;

    public DrawableLeftTextView(Context context) {
        this(context,null);
    }

    public DrawableLeftTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DrawableLeftTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context,attrs);
        setParam();
    }

    private void setParam() {
        Drawable[] compoundDrawables = getCompoundDrawables();
        Drawable lD = compoundDrawables[0];
        Drawable rd = compoundDrawables[2];
        if(lD != null){
            lD.setBounds(0, 0, lW, lH);
        }
        if(rd != null){
            rd.setBounds(0, 0, rW, rH);
        }
        /// 这一步必须要做,否则不会显示.
        setCompoundDrawables(lD,null,rd,null);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DrawableLeftTextView);
        lW = (int) typedArray.getDimension(R.styleable.DrawableLeftTextView_leftWidth, 15);
        lH = (int) typedArray.getDimension(R.styleable.DrawableLeftTextView_leftHeight, 15);
        rH = (int) typedArray.getDimension(R.styleable.DrawableLeftTextView_rightHeight, 15);
        rW = (int) typedArray.getDimension(R.styleable.DrawableLeftTextView_rightWidth, 15);
        typedArray.recycle();
    }
    public void setDrawableLeft(@DrawableRes int res){
        Drawable drawable = getContext().getResources().getDrawable(res);
        drawable.setBounds(0, 0, lW, lH);
        setCompoundDrawables(drawable,null,null,null);
    }
    public void setDrawableRight(@DrawableRes int res){
        Drawable drawable = getContext().getResources().getDrawable(res);
        drawable.setBounds(0, 0, lW, lH);
        setCompoundDrawables(null,null,drawable,null);
    }
}
