package com.zzq.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Model.ReciteMode;
import Model.WordStructure;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class EngToChnActivity extends ListActivity {
	TextView danciView, countDownTextView;
	List<String> optionList = null;
	int duoshaoge;
	Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MyApp appState = (MyApp) getApplicationContext();
		appState.addActivity(this);
		setContentView(R.layout.ten_activity);
		setTitle("选择正确的中文释义");
		findView();

		handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				if (msg.what == -100) {
					gotoWrongAnswer();

				} else
					countDownTextView.setText("" + msg.what);
			};
		};
		CountDownModel.getInstance().startCountDown(handler);

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (Constant.ticketList.size() <= 4) {
			Toast.makeText(EngToChnActivity.this,
					"not enough words to generate the exam", Toast.LENGTH_LONG)
					.show();
			System.out.println("dancitaishaole");
		} else {
			setOption();
			this.setAdapter();
		}

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		// CountDownModel.getInstance().stopCounting();

		printCursor();

		if (position == answerPos) {
			// TODO

			Constant.ticketListCursor++;
			if (Constant.getCurrentTicket() != null) {
				nextWord();
			} else {
				unitFinished();
			}
		} else {
			gotoWrongAnswer();
		}
	}

	private void printCursor() {

		Toast.makeText(this,
				Constant.ticketListCursor + "-" + Constant.ticketList.size(),
				500).show();

	}

	private void gotoWrongAnswer() {
		finishSelf();

		Constant.ticketList.add(Constant.getCurrentTicket());
		Intent intent = new Intent(EngToChnActivity.this,
				WrongAnswerActivity.class);
		startActivity(intent);
	}

	private void nextWord() {

		if (Constant.getCurrentTicket().getReciteMode() == ReciteMode.ChnToEng
				|| Constant.getCurrentTicket().getReciteMode() == ReciteMode.ListenAndSpell) {

			finishSelf();

			Intent intent = new Intent(EngToChnActivity.this,
					ChnToEngActivity.class);
			startActivity(intent);

		} else if (Constant.getCurrentTicket().getReciteMode() == ReciteMode.EngToChnListen) {
			finishSelf();
			Intent intent = new Intent(EngToChnActivity.this,
					EngToChnListenActivity.class);
			startActivity(intent);
		} else if (Constant.getCurrentTicket().getReciteMode() == ReciteMode.EngToChn) {
			CountDownModel.countDown = 11;
			setOption();
			setAdapter();
		}

	}

	private void unitFinished() {

		Toast.makeText(this, " unit " + Constant.unit_number + " finished~",
				1000).show();

		SharedPreferences preferences = getSharedPreferences(
				Constant.FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("" + Constant.unit_number, true);
		editor.commit();
		//
		finishSelf();
		Intent intent = new Intent(EngToChnActivity.this,
				ChooseUnitActivity.class);
		startActivity(intent);
	}

	private int answerPos;

	private void setOption() {
		optionList = null;
		optionList = new ArrayList<String>();

		int[] arr = gererateRandom(Constant.wordList.size(), Constant
				.getCurrentTicket().getWordCursor());
		for (int j = 0; j < 3; j++) {
			optionList.add(Constant.wordList.get(arr[j]).getMeanString());
		}
		answerPos = new Random().nextInt(4);
		optionList.add(answerPos, Constant.getCurrentWord().getMeanString());
	}

	private int[] gererateRandom(int size, int exclude) {

		int[] arr = new int[3];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) (new Random().nextInt(size));
			while (arr[i] == exclude) {
				arr[i] = (int) (new Random().nextInt(size));
			}
			for (int j = 0; j < i; j++) {
				if (arr[i] == arr[j]) {
					i--;
					break;
				}
			}
		}
		return arr.clone();
	}

	private void findView() {
		danciView = (TextView) findViewById(R.id.ten_act_tv);
		countDownTextView = (TextView) findViewById(R.id.ten_act_countdown_textview);
		
		Button proButton;
		proButton = (Button) findViewById(R.id.EngToChn_pronounce);
		proButton.setVisibility(View.GONE);

	}

	private void setAdapter() {
		ArrayList<Map<String, String>> tmpList = new ArrayList<Map<String, String>>();

		for (int i = 0; i < optionList.size(); i++) {
			Map<String, String> tmpMap = new HashMap<String, String>();
			tmpMap.put("meaning", optionList.get(i));
			tmpList.add(tmpMap);
		}

		SimpleAdapter listAdapter = new SimpleAdapter(this, tmpList,
				R.layout.ten_activity_item, new String[] { "meaning" },
				new int[] { R.id.exercise_selection });

		setListAdapter(listAdapter);

		danciView.setText(getWordNameString());

	}

	private String getWordNameString() {
		return Constant.getCurrentWord().getNameString();
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		boolean retValue;
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			retValue = true;
		} else {
			retValue = super.onKeyUp(keyCode, event);
		}
		return retValue;
	}

	// ******************************************************
	// menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "EXIT");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finishSelf();
		Intent intent = new Intent(EngToChnActivity.this,
				ChooseUnitActivity.class);
		startActivity(intent);
		return super.onOptionsItemSelected(item);
	}

	private void finishSelf() {
		CountDownModel.getInstance().stopCounting();
		this.finish();
	}

}
