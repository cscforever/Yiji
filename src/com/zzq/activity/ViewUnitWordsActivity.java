package com.zzq.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import Model.ReciteMode;
import Model.Ticket;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ViewUnitWordsActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MyApp appState = (MyApp) getApplicationContext();
		appState.addActivity(this);
		setContentView(R.layout.view_unit_words_activity);

		// *******************************
		// SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		// int unit_number = preferences.getInt(Constant.UNIT_NUMBER, -111);
		// setTitle("UNIT " + unit_number);
		setTitle("UNIT " + Constant.unit_number);

		// *******************************
		setListAdapter();
		findView();
	}

	private void findView() {

		Button startButton = (Button) findViewById(R.id.unit_words_start_button);
		startButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (Constant.wordList.size() > 4) {
					if (Constant.getCurrentTicket().getReciteMode() == ReciteMode.ChnToEng
							|| Constant.getCurrentTicket().getReciteMode() == ReciteMode.ListenAndSpell) {
						startNewActivity(ChnToEngActivity.class);
					} else {
						startNewActivity(EngToChnActivity.class);
					}
				} else {
					Toast.makeText(ViewUnitWordsActivity.this,
							"not enough words", 500).show();
				}
			}
		});

	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
		}
		return super.onKeyUp(keyCode, event);
	}

	private void startNewActivity(Class<?> arg0) {
		Intent intent = new Intent(ViewUnitWordsActivity.this, arg0);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		setListAdapter();
		initTicketListFromWordList();

		super.onResume();
	}

	private void initTicketListFromWordList() {

		if (Constant.ticketList == null) {
			Constant.ticketList = new ArrayList<Ticket>();
		}

		Constant.ticketList.clear();
		Constant.ticketListCursor = 0;
		Random random = new Random();
		for (int i = 0; i < Constant.wordList.size(); i++) {

			Ticket ticket4 = new Ticket(i, ReciteMode.ListenAndSpell);
			Ticket ticket3 = new Ticket(i, ReciteMode.EngToChnListen);
			Ticket ticket2 = new Ticket(i, ReciteMode.EngToChn);
			Ticket ticket = new Ticket(i, ReciteMode.ChnToEng);

			if (Constant.ticketList.size() == 0) {
				Constant.ticketList.add(ticket4);
				Constant.ticketList.add(ticket3);
				Constant.ticketList.add(ticket2);
				Constant.ticketList.add(ticket);

			} else {
				Constant.ticketList.add(
						random.nextInt(Constant.ticketList.size()), ticket4);
				Constant.ticketList.add(
						random.nextInt(Constant.ticketList.size()), ticket3);
				Constant.ticketList.add(
						random.nextInt(Constant.ticketList.size()), ticket2);
				Constant.ticketList.add(
						random.nextInt(Constant.ticketList.size()), ticket);
			}

		}

	}

	private void setListAdapter() {

		ArrayList<Map<String, String>> tmpList = new ArrayList<Map<String, String>>();

		for (int i = 0; i < Constant.wordList.size(); i++) {
			Map<String, String> tmpMap = new HashMap<String, String>();
			tmpMap.put("meaning", Constant.wordList.get(i).getMeanString());
			tmpMap.put("danci", Constant.wordList.get(i).getNameString());
			tmpList.add(tmpMap);
		}

		SimpleAdapter listAdapter = new SimpleAdapter(this, tmpList,
				android.R.layout.simple_list_item_2, new String[] { "danci",
						"meaning" }, new int[] { android.R.id.text1,
						android.R.id.text2 });
		setListAdapter(listAdapter);
	}

}
