package com.start.xinkuxue.vocabulary;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.start.core.BaseActivity;
import com.start.xinkuxue.R;
import com.start.xinkuxue.learn.ListenLookWordActivity;

/**
 * 词汇测试等级选择界面
 * @author start
 *
 */
public class VocabularyLevelSwitchActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vocabulary_switch_difficulty);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_levelname1){
			//1-500
			toTest(1,500);
		}else if(v.getId()==R.id.btn_levelname2){
			//501-1000
			toTest(501,1000);
		}else if(v.getId()==R.id.btn_levelname3){
			//1001-1500
			toTest(1001,1500);
		}else if(v.getId()==R.id.btn_levelname4){
			//1501-2000
			toTest(1501,2000);
		}else if(v.getId()==R.id.btn_levelname5){
			//2001-2500
			toTest(2001,2500);
		}else if(v.getId()==R.id.btn_levelname6){
			//2501-3000
			toTest(2501,3000);
		}else if(v.getId()==R.id.btn_levelname7){
			//3001-3500
			toTest(3001,3500);
		}else if(v.getId()==R.id.btn_levelname8){
			//3501-4000
			toTest(3501,4000);
		}else if(v.getId()==R.id.btn_levelname9){
			//4001--4500
			toTest(4001,4500);
		}else if(v.getId()==R.id.btn_levelname10){
			//4501--5000
			toTest(4501,5000);
		}else{
			super.onClick(v);
		}
	}
	
	public void toTest(int start,int end){
		List<String> ids=new ArrayList<String>();
		for(int i=start;i<=end;i++){
			ids.add(String.valueOf(i));
		}
		Bundle bundle=new Bundle();
		bundle.putStringArray(ListenLookWordActivity.BUNDLE_ANSWER_ARRAY, ids.toArray(new String[ids.size()]));
		Intent intent=new Intent(this,VocabularyLevelTestActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
	
}
