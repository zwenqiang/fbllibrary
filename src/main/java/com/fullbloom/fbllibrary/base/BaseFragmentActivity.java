package com.fullbloom.fbllibrary.base;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fullbloom.fbllibrary.R;
import com.fullbloom.fbllibrary.receive.NetWorkListenerRecever;
import com.fullbloom.fbllibrary.util.CommonUtils;
import com.fullbloom.fbllibrary.util.NToast;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;


/**
 * 作者：zwq
 * 日期: 2016/9/5 11:02
 * 描述: fragment使用 懒加载方式 获取数据
 *			setUserVisibleHint(boolean isVisibleToUser)判断 当前fragment是否可见，isVisibleToUser true可见，false 不可见
 *
 *			fragment继承本基类主要重写 5 个方法
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
 *		   isFirst	 需要在子类中 lazyload方法中 手动置为 false；
 *		   子类在 lazyload 方法中请求网络数据 ， 并且在 item = 0;的fragment中initData()手动调用 lazyload();
 */
@SuppressWarnings("unused")
public abstract class BaseFragmentActivity extends Fragment implements OnClickListener{
	
	
	public ImageView iv_top_left_back = null;
	public TextView tv_top_left_back = null;
	public TextView tv_top_center = null;
	public ImageView iv_top_right = null;
	public TextView tv_top_right = null;
	public Activity mContext = null;
	private LinearLayout ll_content;
	private LinearLayout ll_comment_title;
	private View network_connection_ll;
	protected int pageSize=15;
	protected int pageNo=1;
	public ArrayList<String> datas;
	private NetWorkListenerRecever netWorkListenerRecever;
	private SparseArray<View> mViews = null;
	protected boolean isVisible = true;
	// 标志位，标志已经初始化完成。
	private boolean isPrepared;

	private boolean isFirst = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.toptitle, container, false);


		datas = new ArrayList<>();
		datas.add("dddddd");
		datas.add("dddddd");
		datas.add("dddddd");
		datas.add("dddddd");
		datas.add("dddddd");
		datas.add("dddddd");


		mViews = new SparseArray<>();
		ll_content = (LinearLayout) view.findViewById(R.id.ll_content);
		ll_comment_title = (LinearLayout) view.findViewById(R.id.ll_comment_title);
		network_connection_ll = view.findViewById(R.id.network_connection_ll);

		if(0 != getLayout()){
			View view2 = inflater.inflate(getLayout(), container, false);
			ll_content.addView(view2);
			return view;
		}else {
			return super.onCreateView(inflater, container, savedInstanceState);
		}
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mContext = getActivity();
		// 注册对象
		EventBus.getDefault().register(this);
		initTitle();
		isPrepared = true;
		initView(savedInstanceState);
		initData();
		registerNetWork();
		if(!CommonUtils.isNetWorkConnecteds(mContext)){
			NToast.show(mContext, "网络连接异常，请查看");
			network_connection_ll.setVisibility(View.VISIBLE);
			return;
		} 
	}

	public boolean isSuccess(String isSuccess){
		if("success".equals(isSuccess)){
			return true;
		}else {
			return false;
		}

	}
	public boolean isSuccess(int code){
		if(200 == code){
			return true;
		}else {
			return false;
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
	 * @param text
	 * 吐司
	 */
	public void showToast(String text){
		Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
        // 注销！！！！
        EventBus.getDefault().unregister(this);
        unRegisterNetWork();
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
		network_connection_ll = (LinearLayout) getActivity().findViewById(R.id.network_connection_ll);
		network_connection_ll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
			    startActivity(intent);
			}
		});
	}

	/**
	 * 初始化View
	 * @param savedInstanceState
     */
	public abstract void initView(Bundle savedInstanceState);
	/**
	 * 初始化数据
	 */
	public abstract void initData();
	/**
	 * 加载布局
	 */
	public abstract int getLayout();
	/**
	 * 点击事件
	 */
	public abstract void onClicks(View v);

	public void startActivity(Class clazz){
		if(clazz != null){
			Intent intent = new Intent(mContext, clazz);
			startActivity(intent);
		}
	}

	public <E extends View> E findView(int resourseId){
		E view = (E)mViews.get(resourseId);
		if(view == null){
			view = (E) getView().findViewById(resourseId);
			mViews.put(resourseId,view);
		}
		return view;
	}
	public <E extends View> E findViewClick(int resourseId){
		E view = (E)mViews.get(resourseId);
		if(view == null){
			view = (E) getView().findViewById(resourseId);
			mViews.put(resourseId,view);
		}
		view.setOnClickListener(this);
		return view;
	}

	/**
	 * 网络请求
	 * @author zwq
	 */
	private void registerNetWork(){

		IntentFilter intentFilter = new IntentFilter();
		  intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		  netWorkListenerRecever = new NetWorkListenerRecever();
		  getActivity().registerReceiver(netWorkListenerRecever, intentFilter);
	}
	private void unRegisterNetWork(){
		if(netWorkListenerRecever != null){
			getActivity().unregisterReceiver(netWorkListenerRecever);
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

	@Override
	public void onClick(View v) {
			if(!CommonUtils.isNetWorkConnecteds(mContext)){
				NToast.show(mContext, "网络异常");
				return;
			}
		onClicks(v);
	}


	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (!hidden) {
			onVisible();
		} else {
			onInVisible();
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			isVisible = true;
			onVisible();
		} else {
			isVisible = false;
			onInVisible();
		}
	}

	/**
	 * 当界面可见时的操作
	 */
	protected void onVisible() {
			lazyLoad();
	}

	/**
	 * 数据懒加载
	 */
	protected void lazyLoad() {
	}

	/**
	 * 当界面不可见时的操作
	 */
	protected void onInVisible() {

	}


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
