package com.start.zmcy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.start.core.BaseActivity;

/**
 * 成员
 */
public class MemberActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member);
		setMainHeadTitle(getString(R.string.member));
 	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.txt_gotologin){
			Intent intent=new Intent(this,LoginActivity.class);
			startActivity(intent);
		}else{
			super.onClick(v);
		}
	}
	
}
