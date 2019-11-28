package org.asura.core.util.spliter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.asura.core.util.StringUtil;

public class CommonSpliter {
	public static final String[] SEPARATOR = { " ", " 　", "[", "]", "(", ")", "-", "（", "）", "【", "】", "－", "—", "_",
			"―", "　", "+", "{", "}", "｛", "｝", ",", "，", ";", "；", ":", "：", "?", "？", "。", "\\", "/", "、", "／", "!",
			"！", "＼", "*", "×", "“", "”", "·", "\t", "?", "？", "\"", "#", "《", "\n", "\r\n", "\r", "^", "|" };
	private static final String commonString = "ⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩⅪⅫⅰⅱⅲⅳⅴⅵⅶⅷⅸⅹαβμωπ.-@_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789０１２３４５６７８９ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ";
	private static HashSet<String> commonStringTable = new HashSet<>();

	public static HashSet<String> separatorTable = new HashSet<>();
	private static Pattern chinesePattern;

	static {
		for (int i = 0; i < SEPARATOR.length; ++i) {
			separatorTable.add(SEPARATOR[i]);
		}
		char[] chars = commonString.toCharArray();
		for (int i = 0; i < chars.length; ++i) {
			commonStringTable.add(String.valueOf(chars[i]));
		}

		chinesePattern = Pattern.compile("[一-龥]");
	}

	public static String[] filter(String sentence) {
		List<String> list = new ArrayList<>();
		String[] ss = getSeparatedString(sentence);
		for (int i = 0; i < ss.length; ++i) {
			String[] ecw = extractCommonWord(ss[i]);
			for (int j = 0; j < ecw.length; ++j) {
				list.add(ecw[j]);
			}
		}
		return list.toArray(new String[0]);
	}

	public static boolean isChineseCharacter(char ch) {
		String string = String.valueOf(ch);
		return chinesePattern.matcher(string).find();
	}

	public static boolean needSplit(String sentence) {
		char[] chars = sentence.toCharArray();

		for (int i = 0; i < chars.length; ++i) {
			if (isChineseCharacter(chars[i])) {
				return true;
			}
		}
		return false;
	}

	public static String[] getSeparatedString(String sentence) {
		if (sentence != null) {
			return getSeparatedString(sentence, separatorTable);
		}
		return new String[0];
	}

	public static String[] getSeparatedString(String sentence, HashSet<String> spliterSet) {
		List<String> list = new ArrayList<>();
		List<String> lastlist = new ArrayList<>();
		Set<Integer> ihs = new HashSet<>();
		int max = 1;
		for (String spliter : spliterSet) {
			ihs.add(Integer.valueOf(spliter.length()));
			if (max < spliter.length()) {
				max = spliter.length();
			}
		}

		if (max > sentence.length()) {
			if (StringUtil.isNullOrEmpty(sentence)) {
				return new String[0];
			}
			return new String[] { sentence };
		}

		for (int i = max; i > 0; --i) {
			if (ihs.contains(Integer.valueOf(i))) {
				if (list.size() == 0) {
					list.addAll(Arrays.asList(getSeparatedString(sentence, spliterSet, i)));
					lastlist.clear();
					lastlist.addAll(list);
				} else {
					lastlist.clear();
					for (String s : list) {
						lastlist.addAll(Arrays.asList(getSeparatedString(s, spliterSet, i)));
					}
					list.clear();
					list.addAll(lastlist);
				}
			}
		}
		List<String> newList = new ArrayList<>();
		for (int i = 0; i < list.size(); ++i) {
			if (!(StringUtil.isNullOrEmpty(list.get(i)))) {
				newList.add(list.get(i));
			}
		}
		return newList.toArray(new String[0]);
	}

	private static String[] getSeparatedString(String sentence, HashSet<String> spliterSet, int awp) {
		List<String> list = new ArrayList<>();
		String[] chars = split(sentence, awp);
		int lastSeparator = -1;
		for (int i = 0; i < chars.length; ++i) {
			if (spliterSet.contains(chars[i])) {
				if (i > lastSeparator) {
					if (lastSeparator == -1) {
						list.add(sentence.substring(0, i));
					} else if (i > lastSeparator + chars[i].length()) {
						list.add(sentence.substring(lastSeparator + chars[i].length(), i));
					}
				}

				lastSeparator = i;
			} else {
				if ((i != chars.length - 1) || (i <= lastSeparator))
					continue;
				if (lastSeparator == -1)
					list.add(sentence.substring(0, (i + awp > sentence.length()) ? sentence.length() : i + awp));
				else {
					list.add(sentence.substring(lastSeparator + chars[i].length(), i + awp));
				}
			}
		}

		List<String> newList = new ArrayList<>();
		for (int i = 0; i < list.size(); ++i) {
			if (!(StringUtil.isNullOrEmpty(list.get(i)))) {
				newList.add(list.get(i));
			}
		}
		return newList.toArray(new String[0]);
	}

	public static String removeCommonWord(String sentence) {
		return null;
	}

	public static String[] getChineseEnglishSeparatedString(String sentence) {
		return getChineseEnglishSeparatedString(sentence, separatorTable);
	}

	public static String[] getChineseEnglishSeparatedString(String sentence, HashSet<String> spliterSet) {
		List<String> list = new ArrayList<>();
		if (!(StringUtil.isNullOrEmpty(sentence))) {
			String[] sws = getSeparatedString(sentence, spliterSet);
			for (String sw : sws) {
				list.addAll(Arrays.asList(extractCommonWord(sw)));
			}
		}

		return list.toArray(new String[0]);
	}

	public static String[] extractCommonWord(String sentence) {
		List<String> list = new ArrayList<>();
		List<Integer> indexList = new LinkedList<>();
		char[] chars = sentence.toCharArray();
		for (int i = 0; i < chars.length; ++i) {
			if (commonString.contains(String.valueOf(chars[i]))) {
				indexList.add(Integer.valueOf(i));
			}
		}
		Integer[] indexs = indexList.toArray(new Integer[0]);
		if (indexs.length >= 1) {
			if (indexs[0].intValue() > 0) {
				list.add(sentence.substring(0, indexs[0].intValue()));
			}
			for (int i = 0; i < indexs.length; ++i) {
				if (i != indexs.length - 1) {
					for (int j = i + 1; j < indexs.length; ++j) {
						if ((indexs[j].intValue() - indexs[i].intValue() <= j - i) && (j != indexs.length - 1))
							continue;
						if (indexs[j].intValue() - indexs[i].intValue() > j - i) {
							list.add(sentence.substring(indexs[i].intValue(), indexs[(j - 1)].intValue() + 1));
							list.add(sentence.substring(indexs[(j - 1)].intValue() + 1, indexs[j].intValue()));
							i = j - 1;
							break;
						}
						list.add(sentence.substring(indexs[i].intValue(), indexs[j].intValue() + 1));
						i = j;
					}

				} else {
					list.add(sentence.substring(indexs[i].intValue(), indexs[i].intValue() + 1));
				}
			}

			if (indexs[(indexs.length - 1)].intValue() < chars.length - 1)
				list.add(sentence.substring(indexs[(indexs.length - 1)].intValue() + 1));
		} else {
			list.add(sentence);
		}
		return list.toArray(new String[0]);
	}
	
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
