package com.start.xinkuxue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.start.core.BaseActivity;

/**
 * 测试单词完成页面
 * @author start
 *
 */
public class TestWordsPageDoneActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_words_page_done);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_login){
			Intent intent=new Intent(this,MainActivity.class);
			startActivity(intent);
			finish();
		}else if(v.getId()==R.id.btn_register){
			getHandlerContext().makeTextLong("暂无注册功能");
		}else{
			super.onClick(v);
		}
	}
	
}
