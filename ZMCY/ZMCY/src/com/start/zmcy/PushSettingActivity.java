package com.start.zmcy;

import java.util.HashMap;
import java.util.Map;

import start.core.AppException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.core.Constant;
import com.start.service.HttpRunnable;
import com.start.service.HttpServer;
import com.start.service.Response;
import com.start.service.User;

/**
 * 推送设置
 */
public class PushSettingActivity extends BaseActivity{
	
	private Drawable img_off;
	private Drawable img_on;
	
	private TextView txt1;
	
	private Boolean state;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pushsettting);
		setMainHeadTitle(getString(R.string.pushsetting));
		
		img_off= getResources().getDrawable(R.drawable.button_state_off);
		img_off.setBounds(0, 0, img_off.getMinimumWidth(), img_off.getMinimumHeight());
		img_on= getResources().getDrawable(R.drawable.button_state_no);
		img_on.setBounds(0, 0, img_on.getMinimumWidth(), img_on.getMinimumHeight());
		
		txt1=(TextView)findViewById(R.id.txt1);
		

		HttpServer hServer = new HttpServer(Constant.URL.GetPushState,getHandlerContext());
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", User.ACCESSKEY);
		hServer.setParams(params);
		hServer.get(new HttpRunnable() {

			@Override
			public void run(Response response) throws AppException {
				state=Boolean.parseBoolean(response.getData("PushState")+"");
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						setState();
					}
				});
			}
			
		});
		
		
 	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.txt1){
			state=!state;
			setState();
			HttpServer hServer = new HttpServer(Constant.URL.ChangePushState,getHandlerContext());
			Map<String, String> params = new HashMap<String, String>();
			params.put("access_token", User.ACCESSKEY);
			params.put("state", state+"");
			hServer.setParams(params);
			hServer.get(new HttpRunnable() {

				@Override
				public void run(Response response) throws AppException {
					getHandlerContext().makeTextShort("设置成功");
				}
				
			});
		}else{
			super.onClick(v);
		}
	}
	
	public void setState(){
		if(state){
			txt1.setCompoundDrawables(null, null, img_on, null);
		}else{
			txt1.setCompoundDrawables(null, null, img_off, null);
		}
	}
	
}
