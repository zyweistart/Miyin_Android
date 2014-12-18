package com.start.zmcy;

import java.util.HashMap;
import java.util.Map;

import start.core.AppException;
import start.utils.StringUtils;
import start.widget.CustomEditText;
import android.os.Bundle;
import android.view.View;

import com.start.core.BaseActivity;
import com.start.core.Constant;
import com.start.service.HttpRunnable;
import com.start.service.HttpServer;
import com.start.service.Response;
import com.start.service.User;

/**
 * 咨询
 */
public class ConsultationActivity extends BaseActivity{
	
	public static final String CONSOLTATIONID="CONSOLTATIONID";
	private String expertId;
	private CustomEditText et_content;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consultation);
		setMainHeadTitle(getString(R.string.consultation));
		et_content=(CustomEditText)findViewById(R.id.et_content);
		
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			expertId=bundle.getString(CONSOLTATIONID);
		}else{
			finish();
		}
		
 	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_submit){
			String content=String.valueOf(et_content.getText());
			if(StringUtils.isEmpty(content)){
				getHandlerContext().makeTextLong(getString(R.string.questioncontenthint));
				return;
			}
			HttpServer hServer = new HttpServer(Constant.URL.UserLogin,getHandlerContext());
			Map<String, String> params = new HashMap<String, String>();
			params.put("access_token",User.ACCESSKEY);
			params.put("expertId",expertId);
			params.put("content", content);
			hServer.setParams(params);
			hServer.get(new HttpRunnable() {

				@Override
				public void run(Response response) throws AppException {
					getHandlerContext().makeTextLong("咨询成功，请等待专家解答!");
					finish();
				}
				
			});
		}else{
			super.onClick(v);
		}
	}
	
}
