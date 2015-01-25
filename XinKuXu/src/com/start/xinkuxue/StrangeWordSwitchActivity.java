package com.start.xinkuxue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.start.core.BaseActivity;

/**
 * 单词本
 * @author start
 *
 */
public class StrangeWordSwitchActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_strangeword_switch);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_vocabularytest){
			Intent intent=new Intent(this,VocabularyLevelSwitchActivity.class);
			startActivity(intent);
		}else if(v.getId()==R.id.btn_practicetest){
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
