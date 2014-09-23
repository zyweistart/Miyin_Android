package com.start.core;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.start.navigation.AppContext;
import com.start.navigation.R;
import com.start.service.HttpService;
import com.umeng.analytics.MobclickAgent;

public abstract class CoreActivity extends Activity{

	protected final String TAG=this.getClass().getSimpleName();
	
	@Override
	protected void onResume() {
		super.onResume();
		if(!Constant.ISTEST){
			MobclickAgent.onResume(this);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(!Constant.ISTEST){
			MobclickAgent.onPause(this);
		}
	}
	
	private HttpService httpService;
	
	public HttpService getHttpService() {
		if(httpService==null){
			httpService=new HttpService(this);
		}
		return httpService;
	}
	
	public AppContext getAppContext(){
		return AppContext.getInstance();
	}
	
	public void makeTextShort(int resId){
		getAppContext().makeTextShort(resId);
    }
	
	public void makeTextShort(String text){
		getAppContext().makeTextShort(text);
    }
    
	public void makeTextLong(int resId){
		getAppContext().makeTextLong(resId);
    }
	
	public void makeTextLong(String text){
		getAppContext().makeTextLong(text);
    }
	
	public void setCurrentActivityTitle(int resId){
		setCurrentActivityTitle(getString(resId));
	}
	
	public void setCurrentActivityTitle(String title){
		TextView tv=(TextView)findViewById(R.id.module_main_header_content_title);
		if(tv!=null){
			tv.setText(title);
		}
	}
	
	public Handler handler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			onMainUpdate(msg.what);
		}
		
	};
	
	protected void onMainUpdate(int what){}
	
}