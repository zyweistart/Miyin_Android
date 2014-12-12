package com.start.zmcy;

import android.os.Bundle;

/**
 * 新闻列表
 */
public class NewsListActivity extends BaseNewsActivity{
	
	public static final String BUNDLE_SEARCH="BUNDLE_SEARCH";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		categoryId="13";
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			keyword=bundle.getString(BUNDLE_SEARCH);
		}
		super.onCreate(savedInstanceState);
		setMainHeadTitle(getString(R.string.news));

		mRefreshListServer.getCurrentListView().startLoadMore();
 	}
	
}
