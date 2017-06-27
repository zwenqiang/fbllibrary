package com.fullbloom.fbllibrary;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.congcundai.app.MainApplication;
import com.congcundai.app.base.Commons;
import com.congcundai.app.utils.SharedUtil;
import com.fullbloom.fbllibrary.network.APIParams;
import com.fullbloom.fbllibrary.network.CountingRequestBody;
import com.fullbloom.fbllibrary.network.OnResponseListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * okhttp控制类
 *
 * @author kyle
 * @date 2016-1-14
 */
@SuppressWarnings("unused")
public class OkHttpClientManager {

    public static final String TAG = "kyle";

    public static final MediaType mJSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private static final MediaType STREAM = MediaType.parse("application/octet-stream");
    private static final int ERROR = 444444;
    private static final int SUCCESS = 666666;
    private static OnResponseListener listeners = null;
    private static int requestTypes;
    @SuppressWarnings("rawtypes")
    private static Class clazzs;
    private OkHttpClient mOkHttpClient = null;
    private static OkHttpHandler mHandler = new OkHttpHandler();
    private static int code;
    private SSLSocketFactory sslSocketFactory;

    private OkHttpClientManager() {
//        mOkHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(20, TimeUnit.SECONDS)
//                .build();

//        mOkHttpClient = new OkHttpClient().newBuilder()
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(20, TimeUnit.SECONDS)
//                .hostnameVerifier(new HostnameVerifier() {
//
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        //强行返回true 即验证成功
//                        return true;
//                    }
//                }).build();

        mOkHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .hostnameVerifier(new HostnameVerifier() {

                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        //强行返回true 即验证成功
                        return true;
                    }
                }).build();
    }


    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    ;
    private static OkHttpClientManager instance = null;

    public static OkHttpClientManager getInstance() {


        if (instance == null) {
            synchronized (OkHttpClientManager.class) {
                if (instance == null) {
                    instance = new OkHttpClientManager();
                }
            }
        }
        return instance;
    }


    /**
     * post  ---> 键值对提交
     *
     * @param questType          接口回调url --> key区分
     * @param url
     * @param clazz              javabean
     * @param params
     * @param onResponseListener 响应回调
     * @author kyle
     * @date 2016-1-14
     */
    @SuppressWarnings("rawtypes")
    public void postRequestKV(int questType, String url, Class clazz, APIParams params, HashMap<String, String> headers, OnResponseListener onResponseListener) {
//        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        MultipartBody.Builder builder = new MultipartBody.Builder("AaB03x");
        FormBody.Builder requestBodyPost = new FormBody.Builder();
//        FormEncodingBuilder formBody = new FormEncodingBuilder();
        if (params != null) {
            for (IdentityHashMap.Entry<String, Object> entry : params.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof File) {
                    File file = (File) value;
                    RequestBody fileBody = RequestBody.create(MEDIA_TYPE_PNG, file);
                    builder.addFormDataPart(key, null, fileBody);
                    //TODO 根据文件名设置contentType
                    builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\"; filename=\"" + file.getName() + "\""),
                            fileBody);
                } else if (value instanceof InputStream) {
                    try {
                        builder.addFormDataPart(key, null, RequestBody.create(STREAM, toByteArray((InputStream) value)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    builder.addFormDataPart(key, String.valueOf(value));
                }
            }
        }
        RequestBody requestBody = builder.build();
        Request.Builder builderq = new Request.Builder();
        CountingRequestBody countingRequestBody = new CountingRequestBody(questType, requestBody, onResponseListener);
        builderq.url(url).post(countingRequestBody).tag(url).build();

        HashMap<String, String> map = new HashMap<String, String>();

//        String phoneIMEI = CommonUtils.getPhoneIMEI(MainApplication.getInstance());
//        if (!TextUtils.isEmpty(phoneIMEI)) {
//            String value = phoneIMEI;
//            map.put("ext", value.trim());
//        }
//
//        map.put("v", "1.0");
//        map.put("source", "android");
//        Log.i(TAG, "source -->" + "android");
        appendHeaders(builderq, map);
        enqueueRequest(questType, url, params, clazz, builderq.build(), onResponseListener);
    }

    public void postRequestKV(int questType, String url, Class clazz, APIParams params, OnResponseListener onResponseListener) {
        postRequestKV(questType, url, clazz, params, null, onResponseListener);
    }

    /**
     * post  --->  json提交
     *
     * @param questType          接口回调key区分
     * @param url
     * @param clazz
     * @param params
     * @param onResponseListener
     * @author kyle
     * @date 2016-1-14
     */
    @SuppressWarnings("rawtypes")
    public void postRequestJson(int questType, String url, Class clazz, APIParams params, HashMap<String, String> headers, OnResponseListener onResponseListener) {
        Request.Builder builder = new Request.Builder();
        builder.header("Content-Type", "application/json");
        if (headers==null){
            headers = new HashMap<>();
        }
        headers.put("Cookie",SharedUtil.getString(Commons.SESSION));
        Log.i("postRequestJson", "session is  :" + SharedUtil.getString(Commons.SESSION));
        RequestBody body = RequestBody.create(mJSON, JSON.toJSONString(params));
        Request.Builder builderq = new Request.Builder();
        CountingRequestBody countingRequestBody = new CountingRequestBody(questType, body, onResponseListener);
        builderq.url(url).post(countingRequestBody).tag(url).build();
        appendHeaders(builderq, headers);
        enqueueRequest(questType, url, params, clazz, builderq.build(), onResponseListener);
    }

    public void postRequestJson(int questType, String url, Class clazz, APIParams params, OnResponseListener onResponseListener) {
        postRequestJson(questType, url, clazz, params, null, onResponseListener);
    }


