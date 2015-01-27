package com.start.xinkuxue.learn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.start.core.BaseActivity;
import com.start.xinkuxue.R;

/**
 * 单词学习选择界面
 * @author start
 *
 */
public class LearnTypeSwitchActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_learn_words_switch);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_listenlooklearnwords){
			Bundle bundle=new Bundle();
			bundle.putInt(WordSwitchSectionActivity.TESTSWITCHTYPE, 0);
			Intent intent=new Intent(this,WordSwitchSectionActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}else if(v.getId()==R.id.btn_words123){
			Bundle bundle=new Bundle();
			bundle.putInt(WordSwitchSectionActivity.TESTSWITCHTYPE, 2);
			Intent intent=new Intent(this,WordSwitchSectionActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}else if(v.getId()==R.id.btn_testeveryday){
			Bundle bundle=new Bundle();
			bundle.putInt(WordSwitchSectionActivity.TESTSWITCHTYPE, 1);
			Intent intent=new Intent(this,WordSwitchSectionActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}else{
			super.onClick(v);
		}
	}
	
}
