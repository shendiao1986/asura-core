package org.asura.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.asura.core.util.spliter.CommonSpliter;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

public class PinyinUtil {
	private static Hashtable<String, String> endTable = new Hashtable<>();

	private static Hashtable<String, String> startTable = new Hashtable<>();

	private static Hashtable<String, String> rendTable = new Hashtable<>();

	private static Hashtable<String, String> rstartTable = new Hashtable<>();

	private static Hashtable<Character, String[]> resultCache = new Hashtable<>();
	private static String[] yinjie;

	static {
		startTable.put("zh", "z");
		startTable.put("ch", "c");
		startTable.put("sh", "s");
		endTable.put("ang", "an");
		endTable.put("ong", "on");
		endTable.put("eng", "en");
		endTable.put("ing", "in");
		endTable.put("iang", "ian");
		endTable.put("uang", "uan");
		startTable.put("r", "l");
		startTable.put("l", "n");
		startTable.put("h", "f");

		rstartTable.put("z", "zh");
		rstartTable.put("c", "ch");
		rstartTable.put("s", "sh");
		rendTable.put("an", "ang");
		rendTable.put("en", "eng");
		rendTable.put("on", "ong");
		rendTable.put("in", "ing");
		rendTable.put("ian", "iang");
		rendTable.put("uan", "uang");
		rstartTable.put("l", "r");
		rstartTable.put("n", "l");
		rstartTable.put("f", "h");

		yinjie = new String[] { "a", "e", "ai", "an", "ang", "ao", "ba", "bai", "ban", "bang", "bao", "bei", "ben",
				"beng", "bi", "bian", "biao", "bie", "bin", "bing", "bo", "bu", "ca", "cai", "can", "cang", "cao", "ce",
				"cen", "ceng", "cha", "chai", "chan", "chang", "chao", "che", "chen", "cheng", "chi", "chon", "chong",
				"chou", "chu", "chuai", "chuan", "chuang", "chui", "chun", "chuo", "ci", "con", "cong", "cou", "cu",
				"cuan", "cui", "cun", "cuo", "da", "dai", "dan", "dang", "dao", "de", "den", "deng", "di", "dian",
				"diao", "die", "din", "ding", "diu", "don", "dong", "dou", "du", "duan", "dui", "dun", "duo", "en",
				"er", "fa", "fan", "fang", "fei", "fen", "feng", "fo", "fou", "fu", "ga", "gai", "gan", "gang", "gao",
				"ge", "gei", "gen", "geng", "gon", "gong", "gou", "gu", "gua", "guai", "guan", "guang", "gui", "gun",
				"guo", "ha", "hai", "han", "hang", "hao", "he", "hei", "hen", "heng", "hon", "hong", "hou", "hu", "hua",
				"huai", "huan", "huang", "hui", "hun", "huo", "ji", "jia", "jian", "jiang", "jiao", "jie", "jin",
				"jing", "jion", "jiong", "jiu", "ju", "juan", "jue", "jun", "ka", "kai", "kan", "kang", "kao", "ke",
				"ken", "keng", "kon", "kong", "kou", "ku", "kua", "kuai", "kuan", "kuang", "kui", "kun", "kuo", "la",
				"lai", "lan", "lang", "lao", "le", "lei", "len", "leng", "li", "lia", "lian", "liang", "liao", "lie",
				"lin", "ling", "liu", "lon", "long", "lou", "lu", "lv", "luan", "lue", "lun", "luo", "ma", "mai", "man",
				"mang", "mao", "me", "mei", "men", "meng", "mi", "mian", "miao", "mie", "min", "ming", "miu", "mo",
				"mou", "mu", "na", "nai", "nan", "nang", "nao", "ne", "nei", "nen", "neng", "ni", "nian", "niang",
				"niao", "nie", "nin", "ning", "niu", "non", "nong", "nu", "nv", "nuan", "nue", "nuo", "ou", "pa", "pai",
				"pan", "pang", "pao", "pei", "pen", "peng", "pi", "pian", "piao", "pie", "pin", "ping", "po", "pu",
				"qi", "qia", "qian", "qiang", "qiao", "qie", "qin", "qing", "qion", "qiong", "qiu", "qu", "quan", "que",
				"qun", "ran", "rang", "rao", "re", "ren", "reng", "ri", "ron", "rong", "rou", "ru", "ruan", "rui",
				"run", "ruo", "sa", "sai", "san", "sang", "sao", "se", "sen", "seng", "sha", "shai", "shan", "shang",
				"shao", "she", "shen", "sheng", "shi", "shou", "shu", "shua", "shuai", "shuan", "shuang", "shui",
				"shun", "shuo", "si", "son", "song", "sou", "su", "suan", "sui", "sun", "suo", "ta", "tai", "tan",
				"tang", "tao", "te", "ten", "teng", "ti", "tian", "tiao", "tie", "tin", "ting", "ton", "tong", "tou",
				"tu", "tuan", "tui", "tun", "tuo", "wa", "wai", "wan", "wang", "wei", "wen", "weng", "wo", "wu", "xi",
				"xia", "xian", "xiang", "xiao", "xie", "xin", "xing", "xion", "xiong", "xiu", "xu", "xuan", "xue",
				"xun", "ya", "yan", "yang", "yao", "ye", "yi", "yin", "ying", "yo", "yon", "yong", "you", "yu", "yuan",
				"yue", "yun", "za", "zai", "zan", "zang", "zao", "ze", "zei", "zen", "zeng", "zha", "zhai", "zhan",
				"zhang", "zhao", "zhe", "zhen", "zheng", "zhi", "zhon", "zhong", "zhou", "zhu", "zhua", "zhuai",
				"zhuan", "zhuang", "zhui", "zhun", "zhuo", "zi", "zon", "zong", "zou", "zu", "zuan", "zui", "zun",
				"zuo", "yon" };
	}

