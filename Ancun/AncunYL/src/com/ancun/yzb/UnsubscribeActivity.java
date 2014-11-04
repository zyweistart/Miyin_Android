package com.ancun.yzb;

import java.util.HashMap;
import java.util.Map;

import start.core.AppException;
import start.service.HttpRunnable;
import start.service.HttpServer;
import start.service.Response;
import start.utils.MD5;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ancun.core.BaseActivity;
import com.ancun.core.Constant;
import com.ancun.service.ButtonTextWatcher;
import com.ancun.service.User;

public class UnsubscribeActivity extends BaseActivity {

	private EditText et_pwd;
	private Button btn_submit,btn_return;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsubscribe);
		setMainHeadTitle(getString(R.string.unsubscribe));
		et_pwd =(EditText)findViewById(R.id.et_pwd);
		btn_submit =(Button)findViewById(R.id.btn_submit);
		btn_return =(Button)findViewById(R.id.btn_return);
		et_pwd.addTextChangedListener(new ButtonTextWatcher(btn_submit));
    }

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_submit){
			String password=String.valueOf(et_pwd.getText());
			if(TextUtils.isEmpty(password)){
				getHandlerContext().makeTextLong(getString(R.string.passwordhint));
				return;
			}
			HttpServer hServer=new HttpServer(Constant.URL.v4Canserv, getHandlerContext());
			Map<String,String> headers=new HashMap<String,String>();
			headers.put("sign", User.ACCESSKEY);
			hServer.setHeaders(headers);
			Map<String,String> params=new HashMap<String,String>();
			params.put("accessid", User.ACCESSID);
			params.put("password", MD5.md5(password));
			hServer.setParams(params);
			hServer.get(new HttpRunnable() {
				
				@Override
				public void run(Response response) throws AppException {
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							btn_submit.setVisibility(View.GONE);
							btn_return.setVisibility(View.VISIBLE);
						}
					});
					
				}
				
			});
		}else if(v.getId()==R.id.btn_return){
			finish();
		}else{
			super.onClick(v);
		}
	}
    
}