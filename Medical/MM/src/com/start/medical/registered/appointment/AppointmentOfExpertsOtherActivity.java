package com.start.medical.registered.appointment;

import android.os.Bundle;

import com.start.core.BaseActivity;
import com.start.medical.R;

/**
 * 代他人预约
 * @author start
 * 
 */
public class AppointmentOfExpertsOtherActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_of_experts);
		setMainHeadTitle("代他人预约");
	}

}
