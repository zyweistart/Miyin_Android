package com.start.medical.wikipedia;

import java.util.HashMap;
import java.util.Map;

import start.core.AppException;
import start.service.HttpServer;
import start.service.Response;
import start.service.UIRunnable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.core.Constant;
import com.start.medical.R;
import com.start.service.User;

/**
 * 健康百科-详情
 * @author start
 *
 */
public class HealthWikipediaDetailActivity extends BaseActivity {
	
	public static final String RECORDNO="recordno";
	
	private String recordno;
	
	private TextView detail_title;
	private TextView detail_source;
	private TextView detail_content;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_healthwikipedia_detail);
		setMainHeadTitle(getString(R.string.mainfunctiontxt7));
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			recordno=bundle.getString(RECORDNO);
		}
		
		if(TextUtils.isEmpty(recordno)){
			finish();
		}

		detail_title=(TextView)findViewById(R.id.detail_title);
		detail_source=(TextView)findViewById(R.id.detail_source);
		detail_content=(TextView)findViewById(R.id.detail_content);
		
		this.setDetailMore();
		
		this.loadData();
		
	}
	
	public void loadData(){
		HttpServer hServer=new HttpServer(Constant.URL.htwikidisDetail, getHandlerContext());
		Map<String,String> headers=new HashMap<String,String>();
		headers.put("sign", User.COMMON_ACCESSKEY_LOCAL);
		hServer.setHeaders(headers);
		Map<String, String> params = new HashMap<String, String>();
		params.put("accessid", User.COMMON_ACCESSID_LOCAL);
		params.put("recordno", recordno);
		hServer.setParams(params);
		hServer.get(new UIRunnable() {
			
			@Override
			public void run(Response response) throws AppException {
				final Map<String,String> data=response.getMapData("disinfo");
				runOnUiThread(new Runnable() {
					
					public void run() {
						detail_title.setText(data.get("name"));
						detail_source.setText("发布者："+data.get("dept")+"发布时间："+data.get("desc"));
						detail_content.setText(data.get("overview"));
					}
					
				});
				
			}
			
		});
	}
	
	public void setDetailMore(){
		Button module_header_right_more=(Button)findViewById(R.id.module_header_right_more);
		module_header_right_more.setVisibility(View.VISIBLE);
		module_header_right_more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
			}
			
		});
	}
	
}