package com.zzq.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Model.Ticket;
import Model.WordStructure;
import android.os.Environment;

public class Constant {
	static String FILE_NAME = "1.xml";
	static String FILE_PATH = "";

	static final String UNIT_NUMBER = "unit_number";

	static List<WordStructure> wordList = new ArrayList<WordStructure>();
	static List<WordStructure> totalWordList = new ArrayList<WordStructure>();
	static List<Ticket> ticketList = new ArrayList<Ticket>();
	static int ticketListCursor = 0;
	static int unit_number = 1;

	static Ticket getCurrentTicket() {

		Ticket retValTicket = null;
		if (ticketListCursor < ticketList.size()) {
			retValTicket = ticketList.get(ticketListCursor);
		} else {
			retValTicket = null;
		}

		return retValTicket;
	}

	static WordStructure getCurrentWord() {
		WordStructure retValTicket = null;

		if (wordList == null || wordList.size() == 0) {
			retValTicket = null;
		} else if (getCurrentTicket() == null
				|| getCurrentTicket().getWordCursor() >= wordList.size()) {
			retValTicket = null;
		} else {
			retValTicket = wordList.get(getCurrentTicket().getWordCursor());
		}
		return retValTicket;
	}

	static void printCurrentTicket() {
		System.out.println("**************************************");
		for (int i = 0; i < ticketList.size(); i++) {
			Ticket ticket = ticketList.get(i);
			System.out.println(wordList.get(ticket.getWordCursor()).toString()
					+ "," + ticket);
		}
		System.out.println("**************************************");
	}

	static String getMp3LocationString(String wordname) {

		File file1 = new File(Environment.getExternalStorageDirectory()
				+ "/yiji");
		if (!file1.exists()) {
			try {
				file1.mkdirs();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		File file2 = new File(Environment.getExternalStorageDirectory()
				+ "/yiji/sound/");
		if (!file2.exists()) {
			try {
				file2.mkdirs();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (file1.exists() && file2.exists()) {

			return Environment.getExternalStorageDirectory() + "/yiji/sound/"
					+ wordname + ".mp3";
		} else {
			return null;
		}

	}
	
	static public void mp3load(String wordName) {
		
		FetchMp3Thread thread = new FetchMp3Thread();
		thread.wordName = wordName;
		thread.start();

	}

	static public void mp3load(String urlString, String file_path) {
		try {
			URL url = new URL(urlString);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			conn.connect();

			File outputFile = new File(file_path);

			FileOutputStream fos = new FileOutputStream(outputFile);

			InputStream is = conn.getInputStream();

			byte[] buffer = new byte[1024];
			int len1 = 0;
			while ((len1 = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len1);
			}
			fos.close();
			is.close();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
