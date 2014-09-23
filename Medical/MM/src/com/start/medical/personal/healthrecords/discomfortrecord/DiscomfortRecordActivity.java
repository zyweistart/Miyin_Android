package com.start.medical.personal.healthrecords.discomfortrecord;

import android.os.Bundle;

import com.start.core.BaseActivity;
import com.start.medical.R;

public class DiscomfortRecordActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_discomfortrecord);
		setMainHeadTitle("不适记录");
	}
	
}
