package com.start.medical.personal.healthrecords.medicationreminder;

import android.os.Bundle;

import com.start.core.BaseActivity;
import com.start.medical.R;

public class MedicationReminderActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medicationreminder);
		setMainHeadTitle("用药提醒");
	}
	
}
