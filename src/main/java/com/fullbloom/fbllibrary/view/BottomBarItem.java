package com.fullbloom.fbllibrary.view;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fullbloom.fbllibrary.R;


/**
 * 导航条条目
 * @author zwq
 */
public class BottomBarItem extends LinearLayout {

	private ImageView iv_icon;
	private ImageView iv_hint;
	private TextView tv_name;
	
	private boolean mChecked = false;
	private int defaultTextColor;
	private int checkedTextColor;
	private int defaultImageRes;
	private int checkedImageRes;
	private TextView tv_hint;

	public BottomBarItem(Context context) {
		super(context);
		init();
	}

	private void init() {
		View.inflate(getContext(), R.layout.view_bottom_bar_item, this);
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		iv_hint = (ImageView) findViewById(R.id.iv_hint);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_hint = (TextView) findViewById(R.id.tv_hint);
		hideHint();
	}
	
	public void setIcon(int resId){
		iv_icon.setImageResource(resId);
	}
	
	public void setText(String text){
		tv_name.setText(text);
	}public void setText(@StringRes int text){
		tv_name.setText(text);
	}
	
	public void setTextColor(int color){
		tv_name.setTextColor(color);
	}
	
	/**
	 * 显示提示数量
	 * @param num
	 */
	public void setHintNum(int num){
		String hint = String.valueOf(num);
		if(num<=0){
			tv_hint.setVisibility(View.INVISIBLE);
			return;
		}else if(num>99){
			hint = "99+";
		}
		tv_hint.setVisibility(View.VISIBLE);
		tv_hint.setText(hint);
	}
	
	/**
	 * 显示红点
	 */
	public void showHintPoint(){
		iv_hint.setVisibility(View.VISIBLE);
	}
	
	public void hideHint(){
		tv_hint.setVisibility(View.INVISIBLE);
		iv_hint.setVisibility(View.INVISIBLE);
	}
	
	/**
	 * 设置资源
	 * @param defaultTextColor
	 * @param checkedTextColor
	 * @param defaultImageRes
	 * @param checkedImageRes
	 */
	public void setRes(int defaultTextColor, int checkedTextColor,
			int defaultImageRes, int checkedImageRes) {
		this.defaultTextColor = defaultTextColor;
		this.checkedTextColor = checkedTextColor;
		this.defaultImageRes = defaultImageRes;
		this.checkedImageRes = checkedImageRes;
		freshView();
	}
	
	private void freshView(){
		if(mChecked){
			setTextColor(checkedTextColor);
			setIcon(checkedImageRes);
		}else{
			setTextColor(defaultTextColor);
			setIcon(defaultImageRes);
		}
	}
	
	/**
	 * 设置是否选中
	 * @param check
	 */
	public void setChecked(boolean check){
		mChecked = check;
		freshView();
	}

}
