package org.asura.core.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CommandUtil {
	
	public static String execute(String cmd) {
		try {
			Process ps = Runtime.getRuntime().exec(cmd);

			BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
			String result = sb.toString();

			return result;
		} catch (Exception e) {
			return ExceptionUtil.getExceptionContent(e);
		}
	}
}
