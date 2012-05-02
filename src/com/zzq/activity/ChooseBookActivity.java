package com.zzq.activity;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import utils.FileUtils;
import utils.ImgUtil;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ChooseBookActivity extends Activity {
	private LinkedList<Map<String, String>> data;
	private final String FILE_NAME = "fileName";
	private final String FILE_ID = "fileId";
	// private ViewFlipper flipper;
	private GestureDetector detector;
	private int height;
	private int width;
	private final int numberOfBooksPerPage = 6;
	private ScrollLayout workspace;
	private Handler handler;
	private boolean isFile = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MyApp appState = (MyApp) getApplicationContext();
		appState.addActivity(this);
		// ************************

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setType(2004);
		getWindow().setFormat(1);

		// ************************
		this.height = getWindowManager().getDefaultDisplay().getHeight();
		this.width = getWindowManager().getDefaultDisplay().getWidth();
		Data.WRATE = this.width / 320.0F;
		Data.HRATE = this.height / 480.0F;
		Data.height = height;
		Data.width = width;

		RelativeLayout welcomeRelativeLayout;
		welcomeRelativeLayout = new RelativeLayout(this);
		welcomeRelativeLayout.setBackgroundResource(R.drawable.welcome);
		setContentView(welcomeRelativeLayout);

		// ******************************************************
		AnimationLoader.load(this);
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				// TODO Auto-generated method stub

				if (isFile) {
					Constant.FILE_PATH = Constant.FILE_NAME + "/"
							+ data.get(msg.what).get(FILE_NAME);
					Constant.FILE_NAME = Constant.FILE_NAME + "_"
							+ data.get(msg.what).get(FILE_NAME);

					Intent intent = new Intent(ChooseBookActivity.this,
							ChooseUnitActivity.class);
					startActivity(intent);
				} else {
					Constant.FILE_NAME = data.get(msg.what).get(FILE_NAME);

					isFile = true;
					boolean isInited = initData();
					if (isInited) {
						loadData();
					}
				}
			}
		};

		boolean isInited = initData();
		if (isInited) {
			loadData();
		} else {
			Toast.makeText(this, "no file", 200).show();
			this.finish();
		}

	}

	private void loadData() {

		int rest = data.size() % numberOfBooksPerPage;
		int pageNum = data.size() / numberOfBooksPerPage + (rest == 0 ? 0 : 1);
		if (pageNum == 0) {
			pageNum = 1;
		}
		System.out.println(pageNum);
		pages = new Page[pageNum];
		for (int i = 0; i < pageNum && rest == 0; i++) {
			String[] bookNames = new String[numberOfBooksPerPage];
			for (int j = 0; j < bookNames.length; j++) {
				bookNames[j] = data.get(i * numberOfBooksPerPage + j).get(
						FILE_NAME);
			}
			pages[i] = new Page(this, numberOfBooksPerPage, bookNames, i,
					handler);
			pages[i].getCover();
			pages[i].initView();
			pages[i].setCover();
		}

		for (int i = 0; i < pageNum - 1 && rest != 0; i++) {
			String[] bookNames = new String[numberOfBooksPerPage];
			for (int j = 0; j < bookNames.length; j++) {
				bookNames[j] = data.get(i * numberOfBooksPerPage + j).get(
						FILE_NAME);
			}
			pages[i] = new Page(this, numberOfBooksPerPage, bookNames, i,
					handler);
			pages[i].getCover();
			pages[i].initView();
			pages[i].setCover();
		}

		if (rest != 0) {
			String[] bookNames = new String[rest];
			for (int j = 0; j < bookNames.length; j++) {
				bookNames[j] = data.get(
						(pageNum - 1) * numberOfBooksPerPage + j)
						.get(FILE_NAME);
			}
			pages[pageNum - 1] = new Page(this, rest, bookNames, pageNum - 1,
					handler);
			pages[pageNum - 1].getCover();
			pages[pageNum - 1].initView();
			pages[pageNum - 1].setCover();
		}

		View view = View.inflate(this, R.layout.library, null);
		setContentView(view);
		PageControlView pageControlView;
		this.workspace = (ScrollLayout) findViewById(R.id.ScrollLayoutTest);
		pageControlView = (PageControlView) findViewById(R.id.pageControl);
		initView(view);
		for (int i = 0; i < pageNum; i++) {
			this.workspace.addView(this.pages[i], this.width, this.height);
		}
		pageControlView.bindScrollLayout(workspace);
	}

	private void initView(View paramView) {
		((RelativeLayout) paramView.findViewById(R.id.library_title))
				.setLayoutParams(new RelativeLayout.LayoutParams(-1,
						(int) (45.0F * Data.HRATE)));

		ImageView localImageView;
		localImageView = (ImageView) findViewById(R.id.library_background);
		Bitmap bitmap = getBackground();
		localImageView.setImageBitmap(bitmap);
		localImageView.setBackgroundResource(R.drawable.library_back);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		VelocityTracker localVelocityTracker = VelocityTracker.obtain();
		localVelocityTracker.addMovement(event);
		localVelocityTracker.computeCurrentVelocity(1000);
		int j = (int) localVelocityTracker.getXVelocity();
		Log.e("ScrollLayout", "velocityX:" + j);
		if ((j > 600)) {
			Log.e("ScrollLayout", "snap left");
		}
		localVelocityTracker.recycle();
		localVelocityTracker = null;
		return this.detector.onTouchEvent(event);
	}

	private Page[] pages;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			startActivity(new Intent(ChooseBookActivity.this,
					MainActivity.class));
			this.finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	public Bitmap getBackground() {
		Bitmap localBitmap1 = ImgUtil.zoomBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.library_back), this.width,
				this.height - 45 * this.height / 480);

		Bitmap localBitmap2 = Bitmap.createBitmap(this.width,
				localBitmap1.getHeight(), Bitmap.Config.ARGB_4444);

		Bitmap localBitmap3 = ImgUtil.zoomBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.library_shelf),
				11 * this.width / 40, 11 * this.width / 200);

		Canvas localCanvas = new Canvas(localBitmap2);
		Paint localPaint = new Paint();
		localCanvas.drawBitmap(localBitmap3, 3 * this.width / 80, this.height
				/ 9 + 0.3F * this.width, localPaint);
		localCanvas.drawBitmap(localBitmap3, 57 * this.width / 160, this.height
				/ 9 + 0.3F * this.width, localPaint);
		localCanvas.drawBitmap(localBitmap3, 27 * this.width / 40, this.height
				/ 9 + 0.3F * this.width, localPaint);
		localCanvas.drawBitmap(localBitmap3, 3 * this.width / 80, 2
				* this.height / 5 + 0.3F * this.width, localPaint);
		localCanvas.drawBitmap(localBitmap3, 57 * this.width / 160, 2
				* this.height / 5 + 0.3F * this.width, localPaint);
		localCanvas.drawBitmap(localBitmap3, 27 * this.width / 40, 2
				* this.height / 5 + 0.3F * this.width, localPaint);
		localCanvas.save(8);
		localCanvas.restore();

		return localBitmap2;
	}

	private String[] getFiles(String folderName) {
		LinkedList<String> files = new LinkedList<String>();
		String filePathString = FileUtils.SDPATH_zzq + "/" + folderName;
		File dir = new File(filePathString);
		// File dir = new File(Constant.FILE_PATH );

		if (!dir.exists()) {
			Toast.makeText(this, "find find dir sdroot/zzq", Toast.LENGTH_LONG);
			dir.mkdir();
			System.out.println("find find dir sdroot/zzq");
		}
		File files_list = new File(filePathString);
		for (File f : files_list.listFiles()) {
			if (f.getName().contains(".xml")) {
				files.add(f.getName());
			}
		}

		String s[] = new String[files.size()];
		for (int i = 0; i < s.length; i++) {
			s[i] = files.get(i);
		}
		return s;
	}

	private String[] getFolders() {
		LinkedList<String> files = new LinkedList<String>();
		File dir = new File(FileUtils.SDPATH_zzq);
		if (!dir.exists()) {
			Toast.makeText(this, "find find dir sdroot/zzq", Toast.LENGTH_LONG);
			dir.mkdir();
			System.out.println("find find dir sdroot/zzq");
		}
		File files_list = new File(FileUtils.SDPATH_zzq);
		for (File f : files_list.listFiles()) {
			if (!f.isFile()) {
				files.add(f.getName());
			}
		}

		String s[] = new String[files.size()];
		for (int i = 0; i < s.length; i++) {
			s[i] = files.get(i);
		}
		return s;
	}

	private boolean initData() {
		data = new LinkedList<Map<String, String>>();
		// TODO Auto-generated method stub

		String[] files;
		if (isFile) {
			files = getFiles(Constant.FILE_NAME);
		} else {
			files = getFolders();
		}

		if (files.length == 0) {
			System.out.println("cant find files");
			Toast.makeText(this, "can not find files", Toast.LENGTH_LONG);
			return false;
		}

		Map<String, String> map;
		for (int i = 0; i < files.length; i++) {
			map = new HashMap<String, String>();
			System.out.println(files[i]);
			map.put(FILE_NAME, files[i]);
			map.put(FILE_ID, i + "");
			data.add(map);
		}
		System.out.println("huoqu wanbi ");
		return true;
	}

}
