package com.fullbloom.fbllibrary;

import android.content.Context;

/**
 * Created by zwq on 2017/6/12.
 *
 * @desc:
 */

public class FBLLibrary {


    public static Context mContext;
    public static boolean isDebug = false;

    private FBLLibrary(){}
    private static FBLLibrary instance = null;
    public static FBLLibrary getInstance(){
        if(instance == null){
            synchronized (FBLLibrary.class){
                if(instance == null){
                    instance = new FBLLibrary();
                }
            }
        }
        return instance;
    }

    public FBLLibrary init(Context context){
        mContext = context;
        return instance;
    }
    public FBLLibrary isDebug(boolean debug){
        isDebug = debug;
        return instance;
    }
}
