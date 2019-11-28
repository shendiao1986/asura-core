package org.asura.core.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TranslateUtil {
	private static Set<String> spliters = new HashSet<>();
	protected static final String URL_TEMPLATE = "http://fanyi.youdao.com/openapi.do?keyfrom=IBMTest&key=1968469754&type=data&doctype=xml&version=1.1&q=";

	static {
		spliters.add(".");
		spliters.add(",");
		spliters.add("?");
		spliters.add("!");
		spliters.add(":");
		spliters.add("。");
		spliters.add("，");
		spliters.add("？");
		spliters.add("！");
		spliters.add("：");
	}

	public static String translate(String string) {
		String result = "";
		try {
			String[] ss = split(string);

			for (String s : ss) {
				String t = HttpUtil.getContent(
						"http://fanyi.youdao.com/openapi.do?keyfrom=IBMTest&key=1968469754&type=data&doctype=xml&version=1.1&q="
								+ URLEncoder.encode(s, "utf8"));

				XMLParser parser = new XMLParser(t, "");
				String error = parser.getPathValue("//errorCode");
				if ("0".equals(error))
					result = result + StringUtil.getStringFromStrings(parser.getPathValues("//paragraph"), "\n");
				else
					result = result + s;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();

			result = string;
		}

		return result;
	}

	public static String[] split(String string) {
		if (string.length() >= 200) {
			List<String> list = new ArrayList<String>();
			int start = 0;
			while (true) {
				int newStart = -1;
				for (int i = Math.min(string.length() - 1, 199 + start); i > start; --i) {
					if (spliters.contains(String.valueOf(string.charAt(i)))) {
						newStart = i;
						break;
					}
				}
				if (newStart == -1) {
					newStart = Math.min(start + 199, string.length());
				}
				list.add(string.substring(start, newStart));
				start = newStart;
				newStart = -1;
				if (start >= string.length() - 1) {
					return ((String[]) list.toArray(new String[0]));
				}
			}
		}
		return new String[] { string };
	}
}
