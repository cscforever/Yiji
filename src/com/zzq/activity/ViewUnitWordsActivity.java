package com.zzq.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import Model.ReciteMode;
import Model.Ticket;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
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

	LinkedList<String> wordToDelArray = new LinkedList<String>();

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

		if (position < Constant.wordList.size()) {
			
			String currentwordString = Constant.wordList.get(position)
					.getNameString();
			if (wordToDelArray.contains(currentwordString)) {
				
				for (int i = 0; i < wordToDelArray.size(); i++) {
					if (wordToDelArray.get(i).equals(  currentwordString )) {
						wordToDelArray.remove(currentwordString);
						break;
					}
				}
				
			} else {
				wordToDelArray.add(currentwordString);
			}
			
			setListAdapter();
			
		}
		
	}

	private void findView() {

		Button startButton = (Button) findViewById(R.id.unit_words_start_button);
		startButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				for (int i = 0; i < wordToDelArray.size() ; i++) {
					String tmp = wordToDelArray.get(i);
					for (int j = 0; j < Constant.wordList.size(); j++) {
						if (Constant.wordList.get(j).getNameString().equals(tmp) ) {
							Constant.wordList.remove(j);
						}
					}
				}
				
				initTicketListFromWordList();

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

		FetchMp3Thread thread = new FetchMp3Thread();
		thread.isBunch = true;
		thread.start();

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
			String tempString = Constant.wordList.get(i).getNameString();
			if (wordToDelArray.contains(tempString)) {
				tmpMap.put("danci", "--" + tempString);
			} else {
				tmpMap.put("danci", tempString);
			}
			tmpMap.put("meaning", Constant.wordList.get(i).getMeanString());
			tmpList.add(tmpMap);
		}

		SimpleAdapter listAdapter = new SimpleAdapter(this, tmpList,
				android.R.layout.simple_list_item_2, new String[] { "danci",
						"meaning" }, new int[] { android.R.id.text1,
						android.R.id.text2 });
		setListAdapter(listAdapter);
	}

}
