package org.asura.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.asura.core.cjf.CJFBeanFactory;
import org.asura.core.cjf.ChineseJF;
import org.asura.core.string.IStringCondition;
import org.asura.core.util.cache.SimpleCache;
import org.asura.core.util.spliter.CommonSpliter;

public class StringUtil {
	public static final String SPLITER_RECORD = "원";
	public static final String SPLITER_FIELD = "빈";
	public static final String UNIQUE_STRING = "어";
	private static final Pattern IP_PATTERN = Pattern.compile("\\d+\\.\\d+\\.\\d+\\.\\d+");
	private static SimpleCache<String, String> standardCache = new SimpleCache<>(1000000);
	private static Pattern chinesePattern = Pattern.compile("[一-龥]");

	public static String getStandardString(String string) {
		if (isNullOrEmpty(string)) {
			return "";
		} else {
			if (standardCache.get(string) == null) {
				standardCache.cache(string, chineseFJChange(qBchange(string.toLowerCase())).trim());
			}

			return (String) standardCache.get(string);
		}
	}

	public static String unicode10ToWord(String unicode) {
		try {
			if (!unicode.contains("&#")) {
				return unicode;
			} else {
				String[] ss = unicode.split("&#");
				String newString = "";
				String[] arrayOfString1 = ss;
				int j = ss.length;
				int i = 0;
				if (j == 0) {
					return newString;
				} else {
					do {
						String s = arrayOfString1[i];
						if (containsNumber(s) && s.contains(";")) {
							int index = s.indexOf(";");
							String number = s.substring(0, index);
							String s1 = "";
							int a = Integer.parseInt(number, 10);
							s1 = s1 + (char) a;
							newString = newString + s1 + s.substring(index + 1);
						} else {
							newString = newString + s;
						}

						++i;
					} while (i < j);

					return newString;
				}
			}
		} catch (Exception arg10) {
			return unicode;
		}
	}

	public static boolean standardEquals(String s1, String s2) {
		return getStandardString(s1).equals(getStandardString(s2));
	}

	public static boolean containsNumber(String source) {
		for (char i = 48; i < 58; ++i) {
			if (source.contains(String.valueOf(i))) {
				return true;
			}
		}

		return false;
	}

	public static String getPascalString(String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}

	public static String[] splitRemainRex(String string, String[] rexs) {
		List<String> list = new ArrayList<>();
		Set<String> set = new HashSet<>();
		String[] i = rexs;
		int newList = rexs.length;

		String s;
		for (int it = 0; it < newList; ++it) {
			s = i[it];
			set.add(s);
		}

		list.add(string);

		for (int ii = 0; ii < rexs.length; ++ii) {
			List<String> result = new ArrayList<>();
			for (String str : list) {
				if (set.contains(str)) {
					result.add(str);
				} else {
					result.addAll(Arrays.asList(splitRemainRex(str, rexs[ii])));
				}
			}

			list = result;
		}

		return list.toArray(new String[0]);
	}

	public static String[] splitRemainRex(String source, String rex) {
		String[] ss = split(source, rex);
		List<String> list = new ArrayList<>();

		for (int i = 0; i < ss.length; ++i) {
			if (ss[i].length() > 0) {
				list.add(ss[i].trim());
			}

			if (i < ss.length - 1) {
				list.add(rex);
			}
		}

		return list.toArray(new String[0]);
	}

	public static String[] split(String string, String rex) {
		int[] indexs = getAllIndex(string, rex);
		List<String> list = new ArrayList<>();

		for (int i = 0; i < indexs.length; ++i) {
			if (i == 0) {
				list.add(string.substring(0, indexs[i]));
			}

			if (i < indexs.length - 1) {
				list.add(string.substring(indexs[i] + rex.length(), indexs[i + 1]));
			} else if (i == indexs.length - 1) {
				list.add(string.substring(indexs[i] + rex.length()));
			}
		}

		if (indexs.length == 0) {
			list.add(string);
		}

		return (String[]) ((String[]) list.toArray(new String[0]));
	}

