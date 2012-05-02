package com.zzq.activity;

import Model.ReciteMode;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WrongAnswerActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApp appState = (MyApp) getApplicationContext();
		appState.addActivity(this);
		setContentView(R.layout.spell_activity);
		setTitle("你选错了.");
		
		findView();
		Speak.init(Constant.getCurrentWord().getNameString(), this);

	}

	private void printCursor() {

		Toast.makeText(this,
				Constant.ticketListCursor + "-" + Constant.ticketList.size(),
				500).show();

	}

	private void findView() {
		TextView danciView;

		danciView = (TextView) findViewById(R.id.spell_danciText);
		String currentWordNameString = Constant.getCurrentWord()
				.getNameString();
		if (currentWordNameString == null) {
			currentWordNameString = "null";
		}
		danciView.setText(currentWordNameString);

		// *********************************
		TextView meaningView;
		meaningView = (TextView) findViewById(R.id.spell_meaningText);
		String currentWordMeaningString = Constant.getCurrentWord()
				.getMeanString();
		if (currentWordMeaningString == null) {
			currentWordMeaningString = "null";
		}
		meaningView.setText(currentWordMeaningString);
		// *********************************
		TextView countDownTextView;

		countDownTextView = (TextView) findViewById(R.id.spell_countdown_text);
		countDownTextView.setVisibility(View.GONE);

		Button backspaceButton;
		backspaceButton = (Button) findViewById(R.id.spell_back_btn);
		backspaceButton.setVisibility(View.GONE);

		Button proButton;
		proButton = (Button) findViewById(R.id.spell_pronounce);
		proButton.setBackgroundResource(R.drawable.pro_bg);
		proButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Speak.speak(Constant.getCurrentWord().getNameString());

			}
		});

		// buttons on the bottom
		Button resetButton, nextButton;
		resetButton = (Button) findViewById(R.id.rd_1);
		resetButton.setVisibility(View.GONE);
		nextButton = (Button) findViewById(R.id.rd_2);
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				printCursor();
				// TODO
				finishSelf();
				if (Constant.getCurrentTicket().getReciteMode() == ReciteMode.ChnToEng
						|| Constant.getCurrentTicket().getReciteMode() == ReciteMode.ListenAndSpell) {
					Intent intent = new Intent(WrongAnswerActivity.this,
							ChnToEngActivity.class);
					startActivity(intent);

				} else {
					Intent intent = new Intent(WrongAnswerActivity.this,
							EngToChnActivity.class);
					startActivity(intent);
				}

			}
		});

	}

	private void finishSelf() {
		Speak.stop();
		this.finish();
	}

	@Override
	protected void onPause() {
		super.onPause();
		System.out.println("save infos");
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

}
