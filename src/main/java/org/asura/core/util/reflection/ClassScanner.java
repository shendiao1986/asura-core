package org.asura.core.util.reflection;

import java.lang.annotation.Annotation;
import java.util.Set;

public class ClassScanner {
	public static Set<Class<?>> scan(String basePackage) throws Exception {
		return scan(basePackage, null, null);
	}

	@SafeVarargs
	public static Set<Class<?>> scan(String basePackage, Class<? extends Annotation>... annotations) throws Exception {
		return scan(basePackage, annotations, null);
	}

	public static Set<Class<?>> scan(String basePackage, String... className) throws Exception {
		return scan(basePackage, null, className);
	}

	public static Set<Class<?>> scan(String basePackage, Class<? extends Annotation>[] annotations, String[] classNames)
			throws Exception {
		return new ClassScanExecutor(basePackage, annotations, classNames).execute();
	}
}
