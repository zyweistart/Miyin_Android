package com.start.medical.report;

import android.os.Bundle;

import com.start.core.BaseActivity;
import com.start.medical.R;

/**
 * 取报告单
 * @author start
 *
 */
public class TakeReportActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_report);
		setMainHeadTitle(getString(R.string.mainfunctiontxt3));
	}
	
}
