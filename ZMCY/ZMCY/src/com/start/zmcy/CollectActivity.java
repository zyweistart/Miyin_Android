package com.start.zmcy;

import android.os.Bundle;

import com.start.core.BaseActivity;

/**
 * 收藏
 */
public class CollectActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collect);
		setMainHeadTitle(getString(R.string.collect));
 	}
}
