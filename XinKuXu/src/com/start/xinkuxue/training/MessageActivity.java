package com.start.xinkuxue.training;

import java.util.HashMap;
import java.util.Map;

import start.core.AppException;
import start.widget.CustomEditText;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.core.Constant;
import com.start.service.HttpRunnable;
import com.start.service.HttpServer;
import com.start.service.Response;
import com.start.service.User;
import com.start.xinkuxue.R;

/**
 * 留言板
 * @author zhenyao
 *
 */
public class MessageActivity extends BaseActivity{

	private TextView tvTitle;
	private CustomEditText et_title,et_content;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		tvTitle=(TextView)findViewById(R.id.tvTitle);
		tvTitle.setText("留言板");
		et_title=(CustomEditText)findViewById(R.id.et_title);
		et_content=(CustomEditText)findViewById(R.id.et_content);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_submit){
			String title=String.valueOf(et_title.getText());
			String content=String.valueOf(et_content.getText());
			if(TextUtils.isEmpty(title)){
				getHandlerContext().makeTextLong(getString(R.string.message_title_hint));
				return;
			}
			if(TextUtils.isEmpty(content)){
				getHandlerContext().makeTextLong(getString(R.string.message_content_hint));
				return;
			}
			HttpServer hServer = new HttpServer(Constant.URL.SaveForm,getHandlerContext());
			Map<String, String> params = new HashMap<String, String>();
			params.put("access_token", User.ACCESSKEY);
			params.put("classId","45");
			params.put("Id","0");
			params.put("title",title);
			params.put("content", content);
			hServer.setParams(params);
			hServer.get(new HttpRunnable() {
	
				@Override
				public void run(Response response) throws AppException {
					getHandlerContext().makeTextLong("留言成功");
					finish();
				}
				
			});
		}
	}
	
}
