package com.start.core;

import start.core.AppActivity;
import start.core.AppException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.start.core.Constant.ResultCode;
import com.start.medical.BaseContext;
import com.start.medical.R;
import com.start.medical.personal.LoginActivity;

public class BaseActivity extends AppActivity {

	public BaseContext getAppContext() {
		return (BaseContext)BaseContext.getInstance();
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.module_header_left_back){
			finish();
		}else if(v.getId()==R.id.module_header_right_more){
			finish();
		}
	}
	
	@Override
	public void onProcessMessage(Message msg) throws AppException {
		switch(msg.what){
		case ResultCode.NOLOGIN:
			goLogin(String.valueOf(msg.obj));
			break;
		default:
			super.onProcessMessage(msg);
			break;
		}
	}
	
	/**
	 * 设置导航标题
	 */
	public void setMainHeadTitle(String title){
		TextView tvTitle=(TextView)findViewById(R.id.module_header_title);
		if(tvTitle!=null){
			tvTitle.setText(title);
		}
	}
	
	public void goLogin(Boolean autoLogin){
		Bundle bundle=new Bundle();
		bundle.putBoolean(LoginActivity.BUNLE_AUTOLOGINFLAG, autoLogin);
		Intent intent=new Intent(this,LoginActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
	
	public void goLogin(String message){
		getAppContext().currentUser().clearCachePassword();
		Bundle bundle=new Bundle();
		bundle.putString(LoginActivity.BUNLE_MESSAGE, message);
		Intent intent=new Intent(this,LoginActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
	
}