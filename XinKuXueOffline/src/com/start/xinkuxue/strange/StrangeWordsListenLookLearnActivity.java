package com.start.xinkuxue.strange;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.start.xinkuxue.R;
import com.start.xinkuxue.learn.ListenLookWordActivity;

/**
 * 生词本边读边听边写
 * @author zhenyao
 *
 */
public class StrangeWordsListenLookLearnActivity extends ListenLookWordActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.immediatetest){
			Intent intent=new Intent(this,StrangeWordsTestTypeSwitchActivity.class);
			intent.putExtras(mBundle);
			startActivity(intent);
			finish();
		}else{
			super.onClick(v);
		}
	}
	
}
