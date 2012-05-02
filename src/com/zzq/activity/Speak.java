package com.zzq.activity;

import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.widget.Toast;

public class Speak {

	static private TextToSpeech mSpeech;
	static private Context myContext;
	static private String string;
	static private SpeachListener speachListener = new SpeachListener();

	static void init(String str, Context context) {
		try {
			string = str;
			myContext = context;
			mSpeech = new TextToSpeech(context, speachListener);
		} catch (Exception e) {
			Toast.makeText(context, "pronounce failed", 500).show();
			e.printStackTrace();
		}
	}

	static boolean speak(String str) {
		if (mSpeech != null) {
			if (!mSpeech.isSpeaking()) {
				mSpeech.speak(string, TextToSpeech.QUEUE_FLUSH, null);
			}
			return true;
		}
		return false;
	}

	static void stop() {
		if (mSpeech != null) {
			mSpeech.shutdown();
		}
	}

	static class SpeachListener implements OnInitListener {

		public void onInit(int status) {
			try {
				if (status == TextToSpeech.SUCCESS) {
					int result = mSpeech.setLanguage(Locale.ENGLISH);
					// set read english only
					if (result == TextToSpeech.LANG_MISSING_DATA
							|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
						// if the result is null,print it
						Log.e("lanageTag", "not use");
					} else {
						mSpeech.speak(string, TextToSpeech.QUEUE_FLUSH, null);
					}
				} else {
					// Toast.makeText(myContext,
					// "no tts, can't pronouce the word",
					// 500).show();
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
	}
}
