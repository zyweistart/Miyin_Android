package com.start.core;

import android.app.Activity;
import android.content.Intent;

public class CacheActivity {

	private Intent mIntent;
	
	public void setIntent(Intent intent) {
		this.mIntent = intent;
	}
	
	public boolean isGotoActivity(){
		return mIntent!=null;
	}
	
	public void startActivity(Activity activity){
		activity.startActivity(mIntent);
		mIntent=null;
	}
	
}
