package com.start.xinkuxue.vocabulary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.start.core.BaseActivity;
import com.start.xinkuxue.R;
import com.start.xinkuxue.learn.WordTestSectionActivity;

/**
 * 词汇测试界面
 * @author start
 *
 */
public class VocabularyTypeSwitchActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vocabulary_type_switch);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_vocabularytest){
			Intent intent=new Intent(this,VocabularyLevelSwitchActivity.class);
			startActivity(intent);
		}else if(v.getId()==R.id.btn_practicetest){
			Intent intent=new Intent(this,VocabularyTestSectionActivity.class);
			startActivity(intent);
		}else if(v.getId()==R.id.btn_testeveryday){
			Intent intent=new Intent(this,WordTestSectionActivity.class);
			startActivity(intent);
		}else{
			super.onClick(v);
		}
	}
	
}
