package com.zzq.activity;

import java.util.ArrayList;
import java.util.List;

import Model.Ticket;
import Model.WordStructure;

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

}
