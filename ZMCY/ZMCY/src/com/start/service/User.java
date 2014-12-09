package com.start.service;

import java.util.HashMap;
import java.util.Map;

import start.core.AppConstant;
import start.core.AppContext;

import com.start.core.Constant.Preferences;
import com.start.zmcy.BaseContext;

public class User {

//	public static String ACCESSID = AppConstant.EMPTYSTR;
	public static String ACCESSKEY = AppConstant.EMPTYSTR;
//	public static final String USER_ACCESSID_LOCAL = AppContext.getInstance().isTestEnvironmental() ? 
//			"930bb8937c42109377e73bea7b92f88c":"3c075d12a568dbc5be68902f99c24393";
	public static final String USER_ACCESSKEY_LOCAL =AppContext.getInstance().isTestEnvironmental() ? 
			"930bb8937c42109377e73bea7b92f88c":"930bb8937c42109377e73bea7b92f88c";
	
	private Boolean login;
	private static User mUser;
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
		BaseContext.getSharedPreferences().putString(Preferences.SP_ACCOUNT_CONTENT_DATA, account);
		BaseContext.getSharedPreferences().putBoolean(Preferences.SP_AUTOLOGIN_CONTENT_DATA, autoLogin);
		if(autoLogin){
			BaseContext.getSharedPreferences().putString(Preferences.SP_PASSWORD_CONTENT_DATA, password);
		}
	}
	
	/**
	 * 修改存储的密码
	 */
	public void changeCacheUser(String password){
		if(BaseContext.getSharedPreferences().getBoolean(Preferences.SP_AUTOLOGIN_CONTENT_DATA, false)){
			BaseContext.getSharedPreferences().putString(Preferences.SP_PASSWORD_CONTENT_DATA, password);
		}
	}
	
	public void clearCacheUser(){
		setLogin(false);
		BaseContext.getSharedPreferences().putString(Preferences.SP_ACCOUNT_CONTENT_DATA, AppConstant.EMPTYSTR);
		BaseContext.getSharedPreferences().putString(Preferences.SP_PASSWORD_CONTENT_DATA, AppConstant.EMPTYSTR);
		BaseContext.getSharedPreferences().putBoolean(Preferences.SP_AUTOLOGIN_CONTENT_DATA, false);
		BaseContext.getSharedPreferences().putString(Preferences.SP_LOCK_KEY_DATA, AppConstant.EMPTYSTR);
	}
	
	public void clearCachePassword(){
		BaseContext.getSharedPreferences().putString(Preferences.SP_PASSWORD_CONTENT_DATA, AppConstant.EMPTYSTR);
	}
	
	public String getCacheAccount(){
		return BaseContext.getSharedPreferences().getString(Preferences.SP_ACCOUNT_CONTENT_DATA, AppConstant.EMPTYSTR);
	}
	
	public String getCachePassword(){
		return BaseContext.getSharedPreferences().getString(Preferences.SP_PASSWORD_CONTENT_DATA, AppConstant.EMPTYSTR);
	}
	
	public Boolean getCacheAutoLogin(){
		return BaseContext.getSharedPreferences().getBoolean(Preferences.SP_AUTOLOGIN_CONTENT_DATA, false);
	}
	
	public void resolve(Map<String,String> userinfo){
		setLogin(true);
		getInfo().putAll(userinfo);
	}
	
}