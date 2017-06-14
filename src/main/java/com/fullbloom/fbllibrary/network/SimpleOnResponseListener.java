package com.fullbloom.fbllibrary.network;


/**
 * Created by zwq on 2017/4/20.
 */

public abstract class SimpleOnResponseListener<T> implements OnResponseListener<T> {
    @Override
    public void onSuccess2String(int key, String result) {

    }

    @Override
    public void onFailure(int key, Object request) {

    }

    @Override
    public void onOkRequestProgress(int key, long bytesWritten, long contentLength, int currentLength) {

    }
}
