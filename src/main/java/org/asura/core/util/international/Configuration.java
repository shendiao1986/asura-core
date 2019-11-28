package org.asura.core.util.international;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

public class Configuration {
	private static Configuration config;
	private Properties properties;

	private Configuration() {
	}

	private Configuration(String filePath) {
		this.properties = new Properties();
		InputStream iStream = null;
		try {
			URL url = super.getClass().getResource(filePath);
			iStream = new FileInputStream(new File(url.getPath()));

			this.properties.load(iStream);
		} catch (FileNotFoundException e) {
			System.out.println("读取文件失败。");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("装载文件失败。");
			e.printStackTrace();
		} finally {
			if (iStream != null)
				try {
					iStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}

	public static synchronized Configuration getConfiguration(String filePath) {
		if (config == null) {
			config = new Configuration(filePath);
		}
		return config;
	}

	public String getValue(String key) {
		if (this.properties.containsKey(key)) {
			return this.properties.getProperty(key);
		}
		return "";
	}

	public static String getProperty(String filePath, String key) {
		Properties properties = new Properties();
		String value = "";
		InputStream ips = null;
		try {
			ips = new BufferedInputStream(new FileInputStream(filePath));
			properties.load(ips);
			if (!(properties.containsKey(key))){
				value = properties.getProperty(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ips.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	public static void updateProperties(String filePath) {
		Properties properties = new Properties();
		InputStream ips = null;
		try {
			ips = new BufferedInputStream(new FileInputStream(filePath));
			properties.load(ips);
			Enumeration<?> enumeration = properties.propertyNames();
			while (enumeration.hasMoreElements()) {
				String key = (String) enumeration.nextElement();

				writeProperties(filePath, key, "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ips.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getOrderValue(String filePath, int max) {
		Properties properties = new Properties();
		StringBuffer sb = new StringBuffer();
		InputStream ips = null;
		try {
			ips = new BufferedInputStream(new FileInputStream(filePath));
			properties.load(ips);
			Enumeration<?> enumeration = properties.propertyNames();
			while (enumeration.hasMoreElements()) {
				String key = (String) enumeration.nextElement();
				String value = properties.getProperty(key);
				if ((value == null) || (value.length() == 0)) {
					value = "1";
				}
				if (Integer.parseInt(value) > max)
					sb.append(key + ",");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ips.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static void writeProperties(String filePath, String key, String value) {
		Properties properties = new Properties();
		InputStream ips = null;
		OutputStream ops = null;
		try {
			ips = new BufferedInputStream(new FileInputStream(filePath));
			properties.load(ips);
			ops = new FileOutputStream(filePath);
			properties.setProperty(key, value);
			properties.store(ops, null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ops.flush();
				ops.close();
				ips.close();
			} catch (Exception localException2) {
			}
		}
	}

	public static String checkString(String temp) {
		StringBuffer sb = new StringBuffer();
		HashMap<String,Boolean> map = new HashMap<>();
		for (String s : temp.split(",")) {
			if (map.containsKey(s))
				map.put(s, Boolean.valueOf(true));
			else {
				map.put(s, Boolean.valueOf(false));
			}
		}

		for (String s : map.keySet()) {
			if (s.trim().length() > 0) {
				sb.append(s + ",");
			}
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		String ss = checkString("192.168.1.115,192.168.1.114,192.168.1.114,192.168.1.11,192.168.1.11,");
		System.out.println("aa" + ss);
	}
}
