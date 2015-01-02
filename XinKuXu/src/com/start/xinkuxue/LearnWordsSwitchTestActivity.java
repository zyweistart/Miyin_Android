package com.start.xinkuxue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.start.core.BaseActivity;

/**
 * 单词测试选择界面
 * @author start
 *
 */
public class LearnWordsSwitchTestActivity extends BaseActivity{
	
	public static final String TESTSWITCHTYPE="TESTSWITCHTYPE";
	
	private LinearLayout frame_test_start1,frame_test_start2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_learn_words_switch_test);
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			frame_test_start1=(LinearLayout)findViewById(R.id.frame_test_start1);
			frame_test_start2=(LinearLayout)findViewById(R.id.frame_test_start2);
			if(bundle.getInt(TESTSWITCHTYPE)==0){
				frame_test_start1.setVisibility(View.VISIBLE);
				frame_test_start2.setVisibility(View.GONE);
			}else{
				frame_test_start1.setVisibility(View.GONE);
				frame_test_start2.setVisibility(View.VISIBLE);
			}
		}else{
			finish();
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_test_start1){
			Intent intent=new Intent(this,LearnWordsListenLookActivity.class);
			startActivity(intent);
			finish();
		}else if(v.getId()==R.id.btn_test_start2){
			Intent intent=new Intent(this,TestWordsPageActivity.class);
			startActivity(intent);
			finish();
		}else{
			super.onClick(v);
		}
	}
	
}
