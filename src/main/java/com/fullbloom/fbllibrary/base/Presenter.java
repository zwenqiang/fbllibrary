package com.fullbloom.fbllibrary.base;

public interface Presenter<V> {

    void attachView(V view);

    void detachView();

}
