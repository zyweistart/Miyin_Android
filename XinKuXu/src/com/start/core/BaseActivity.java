package com.start.core;

import start.core.AppActivity;
import start.core.AppException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.start.core.Constant.ResultCode;
import com.start.xinkuxue.BaseContext;
import com.start.xinkuxue.LoginActivity;
import com.start.xinkuxue.R;

public class BaseActivity extends AppActivity {

	private InputMethodManager mInputMethodManager;
	
	protected int[] mCountdownimg={
			R.drawable.countdown0,
			R.drawable.countdown1,
			R.drawable.countdown2,
			R.drawable.countdown3,
			R.drawable.countdown4,
			R.drawable.countdown5,
			R.drawable.countdown6,
			R.drawable.countdown7,
			R.drawable.countdown8,
			R.drawable.countdown9,
			R.drawable.countdown10,
			R.drawable.countdown11,
			R.drawable.countdown12};
	
	public BaseContext getAppContext() {
		return (BaseContext)BaseContext.getInstance();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onClick(View v) {
//		if(v.getId()==R.id.head_back){
//			finish();
//		}
	}
	
	@Override
	public void onProcessMessage(Message msg) throws AppException {
		switch(msg.what){
		case ResultCode.NOLOGIN:
			goLogin(null,String.valueOf(msg.obj));
			break;
		default:
			super.onProcessMessage(msg);
			break;
		}
	}
	
	public InputMethodManager getInputMethodManager(){
		if(mInputMethodManager==null){
			mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		}
		return mInputMethodManager;
	}
	
	public void setMainHeadTitle(String title){
//		TextView tvTitle=(TextView)findViewById(R.id.head_title);
//		if(tvTitle!=null){
//			tvTitle.setText(title);
//		}
	}
	
	public void goLogin(Intent startIntent,String message){
		getAppContext().getCacheActivity().setIntent(startIntent);
		Bundle bundle=new Bundle();
		bundle.putString(LoginActivity.BUNLE_MESSAGE, message);
		Intent intent=new Intent(this,LoginActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
	
	public void goLoginResult(int requestCode,String message){
		Bundle bundle=new Bundle();
		bundle.putString(LoginActivity.BUNLE_MESSAGE, message);
		Intent intent=new Intent(this,LoginActivity.class);
		intent.putExtras(bundle);
		startActivityForResult(intent,requestCode);
	}
	
}