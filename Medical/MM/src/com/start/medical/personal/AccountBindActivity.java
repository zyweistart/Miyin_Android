package com.start.medical.personal;

import java.util.HashMap;
import java.util.Map;

import start.core.AppException;
import start.service.HttpServer;
import start.service.Response;
import start.service.UIRunnable;
import start.widget.CustomEditText;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.start.core.BaseActivity;
import com.start.core.Constant;
import com.start.medical.R;
import com.start.service.User;

/**
 * 就诊信息绑定
 * @author Start
 * 
 */
public class AccountBindActivity extends BaseActivity  {
	
	private CustomEditText et_bind_account;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_bind);
		setMainHeadTitle("就诊信息绑定");
		et_bind_account=(CustomEditText)findViewById(R.id.et_bind_account);
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_bind){
			String account=String.valueOf(et_bind_account.getText());
			if(!TextUtils.isEmpty(account)){
				HttpServer hServer=new HttpServer(Constant.URL.hisinfoBind, getHandlerContext());
				Map<String,String> headers=new HashMap<String,String>();
				headers.put("sign", User.ACCESSKEY);
				hServer.setHeaders(headers);
				Map<String, String> params = new HashMap<String, String>();
				params.put("accessid", User.ACCESSID);
				params.put("name", account);
				hServer.setParams(params);
				hServer.get(new UIRunnable() {
					
					@Override
					public void run(Response response) throws AppException {
						
						getHandlerContext().makeTextLong("还好");
						
					}
					
				});
			}else{
				getHandlerContext().makeTextLong("请输入账户");
			}
		}else{
			super.onClick(v);
		}
	}
	
}
