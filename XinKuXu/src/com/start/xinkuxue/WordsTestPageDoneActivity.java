package com.start.xinkuxue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.start.core.BaseActivity;

/**
 * 测试单词完成页面
 * @author start
 *
 */
public class WordsTestPageDoneActivity extends BaseActivity{
	
	public static final String ANSWERCOUNT="ANSWERCOUNT";
	public static final String RIGHTCOUNT="RIGHTCOUNT";
	
	private int mAnswerCount,mRightCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_words_page_done);
		TextView tv=(TextView)findViewById(R.id.tipinfo);
		Bundle bundle=getIntent().getExtras();
		if(bundle==null){
			finish();
		}else{
			mAnswerCount=bundle.getInt(ANSWERCOUNT);
			mRightCount=bundle.getInt(RIGHTCOUNT);
			tv.setText("答案结束共："+mAnswerCount+"道题，答对了："+mRightCount+"道题");
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_login){
			Intent intent=new Intent(this,MainActivity.class);
			startActivity(intent);
			finish();
		}else if(v.getId()==R.id.btn_register){
			getHandlerContext().makeTextLong("暂无注册功能");
		}else{
			super.onClick(v);
		}
	}
	
}
