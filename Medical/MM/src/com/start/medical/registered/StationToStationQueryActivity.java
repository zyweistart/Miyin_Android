package com.start.medical.registered;

import android.os.Bundle;

import com.start.core.BaseActivity;
import com.start.medical.R;

/**
 * 叫号查询
 * @author start
 *
 */
public class StationToStationQueryActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_station_to_station_query);
		setMainHeadTitle("叫号查询");
	}
	
}
