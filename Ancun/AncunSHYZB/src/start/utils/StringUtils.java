package start.utils;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.text.TextUtils;
import start.core.AppConstant;
/**
 * 字符串处理类
 * @author Start
 */
public class StringUtils {
	
	 /**
	 * HmacSHA1签名
	 */
	public static String signatureHmacSHA1(String data,String key){
		try {
			return Base64.encode(HmacSHA1.signature(data, key,AppConstant.ENCODE));
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	 /**
     * DES加密
     */
    public static String doKeyEncrypt(String str,InputStream deskey){
    	try {
			return DES.encrypt(str, deskey);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
    
    /**
     * DES解密
     */
	public static String doKeyDecrypt(String str,InputStream deskey){
		try {
			return DES.decrypt(str, deskey);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
     * URL编码
     */
	public static String encode(String str) {
		return encode(str,AppConstant.ENCODE);
	}
	
	/**
     * URL编码
     */
	public static String encode(String str, String enc) {
		String strEncode = "";
		try {
			if (str != null){
				strEncode = URLEncoder.encode(str, enc);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return strEncode;
	}
	
	/**
	 * 字符串转整数
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		try{
			return Integer.parseInt(str);
		}catch(Exception e){}
		return defValue;
	}
	
	/**
	 * 对象转整数
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static int toInt(Object obj) {
		if(obj==null) return 0;
		return toInt(obj.toString(),0);
	}
	
	public static boolean isEmpty(String str){
		return str==null||"".equals(str);
	}
	
	/**
	 * 获取当前方法执行的信息，用于异常时保存信息
	 */
	public static String getTraceInfo() {
		StringBuffer sb = new StringBuffer();
		StackTraceElement stackTrace = new Throwable().getStackTrace()[1];
		sb.append("{");
		sb.append("ClassName:").append(stackTrace.getClassName());
		sb.append(";FileName:").append(stackTrace.getFileName());
		sb.append(";MethodName:").append(stackTrace.getMethodName());
		sb.append(";LineNumber:").append(stackTrace.getLineNumber());
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * 号码转换，例：1 37-3887-3386转换成13738873386把有格式的转成标准号码格式
	 * @param phone
	 * @return
	 */
	public static String phoneFormat(String phone){
		if(TextUtils.isEmpty(phone)){
			return AppConstant.EMPTYSTR;
		}
		//号码转换
		StringBuilder sbphone=new StringBuilder();
		for(int i=0;i<phone.length();i++){
			char c=phone.charAt(i);
			int asnum=(int)c;
			if(asnum>=48&&asnum<=57){
				sbphone.append(c);
			}
		}
		return sbphone.toString();
	}
	
	public static String convertString(Object o){
		if(o==null){
			return AppConstant.EMPTYSTR;
		}
		return String.valueOf(o);
	}
	
	/**
	 * 半角转换为全角
	 * 有效解决TextView自动换行的问题
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}
	
}