package com.start.zmcy;

import java.io.File;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.start.core.BaseActivity;

public class AdvertisingActivity extends BaseActivity{
	
	private LinearLayout mAdvertising;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advertising);
		mAdvertising=(LinearLayout)findViewById(R.id.advertising);
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		String myJpgPath = BaseContext.getInstance().getStorageDirectory("advertising")+"app_guide2.png";
		File dbFile=new File(myJpgPath);
		if(dbFile.exists()){
			mAdvertising.setBackground(new BitmapDrawable(BitmapFactory.decodeFile(myJpgPath, options)));
		}
		
	}
}
