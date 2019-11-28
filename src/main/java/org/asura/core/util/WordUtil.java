package org.asura.core.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.asura.core.util.model.WordDistance;

import com.github.stuxuhai.jpinyin.ChineseHelper;

public class WordUtil {

	private static String SPECIAL_CHAR = "[\\s!@#$%^&\\[\\]{}*()-]";
	private static String NUMBER_REGEX = "[0-9]";

	public static int getPositionDistance(String s1, String s2, String sentence) {
		int[] ids1 = StringUtil.getAllIndex(sentence, s1);
		int[] ids2 = StringUtil.getAllIndex(sentence, s2);
		if ((ids1.length > 0) && (ids2.length > 0)) {
			WordDistance wd = getMinDistanceAscenOrdered(ids1, ids2);
			wd.setS1(s1);
			wd.setS2(s2);
			wd.setSentence(sentence);

			return (wd.getDistance() - wd.getFrontWord().length());
		}
		return -1;
	}

	public static boolean isWithinDistance(String s1, String s2, String sentence, int distance) {
		return (getPositionDistance(s1, s2, sentence) <= distance);
	}

	private static WordDistance getMinDistanceAscenOrdered(int[] c1s, int[] c2s) {
		WordDistance wd = new WordDistance();
		int result = 2147483647;
		int p1 = 0;
		int p2 = 0;
		while ((p1 < c1s.length) && (p2 < c2s.length)) {
			if (c1s[p1] >= c2s[p2]) {
				int value = c1s[p1] - c2s[p2];
				if (value < result) {
					result = value;
					wd.setPosition1(c1s[p1]);
					wd.setPosition2(c2s[p2]);
					wd.setDistance(result);
				}
				++p2;
			} else {
				int value = c2s[p2] - c1s[p1];
				if (value < result) {
					result = value;
					wd.setPosition1(c1s[p1]);
					wd.setPosition2(c2s[p2]);
					wd.setDistance(result);
				}
				++p1;
			}
		}

		return wd;
	}

