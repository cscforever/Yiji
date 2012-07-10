package com.zzq.activity;

import java.util.ArrayList;
import java.util.Arrays;

import Model.ReciteMode;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ChnToEngActivity extends Activity {
	private TextView countDownTextView;
	private TextView danciView;
	private TextView meaningView;
	private char[] optionList;
	private final int buttonNumber = 12;
	private Button[] keyboardButtons = new Button[buttonNumber];
	private Handler handler;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApp appState = (MyApp) getApplicationContext();
		appState.addActivity(this);
		setContentView(R.layout.spell_activity);

		setTitle("根据中文拼写出英文单词");

		System.out.println("so far so good");
		findView();
		handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				if (msg.what == -100) {
					gotoWrongAnswer();
					System.out.println("time out");
				} else
					countDownTextView.setText("" + msg.what);
			};
		};
		CountDownModel.getInstance().startCountDown(handler, 20);
		meaningView.setText(Constant.getCurrentWord().getMeanString());
		danciView.setText("");
	}

	private void findView() {
		danciView = (TextView) findViewById(R.id.spell_danciText);
		meaningView = (TextView) findViewById(R.id.spell_meaningText);
		countDownTextView = (TextView) findViewById(R.id.spell_countdown_text);

		Button backspaceButton;
		backspaceButton = (Button) findViewById(R.id.spell_back_btn);
		backspaceButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				String tmpString = danciView.getText().toString();
				if (tmpString != null && tmpString.length() != 0) {
					danciView.setText(tmpString.substring(0,
							tmpString.length() - 1) + "");
				}
			}
		});
		Button proButton;
		proButton = (Button) findViewById(R.id.spell_pronounce);
		proButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				speakEn();

			}
		});

		// buttons on the bottom
		Button resetButton, nextButton;
		resetButton = (Button) findViewById(R.id.rd_1);
		resetButton.setVisibility(View.GONE);
		resetButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// danciView.setText("");
			}
		});

		nextButton = (Button) findViewById(R.id.rd_2);
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				printCursor();

				// TODO

				if (danciView
						.getText()
						.toString()
						.trim()
						.equals(Constant.getCurrentWord().getNameString()
								.trim())) {

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
		});
	}

	private void nextWord() {
		if (Constant.getCurrentTicket().getReciteMode() == ReciteMode.ChnToEng) {

			// CountDownModel.getInstance().startCountDown(handler, 21);
			CountDownModel.countDown = 21;
			initKeyboardChars();
			freashButtons();
			setButtonsListener();
			danciView.setText(Constant.getCurrentWord().getNameString());
			danciView.setText("");
			meaningView.setText(Constant.getCurrentWord().getMeanString());

		} else if (Constant.getCurrentTicket().getReciteMode() == ReciteMode.ListenAndSpell) {
			finishSelf();
			Intent intent = new Intent(ChnToEngActivity.this,
					ChnToEngListenActivity.class);
			startActivity(intent);
		} else if (Constant.getCurrentTicket().getReciteMode() == ReciteMode.EngToChn) {
			finishSelf();
			Intent intent = new Intent(ChnToEngActivity.this,
					EngToChnActivity.class);
			startActivity(intent);
		} else if (Constant.getCurrentTicket().getReciteMode() == ReciteMode.EngToChnListen) {
			finishSelf();
			Intent intent = new Intent(ChnToEngActivity.this,
					EngToChnListenActivity.class);
			startActivity(intent);

		}
	}

	private void unitFinished() {

		Toast.makeText(ChnToEngActivity.this,
				" unit " + Constant.unit_number + " finished.", 1000).show();

		SharedPreferences preferences = getSharedPreferences(
				Constant.FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("" + Constant.unit_number, true);
		editor.commit();
		//
		finishSelf();
		Intent intent = new Intent(ChnToEngActivity.this,
				ChooseUnitActivity.class);
		startActivity(intent);
	}

	private void printCursor() {

		Toast.makeText(this,
				Constant.ticketListCursor + "-" + Constant.ticketList.size(),
				500).show();

	}

	private void speakEn() {
		CountDownModel.getInstance().stopCounting();
		try {
			Intent localIntent = new Intent(
					RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			localIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
					"free_form");
			localIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
			// localIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,
			// "Speech recognition demo");

			startActivityForResult(localIntent, 1010);
		} catch (Exception localActivityNotFoundException) {
			Toast.makeText(this, "you did not install google voice input", 500)
					.show();
			localActivityNotFoundException.printStackTrace();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1010 && (resultCode == -1)) {
			ArrayList<String> localArrayList = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

			StringBuilder localStringBuilder = new StringBuilder();
			String tmp = Constant.getCurrentWord().getNameString();
			if (tmp != null) {
				if (localArrayList.size() > 0) {
					int i;
					for (i = 0; i < localArrayList.size(); i++) {
						if (localArrayList.get(i).contains(tmp)) {
							localStringBuilder.append(tmp);
							break;
						}
					}

					if (i == localArrayList.size()) {
						localStringBuilder.append(localArrayList.get(0));
					}
				}
				danciView.setText(localStringBuilder.toString());
			}
		}
	}

	private void gotoWrongAnswer() {
		finishSelf();

		Constant.ticketList.add(Constant.getCurrentTicket());
		Intent intent = new Intent(ChnToEngActivity.this,
				WrongAnswerActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		initKeyboardChars();
		initButtons();
		freashButtons();
		setButtonsListener();
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// save infos
		System.out.println("save infos");
	}

	private void setButtonsListener() {
		for (int buttonI = 0; buttonI < keyboardButtons.length; buttonI++) {
			keyboardButtons[buttonI]
					.setOnClickListener(new btnListener(buttonI));
		}
	}

	private class btnListener implements OnClickListener {
		int index;

		public btnListener(int index) {
			super();
			this.index = index;
		}

		public void onClick(View arg0) {
			danciView.setText(danciView.getText().toString()
					+ keyboardButtons[index].getText());
		}
	}

	private char[] cutChars(char[] chars) {
		Arrays.sort(chars);
		int count = 0;
		for (int i = 1; i < chars.length; i++) {
			if (chars[i] == chars[i - 1]) {
				count++;
				for (int j = i; j + 1 < chars.length; j++) {
					chars[j] = chars[j + 1];
				}
				i--;
			}
			if (count > 40) {
				break;
			}
		}
		char[] tm = new char[buttonNumber];
		for (int j = 0; j < tm.length; j++) {
			tm[j] = chars[j];
		}
		System.out.println(Arrays.toString(tm));
		return tm.clone();
	}

	// generate keyboard button chars from current word
	private void initKeyboardChars() {

		char[] tmpchar = sortWordString(Constant.getCurrentWord()
				.getNameString());
		if (tmpchar.length >= buttonNumber) {
			// if the keyboard button is not enough for use
			tmpchar = cutChars(tmpchar);
		}
		optionList = new char[tmpchar.length];
		for (int i = 0; i < tmpchar.length; i++) {
			optionList[i] = tmpchar[i];
		}
	}

	private char[] sortWordString(String input) {
		System.out.println("input----" + input);
		char[] chars = input.toCharArray();
		Arrays.sort(chars);
		System.out.println(Arrays.toString(chars));
		return chars.clone();
	}

	private int fontsize = 30;

	private void initButtons() {
		RelativeLayout aLayout = (RelativeLayout) this
				.findViewById(R.id.relativeLayout1);
		RelativeLayout.LayoutParams relativeLayoutParams = null;
		int chk_id = 1000;
		int colCount = 4;
		int count = 0;
		for (int i = 0; count < buttonNumber; i++) {
			keyboardButtons[count] = new Button(this);
			keyboardButtons[count].setTextSize(fontsize);
			keyboardButtons[count].setId(chk_id += 10);
			relativeLayoutParams = new RelativeLayout.LayoutParams(100, 100);
			if (0 == i) {
				relativeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				relativeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			} else {
				relativeLayoutParams.addRule(RelativeLayout.ALIGN_LEFT,
						chk_id - 10);
				relativeLayoutParams.addRule(RelativeLayout.BELOW, chk_id - 10);
			}
			keyboardButtons[count].setLayoutParams(relativeLayoutParams);
			aLayout.addView(keyboardButtons[count]);
			count++;
			// ******************************************************
			for (int j = 1; j < colCount && count < buttonNumber; j++) {
				keyboardButtons[count] = new Button(this);
				keyboardButtons[count].setTextSize(fontsize);
				keyboardButtons[count].setId(chk_id + j);
				relativeLayoutParams = new RelativeLayout.LayoutParams(100, 100);
				relativeLayoutParams.addRule(RelativeLayout.RIGHT_OF, chk_id
						+ j - 1);
				relativeLayoutParams.addRule(RelativeLayout.ALIGN_TOP, chk_id
						+ j - 1);
				keyboardButtons[count].setLayoutParams(relativeLayoutParams);
				aLayout.addView(keyboardButtons[count]);
				count++;
			}
		}
	}

	private void freashButtons() {
		int i;
		for (i = 0; i < optionList.length; i++) {
			keyboardButtons[i].setText(optionList[i] + "");
			keyboardButtons[i].setVisibility(View.VISIBLE);
		}
		System.out.println("freshbuttons--------i==" + i);
		for (int j = i; j < keyboardButtons.length; j++) {
			keyboardButtons[j].setText("");
			keyboardButtons[j].setVisibility(View.INVISIBLE);
		}
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
		Intent intent = new Intent(ChnToEngActivity.this,
				ChooseUnitActivity.class);
		startActivity(intent);
		return super.onOptionsItemSelected(item);
	}

	private void finishSelf() {
		CountDownModel.getInstance().stopCounting();
		this.finish();
	}

}
