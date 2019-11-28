package org.asura.core.util.spliter;

public class Spliter {
	public static String[] split(String sentence, int APW) {
		if (sentence.length() < APW) {
			return new String[] { sentence };
		}
		String[] words = new String[sentence.length() - APW + 1];
		for (int i = 0; i < words.length; ++i) {
			words[i] = sentence.substring(i, i + APW);
		}
		return words;
	}
}
