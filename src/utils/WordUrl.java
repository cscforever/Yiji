package utils;


public class WordUrl {
	private String meaning;

	public WordUrl(String danci) {
		HttpDownloader httpDownloader = new HttpDownloader();

		String lrc = httpDownloader.download(33,
				"http://dict.youdao.com/m/search?q=" + danci
						+ "&keyfrom=smartresult.dict.m#hanhan", -1);
		lrc = lrc.replaceAll("有道", "易记");
		lrc = lrc.replaceAll("youdao", "yiji");
		lrc = lrc.replaceAll("网易公司", "周卓潜出品");
		meaning = lrc;

	}

	public String getMeaning() {
		return meaning;
	}

}
