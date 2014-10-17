package com.start.medical.registered.appointment;

import android.os.Bundle;

import com.start.core.BaseActivity;
import com.start.medical.R;

/**
 * 预约详情
 * @author start
 * 
 */
public class AppointmentOfExpertsDetailActivity extends BaseActivity {

	public static final String RECORDNO="recordno";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_of_experts);
		setMainHeadTitle("预约详情");
	}

}
