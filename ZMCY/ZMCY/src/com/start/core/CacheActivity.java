package com.start.core;

import android.app.Activity;
import android.content.Intent;

public class CacheActivity {

	private Intent mIntent;
	
	public void setIntent(Intent intent) {
		this.mIntent = intent;
	}

	public void startActivity(Activity activity){
		activity.startActivity(mIntent);
		mIntent=null;
	}
	
	public boolean isGotoActivity(){
		return mIntent!=null;
	}
	
}
