package com.start.zmcy;

import android.os.Bundle;

import com.start.core.BaseActivity;

/**
 * 分享绑定
 */
public class BindSharedActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bindshared);
		setMainHeadTitle(getString(R.string.bindshared));
 	}
}
