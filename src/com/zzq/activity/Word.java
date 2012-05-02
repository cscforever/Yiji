package com.zzq.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import utils.FileUtils;

import Model.WordStructure;
import android.os.Environment;

public class Word {

	public String readFile(String filePath) {
		System.out.println("reading file");
		String dirPathString = FileUtils.SDPATH_zzq + filePath;
		System.out.println(dirPathString);

		File file = new File(dirPathString);
		// ======================
		if (!file.exists()) {
			System.out.println("file not exites");
			return null;
		}
		FileInputStream is;
		BufferedReader br;
		String result = "";
		try {
			is = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.length() > 2) {
					result += line.trim();
					System.out.println(line);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("--------->reading file finish");
		return result;
	}

	public LinkedList<WordStructure> getListFromXmlContent(String fileContent)
			throws Exception {
		MyHandler myContentHandler = null;
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			XMLReader reader = factory.newSAXParser().getXMLReader();
			myContentHandler = new MyHandler();
			reader.setContentHandler(myContentHandler);
			System.out.println("reading size: " + fileContent.length());
			System.out.println("parsing xml");
			InputSource inputSource = new InputSource(new StringReader(
					fileContent));
			inputSource.setEncoding("utf-8");
			reader.parse(inputSource);
			System.out.println("parsing xml finish");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return myContentHandler.list;

	}

	public List<WordStructure> getListFromXmlFile(String filePath)
			throws Exception {

		return getListFromXmlContent(readFile(filePath));

		// MyHandler myContentHandler = null;
		// try {
		// SAXParserFactory factory = SAXParserFactory.newInstance();
		// XMLReader reader = factory.newSAXParser().getXMLReader();
		// myContentHandler = new MyHandler();
		// reader.setContentHandler(myContentHandler);
		// String result;
		// if ((result = readFile(filePath)) == null) {
		// return null;
		// }
		//
		// System.out.println("reading size: " + result.length());
		// System.out.println("parsing xml");
		// InputSource inputSource = new InputSource(new StringReader(result));
		// inputSource.setEncoding("utf-8");
		// reader.parse(inputSource);
		// System.out.println("parsing xml finish");
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// return myContentHandler.list;

	}

	private class MyHandler extends DefaultHandler {
		private LinkedList<WordStructure> list = new LinkedList<WordStructure>();
		WordStructure map = new WordStructure();
		String tagName = "";

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attr) throws SAXException {

			if (qName.equals("word")) {
				tagName = qName;
				map = new WordStructure();
			} else if (localName.equals("word")) {
				tagName = localName;
				map = new WordStructure();
			} else {
				if (qName.length() > localName.length()) {
					tagName = qName;
				} else {
					tagName = localName;
				}
			}
			super.startElement(uri, localName, qName, attr);
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			super.endElement(uri, localName, qName);
			if (localName.equals("word") || qName.equals("word")) {
				if (map.getNameString() != null
						&& map.getNameString().length() > 0
						&& map.getMeanString() != null
						&& map.getMeanString().length() > 0) {
					list.add(map);
					System.out.println("added");
				}
			}
			tagName = "";
		}

		String tmp;

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			super.characters(ch, start, length);
			tmp = new String(ch, start, length);
			if (tagName.equals("danci")) {
				map.setNameString(tmp);
				System.out.println(tmp);
			} else if (tagName.equals("meaning")) {
				map.setMeanString(tmp);
				System.out.println(tmp);
			}
		}

		@Override
		public void endDocument() throws SAXException {
			super.endDocument();
			System.out.println("yi gong =" + list.size());

		}

	}
}
