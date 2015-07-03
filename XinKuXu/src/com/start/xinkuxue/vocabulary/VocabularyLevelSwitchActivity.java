package com.start.xinkuxue.vocabulary;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
		setContentView(R.layout.activity_vocabulary_level_switch);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_levelname1){
			toTest(1,300);
		}else if(v.getId()==R.id.btn_levelname2){
			toTest(301,600);
		}else if(v.getId()==R.id.btn_levelname3){
			toTest(601,900);
		}else if(v.getId()==R.id.btn_levelname4){
			toTest(901,1200);
		}else if(v.getId()==R.id.btn_levelname5){
			toTest(1201,1500);
		}else if(v.getId()==R.id.btn_levelname6){
			toTest(1501,1800);
		}else if(v.getId()==R.id.btn_levelname7){
			toTest(1801,2100);
		}else if(v.getId()==R.id.btn_levelname8){
			toTest(2101,2400);
		}else if(v.getId()==R.id.btn_levelname9){
			toTest(2401,2700);
		}else if(v.getId()==R.id.btn_levelname10){
			toTest(2701,3000);
		}else if(v.getId()==R.id.btn_levelname11){
			toTest(3001,3300);
		}else if(v.getId()==R.id.btn_levelname12){
			toTest(3301,3600);
		}else if(v.getId()==R.id.btn_levelname13){
			toTest(3601,3900);
		}else if(v.getId()==R.id.btn_levelname14){
			toTest(3901,4200);
		}else if(v.getId()==R.id.btn_levelname15){
			toTest(4201,4500);
		}else if(v.getId()==R.id.btn_levelname16){
			toTest(4501,4800);
		}else{
			super.onClick(v);
		}
	}
	
	public void toTest(int start,int end){
		List<String> ids=new ArrayList<String>();
//		for(int i=start;i<=end;i++){
//			ids.add(String.valueOf(i));
//		}
		Random rnRandom=new Random();
		//几选一
		Integer se=(end-start+1)/100;
		//答案总数
		int mAnswerCount=(end-start+1)/se;
		for(int i=0;i<mAnswerCount;i++){
			ids.add(String.valueOf(start+i*se+rnRandom.nextInt(se)));
		}
		Bundle bundle=new Bundle();
		bundle.putStringArray(ListenLookWordActivity.BUNDLE_ANSWER_ARRAY, ids.toArray(new String[ids.size()]));
		Intent intent=new Intent(this,VocabularyLevelTestActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
	
}
