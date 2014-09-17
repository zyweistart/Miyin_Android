package com.start.medical.personal;

import java.util.HashMap;
import java.util.Map;

import start.core.AppException;
import start.service.HttpServer;
import start.service.Response;
import start.service.UIRunnable;
import start.utils.MD5;
import start.utils.StringUtils;
import start.utils.TimeUtils;
import start.widget.CustomEditText;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.core.Constant;
import com.start.core.Constant.Handler;
import com.start.medical.R;
import com.start.service.User;

/**
 * 注册
 * @author start
 *
 */
public class RegisterActivity extends BaseActivity implements OnClickListener {
	
	protected String phone;
	protected String authcode;
	protected String password;
	
	protected Button btn_re_get_checksum;
	protected CustomEditText et_phone;
	protected CustomEditText et_checksum;
	protected CustomEditText et_setting_password;
	protected CustomEditText et_setting_repassword;
	protected LinearLayout ll_main_frame;
	protected LinearLayout ll_first_frame;
	protected LinearLayout ll_second_frame;
	protected TextView success_title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_reset_password);
		setMainHeadTitle(getString(R.string.register));
		btn_re_get_checksum=(Button)findViewById(R.id.btn_re_get_checksum);
		et_phone=(CustomEditText)findViewById(R.id.et_phone);
		et_checksum=(CustomEditText)findViewById(R.id.et_checksum);
		et_setting_password=(CustomEditText)findViewById(R.id.et_setting_password);
		et_setting_repassword=(CustomEditText)findViewById(R.id.et_setting_repassword);
		ll_main_frame=(LinearLayout)findViewById(R.id.ll_main_frame);
		ll_first_frame=(LinearLayout)findViewById(R.id.ll_first_frame);
		ll_second_frame=(LinearLayout)findViewById(R.id.ll_second_frame);
		success_title=(TextView)findViewById(R.id.success_title);
		success_title.setText(R.string.register_success);
	}
	
	@Override
	public void onProcessMessage(Message msg) throws AppException {
		switch (msg.what) {
		case Handler.REGISTER_RESET_PASSWORD_STEP1:
			ll_main_frame.setVisibility(View.VISIBLE);
			btn_re_get_checksum.setEnabled(false);
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					int sec=60;
					while(sec>0){
						sec--;
						final int n=sec;
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								if(n==0){
									btn_re_get_checksum.setText(R.string.regetauthcode);
									btn_re_get_checksum.setEnabled(true);
								}else{
									btn_re_get_checksum.setText(n+"秒,后可重新获取验证码");
									btn_re_get_checksum.setEnabled(false);
								}
							}
							
						});
						
						TimeUtils.sleep(1000);
					}
					
				}
				
			}).start();
			
			break;
		case Handler.REGISTER_RESET_PASSWORD_STEP2:
			ll_first_frame.setVisibility(View.GONE);
			ll_second_frame.setVisibility(View.VISIBLE);
			break;
		default:
			super.onProcessMessage(msg);
			break;
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_re_get_checksum){
			phone=String.valueOf(et_phone.getText());
			if(StringUtils.isEmpty(phone)){
				getHandlerContext().makeTextLong(getString(R.string.phoneemptytip));
				return;
			}
			getAuthCode(1);
		}else if(v.getId()==R.id.btn_next){
			phone=String.valueOf(et_phone.getText());
			if(StringUtils.isEmpty(phone)){
				getHandlerContext().makeTextLong(getString(R.string.phoneemptytip));
				return;
			}
			authcode=String.valueOf(et_checksum.getText());
			if(StringUtils.isEmpty(authcode)){
				getHandlerContext().makeTextLong(getString(R.string.autocodeemptytip));
				return;
			}
			password=String.valueOf(et_setting_password.getText());
			if(StringUtils.isEmpty(password)){
				getHandlerContext().makeTextLong(getString(R.string.pwdemptytip));
				return ;
			}
			if(!password.equals(String.valueOf(et_setting_repassword.getText()))){
				getHandlerContext().makeTextLong(getString(R.string.twopwddifftip));
				return;
			}
			HttpServer hServer=new HttpServer(Constant.URL.userReg, getHandlerContext());
			Map<String,String> headers=new HashMap<String,String>();
			headers.put("sign", User.USER_ACCESSKEY_LOCAL);
			hServer.setHeaders(headers);
			Map<String,String> params=new HashMap<String,String>();
			params.put("accessid", User.USER_ACCESSID_LOCAL);
			params.put("mobile", phone);
			params.put("pwd", MD5.md5(password));
			params.put("authcode", authcode);
			params.put("regsource", "10");
			params.put("loginflag", "1");
			hServer.setParams(params);
			hServer.get(new UIRunnable() {
				
				@Override
				public void run(Response response) {
					getHandlerContext().getHandler().sendEmptyMessage(Handler.REGISTER_RESET_PASSWORD_STEP2);
				}
				
			});
		}else if(v.getId()==R.id.btn_done){
			getAppContext().currentUser().addCacheUser(phone, MD5.md5(password), true);
			goLogin(true);
		}else if(v.getId()==R.id.tvHaveAccount){
			goLogin(false);
		}
	}
	
	/**
	 * 获取验证码
	 */
	public void getAuthCode(int type){
		HttpServer hServer=new HttpServer(Constant.URL.useracodeGet, getHandlerContext());
		Map<String,String> headers=new HashMap<String,String>();
		headers.put("sign", User.USER_ACCESSKEY_LOCAL);
		hServer.setHeaders(headers);
		Map<String,String> params=new HashMap<String,String>();
		params.put("accessid", User.USER_ACCESSID_LOCAL);
		params.put("mobile", phone);
		params.put("type", String.valueOf(type));
		hServer.setParams(params);
		hServer.get(new UIRunnable() {
			
			@Override
			public void run(Response response) {
				getHandlerContext().getHandler().sendEmptyMessage(Handler.REGISTER_RESET_PASSWORD_STEP1);
			}
			
		});
	}
	
}