package com.start.zmcy;

import android.os.Bundle;

import com.start.core.BaseActivity;

/**
 * 咨询
 */
public class ConsultationActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consultation);
		setMainHeadTitle(getString(R.string.consultation));
 	}
}
