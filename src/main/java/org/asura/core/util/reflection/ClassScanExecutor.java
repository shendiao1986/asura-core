package org.asura.core.util.reflection;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassScanExecutor {
	private String basePackage;
	private ClassMather mather;

	private Set<Class<?>> results;

	public ClassScanExecutor(String basePackage, Class<? extends Annotation>[] annotations, String[] classNames) {
		this.basePackage = basePackage;
		mather = new ClassMather(annotations, classNames);
	}

	public Set<Class<?>> execute() throws IOException, ClassNotFoundException {
		results = new HashSet<Class<?>>();
		String packageDir = basePackage.replace('.', '/');
		Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(packageDir);
		URL url;
		String protocol;
		while (urls.hasMoreElements()) {
			url = urls.nextElement();
			protocol = url.getProtocol();
			if ("file".equals(protocol)) {
				findAndAddClassesByFile(basePackage, new File(URLDecoder.decode(url.getFile(), "UTF-8")));
			} else if ("jar".equals(protocol)) {
				findAndAddClassesByJar(((JarURLConnection) url.openConnection()).getJarFile(), packageDir);
			}
		}
		return results;
	}

	private void findAndAddClassesByFile(String currentPackage, File currentFile) throws ClassNotFoundException {
		if (!currentFile.exists() || !currentFile.isDirectory()) {
			return;
		}
		File[] files = currentFile.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return (file.isDirectory()) || (file.getName().endsWith(".class"));
			}
		});
		for (File file : files) {
			if (file.isDirectory()) {
				findAndAddClassesByFile(currentPackage + "." + file.getName(), file);
			} else {
				String className = file.getName().substring(0, file.getName().length() - 6);
				Class<?> clazz = Thread.currentThread().getContextClassLoader()
						.loadClass(currentPackage + '.' + className);
				if (mather.isMath(clazz)) {
					results.add(clazz);
				}
			}
		}
	}

	private void findAndAddClassesByJar(JarFile jar, String currentPath) throws ClassNotFoundException {
		Enumeration<JarEntry> entries = jar.entries();
		JarEntry jarEntry;
		String jarName;
		while (entries.hasMoreElements()) {
			jarEntry = entries.nextElement();
			jarName = jarEntry.getName();
			if (jarName.charAt(0) == '/') {
				jarName = jarName.substring(1);
			}
			if (jarName.startsWith(currentPath)) {
				int idx = jarName.lastIndexOf('/');
				if (jarName.endsWith(".class") && !jarEntry.isDirectory()) {
					String className = jarName.substring(jarName.lastIndexOf('/') + 1, jarName.length() - 6);
					Class<?> clazz = Class.forName(jarName.substring(0, idx).replace('/', '.') + '.' + className);
					if (mather.isMath(clazz)) {
						results.add(clazz);
					}
				}
			}
		}

	}
}
