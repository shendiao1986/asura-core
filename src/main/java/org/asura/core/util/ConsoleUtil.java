package org.asura.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleUtil {
	private static BufferedReader reader;

	static {
		InputStreamReader stdin = new InputStreamReader(System.in);
		reader = new BufferedReader(stdin);
	}

	public static String read() {
		System.out.print("请输入字符:");
		try {
			return reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}
}
