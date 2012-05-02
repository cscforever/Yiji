package com.zzq.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zzq.activity.ScrollLayout.OnScreenChangeListener;

public class PageControlView extends LinearLayout {
	private Context context;
	private int count;

	public PageControlView(Context context) {
		super(context);
		this.context = context;
	}

	public PageControlView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public void bindScrollLayout(ScrollLayout scrollLayout) {
		this.count = scrollLayout.getChildCount();
		Log.v("=count==", count + "=");
		//
		generatePageControl(0);
		scrollLayout.setOnScreenChangeListener(new OnScreenChangeListener() {

			@Override
			public void onScreenChange(int currentIndex) {

				generatePageControl(currentIndex);
			}
		});
	}

	private void generatePageControl(int currentIndex) {
		this.removeAllViews();
		for (int i = 0; i < this.count; i++) {
			Log.v("=i==", i + "=");
			ImageView imageView = new ImageView(context);
			if (i == currentIndex) {
				imageView.setImageResource(R.drawable.point_on);
			} else {
				imageView.setImageResource(R.drawable.point_off);
			}
			this.addView(imageView);
		}
	}
}
