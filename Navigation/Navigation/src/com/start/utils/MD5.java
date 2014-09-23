package com.start.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	
	public static final String ALGORITHM = "MD5";

	public static String byte2hex(byte[] b) {
		String str = "";
		String stmp = "";
		int length = b.length;
		for (int n = 0; n < length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				str = str + "0" + stmp;
			} else {
				str = str + stmp;
			}
			if (n < length - 1) {
				str = str + "";
			}
		}
		return str.toLowerCase();
	}
	/**
	 * 对字符进行MD5计算
	 */
	public static String md5(String input) {
		try {
			MessageDigest md5 = MessageDigest.getInstance(ALGORITHM);
			byte[] md5Bytes = md5.digest(input.getBytes());
			return byte2hex(md5Bytes).toLowerCase();
		} catch (NoSuchAlgorithmException e) {
			return input;
		}
	}
	/**
	 * 对字符进行MD5计算
	 * @param charsetName
	 * 编码名称
	 */
	public static String md5(String input, String charsetName) {
		try {
			MessageDigest md5 = MessageDigest.getInstance(ALGORITHM);
			byte[] md5Bytes = md5.digest(input.getBytes(charsetName));
			return byte2hex(md5Bytes).toLowerCase();
		} catch (Exception e) {
			return input;
		}
	}
	/**
	 * 对文件进行MD5计算
	 */
	public static String md5file(String filename) {
		BufferedInputStream bufferedInputStream = null;
		try {
			int len = -1;
			byte[] buffer = new byte[8096];
			bufferedInputStream = new BufferedInputStream(new FileInputStream(filename), 8096);
			MessageDigest md = MessageDigest.getInstance(ALGORITHM);
			while ((len = bufferedInputStream.read(buffer)) != -1) {
				md.update(buffer, 0, len);
			}
			buffer = null;
			return byte2hex(md.digest()).toLowerCase();
		} catch (Exception e) {
			return "";
		} finally {
			if(bufferedInputStream!=null){
				try {
					bufferedInputStream.close();
				} catch (IOException e) {
					return "";
				}finally{
					bufferedInputStream=null;
				}
			}
		}
	}
}
