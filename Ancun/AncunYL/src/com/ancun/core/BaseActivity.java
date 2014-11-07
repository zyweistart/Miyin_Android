package com.ancun.core;

import start.core.AppActivity;
import start.core.AppException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.ancun.core.Constant.ResultCode;
import com.ancun.service.impl.ContactDaoImpl;
import com.ancun.service.impl.RecentDaoImpl;
import com.ancun.yl.BaseContext;
import com.ancun.yl.LoginActivity;
import com.ancun.yl.R;

public class BaseActivity extends AppActivity {

	private RecentDaoImpl mRecentDaoImpl;
	private ContactDaoImpl mContactDaoImpl;
	private InputMethodManager mInputMethodManager;
	
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
	
	public RecentDaoImpl getRecentDaoImpl(){
		if(mRecentDaoImpl==null){
			mRecentDaoImpl=new RecentDaoImpl(this);
		}
		return mRecentDaoImpl;
	}
	
	public ContactDaoImpl getContactDaoImpl(){
		if(mContactDaoImpl==null){
			mContactDaoImpl=new ContactDaoImpl(this);
		}
		return mContactDaoImpl;
	}
	
	public InputMethodManager getInputMethodManager(){
		if(mInputMethodManager==null){
			mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		}
		return mInputMethodManager;
	}
	
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