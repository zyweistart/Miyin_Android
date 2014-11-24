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
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.start.core.BaseActivity;
import com.start.core.Constant.Handler;
import com.start.core.Constant.ResultCode;
import com.start.service.SocialService;


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
			this.registerLogin(QQ_LOGIN,(Map<String,Object>)msg.obj);
		case Handler.HANDLERTHIRDPARTYLANDINGWX:
			//WX登陆
			this.registerLogin(WX_LOGIN,(Map<String,Object>)msg.obj);
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
			Bundle bundle=data.getExtras();
			String type=bundle.getString(RegisterActivity.REGISTERTYPE);
			if(RegisterActivity.REGISTERTYPE_ACCOUNT.equals(type)){
				String account=bundle.getString(RegisterActivity.STR_ACCOUNT);
				String password=bundle.getString(RegisterActivity.STR_PASSWORD);
				login(account, password, true);
			}else if(RegisterActivity.REGISTERTYPE_QQ.equals(type)){
				this.registerLogin(QQ_LOGIN,bundle);
			}else if(RegisterActivity.REGISTERTYPE_WX.equals(type)){
				this.registerLogin(WX_LOGIN,bundle);
			}
		}
	}

	/**
	 * 登录
	 * @param account
	 * @param password MD5加密后的密码
	 * @param autoLogin  
	 */
	private void login(final String account,final String password,final Boolean autoLogin){
		this.loginSuccess(account,password,autoLogin);
	}
	
	/**
	 * 注册登录
	 * @param loginType登录类型1:QQ 2:WX
	 */
	private void registerLogin(int loginType,Map<String,Object> info){
		Bundle bundle=new Bundle();
		Set<String> keys = info.keySet();
		for (String key : keys) {
			bundle.putString(key, String.valueOf(info.get(key)));
		}
		registerLogin(loginType, bundle);
	}
	
	/**
	 * 注册登录
	 * @param loginType登录类型1:QQ 2:WX
	 */
	private void registerLogin(int loginType,Bundle data){
		this.loginSuccess("","",true);
	}
	
	/**
	 * 登陆成功
	 */
	private void loginSuccess(String account,String password,Boolean autoLogin){
		getAppContext().currentUser().addCacheUser(account, password, autoLogin);
		if(getInputMethodManager().isActive()){
			getInputMethodManager().hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		getAppContext().currentUser().setLogin(true);
		if(getAppContext().getCacheActivity().isGotoActivity()){
			getAppContext().getCacheActivity().startActivity(this);
		}else{
			setResult(RESULT_LOGIN_SUCCESS);
		}
		finish();
	}
	
}
