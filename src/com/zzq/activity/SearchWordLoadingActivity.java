package com.zzq.activity;

import utils.WordUrl;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SearchWordLoadingActivity extends Activity {
	private GameView mGameView = null;
	Handler handler;
	String danci;
	AssetFileDescriptor mp3NmaeString;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mGameView = new GameView(this);
		setContentView(mGameView);
		// play mp3
		danci = getIntent().getExtras().getString("danci");
		
		new Thread() {
			public void run() {
				// get word meaning
				String meaning = new WordUrl(danci).getMeaning();
				Message msg = new Message();
				msg.obj = meaning;
				handler.sendMessage(msg);
			};
		}.start();

		handler = new Handler() {
			public void handleMessage(Message msg) {
				String meaning = (String) msg.obj;
				Intent intent = new Intent(SearchWordLoadingActivity.this,
						SearchActivity.class);
				intent.putExtra("meaning", meaning);
				mGameView.destroyDrawingCache();
				setResult(9, intent);
				SearchWordLoadingActivity.this.finish();
			};
		};
	}
}
