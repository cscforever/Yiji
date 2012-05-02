package com.zzq.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button engButton, japButton, gerButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		MyApp appState = (MyApp) getApplicationContext();
		appState.addActivity(this);
		setContentView(R.layout.language_activity);
		findView();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			System.out.println("back----------");
			AlertDialog.Builder builder = new AlertDialog.Builder(this)
					.setMessage("EXIT?");
			builder.setPositiveButton("yes", new AlertDialog.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

					MyApp appState = (MyApp) getApplicationContext();
					appState.finishAll();
					// finish();
				}
			});

			builder.setNegativeButton("no", new AlertDialog.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
				}

			});
			builder.create().show();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	void findView() {
		engButton = (Button) findViewById(R.id.eng_button);
		japButton = (Button) findViewById(R.id.jap_button);
		gerButton = (Button) findViewById(R.id.ger_button);
		engButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				startActivity(new Intent(MainActivity.this,
						ChooseBookActivity.class));
			}
		});

		japButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				startActivity(new Intent(MainActivity.this,
						SearchActivity.class));
			}
		});

		gerButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// Toast.makeText(MainActivity.this, R.string.notYetGer,
				// Toast.LENGTH_LONG).show();
				startActivity(new Intent(MainActivity.this,
						ChooseImportFileActivity.class));
			}
		});

	}
}
