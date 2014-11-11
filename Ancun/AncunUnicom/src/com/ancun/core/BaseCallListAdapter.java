package com.ancun.core;

import start.core.AppListAdapter;

public abstract class BaseCallListAdapter extends AppListAdapter {

	private int lastPosition=-1;
	
	public BaseCallListAdapter(BaseActivity activity) {
		super(activity);
	}
	
	public int getLastPosition(){
		return lastPosition;
	}
	
	public void setLastPosition(int position){
		if(lastPosition!=position){
			this.lastPosition=position;
		}else{
			this.lastPosition=-1;
		}
		this.notifyDataSetChanged();
	}
	
}