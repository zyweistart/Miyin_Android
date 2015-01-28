package com.start.xinkuxue.learn;

import android.content.Intent;
import android.os.Bundle;

import com.start.service.bean.StrangeWordItem;
import com.start.xinkuxue.BaseContext;
import com.start.xinkuxue.vocabulary.VocabularyTestActivity;

/**
 * 每日测试
 * @author start
 *
 */
public class WordTestActivity extends VocabularyTestActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void joinWords(){
		BaseContext.getDBManager().joinToStrangeWord(String.valueOf(mCurrentWordId), getAppContext().currentUser().getCacheAccount(),StrangeWordItem.CATEGORY_ERROR);
	}
	
	@Override
	public void toDonePage(){
		Bundle bundle=new Bundle();
		bundle.putInt(WordTestGoResultsActivity.RIGHTCOUNT, mRightCount);
		bundle.putInt(WordTestGoResultsActivity.ANSWERCOUNT, mAnswerArray.length);
		Intent intent=new Intent(this,WordTestGoResultsActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
	
}
