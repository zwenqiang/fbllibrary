package com.fullbloom.fbllibrary.network;



/**
 * Created by zwq on 2017/3/27.
 */

public class BaseRequestContext {
    private static BaseRequest baseRequest;

    private BaseRequestContext(){}
    private static BaseRequestContext instance = null;
    public static BaseRequestContext getInstance(BaseRequest request){
        baseRequest = request;
        if(instance == null){
            synchronized (BaseRequestContext.class){
                if(instance == null){
                    instance = new BaseRequestContext(request);
                }
            }
        }
        return instance;
}

    private BaseRequestContext(BaseRequest baseRequest) {
        this.baseRequest = baseRequest;
    }
    public void doRequest(int requestType,String url, Class clazz,String jsonKey, APIParams params,OnResponseListener responseListener){
        baseRequest.doRequest(requestType,url,clazz,jsonKey,params,responseListener);
    }
}
