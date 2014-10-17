package com.start.medical.registered.appointment;

import android.os.Bundle;

import com.start.core.BaseActivity;
import com.start.medical.R;

/**
 * 代他人预约信息添加
 * @author start
 * 
 */
public class AppointmentOfExpertsOtherAddInfoActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_of_experts);
		setMainHeadTitle("代他人预约信息添加");
	}

}
