package com.start.medical.registered.outpatient;

import java.util.HashMap;
import java.util.Map;

import start.core.AppException;
import start.service.HttpServer;
import start.service.Response;
import start.service.UIRunnable;
import android.os.Bundle;
import android.text.TextUtils;

import com.start.core.BaseActivity;
import com.start.core.Constant;
import com.start.medical.R;
import com.start.service.User;

/**
 * 挂号详情
 * @author start
 * 
 */
public class OutpatientRegistrationDetailActivity extends BaseActivity {

	public static final String RECORDNO="ksmc";
	
	private String recordno;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_of_experts);
		setMainHeadTitle("挂号详情");
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			recordno=bundle.getString(RECORDNO);
		}
		
		if(TextUtils.isEmpty(recordno)){
			finish();
		}
		
		this.loadData();
		
	}
	
	public void loadData(){
		HttpServer hServer=new HttpServer(Constant.URL.hispbxxptkspbList, getHandlerContext());
		Map<String,String> headers=new HashMap<String,String>();
		headers.put("sign", User.ACCESSKEY);
		hServer.setHeaders(headers);
		Map<String, String> params = new HashMap<String, String>();
		params.put("accessid", User.ACCESSID);
		params.put("ksmc", recordno);
		hServer.setParams(params);
		hServer.get(new UIRunnable() {
			
			@Override
			public void run(Response response) throws AppException {
				getHandlerContext().makeTextLong("fdsfsdf");
			}
			
		});
	}
	
}
