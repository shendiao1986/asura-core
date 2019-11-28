package com.asura.tools.util.international;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;

public class ChineseDetermination {
	public static int GB2312 = 0;
	public static int GBK = 1;
	public static int UTF8 = 2;
	public static int UNKNOWN = 3;
	public static int TOTALT = 4;
	public static String[] charSetName;

	public ChineseDetermination() {
		charSetName = new String[TOTALT];
		charSetName[GB2312] = "GB2312";
		charSetName[GBK] = "GBK";
		charSetName[UTF8] = "UTF8";
		charSetName[UNKNOWN] = "ISO8859_1";
	}

	public int compare(String key) {
		String baseCharPath = "chineseSet";
		Configuration config = Configuration.getConfiguration(baseCharPath);
		String baseCharString = config.getValue("baseChar");
		int count = 0;
		try {
			byte[] baseCharBytes = baseCharString.getBytes("ISO8859-1");
			baseCharString = new String(baseCharBytes, "utf-8");
			for (int i = 0; i < key.length(); ++i) {
				for (int j = 0; j < baseCharString.length(); ++j)
					if (key.charAt(i) == baseCharString.charAt(j))
						++count;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return count;
	}

	public String getEncodeByBytes(byte[] content) {
		if (content == null) {
			return charSetName[UNKNOWN];
		}
		int maxscore = 0;
		int encoding = UNKNOWN;
		int[] scores = new int[TOTALT];
		try {
			String gbkString = new String(content, "GBK");
			String gb2312String = new String(content, "GB2312");
			String utf8String = new String(content, "UTF-8");
			scores[GB2312] = compare(gb2312String);
			scores[GBK] = compare(gbkString);
			scores[UTF8] = compare(utf8String);
			for (int index = 0; index < TOTALT; ++index) {
				System.out.println(scores[index]);
				if (scores[index] > maxscore) {
					encoding = index;
					maxscore = scores[index];
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return charSetName[encoding];
	}

	public String getEncodeByFile(String filePath) {
		File file = new File(filePath);

		String encoding = "";
		try {
			InputStream in = new FileInputStream(file);
			byte[] a = new byte[8192];
			in.read(a);
			encoding = getEncodeByBytes(a);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (encoding.length() == 0) {
			return charSetName[UNKNOWN];
		}
		return encoding;
	}

	public String getEncodeByString(String key) {
		String encoding = "";
		try {
			byte[] a = key.getBytes();
			encoding = getEncodeByBytes(a);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (encoding.length() == 0) {
			return charSetName[UNKNOWN];
		}
		return encoding;
	}

	public String getEncodeByUrl(String urlString) {
		String encoding = "";
		try {
			URL url = new URL(urlString);
			InputStream in = url.openStream();
			int count = 0;
			while (count == 0) {
				count = in.available();
			}
			byte[] a = new byte[8192];

			in.read(a);
			encoding = getEncodeByBytes(a);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (encoding.length() == 0) {
			return charSetName[UNKNOWN];
		}
		return encoding;
	}

	public boolean isChinese(String key) {
		StringBuffer sb = new StringBuffer();
		char[] ch = key.trim().toCharArray();
		for (int i = 0; i < ch.length; ++i) {
			char c = ch[i];

			if (!(Character.isLetter(c)))
				continue;
			String tempString = String.valueOf(c);
			if (!(tempString.matches("[a-zA-Z]"))) {
				sb.append(c);
			}
		}

		System.out.println(sb.toString());
		float chLength = sb.toString().length();
		int count = compare(sb.toString());
		float result = count / chLength;

		return (result > 0.4D);
	}

	public static void main(String[] args) {
		ChineseDetermination compare = new ChineseDetermination();

		String aa = "[ti:快乐四季][ar:xuer8037][00:42.62]春是个刚会跑的小娃娃[00:46.81]嫩绿色的衣服短短的头发[00:53.81]一张粉红色的桃花脸[00:55.95]咿呀学语挪步伐[00:59.15][01:00.30]夏是个美如玉的俏姑娘[01:04.59]多彩的衣裙长长的秀发[01:08.98][01:26.48]一张清秀的荷花脸[01:13.27][01:35.42]超凡脱俗气质佳[01:16.92][01:57.42]秋是个端庄成熟好妈妈[02:01.86]金黄色的衣裳高挽起云发[02:06.25]一张慈祥的菊花脸[02:10.54]充满深情多优雅[02:15.17][02:15.82]冬是个豪爽博爱老爸爸[02:19.37]银白色的衣衫灰色的华发[02:23.85][02:41.53][03:03.40]一张傲气的梅花脸[02:28.25][02:50.21][03:12.33]饱经风霜胸襟大";
		String keyString = "锘縖ti:鍒氬垰鏀炬墜][ar:鑸炲ぇ鎵琞[00:08.46]杩囧勾鐨勬椂鍊欐垜浠繕浜嗘椂闂?[00:13.43]鐒扮伀涔嬪悗鐨勫瘋闈欓?佽蛋绁炰粰[00:18.47]鍗疯捣鐝犲笜鐨勬俯鏌旈棶鍊?[00:23.14]杞诲徆鐫?闂茶姳钀藉湴鐨勯挓鐐?[00:29.55]绂诲紑鐨勮绾夸技涔庡繕浜嗗湴鐐?[00:34.83]鐔勭伃涔嬪墠鐨勭儹搴︽俯鏆栧弻鐪?[00:40.24]鎾╄捣杩囧線鐨勯洩鐧借。琚?[00:45.53]鑸掑睍鐫?缁嗛洦婀胯。鐨勬槬澶?[02:05.95][00:51.15]浜戦敠涓婄殑渚濋潬缁ｄ簡涓?骞村張涓?骞?[02:11.29][00:56.43]瓒婃潵瓒婃竻鏅板氨瓒婃潵瓒婇仴杩?[02:17.02][01:02.03]鏁呴亾涓婄殑鑼堕椋樹簡涓?骞村張涓?骞?[02:22.49][01:06.95]璁板緱鍝竴澶╃殑鐑熻姳[02:25.42][01:10.37]鏄犵孩浜嗕綘鐨勮劯[02:28.46][01:13.29]绐楄姳涓婄殑楦抽腐鍓簡涓?骞村張涓?骞?[02:33.81][01:18.77]瓒婃潵瓒婄簿鑷村氨瓒婃潵瓒婅偆娴?[02:39.54][01:24.12]楹︾涓婄殑楗哄瓙涓嬩簡涓?骞村張涓?骞?[02:45.20][01:30.03]璁板緱鍝竴澶╃殑绾㈡灒[02:48.06][01:33.02]鐣欎笅浜嗗ス鐨勭敎[01:43.63]杩囧勾鐨勬椂鍊欎技涔庡繕浜嗘椂闂?[01:48.79]闀夸涵涔嬪鐨勫甯甫璧板瘨鏆?[01:54.45]鎺?璧峰箷鍚庣殑鏇茬粓浜烘暎[02:00.05]鍛煎惛鐫?绐楀濂戒釜鑹抽槼澶?";

		System.out.println(compare.isChinese(aa));
		System.out.println(compare.isChinese(keyString));
	}
}
