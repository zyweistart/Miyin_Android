package com.ancun.zgdxyzb;

import java.util.HashMap;
import java.util.Map;

import start.core.AppConstant;
import start.core.AppException;
import start.service.HttpRunnable;
import start.service.HttpServer;
import start.service.Response;
import start.utils.MD5;
import start.utils.StringUtils;
import start.widget.CustomEditText;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ancun.core.BaseActivity;
import com.ancun.core.Constant;
import com.ancun.core.Constant.ResultCode;
import com.ancun.service.User;

public class LoginActivity extends BaseActivity {
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
	private CheckBox cb_login_autologin;
	private TextView txtRegister,txtResetPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setMainHeadTitle(getString(R.string.login));
		et_login_account=(CustomEditText)findViewById(R.id.et_login_account);
		et_login_password=(CustomEditText)findViewById(R.id.et_login_password);
		cb_login_autologin=(CheckBox)findViewById(R.id.cb_login_autologin);
		txtRegister=(TextView)findViewById(R.id.txt_register);
		txtRegister.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		txtRegister.setOnClickListener(this);
		txtResetPassword=(TextView)findViewById(R.id.txt_reset_password);
		txtResetPassword.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		txtResetPassword.setOnClickListener(this);
		findViewById(R.id.btn_back).setVisibility(View.GONE);
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
				cb_login_autologin.setChecked(autoLogin);
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
				getHandlerContext().makeTextLong(getString(R.string.phoneemptytip));
				return;
			}
			String password=String.valueOf(et_login_password.getText());
			if(StringUtils.isEmpty(password)){
				getHandlerContext().makeTextLong(getString(R.string.pwdemptytip));
				return;
			}
			Boolean checked=cb_login_autologin.isChecked();
			login(account,MD5.md5(password),checked);
		}else if(v.getId()==R.id.txt_register){
			Intent intent=new Intent(this,Register1Activity.class);
			startActivity(intent);
		}else if(v.getId()==R.id.txt_reset_password){
			Intent intent=new Intent(this,ResetPassWordActivity.class);
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
		HttpServer hServer=new HttpServer(Constant.URL.ylcnuserpwdCheck, getHandlerContext());
		Map<String,String> headers=new HashMap<String,String>();
		headers.put("sign", User.USER_ACCESSKEY_LOCAL);
		hServer.setHeaders(headers);
		Map<String,String> params=new HashMap<String,String>();
		params.put("accessid", User.USER_ACCESSID_LOCAL);
		params.put("userTel", account);
		params.put("password", password);
		params.put("type", "1");
		hServer.setParams(params);
		hServer.get(new HttpRunnable() {
			
			@Override
			public void run(Response response) throws AppException {
				getAppContext().currentUser().addCacheUser(account, password, autoLogin);
				getAppContext().currentUser().resolve(response.getMapData("userinfo"));
				startActivity(new Intent(LoginActivity.this,MainActivity.class));
				finish();
			}
			
		});
	}

}