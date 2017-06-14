package com.fullbloom.fbllibrary.network;


import java.io.Serializable;


/**
 * 响应监听器
 */
public interface OnResponseListener<T> extends Serializable{
	/** json 字符串回调 */
	void onSuccess2String(int key, String result);
	/** 解析后bean 对象回调 */
	void onSuccess2Object(int key, T object);
	/** 失败 */
	void onFailure(int key, Object request);
	/**
	 * 请求进度回调
	 * bytesWritten 写入长度
	 * contentLength 总长度
	 * currentLength 百分比
	 */
	void onOkRequestProgress(int key, long bytesWritten, long contentLength, int currentLength);

}
