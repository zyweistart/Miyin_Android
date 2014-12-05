package com.start.zmcy;

import java.util.HashMap;
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
import com.start.core.Constant;
import com.start.core.Constant.Handler;
import com.start.service.HttpRunnable;
import com.start.service.HttpServer;
import com.start.service.Response;
import com.start.service.SocialService;


public class RegisterActivity extends BaseActivity{
	
	public static final int LOGINSUCCESS=255;
	public static final String REGISTERTYPE="REGISTERTYPE";
	public static final String REGISTERTYPE_WX="REGISTERTYPE_WX";
	public static final String REGISTERTYPE_QQ="REGISTERTYPE_QQ";
	public static final String REGISTERTYPE_ACCOUNT="REGISTERTYPE_ACCOUNT";
	public static final String STR_ACCOUNT="STR_ACCOUNT";
	public static final String STR_PASSWORD="STR_PASSWORD";
	
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
		int what=msg.what;
		if(what==Handler.HANDLERTHIRDPARTYLANDINGQQ||
				what==Handler.HANDLERTHIRDPARTYLANDINGWX){
			Bundle bundle=new Bundle();
			if(what==Handler.HANDLERTHIRDPARTYLANDINGQQ){
				//QQ登陆
				bundle.putString(REGISTERTYPE, REGISTERTYPE_QQ);
			}else{
				//WX登陆
				bundle.putString(REGISTERTYPE, REGISTERTYPE_WX);
			}
			Map<String,Object> info=(Map<String,Object>)msg.obj;
			Set<String> keys = info.keySet();
			for (String key : keys) {
				bundle.putString(key, String.valueOf(info.get(key)));
			}
			Intent intent=new Intent();
			intent.putExtras(bundle);
			registerSuccess(intent);
		}else{
			super.onProcessMessage(msg);
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_register){
			final String account=String.valueOf(et_register_account.getText());
			final String password=String.valueOf(et_register_password.getText());
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
			
			HttpServer hServer = new HttpServer(Constant.URL.RegUser,getHandlerContext());
			Map<String, String> params = new HashMap<String, String>();
			params.put("userName",account);
			params.put("pwd", password);
			params.put("pwd2", rePassword);
			hServer.setParams(params);
			hServer.get(new HttpRunnable() {

				@Override
				public void run(Response response) throws AppException {
					
					Bundle bundle=new Bundle();
					bundle.putString(STR_ACCOUNT, account);
					bundle.putString(STR_PASSWORD, password);
					bundle.putString(REGISTERTYPE, REGISTERTYPE_ACCOUNT);
					Intent intent=new Intent();
					intent.putExtras(bundle);
					registerSuccess(intent);
					
				}

			});
			
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
