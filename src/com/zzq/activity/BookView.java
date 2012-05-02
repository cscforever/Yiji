package com.zzq.activity;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BookView implements View.OnClickListener {
//	private static final int READY = 1;
	private Bitmap background;
	private ImageView book;
	private Page page;
	private final String sdRoot = Environment.getExternalStorageDirectory()
			.getPath();
	private View view;
	ChooseBookActivity context;
	String text;
	int id;
	Handler handler;

	public BookView(ChooseBookActivity context, String text, int id,
			Handler handler) {
		super();
		// System.out.println("book conducktor");
		this.context = (ChooseBookActivity) context;
		this.text = text;
		this.id = id;
		this.handler = handler;
	}

	public void appear(View paramView) {
		paramView.setVisibility(0);
		paramView.startAnimation(AnimationLoader.inAnim);
	}

	public void disappear(View paramView) {
		if ((paramView != null) && (paramView.getVisibility() == 0)) {
			paramView.startAnimation(AnimationLoader.outAnim);
			paramView.setVisibility(8);
		}
	}

	public ImageView getBook() {
		return this.book;
	}

	public BookView getBookView() {
		return this;
	}

	public void getCover() {
		Bitmap localBitmap = Bitmap.createBitmap((int) (0.2125F * Data.width),
				(int) (0.3F * Data.width), Bitmap.Config.ARGB_4444);
		Canvas localCanvas = new Canvas(localBitmap);
		try {
			localCanvas.drawBitmap(ImgUtil.zoomBitmap(BitmapFactory
					.decodeStream(context.getResources().getAssets().open(
							"nce11.png")), localBitmap.getWidth(), localBitmap
					.getHeight()), 0.0F, 0.0F, new Paint());
			localCanvas.save(31);
			localCanvas.restore();
			this.background = localBitmap;
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		}
	}

	public void setParent(Page page) {
		this.page = page;
	}

	public Page getParent() {
		return this.page;
	}

	public View getView() {
		return this.view;
	}

	public void initView() {
		try {
			this.view = View.inflate(this.context, R.layout.book, null);
			this.view.setBackgroundColor(Color.TRANSPARENT);
			this.book = ((ImageView) this.view.findViewById(R.id.book_book));
			this.book.setFocusable(true);
			this.book.setOnClickListener(this);
			this.book
					.setImageBitmap(BitmapFactory.decodeStream(this.context
							.getResources().getAssets().open(
									"default_book_cover.png")));
			TextView tv = (TextView) view.findViewById(R.id.book_textView1);
			tv.setText(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		System.out.println("onclick");
		handler.sendEmptyMessage(id);

	}

	public void setCover() {
		this.book.setImageBitmap(this.background);
	}

	public void setButtonPosition(boolean paramBoolean) {
		if (!paramBoolean) {
			RelativeLayout.LayoutParams localLayoutParams1 = new RelativeLayout.LayoutParams(
					-2, -2);
			RelativeLayout.LayoutParams localLayoutParams2 = new RelativeLayout.LayoutParams(
					(int) (82.0F * Data.WRATE), (int) (35.0F * Data.HRATE));
			localLayoutParams1.leftMargin = (2 * Data.width / 7);
			localLayoutParams2.addRule(7, this.book.getId());
			localLayoutParams2.addRule(3, this.book.getId());
			localLayoutParams2.topMargin = (int) (11.0F * Data.WRATE);
			this.book.setLayoutParams(localLayoutParams1);
		}
	}
}
