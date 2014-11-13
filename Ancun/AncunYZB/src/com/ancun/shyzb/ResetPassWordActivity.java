package com.ancun.shyzb;

import java.util.HashMap;
import java.util.Map;

import start.service.HttpRunnable;
import start.service.HttpServer;
import start.service.Response;
import start.utils.MD5;
import start.utils.StringUtils;
import android.os.Bundle;
import android.view.View;

import com.ancun.core.Constant;
import com.ancun.core.Constant.Handler;
import com.ancun.service.User;

/**
 * 重置密码
 * @author start
 *
 */
public class ResetPassWordActivity  extends RegisterActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setMainHeadTitle(getString(R.string.reset_password));
		success_title.setText(R.string.reset_password_success);
		fr_server.setVisibility(View.GONE);
		txt_tip.setVisibility(View.GONE);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_re_get_checksum){
			phone=String.valueOf(et_phone.getText());
			if(StringUtils.isEmpty(phone)){
				getHandlerContext().makeTextLong(getString(R.string.phoneemptytip));
				return;
			}
			getAuthCode(2);
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
			HttpServer hServer=new HttpServer(Constant.URL.userSignup, getHandlerContext());
			Map<String,String> headers=new HashMap<String,String>();
			headers.put("sign", User.USER_ACCESSKEY_LOCAL);
			hServer.setHeaders(headers);
			Map<String,String> params=new HashMap<String,String>();
			params.put("accessid",User.USER_ACCESSID_LOCAL);
			params.put("userTel",phone);
			params.put("password", MD5.md5(password));
			params.put("type","2");
			params.put("authcode", authcode);
			params.put("signupsource", "3");
			hServer.setParams(params);
			hServer.get(new HttpRunnable() {
				
				@Override
				public void run(Response response) {
					getHandlerContext().getHandler().sendEmptyMessage(Handler.REGISTER_RESET_PASSWORD_STEP2);
				}
				
			});
		}else{
			super.onClick(v);
		}
	}
}