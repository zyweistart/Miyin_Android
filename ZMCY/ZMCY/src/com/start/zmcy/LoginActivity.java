package com.start.zmcy;

import java.util.Map;
import java.util.Set;

import start.core.AppConstant;
import start.core.AppException;
import start.utils.MD5;
import start.utils.StringUtils;
import start.widget.CustomEditText;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.start.core.BaseActivity;
import com.start.core.Constant.Handler;
import com.start.core.Constant.ResultCode;
import com.start.service.SocialService;


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
		}else if(v.getId()==R.id.qq_login){
			SocialService.socialQQLogin(this);
		}else if(v.getId()==R.id.wx_login){
			SocialService.socialWexinLogin(this);
		}else if(v.getId()==R.id.txtRegister){
			Intent intent=new Intent(this,RegisterActivity.class);
			startActivityForResult(intent, 0);
		}else{
			super.onClick(v);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void onProcessMessage(Message msg) throws AppException {
		switch(msg.what){
		case ResultCode.USERNAMEORPASSWORDERROR:
			getAppContext().currentUser().clearCachePassword();
			et_login_password.setText(AppConstant.EMPTYSTR);
			getHandlerContext().makeTextShort(String.valueOf(msg.obj));
			break;
		case Handler.HANDLERTHIRDPARTYLANDINGQQ:
			//QQ登陆
		case Handler.HANDLERTHIRDPARTYLANDINGWX:
			//WX登陆
			Map<String,Object> info=(Map<String,Object>)msg.obj;
			StringBuilder sb = new StringBuilder();
			Set<String> keys = info.keySet();
			for (String key : keys) {
				sb.append(key + "="+ info.get(key).toString()+ "\r\n");
			}
			Log.d("TestData", sb.toString());
			getHandlerContext().makeTextLong(sb.toString());
			//TODO:模拟登陆注册 
			this.loginSuccess();
			break;
		default:
			super.onProcessMessage(msg);
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RegisterActivity.LOGINSUCCESS){
			this.loginSuccess();
		}
	}

	/**
	 * 登录
	 * @param account
	 * @param password MD5加密后的密码
	 * @param autoLogin  
	 */
	public void login(final String account,final String password,final Boolean autoLogin){
		getAppContext().currentUser().addCacheUser(account, password, autoLogin);
		this.loginSuccess();
	}
	
	/**
	 * 登陆成功
	 */
	public void loginSuccess(){
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
