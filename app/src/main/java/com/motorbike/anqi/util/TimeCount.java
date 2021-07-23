package com.motorbike.anqi.util;


import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;


/*定义一个倒计时的类*/
public class TimeCount extends CountDownTimer {

	private TextView textView;
	private Context context;

	public TimeCount(long millisInFuture, long countDownInterval,
                     TextView textView, Context context) {
		super(millisInFuture, countDownInterval);
		this.textView = textView;
		this.context=context;
	}

	@Override
	public void onFinish() {
		textView.setClickable(true);
		textView.setText("获取验证码");
		textView.setTextColor(Color.parseColor("#ffa670"));
//		textView.setBackgroundResource(R.drawable.hong_yuan_bg_bk);
	}

	@Override
	public void onTick(long millisUntilFinished) {
		// System.out.println("millisUntilFinished:"+millisUntilFinished);
		// 获取当前时间总秒数
		// first = millisUntilFinished / 1000;

		/**
		 * 转成小时分秒
		 */
		// textView.setText(TimeUtils.getDay(millisUntilFinished)+"天"+TimeUtils.getHour(millisUntilFinished)
		// +"小时"+TimeUtils.getMin(millisUntilFinished)+"分钟"+TimeUtils.getMs(millisUntilFinished)+"秒");
		textView.setText(TimeUtils.getMs(millisUntilFinished) + "秒");
		textView.setTextColor(Color.parseColor("#999999"));
//		textView.setBackgroundResource(R.drawable.hui_yuan_bg_bk);
		textView.setClickable(false);

	}
}
