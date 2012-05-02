package com.zzq.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import utils.FileUtils;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ChooseImportFileActivity extends ListActivity {
	private String[] filesStrings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MyApp appState = (MyApp) getApplicationContext();
		appState.addActivity(this);
		setContentView(R.layout.choose_unit_activity);
		// =============================

		setTitle(FileUtils.SDPATH_YIJI_IMPORT);

	}

	@Override
	protected void onResume() {

		if (getFiles()) {
			if (filesStrings != null && filesStrings.length > 0) {
				setListAdapter();
			}

			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
			alertBuilder.setMessage("Choose a txt file in \""
					+ FileUtils.SDPATH_YIJI_IMPORT
					+ "\" folder and convert it into xml file.");
			alertBuilder.setNegativeButton("Ok", null);
			alertBuilder.create().show();

		} else {
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
			alertBuilder.setMessage("No file.");
			alertBuilder.setNegativeButton("Ok",
					new AlertDialog.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							ChooseImportFileActivity.this.finish();
						}
					});
			alertBuilder.create().show();
		}

		super.onResume();
	}

	private int filePos;

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		filePos = position;
		Intent intent = new Intent(this, ImportFileContentActivity.class);
		intent.putExtra("file_name", filesStrings[filePos]);
		startActivity(intent);
		this.finish();

	}

	private void setListAdapter() {
		String file_keyString = "file";
		ArrayList<Map<String, String>> tmpList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < filesStrings.length; i++) {
			Map<String, String> tmpMap = new HashMap<String, String>();
			tmpMap.put(file_keyString, filesStrings[i]);
			tmpList.add(tmpMap);
		}

		SimpleAdapter listAdapter = new SimpleAdapter(this, tmpList,
				android.R.layout.simple_list_item_1,
				new String[] { file_keyString },
				new int[] { android.R.id.text1 });
		setListAdapter(listAdapter);

	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		boolean retValue;
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(ChooseImportFileActivity.this,
					MainActivity.class);
			startActivity(intent);
			this.finish();
			retValue = true;
		} else {
			retValue = super.onKeyUp(keyCode, event);
		}
		return retValue;
	}

	private boolean getFiles() {
		LinkedList<String> files = new LinkedList<String>();
		File dir = new File(FileUtils.SDPATH_YIJI_IMPORT);
		if (!dir.exists()) {
			Toast.makeText(this, "find find dir sdroot/yiji/import",
					Toast.LENGTH_LONG);
			dir.mkdir();
			return false;
		}

		File files_list = new File(FileUtils.SDPATH_YIJI_IMPORT);
		if (files_list.listFiles().length == 0) {
			return false;
		}
		for (File f : files_list.listFiles()) {

			String fileNameString = f.getName();
			int length = fileNameString.length();
			if (length > 4
					&& fileNameString.substring(length - 3, length)
							.equalsIgnoreCase("txt")) {
				files.add(f.getName());
			}
		}

		String s[] = new String[files.size()];
		for (int i = 0; i < s.length; i++) {
			s[i] = files.get(i);
		}
		filesStrings = s.clone();
		return true;
	}

}
