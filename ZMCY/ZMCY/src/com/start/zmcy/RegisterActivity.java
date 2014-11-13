package com.start.zmcy;

import android.os.Bundle;

import com.start.core.BaseActivity;


public class RegisterActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		setMainHeadTitle(getString(R.string.registernewuser));
 	}
}
