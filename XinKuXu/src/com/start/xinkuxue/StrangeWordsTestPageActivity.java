package com.start.xinkuxue;

import android.content.Intent;
import android.os.Bundle;

/**
 * 生词本测试
 * @author zhenyao
 *
 */
public class StrangeWordsTestPageActivity extends WordsTestPageActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void toDonePage(){
		Bundle bundle=new Bundle();
//		bundle.putInt(TestWordsPageDoneActivity.RIGHTCOUNT, mRightCount);
//		bundle.putInt(TestWordsPageDoneActivity.ANSWERCOUNT, mAnswerArray.length);
		Intent intent=new Intent(this,LoginActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
	
	@Override
	public void joinWords(){
		
	}
	
}
