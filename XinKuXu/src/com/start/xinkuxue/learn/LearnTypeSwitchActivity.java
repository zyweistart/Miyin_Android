package com.start.xinkuxue.learn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.start.core.BaseActivity;
import com.start.xinkuxue.R;
import com.start.xinkuxue.strange.StrangeWordTypeSwitchActivity;

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
			bundle.putString(WordListenLookSectionActivity.BUNDLE_START_BUTTON_TITLE, "开始学习");
			Intent intent=new Intent(this,WordListenLookSectionActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}else if(v.getId()==R.id.btn_words123){
			Bundle bundle=new Bundle();
			bundle.putInt(WordListenLookSectionActivity.TESTSWITCHTYPE, 1);
			Intent intent=new Intent(this,WordListenLookSectionActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}else if(v.getId()==R.id.btn_newwords){
			Intent intent = new Intent(this,StrangeWordTypeSwitchActivity.class);
			startActivity(intent);
		}else{
			super.onClick(v);
		}
	}
	
}
