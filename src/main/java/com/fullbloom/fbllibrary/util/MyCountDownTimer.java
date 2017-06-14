package com.fullbloom.fbllibrary.util;

import android.os.CountDownTimer;
import android.widget.TextView;

public class MyCountDownTimer extends CountDownTimer {
	public static boolean iscountdown = false;
	private TextView button = null;
	
	public MyCountDownTimer(TextView button, long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);
		this.button = button;
	}

	@Override
	public void onFinish() {
		iscountdown = false;
		button.setClickable(true);
		button.setText("获取验证码");
	}

	@Override
	public void onTick(long millisUntilFinished) {
		button.setClickable(false);
		button.setText(millisUntilFinished / 1000+"秒");
	}

}