	public static String[] splitWithoutBlank(String string, String rex) {
		int[] indexs = getAllIndex(string, rex);
		List<String> list = new ArrayList<>();

		for (int i = 0; i < indexs.length; ++i) {
			if (i == 0 && !isNullOrEmpty(string.substring(0, indexs[i]))) {
				list.add(string.substring(0, indexs[i]));
			}

			if (i < indexs.length - 1) {
				if (!isNullOrEmpty(string.substring(indexs[i] + rex.length(), indexs[i + 1]))) {
					list.add(string.substring(indexs[i] + rex.length(), indexs[i + 1]));
				}
			} else if (i == indexs.length - 1 && !isNullOrEmpty(string.substring(indexs[i] + rex.length()))) {
				list.add(string.substring(indexs[i] + rex.length()));
			}
		}

		if (indexs.length == 0 && !isNullOrEmpty(string)) {
			list.add(string);
		}

		return list.toArray(new String[0]);
	}

	public static String[] split(String string, String[] rexs) {
		List<String> list = new ArrayList<>();
		list.add(string);

		for (int i = 0; i < rexs.length; ++i) {
			List<String> newList = new ArrayList<>();
			for (String str : list) {
				newList.addAll(Arrays.asList(split(str, rexs[i])));
			}
			list = newList;
		}

		return list.toArray(new String[0]);
	}

	public static boolean isChineseCharacter(char ch) {
		String string = String.valueOf(ch);
		return chinesePattern.matcher(string).find();
	}

	public static String getStringFromPascal(String string) {
		return string.substring(0, 1).toLowerCase() + string.substring(1);
	}

