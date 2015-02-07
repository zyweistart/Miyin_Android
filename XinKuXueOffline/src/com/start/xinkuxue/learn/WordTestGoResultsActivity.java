package com.start.xinkuxue.learn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.start.xinkuxue.R;
import com.start.xinkuxue.vocabulary.VocabularyTestGoResultsActivity;

/**
 * 每日测试结果页面
 * @author start
 *
 */
public class WordTestGoResultsActivity extends VocabularyTestGoResultsActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_learnword){
			Bundle bundle=new Bundle();
			bundle.putInt(WordListenLookSectionActivity.TESTSWITCHTYPE, 0);
			Intent intent=new Intent(this,WordListenLookSectionActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
		}else if(v.getId()==R.id.btn_waittry){
			finish();
		}else{
			super.onClick(v);
		}
	}
	
}
