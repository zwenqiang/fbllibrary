package com.fullbloom.fbllibrary.network;

import com.alibaba.fastjson.JSON;
import com.fullbloom.fbllibrary.BuildConfig;
import com.fullbloom.fbllibrary.FBLLibrary;
import com.fullbloom.fbllibrary.util.NLoger;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by zwq on 2017/5/24.
 *
 * @desc:  Delete 请求逻辑
 */

public class DelRequest extends ContextRequest {

    private DelRequest(){
        super();
    }
    private static DelRequest instance = null;
    public static DelRequest getInstance(){
        if(instance == null){
            synchronized (DelRequest.class){
                if(instance == null){
                    instance = new DelRequest();
                }
            }
        }
        return instance;
    }
    @Override
    public void doRequest( int requestType, String url, Class clazz, String jsonKey, APIParams params, OnResponseListener responseListener) {
        ////处理请求体
        String encrypt = MacKit.encrypt(JSON.toJSONString(params));
        if(FBLLibrary.isDebug){
            String decrypt = MacKit.decrypt(encrypt);
            NLoger.i(TAG,"请求之前 上传参数解密 ："+decrypt);
        }
        FormBody.Builder formBuilder = new FormBody.Builder();
        RequestBody body = formBuilder.add(jsonKey, encrypt).build();
        Request.Builder builderq = new Request.Builder();
        CountingRequestBody countingRequestBody = new CountingRequestBody(requestType,body,responseListener);
        builderq.url(url).delete(countingRequestBody).tag(url).build();
        //处理请求头信息,sign校验
        appendHeaders(builderq,params, url);
        enqueueRequest( params,requestType, url, clazz, builderq.build(), responseListener);
    }
}
