package com.ancun.zgdxyzb;

import android.os.Bundle;

import com.ancun.core.BaseActivity;

/**
 * 公证须知
 * @author Start
 */
public class NotaryNotifyActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_notary_notify);
		setMainHeadTitle(getString(R.string.notary_notice));
	}
 
}