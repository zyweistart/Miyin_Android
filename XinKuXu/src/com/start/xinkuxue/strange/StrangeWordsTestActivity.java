package com.start.xinkuxue.strange;

import com.start.xinkuxue.WordsTestPageActivity;

import android.content.Intent;
import android.os.Bundle;

/**
 * 生词本测试
 * @author zhenyao
 *
 */
public class StrangeWordsTestActivity extends WordsTestPageActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void toDonePage(){
		Bundle bundle=new Bundle();
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
