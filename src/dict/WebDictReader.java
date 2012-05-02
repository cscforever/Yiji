package dict;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.zip.GZIPInputStream;

import android.util.Log;

public class WebDictReader {
	public static String DICT_QUERY_FORMAT = "http://dict.youdao.com/dp/dp?q=";

	public static String webQueryWord(String word) {
		try {
			URL url = buildQueryUrl(word);
			System.out.println(url);
			return loadHtmlFromWeb(url);

		} catch (Exception e) {
			return "";
		}
	}

	static URL buildQueryUrl(String word) throws MalformedURLException,
			UnsupportedEncodingException {
		String string = "";
		String str = URLEncoder.encode(word, "utf-8");
		string = DICT_QUERY_FORMAT + str;
		return new URL(string);

	}

	static String loadHtmlFromWeb(URL paramURL) throws IOException {
		ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
		URLConnection localURLConnection = paramURL.openConnection();
		localURLConnection.setConnectTimeout(30000);
		localURLConnection.addRequestProperty("Accept-Encoding", "gzip");
		localURLConnection.addRequestProperty("Content-Encoding", "gzip");
		GZIPInputStream localGZIPInputStream = new GZIPInputStream(
				localURLConnection.getInputStream());
		byte[] arrayOfByte = new byte[1024];
		int i;
		System.out.println("web query definition is -------->");

		if ((i = localGZIPInputStream.read(arrayOfByte)) > 0) {
			localByteArrayOutputStream.write(arrayOfByte, 0, i);
		}
		String str = localByteArrayOutputStream.toString();
		System.out.println("web query definition is "+str);
		return str;
	}
}
