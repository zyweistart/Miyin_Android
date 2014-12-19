package com.start.zmcy;

import android.os.Bundle;
import android.widget.ImageView;

import com.start.core.BaseActivity;

public class AdvertisingActivity extends BaseActivity{
	
	private ImageView mAdvertisingImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advertising);
		mAdvertisingImage=(ImageView)findViewById(R.id.advertising_image);
		
		mAdvertisingImage.setBackground(getResources().getDrawable(R.drawable.app_guide1));
	}
}
