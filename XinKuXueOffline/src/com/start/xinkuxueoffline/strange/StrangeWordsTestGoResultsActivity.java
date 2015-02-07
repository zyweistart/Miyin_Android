package com.start.xinkuxueoffline.strange;

import android.os.Bundle;
import android.view.View;

import com.start.xinkuxueoffline.R;
import com.start.xinkuxueoffline.vocabulary.VocabularyTestGoResultsActivity;

/**
 * 生词本测试完成界面
 * @author zhenyao
 *
 */
public class StrangeWordsTestGoResultsActivity extends VocabularyTestGoResultsActivity{
	
	public static final String BUNDLE_TYPE="BUNDLE_TYPE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		btn_learnword.setText(R.string.mygoreview);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_learnword){
			String type=mBundle.getString(BUNDLE_TYPE);
			getHandlerContext().makeTextLong(getString(R.string.mygoreview)+type);
		}else if(v.getId()==R.id.btn_waittry){
			finish();
		}else{
			super.onClick(v);
		}
	}
	
}
