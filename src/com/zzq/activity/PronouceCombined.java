package com.zzq.activity;

import player.MyPlayer;
import utils.Mp3Url;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class PronouceCombined {
	private static String myStr = "";
	private static int isNetworkLive = 0;
	// private static boolean isRetriving = false;
	private static Thread thread = null;

	public static void pronouce(String str, Context context) {

		NetworkInfo localNetworkInfo = ((ConnectivityManager) context
				.getSystemService("connectivity")).getActiveNetworkInfo();

		if (localNetworkInfo == null || localNetworkInfo.getType() != 1) {
			if (isNetworkLive == 0) {
				Toast.makeText(context, "no network", 500).show();
			}
			if (!Speak.speak(str)) {
				Speak.init(str, context);
			}
			isNetworkLive = 1;
		} else {
			isNetworkLive = 1;
			if (str.length() > 0) {
				myStr = str;
				thread = new Thread() {
					public void run() {
						String url = new Mp3Url(myStr).getMp3url_string();
						if (url != null) {
							new MyPlayer(url);
						}
					};
				};
				thread.start();
			}
		}
	}

	public static void stopPronouce() {
		System.out.println("stop pronouce");
		Speak.stop();

		if (thread != null && thread.isAlive()) {
			thread.interrupt();
		}
		thread = null;
	}

}
