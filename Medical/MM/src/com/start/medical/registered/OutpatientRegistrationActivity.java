package com.start.medical.registered;

import android.os.Bundle;

import com.start.core.BaseActivity;
import com.start.medical.R;

/**
 * 门诊挂号
 * @author start
 *
 */
public class OutpatientRegistrationActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_outpatient_registration);
		setMainHeadTitle("门诊挂号");
	}
	
}
