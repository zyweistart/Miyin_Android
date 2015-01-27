package com.start.xinkuxue.strange;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.start.core.BaseActivity;
import com.start.service.bean.StrangeWordItem;
import com.start.xinkuxue.R;

/**
 * 生词本选择
 * @author start
 *
 */
public class StrangeWordTypeSwitchActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_strange_word_type_switch);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_wordcategory){
			Bundle bundle=new Bundle();
			bundle.putString(StrangeWordsActivity.BUNDLE_TYPE, StrangeWordItem.CATEGORY_WORDS);
			Intent intent=new Intent(this,StrangeWordsActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}else if(v.getId()==R.id.btn_errorwordcategory){
			Bundle bundle=new Bundle();
			bundle.putString(StrangeWordsActivity.BUNDLE_TYPE, StrangeWordItem.CATEGORY_ERROR);
			Intent intent=new Intent(this,StrangeWordsActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}else{
			super.onClick(v);
		}
	}
	
}
