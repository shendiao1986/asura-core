package org.asura.core.util.reflection;

import java.lang.annotation.Annotation;
import java.util.regex.Pattern;

public class ClassMather {
	private Class<? extends Annotation>[] annotations;
	private String[] classNames;

	public ClassMather(Class<? extends Annotation>[] annotations, String[] classNames) {
		this.annotations = annotations;
		this.classNames = classNames;
	}
	
	public boolean isMath(Class<?> clazz) {
        boolean ret;
        ret = matchAnnotation(clazz);
        if (!ret) {
            return false;
        }
        return matchClassName(clazz);
    }

    private boolean matchAnnotation(Class<?> clazz) {
        if (annotations == null || annotations.length == 0) {
            return true;
        }
        for (int i = 0; i < annotations.length; i++) {
            if (clazz.isAnnotationPresent(annotations[i])) {
                return true;
            }
        }
        return false;
    }

    private boolean matchClassName(Class<?> clazz) {
        if (classNames == null || classNames.length == 0) {
            return true;
        }
        for (int i = 0; i < classNames.length; i++) {
            if (Pattern.compile(classNames[i])
                    .matcher(clazz.getSimpleName()).find()) {
                return true;
            }
        }
        return false;
    }
}
