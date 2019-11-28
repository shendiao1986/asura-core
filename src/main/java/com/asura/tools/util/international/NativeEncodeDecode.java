package com.asura.tools.util.international;

public class NativeEncodeDecode {
	public static String native2ascii(String str) {
		char[] chars = str.toCharArray();

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < chars.length; ++i) {
			sb.append(char2Ascii(chars[i]));
		}

		return sb.toString();
	}

	private static String char2Ascii(char c) {
		if (c > 255) {
			StringBuilder sb = new StringBuilder();

			sb.append("\\u");

			int code = c >> '\b';

			String tmp = Integer.toHexString(code);

			if (tmp.length() == 1) {
				sb.append("0");
			}

			sb.append(tmp);

			code = c & 0xFF;

			tmp = Integer.toHexString(code);

			if (tmp.length() == 1) {
				sb.append("0");
			}

			sb.append(tmp);

			return sb.toString();
		}

		return Character.toString(c);
	}

	public static String ascii2native(String str) {
		StringBuilder sb = new StringBuilder();

		int begin = 0;

		int index = str.indexOf("\\u");

		while (index != -1) {
			sb.append(str.substring(begin, index));

			sb.append(ascii2Char(str.substring(index, index + 6)));

			begin = index + 6;

			index = str.indexOf("\\u", begin);
		}

		sb.append(str.substring(begin));

		return sb.toString();
	}

	private static char ascii2Char(String str) {
		if (str.length() != 6) {
			throw new IllegalArgumentException("Ascii string of a native character must be 6 character.");
		}

		if (!("\\u".equals(str.substring(0, 2)))) {
			throw new IllegalArgumentException("Ascii string of a native character must start with \"\\u\".");
		}

		String tmp = str.substring(2, 4);

		int code = Integer.parseInt(tmp, 16) << 8;

		tmp = str.substring(4, 6);

		code += Integer.parseInt(tmp, 16);

		return (char) code;
	}
}
