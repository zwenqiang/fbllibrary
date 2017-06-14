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
     * @param url
     * @param clazz
     * @param jsonKey
     * @param params
     * @param responseListener
     *
     */
    void doRequest(int requestType, String url, Class clazz, String jsonKey, APIParams params, OnResponseListener responseListener);


}
