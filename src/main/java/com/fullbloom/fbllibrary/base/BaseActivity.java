package com.fullbloom.fbllibrary.base;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fullbloom.fbllibrary.FBLLibrary;
import com.fullbloom.fbllibrary.R;
import com.fullbloom.fbllibrary.network.BaseRequestContext;
import com.fullbloom.fbllibrary.network.DelRequest;
import com.fullbloom.fbllibrary.network.GetRequest;
import com.fullbloom.fbllibrary.network.PostRequest;
import com.fullbloom.fbllibrary.receive.NetWorkListenerRecever;
import com.fullbloom.fbllibrary.util.AppManager;
import com.fullbloom.fbllibrary.util.CommonUtils;
import com.fullbloom.fbllibrary.util.NToast;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;


/** 描述:    Activity继承本基类主要重写 5 个方法
*         initView();                          ---> 初始化View
*         initData(),                          ---> 获取数据
*         getLayout();                         ---> 获取布局填充Activity
*         OnClicks();                          ---> 按钮点击事件
*
*         onSuccess2Object(key,object)         ---> object fastjson解析返回对象
*
*         // ---------------------  一般方法介绍   -----------
*         EventBus  activity --> activity
*         			 activity --> fragment
*         			 					传递数据
*
*         getMyHttpClient() --> 获取网络请求对象实例
 */
@SuppressWarnings("unused")
public abstract class BaseActivity extends FragmentActivity implements OnClickListener{

	public Context mContext = null;

	public int pageNum=1;
	public Activity activity;

	public ImageView iv_top_left_back = null;
	public TextView tv_top_left_back = null;
	public TextView tv_top_center = null;
	public ImageView iv_top_right = null;
	public TextView tv_top_right = null;
	public int pageSize = 15;
	public Bundle savedInstanceState;
	private NetWorkListenerRecever netWorkListenerRecever;
	private LinearLayout ll_content;
	protected LinearLayout ll_comment_title;
	private View network_connection_ll;
	protected ArrayList<String> datas;

