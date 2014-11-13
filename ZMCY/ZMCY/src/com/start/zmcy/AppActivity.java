package com.start.zmcy;

import android.os.Bundle;

import com.start.core.BaseActivity;

/**
 * 应用 
 */
public class AppActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app);
		setMainHeadTitle(getString(R.string.app));
 	}
}
