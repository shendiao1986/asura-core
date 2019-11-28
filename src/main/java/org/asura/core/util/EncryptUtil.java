package org.asura.core.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;

public class EncryptUtil {

	public static void generateKeyFile(String filePath) {
		try {
			FileOutputStream fos = new FileOutputStream(filePath);
			KeyGenerator keyGen = KeyGenerator.getInstance("DESede");
			keyGen.init(new SecureRandom());
			Key key = keyGen.generateKey();
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(key);
			oos.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public static Key getKey(String filePath) throws Exception {
		FileInputStream fis = new FileInputStream(filePath);
		ObjectInputStream ois = new ObjectInputStream(fis);
		Key key = (Key) ois.readObject();
		ois.close();

		return key;
	}

	public static byte[] encrypt(byte[] bytes, String keyFile) throws Exception {
		Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(new byte[8]);
		cipher.init(1, getKey(keyFile), ips);

		return cipher.doFinal(bytes);
	}

	public static byte[] decrypt(byte[] bytes, String keyFile) throws Exception {
		Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(new byte[8]);
		cipher.init(2, getKey(keyFile), ips);

		return cipher.doFinal(bytes);
	}

	public static String encrypt(String content, Key key) throws Exception {
		byte[] bs = content.getBytes("UTF8");
		Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(new byte[8]);
		cipher.init(1, key, ips);

		bs = cipher.doFinal(bs);

		return new String(Base64.encodeBase64(bs));
	}

	public static String encrypt(String content, String keyString, String iv) {
		IvParameterSpec ips = new IvParameterSpec(new byte[8]);
		try {
			byte[] bs = content.getBytes("UTF8");
			Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");

			Key key = getKeyFromString(keyString);

			cipher.init(1, key, ips);
			bs = cipher.doFinal(bs);

			return new String(Base64.encodeBase64(bs));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return content;
	}

	private static Key getKeyFromString(String key) throws Exception {
		DESedeKeySpec p8ksp = new DESedeKeySpec(key.getBytes("utf8"));
		return SecretKeyFactory.getInstance("DESede").generateSecret(p8ksp);
	}

	public static String decrypt(String content, String key, String iv) {
		IvParameterSpec ips = new IvParameterSpec(new byte[8]);
		try {
			byte[] bs = Base64.decodeBase64(content);
			Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");

			Key pkey = getKeyFromString(key);

			cipher.init(2, pkey, ips);
			bs = cipher.doFinal(bs);

			return new String(bs, "utf8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return content;
	}

	public static String encrypt(String content, String keyFile) throws Exception {
		return encrypt(content, getKey(keyFile));
	}

	public static String decrypt(String content, Key key) throws Exception {
		byte[] bs = Base64.decodeBase64(content);
		Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(new byte[8]);
		cipher.init(2, key, ips);

		bs = cipher.doFinal(bs);

		return new String(bs, "utf8");
	}

	public static String decrypt(String content, String keyFile) throws Exception {
		return decrypt(content, getKey(keyFile));
	}
}
