package com.start.xinkuxue.training;

import android.os.Bundle;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.xinkuxue.R;

/**
 * 流程
 * @author zhenyao
 *
 */
public class ProccessActivity extends BaseActivity{

	private TextView tvTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_proccess);
		tvTitle=(TextView)findViewById(R.id.tvTitle);
		tvTitle.setText("流程");
	}
	
}
