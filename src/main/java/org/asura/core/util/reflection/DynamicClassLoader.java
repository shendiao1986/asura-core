package org.asura.core.util.reflection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DynamicClassLoader extends ClassLoader {
	private String classPath;

	public DynamicClassLoader(String classPath) {
		this.classPath = classPath;
	}

	protected Class<?> findClass(String name) throws ClassNotFoundException {
		byte[] classData = null;
		try {
			classData = loadClassData(name);
		} catch (IOException e) {
			return null;
		}

		return super.defineClass(name, classData, 0, classData.length);
	}

	private byte[] loadClassData(String name) throws IOException {
		File file = getFile(name);
		FileInputStream fis = new FileInputStream(file);
		byte[] arrData = new byte[(int) file.length()];
		fis.read(arrData);
		fis.close();
		return arrData;
	}

	private File getFile(String name) throws FileNotFoundException {
		File dir = new File(this.classPath);
		if (!(dir.exists()))
			throw new FileNotFoundException(this.classPath + " 目录不存在！");
		String _classPath = this.classPath.replaceAll("[\\\\]", "/");
		int offset = _classPath.lastIndexOf("/");
		name = name.replaceAll("[.]", "/");
		if ((offset != -1) && (offset < _classPath.length() - 1)) {
			_classPath = _classPath + "/";
		}
		_classPath = _classPath + name + ".class";
		dir = new File(_classPath);
		if (!(dir.exists()))
			throw new FileNotFoundException(dir + " 不存在！");
		return dir;
	}

	public String getClassPath() {
		return this.classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}
}
