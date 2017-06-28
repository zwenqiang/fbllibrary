package com.fullbloom.fbllibrary.network;

import android.support.annotation.IntDef;


/**
 * Created by zwq on 2017/3/27.
 */

public interface BaseRequest {

    int POST = 0;
    int GET = 1;
    int DELETE = 2;

    @IntDef({POST,GET,DELETE})
    @interface RequestType{

    }
    /**
     * @param requestType 请求区分 利用mvp 用不到
     * @param url 请求ur
     * @param clazz 解析实体
     * @param jsonKey 按需填写
     * @param params 请求参数
     * @param responseListener 响应回调
     *
     */
    void doRequest(int requestType, String url, Class clazz, String jsonKey, APIParams params, OnResponseListener responseListener);


}
