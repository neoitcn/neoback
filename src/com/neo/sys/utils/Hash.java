package com.neo.sys.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * 自定义的生成MD5/SHA1基本工具类
 *
 */
public class Hash {

	/**
	 * @param args
	 */
	private static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String getHash(File file, String hashType) throws Exception {
		InputStream fis;
		fis = new FileInputStream(file);
		byte[] buffer = new byte[1024];
		MessageDigest md5 = MessageDigest.getInstance(hashType);
		int numRead = 0;
		while ((numRead = fis.read(buffer)) > 0) {
			md5.update(buffer, 0, numRead);
		}
		fis.close();
		return toHexString(md5.digest());
	}

	public static String getHash(String str, String hashType) throws Exception {
		if (str == null || str.trim().length() == 0) {
			return null;
		}
		MessageDigest md5 = MessageDigest.getInstance(hashType);
		md5.update(str.getBytes());
		return toHexString(md5.digest());
	}

	private static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
			sb.append(hexChar[b[i] & 0x0f]);
		}
		return sb.toString();
	}

}
