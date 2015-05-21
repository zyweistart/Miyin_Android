package com.ancun.unicom;

import java.util.HashMap;
import java.util.Map;

import start.core.AppException;
import start.service.HttpRunnable;
import start.service.HttpServer;
import start.service.Response;
import start.utils.LogUtils;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ancun.core.BaseActivity;
import com.ancun.core.Constant;
import com.ancun.service.ButtonTextWatcher;
import com.ancun.service.User;

public class FeedbackActivity extends BaseActivity {

	private EditText et_content,et_contact;
	private Button btn_submit;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		setMainHeadTitle(getString(R.string.feedback));
		et_content =(EditText)findViewById(R.id.et_content);
		et_contact =(EditText)findViewById(R.id.et_contact);
		btn_submit =(Button)findViewById(R.id.btn_submit);
		et_content.addTextChangedListener(new ButtonTextWatcher(btn_submit));
    }

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_submit){
			String content=String.valueOf(et_content.getText());
			if(TextUtils.isEmpty(content)){
				getHandlerContext().makeTextLong(getString(R.string.feedbackcontentemptytip));
				return;
			}
			String contact=String.valueOf(et_contact.getText());
			HttpServer hServer=new HttpServer(Constant.URL.feedback, getHandlerContext());
			Map<String,String> headers=new HashMap<String,String>();
			headers.put("sign", User.ACCESSKEY);
			hServer.setHeaders(headers);
			Map<String,String> params=new HashMap<String,String>();
			params.put("accessid", User.ACCESSID);
			params.put("content",content);
			params.put("contractMethod", contact);
			params.put("type", "8");
			params.put("termtype", "4");
			try {
				PackageManager packageManager = getPackageManager();
				PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
				params.put("versionno", packInfo.versionCode+"");
			} catch (NameNotFoundException e) {
				LogUtils.logError(e);
			}
			hServer.setParams(params);
			hServer.get(new HttpRunnable() {
				
				@Override
				public void run(Response response) throws AppException {
					getHandlerContext().makeTextLong("反馈成功");
					finish();
				}
				
			});
			
		}else{
			super.onClick(v);
		}
	}
    
}
