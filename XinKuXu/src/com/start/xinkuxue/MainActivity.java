package com.start.xinkuxue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.start.core.BaseActivity;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.learnwords){
			Intent intent=new Intent(this,LearnWordsSwitchActivity.class);
			startActivity(intent);
			finish();
		}else if(v.getId()==R.id.testvocabulary){
			getHandlerContext().makeTextShort("词汇测试,即将上线");
		}else if(v.getId()==R.id.newwords){
			getHandlerContext().makeTextShort("生词本,即将上线");
		}else if(v.getId()==R.id.specialtraining){
			getHandlerContext().makeTextShort("特训营,即将上线");
		}
	}
	
}
