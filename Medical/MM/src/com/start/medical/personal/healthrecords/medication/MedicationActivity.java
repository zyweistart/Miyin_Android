package com.start.medical.personal.healthrecords.medication;

import android.os.Bundle;

import com.start.core.BaseActivity;
import com.start.medical.R;

public class MedicationActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medication);
		setMainHeadTitle("服药");
	}
	
}
