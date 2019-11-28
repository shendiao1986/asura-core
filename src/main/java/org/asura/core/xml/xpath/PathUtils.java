package org.asura.core.xml.xpath;

import java.util.HashSet;
import java.util.Set;

import org.asura.core.util.StringUtil;

public class PathUtils {
	private static Set<Character> validCharacters = new HashSet<>();

	static {
		for (char c = 'a'; c <= 'z'; c = (char) (c + '\1')) {
			validCharacters.add(Character.valueOf(c));
		}

		for (char c = 'A'; c <= 'Z'; c = (char) (c + '\1')) {
			validCharacters.add(Character.valueOf(c));
		}

		validCharacters.add(Character.valueOf('-'));
		validCharacters.add(Character.valueOf('_'));

		for (char c = '0'; c <= '9'; c = (char) (c + '\1'))
			validCharacters.add(Character.valueOf(c));
	}

	public static void checkPathName(String name) throws XmlPathException {
		if (!(StringUtil.isNullOrEmpty(name))) {
			char[] cs = name.toCharArray();
			for (char c : cs)
				if (!(validCharacters.contains(Character.valueOf(c))))
					throw new XmlPathException("path contains invalid character '" + c + "'");
		} else {
			throw new XmlPathException("path can not be empty.");
		}
	}
}
