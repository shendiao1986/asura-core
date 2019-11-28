package org.asura.core.util.reflection;

import org.apache.log4j.Logger;
import org.asura.core.util.ExceptionUtil;

public class ClassLoaderUtil {
	private static final Logger logger = Logger.getLogger(ClassLoaderUtil.class);

	public static Object load(String path, String className) {
		DynamicClassLoader fileClsLoader = new DynamicClassLoader(path);
		try {
			Class<?> cls = fileClsLoader.loadClass(className);

			return cls.newInstance();
		} catch (Exception e) {
			logger.error("load failed at " + path + " - " + className + "\n" + ExceptionUtil.getExceptionContent(e));
		}
		return null;
	}

	public static Class<?> loadClass(String path, String className) {
		DynamicClassLoader fileClsLoader = new DynamicClassLoader(path);
		try {
			Class<?> cls = fileClsLoader.loadClass(className);
			return cls;
		} catch (Exception e) {
			logger.error("load failed at " + path + " - " + className + "\n" + ExceptionUtil.getExceptionContent(e));
		}
		return null;
	}
}
