package com.start.xinkuxue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.start.core.BaseActivity;

/**
 * 生词本测试完成界面
 * @author zhenyao
 *
 */
public class StrangeWordsTestPageDoneActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_strange_words_test_done);
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
