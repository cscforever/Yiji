package com.zzq.activity;

import java.io.File;

import utils.Mp3Url;

public class FetchMp3Thread extends Thread {

	public String wordName;
	public boolean isBunch = false;
public boolean isFinished=false;
	@Override
	public void run() {
		// TODO Auto-generated method stub

		if (isBunch) {

			for (int i = 0; i < Constant.wordList.size(); i++) {

				String nameString = Constant.wordList.get(i).getNameString();
				String url = new Mp3Url(nameString).getMp3url_string();

				String file_pathString = Constant
						.getMp3LocationString(nameString);

				if (!new File(file_pathString).exists()) {

					Constant.mp3load(url, file_pathString);

				}

			}

		} else {
			String url = new Mp3Url(wordName).getMp3url_string();

			String file_pathString = Constant.getMp3LocationString(wordName);

			if (!new File(file_pathString).exists()) {

				Constant.mp3load(url, file_pathString);

			}
		}
		isFinished = true;
		super.run();
	}
}
