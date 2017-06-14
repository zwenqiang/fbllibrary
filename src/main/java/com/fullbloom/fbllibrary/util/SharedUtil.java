package com.fullbloom.fbllibrary.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.fullbloom.fbllibrary.FBLLibrary;
import com.fullbloom.fbllibrary.R;


public class SharedUtil {

	private static SharedPreferences sharedPreferences = null;


	/**
	 * 构造函数
	 */
	public SharedUtil() {
		sharedPreferences = FBLLibrary.mContext.getSharedPreferences(
				"SP_" + FBLLibrary.mContext.getString(R.string.app_name),
				Context.MODE_PRIVATE);

	}

	public static SharedPreferences getInstance() {
		if (sharedPreferences == null) {
			new SharedUtil();
		}
		return sharedPreferences;
	}

	/**
	 * 保存数据
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putData(String key, int value) {
		getInstance().edit().putInt(key, value).commit();
	}

	public static void putData(String key, long value) {
		getInstance().edit().putLong(key, value).commit();
	}

	public static void putData(String key, String value) {
		getInstance().edit().putString(key, value).commit();
	}


	public static void putData(String key, boolean value) {
		getInstance().edit().putBoolean(key, value).commit();
	}

	/**
	 * 获取数据
	 * 
	 * @param context
	 * @param key
	 */
	public static String getString(String key) {
		return getInstance().getString(key, "");
	}


	public static int getInt(String key) {
		return getInstance().getInt(key, -1);
	}

	public static long getLong(String key) {
		return getInstance().getLong(key, 0L);
	}

	/**
	 * 没有值默认是false
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(String key) {
		return getInstance().getBoolean(key, false);
	}

	/**
	 * 没有值默认是 true
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean getBooleanTrue(String key) {
		return getInstance().getBoolean(key, true);
	}
	public static void clear(){
		getInstance().edit().clear().commit();
	}

}