	private SparseArray<View> mViews = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.toptitle);
		this.savedInstanceState = savedInstanceState;

		datas = new ArrayList<>();
		datas.add("dddddd");
		datas.add("dddddd");
		datas.add("dddddd");
		datas.add("dddddd");
		datas.add("dddddd");
		datas.add("dddddd");
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
			//结束你的activity
			finish();
			return;
		}
		activity = this;
		mViews = new SparseArray<>();
		mContext = FBLLibrary.mContext;
		ll_content = (LinearLayout) findViewById(R.id.ll_content);
		ll_comment_title = (LinearLayout) findViewById(R.id.ll_comment_title);
		network_connection_ll = findViewById(R.id.network_connection_ll);
		if( 0 != getLayout() ){
			View view = View.inflate(this, getLayout(), null);
			ll_content.addView(view);
			LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
			lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
			lp.height = LinearLayout.LayoutParams.MATCH_PARENT;
			view.setLayoutParams(lp);
		}
		AppManager.getAppManager().addActivity(this);
		getMyHttpClient();
		// 注册对象
		EventBus.getDefault().register(this);
		initTitle();
		initView(savedInstanceState);
		initData();
		registerNetWork();
		if(!CommonUtils.isNetWorkConnecteds(mContext)){
			NToast.show(mContext, "网络连接异常，请查看");
			network_connection_ll.setVisibility(View.VISIBLE);
			return;
		}

	}
	/**
	 * 设置内容区域背景颜色
	 * @author zwq
	 */
	public void setContentBackGround(String color){
		ll_content.setBackgroundColor(Color.parseColor(color));
	}
	/**
	 * 设置通用顶部布局显示隐藏
	 *               默认 显示
	 * isVisible     true 显示 ， false 隐藏
	 * @author zwq
	 */
	public void setCommentTopVisible(boolean isVisible){
		if(isVisible){
			ll_comment_title.setVisibility(View.VISIBLE);
		}else {
			ll_comment_title.setVisibility(View.GONE);
		}
	}
	/**
	 * 设置顶部中间标题
	 * @author zwq
	 */
	public void setTitle(String title){
		tv_top_center.setText(title);
	}
	/**
	 * 初始化顶部标题栏
	 */
	private void initTitle() {
		iv_top_left_back = findView(R.id.iv_top_left_back);
		tv_top_left_back = findView(R.id.tv_top_left_back);
		tv_top_center = findView(R.id.tv_top_center);
		iv_top_right = findView(R.id.iv_top_right);
		tv_top_right = findView(R.id.tv_top_right);
		network_connection_ll = (LinearLayout) findViewById(R.id.network_connection_ll);
		iv_top_left_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		network_connection_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onDestroy() {

        // 注销！！！！
        EventBus.getDefault().unregister(this);
        mContext = null;
		super.onDestroy();
		unRegisterNetWork();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		if(!CommonUtils.isNetWorkConnecteds(mContext)){
			NToast.show(mContext, "网络异常");
			return;
		}
		onClicks(v);
	}

	/**
	 * @param text
	 * 吐司
	 */
	public void showToast(String text){
		Toast.makeText(this,text, Toast.LENGTH_SHORT).show();
	}
	/**
	 * 判断网络
	 *
	 * @return false 无网络，true 有网
	 */
	public boolean isNetwork() {
		if (!CommonUtils.isNetWorkConnecteds(mContext)) {
			return false;
		}
		return true;
	}
	/**
	 * 初始化控件
	 */
	public abstract void initView(Bundle savedInstanceState);
	/**
	 * 初始化数据
	 */
	public abstract void initData();
	/**
	 * 初始化布局
	 */
	public abstract int getLayout();
	/**
	 * @author zwq    初始化点击事件
	 */
	public abstract void onClicks(View v);

	public <E extends View> E findView(int resourseId){
		E view = (E)mViews.get(resourseId);
		if(view == null){
			view = (E) findViewById(resourseId);
			mViews.put(resourseId,view);
		}
		return view;
	}
	public <E extends View> E findViewClick(int resourseId){
		E view = (E)mViews.get(resourseId);
		if(view == null){
			view = (E) findViewById(resourseId);
			mViews.put(resourseId,view);
		}
		view.setOnClickListener(this);
		return view;
	}
	/**
	 * 跳转界面
	 * @param clazz
	 */
	@SuppressWarnings("rawtypes")
	public void startActivity(Class clazz){
		if(clazz == null){
			return;
		}
		Intent intent = new Intent(mContext,clazz);
		startActivity(intent);
	}
	/**
	 * 跳转界面
	 * @param clazz
	 */
	@SuppressWarnings("rawtypes")
	public void startActivity(Class clazz, Activity activity){
		if(clazz == null){
			return;
		}
		if(activity == null){
			return;
		}
		Intent intent = new Intent(mContext,clazz);
		startActivity(intent);
		activity.finish();
	}

	public boolean isSuccess(int code){
		if(200 == code){
			return true;
		}else {
			return false;
		}
	}

	/**
	 * 获取网络实例
	 */
	private void getMyHttpClient() {
	}

	private void registerNetWork(){
		IntentFilter intentFilter = new IntentFilter();
		  intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		  netWorkListenerRecever = new NetWorkListenerRecever();
		  registerReceiver(netWorkListenerRecever, intentFilter);
	}
	private void unRegisterNetWork(){
		if(netWorkListenerRecever != null){
			unregisterReceiver(netWorkListenerRecever);
		}
	}
	@Subscriber(tag = "isConnectionNetWoek")
	private void isConnectionNetWoek(Boolean isConn){
		if(isConn){
			network_connection_ll.setVisibility(View.GONE);
		}else{
			network_connection_ll.setVisibility(View.VISIBLE);
		}
	}
	public void setRight(@DrawableRes int resId,OnClickListener listener){
		iv_top_right.setImageResource(resId);
		iv_top_right.setVisibility(View.VISIBLE);
		if(listener != null){
			iv_top_right.setOnClickListener(listener);
		}
	}
	public void setRight(@DrawableRes int resId){
		setRight(resId,null);
	}

	public TextView setRight(String rightText,OnClickListener listener){
		tv_top_right.setText(rightText);
		tv_top_right.setVisibility(View.VISIBLE);
		if(listener != null){
			tv_top_right.setOnClickListener(listener);
		}
		return tv_top_right;
	}
	public TextView setRight(String rightText){
		return setRight(rightText,null);
	}
	public void isAct(Intent intent,Context context){
		if(!(context instanceof Activity)){
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
	}


	/**    点击外部隐藏软键盘 start      **/
	//region软键盘的处理

	/**
	 * 清除editText的焦点
	 *
	 * @param v   焦点所在View
	 * @param ids 输入框
	 */
	public void clearViewFocus(View v, int... ids) {
		if (null != v && null != ids && ids.length > 0) {
			for (int id : ids) {
				if (v.getId() == id) {
					v.clearFocus();
					break;
				}
			}
		}


	}

	/**
	 * 隐藏键盘
	 *
	 * @param v   焦点所在View
	 * @param ids 输入框
	 * @return true代表焦点在edit上
	 */
	public boolean isFocusEditText(View v, int... ids) {
		if (v instanceof EditText) {
			EditText tmp_et = (EditText) v;
			for (int id : ids) {
				if (tmp_et.getId() == id) {
					return true;
				}
			}
		}
		return false;
	}

	//是否触摸在指定view上面,对某个控件过滤
	public boolean isTouchView(View[] views, MotionEvent ev) {
		if (views == null || views.length == 0) return false;
		int[] location = new int[2];
		for (View view : views) {
			view.getLocationOnScreen(location);
			int x = location[0];
			int y = location[1];
			if (ev.getX() > x && ev.getX() < (x + view.getWidth())
					&& ev.getY() > y && ev.getY() < (y + view.getHeight())) {
				return true;
			}
		}
		return false;
	}
	//endregion

	//region 右滑返回上级


	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			if (isTouchView(filterViewByIds(), ev)) return super.dispatchTouchEvent(ev);
			if (hideSoftByEditViewIds() == null || hideSoftByEditViewIds().length == 0)
				return super.dispatchTouchEvent(ev);
			View v = getCurrentFocus();
			if (isFocusEditText(v, hideSoftByEditViewIds())) {
				//隐藏键盘
				CommonUtils.hideKeyboard(this);
				//clearViewFocus(v, hideSoftByEditViewIds());
			}
		}
		return super.dispatchTouchEvent(ev);

	}

	/**
	 * 传入EditText的Id
	 * 没有传入的EditText不做处理
	 *
	 * @return id 数组
	 */
	public int[] hideSoftByEditViewIds() {
		return null;
	}

	/**
	 * 传入要过滤的View
	 * 过滤之后点击将不会有隐藏软键盘的操作
	 *
	 * @return id 数组
	 */
	public View[] filterViewByIds() {
		return null;
	}

	/**    点击外部隐藏软键盘 end      **/



	/**
	 *  1、获取main在窗体的可视区域
	 *  2、获取main在窗体的不可视区域高度
	 *  3、判断不可视区域高度
	 *      1、大于100：键盘显示  获取Scroll的窗体坐标
	 *                           算出main需要滚动的高度，使scroll显示。
	 *      2、小于100：键盘隐藏
	 *
	 * @param main 根布局
	 * @param scroll 需要显示的最下方View
	 */
	public void addLayoutListener(final View main, final View scroll) {
		main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				Rect rect = new Rect();
				main.getWindowVisibleDisplayFrame(rect);
				int mainInvisibleHeight = main.getRootView().getHeight() - rect.bottom;
				if (mainInvisibleHeight > 100) {
					int[] location = new int[2];
					scroll.getLocationInWindow(location);
					int srollHeight = (location[1] + scroll.getHeight()) - rect.bottom;
					main.scrollTo(0, srollHeight);
				} else {
					main.scrollTo(0, 0);
				}
			}
		});
	}


}
