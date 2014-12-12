package com.start.zmcy;

import android.os.Bundle;

/**
 * 关注
 */
public class FollowActivity extends BaseNewsActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		categoryId="12";
		super.onCreate(savedInstanceState);
		setMainHeadTitle(getString(R.string.follow));
		mRefreshListServer.initLoad();
 	}
	
}
