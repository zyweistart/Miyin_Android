package com.start.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmacSHA1 {

	private static final String ALGORITHM = "HmacSHA1";

	public static byte[] signature(String data, String key)
			throws NoSuchAlgorithmException, InvalidKeyException {
		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), ALGORITHM);   
        Mac mac = Mac.getInstance(ALGORITHM);
        mac.init(signingKey);
		return mac.doFinal(data.getBytes());
	}

	public static byte[] signature(String data, String key, String charsetName) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, IllegalStateException {
		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(charsetName), ALGORITHM);   
        Mac mac = Mac.getInstance(ALGORITHM);
        mac.init(signingKey);
		return mac.doFinal(data.getBytes(charsetName));
	}

}