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
		setContentView(R.layout.activity_learn_type_switch);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_listenlooklearnwords){
			Bundle bundle=new Bundle();
			bundle.putInt(WordListenLookSectionActivity.TESTSWITCHTYPE, 0);
			Intent intent=new Intent(this,WordListenLookSectionActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}else if(v.getId()==R.id.btn_words123){
			Bundle bundle=new Bundle();
			bundle.putInt(WordListenLookSectionActivity.TESTSWITCHTYPE, 1);
			Intent intent=new Intent(this,WordListenLookSectionActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}else if(v.getId()==R.id.btn_testeveryday){
			Intent intent=new Intent(this,WordTestSectionActivity.class);
			startActivity(intent);
		}else{
			super.onClick(v);
		}
	}
	
}
