package com.start.zmcy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.start.core.BaseActivity;

/**
 * 新闻搜索
 */
public class NewsSearchActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_search);
 	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btnCancel){
			finish();
		}else if(v.getId()==R.id.ivSearch){
			if(getInputMethodManager().isActive()){
				getInputMethodManager().hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
			goSearch();
		}
	}
	
	public void goSearch(){
		Intent intent=new Intent(this,NewsListActivity.class);
		startActivity(intent);
	}
	
}