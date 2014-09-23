package com.start.medical.more;

import android.os.Bundle;

import com.start.core.BaseActivity;
import com.start.medical.R;

/**
 * 更多
 * @author start
 *
 */
public class MoreActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		setMainHeadTitle(getString(R.string.more));
	}
	
}
