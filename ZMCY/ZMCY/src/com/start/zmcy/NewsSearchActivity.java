package com.start.zmcy;

import start.widget.CustomEditText;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.start.core.BaseActivity;

/**
 * 新闻搜索
 */
public class NewsSearchActivity extends BaseActivity{
	
	private CustomEditText et_search_content;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_search);
		et_search_content=(CustomEditText)findViewById(R.id.et_search_content);
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
		}else if(v.getId()==R.id.searchkeysbutton1
				||v.getId()==R.id.searchkeysbutton2
				||v.getId()==R.id.searchkeysbutton3
				||v.getId()==R.id.searchkeysbutton4
				||v.getId()==R.id.searchkeysbutton5
				||v.getId()==R.id.searchkeysbutton6
				||v.getId()==R.id.searchkeysbutton7
				||v.getId()==R.id.searchkeysbutton8
				||v.getId()==R.id.searchkeysbutton9
				||v.getId()==R.id.searchkeysbutton10){
			et_search_content.setText(String.valueOf(((TextView)v).getText()));
			goSearch();
		}
	}
	
	public void goSearch(){
		String content=String.valueOf(et_search_content.getText());
		if(TextUtils.isEmpty(content)){
			getHandlerContext().makeTextLong(getString(R.string.searchkeyshint));
			return;
		}
		Bundle bundle=new Bundle();
		bundle.putString(NewsListActivity.BUNDLE_SEARCH, content);
		Intent intent=new Intent(this,NewsListActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
	
}