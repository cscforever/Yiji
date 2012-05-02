package com.zzq.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.WordStructure;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ChooseUnitActivity extends ListActivity {

	private final int numberOfWordsPerUnit = 5;
	private int unitPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MyApp appState = (MyApp) getApplicationContext();
		appState.addActivity(this);
		setContentView(R.layout.choose_unit_activity);
		// =============================
		setTitle(Constant.FILE_NAME);
	}

	@Override
	protected void onResume() {
		boolean isRead = readWordListFromFile();
		if (isRead) {
			setListAdapter();
		}

		super.onResume();
	}

	private boolean readWordListFromFile() {
		boolean retValue = false;
		try {
			if ((Constant.totalWordList = new Word()
					.getListFromXmlFile(Constant.FILE_PATH)) == null) {
				System.out.println("readWordListFromFile");
				retValue = false;
			} else {
				retValue = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retValue;
	}

	// private View itemView;
	private boolean done;

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		unitPosition = position;
		// itemView = v;
		// TODO*************************************************************
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
				ChooseUnitActivity.this);
		alertBuilder.setCancelable(true);
		String positiveString = "";
		SharedPreferences preferences = getSharedPreferences(
				Constant.FILE_NAME, Context.MODE_PRIVATE);
		done = preferences.getBoolean("" + (unitPosition + 1), false);

		if (done) {
			positiveString = getString(R.string.undone);
			alertBuilder.setMessage("you have learned this unit,\n"
					+ getString(R.string.unitTitle));

		} else {
			positiveString = getString(R.string.done);
			alertBuilder.setMessage("new unit,\n"
					+ getString(R.string.unitTitle));
		}

		alertBuilder.setPositiveButton(positiveString,
				new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						markDone(!done);
					}
				});

		alertBuilder.setNegativeButton(getString(R.string.startRecite),
				new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						startLearn(unitPosition);
					}
				});

		alertBuilder.create().show();

	}

	private void markDone(boolean is) {
		SharedPreferences preferences = getSharedPreferences(
				Constant.FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean((unitPosition + 1) + "", is);
		editor.commit();
		setListAdapter();
	}

	private void startLearn(int position) {
		List<WordStructure> tempList = new ArrayList<WordStructure>();

		for (int i = 0; i < numberOfWordsPerUnit
				&& position * numberOfWordsPerUnit + i < Constant.totalWordList
						.size(); i++) {
			tempList.add(Constant.totalWordList.get(position
					* numberOfWordsPerUnit + i));
		}
		Constant.totalWordList = null;
		Constant.wordList = tempList;

		Constant.unit_number = position + 1;
		Intent intent = new Intent(ChooseUnitActivity.this,
				ViewUnitWordsActivity.class);
		startActivity(intent);
	}

	private void setListAdapter() {

		ArrayList<Map<String, String>> tmpList = new ArrayList<Map<String, String>>();
		int size = Constant.totalWordList.size();
		int unitNumber = 0;

		if (size % numberOfWordsPerUnit == 0) {
			unitNumber = size / numberOfWordsPerUnit;
		} else {
			unitNumber = size / numberOfWordsPerUnit + 1;
		}

		SharedPreferences preferences2 = getSharedPreferences(
				Constant.FILE_NAME, Context.MODE_PRIVATE);

		for (int i = 0; i < unitNumber; i++) {
			Map<String, String> tmpMap = new HashMap<String, String>();
			String screenshortString = "("
					+ Constant.totalWordList.get(i * numberOfWordsPerUnit)
							.getNameString() + ")";

			boolean is = preferences2.getBoolean("" + (i + 1), false);
			if (is) {
				tmpMap.put("unit", "*UNIT " + (i + 1) + screenshortString);
			} else {
				tmpMap.put("unit", "UNIT " + (i + 1) + screenshortString);
			}
			tmpList.add(tmpMap);
		}

		SimpleAdapter listAdapter = new SimpleAdapter(this, tmpList,
				android.R.layout.simple_list_item_checked,
				new String[] { "unit" }, new int[] { android.R.id.text1 });
		setListAdapter(listAdapter);

	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		boolean retValue;
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(ChooseUnitActivity.this,
					ChooseBookActivity.class);
			startActivity(intent);
			this.finish();
			retValue = true;
		} else {
			retValue = super.onKeyUp(keyCode, event);
		}
		return retValue;
	}

}
