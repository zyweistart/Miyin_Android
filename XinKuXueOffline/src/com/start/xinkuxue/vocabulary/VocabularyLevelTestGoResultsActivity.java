package com.start.xinkuxue.vocabulary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.xinkuxue.R;

/**
 * 词汇测试界面
 * 
 * @author start
 *
 */
public class VocabularyLevelTestGoResultsActivity extends BaseActivity {

	public static final String BUNDLE_ANSWER_RIGHTCOUNT = "BUNDLE_ANSWER_RIGHTCOUNT";
	public static final String BUNDLE_ANSWER_COUNT = "BUNDLE_ANSWER_COUNT";
	
	private ImageView ivlevel;
	private TextView txtcount,txttip1,txttip2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vocabulary_level_go_results);
		ivlevel=(ImageView)findViewById(R.id.ivlevel);
		txtcount=(TextView)findViewById(R.id.txtcount);
		txttip1=(TextView)findViewById(R.id.txttip1);
		txttip2=(TextView)findViewById(R.id.txttip2);
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			int rightCount=bundle.getInt(BUNDLE_ANSWER_RIGHTCOUNT);
			int answerCount=bundle.getInt(BUNDLE_ANSWER_COUNT);
			int result=100/answerCount*rightCount;
			if(result>=80){
				txttip1.setText("，高于本级水平");
				txttip2.setText("建议测试下一级别");
				ivlevel.setImageResource(R.drawable.level1);
			}else if(result>=60){
				txttip1.setText("，达标水平");
				txttip2.setText("建议再测试一次");
				ivlevel.setImageResource(R.drawable.level2);
			}else{
				txttip1.setText("，低于本级水平");
				txttip2.setText("建议降低一级测试");
				ivlevel.setImageResource(R.drawable.level3);
			}
			txtcount.setText(result+"分");
		}else{
			finish();
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_gohome){
			finish();
		}else if(v.getId()==R.id.btn_anothertest){
			Intent intent=new Intent(this,VocabularyLevelSwitchActivity.class);
			startActivity(intent);
			finish();
		}
	}
	
}
