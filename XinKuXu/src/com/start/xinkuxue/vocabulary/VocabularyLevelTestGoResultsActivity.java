package com.start.xinkuxue.vocabulary;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import start.utils.TimeUtils;
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

	public static final String BUNDLE_CURRENT_LEVEL = "BUNDLE_CURRENT_LEVEL";
	public static final String BUNDLE_ANSWER_RIGHTCOUNT = "BUNDLE_ANSWER_RIGHTCOUNT";
	public static final String BUNDLE_ANSWER_COUNT = "BUNDLE_ANSWER_COUNT";
	
	private ImageView ivlevel;
	private TextView txtlevel,txtcount1,txtcount2,txtcount3;
	private TextView txttip2,txttime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vocabulary_level_go_results);
		ivlevel=(ImageView)findViewById(R.id.ivlevel);
		txtlevel=(TextView)findViewById(R.id.txtlevel);
		txtcount1=(TextView)findViewById(R.id.txtcount1);
		txtcount2=(TextView)findViewById(R.id.txtcount2);
		txtcount3=(TextView)findViewById(R.id.txtcount3);
		txttip2=(TextView)findViewById(R.id.txttip2);
		txttime=(TextView)findViewById(R.id.txttime);
		txttime.setText("答题时间："+TimeUtils.getSysTime());
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			int rightCount=bundle.getInt(BUNDLE_ANSWER_RIGHTCOUNT);
			int answerCount=bundle.getInt(BUNDLE_ANSWER_COUNT);
			String level=bundle.getString(BUNDLE_CURRENT_LEVEL);
			float result=((float)rightCount)/((float)answerCount)*100;
			if(result>=80){
				txttip2.setText("建议测试下一级别");
				ivlevel.setImageResource(R.drawable.level1);
			}else if(result>=60){
				txttip2.setText("建议再测试一次");
				ivlevel.setImageResource(R.drawable.level2);
			}else{
				txttip2.setText("建议降低一级测试");
				ivlevel.setImageResource(R.drawable.level3);
			}
			txtlevel.setText("你测试的级别是单词"+level+"级");
			DecimalFormat df = (DecimalFormat)NumberFormat.getInstance();  
	        df.setMaximumFractionDigits(1);  
	        df.setRoundingMode(RoundingMode.HALF_UP);  
			txtcount1.setText(String.valueOf(answerCount));
			txtcount2.setText(String.valueOf(rightCount));
			txtcount3.setText(df.format(result)+"%");
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
