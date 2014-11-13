package com.start.zmcy;

import android.os.Bundle;

import com.start.core.BaseActivity;

/**
 * 关注
 */
public class FollowActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_follow);
		setMainHeadTitle(getString(R.string.follow));
 	}
}
