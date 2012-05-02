package com.zzq.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

public class MyApp extends Application {

	private List<Activity> mainActivity = new ArrayList<Activity>();

	public List<Activity> getMainActivity() {
		return mainActivity;
	}

	public void addActivity(Activity act) {
		mainActivity.add(act);
		System.out.println("Added");
	}

	public void finishAll() {
		for (Activity act : mainActivity) {
			if (!act.isFinishing()) {
				act.finish();
			}
		}
	}

}
