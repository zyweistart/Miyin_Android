package com.start.xinkuxueoffline.learn;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.start.xinkuxueoffline.vocabulary.VocabularyTestSectionActivity;

/**
 * 每日测试区间选择界面
 * @author start
 *
 */
public class WordTestSectionActivity extends VocabularyTestSectionActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tip2.setVisibility(View.GONE);
		frame_et_section.setVisibility(View.GONE);
	}
	
	@Override
	public void goTestPage(List<String> indexs,List<String> ids){
		Bundle bundle=new Bundle();
		bundle.putStringArray(WordTestActivity.BUNDLE_WORDS, indexs.toArray(new String[indexs.size()]));
		bundle.putStringArray(WordTestActivity.BUNDLE_ANSWER_ARRAY, ids.toArray(new String[ids.size()]));
		Intent intent=new Intent(this,WordTestActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
	
}
