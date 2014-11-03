package com.ancun.yzb;


import java.util.HashMap;
import java.util.Map;

import start.core.AppException;
import start.service.HttpRunnable;
import start.service.HttpServer;
import start.service.Response;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ancun.core.BaseActivity;
import com.ancun.core.Constant;
import com.ancun.service.User;
import com.ancun.yzb.layout.RecordingAdapter;

public class ExtractionConfirmActivity extends BaseActivity {
	
	private ImageView ivTitle=null;
	private TextView tvMessage=null;
	private ImageButton ibYes=null;
	private ImageButton ibNo=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);   
		setContentView(R.layout.activity_extraction_confirm);
		ivTitle=(ImageView)findViewById(R.id.recorded_appeal_confirm_title);
		tvMessage=(TextView)findViewById(R.id.recorded_appeal_confirm_message);
		final int appealType=getIntent().getExtras().getInt("appeal_type");
		final String fileno=getIntent().getExtras().getString("fileno");
		final int cerflag=getIntent().getExtras().getInt("cerflag");
		if (1==appealType) {//淘申述
			ivTitle.setBackgroundResource(R.drawable.apply_extracting_code);
			tvMessage.setText("凭提取码可在官网公开查询、验证本条通话录音，确定申请？");
		}else if (2==appealType) {//申请公证
			ivTitle.setBackgroundResource(R.drawable.appeal_notary);
			if(cerflag==1){
				tvMessage.setText("您确定将该录音提交至公证机构申办公证吗？");
			}else if(cerflag==2){
				tvMessage.setText("您确定要取消该录音申办公证吗？");
			}
		}
		ibYes=(ImageButton)findViewById(R.id.recorded_appeal_confirm_yes);
		ibYes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle=getIntent().getExtras();
				if (1==appealType) {
					Intent intentTaobao=new Intent(ExtractionConfirmActivity.this,ExtractionViewActivity.class);
					intentTaobao.putExtras(bundle);
					startActivityForResult(intentTaobao,RecordedDetailActivity.TAOBAOREQUESTCODE);
				}else if (2==appealType) {
					HttpServer hServer=new HttpServer(Constant.URL.v4recCer, getHandlerContext());
	 				Map<String,String> headers=new HashMap<String,String>();
	 				headers.put("sign", User.ACCESSKEY);
	 				hServer.setHeaders(headers);
	 				Map<String,String> params=new HashMap<String,String>();
	 				params.put("accessid", User.ACCESSID);
	 				params.put(RecordingAdapter.RECORDED_FILENO,fileno);
					//1:取消出证;2:申请出证
					if(cerflag==1){
						params.put(RecordingAdapter.RECORDED_CEFFLAG,"2");
					}else{
						params.put(RecordingAdapter.RECORDED_CEFFLAG,"1");
					}
	 				hServer.setParams(params);
	 				hServer.get(new HttpRunnable() {
	 					
	 					@Override
	 					public void run(Response response) throws AppException {
	 						
	 						Bundle resultBundle=new Bundle();
							resultBundle.putString(RecordingAdapter.RECORDED_FILENO, fileno);
							if(cerflag==1){
								Intent intentNotaryNotify=new Intent(ExtractionConfirmActivity.this,NotarySuccessActivity.class);
								intentNotaryNotify.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intentNotaryNotify);
								resultBundle.putInt(RecordingAdapter.RECORDED_CEFFLAG, 2);
							}else{
								resultBundle.putInt(RecordingAdapter.RECORDED_CEFFLAG, 1);
							}
							Intent resultIntent=new Intent();
							resultIntent.putExtras(resultBundle);
							setResult(3,resultIntent);
							finish();
	 						
	 					}
	 				});
				}
			}
		});
		ibNo=(ImageButton)findViewById(R.id.recorded_appeal_confirm_no);
		ibNo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==RecordedDetailActivity.TAOBAOREQUESTCODE){
			setResult(resultCode,data);
		}
		finish();
	}
	
}