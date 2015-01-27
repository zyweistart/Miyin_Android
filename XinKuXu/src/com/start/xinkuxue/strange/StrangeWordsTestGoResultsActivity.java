package com.start.xinkuxue.strange;

import android.os.Bundle;
import android.view.View;

import com.start.xinkuxue.R;
import com.start.xinkuxue.vocabulary.VocabularyTestGoResultsActivity;

/**
 * 生词本测试完成界面
 * @author zhenyao
 *
 */
public class StrangeWordsTestGoResultsActivity extends VocabularyTestGoResultsActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		btn_learnword.setText(R.string.mygoreview);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_learnword){
			getHandlerContext().makeTextLong(getString(R.string.mygoreview));
		}else if(v.getId()==R.id.btn_waittry){
			finish();
		}else{
			super.onClick(v);
		}
	}
	
}
