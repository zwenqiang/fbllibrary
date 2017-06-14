package com.fullbloom.fbllibrary.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class NToast {
	private NToast() {
	}

	/**
	 * Toast 展示在中�?	 * @param context
	 * @param toast_str
	 */
	public static void ShowCenter(Context context, String toast_str) {
		Toast toast = Toast.makeText(context, toast_str, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER | Gravity.TOP, 0, 0);
		toast.setMargin(0, 0.4F);	
		toast.show();
	}
	/**
	 * Toast 长时间展�?	 * @param context
	 * @param toast_str
	 */
	public static void ShowLong(Context context, String toast_str) {
		Toast toast = Toast.makeText(context, toast_str, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER | Gravity.TOP, 0, 0);
		toast.setMargin(0, 0.4F);
		toast.show();
	}
	/**
	 * Toast 正常展示
	 * @param context
	 * @param toast_str
	 */
	public static void show(Context context, String toast_str) {
		Toast toast = Toast.makeText(context, toast_str+"", Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		
		toast.show();
	}
}
