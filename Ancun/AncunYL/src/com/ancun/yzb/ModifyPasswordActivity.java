package com.ancun.yzb;

import android.os.Bundle;

import com.ancun.core.BaseActivity;

public class ModifyPasswordActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modifypassword);
		setMainHeadTitle(getString(R.string.modifypassword));
	}
}
