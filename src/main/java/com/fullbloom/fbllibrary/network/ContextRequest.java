package com.fullbloom.fbllibrary.network;

import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.fullbloom.fbllibrary.FBLLibrary;
import com.fullbloom.fbllibrary.util.CommonUtils;
import com.fullbloom.fbllibrary.util.NLoger;
import com.fullbloom.fbllibrary.util.SharedUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by zwq on 2017/5/24.
 *
 * @desc:
 */

public abstract class ContextRequest implements BaseRequest {

    protected static final String TAG = "TAG";
    public static String PHONEIMEI = "PHONEIMEI";
    public static String ACCESS_TOKEN = "ACCESS_TOKEN";
    protected static final MediaType mJSON = MediaType.parse("application/json; charset=utf-8");
    protected static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    protected static final MediaType STREAM = MediaType.parse("application/octet-stream");


    /**
     * 网络请求
     * @author zwq
     * @date 2016-1-14
     * @param request
     */
    @SuppressWarnings("rawtypes")
    protected void enqueueRequest(final APIParams params
            , final int questType
            , final String url
            , final Class clazz
            , Request request
            , final OnResponseListener onResponseListener){
        Callback callback = new Callback(){

            @Override
            public void onFailure(Call call, IOException e) {
                Request request = call.request();
                NLoger.i(TAG,"--------------------------------------");
                NLoger.i(TAG,"url -->" + url);
                if(params != null){
                    NLoger.i(TAG,"请求参数 -->" + params.toString());
                }
                NLoger.i(TAG,"响应失败 -->" + e.toString());
                NLoger.i(TAG,"--------------------------------------");

                Message msg = OkHttpClientManager.getInstance().getmOkHttpHandler().obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putSerializable("onResponseListener", onResponseListener);
                bundle.putInt("questType",questType);
                msg.setData(bundle);
                msg.obj = request;
                msg.what = OkHttpClientManager.ERROR;
                msg.sendToTarget();
                OkHttpClientManager.getInstance().completeRemove(url);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = "";
                ResponseBody body = null;
                if (response.isSuccessful()) {
                    body = response.body();
                    result = body.string();
                } else {
                    result = "请求失败,稍后再试";
                }
                NLoger.i(TAG, "             ");
                NLoger.i(TAG,"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                NLoger.i(TAG,"responseCode -->  " + response.code());
                NLoger.i(TAG,"questType -->  " + questType);
                NLoger.i(TAG,"url -->" + url);
                if(params != null){
                    NLoger.i(TAG,"请求参数 -->" + JSON.toJSONString(params));
                }
                NLoger.i(TAG,questType+"-->响应成功 --> url="+url+" \nresult " + result);
                HeaderEntity headerEntity = (HeaderEntity) response.request().tag();
                Message msg = OkHttpClientManager.getInstance().getmOkHttpHandler().obtainMessage();
                msg.obj = result;
                Bundle bundle = new Bundle();
                bundle.putSerializable("onResponseListener", onResponseListener);
                bundle.putSerializable("headerEntity", headerEntity);
                bundle.putSerializable("clazz",clazz);
                bundle.putInt("questType",questType);
                msg.setData(bundle);
                msg.what = OkHttpClientManager.SUCCESS;
                msg.sendToTarget();
                OkHttpClientManager.getInstance().completeRemove(url);
            }
        };
        Call call = OkHttpClientManager.getInstance().mOkHttpClient.newCall(request);
        OkHttpClientManager.getInstance().putReq(url,call);
        call.enqueue(callback);
    }

    protected void appendHeaders(Request.Builder builder, APIParams params, String url)
    {
        builder.headers(buildHeader(builder,params,url));
    }


    /**
     * 作者：zwq
     * 时间: 2017/3/17 10:37
     * 描述:  头信息加密 组装 Header
     */
    private Headers buildHeader(Request.Builder builder, APIParams params, String url){

        Headers.Builder headerBuilder = new Headers.Builder();
        //设备版本
        String os_version = Build.VERSION.RELEASE;  //版本判断
        String phoneIMEI = "";
        //设备序列号 加密
        if (Build.VERSION.SDK_INT >= 23) {
            phoneIMEI = SharedUtil.getString(PHONEIMEI);
        }else {
            phoneIMEI = SharedUtil.getString(PHONEIMEI);
            if(phoneIMEI == null || TextUtils.isEmpty(phoneIMEI)){
                phoneIMEI = CommonUtils.getPhoneIMEI(FBLLibrary.mContext);
                SharedUtil.putData(PHONEIMEI,phoneIMEI);
            }
        }
        //当前时间
        long currentTiem = System.currentTimeMillis();
        //请求URL
        //app版本号
        String appVersionCode = String.valueOf(CommonUtils.getVersionCode());
        //本地语言
        String language = "zh_CN";
        //token获取
        String token = SharedUtil.getString(ACCESS_TOKEN);

        HeaderEntity headerEntry = new HeaderEntity();
        headerEntry.setMachine_code(phoneIMEI == null || TextUtils.isEmpty(phoneIMEI)?"1":phoneIMEI);
        headerEntry.setAccess_time(currentTiem);
        headerEntry.setAccess_url(url);
        headerEntry.setOs_type("Android");
        headerEntry.setOs_version(os_version);
        headerEntry.setApp_version(appVersionCode);
        headerEntry.setLocale_language("zh_CN");
        if(token != null
                && !TextUtils.isEmpty(token)
//                && url.indexOf("inter") != -1
                ){
            headerBuilder.add("access_token",token);
            headerEntry.setAccess_token(token);
        }
        // 处理sign值
        StringBuilder sb = new StringBuilder();
        sb.append(headerEntry.toString());
        if(params != null){
            String jsonStr = JSON.toJSONString(params);
            jsonStr = MacKit.encrypt(jsonStr);
            sb.append(jsonStr);
            NLoger.i("zwq","paramEcrypt == "+jsonStr);
        }
        String sign = HMACKit.sign(sb.toString());

        /**  Header start  */
        headerBuilder.add("machine_code"
                ,MacKit.encrypt(phoneIMEI == null || TextUtils.isEmpty(phoneIMEI)?"1":phoneIMEI));
        headerBuilder.add("access_time",String.valueOf(currentTiem));
        headerBuilder.add("access_url",url);
        headerBuilder.add("os_type","Android");
        headerBuilder.add("os_version",os_version);
        headerBuilder.add("app_version",appVersionCode);
        headerBuilder.add("locale_language",language);
        headerBuilder.add("sign",sign);
        /**  Header end  */

        builder.tag(headerEntry);

        NLoger.i("zwq","headerEntry == "+headerEntry.toString());
        NLoger.i("zwq","sb.toString() == "+sb.toString());
        NLoger.i("zwq","sign == "+sign);
        return headerBuilder.build();
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }
}
