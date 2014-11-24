package com.start.zmcy;

import java.util.Map;
import java.util.Set;

import start.core.AppException;
import start.widget.CustomEditText;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.start.core.BaseActivity;
import com.start.core.Constant.Handler;
import com.start.service.SocialService;


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
	
	@SuppressWarnings("unchecked")
	@Override
	public void onProcessMessage(Message msg) throws AppException {
		switch(msg.what){
		case Handler.HANDLERTHIRDPARTYLANDINGQQ:
			//QQ登陆
		case Handler.HANDLERTHIRDPARTYLANDINGWX:
			//WX登陆
			Map<String,Object> info=(Map<String,Object>)msg.obj;
			Set<String> keys = info.keySet();
			Bundle bundle=new Bundle();
			for (String key : keys) {
				bundle.putString(key, String.valueOf(info.get(key)));
			}
			Intent intent=new Intent();
			intent.putExtras(bundle);
			registerSuccess(intent);
			break;
		default:
			super.onProcessMessage(msg);
			break;
		}
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
			registerSuccess(null);
		}else if(v.getId()==R.id.qq_login){
			SocialService.socialQQLogin(this);
		}else if(v.getId()==R.id.wx_login){
			SocialService.socialWexinLogin(this);
		}else{
			super.onClick(v);
		}
	}
	
	public void registerSuccess(Intent intent){
		setResult(LOGINSUCCESS,intent);
		finish();
	}
	
}
