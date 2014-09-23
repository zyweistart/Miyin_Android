package com.start.medical.personal.healthrecords.inspect;

import android.os.Bundle;

import com.start.core.BaseActivity;
import com.start.medical.R;

public class InspectActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inspect);
		setMainHeadTitle("检查");
	}
	
}
