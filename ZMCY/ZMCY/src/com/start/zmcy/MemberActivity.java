package com.start.zmcy;

import android.os.Bundle;

import com.start.core.BaseActivity;

/**
 * 成员
 */
public class MemberActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member);
		setMainHeadTitle(getString(R.string.member));
 	}
}