//    public void postRequestJson(int type, String url, Class clazz, Map<String, Object> params, BaseActivity2 baseActivity2) {
//    }


    /**
     * post  --->  键值对 json提交        如：json = {key:value}
     *
     * @param questType          接口回调key区分
     * @param url
     * @param clazz
     * @param params
     * @param onResponseListener
     * @author kyle
     * @date 2016-1-14
     */
    @SuppressWarnings("rawtypes")
    public void postRequestKVJson(int questType, String url, Class clazz, String jsonKey, APIParams params, HashMap<String, String> headers, OnResponseListener onResponseListener) {

        RequestBody body = new FormBody.Builder().add(jsonKey, JSON.toJSONString(params)).build();
        Request.Builder builderq = new Request.Builder();
        CountingRequestBody countingRequestBody = new CountingRequestBody(questType, body, onResponseListener);
        builderq.url(url).post(countingRequestBody).tag(url).build();
        appendHeaders(builderq, headers);
        enqueueRequest(questType, url, params, clazz, builderq.build(), onResponseListener);
    }

    public void postRequestKVJson(int questType, String url, Class clazz, String jsonKey, APIParams params, OnResponseListener onResponseListener) {
        postRequestKVJson(questType, url, clazz, jsonKey, params, null, onResponseListener);
    }

    /**
     * get ----> get请求
     *
     * @param questType          接口回调key区分
     * @param url
     * @param clazz
     * @param
     * @param onResponseListener
     * @author kyle
     * @date 2016-1-14
     */
    @SuppressWarnings("rawtypes")
    public void getRequest(int questType, String url, Class clazz, HashMap<String, String> headers, OnResponseListener onResponseListener) {
        Request.Builder builderq = new Request.Builder();
        builderq.url(url).tag(url).build();
//        Request request = new Request.Builder().url(url).build();
        HashMap<String, String> map = new HashMap<String, String>();
//		String value = "351694060119430";
//		map.put("ext",value.trim());

//        String phoneIMEI = CommonUtils.getPhoneIMEI(MainApplication.getInstance());
//        if (!TextUtils.isEmpty(phoneIMEI)) {
//            String value = phoneIMEI;
//            map.put("ext", value.trim());
//        }
//        map.put("v", "1.0");
//        map.put("source", "android");
//        Log.i(TAG, "source -->" + "android");
//        Log.i(TAG, "v -->" + "1.0");

//        appendHeaders(builderq, map);

        enqueueRequest(questType, url, null, clazz, builderq.build(), onResponseListener);
    }

    public void getRequest(int questType, String url, Class clazz, OnResponseListener onResponseListener) {
        getRequest(questType, url, clazz, null, onResponseListener);
    }


//    /**
//     * @param url
//     * @param ashandler
//     */
//    public void get(String url, final AsyncHttpResponseHandler ashandler) {
//        Log.i(TAG, "requestUrl --> " + url);
//        myCookieStore = new PersistentCookieStore(MainApplication.getInstance());
//        asynchttpclient.setCookieStore(myCookieStore);
////			if(TextUtils.isEmpty(uuid)){
//////				uuid = CommonUtils.getPhoneIMEI();
////			}
////        String phoneIMEI = CommonUtils.getPhoneIMEI(MainApplication.getInstance());
////        if (!TextUtils.isEmpty(phoneIMEI)) {
////            String uuid = phoneIMEI;
////            asynchttpclient.addHeader("ext", uuid.trim());
////
////        }
//        asynchttpclient.get(url, ashandler);
//    }




