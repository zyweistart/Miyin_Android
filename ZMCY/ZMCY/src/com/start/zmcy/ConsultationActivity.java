package com.start.zmcy;

import android.os.Bundle;

import com.start.core.BaseActivity;

/**
 * 咨询
 */
public class ConsultationActivity extends BaseActivity{
	
	public static final String CONSOLTATIONID="CONSOLTATIONID";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consultation);
		setMainHeadTitle(getString(R.string.consultation));
		
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			String id=bundle.getString(CONSOLTATIONID);
			getHandlerContext().makeTextLong(id);
		}else{
			finish();
		}
		
 	}
}