	public static String getStringFromStrings(String[] strings) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < strings.length; ++i) {
			String string = strings[i];
			buf.append(string);
			buf.append(" ");
		}
		return buf.toString().trim();
	}

	public static String getStringFromStringsWithUnique(String[] strings) {
		StringBuffer buf = new StringBuffer();
		if (strings.length > 0) {
			for (int i = 0; i < strings.length - 1; ++i) {
				buf.append(strings[i]);
				buf.append("어");
			}

			buf.append(strings[strings.length - 1]);
		}

		return buf.toString().trim();
	}

	public static String getStringFromStrings(List<String> list, String spliter) {
		return getStringFromStrings((String[]) ((String[]) list.toArray(new String[0])), spliter);
	}

	public static String getStringFromStrings(String[] strings, String spliter) {
		if (strings != null && strings.length != 0) {
			if (spliter == null) {
				spliter = "";
			}
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < strings.length; ++i) {
				String string = strings[i];
				buf.append(string);
				buf.append(spliter);
			}
			return buf.toString().substring(0, buf.toString().length() - spliter.length());
		} else {
			return "";
		}
	}

	public static String[] getStringsFromString(String stirng, String spliter) {
		return stirng.split(spliter);
	}

	public static boolean isNullOrEmpty(String string) {
		return string == null || string.trim().length() == 0;
	}

	public static String removeWhiteSpace(String string) {
		if (isNullOrEmpty(string)) {
			return "";
		} else {
			string = string.replace(" ", "");
			string = string.replace("\t", "");
			return string;
		}
	}

	public static boolean isCharOrNumberString(String string) {
		char[] cs = string.toCharArray();
		for (int i = 0; i < cs.length; ++i) {
			char c = cs[i];
			if (!Character.isDigit(c) && !isEnglishCharacter(c)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNumberString(String string) {
		char[] cs = string.toCharArray();
		for (int i = 0; i < cs.length; ++i) {
			char c = cs[i];
			if (!Character.isDigit(c) && c != 46) {
				return false;
			}
		}

		return true;
	}

	public static boolean isIpAddress(String str) {
		Matcher matcher = IP_PATTERN.matcher(str);
		return matcher.matches();
	}

	public static boolean isEnglishCharacter(char ch) {
		String a = String.valueOf(ch).toLowerCase();
		return a.charAt(0) >= 97 && a.charAt(0) <= 122;
	}

	public static boolean isEnglishString(String string) {
		string = getStandardString(string);
		char[] cs = string.toCharArray();
		for (int i = 0; i < cs.length; ++i) {
			char c = cs[i];
			if (c < 97 || c > 122) {
				return false;
			}
		}

		return true;
	}

	public static boolean isEnglishOrNumberCharacter(char ch) {
		return isEnglishCharacter(ch) || Character.isDigit(ch);
	}

	public static boolean isNumberCharacter(char ch) {
		return Character.isDigit(ch);
	}

	public static boolean containsChinese(String word) {
		if (isNullOrEmpty(word)) {
			return false;
		} else {
			int i = 0;
			while (!isChineseCharacter(word.charAt(i))) {
				++i;
				if (i >= word.length()) {
					return false;
				}
			}

			return true;
		}
	}

	public static String removeParenthesis(String source) {
		return removeParenthesis(source, new String[] { "(" }, new String[] { ")" });
	}

	public static String removeParenthesis(String source, String[] starts, String[] ends) {
		for (int si = 0; si < starts.length; ++si) {
			source = source.replace(starts[si], "(");
			source = source.replace(ends[si], ")");
		}

		int[] arg11 = getAllIndex(source, "(");
		int[] ei = getAllIndex(source, ")");
		String ns = "";
		if (arg11.length <= 0) {
			return source;
		} else {
			int es = 0;
			boolean find = false;
			int[] lastposition = ei;
			int j1 = ei.length;

			for (int arg9 = 0; arg9 < j1; ++arg9) {
				int e = lastposition[arg9];
				if (e > arg11[0]) {
					find = true;
					break;
				}

				++es;
			}

			int arg12 = 0;
			if (find) {
				for (j1 = 0; j1 < arg11.length && es < ei.length; ++j1) {
					if (arg11[j1] >= arg12) {
						ns = ns + " " + source.substring(arg12, arg11[j1]);
						arg12 = ei[es] + 1;
						++es;
					}
				}
			}

			ns = ns + " " + source.substring(arg12);
			return ns.trim();
		}
	}

	public static boolean containsEnglishOrNumber(String word) {
		if (isNullOrEmpty(word)) {
			return false;
		} else {
			int i = 0;

			while (!isEnglishOrNumberCharacter(word.charAt(i))) {
				++i;
				if (i >= word.length()) {
					return false;
				}
			}

			return true;
		}
	}

	public static boolean containsEnglish(String word) {
		if (isNullOrEmpty(word)) {
			return false;
		} else {
			int i = 0;

			while (!isEnglishCharacter(word.charAt(i))) {
				++i;
				if (i >= word.length()) {
					return false;
				}
			}

			return true;
		}
	}

	public static boolean isAllChineseCharacter(String word) {
		if (isNullOrEmpty(word)) {
			return false;
		} else {
			int i = 0;

			while (isChineseCharacter(word.charAt(i))) {
				++i;
				if (i >= word.length()) {
					return true;
				}
			}

			return false;
		}
	}

	public static int[] getAllInDependentIndex(String source, String rex) {
		List<Integer> list = new ArrayList<>();
		if (!isCharOrNumberString(rex)) {
			return getAllIndex(source, rex);
		} else {
			int i;
			for (int ins = 0; ins < source.length(); ins = i + 1) {
				i = source.indexOf(rex, ins);
				if (i <= -1) {
					break;
				}

				if (i > 0 && i + rex.length() < source.length()) {
					if (!isEnglishOrNumberCharacter(source.charAt(i - 1))
							&& !isEnglishOrNumberCharacter(source.charAt(i + rex.length()))) {
						list.add(Integer.valueOf(source.indexOf(rex, ins)));
					}
				} else if (i > 0) {
					if (!isEnglishOrNumberCharacter(source.charAt(i - 1))) {
						list.add(Integer.valueOf(source.indexOf(rex, ins)));
					}
				} else if (i + rex.length() < source.length()) {
					list.add(Integer.valueOf(source.indexOf(rex, ins)));
				} else if (i + rex.length() == source.length()) {
					list.add(Integer.valueOf(source.indexOf(rex, ins)));
				}
			}

			int[] result = new int[list.size()];
			for (i = 0; i < result.length; ++i) {
				result[i] = list.get(i).intValue();
			}

			return result;
		}
	}

	public static int[] getAllIndex(String source, String rex) {
		List<Integer> list = new ArrayList<>();

		int ins;
		for (int position = 0; position < source.length(); position = ins + 1) {
			ins = source.indexOf(rex, position);
			if (ins <= -1) {
				break;
			}

			list.add(Integer.valueOf(source.indexOf(rex, position)));
		}

		int[] result = new int[list.size()];

		for (int i = 0; i < result.length; ++i) {
			result[i] = list.get(i).intValue();
		}

		return result;
	}

	public static int[] getAllIndex(String source, String[] rexs) {
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < rexs.length; ++i) {
			String s = rexs[i];
			int[] indexs = getAllIndex(source, s);

			for (int j = 0; j < indexs.length; ++j) {
				int index = indexs[j];
				list.add(Integer.valueOf(index));
			}
		}

		int[] result = new int[list.size()];

		for (int j = 0; j < result.length; ++j) {
			result[j] = list.get(j).intValue();
		}

		return result;
	}

	public static String reverseString(String string) {
		if (isNullOrEmpty(string)) {
			return "";
		} else {
			StringBuffer sb = new StringBuffer();

			for (int i = 1; i <= string.length(); ++i) {
				sb.append(string.charAt(string.length() - i));
			}

			return sb.toString();
		}
	}

	public static String getNotNullValue(String string) {
		if (string == null) {
			string = "";
		}

		return string;
	}

	public static String qBchange(String QJstr) {
		if (isNullOrEmpty(QJstr)) {
			return "";
		} else {
			char[] c = QJstr.toCharArray();

			for (int i = 0; i < c.length; ++i) {
				if (c[i] == 12288) {
					c[i] = 32;
				} else if (c[i] > '＀' && c[i] < '｟') {
					c[i] -= 'ﻠ';
				}
			}

			return new String(c);
		}
	}

	public static String chineseFJChange(String fanString) {
		if (isNullOrEmpty(fanString)) {
			return "";
		} else {
			ChineseJF chinesdJF = CJFBeanFactory.getChineseJF();
			String janText = chinesdJF.chineseFan2Jan(fanString);
			return janText;
		}
	}

	public static String removeSpecialChars(String str) {
		String regEx = "[`~!@#$%^&* ()_+=|{}\':;\',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Matcher m = null;

		try {
			Pattern p = Pattern.compile(regEx);
			m = p.matcher(str);
		} catch (PatternSyntaxException arg3) {
			arg3.printStackTrace();
		}

		return m.replaceAll("").trim();
	}

	public static String getTimeString(String time, int length) {
		if (time.contains(".")) {
			int dl = time.length() - time.indexOf(".") - 1;
			if (dl > length) {
				time = time.substring(0, time.length() - dl + length);
			}
		}

		return time;
	}

	public static boolean parseBoolean(String bool) {
		try {
			return Boolean.parseBoolean(bool);
		} catch (Exception arg1) {
			return false;
		}
	}

	public static int getChineseLength(String string) {
		if (string == null) {
			return 0;
		} else {
			int length = 0;
			char[] chars = string.toCharArray();
			for (int i = 0; i < chars.length; ++i) {
				char c = chars[i];
				if (isChineseCharacter(c)) {
					++length;
				}
			}
			return length;
		}
	}

	public static int getEnglishLength(String string) {
		if (string == null) {
			return 0;
		} else {
			int length = 0;
			char[] chars = string.toCharArray();

			for (int i = 0; i < chars.length; ++i) {
				char c = chars[i];
				if (isEnglishCharacter(c)) {
					++length;
				}
			}

			return length;
		}
	}

	public static String getBlankString(int len) {
		String s = "";

		for (int i = 0; i < len; ++i) {
			s = s + " ";
		}

		return s;
	}

	public static String getOriginalStringFromStandardString(String originalString, String standardString) {
		standardString = standardString.trim();
		if (sameTypeCharacter(originalString, standardString)) {
			String ss = getStandardString(originalString);
			String[] ws = CommonSpliter.split(ss, standardString.length());
			for (int i = 0; i < ws.length; ++i) {
				String w = ws[i];
				if (w.equals(standardString)) {
					return originalString.substring(ss.indexOf(w), ss.indexOf(w) + standardString.length());
				}
			}
			for (int i = 0; i < ws.length; ++i) {
				String w = ws[i];
				if (pinyinEquals(w, standardString)) {
					return originalString.substring(ss.indexOf(w), ss.indexOf(w) + standardString.length());
				}
			}
		}

		return "";
	}

	public static boolean sameTypeCharacter(String a, String b) {
		return a != null && b != null
				? (containsChinese(a) && containsChinese(b) ? true
						: (containsEnglish(a) && containsEnglish(b) ? true : containsNumber(a) && containsNumber(b)))
				: false;
	}

	public static boolean pinyinEquals(String a, String b) {
		if (a != null && b != null) {
			a = getStandardString(a);
			b = getStandardString(b);
			return PinyinUtil.getPinyinString(a).equals(PinyinUtil.getPinyinString(b));
		} else {
			return false;
		}
	}

	public static boolean stringEquals(String a, String b) {
		return a == null && b == null ? true : (a != null && b != null ? a.equals(b) : false);
	}

	public static String replaceFirst(String string, String source, String target) {
		int[] ids = getAllIndex(string, source);
		return ids.length > 0 ? string.substring(0, ids[0]) + target + string.substring(ids[0] + source.length())
				: string;
	}

	public static String replaceOnce(String source, String rex) {
		if (isNullOrEmpty(rex)) {
			return source;
		} else if (!getStandardString(source).contains(getStandardString(rex))) {
			return source;
		} else {
			boolean replaced = false;
			boolean loop = true;

			while (true) {
				int[] ids;
				while (loop) {
					loop = false;
					ids = getAllIndex(source, rex);
					for (int i = 0; i < ids.length; ++i) {
						int i1 = ids[i];
						if (isOneSideAbsoluteWord(source, rex.charAt(0), i1 - 1)
								&& isOneSideAbsoluteWord(source, rex.charAt(rex.length() - 1), i1 + rex.length())) {
							source = source.substring(0, i1) + " "
									+ source.substring(i1 + rex.length(), source.length());
							replaced = true;
							loop = true;
							break;
						}
					}
				}

				ids = getAllIndex(source, rex);

				for (int i = 0; i < ids.length; ++i) {
					int value = ids[i];
					if ((isOneSideAbsoluteWord(source, rex.charAt(0), value - 1)
							|| isOneSideAbsoluteWord(source, rex.charAt(rex.length() - 1), value + rex.length()))
							&& !replaced) {
						return source.substring(0, value) + " "
								+ source.substring(value + rex.length(), source.length());
					}
				}

				return source;
			}
		}
	}

	private static boolean isOneSideAbsoluteWord(String source, char rex, int i) {
		return i < 0 || i >= source.length() || !isEnglishCharacter(rex) && !isNumberCharacter(rex)
				|| !isEnglishCharacter(source.charAt(i)) && !isNumberCharacter(source.charAt(i));
	}

	public static String getTableField(String objectField) {
		if (isNullOrEmpty(objectField)) {
			throw new RuntimeException("empty string is not allowed");
		} else {
			try {
				char[] e = objectField.toCharArray();
				List<Integer> ids = new ArrayList<>();

				for (int sb = 0; sb < e.length; ++sb) {
					if (e[sb] >= 65 && e[sb] <= 90) {
						ids.add(Integer.valueOf(sb));
					}
				}

				if (ids.size() == 0) {
					return objectField;
				} else {
					StringBuffer result = new StringBuffer();
					result.append(objectField.substring(0, ((Integer) ids.get(0)).intValue()));

					for (int i = 0; i < ids.size() - 1; ++i) {
						result.append("_" + objectField
								.substring(((Integer) ids.get(i)).intValue(), ((Integer) ids.get(i + 1)).intValue())
								.toLowerCase());
					}

					result.append("_" + objectField
							.substring(((Integer) ids.get(ids.size() - 1)).intValue(), objectField.length())
							.toLowerCase());
					return result.toString();
				}
			} catch (Exception arg4) {
				throw new RuntimeException(arg4);
			}
		}
	}

	public static String[] getSeparatedString(String s, boolean keepMark) {
		List<String> list = new ArrayList<>();
		char[] cs = s.toCharArray();
		int lastType = -1;
		String currentString = "";

		for (int i = 0; i < cs.length; ++i) {
			if (i == 0) {
				currentString = String.valueOf(cs[i]);
				lastType = getType(cs[i]);
			} else {
				int type = getType(cs[i]);
				if (type == lastType) {
					currentString = currentString + cs[i];
				} else {
					if ((lastType > 0 || keepMark) && !isNullOrEmpty(currentString)) {
						list.add(currentString);
					}

					currentString = String.valueOf(cs[i]);
					lastType = type;
				}
			}

			if (i == cs.length - 1 && (lastType > 0 || keepMark) && !isNullOrEmpty(currentString)) {
				list.add(currentString);
			}
		}

		return list.toArray(new String[0]);
	}

	private static int getType(char c) {
		return isEnglishCharacter(c) ? 1 : (!isNumberCharacter(c) && c != 46 ? (isChineseCharacter(c) ? 3 : -1) : 2);
	}

	public static int getDependency(String source, String word, int start) {
		int dependency = 0;
		if (word.length() <= 0) {
			return -1;
		} else {
			String right;
			String right2;
			if (start == 0) {
				++dependency;
			} else {
				right = String.valueOf(source.charAt(start - 1));
				right2 = source.substring(Math.min(0, start - 2), start);
				if (!isSameType(word.substring(0, 1), right)) {
					if (containsEnglishOrNumber(word) && right.equals("-") && containsEnglishOrNumber(right2)) {
						dependency += 0;
					} else {
						++dependency;
					}
				}
			}

			if (start + word.length() == source.length()) {
				++dependency;
			} else {
				right = source.substring(start + word.length(), start + word.length() + 1);
				right2 = source.substring(start + word.length(), Math.min(source.length(), start + word.length() + 2));
				if (!isSameType(word.substring(word.length() - 1), right)) {
					if (containsEnglishOrNumber(word) && right.equals("-") && containsEnglishOrNumber(right2)) {
						dependency += 0;
					} else {
						++dependency;
					}
				}
			}

			return dependency;
		}
	}

	private static boolean isSameType(String s1, String s2) {
		return isCharOrNumberString(s1) && isCharOrNumberString(s2)
				|| isAllChineseCharacter(s1) && isAllChineseCharacter(s2);
	}

	public static String getMeanfullString(String s) {
		if (isNullOrEmpty(s)) {
			return "";
		} else {
			List<String> list = new ArrayList<>();
			char[] chars = s.toCharArray();
			for (int i = 0; i < chars.length; ++i) {
				char c = chars[i];
				if (isChineseCharacter(c) || isEnglishOrNumberCharacter(c)) {
					list.add(String.valueOf(c));
				}
			}

			return getStringFromStrings(list, "");
		}
	}

	public static boolean isMeanfullString(String s) {
		char[] chars = s.toCharArray();
		
		for (int i = 0; i < chars.length; ++i) {
			char c = chars[i];
			if (isChineseCharacter(c) || isEnglishOrNumberCharacter(c)) {
				return true;
			}
		}

		return false;
	}

	public static String replace(String temple, HashMap<String, String> map) {
		Map<Integer, String> indexMap = new HashMap<>();
		Set<Integer> set = new HashSet<>();
		Iterator<String> s = map.keySet().iterator();

		while (true) {
			String tmp;
			int index;
			do {
				if (!s.hasNext()) {
					String resStr = "";

					for (int i = 0; i < temple.length(); ++i) {
						if (!set.contains(Integer.valueOf(i))) {
							resStr = resStr + temple.charAt(i);
						} else if (indexMap.containsKey(Integer.valueOf(i))) {
							resStr = resStr + (String) map.get(indexMap.get(Integer.valueOf(i)));
						}
					}

					return resStr;
				}

				tmp = s.next();
				index = temple.indexOf(tmp);
			} while (index < 0);

			indexMap.put(Integer.valueOf(index), tmp);

			for (int i1 = index; i1 < index + tmp.length(); ++i1) {
				set.add(Integer.valueOf(i1));
			}
		}
	}

	public static String replaceAll(String temple, HashMap<String, String> map) {
		String t = temple;
		String key;
		for (Iterator<String> its = map.keySet().iterator(); its.hasNext(); t = t.replace(key, (CharSequence) map.get(key))) {
			key = its.next();
		}
		return t;
	}

	public static String[] getSameTypeString(String source, IStringCondition condition) {
		String s = "";

		for (int ss = 0; ss < source.length(); ++ss) {
			char list = source.charAt(ss);
			if (condition.meet(String.valueOf(list))) {
				s = s + list;
			} else {
				s = s + " ";
			}
		}

		String[] strs = split(s, " ");
		List<String> result = new ArrayList<>();

		for (int i = 0; i < strs.length; ++i) {
			String vs = strs[i];
			if (!isNullOrEmpty(vs)) {
				result.add(vs);
			}
		}

		return result.toArray(new String[0]);
	}

	public static boolean contains(String big, String small, String spliter) {
		String[] ss = big.split(spliter);
		for (int i = 0; i < ss.length; ++i) {
			String s = ss[i];
			if (s.trim().toLowerCase().equals(small.trim().toLowerCase())) {
				return true;
			}
		}

		return false;
	}

	public static String[] split(String source, String front, String back) {
		int[] fs = getAllIndex(source, front);
		int[] bs = getAllIndex(source, back);
		List<String> list = new ArrayList<>();

		for (int i = 0; i < fs.length; ++i) {
			int f1 = fs[i];
			for (int j = i + 1; j <= fs.length; ++j) {
				int end = tryNext(source, f1, i, fs, bs, list, j);
				if (end >= 0) {
					if (i == 0) {
						list.add(0, source.substring(0, fs[i]));
					} else if (end == bs.length - 1) {
						list.add(source.substring(bs[end] + 1, source.length()));
					} else if (end == bs.length) {
						list.add(source.substring(bs[end - 1] + 1, source.length()));
					}
					i = j - 1;
					break;
				}
			}
		}

		return list.toArray(new String[0]);
	}

	private static int tryNext(String source, int f1, int i, int[] fs, int[] bs, List<String> list, int end) {
		int f2 = source.length();
		if (end < fs.length) {
			f2 = fs[end];
		}

		for (int j = bs.length - 1; j >= 0; --j) {
			int b = bs[j];
			if (f1 < b && f2 > b) {
				list.add(source.substring(f1 + 1, b));
				return j;
			}
		}

		return -1;
	}

	public static String[] splitStrict(String source, String front, String back) {
		int[] fs = getAllIndex(source, front);
		int[] bs = getAllIndex(source, back);
		if (fs.length == bs.length) {
			for (int list = 0; list < bs.length; ++list) {
				if (fs[list] > bs[list]) {
					return new String[0];
				}
			}

			List<String> result = new ArrayList<>();

			for (int i = 0; i < fs.length; ++i) {
				String head = "";
				if (i == 0) {
					head = source.substring(0, fs[i]);
				} else {
					head = source.substring(bs[i - 1] + 1, fs[i]);
				}

				if (isMeanfullString(head)) {
					result.add(head);
				}

				result.add(source.substring(fs[i] + 1, bs[i]));
			}

			if (bs[bs.length - 1] < source.length()) {
				result.add(source.substring(bs[bs.length - 1] + 1));
			}

			return result.toArray(new String[0]);
		} else {
			return new String[0];
		}
	}
}