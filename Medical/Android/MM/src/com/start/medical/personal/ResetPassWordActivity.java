package com.start.medical.personal;

import java.util.HashMap;
import java.util.Map;

import start.service.HttpServer;
import start.service.Response;
import start.service.UIRunnable;
import start.utils.MD5;
import start.utils.StringUtils;
import android.os.Bundle;
import android.view.View;

import com.start.core.Constant;
import com.start.core.Constant.Handler;
import com.start.medical.R;
import com.start.service.User;

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
			HttpServer hServer=new HttpServer(Constant.URL.userpwdReset, getHandlerContext());
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
		}else{
			super.onClick(v);
		}
	}
}