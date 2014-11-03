package com.ancun.yzb;

import android.os.Bundle;

import com.ancun.core.BaseActivity;

/**
 * 公证列表
 * @author Start
 */
public class NotaryListActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_notary_list);
		setMainHeadTitle(getString(R.string.notary_list));
	}
	
	 
 
}