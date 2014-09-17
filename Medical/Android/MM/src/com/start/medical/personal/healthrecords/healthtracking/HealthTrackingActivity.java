package com.start.medical.personal.healthrecords.healthtracking;

import android.os.Bundle;

import com.start.core.BaseActivity;
import com.start.medical.R;

public class HealthTrackingActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_healthtracking);
		setMainHeadTitle("健康跟踪");
	}
	
}
