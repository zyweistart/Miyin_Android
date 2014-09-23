package com.start.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.start.core.CoreActivity;
import com.start.model.Introduction;
import com.start.navigation.R;

/**
 * 医院详细
 * @author start
 *
 */
public class HospitalDetailActivity extends CoreActivity implements OnClickListener {

	private TextView mHotspitalDetailIntroduction;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hospital_detail);
		setCurrentActivityTitle(R.string.activity_title_hospital_detail);
		mHotspitalDetailIntroduction=(TextView)findViewById(R.id.hotspital_detail_introduction);
		
		Introduction introduction=getAppContext().getIntroductionService().findCurrentIntroduction();
		if(introduction!=null){
			mHotspitalDetailIntroduction.setText(introduction.getContent());
		}
		
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.hotspital_detail_show){
			MainActivity.ShowMainData=true;
			startActivity(new Intent(this,MainActivity.class));
			finish();
		}
	}

}