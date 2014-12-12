package com.start.zmcy;

import android.os.Bundle;

/**
 * 收藏
 */
public class CollectActivity extends BaseNewsActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		categoryId="11";
		super.onCreate(savedInstanceState);
		setMainHeadTitle(getString(R.string.collect));
		mRefreshListServer.initLoad();
 	}
	
}
