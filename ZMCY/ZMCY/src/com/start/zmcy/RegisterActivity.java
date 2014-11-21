package com.start.zmcy;

import start.widget.CustomEditText;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.start.core.BaseActivity;


public class RegisterActivity extends BaseActivity{
	
	public static final int LOGINSUCCESS=255;
	
	private CustomEditText et_register_account;
	private CustomEditText et_register_password;
	private CustomEditText et_register_repassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		setMainHeadTitle(getString(R.string.registernewuser));
		et_register_account=(CustomEditText)findViewById(R.id.et_register_account);
		et_register_password=(CustomEditText)findViewById(R.id.et_register_password);
		et_register_repassword=(CustomEditText)findViewById(R.id.et_register_repassword);
		
 	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_register){
			String account=String.valueOf(et_register_account.getText());
			String password=String.valueOf(et_register_password.getText());
			String rePassword=String.valueOf(et_register_repassword.getText());
			if(TextUtils.isEmpty(account)){
				getHandlerContext().makeTextLong(getString(R.string.accounthint));
				return;
			}
			if(TextUtils.isEmpty(password)){
				getHandlerContext().makeTextLong(getString(R.string.passwordhint));
				return;
			}
			if(!password.equals(rePassword)){
				getHandlerContext().makeTextLong(getString(R.string.passworddiffhint));
				return;
			}
			//TODO:注册动作
			registerSuccess();
		}else{
			super.onClick(v);
		}
	}
	
	public void registerSuccess(){
		setResult(LOGINSUCCESS);
		finish();
	}
	
}