//    @RequestMapping(value = "getPicture" ,produces = "application/json;charset=UTF-8",method = RequestMethod.GET)
//    @ResponseBody
//    public String demo(HttpServletRequest request, HttpServletResponse response, @RequestHeader HttpHeaders headers){
//        Cookie[] cookies = request.getCookies();
//
//
//        Cookie cookie = new Cookie("Set-Cookie","ddddddddd");
//        response.addCookie(cookie);
//        return "ssssss"
//    }


    /**
     * 上传图片   单张图片
     *
     * @param questType
     * @param url
     * @param clazz
     * @param
     * @param onResponseListener
     * @author kyle
     * @date 2016-1-15
     */
//    @SuppressWarnings("rawtypes")

    private void upLoadImage(int questType, String url, Class clazz, APIParams params, HashMap<String, String> headers, OnResponseListener onResponseListener) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (IdentityHashMap.Entry<String, Object> entry : params.entrySet()) {

            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof File) {
                File file = (File) value;
                builder.addFormDataPart(key, file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
//                builder.addFormDataPart(key, null,
//                        builder.addPart(
//                                Headers.of("Content-Disposition", "form-data; filename=\"img.png\""),
////                                Headers.of("Content-Disposition", "form-data; name=\"" + key + "\"; filename=\"" + file.getName() + "\""),
//                                RequestBody.create(MediaType.parse("image/png"), file))
//                                .build());

            }else if (value instanceof InputStream) {

                InputStream inputStream = (InputStream) value;
                try {
                    builder.addFormDataPart(key, null,
                            builder.addPart(
                                    Headers.of("Content-Disposition", "form-data; filename=\"img.png\""),
    //                                Headers.of("Content-Disposition", "form-data; name=\"" + key + "\"; filename=\"" + file.getName() + "\""),
                                    RequestBody.create(MediaType.parse("image/png"), toByteArray((InputStream) value)))
                                    .build());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        builder.build();

        RequestBody requestBody = builder.build();
        Request.Builder builderq = new Request.Builder();
        CountingRequestBody countingRequestBody = new CountingRequestBody(questType, requestBody, onResponseListener);
        builderq.url(url).post(countingRequestBody).tag(url).build();
        appendHeaders(builderq, headers);
        enqueueRequest(questType, url, params, clazz, builderq.build(), onResponseListener);

    }

    public void upLoadImage(int questType, String url, Class clazz, APIParams params, OnResponseListener onResponseListener) {
        upLoadImage(questType, url, clazz, params, null, onResponseListener);
    }

    /**
     * 网络请求
     *
     * @author kyle
     * @date 2016-1-14
     * @param request
     */
    @SuppressWarnings("rawtypes")
    int responsecode = 401;

    private void enqueueRequest(final int questType, final String url, final Object params, Class clazz, Request request, OnResponseListener onResponseListener) {
        requestTypes = questType;
        listeners = onResponseListener;
        clazzs = clazz;
        mOkHttpClient.newCall(request).enqueue(new Callback() {

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
                code = response.code();
                responsecode = response.code();

                Log.i(TAG, "             ");
                Log.i(TAG, "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                Log.i(TAG, "responseCode -->  " + response.code());
                Log.i(TAG, "questType -->  " + questType);
                Log.i(TAG, "url -->" + url);
                if (params != null) {
//                    Log.i(TAG, "请求参数 -->" + params.toString());
                    Log.i(TAG, "请求参数 -->" + JSON.toJSONString(params));
                }
                Log.i(TAG, "响应成功 -->" + result);
                Message msg = Message.obtain();
                msg.obj = result;
                msg.what = SUCCESS;
                mHandler.sendMessage(msg);

                //获取session的操作，session放在cookie头，且取出后含有“；”，取出后为下面的 s （也就是jsesseionid）
                Headers headers = response.headers();
                Log.d("info_headers", "header " + headers);
                List<String> cookies = headers.values("Set-Cookie");
                if (cookies.size() > 0) {
                    String session = cookies.get(0);
                    Log.d("info_cookies", "onResponse-size: " + cookies);
                    String s = session.substring(0, session.indexOf(";"));

                    if (!TextUtils.isEmpty(s)) {

                        SharedUtil.putData(Commons.SESSION, s);
                    }
                    Log.i("info_s", "session is  :" + s);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {

//                try {
//                    Response execute = call.execute();
//                    Headers headers = execute.headers();
//                    Log.d("info_headers", "header " + headers);
//                    List<String> cookies = headers.values("Set-Cookie");
//                    String session = cookies.get(0);
//                    Log.d("info_cookies", "onResponse-size: " + cookies);
//                    String s = session.substring(0, session.indexOf(";"));
//
//                    if (!TextUtils.isEmpty(s)) {
//
//                        SharedUtil.putData(Commons.SESSION, s);
//                    }
//                    Log.i("info_s", "session is  :" + s);
//                    Log.i("info_s", "session is  :" + s);
//
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }


                Log.i(TAG, "--------------------------------------");
                Log.i(TAG, "url -->" + url);
                if (params != null) {
                    Log.i(TAG, "请求参数 -->" + JSON.toJSONString(params));
                }
                Log.i(TAG, "响应失败 -->" + e.toString());
                Log.i(TAG, "--------------------------------------");
                Message msg = Message.obtain();
                msg.obj = call;
                msg.what = ERROR;
                mHandler.sendMessage(msg);
            }
        });
    }

    private static class OkHttpHandler extends Handler {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ERROR:
                    Call call = (Call) msg.obj;
                    if (listeners != null) {
                        listeners.onFailure(requestTypes, call);
                    }
                    break;
                case SUCCESS:
                    String result = ((String) msg.obj);
//					Map h = JSON.parseObject(resultbody.toString(), Map.class);
//					if (h.containsKey("result")) {
//						Object object = h.get("result");
//						DesEncrypt aesEncrypt2 = new DesEncrypt(Constants.DES_PUBLIC_ENCRYPT_KEY);
//						String result = aesEncrypt2.decrypt(object.toString());
//						h.put("result", result);
//						resultbody = JSON.toJSONString(h);
//						Log.e(TAG, "jsonString" + resultbody);
                    if (listeners != null) {
                        if (requestTypes == 30) {
//                            try {
////                                BitmapBean bitmapBean = new BitmapBean();
////                                bitmapBean.setResult(result);
////                                listeners.onSuccess2Object(requestTypes, bitmapBean);
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
                        } else {

                            listeners.onSuccess2String(requestTypes, result);
                        }
                        if (code == 401) {
//                            Intent intent = new Intent(MainApplication.getInstance(), LoginUpdateActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            MainApplication.getInstance().startActivity(intent);
                        } else if (code == 404) {
                            Toast.makeText(MainApplication.getInstance(), "服务器异常，请稍后再试!", Toast.LENGTH_LONG).show();
//                            return;
                        }
//				  else {
                        if (clazzs == null) {
                            try {
                                listeners.onSuccess2Object(requestTypes, new Object());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        } else {
                            if (result.startsWith("{") && result.endsWith("}"))
                                try {
                                    listeners.onSuccess2Object(requestTypes, JSON.parseObject(result, clazzs));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                        }
                    }
//			}
                    break;

                default:
                    break;
            }
        }
    }

//    private void cancle(Object object) {
//        Call call = mOkHttpClient.newCall(request);
//       call.cancel();
//    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    /**
     * @author kyle
     * @date 2016-1-14
     * 为了销毁Handler
     */
    private void destory() {
        instance = null;
    }

    protected void appendHeaders(Request.Builder builder, Map<String, String> headers) {
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) return;
        for (String key : headers.keySet()) {
            headerBuilder.add(key, headers.get(key));
        }
        builder.headers(headerBuilder.build());
    }

//    public void GetServersList(IServersListEvent serversListEvent) {
//        this.serversListEvent = serversListEvent;
//        serversLoadTimes = 0;
//        Request request = new Request.Builder()
//                .url(serversListUrl)
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//                if(e.getCause().equals(SocketTimeoutException.class) && serversLoadTimes<maxLoadTimes)//如果超时并未超过指定次数，则重新连接
//                {
//                    serversLoadTimes++;
//                    client.newCall(call.request()).enqueue(this);
//                }else {
//                    e.printStackTrace();
//                    WebApi.this.serversListEvent.getServers(null);
//                }
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String html = new String(response.body().bytes(), "big5");
//                Matcher m = serversListPattern.matcher(html);
//                ServersList serverList = new ServersList();
//                while (m.find()){
//                    serverList.add(new ServerInfo(m.group(1), m.group(2)));
//                }
//
//                Matcher mc1 = selectServerCodePattern.matcher(html);
//                Matcher mc2 = selectCityCodePattern.matcher(html);
//                if(mc1.find())
//                    serverList.selectServerCode=mc1.group(1);
//                if(mc2.find())
//                    serverList.selectCityCode=mc2.group(1);
//
//                WebApi.this.serversListEvent.getServers(serverList);
//            }
//        });
//    }
}
