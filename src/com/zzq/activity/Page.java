package com.zzq.activity;

import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class Page extends FrameLayout implements View.OnLongClickListener {
	int bookNum = 6;
	private RelativeLayout.LayoutParams[] lps;
	private RelativeLayout[] relative;
	private BookView[] bookviews;
	private ChooseBookActivity context;

	public Page(ChooseBookActivity context, int bookNum,String[] bookNames,int id,Handler handler) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.bookNum = bookNum;
		lps = new RelativeLayout.LayoutParams[bookNum];
		relative = new RelativeLayout[bookNum];
		bookviews = new BookView[bookNum];
		// System.out.println("page conductor");
		for (int i = 0; i < bookNum; i++) {
			this.bookviews[i] = new BookView(context,bookNames[i],id*6+i,handler);
		}
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		return false;
	}

	public void getCover() {
		for (int i = 0; i < bookNum; i++) {
			this.bookviews[i].getCover();
		}
	}

	public void setCover() {
		for (int i = 0; i < bookNum; i++) {
			this.bookviews[i].setCover();
		}
	}

	public void initView() {
		for (int i = 0; i < bookNum; i++) {
			this.bookviews[i].initView();
			this.lps[i] = new RelativeLayout.LayoutParams(-2, -2);
			this.bookviews[i].setParent(this);
			this.bookviews[i].getBook().setTag(Integer.valueOf(i));
			this.bookviews[i].getBook().setOnLongClickListener(this);
			this.bookviews[i].getView().setId(i);
			this.relative[i] = new RelativeLayout(this.context);
		}
		for (int j = 0; j < 2; j++) {
			if ((0 + j * 3) >= bookNum) {
				break;
			}
			this.lps[(0 + j * 3)].leftMargin = (3 * Data.width / 40);// 24
			this.lps[(0 + j * 3)].topMargin = ((1 - j) * Data.height / 9 + 2 * (j * Data.height) / 5);
			this.bookviews[(0 + j * 3)].setButtonPosition(true);
			this.relative[(0 + j * 3)].addView(this.bookviews[(0 + j * 3)]
					.getView(), this.lps[(0 + j * 3)]);
			addView(this.relative[(0 + j * 3)]);
			// -----------------------------------------
			if ((1 + j * 3) >= bookNum) {
				break;
			}
			this.lps[(1 + j * 3)].leftMargin = (37 * Data.width / 94);// 188
			this.lps[(1 + j * 3)].topMargin = ((1 - j) * Data.height / 9 + 2 * (j * Data.height) / 5);
			this.bookviews[(1 + j * 3)].setButtonPosition(true);
			this.relative[(1 + j * 3)].addView(this.bookviews[(1 + j * 3)]
					.getView(), this.lps[(1 + j * 3)]);
			addView(this.relative[(1 + j * 3)]);
			// -----------------------------------------
			if ((2 + j * 3) >= bookNum) {
				break;
			}
			this.lps[(2 + j * 3)].leftMargin = (2 * Data.width / 7 + 17 * Data.width / 40);
			this.lps[(2 + j * 3)].topMargin = ((1 - j) * Data.height / 9 + 2 * (j * Data.height) / 5);
			// this.bookviews[(2 + j * 3)].setButtonPosition(false);
			this.relative[(2 + j * 3)].addView(this.bookviews[(2 + j * 3)]
					.getView(), this.lps[(2 + j * 3)]);
			addView(this.relative[(2 + j * 3)]);
		}

	}

}
