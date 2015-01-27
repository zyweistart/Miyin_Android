package com.start.xinkuxue.vocabulary;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.xinkuxue.R;

/**
 * 词汇测试界面
 * 
 * @author start
 *
 */
public class VocabularyTestGoResultsActivity extends BaseActivity{
	
	public static final String ANSWERCOUNT="ANSWERCOUNT";
	public static final String RIGHTCOUNT="RIGHTCOUNT";
	
	private int mAnswerCount,mRightCount;
	private RelativeLayout frame_bg;
	private ImageView ivlevel;
	private TextView txtcount;
	private TextView txttitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_words_page_done);
		frame_bg=(RelativeLayout)findViewById(R.id.frame_bg);
		ivlevel=(ImageView)findViewById(R.id.ivlevel);
		txtcount=(TextView)findViewById(R.id.txtcount);
		txttitle=(TextView)findViewById(R.id.txttitle);
		
		Bundle bundle=getIntent().getExtras();
		if(bundle==null){
			finish();
		}else{
			mAnswerCount=bundle.getInt(ANSWERCOUNT);
			mRightCount=bundle.getInt(RIGHTCOUNT);
			int result=100/mAnswerCount*mRightCount;
			if(result==100){
				frame_bg.setBackgroundResource(R.drawable.bm_level_3);
				ivlevel.setImageResource(R.drawable.ic_level_icon_3);
				txttitle.setText("，全中有奖！");
			}else if(result>=80){
				frame_bg.setBackgroundResource(R.drawable.bm_level_2);
				ivlevel.setImageResource(R.drawable.ic_level_icon_2);
				txttitle.setText("，顺利完成本日任务！");
			}else{
				frame_bg.setBackgroundResource(R.drawable.bm_level_1);
				ivlevel.setImageResource(R.drawable.ic_level_icon_1);
				txttitle.setText("，还需好好复习！");
			}
			txtcount.setText(String.valueOf(result));
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_learnword){
			finish();
		}else if(v.getId()==R.id.btn_waittry){
			finish();
		}else{
			super.onClick(v);
		}
	}
	
}