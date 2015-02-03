package com.start.xinkuxue.training;

import android.os.Bundle;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.xinkuxue.R;

/**
 * 公告栏
 * @author zhenyao
 *
 */
public class NoticeActivity extends BaseActivity{

	private TextView tvTitle,tvNotice1,tvNotice2,tvNotice3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice);
		tvTitle=(TextView)findViewById(R.id.tvTitle);
		tvTitle.setText("公告栏");
		tvNotice1=(TextView)findViewById(R.id.tvnotice1);
		tvNotice2=(TextView)findViewById(R.id.tvnotice2);
		tvNotice3=(TextView)findViewById(R.id.tvnotice3);
		setEnabledByIndex(0);
	}
	
	public void setEnabledByIndex(int index){
		tvNotice1.setEnabled(index==0?false:true);
		tvNotice2.setEnabled(index==1?false:true);
		tvNotice3.setEnabled(index==2?false:true);
	}
	
}
