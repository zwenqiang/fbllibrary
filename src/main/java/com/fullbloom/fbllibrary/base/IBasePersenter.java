package com.fullbloom.fbllibrary.base;

/**
 * Created by zwq on 2017/4/18.
 */

public interface IBasePersenter<V> {

    void attachView(V view);

    void detachView();

}
