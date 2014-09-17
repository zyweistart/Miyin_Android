package com.start.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.start.core.Constant;
import com.start.core.CoreActivity;
import com.start.navigation.R;

/**
 * 首次设置地图数据页面
 * @author start
 *
 */
public class FirstSetMapDataActivity extends CoreActivity implements OnClickListener{

	private static final int REQUEST_SET_DATA=111;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first_set_map_data);
		setCurrentActivityTitle(R.string.activity_title_first_set_mapdata);
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.activity_first_set_map_data_btn_set){
			startActivityForResult(new Intent(this,MapDataListActivity.class), REQUEST_SET_DATA);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==REQUEST_SET_DATA){
			if(!Constant.EMPTYSTR.equals(getAppContext().getCurrentDataNo())){
				startActivity(new Intent(this,MainActivity.class));
				finish();
			}
		}
	}
	
}