package com.start.medical.personal.registered;

import android.os.Bundle;

import com.start.core.BaseActivity;
import com.start.medical.R;

/**
 * 挂号详情
 * @author start
 *
 */
public class MyRegisteredDetailActivity extends BaseActivity {
	
	public static final String RECORDNO="recordno";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myregistered_detail);
		setMainHeadTitle("挂号详情");
	}
	
}