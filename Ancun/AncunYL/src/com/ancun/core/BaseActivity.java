package com.ancun.core;

import start.core.AppActivity;
import start.core.AppException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.ancun.core.Constant.ResultCode;
import com.ancun.yzb.BaseContext;
import com.ancun.yzb.LoginActivity;
import com.ancun.yzb.R;

public class BaseActivity extends AppActivity {

	public BaseContext getAppContext() {
		return (BaseContext)BaseContext.getInstance();
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_back){
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
		TextView tvTitle=(TextView)findViewById(R.id.main_head_title);
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