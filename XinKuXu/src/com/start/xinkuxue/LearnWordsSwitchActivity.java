package com.start.xinkuxue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.start.core.BaseActivity;

/**
 * 单词学习选择界面
 * @author start
 *
 */
public class LearnWordsSwitchActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_learn_words_switch);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_listenlooklearnwords){
			Bundle bundle=new Bundle();
			bundle.putInt(LearnWordsSwitchTestActivity.TESTSWITCHTYPE, 0);
			Intent intent=new Intent(this,LearnWordsSwitchTestActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}else if(v.getId()==R.id.btn_words123){
			getHandlerContext().makeTextLong("暂无注册功能");
		}else if(v.getId()==R.id.btn_testeveryday){
			Bundle bundle=new Bundle();
			bundle.putInt(LearnWordsSwitchTestActivity.TESTSWITCHTYPE, 1);
			Intent intent=new Intent(this,LearnWordsSwitchTestActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}else{
			super.onClick(v);
		}
	}
	
}
