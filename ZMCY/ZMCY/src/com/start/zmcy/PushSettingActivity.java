package com.start.zmcy;

import android.os.Bundle;

import com.start.core.BaseActivity;

/**
 * 推送设置
 */
public class PushSettingActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pushsettting);
		setMainHeadTitle(getString(R.string.pushsetting));
 	}
}
