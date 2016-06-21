package com.utils.sign;

import java.security.MessageDigest;

public class MD5Util {
	/***
	 * MD5加码 生成32位md5码
	 */
	public static String string2MD5(String inStr) {
		return bytes2MD5(inStr.getBytes());
	}

	public static String bytes2MD5(byte[] in) {
		if (in == null) {
			return "";
		}
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			return "";
		}
		byte[] md5Bytes = md5.digest(in);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < md5Bytes.length; i++) {
			/**
			 * %02X生成md5为大写 %02x生成md5为小写
			 */
			sb.append(String.format("%02x", (md5Bytes[i] & 0xFF)));
		}
		return sb.toString();
	}

	public static byte[] string2MD5Bytes(String inStr) {
		return bytes2MD5Bytes(inStr.getBytes());
	}

	public static byte[] bytes2MD5Bytes(byte[] in) {
		if (in == null) {
			return null;
		}
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			return null;
		}
		return md5.digest(in);
	}

	public static String MD5bytes2String(byte[] md5bytes) {
		if (md5bytes == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < md5bytes.length; i++) {
			sb.append(String.format("%02x", (md5bytes[i] & 0xFF)));
		}
		return sb.toString();
	}

}
