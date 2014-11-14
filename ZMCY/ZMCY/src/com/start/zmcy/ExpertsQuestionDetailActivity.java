package com.start.zmcy;

import android.os.Bundle;

import com.start.core.BaseActivity;

/**
 * 问题详细
 */
public class ExpertsQuestionDetailActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutus);
		setMainHeadTitle(getString(R.string.aboutus));
 	}
}