	public static String getPinyinString(String word) {
		HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
		outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		StringBuffer sb = new StringBuffer();
		char[] chars = word.toCharArray();
		for (char cr : chars) {
			try {
				String[] ps = null;
				if (resultCache.containsKey(Character.valueOf(cr))) {
					ps = (String[]) resultCache.get(Character.valueOf(cr));
				} else {
					ps = PinyinHelper.toHanyuPinyinStringArray(cr, outputFormat);
					if (ps != null) {
						resultCache.put(Character.valueOf(cr), ps);
					}
				}
				if (ps == null) {
					sb.append(cr);
					break;
				}
				sb.append(ps[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return sb.toString().replace("/", "");
	}

	private static void multiAnalyzer(String pinYin, String keyword, List<String> result) {
		for (int j = yinjie.length - 1; j >= 0; --j) {
			String str = yinjie[j];
			String keyword1 = keyword.replaceAll(",", "");
			String keyword2 = pinYin.substring(keyword1.length());
			if (keyword2.equals(str))
				result.add(keyword + "," + str);
			else if (keyword2.startsWith(str))
				multiAnalyzer(pinYin, keyword + "," + str, result);
		}
	}

	public static String[][] getMultiYinjie(String pinYin) {
		List<String> pinyinList = new Vector<>();
		multiAnalyzer(pinYin, "", pinyinList);
		String[][] pys = new String[pinyinList.size()][];
		for (int i = 0; i < pinyinList.size(); ++i) {
			pys[i] = ((String) pinyinList.get(i)).substring(1).split(",");
		}
		return pys;
	}

	public static String[] getPinyinStrings(String word) {
		if ((word.length() > 10) || (!(StringUtil.isAllChineseCharacter(word)))) {
			return new String[] { getPinyinString(word) };
		}
		List<String> result = new ArrayList<>();
		List<List<String>> llist = getPinyinList(word);
		for (List<String> list : llist) {
			StringBuilder sb = new StringBuilder();
			for (Iterator<String> localIterator2 = list.iterator(); localIterator2.hasNext();) {
				String o = localIterator2.next();
				sb.append(o);
			}
			result.add(sb.toString());
		}

		return ((String[]) result.toArray(new String[0]));
	}

	public static List<List<String>> getPinyinList(String word) {
		HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
		outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		List<List<String>> llist = new ArrayList<>();
		char[] chars = word.toCharArray();
		for (char cr : chars) {
			try {
				String[] ps = null;
				if (resultCache.containsKey(Character.valueOf(cr))) {
					ps = (String[]) resultCache.get(Character.valueOf(cr));
				} else {
					ps = PinyinHelper.toHanyuPinyinStringArray(cr, outputFormat);
					if (ps != null) {
						resultCache.put(Character.valueOf(cr), ps);
					}
				}
				List<String> list = new ArrayList<>();
				HashSet<String> set = new HashSet<>();
				if (ps != null)
					set.addAll(Arrays.asList(ps));
				else {
					set.add(String.valueOf(cr));
				}
				list.addAll(set);
				llist.add(list);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		llist = CombinationUtil.getCombination(llist);

		return llist;
	}

	public static String getFuzzyPinyin(String word) {
		StringBuilder sb = new StringBuilder();
		String[] ss = CommonSpliter.getChineseEnglishSeparatedString(word);
		for (String s : ss) {
			if (!(s.equals(getPinyinString(s)))) {
				for (char c : s.toCharArray())
					sb.append(fuzzyPinyin(getPinyinString(String.valueOf(c))));
			} else {
				sb.append(fuzzyPinyin(s));
			}
		}

		return sb.toString();
	}

	public static String[] getFuzzyPinyins(String word) {
		if ((word.length() > 5) || (!(StringUtil.isAllChineseCharacter(word)))) {
			return new String[] { getFuzzyPinyin(word) };
		}
		String[] ss = CommonSpliter.getChineseEnglishSeparatedString(word);
		List<String> list = new ArrayList<>();
		List<List<String>> llist = new ArrayList<>();
		int count = 0;
		for (String s : ss) {
			List<String> l = new ArrayList<>();
			if (!(s.equals(getPinyinString(s)))) {
				for (char c : s.toCharArray()) {
					if (count++ < 4) {
						for (String p : getPinyinStrings(String.valueOf(c)))
							l.addAll(Arrays.asList(fuzzyPinyins(p)));
					} else {
						for (String p : getPinyinStrings(String.valueOf(c))) {
							l.add(fuzzyPinyin(p));
						}
					}
					llist.add(l);
					l = new ArrayList<>();
				}
			} else {
				l.add(s);
				llist.add(l);
			}
		}
		llist = CombinationUtil.getCombination(llist);
		for (List<String> l1 : llist) {
			StringBuffer sb = new StringBuffer();
			for (Iterator<String> it = l1.iterator(); it.hasNext();) {
				String o = it.next();
				sb.append(o);
			}
			list.add(sb.toString());
		}

		return list.toArray(new String[0]);
	}

	private static String fuzzyPinyin(String pinyin) {
		for (int i = 1; i < pinyin.length(); ++i) {
			if (startTable.containsKey(pinyin.substring(0, i))) {
				pinyin = ((String) startTable.get(pinyin.substring(0, i))) + pinyin.substring(i, pinyin.length());
			}
		}
		for (int i = 1; i < pinyin.length(); ++i) {
			if (endTable.containsKey(pinyin.substring(pinyin.length() - i, pinyin.length()))) {
				pinyin = pinyin.substring(0, pinyin.length() - i)
						+ ((String) endTable.get(pinyin.substring(pinyin.length() - i, pinyin.length())));
			}
		}

		return pinyin;
	}

	public static String[] fuzzyPinyins(String pinyin) {
		List<String> list = new ArrayList<String>();

		for (int i = 1; i <= pinyin.length(); ++i) {
			if (startTable.containsKey(pinyin.substring(0, i))) {
				list.add(pinyin);
				pinyin = ((String) startTable.get(pinyin.substring(0, i))) + pinyin.substring(i, pinyin.length());
				list.add(pinyin);
			}
		}
		if (list.size() == 0) {
			for (int i = 1; i <= pinyin.length(); ++i) {
				if (rstartTable.containsKey(pinyin.substring(0, i))) {
					list.add(pinyin);
					pinyin = ((String) rstartTable.get(pinyin.substring(0, i))) + pinyin.substring(i, pinyin.length());
					list.add(pinyin);
				}
			}
		}
		if (list.size() == 0) {
			list.add(pinyin);
		}
		List<String> elist = new ArrayList<>();
		for (String py : list) {
			for (int i = 1; i <= py.length(); ++i) {
				if (endTable.containsKey(py.substring(py.length() - i, py.length()))) {
					py = py.substring(0, py.length() - i)
							+ ((String) endTable.get(py.substring(py.length() - i, py.length())));
					elist.add(py);
				}
			}
		}
		if (elist.size() == 0) {
			for (String py : list) {
				for (int i = 1; i <= py.length(); ++i) {
					if (rendTable.containsKey(py.substring(py.length() - i, py.length()))) {
						py = py.substring(0, py.length() - i)
								+ ((String) rendTable.get(py.substring(py.length() - i, py.length())));
						elist.add(py);
					}
				}
			}
		}
		list.addAll(elist);

		return ((String[]) list.toArray(new String[0]));
	}

	public static String[] getPinyinHeaders(String word) {
		if ((word.length() > 10) || (!(StringUtil.isAllChineseCharacter(word)))) {
			return new String[] { getPinyinHeader(word) };
		}
		HashSet<String> result = new HashSet<>();
		List<List<String>> llist = getPinyinList(word);
		for (List<String> list : llist) {
			StringBuilder sb = new StringBuilder();
			for (Iterator<String> localIterator2 = list.iterator(); localIterator2.hasNext();) {
				String s = localIterator2.next();
				if ((s != null) && (s.length() > 0))
					sb.append(s.charAt(0));
				else {
					return new String[] { word };
				}
			}
			result.add(sb.toString());
		}

		return result.toArray(new String[0]);
	}

	public static String getPinyinHeader(String word) {
		StringBuilder sb = new StringBuilder();
		if (!(getPinyinString(word).equals(word))) {
			char[] arrayOfChar;
			int j = (arrayOfChar = word.toCharArray()).length;
			int i = 0;
			while (true) {
				char c = arrayOfChar[i];
				String pinyin = getPinyinString(String.valueOf(c));
				if (pinyin.length() > 0)
					sb.append(pinyin.charAt(0));
				else
					return word;
				++i;
				if (i >= j) {
					return sb.toString();
				}
			}
		}
		return word;
	}
}
