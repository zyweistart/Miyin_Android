package com.ancun.yl;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ancun.core.BaseActivity;

/**
 * 申办公证成功
 * @author Start
 */
public class NotarySuccessActivity extends BaseActivity {

	private TextView btnNotaryNotify;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_notary_success);
		setMainHeadTitle(getString(R.string.notary_request_success));
		btnNotaryNotify=(TextView)findViewById(R.id.recorded_appeal_notary_notify);
		//下划线
		btnNotaryNotify.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		btnNotaryNotify.setOnClickListener(this);
	}
	
	 
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.recorded_appeal_notary_notify){
			Intent intentNotaryNotify=new Intent(NotarySuccessActivity.this,NotaryNotifyActivity.class);
			startActivity(intentNotaryNotify);
		}else{
			super.onClick(v);
		}
	}
}