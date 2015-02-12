package com.start.xinkuxue.strange;

import android.content.Intent;
import android.os.Bundle;

import com.start.xinkuxue.vocabulary.VocabularyTestActivity;

/**
 * 生词本测试
 * @author zhenyao
 *
 */
public class StrangeWordsTestActivity extends VocabularyTestActivity{
	
	public static final String BUNDLE_TYPE="BUNDLE_TYPE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		isCountdowning=false;
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void toDonePage(){
		Bundle bundle=new Bundle();
		bundle.putString(StrangeWordsTestGoResultsActivity.BUNDLE_TYPE, mBundle.getString(BUNDLE_TYPE));
		bundle.putInt(StrangeWordsTestGoResultsActivity.RIGHTCOUNT, mRightCount);
		bundle.putInt(StrangeWordsTestGoResultsActivity.ANSWERCOUNT, mAnswerArray.length);
		Intent intent=new Intent(this,StrangeWordsTestGoResultsActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
	
	@Override
	public void joinWords(){
		
	}
	
}
