package com.start.medical.personal.healthrecords.medicalhistory;

import android.os.Bundle;

import com.start.core.BaseActivity;
import com.start.medical.R;

public class MedicalHistoryActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medicalhistory);
		setMainHeadTitle("病史");
	}
	
}
