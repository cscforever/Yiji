package com.zzq.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import utils.FileUtils;
import utils.WordUrl;
import Model.WordStructure;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import dict.LocalDictReaderContainer;

public class SearchActivity extends Activity {
	// private boolean play = false;
	// CheckBox checkBox = null;
	Handler handler;
	private Button proButton, searchButton, addButton;
	private AutoCompleteTextView editText;
	private TextView textView;
	// private Map<String, String> newWordMap;
	private WordStructure newWordStructure;
	private String danci = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity);
		MyApp appState = (MyApp) getApplicationContext();
		appState.addActivity(this);
		findView();

	}

	public static String searchWordLocal(String str, AssetManager manager) {
		LocalDictReaderContainer dictReader = null;

		String LOCAL_DICT_DEFAULT_DIR = "dict_deluxe";
		String LOCAL_DICT_SDCARD_DIR = "/sdcard/yd_lib/";
		String retValString = null;
		try {
			dictReader = new LocalDictReaderContainer(manager,
					LOCAL_DICT_DEFAULT_DIR, LOCAL_DICT_SDCARD_DIR);
			retValString = dictReader.getDefinition(str.trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retValString;
	}

	private String generateFileName() {

		String retValString = null;

		int fileMaxLength = 8000;
		String fileNameString = "生词本";

		File dir = new File(FileUtils.SDPATH_zzq + fileNameString);

		long lastModified = 0;
		File destinationFile = null;

		for (File file2 : dir.listFiles()) {

			if (lastModified < file2.lastModified()) {

				lastModified = file2.lastModified();
				destinationFile = file2;
			}

		}

		if (destinationFile != null
				&& new FileUtils().readFileFrom(destinationFile).length() < fileMaxLength) {
			retValString = "生词本/" + destinationFile.getName();

		} else {
			String fileName = "生词本/" + DateFormat.format("MM-dd", new Date())
					+ ".xml";
			retValString = fileName;
		}

		return retValString;

	}

	private void addToNewWordList(String danci, String meaning) {
		System.out.println("add to new word list" + danci + meaning);

		String fileName = generateFileName();
		String outXml = "";
		try {
			File file = new File(FileUtils.SDPATH_zzq + fileName);
			if (!file.exists()) {
				if (!file.createNewFile()) {
					Toast.makeText(this, "fail to create file",
							Toast.LENGTH_SHORT).show();
					return;
				}
				outXml = "" + "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
						+ "<wordsList>";
				outXml += "\n<word>\n<danci>\n" + danci
						+ "\n</danci>\n<meaning>\n" + meaning
						+ "\n</meaning>\n</word>\n";
				outXml += "\n</wordsList>";
			} else {
				List<WordStructure> wordList = new Word()
						.getListFromXmlFile(fileName);

				if (newWordStructure != null
						&& newWordStructure.getMeanString() != null
						&& newWordStructure.getNameString() != null
						&& newWordStructure.getMeanString().length() > 0
						&& newWordStructure.getNameString().length() > 0) {

					boolean isExist = false;
					for (WordStructure wordStructure__ : wordList) {
						if (wordStructure__.getNameString().equals(
								newWordStructure.getNameString())) {
							isExist = true;
							break;
						}
					}

					if (isExist) {
						Toast.makeText(this, "already exists!", 500).show();
						return;

					} else {
						wordList.add(newWordStructure);
						outXml = ""
								+ "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
								+ "<wordsList>";

						for (WordStructure wordStructure : wordList) {
							outXml += "\n<word>\n<danci>\n"
									+ wordStructure.getNameString()
									+ "\n</danci>\n<meaning>\n"
									+ wordStructure.getMeanString()
									+ "\n</meaning>\n</word>\n";
						}
						outXml += "\n</wordsList>";
					}

				} else {
					Toast.makeText(this, "saving failed!", 500).show();
					return;
				}
			}
			System.out.println(outXml);

			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(file), "utf-8");
			writer.write(outXml);
			if (writer != null) {
				Toast.makeText(SearchActivity.this, "file saved",
						Toast.LENGTH_SHORT).show();
				System.out.println("write to sd");
				writer.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String[] getSuggests(String s) {
		if (s.length() == 0) {
			return new String[] { "" };
		}
		LocalDictReaderContainer dictReader = null;
		AssetManager manager = getAssets();
		System.out.println("hello world");
		String LOCAL_DICT_DEFAULT_DIR = "dict_deluxe";
		String LOCAL_DICT_SDCARD_DIR = "/sdcard/yd_lib/";
		try {
			dictReader = new LocalDictReaderContainer(manager,
					LOCAL_DICT_DEFAULT_DIR, LOCAL_DICT_SDCARD_DIR);
			String[] str1 = dictReader.getSuggest(s, 1);
			return str1;
			// System.out.println(str1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private ArrayAdapter<String> adapter;
	private ProgressBar progressBar;
	private String suggestString = "";

	private void findView() {
		proButton = (Button) findViewById(R.id.search_pro);
		addButton = (Button) findViewById(R.id.search_addToNewWords);
		editText = (AutoCompleteTextView) findViewById(R.id.search_edit);
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				suggestString = null;

				System.out.println(".............text changed");
				String suggests[] = getSuggests(editText.getText().toString());
				if (suggests == null) {
					System.out.println("null     suggest");
					suggests = new String[1];
					suggests[0] = editText.getText().toString();
				} else {
					suggestString = suggests[0];
				}
				System.out.println(Arrays.toString(suggests));
				adapter = new ArrayAdapter<String>(SearchActivity.this,
						android.R.layout.simple_dropdown_item_1line, suggests);
				editText.setAdapter(adapter);
				SearchActivity.this.editText.setThreshold(0);
				// TODO
				editText.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						if (suggestString != null) {
							findMp3File(suggestString);

						}

					}

				});
				searchButtonPressed(false);

			}
		});

		textView = (TextView) findViewById(R.id.search_textview);
		textView.setMovementMethod(new ScrollingMovementMethod());

		progressBar = (ProgressBar) findViewById(R.id.progressBar1);

		searchButton = (Button) findViewById(R.id.search_cls);
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				searchButtonPressed(true);
			}
		});

		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				System.out.println("add");
				addButton.setClickable(false);

				if (newWordStructure != null) {
					String danci = newWordStructure.getNameString();
					String meaning = newWordStructure.getMeanString();
					if (danci.length() > 0 && meaning.length() > 0) {
						addToNewWordList(danci, meaning);
					}
				}

				addButton.setClickable(true);
			}
		});

		proButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String tmp = editText.getText().toString();
				PronouceCombined.pronouce(tmp, SearchActivity.this);
			}
		});

		handler = new Handler() {
			public void handleMessage(android.os.Message msg) {

				searchButton.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);

				if (meaning != null) {
					textView.setText(Html.fromHtml(meaning));
				}

			};
		};
	}

	private void searchButtonPressed(boolean isWebSearch) {
		String meaningForDisPlay = "";

		meaningForDisPlay = "";
		textView.setText("");
		danci = editText.getText().toString();
		if (danci.length() != 0) {
			String meaning = searchWordLocal(danci, getAssets());
			if (meaning != null) {
				// System.out.println(meaning);
				meaning = meaning.replace("\\n", "<br>");
				meaningForDisPlay = meaning;
				textView.setText(Html.fromHtml(meaning));

				// newWordMap = new HashMap<String, String>();
				// newWordMap.put("danci", danci);

				String[] meaningsSubString = meaningForDisPlay.split("<br>");
				int size = meaningsSubString.length;
				System.out.println("size  = " + size);

				if (size - 1 >= 0) {
					// newWordMap.put("meaning", meaningsSubString[size - 1]);

					newWordStructure = new WordStructure(danci,
							meaningsSubString[size - 1]);

				} else {
					// newWordMap.put("meaning", meaningForDisPlay);
					newWordStructure = new WordStructure(danci,
							meaningForDisPlay);
				}

			} else {
				meaningForDisPlay = "cant find definition in local dict";
				textView.setText("cant find definition in local dict");
			}
		}

		if (isWebSearch) {

			NetworkInfo localNetworkInfo = ((ConnectivityManager) SearchActivity.this
					.getSystemService("connectivity")).getActiveNetworkInfo();

			if (localNetworkInfo == null || !localNetworkInfo.isConnected() // 网络是否已经连接
			) {
				Toast.makeText(SearchActivity.this, "no network", 200).show();
			} else {
				danci = editText.getText().toString();
				if (danci.length() != 0) {
					// Intent intent = new Intent(SearchActivity.this,
					// SearchWordLoadingActivity.class);
					// intent.putExtra("danci", danci);
					// startActivityForResult(intent, 0);

					progressBar.setVisibility(View.VISIBLE);
					searchButton.setVisibility(View.GONE);

					new Thread() {
						public void run() {
							meaning = new WordUrl(danci, handler).getMeaning();
							handler.sendEmptyMessage(-1);
						};
					}.start();

					if (suggestString != null) {
						findMp3File(suggestString);
					}

				}
			}

		}

	}

	private String meaning;
	private boolean isFinished = true;

	private void findMp3File(String wordName) {

		if (isFinished) {
			isFinished = false;
			FetchMp3Thread thread = new FetchMp3Thread();
			thread.wordName = wordName;
			thread.start();

			while (!thread.isFinished) {
			}
			isFinished = true;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 9) {
			String meaning = data.getExtras().getString("meaning");
			textView.setText(Html.fromHtml(meaning));

		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
