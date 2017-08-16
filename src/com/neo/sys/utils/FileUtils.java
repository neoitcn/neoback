package com.neo.sys.utils;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class FileUtils {

//	public String getHash(String text, String hashType) throws Exception {
//		if (text == null) {
//			throw new Exception("text is null");
//		}
//		MessageDigest md5 = MessageDigest.getInstance(hashType);
//		md5.update(text.getBytes());
//		return toHexString(md5.digest());
//	}

//	private String toHexString(byte[] b) {
//		StringBuilder sb = new StringBuilder(b.length * 2);
//		for (int i = 0; i < b.length; i++) {
//			sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
//			sb.append(hexChar[b[i] & 0x0f]);
//		}
//		return sb.toString();
//	}

	/**
	 * 转换文件大小格式
	 * @param b
	 * @return
	 */
	public static String formatB(long b) {

		if (b >= 1073741824L) { // GB等级

			return formatDoubleNum(b / 1024 / 1024 / 1024D) + "GB";

		} else if (b >= 1048576) { // MB级别

			return formatDoubleNum(b / 1024 / 1024D) + "MB";

		} else if (b >= 1024) { // KB级别

			return formatDoubleNum(b / 1024D) + "KB";

		} else {

			return (int) b + "Byte";

		}

	}

	/**
	 * 保留两位小数 向下取
	 * @param num
	 * @return
	 */
	public static String formatDoubleNum(double num) {

		DecimalFormat df = (DecimalFormat)NumberFormat.getInstance();
		df.setGroupingUsed(false);
		df.setMaximumFractionDigits(2);
		df.setRoundingMode(RoundingMode.HALF_UP);
		return df.format(num);

	}

	/**
	 * 获取文件名称，去掉扩展名
	 * @param name
	 * @return
	 */
	public static String getFileLastName(String name) {
		if (name.contains(".")) {
			return name.substring(name.lastIndexOf(".") + 1, name.length());
		}
		return null;
	}
	
	/**
	 * 获取文件扩展名
	 * @param name
	 * @return
	 */
	public static String getFileFirstName(String name) {
		if (name.contains(".")) {
			return name.substring(0,name.lastIndexOf("."));
		}
		return null;
	}
	
	/**
	 * 文件copy
	 * @param is
	 * @param os
	 * @throws Exception
	 */
	public static void copyFile(InputStream is,OutputStream os) throws Exception{
		int read=0;
		byte b[] = new byte[1024];
		while((read=is.read(b))>0){
			os.write(b,0,read);
		}
		os.flush();
		os.close();
		is.close();
	}
	
	public static byte[] readFileToByteArray(File file) throws Exception{
		return org.apache.commons.io.FileUtils.readFileToByteArray(file);
	}
	
}
