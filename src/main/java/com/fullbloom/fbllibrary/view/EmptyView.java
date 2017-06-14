package com.fullbloom.fbllibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.fullbloom.fbllibrary.R;


/**
 * listview 无数据时展示空白页面 带 刷新
 * 作者：zwq on 2016/9/14 10:54
 */
public class EmptyView extends RelativeLayout {
    private OnRefreshUI onRefreshUI;
    public EmptyView(Context context) {
        this(context,null);
    }
    public EmptyView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutParams lp = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(lp);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(View.inflate(context, R.layout.empty_deal_data,null),lp);
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        int cCount = getChildCount();
        if(cCount > 0){
            getChildAt(0).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onRefreshUI != null){
                        onRefreshUI.refreshUI();
                    }
                }
            });
        }
    }
    public void addCustumView(View view){
        removeAllViews();
        LayoutParams lp = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(view,lp);
    }
    /**
     * 作者：zwq
     * 日期: 2016/9/14 11:02
     * 描述: (中间布局)点击刷新当前页面回调
     */
    public void setOnRefreshUI(OnRefreshUI onRefreshUI){
        this.onRefreshUI = onRefreshUI;
    }
    public interface OnRefreshUI{
        void refreshUI();
    }
}
