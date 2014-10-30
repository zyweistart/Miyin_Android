package com.ancun.service;

import java.util.HashMap;
import java.util.Map;

import start.core.AppConstant;
import start.core.AppContext;
import android.text.TextUtils;

import com.ancun.core.Constant.Preferences;

public class User {

	public static String ACCESSID = AppConstant.EMPTYSTR;
	public static String ACCESSKEY = AppConstant.EMPTYSTR;
	public static final String USER_ACCESSID_LOCAL = AppContext.getInstance().isTestEnvironmental() ? 
			"8e39719bf7f47f0332d2f3a4c7093229":"4a069ace58ba0918a3aa11b62b472b9e";
	public static final String USER_ACCESSKEY_LOCAL = AppContext.getInstance().isTestEnvironmental() ? 
			"YzQ2YzdkZjgwNDA0ZWJmNDU4YTkxYzQ5ZTU3ZDRhZTA=":"MThjMDE3ZjAwMTE3NTIzNmE4OTM2ZjQ5N2M2Y2JhNDc=";
	public static final String COMMON_ACCESSID_LOCAL = AppContext.getInstance().isTestEnvironmental() ? 
			"1cbaaa4c6e667f169e2630f871cb57cd":"3c075d12a568dbc5be68902f99c24393";
	public static final String COMMON_ACCESSKEY_LOCAL =AppContext.getInstance().isTestEnvironmental() ? 
			"MjAwOTAwMWFlYjUyNmU1MDY1NDNlMzU3MGM1NmFjOTk=":"MDY4YmVmMDM2NGM0ODliMGVhYmQ1MDI1NzE5YzQwOWI=";
	
	/**
	 * 用户登录标记
	 */
	private Boolean login;
	private static User mUser;
	/**
	 * 用户信息
	 */
	private Map<String,String> info;
	
	private User(){}
	
	public static User getInstance(){
		if(mUser==null){
			mUser=new User();
			mUser.setLogin(false);
		}
		return mUser;
	}
	
	/**
	 * 判断是否已经绑定就诊信息
	 */
	public Boolean isBind(){
		return !TextUtils.isEmpty(getInfo().get("name"));
	}
	
	/**
	 * 判断是否有用户登录
	 */
	public Boolean isLogin() {
		return login;
	}

	public void setLogin(Boolean login) {
		this.login = login;
	}

	public Map<String, String> getInfo() {
		if(info==null){
			info=new HashMap<String,String>();
		}
		return info;
	}
	
	/**
	 * 用户信息加入缓存
	 */
	public void addCacheUser(String account,String password,Boolean autoLogin){
		AppContext.getSharedPreferences().putString(Preferences.SP_ACCOUNT_CONTENT_DATA, account);
		AppContext.getSharedPreferences().putBoolean(Preferences.SP_AUTOLOGIN_CONTENT_DATA, autoLogin);
		if(autoLogin){
			AppContext.getSharedPreferences().putString(Preferences.SP_PASSWORD_CONTENT_DATA, password);
		}
	}
	
	/**
	 * 修改存储的密码
	 */
	public void changeCacheUser(String password){
		if(AppContext.getSharedPreferences().getBoolean(Preferences.SP_AUTOLOGIN_CONTENT_DATA, false)){
			AppContext.getSharedPreferences().putString(Preferences.SP_PASSWORD_CONTENT_DATA, password);
		}
	}
	
	public void clearCacheUser(){
		setLogin(false);
		AppContext.getSharedPreferences().putString(Preferences.SP_ACCOUNT_CONTENT_DATA, AppConstant.EMPTYSTR);
		AppContext.getSharedPreferences().putString(Preferences.SP_PASSWORD_CONTENT_DATA, AppConstant.EMPTYSTR);
		AppContext.getSharedPreferences().putBoolean(Preferences.SP_AUTOLOGIN_CONTENT_DATA, false);
	}
	
	public void clearCachePassword(){
		AppContext.getSharedPreferences().putString(Preferences.SP_PASSWORD_CONTENT_DATA, AppConstant.EMPTYSTR);
	}
	
	public String getCacheAccount(){
		return AppContext.getSharedPreferences().getString(Preferences.SP_ACCOUNT_CONTENT_DATA, AppConstant.EMPTYSTR);
	}
	
	public String getCachePassword(){
		return AppContext.getSharedPreferences().getString(Preferences.SP_PASSWORD_CONTENT_DATA, AppConstant.EMPTYSTR);
	}
	
	public Boolean getCacheAutoLogin(){
		return AppContext.getSharedPreferences().getBoolean(Preferences.SP_AUTOLOGIN_CONTENT_DATA, false);
	}
	
	public void resolve(Map<String,String> userinfo){
		setLogin(true);
		getInfo().putAll(userinfo);
		User.ACCESSID=getInfo().get("accessid");
		User.ACCESSKEY=getInfo().get("accesskey");
	}
	
}