	public static boolean isOnlyEnglish(String word) {
		if (word != null) {
			for (int i = 0; i < word.length(); i++) {
				char c = word.charAt(i);
				boolean isEnglishLetter = (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
				if (!isEnglishLetter) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * 传入的参数去掉!@#$%^&\\[\\]{}*()这些特殊字符
	 * @param word
	 * @return
	 */
	public static String removeSpecialChar(String word) {
		String result = word.replaceAll(SPECIAL_CHAR, "");
		return result.trim();
	}

	public static String removeNumber(String word) {
		String result = word.replaceAll(NUMBER_REGEX, "");
		return result.trim();
	}

	/*
	 * 去除中英文之外的字符
	 */
	public static String onlyChineseAndEnglish(String word) {
		StringBuilder sb = new StringBuilder();
		if (word != null) {
			for (int i = 0; i < word.length(); i++) {
				char c = word.charAt(i);
				boolean isEnglishLetter = (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
				if (ChineseHelper.isChinese(c) || isEnglishLetter) {
					sb.append(c);
				}
			}
		}

		return sb.toString();
	}

	public static List<String> extractPrefix(String word) {
		return extractPrefix(word, false);
	}

	/**
	 * 提取前缀，
	 * 
	 * @param word
	 * @param needFirst 是否生成首字，如果为false,则返回的前缀中至少需要有两个字符，也就是说“亚马逊” -》 “亚马” 和 “亚马逊” 而不会有 “亚”
	 * 如果是true,则会生成"亚"
	 * @return
	 */
	public static List<String> extractPrefix(String word, boolean needFirst) {
		List<String> result = new ArrayList<String>();
		if (StringUtils.isNotBlank(word)) {
			StringBuilder sb = new StringBuilder();
			boolean previousIsChinese = false;
			for (int i = 0; i < word.length(); i++) {
				char c = word.charAt(i);
				boolean isChinese = ChineseHelper.isChinese(c);
				if (isChinese) {
					if (previousIsChinese) {
						sb.append(c);
						//needFirst为false则不会生成单字的前缀，比如“亚马逊”不会生成前缀“亚”
						if (needFirst || sb.length() > 1) {
							result.add(sb.toString());
						}
					} else {
						if (i != 0) {//避免空字符串被生成
							result.add(sb.toString());
						}
						sb.append(c);
						//needFirst为false则不会生成单字的前缀，比如“亚马逊”不会生成前缀“亚”
						if (needFirst || sb.length() > 1) {
							result.add(sb.toString());
						}
					}
					previousIsChinese = true;
				} else {
					sb.append(c);
					previousIsChinese = false;
				}
			}

			/*
			 * ---如果最后一个不是中文，需要将最后的加进来，否则，形如"大数据量298"
			 * 就只能生成"大数" "大数据" "大数据量",而最后一个"大数据量298"就不会生成
			 */
			if (!previousIsChinese) {
				result.add(sb.toString());
			}
		}

		return result;
	}

	/**
	 * 提取前缀，返回的前缀中至少需要有两个字符，也就是说
	 * “亚马逊” -》 “亚马” 和 “亚马逊” 而不会有 “亚”
	 * @param word
	 * @param length   前缀最长字符串，也就是说如果长度是3，那“植物大战僵尸” 就只会取到“植物大”，
	 * 不会生成 “植物大战”
	 * 注意，不同于extractPrefixWithEnglish，本方法将“星际weibo”这个词拆分成“星”“星际”“星际weibo”
	 * @return
	 */
	public static List<String> extractPrefix(String word, int length, boolean needFirst) {
		List<String> result = extractPrefix(word, needFirst);
		if (CollectionUtils.isNotEmpty(result) && result.size() > length) {
			return result.subList(0, length);
		}
		return result;
	}

	public static List<String> extractPrefixWithEnglish(String word, boolean needFirst) {
		List<String> result = new ArrayList<String>();
		if (StringUtils.isNotBlank(word)) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < word.length(); i++) {
				char c = word.charAt(i);
				sb.append(c);
				//needFirst为false则不会生成单字的前缀，比如“亚马逊”不会生成前缀“亚”
				if (needFirst || sb.length() > 1) {
					result.add(sb.toString());
				}
			}
		}

		return result;
	}

	/**
	 * 同上面的extractPrefix()方法不同，该方法将“星际weibo”这个词拆分成“星”“星际”“星际w”“星际we”“星际wei”“星际weib”“星际weibo”
	 * @param word
	 * @param length
	 * @param needFirst
	 * @return
	 */
	public static List<String> extractPrefixWithEnglish(String word, int length, boolean needFirst) {
		List<String> result = extractPrefixWithEnglish(word, needFirst);
		if (CollectionUtils.isNotEmpty(result) && result.size() > length) {
			return result.subList(0, length);
		}
		return result;
	}

	/**
	 * 只提取汉字，其他都丢弃
	 * @param word
	 * @return
	 */
	public static String extractChinese(String word) {
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(word)) {
			for (int i = 0; i < word.length(); i++) {
				char c = word.charAt(i);
				if (ChineseHelper.isChinese(c)) {
					sb.append(c);
				}
			}
		}

		return sb.toString();
	}

	/**
	 * 判断是否包含中文
	 * @param word
	 * @return
	 */
	public static boolean containChinese(String word) {
		String chinese = extractChinese(word);
		if (StringUtils.isNotBlank(chinese)) {
			return true;
		} else {
			return false;
		}
	}

	public static String toLowerCase(String str) {
		if (str == null) {
			return null;
		}

		return str.toLowerCase();
	}

	public static List<String> toLowerCase(List<String> strList) {
		List<String> result = new ArrayList<String>();
		if (CollectionUtils.isNotEmpty(strList)) {
			for (String str : strList) {
				if (str != null) {
					result.add(str.toLowerCase());
				}
			}
		}

		return result;
	}
}
