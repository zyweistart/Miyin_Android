package com.start.medical.navigation;

import android.os.Bundle;

import com.start.core.BaseActivity;
import com.start.medical.R;

/**
 * 医院导航
 * @author start
 *
 */
public class NavigationActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);
		setMainHeadTitle(getString(R.string.mainfunctiontxt5));
	}
	
}
