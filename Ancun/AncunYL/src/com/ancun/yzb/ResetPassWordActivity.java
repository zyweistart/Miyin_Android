package com.ancun.yzb;

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
			HttpServer hServer=new HttpServer(Constant.URL.v4pwdReset, getHandlerContext());
			Map<String,String> headers=new HashMap<String,String>();
			headers.put("sign", "");
			hServer.setHeaders(headers);
			Map<String,String> params=new HashMap<String,String>();
			params.put("phone",phone);
			params.put("password", MD5.md5(password));
			params.put("authcode", authcode);
			params.put("operatesource", "9");
			params.put("ip", "");
			params.put("mac", "");
			params.put("loginflag", "1");
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