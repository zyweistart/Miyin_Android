package com.ancun.yzb;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ancun.core.BaseActivity;

/**
 * 申办公证成功
 * @author Start
 */
public class NotarySuccessActivity extends BaseActivity implements OnClickListener {

	private TextView btnNotaryNotify;
	private TextView btnNotaryList;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_notary_success);
		
		setMainHeadTitle(getString(R.string.notary_request_success));
		
		btnNotaryNotify=(TextView)findViewById(R.id.recorded_appeal_notary_notify);
		btnNotaryNotify.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
		btnNotaryNotify.setOnClickListener(this);
		btnNotaryList=(TextView)findViewById(R.id.recorded_appeal_notary_list);
		btnNotaryList.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
		btnNotaryList.setOnClickListener(this);
	}
	
	 
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.recorded_appeal_notary_notify:
			Intent intentNotaryNotify=new Intent(NotarySuccessActivity.this,NotaryNotifyActivity.class);
			startActivity(intentNotaryNotify);
			break;
		case R.id.recorded_appeal_notary_list:
			Intent intentNotaryList=new Intent(NotarySuccessActivity.this,NotaryListActivity.class);
			startActivity(intentNotaryList);
			break;
		}
	}
}