package com.start.medical.more;

import android.os.Bundle;

import com.start.core.BaseActivity;
import com.start.medical.R;

/**
 * 关于我们
 * @author start
 *
 */
public class AboutUsActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutus);
		setMainHeadTitle(getString(R.string.aboutus));
	}
	
}
