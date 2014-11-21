package com.start.zmcy;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.start.core.BaseActivity;

/**
 * 推送设置
 */
public class PushSettingActivity extends BaseActivity{
	
	private Drawable img_off;
	private Drawable img_on;
	
	private TextView txt1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pushsettting);
		setMainHeadTitle(getString(R.string.pushsetting));
		
		img_off= getResources().getDrawable(R.drawable.button_state_off);
		img_off.setBounds(0, 0, img_off.getMinimumWidth(), img_off.getMinimumHeight());
		img_on= getResources().getDrawable(R.drawable.button_state_no);
		img_on.setBounds(0, 0, img_on.getMinimumWidth(), img_on.getMinimumHeight());
		
		txt1=(TextView)findViewById(R.id.txt1);
		txt1.setCompoundDrawables(null, null, img_on, null);
		
 	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.txt1){
			
		}else{
			super.onClick(v);
		}
	}
	
}
