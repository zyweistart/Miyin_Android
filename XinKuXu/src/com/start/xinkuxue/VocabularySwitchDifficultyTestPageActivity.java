package com.start.xinkuxue;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.start.core.BaseActivity;

/**
 * 词汇测试界面
 * @author start
 *
 */
public class VocabularySwitchDifficultyTestPageActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vocabulary_switch_difficulty);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_levelname1){
			//0-500
		}else if(v.getId()==R.id.btn_levelname2){
			//501-1000
		}else if(v.getId()==R.id.btn_levelname3){
			//1001-1500
		}else if(v.getId()==R.id.btn_levelname4){
			//1501-2000
		}else if(v.getId()==R.id.btn_levelname5){
			//2001-2500
		}else if(v.getId()==R.id.btn_levelname6){
			//2501-3000
		}else if(v.getId()==R.id.btn_levelname7){
			//3001-3500
		}else if(v.getId()==R.id.btn_levelname8){
			//3501-4000
		}else if(v.getId()==R.id.btn_levelname9){
			//4001--4500
		}else if(v.getId()==R.id.btn_levelname10){
			//4501--5000
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
		bundle.putStringArray(LearnWordsListenLookActivity.BUNDLE_ANSWER_ARRAY, ids.toArray(new String[ids.size()]));
		Intent intent=new Intent(this,LearnWordsListenLookActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
	
}
