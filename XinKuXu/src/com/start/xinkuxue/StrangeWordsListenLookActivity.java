package com.start.xinkuxue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 生词本边读边听边写
 * @author zhenyao
 *
 */
public class StrangeWordsListenLookActivity extends ListenLookLearnWordActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.immediatetest){
			Intent intent=new Intent(this,StrangeWordsSwitchTestActivity.class);
			intent.putExtras(mBundle);
			startActivity(intent);
			finish();
		}else{
			super.onClick(v);
		}
	}
	
}
