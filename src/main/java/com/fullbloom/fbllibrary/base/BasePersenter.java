package com.fullbloom.fbllibrary.base;


import com.fullbloom.fbllibrary.network.APIParams;
import com.fullbloom.fbllibrary.network.BaseRequestContext;
import com.fullbloom.fbllibrary.network.DelRequest;
import com.fullbloom.fbllibrary.network.GetRequest;
import com.fullbloom.fbllibrary.network.OnResponseListener;
import com.fullbloom.fbllibrary.network.PostRequest;

/**
 * Created by zwq on 2017/4/18.
 */

public class BasePersenter<V> implements IBasePersenter<V>{


    public V mvpView;

    public BasePersenter(V mvpView)
    {
        attachView(mvpView);
    }
    @Override
    public void attachView(V view) {
        this.mvpView = view;
    }

    @Override
    public void detachView() {
        mvpView = null;
    }

    protected static void doPost(int requestType, String url, Class clazz,String jsonKey, APIParams params, OnResponseListener listener){
        BaseRequestContext.getInstance(PostRequest.getInstance()).doRequest(requestType,url,clazz, jsonKey,params,listener);
    }
    protected static void doGet(int requestType, String url, Class clazz, String jsonKey,APIParams params,OnResponseListener listener){
        BaseRequestContext.getInstance(GetRequest.getInstance()).doRequest(requestType,url,clazz,jsonKey,params,listener);
    }
    protected static void doDel(int requestType, String url, Class clazz, String jsonKey,APIParams params,OnResponseListener listener){
        BaseRequestContext.getInstance(DelRequest.getInstance()).doRequest(requestType,url,clazz,jsonKey,params,listener);
    }

}
