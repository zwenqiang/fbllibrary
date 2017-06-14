package com.fullbloom.fbllibrary.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

import org.simple.eventbus.EventBus;


public class NetWorkListenerRecever extends BroadcastReceiver {

	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		//获得网络连接服务
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState(); // 获取网络连接状态
		NetworkInfo mobNetInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	    NetworkInfo wifiNetInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

	    if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
	     // unconnect network
	    	EventBus.getDefault().post(Boolean.valueOf(false), "isConnectionNetWoek");
	     }else {
	    // connect network
	    	 EventBus.getDefault().post(Boolean.valueOf(true), "isConnectionNetWoek");
	    	 
	     }
	}
}
