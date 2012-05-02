package com.zzq.activity;

import java.util.Calendar;

import android.os.Handler;

public class CountDownModel {
	private static Handler handler;
	static int countDown;
	private static boolean isCounting;
	private static CountDownModel self;

	public static CountDownModel getInstance() {

		if (self == null) {
			self = new CountDownModel();
		}

		return self;
	}

	static private Thread thread;

	public void startCountDown(Handler h, int time) {

		while (thread != null && thread.isAlive()) {
			stopCounting();
		}

		isCounting = true;
		handler = h;
		countDown = time;

		thread = new Thread() {
			public void run() {

				long time = Calendar.getInstance().getTimeInMillis();
				int count = 0;

				while (countDown > 0 && isCounting && handler != null) {
					try {
						Thread.sleep(10);
					} catch (Exception e) {
						// TODO: handle exception
					}

					long time2 = Calendar.getInstance().getTimeInMillis();

					if ((time2 - time) > 1000 * count) {
						countDown--;
						handler.sendEmptyMessage(countDown);
						count++;
					}

				}
				if (countDown <= 0 && handler != null) {
					handler.sendEmptyMessage(-100);
				}
				handler = null;

			};
		};
		thread.start();
	}

	public void startCountDown(Handler h) {
		startCountDown(h, 11);
	}

	public void stopCounting() {
		isCounting = false;

	}

}
