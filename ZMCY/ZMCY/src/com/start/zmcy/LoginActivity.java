package com.start.zmcy;

import start.core.AppConstant;
import start.core.AppException;
import start.utils.MD5;
import start.utils.StringUtils;
import start.widget.CustomEditText;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.start.core.BaseActivity;
import com.start.core.Constant.ResultCode;


public class LoginActivity extends BaseActivity{
	
	/**
	 * 提示信息
	 */
	public static final String BUNLE_MESSAGE="BUNLE_MESSAGE";
	/**
	 * 自动登录标记
	 */
	public static final String BUNLE_AUTOLOGINFLAG="BUNLE_AUTOLOGINFLAG";
	
	private CustomEditText et_login_account;
	private CustomEditText et_login_password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setMainHeadTitle(getString(R.string.login));
		et_login_account=(CustomEditText)findViewById(R.id.et_login_account);
		et_login_password=(CustomEditText)findViewById(R.id.et_login_password);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			
			if(bundle.getBoolean(BUNLE_AUTOLOGINFLAG, false)){
				String account=getAppContext().currentUser().getCacheAccount();
				if(StringUtils.isEmpty(account)){
					return;
				}
				et_login_account.setText(account);
				Boolean autoLogin=getAppContext().currentUser().getCacheAutoLogin();
				if(autoLogin){
					String password=getAppContext().currentUser().getCachePassword();
					if(StringUtils.isEmpty(password)){
						return;
					}
					et_login_password.setText(password);
					login(account, password, autoLogin);
				}
			}else{
				
				String message=bundle.getString(BUNLE_MESSAGE,AppConstant.EMPTYSTR);
				if(!StringUtils.isEmpty(message)){
					getHandlerContext().makeTextLong(message);
					return;
				}
				
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_login){
			String account=String.valueOf(et_login_account.getText());
			if(StringUtils.isEmpty(account)){
				getHandlerContext().makeTextLong(getString(R.string.accounthint));
				return;
			}
			String password=String.valueOf(et_login_password.getText());
			if(StringUtils.isEmpty(password)){
				getHandlerContext().makeTextLong(getString(R.string.passwordhint));
				return;
			}
			login(account,MD5.md5(password),true);
		}else if(v.getId()==R.id.txtRegister){
			Intent intent=new Intent(this,RegisterActivity.class);
			startActivity(intent);
		}else{
			super.onClick(v);
		}
	}
	
	
	@Override
	public void onProcessMessage(Message msg) throws AppException {
		switch(msg.what){
		case ResultCode.USERNAMEORPASSWORDERROR:
			getAppContext().currentUser().clearCachePassword();
			et_login_password.setText(AppConstant.EMPTYSTR);
			getHandlerContext().makeTextShort(String.valueOf(msg.obj));
			break;
		default:
			super.onProcessMessage(msg);
			break;
		}
	}
	
	/**
	 * 登录
	 * @param account
	 * @param password MD5加密后的密码
	 * @param autoLogin  
	 */
	public void login(final String account,final String password,final Boolean autoLogin){
		if(getInputMethodManager().isActive()){
			getInputMethodManager().hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		getAppContext().currentUser().setLogin(true);
		if(getAppContext().getCacheActivity().isGotoActivity()){
			getAppContext().getCacheActivity().startActivity(this);
			finish();
		}
	}
}
