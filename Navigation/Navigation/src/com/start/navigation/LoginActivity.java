package com.start.navigation;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.start.core.Constant;
import com.start.core.CoreActivity;
import com.start.model.UIRunnable;
import com.start.utils.MD5;

/**
 * 登录
 * @author start
 *
 */
public class LoginActivity extends CoreActivity implements  OnClickListener {

	private EditText etName;
	private EditText etPassword;
	private CheckBox ckAutoLogin;
	private Button btnLogin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setCurrentActivityTitle(R.string.login);
		
		etName=(EditText)findViewById(R.id.login_account_name);
		etPassword=(EditText)findViewById(R.id.login_account_password);
		ckAutoLogin=(CheckBox)findViewById(R.id.login_ck_autologin);
		btnLogin=(Button)findViewById(R.id.login_btn_account);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			makeTextLong(bundle.getString(Constant.Bundle.RELOGINMESSAGE));
		}
		Boolean isChecked=getAppContext().getSharedPreferencesUtils().getBoolean(Constant.SharedPreferences.LOGIN_AUTOLOGIN, true);
		ckAutoLogin.setChecked(isChecked);
		if(isChecked){
			String account=getAppContext().getSharedPreferencesUtils().getString(Constant.SharedPreferences.LOGIN_ACCOUNT, Constant.EMPTYSTR);
			if(!TextUtils.isEmpty(account)){
				etName.setText(account);
				String password=getAppContext().getSharedPreferencesUtils().getString(Constant.SharedPreferences.LOGIN_PASSWORD, Constant.EMPTYSTR);
				if(!TextUtils.isEmpty(password)){
					etPassword.setText(password);
					btnLogin.performClick();
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.login_btn_account){
			final String username=String.valueOf(etName.getText());
			final String password=String.valueOf(etPassword.getText());
			if(TextUtils.isEmpty(username)){
				makeTextShort(R.string.msg_account_not_empty);
			}else if(TextUtils.isEmpty(password)){
				makeTextShort(R.string.msg_password_not_empty);
			}else{
				Map<String,String> requestParams=new HashMap<String,String>();
				requestParams.put("accessid",Constant.ACCESSID_LOCAL);
				requestParams.put("account", username);
				Map<String,String> headerParams=new HashMap<String,String>();
				headerParams.put("sign", MD5.md5(password));
				getHttpService().exeNetRequest(Constant.ServerAPI.userLogin,requestParams,headerParams,new UIRunnable() {
					
					@Override
					public void run() {
						Boolean isChecked=ckAutoLogin.isChecked();
						getAppContext().getSharedPreferencesUtils().putBoolean(Constant.SharedPreferences.LOGIN_AUTOLOGIN, isChecked);
						if(isChecked){
							getAppContext().getSharedPreferencesUtils().putString(Constant.SharedPreferences.LOGIN_ACCOUNT, username);
							getAppContext().getSharedPreferencesUtils().putString(Constant.SharedPreferences.LOGIN_PASSWORD, password);
						}else{
							getAppContext().getSharedPreferencesUtils().putString(Constant.SharedPreferences.LOGIN_ACCOUNT, Constant.EMPTYSTR);
							getAppContext().getSharedPreferencesUtils().putString(Constant.SharedPreferences.LOGIN_PASSWORD, Constant.EMPTYSTR);
						}
						
						getAppContext().initUserInfo(getContent());
						
						finish();
					}
					
				});
			}
		}else if(v.getId()==R.id.login_btn_register){
			startActivity(new Intent(this,RegisterActivity.class));
		}else if(v.getId()==R.id.login_btn_forgetpwd){
			startActivity(new Intent(this,ForgetPwdActivity.class));
		}
	}

}