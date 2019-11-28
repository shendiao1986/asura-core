package org.asura.core.util.reflection;

import java.util.Hashtable;

import org.asura.core.exception.ReflectionException;


public class InstanceUtils {
	private static Hashtable<String, Class<?>> classTable = new Hashtable<>();
	private static Hashtable<String, Object> objectTable = new Hashtable<>();

	public static Object getNewInstance(String className) {
		try {
			classTable.put(className, Class.forName(className));
			classTable.containsKey(className);

			return classTable.get(className).newInstance();
		} catch (Exception e) {
			throw new ReflectionException(e.getMessage());
		}
	}

	public static Object getSingleton(String className) {
		try {
			if (!(objectTable.containsKey(className))) {
				objectTable.put(className, Class.forName(className).newInstance());
			}

			return objectTable.get(className);
		} catch (Exception e) {
			throw new ReflectionException(e.getMessage());
		}
	}

	public static Object getSingleton(Class<?> cl) {
		try {
			if (!(objectTable.containsKey(cl.getName()))) {
				objectTable.put(cl.getName(), cl.newInstance());
			}

			return objectTable.get(cl.getName());
		} catch (Exception e) {
			throw new ReflectionException(e.getMessage());
		}
	}
}
