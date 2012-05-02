package com.zzq.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.FileUtils;

import Model.WordStructure;

public class ImportWord {

	public static LinkedList<String> getXmlList(String allEnFileContent) {

		return getXmlListWithLengthLimit(allEnFileContent, 99999);
	}

	public static LinkedList<String> getXmlListWithLengthLimit(
			String allEnFileContent, int length) {

		LinkedList<String> danci = new LinkedList<String>();
		Pattern pattern = Pattern.compile("[A-Za-z]{3,}");
		Matcher matcher = pattern.matcher(allEnFileContent);
		String wordString = "";

		int count = 0;
		while (matcher.find() && count < length) {
			wordString = matcher.group().trim();
			if (wordString.length() >= 3) {
				danci.add(wordString.trim());
				count++;
			}
		}
		return danci;
	}

	public static LinkedList<String> getXmlList(
			LinkedList<WordStructure> wordStructures, int groupLength) {

		LinkedList<String> list = new LinkedList<String>();

		for (int i = 0; i < wordStructures.size() / groupLength + 1; i++) {
			String outXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
			outXml += "<wordsList>";

			for (int j = i * groupLength; j < Math.min((i + 1) * groupLength,
					wordStructures.size()); j++) {
				outXml += "\n<word>\n<danci>"
						+ wordStructures.get(j).getNameString()
						+ "</danci>\n<meaning>"
						+ wordStructures.get(j).getMeanString()
						+ "</meaning>\n</word>";
			}
			outXml += "\n</wordsList>";
			list.add(outXml);

		}
		return list;
	}

	public static LinkedList<String> getXmlList(LinkedList<String> danci,
			LinkedList<String> meaning, int groupLength) {

		LinkedList<String> list = new LinkedList<String>();

		for (int i = 0; i < danci.size() / groupLength + 1; i++) {
			String outXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
			outXml += "<wordsList>";

			for (int j = i * groupLength; j < Math.min((i + 1) * groupLength,
					danci.size()); j++) {
				outXml += "\n<word>\n<danci>" + danci.get(j)
						+ "</danci>\n<meaning>" + meaning.get(j)
						+ "</meaning>\n</word>";
			}
			outXml += "\n</wordsList>";
			list.add(outXml);

		}
		return list;
	}

	public static void writeToFileFromXmlList(LinkedList<String> inList,
			String fileNameString) throws Exception {
		if (inList == null || inList.size() == 0) {
			return;
		}
		String dirPathString = FileUtils.SDPATH_zzq 
				+ fileNameString;
		File dir = new File(dirPathString);
		if (!dir.exists()) {
			dir.mkdirs();
			
			System.out.println("sdfsdfsdfgdssdfg");
		}
		


		
		
		File file;
		for (int i = 0; i < inList.size(); i++) {
			if (i <= 9) {
				file = new File(dirPathString + File.separator + fileNameString
						+ "_0" + i + "" + ".xml");
			} else {
				file = new File(dirPathString + File.separator + fileNameString
						+ "_0" + i + "" + ".xml");
			}
			Writer ow = new OutputStreamWriter(new FileOutputStream(file),
					"utf-8");

			ow.write(inList.get(i));

			if (ow != null) {
				System.out.println("write to sd_" + i);
				ow.close();
			}
		}
	}

	public static LinkedList<String> getXmlList(String input, int groupLength) {
		return getXmlList(input, groupLength, 9999);
	}

	public static LinkedList<String> getXmlList(String input, int groupLength,
			int length) {

		LinkedList<String> danci = new LinkedList<String>();
		LinkedList<String> meaning = new LinkedList<String>();
		Pattern pattern = Pattern.compile("[A-Za-z]{3,} ");
		Matcher matcherForName = pattern.matcher(input);
		String nameString = "";
		String meaningString = "";

		while (matcherForName.find() && meaning.size() < length) {
			nameString = matcherForName.group().trim();
			if (nameString.length() >= 3) {
				System.out.println("name------->" + nameString);

				int pos = input.indexOf(nameString) + nameString.length();

				String subString = input.substring(pos,
						Math.min(pos + 50, input.length()));

				// String matherString =
				// "[\\(\\（\\)\\）\\【\\[\\】\\]\\；\\;\\，\\,\\。\\;\\ \\、\u4e00-\u9fa5]";
				String matherString = "([; ,/(/)，。？：；‘’！“”—…、（）【】{}《》\u4e00-\u9fa5]|(/././.)){3,}";
				// String matherStringWithDot =
				// "[/.\\;\\ \\,\\(\\)，。？：；‘’！“”—…、（）【】{}《》\u4e00-\u9fa5]{3,}";

				// String matherString = "[^x00-xff]";

				Matcher matcherForChn = Pattern.compile(matherString).matcher(
						subString);
				while (matcherForChn.find()) {
					meaningString = matcherForChn.group().trim();

					if (meaningString.length() >= 1) {
						// System.out.println("tm3---->" + tm3);
						// int end = input.indexOf(tm3) + tm3.length();
						// input = input.substring(end);
						// System.out.println(input);
						// matcher = pattern.matcher(input);
						System.out.println("meaning---->" + meaningString);
						danci.add(nameString.trim());
						meaning.add(meaningString.trim());
						break;
					}
				}
			}
		}

		// printList(danci, meaning);

		return getXmlList(danci, meaning, groupLength);
	}

	private static void printList(LinkedList<String> list,
			LinkedList<String> list2) {

		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
			System.out.println(list2.get(i));

		}

	}

	private static void printList(LinkedList<String> list) {

		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}

	}

}
