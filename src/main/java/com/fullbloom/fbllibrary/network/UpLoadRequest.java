package com.fullbloom.fbllibrary.network;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.IdentityHashMap;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by zwq on 2017/6/27.
 *
 * @desc:
 */

public class UpLoadRequest extends ContextRequest {
    @Override
    public void doRequest(int requestType, String url, Class clazz, String jsonKey, APIParams params, OnResponseListener responseListener) {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (IdentityHashMap.Entry<String, Object> entry : params.entrySet()) {

            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof File) {

                File file = (File) value;
                builder.addFormDataPart(key, file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));

            }else if (value instanceof InputStream) {
                InputStream inputStream = (InputStream) value;
                try {
                    builder.addFormDataPart(key, System.currentTimeMillis()+"", RequestBody.create(MEDIA_TYPE_PNG, toByteArray(inputStream)));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        MultipartBody requestBody = builder.build();
        Request.Builder builderq = new Request.Builder();
        CountingRequestBody countingRequestBody = new CountingRequestBody(requestType,requestBody,responseListener);
        builderq.url(url).post(countingRequestBody).tag(url).build();
        //处理请求头信息,sign校验
        appendHeaders(builderq,params, url);
        enqueueRequest( params,requestType, url, clazz, builderq.build(), responseListener);
    }
}
