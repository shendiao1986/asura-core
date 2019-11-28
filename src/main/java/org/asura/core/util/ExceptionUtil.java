package org.asura.core.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionUtil {
	private static final Logger logger = LoggerFactory.getLogger(ExceptionUtil.class);

	public static String getExceptionContent(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String content = sw.toString();
		try {
			sw.close();
		} catch (IOException e1) {
			logger.error("exception occurred", e1);
		}
		return content;
	}
}
