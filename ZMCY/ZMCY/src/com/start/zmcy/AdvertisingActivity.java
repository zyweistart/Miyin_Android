package com.start.zmcy;

import java.io.File;
import java.util.List;

import start.utils.TimeUtils;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;

import com.start.core.BaseActivity;
import com.start.service.bean.AdvertisingItem;

public class AdvertisingActivity extends BaseActivity{
	
	private List<AdvertisingItem> mAdvertisingItems;
	
	private RelativeLayout mAdvertising;
	private Boolean skipflag=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advertising);
		mAdvertising=(RelativeLayout)findViewById(R.id.advertising);
		
		mAdvertisingItems=BaseContext.getDBManager().findAdvertisingItemAll(TimeUtils.getSysTimeLong());
		index=0;
		if(mAdvertisingItems.size()>0){
			setAdvertising(index);
		}else{
			goMain();
		}
	}
	
	private int index=0;
	
	@SuppressWarnings("deprecation")
	public void setAdvertising(int i){
		AdvertisingItem ai=mAdvertisingItems.get(i);
		final String myJpgPath = BaseContext.getInstance().getStorageDirectory("adv")+ai.getFileName();
		File dbFile=new File(myJpgPath);
		if(dbFile.exists()){
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			mAdvertising.setTag(ai.getUrl());
			mAdvertising.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeFile(myJpgPath, options)));
			AlphaAnimation aa = new AlphaAnimation(0.3f,1.0f);
			aa.setDuration(2000);
			mAdvertising.startAnimation(aa);
			aa.setAnimationListener(new AnimationListener(){
				
				@Override
				public void onAnimationRepeat(Animation animation) {}
				
				@Override
				public void onAnimationStart(Animation animation) {}
				
				@Override
				public void onAnimationEnd(Animation arg0) {
					index++;
					if(index<mAdvertisingItems.size()){
						setAdvertising(index);
					}else{
						goMain();
					}
				}
				
			});
		}else{
			BaseContext.getDBManager().deleteAdvertising(ai.getFileName());
			index++;
			if(index<mAdvertisingItems.size()){
				setAdvertising(index);
			}else{
				goMain();
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.advertising){
			Intent intent = new Intent();
	        intent.setAction("android.intent.action.VIEW");
	        Uri content_url = Uri.parse(String.valueOf(v.getTag()));
	        intent.setData(content_url);
	        startActivity(intent);
			goMain();
		}else if(v.getId()==R.id.btn_skip){
			goMain();
		}
	}
	
	public void goMain(){
		if(!skipflag){
			skipflag=true;
			index=mAdvertisingItems.size();
			Intent intent=new Intent(AdvertisingActivity.this,MainActivity.class);
			startActivity(intent);
			finish();
		}
	}
}
