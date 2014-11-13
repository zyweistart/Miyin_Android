package com.start.zmcy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.start.core.BaseActivity;

/**
 * 活动
 */
public class ActivitiesActivity extends BaseActivity {

	private Button main_head_1;
	private Button main_head_2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activities);
		setMainHeadTitle(getString(R.string.activities));

		main_head_1 = (Button) findViewById(R.id.main_head_1);
		main_head_1.setText(getString(R.string.activities_mall));
		main_head_1.setVisibility(View.VISIBLE);
		main_head_2 = (Button) findViewById(R.id.main_head_2);
		main_head_2.setText(getString(R.string.activities_exhibition));
		main_head_2.setVisibility(View.VISIBLE);
		setHeadButtonEnabled(0);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.main_head_1) {
			setHeadButtonEnabled(0);
		} else if (v.getId() == R.id.main_head_2) {
			setHeadButtonEnabled(1);
		} else {
			super.onClick(v);
		}
	}

	public void setHeadButtonEnabled(int index) {
		main_head_1.setEnabled(index == 0 ? false : true);
		main_head_2.setEnabled(index == 1 ? false : true);
	}
	
}
