package com.fullbloom.fbllibrary.view;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * 导航条
 * @author Bruce.Wang
 */
public class BottomBar extends LinearLayout implements OnClickListener {

	private ArrayList<BottomBarItem> items = new ArrayList<BottomBarItem>();
	private OnCheckedChangeListener mListener;
	private int defaultTextColor;
	private int checkedTextColor;
	
	public BottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}


	public BottomBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}


	private void init() {
		setOrientation(LinearLayout.HORIZONTAL);
	}
	
	/**
	 * 设置文本颜色
	 */
	public void setTextColor(int defaultTextColorRes,int checkedTextColorRes){
		this.defaultTextColor = getResources().getColor(defaultTextColorRes);
		this.checkedTextColor = getResources().getColor(checkedTextColorRes);
	}
	
	/**
	 * 添加条目
	 * @param text
	 * @param defaultIcon
	 * @param checkedIcon
	 * @return
	 */
	public BottomBarItem addItem(String text, int defaultIcon, int checkedIcon){
		BottomBarItem item = new BottomBarItem(getContext());
		item.setText(text);
		item.setRes(defaultTextColor, checkedTextColor, defaultIcon, checkedIcon);
		LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
		params.weight = 1;
		item.setLayoutParams(params);
		addView(item);
		//设置ID为此item的Index
		item.setId(items.size());
		item.setOnClickListener(this);
		items.add(item);
		return item;
	}
	public BottomBarItem addItem(@StringRes int text, int defaultIcon, int checkedIcon){
		BottomBarItem item = new BottomBarItem(getContext());
		item.setText(text);
		item.setRes(defaultTextColor, checkedTextColor, defaultIcon, checkedIcon);
		LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
		params.weight = 1;
		item.setLayoutParams(params);
		addView(item);
		//设置ID为此item的Index
		item.setId(items.size());
		item.setOnClickListener(this);
		items.add(item);
		return item;
	}
	
	public void setOnCheckedChangeListener(OnCheckedChangeListener listener){
		this.mListener = listener;
	}


	/**
	 * 设置当前选中条目
	 * @param index
	 */
	public void setCurrentItem(int index){
		if(index>=items.size()){
			return;
		}
		initItemState();
		items.get(index).setChecked(true);
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id>=items.size()){
			return;
		}
		setCurrentItem(id);
		if(mListener!=null){
			mListener.onChange(id);
		}
	}
	
	private void initItemState(){
		for(BottomBarItem item :items){
			item.setChecked(false);
		}
	}
	
	public interface OnCheckedChangeListener{
		void onChange(int index);
	}

}
