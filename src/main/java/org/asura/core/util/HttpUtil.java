package org.asura.core.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

	public static Map<String, String> getParameters(String url) {
		Map<String, String> result = new HashMap<String, String>();
		int index = url.indexOf("?");
		if (index > 0) {
			String subUrl = url.substring(index + 1);
			String[] ss = StringUtil.split(subUrl, "&");
			for (String s : ss) {
				int ei = s.indexOf("=");
				if (ei <= 0) {
					continue;
				}
				try {
					result.put(s.substring(0, ei), URLDecoder.decode(s.substring(ei + 1), "utf8"));
				} catch (UnsupportedEncodingException e) {
					logger.error("exception occurred", e);
				}
			}
		}
		return result;
	}

	public static String getContent(String urlStr, String encoding) {
		try {
			URL url = new URL(urlStr);
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), encoding));
			String s = "";
			StringBuffer sb = new StringBuffer("");
			while ((s = br.readLine()) != null) {
				sb.append(s + "\n");
			}

			br.close();
			return sb.toString();
		} catch (Exception e) {
			return "error open url:" + urlStr + "\n" + ExceptionUtil.getExceptionContent(e);
		}
	}

	public static byte[] fetchBytes(String url) {
		HttpClient client = getHttpClient();

		byte[] content = null;
		HttpGet method = new HttpGet(url);

		try {
			HttpResponse response = client.execute(method);
			content = EntityUtils.toByteArray(response.getEntity());
		} catch (Exception e) {
			logger.error("exception occurred", e);
		}

		return content;
	}

	public static String fetchConent(String url, String charsetName) {
		byte[] content = fetchBytes(url);
		if (content != null) {
			try {
				return new String(content, charsetName);
			} catch (UnsupportedEncodingException e) {
				logger.error("error encoding = " + charsetName + " use utf-8 replace it!");
				try {
					return new String(content, "utf-8");
				} catch (UnsupportedEncodingException ex) {
					logger.error("error encoding by utf8", ex);
					return "";
				}
			}
		}
		return null;
	}

	public static HttpClient getHttpClient() {
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).setSocketTimeout(60000)
				.setRedirectsEnabled(true).build();
		return HttpClients.custom().setMaxConnTotal(1000).setDefaultRequestConfig(requestConfig).build();
	}

	public static HttpClient getProxyClient(String host, int port) {
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).setSocketTimeout(60000)
				.setRedirectsEnabled(true).setProxy(new HttpHost(host, port)).build();
		return HttpClients.custom().setMaxConnTotal(1000).setDefaultRequestConfig(requestConfig).build();
	}

	public static String post(String url, Map<String, String> params) {
		List<String> result = new ArrayList<>();
		try {
			URL postUrl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();

			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.connect();
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());

			List<String> list = new ArrayList<>();
			for (String key : params.keySet()) {
				list.add(key + "=" + URLEncoder.encode(params.get(key), "utf-8"));
			}
			String content = StringUtil.getStringFromStrings(list, "&");
			out.writeBytes(content);
			out.flush();
			out.close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
			String line = "";
			while ((line = reader.readLine()) != null) {
				result.add(line);
			}
			reader.close();
			connection.disconnect();
		} catch (Exception e) {
			logger.error("post url exception occurred", e);
		}

		return StringUtil.getStringFromStrings(result, "\n");
	}

	public static String getContent(String url) {
		return getContent(url, "utf8");
	}
}
