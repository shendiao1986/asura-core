package org.asura.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SerializeUtil {

	private static final Logger logger = LoggerFactory.getLogger(SerializeUtil.class);

	public static Object byteToObj(byte[] bytes) {
		Object obj = null;
		try (ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
				ObjectInputStream oi = new ObjectInputStream(bi)) {
			obj = oi.readObject();
		} catch (Exception e) {
			logger.error("translate exception:byte2Obj", e);
		}
		return obj;
	}

	public static byte[] objToByte(Object obj) {
		byte[] bytes = null;
		try (ByteArrayOutputStream bo = new ByteArrayOutputStream();
				ObjectOutputStream oo = new ObjectOutputStream(bo)) {
			oo.writeObject(obj);
			bytes = bo.toByteArray();
		} catch (Exception e) {
			logger.error("translate exception:obj2Byte", e);
		}
		return bytes;
	}

	public static String objToByteString(Object obj) {
		byte[] bytes = objToByte(obj);
		StringBuffer sb = new StringBuffer();
		for (byte bt : bytes) {
			sb.append(bt + " ");
		}
		return sb.toString().trim();
	}

	public static Object byteStringToObj(String byteString) {
		String[] ss = byteString.trim().split(" ");
		byte[] bytes = new byte[ss.length];
		for (int i = 0; i < ss.length; ++i) {
			bytes[i] = Byte.valueOf(ss[i]).byteValue();
		}
		return byteToObj(bytes);
	}

	public static void writeObjToFile(Object obj, String fileName) {
		File f = new File(fileName);
		if (f.exists()) {
			f.delete();
		}
		try (FileOutputStream os = new FileOutputStream(f); ObjectOutputStream oos = new ObjectOutputStream(os)) {
			oos.writeObject(obj);
		} catch (Exception e) {
			logger.error("write obj to file exception", e);
		}
	}

	public static Object readObj(String fileName) {
		try (InputStream is = new FileInputStream(new File(fileName));
				ObjectInputStream ois = new ObjectInputStream(is)) {
			return ois.readObject();
		} catch (Exception e) {
			logger.error("read obj exception", e);
		}
		return null;
	}

	public static Object readObj(InputStream is) {
		try (ObjectInputStream ois = new ObjectInputStream(is)) {
			return ois.readObject();
		} catch (Exception e) {
			logger.error("read obj exception", e);
		}
		return null;
	}

}
