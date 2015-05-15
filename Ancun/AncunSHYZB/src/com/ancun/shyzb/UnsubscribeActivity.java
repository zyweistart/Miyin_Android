package com.ancun.shyzb;

import java.util.HashMap;
import java.util.Map;

import start.core.AppException;
import start.core.AppManager;
import start.service.HttpRunnable;
import start.service.HttpServer;
import start.service.Response;
import start.utils.MD5;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ancun.core.BaseActivity;
import com.ancun.core.Constant;
import com.ancun.service.ButtonTextWatcher;
import com.ancun.service.User;

public class UnsubscribeActivity extends BaseActivity {

	private EditText et_pwd;
	private Button btn_submit;
	private LinearLayout ll_first_frame,ll_second_frame;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsubscribe);
		setMainHeadTitle(getString(R.string.unsubscribe));
		et_pwd =(EditText)findViewById(R.id.et_pwd);
		btn_submit =(Button)findViewById(R.id.btn_submit);
		et_pwd.addTextChangedListener(new ButtonTextWatcher(btn_submit));
		ll_first_frame =(LinearLayout)findViewById(R.id.ll_first_frame);
		ll_second_frame =(LinearLayout)findViewById(R.id.ll_second_frame);
    }

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_submit){
			String password=String.valueOf(et_pwd.getText());
			if(TextUtils.isEmpty(password)){
				getHandlerContext().makeTextLong(getString(R.string.passwordhint));
				return;
			}
			HttpServer hServer=new HttpServer(Constant.URL.ylTaoCancelAPP, getHandlerContext());
			Map<String,String> headers=new HashMap<String,String>();
			headers.put("sign", User.ACCESSKEY);
			hServer.setHeaders(headers);
			Map<String,String> params=new HashMap<String,String>();
			params.put("accessid", User.ACCESSID);
			params.put("ownerno", getAppContext().currentUser().getPhone());
			params.put("password", MD5.md5(password));
			hServer.setParams(params);
			hServer.get(new HttpRunnable() {
				
				@Override
				public void run(Response response) throws AppException {
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							getAppContext().currentUser().clearCacheUser();
							ll_first_frame.setVisibility(View.GONE);
							ll_second_frame.setVisibility(View.VISIBLE);
						}
					});
					
				}
				
			});
		}else if(v.getId()==R.id.btn_return){
			AppManager.getInstance().finishAllActivity();
		}else{
			super.onClick(v);
		}
	}
    
}