package start.utils;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.security.Key;

import javax.crypto.Cipher;

import start.core.AppConstant;

public class DES {
	
	public final static String ALGORITHM="DES";
	
	/**
	 * 获取文件密钥
	 */
	public static Key getKey(InputStream is) throws Exception {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(is);
			return (Key) ois.readObject();
		} finally {
			if(ois != null) {
				try {
					ois.close();
				}finally{
					ois = null;
				}
			}
			if(is != null) {
				try {
					is.close();
				}finally{
					is = null;
				}
			}
		}
	}
	
	/**
	 * DES加密
	 */
	public static String encrypt(String str,InputStream deskey) throws Exception{
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, getKey(deskey));
		byte[] buffer = cipher.doFinal(str.getBytes(AppConstant.ENCODE));
		return new String(Base64.encode(buffer));
	}
	
	/**
	 * DES解密
	 */
	public static String decrypt(String str,InputStream deskey) throws Exception{
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, getKey(deskey));
		byte[] buffer = cipher.doFinal(Base64.decode(str));
		return new String(buffer,AppConstant.ENCODE).trim();
	}

}