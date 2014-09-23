package com.start.medical.personal.report;

import android.os.Bundle;

import com.start.core.BaseActivity;
import com.start.medical.R;

/**
 * 报告单详情
 * @author start
 *
 */
public class MyReportDetailActivity extends BaseActivity {

	public static final String RECORDNO="recordno";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myreport_detail);
		setMainHeadTitle("报告单详情");
	}
	
}