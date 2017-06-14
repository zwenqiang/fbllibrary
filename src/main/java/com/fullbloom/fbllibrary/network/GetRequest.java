package com.fullbloom.fbllibrary.network;

import com.alibaba.fastjson.JSON;
import com.fullbloom.fbllibrary.util.NLoger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.Request;

/**
 * Created by zwq on 2017/5/24.
 *
 * @desc:  GET 请求逻辑
 */

public class GetRequest extends ContextRequest {

    private GetRequest(){
        super();
    }
    private static GetRequest instance = null;
    public static GetRequest getInstance(){
        if(instance == null){
            synchronized (GetRequest.class){
                if(instance == null){
                    instance = new GetRequest();
                }
            }
        }
        return instance;
    }
    @Override
    public void doRequest(int requestType, String url, Class clazz, String jsonKey, APIParams params, OnResponseListener responseListener) {
        StringBuilder buffer  = new StringBuilder(url);
        if(params != null){
            try {
                String originalStr = JSON.toJSONString(params);
                NLoger.i("get 请求之前Param-->"+params);
                buffer.append("?")
                        .append(jsonKey)
                        .append("=")
                        .append(URLEncoder.encode(MacKit.encrypt(originalStr),"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        url = buffer.toString();
        Request.Builder builderq = new Request.Builder();
        builderq.url(url).get().tag(url).build();
        //处理请求头信息,sign校验
        appendHeaders(builderq, params, url);
        enqueueRequest(null,requestType, url, clazz, builderq.build(), responseListener);
    }
}
