package utils;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

public class UrlUtils {

	public static String buildUrlByMapAndBaseUrl(String baseString,
			List<NameValuePair> nameValuePairs) {
		// TODO Auto-generated method stub
		if (nameValuePairs==null) {
			return baseString ;
		}
		String queryString = URLEncodedUtils.format(nameValuePairs, "UTF-8");
		return baseString + "?" + queryString;
	}

}
