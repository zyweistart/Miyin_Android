package com.start.core;

import com.start.zmcy.R;

import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

public class BaseFragmentActivity extends FragmentActivity {
	
	public void setMainHeadTitle(String title){
		TextView tvTitle=(TextView)findViewById(R.id.head_title);
		if(tvTitle!=null){
			tvTitle.setText(title);
		}
	}
	
}
