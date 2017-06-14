package com.fullbloom.fbllibrary.base;

import android.os.Bundle;

/**
 * Created by RayYeung on 2016/8/8.
 */
public abstract class BaseMvpActivity<P extends BasePersenter> extends BaseActivity {
    protected P mvpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mvpPresenter = createPresenter();
        super.onCreate(savedInstanceState);
    }

    protected abstract P createPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }
    }





//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        user = BaseApplication.getInstance().getUserInfo();
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    protected void onResume() {
//        user = BaseApplication.getInstance().getUserInfo();
//        super.onResume();
//    }

//    public boolean checkLogin() {
//        if (user == null) {
//            intent2Activity(LoginActivity.class);
//            return false;
//        }
//        return true;
//    }

}
