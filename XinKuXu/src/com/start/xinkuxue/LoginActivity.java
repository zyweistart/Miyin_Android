package com.start.xinkuxue;

import android.os.Bundle;

import com.start.core.BaseActivity;


public class LoginActivity extends BaseActivity{
	
	public static final int RESULT_LOGIN_SUCCESS=222;
	
	public static final int QQ_LOGIN=1;
	public static final int WX_LOGIN=2;
	
	/**
	 * 提示信息
	 */
	public static final String BUNLE_MESSAGE="BUNLE_MESSAGE";
	/**
	 * 自动登录标记
	 */
	public static final String BUNLE_AUTOLOGINFLAG="BUNLE_AUTOLOGINFLAG";
	
//	private CustomEditText et_login_account;
//	private CustomEditText et_login_password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}
	
}
