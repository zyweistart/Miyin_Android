package com.start.medical.more;

import java.util.HashMap;
import java.util.Map;

import start.core.AppException;
import start.service.HttpServer;
import start.service.Response;
import start.service.HttpRunnable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.start.core.BaseActivity;
import com.start.core.Constant;
import com.start.medical.R;
import com.start.service.User;

/**
 * 意见反馈
 * @author start
 *
 */
public class FeedbackActivity extends BaseActivity {
	
	private EditText et_content;
	private EditText et_contact;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		setMainHeadTitle(getString(R.string.feedback));
		et_content=(EditText)findViewById(R.id.et_content);
		et_contact=(EditText)findViewById(R.id.et_contact);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_submit){
			String content=String.valueOf(et_content.getText());
			if(TextUtils.isEmpty(content)){
				getHandlerContext().makeTextLong("请输入反馈内容");
				return;
			}
			String contact=String.valueOf(et_contact.getText());
			if(TextUtils.isEmpty(contact)){
				getHandlerContext().makeTextLong("请输入联系人信息");
				return;
			}
			HttpServer hServer=new HttpServer(Constant.URL.userFeedback, getHandlerContext());
			Map<String,String> headers=new HashMap<String,String>();
			headers.put("sign", User.ACCESSKEY);
			hServer.setHeaders(headers);
			Map<String,String> params=new HashMap<String,String>();
			params.put("accessid", User.ACCESSID);
			params.put("content", content);
			params.put("contact", contact);
			hServer.setParams(params);
			hServer.get(new HttpRunnable() {
				
				@Override
				public void run(Response response) throws AppException {
					getHandlerContext().makeTextLong("反馈成功，你的意见是我们前进的动力");
					finish();
				}
				
			});
		}
	}
	
}
