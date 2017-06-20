package com.fullbloom.fbllibrary.network;



/**
 * Created by zwq on 2017/3/27.
 */

public class NetUtils {
    private static BaseRequest baseRequest;

    private NetUtils(){}
    private static NetUtils instance = null;
    public static NetUtils getInstance(BaseRequest request){
        baseRequest = request;
        if(instance == null){
            synchronized (NetUtils.class){
                if(instance == null){
                    instance = new NetUtils(request);
                }
            }
        }
        return instance;
}

    private NetUtils(BaseRequest baseRequest) {
        this.baseRequest = baseRequest;
    }
    public void doRequest(int requestType,String url, Class clazz,String jsonKey, APIParams params,OnResponseListener responseListener){
        baseRequest.doRequest(requestType,url,clazz,jsonKey,params,responseListener);
    }
}
