package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PicUtils {
	private String goodUrls[] = new String[] { "pics.dmm.co.jp", "gigaimg",
			"pixcool.biz", "image.posren" };
	private String badUrl[] = new String[] { "mysave", "imgkeep", "i.imgur",
			"flare.me" };
	private String path;
	private String[] urls;

	public PicUtils(String path, int num) {
		super();
		this.path = path;
		String in = readFile();
		urls = parseFile(in, num);
	}
	public PicUtils(int num,String in) {
		super();
		urls = parseFile(in, num);
	}

	public String[] getUrls() {
		return urls;
	}

	
	private String readFile() {
		File file = new File(path);
		// ======================
		FileInputStream is;
		BufferedReader br;
		String result = "";
		try {
			is = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(is, "GB2312"));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.length() > 2) {
					result += line.trim();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	private String[] parseFile(String in, int x) {
		String regx = "http:[a-zA-Z0-9/\\.]{1,}jpg";
		Pattern pattern = Pattern.compile(regx);
		Matcher matcher = pattern.matcher(in);
		String tmp;
		int i = 0;
		LinkedList<String> list = new LinkedList<String>();
		while (matcher.find() && i < x) {
			i++;
			tmp = matcher.group();
			list.add(tmp);
		}
		
		String tmpStrings[]= new String[list.size()];
		for (int j = 0; j < tmpStrings.length; j++) {
			tmpStrings[j] = list.get(j);
		}
		
		
		return tmpStrings.clone();
	}
}
