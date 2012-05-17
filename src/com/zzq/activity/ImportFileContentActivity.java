package com.zzq.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import utils.FileUtils;
import Model.WordStructure;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ImportFileContentActivity extends ListActivity {
	private Button allEnButton, enAndChButton;
	private TextView textView;
	private String filenameString = "";
	private EditText saveFileEditText;
	private boolean isAllEng = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.import_file_content_activity);
		MyApp appState = (MyApp) getApplicationContext();
		appState.addActivity(this);

		filenameString = getIntent().getStringExtra("file_name");

		setTitle(FileUtils.SDPATH_YIJI_IMPORT + "" + filenameString);
		findView();

		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
		alertBuilder
				.setMessage("<-- Left is the txt file content \n-->Right is the preview-->");
		alertBuilder.setNegativeButton("Ok", null);
		alertBuilder.create().show();
	}

	private void setListAdapter(LinkedList<WordStructure> list) {

		ArrayList<Map<String, String>> tmpList = new ArrayList<Map<String, String>>();

		for (int i = 0; i < list.size() && i < 10; i++) {
			Map<String, String> tmpMap = new HashMap<String, String>();
			tmpMap.put("name", list.get(i).getNameString());
			tmpMap.put("mean", list.get(i).getMeanString());
			tmpList.add(tmpMap);
		}

		SimpleAdapter listAdapter = new SimpleAdapter(this, tmpList,
				android.R.layout.simple_list_item_2, new String[] { "name",
						"mean" }, new int[] { android.R.id.text1,
						android.R.id.text2 });
		setListAdapter(listAdapter);

	}

	private void print(String str) {
		Toast.makeText(this, str, 500).show();
	}

	private String prefixString = "sdfsdf";
	String fileContentString = "";

	private void findView() {

		FileUtils fileUtils = new FileUtils();
		fileContentString = fileUtils.readFileFrom_Yiji_import(filenameString);

		textView = (TextView) findViewById(R.id.content_textview);
		textView.setText(fileContentString);
		textView.setMovementMethod(new ScrollingMovementMethod());
		allEnButton = (Button) findViewById(R.id.rd_1);
		allEnButton.setText("纯英文");
		enAndChButton = (Button) findViewById(R.id.rd_2);
		enAndChButton.setText("英-中");

		allEnButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isAllEng = true;

				makeDialogPreview();
			}
		});

		enAndChButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isAllEng = false;
				makeDialogPreview();

			}
		});

		new Thread() {
			public void run() {
				LinkedList<String> danciList = ImportWord
						.getXmlList(fileContentString);
				wordStructures_scope_marco = getWordStructures(danciList);
				xmlList_scope_marco = ImportWord.getXmlList(
						wordStructures_scope_marco, 100);
				
				handler.sendEmptyMessage(0);

				isDone = true;
			};
		}.start();
		
		handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				
				setListAdapter(wordStructures_scope_marco);
				
			};
		};

	}

	private Handler handler ;
	private LinkedList<WordStructure> wordStructures_scope_marco;
	private LinkedList<String> xmlList_scope_marco;
	private boolean isDone = false;

	private void makeDialogPreview() {

		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
		alertBuilder.setCancelable(true);
		alertBuilder.setMessage("-->Right is the preview-->");
		alertBuilder.setNegativeButton("Preview",
				new AlertDialog.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO
						// isPreview = true;

						if (isAllEng) {

							LinkedList<String> danciList = ImportWord
									.getXmlListWithLengthLimit(
											fileContentString, 10);
							LinkedList<WordStructure> wordStructures = getWordStructures(danciList);

							setListAdapter(wordStructures);
						} else {
							// TODO
							LinkedList<String> xmlList = ImportWord.getXmlList(
									fileContentString, 100, 10);

							try {

								LinkedList<WordStructure> wordStructures = new Word()
										.getListFromXmlContent(xmlList.get(0));

								setListAdapter(wordStructures);

							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}

						}

					}
				});
		alertBuilder.setPositiveButton("Import",
				new AlertDialog.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO
						// isPreview = false;

						makeDialogToTypePrefix();
					}
				});
		alertBuilder.create().show();
	}

	private void makeDialogToTypePrefix() {

		saveFileEditText = new EditText(this);
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
		saveFileEditText.setText(filenameString.substring(0,
				filenameString.length() - 4));
		alertBuilder.setView(saveFileEditText);
		alertBuilder.setMessage("what's prefix for these files");
		alertBuilder.setCancelable(true);
		alertBuilder.setPositiveButton("Yes",
				new AlertDialog.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO
						prefixString = saveFileEditText.getText().toString();

						String dirPathString = FileUtils.SDPATH_zzq
								+ prefixString;
						File dir = new File(dirPathString);
						boolean excuted = false;
						if (dir.exists()) {
							AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
									ImportFileContentActivity.this);
							alertBuilder
									.setMessage("This folder already exists, do you want to overwrite?");
							alertBuilder.setPositiveButton("yes",
									new AlertDialog.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											excuteImport();
										}
									});
							alertBuilder.setNegativeButton("cancel", null);
							alertBuilder.create().show();

						} else {
							excuteImport();
						}

					}
				});
		alertBuilder.setNegativeButton("Cancel", null);
		alertBuilder.create().show();
	}

	private void excuteImport() {
		boolean isSuccess = false;
		if (isAllEng) {
			isSuccess = startImport_AllEng();
		} else {
			isSuccess = startImport_EngAndChn();
		}
		if (isSuccess) {
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
			alertBuilder
					.setMessage("Txt file has been converted to xml files and saved in \""
							+ prefixString + "\" folder.");
			alertBuilder.setNegativeButton("Ok", null);
			alertBuilder.create().show();
		}

	}

	private LinkedList<WordStructure> getWordStructures(
			LinkedList<String> danciList) {
		LinkedList<String> meaningList = new LinkedList<String>();

		LinkedList<WordStructure> wordStructures = new LinkedList<WordStructure>();

		for (int i = 0; i < danciList.size(); i++) {
			String meaning = SearchActivity.searchWordLocal(danciList.get(i),
					getAssets());
			if (meaning != null) {
				meaning = meaning.replace("\\n", "<br>");
				String[] meaningsSubString = meaning.split("<br>");
				int size = meaningsSubString.length;

				if (size >= 1) {
					meaning = meaningsSubString[size - 1];
				}
				meaningList.add(meaning);
				WordStructure wordStructure = new WordStructure(
						danciList.get(i), meaning);
				wordStructures.add(wordStructure);
			}

		}
		for (int i = 0; i < wordStructures.size(); i++) {
			System.out.println(wordStructures.get(i));
		}

		return wordStructures;

	}

	private boolean startImport_EngAndChn() {

		try {
			LinkedList<String> xmlList = ImportWord.getXmlList(
					fileContentString, 100);
			ImportWord.writeToFileFromXmlList(xmlList, prefixString);
		} catch (Exception e) {
			e.printStackTrace();
			print("fail to import" + e.getStackTrace());
			return false;
		}
		return true;
	}

	private boolean startImport_AllEng() {

		try {

			while (!isDone) {
			}

			setListAdapter(wordStructures_scope_marco);
			ImportWord
					.writeToFileFromXmlList(xmlList_scope_marco, prefixString);

		} catch (Exception e) {
			e.printStackTrace();

			print("fail to import" + e.getStackTrace());
			return false;
		}
		return true;

	}

}
