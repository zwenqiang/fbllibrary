package com.fullbloom.fbllibrary.network;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.fullbloom.fbllibrary.FBLLibrary;
import com.fullbloom.fbllibrary.util.NLoger;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;


/**
 * okhttp控制类
 * @author zwq
 * @date 2016-1-14
 */
@SuppressWarnings("unused")
public class OkHttpClientManager {

	public static final int ERROR = 444444;
	public static final int SUCCESS = 666666;
	private static final String TAG = "Request";
	private HashMap<String,Call> allRequests = null;

	public OkHttpClient mOkHttpClient = null;
	private OkHttpHandler mOkHttpHandler = null;
	private OkHttpClientManager(){
		mOkHttpClient = new OkHttpClient();
		mOkHttpHandler = new OkHttpHandler();
		allRequests = new HashMap<>();
		OkHttpClient.Builder builder = mOkHttpClient.newBuilder();
		builder.connectTimeout(60000, TimeUnit.MILLISECONDS);
		builder.readTimeout(60000, TimeUnit.MILLISECONDS);
		builder.writeTimeout(60000, TimeUnit.MILLISECONDS);
	};
	private static OkHttpClientManager instance = null;
	public static OkHttpClientManager getInstance(){
		if(instance == null){
			synchronized (OkHttpClientManager.class) {
				if(instance == null){
					instance = new OkHttpClientManager();
				}
			}
		}
		return instance;
	}

	public OkHttpHandler getmOkHttpHandler() {
		return mOkHttpHandler;
	}


	protected class OkHttpHandler extends Handler{

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			Bundle data = msg.getData();
			OnResponseListener onResponseListener = (OnResponseListener) data.getSerializable("onResponseListener");
			int questType = data.getInt("questType");
			switch (msg.what) {
				case ERROR:
					Request request = (Request) msg.obj;
					if(onResponseListener != null){
						onResponseListener.onFailure(questType, request);
					}
					break;
				case SUCCESS:
					HeaderEntity headerEntity = (HeaderEntity) data.getSerializable("headerEntity");
					Class clazz = (Class) data.getSerializable("clazz");

					String result = ((String) msg.obj);
					try {
						JSONObject jsonObject = new JSONObject(result);
						String sign = jsonObject.getString("sign");
						String responseResult = jsonObject.getString("result");
						StringBuffer sb = new StringBuffer(headerEntity.toString());
						sb = sb.append(responseResult);
						//sign校验成功
						NLoger.i("zwq","response Header== "+sb.toString());
						if(sign.equals(HMACKit.sign(sb.toString()))){
							String decrypt = MacKit.decrypt(responseResult);

							JSONObject jsonObject1 = new JSONObject(decrypt);
							int code = jsonObject1.getInt("code");
							if(checkToken(code))
								return;
							if(code == -1){
								//Toast.makeText(MainApplication.getInstance(),"未知错误!",Toast.LENGTH_SHORT).show();
								NLoger.i("zwq","url="+headerEntity.getAccess_url());
								NLoger.i("zwq",decrypt);
								return;
							}

							if(onResponseListener != null) {
								if(FBLLibrary.isDebug){
									NLoger.i("zwq","url="+headerEntity.getAccess_url());
									Logger.json(TAG+questType,decrypt);
								}
								onResponseListener.onSuccess2String(questType, decrypt);
								if(clazz == null){
									onResponseListener.onSuccess2Object(questType, JSON.parseObject(decrypt, BaseResult.class));
								}else {
									onResponseListener.onSuccess2Object(questType,  JSON.parseObject(decrypt, clazz));
								}

							}
						}else {
							//sign校验失败
							onResponseListener.onFailure(questType, "sign校验失败");
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;

				default:
					break;
			}
		}
	}
	/**
	 * 作者：zwq
	 * 时间: 2017/3/22 12:01
	 * 描述: 如果 true 提示用户重新登录
	 * 			false 处理业务
	 */
	private static boolean checkToken(int code){
		if(1020 == code){//token失效
			Toast.makeText(FBLLibrary.mContext,
					"登录过期,请重新登录",Toast.LENGTH_LONG).show();
			return true;
		}
		return false;
	}

	public void putReq(String key,Call call){
		allRequests.put(key,call);
	}
	public void cancleReq(String key){
		Call call = allRequests.get(key);
		if(call != null){
			call.cancel();
			allRequests.remove(key);
		}
	}

	public void completeRemove(String url){
		allRequests.remove(url);
	}

}
