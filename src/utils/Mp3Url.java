package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mp3Url {
	private String mp3url_string;

	public Mp3Url(String word) {
		// TODO Auto-generated constructor stub
//		HttpDownloader httpDownloader = new HttpDownloader();
//		String lrc = httpDownloader.download("http://dict.youdao.com/speech?audio=" + word,-1);
		mp3url_string  = "http://dict.youdao.com/speech?audio=" + word;
//		htmlParse(lrc);
	}


	public String getMp3url_string() {
		return mp3url_string;
	}

	void htmlParse(String in) {
		Matcher matcher2 = Pattern.compile("http://[/a-zA-Z\\.0-9]*\\.mp3")
				.matcher(in);
		if (matcher2.find()) {
			mp3url_string = matcher2.group();
		}
	}

}
