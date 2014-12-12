package com.start.zmcy;

import android.os.Bundle;

import com.start.core.BaseActivity;

/**
 * 问题详细
 */
public class ExpertsQuestionDetailActivity extends BaseActivity{
	
	public static final String QUESTIONID="QUESTIONID";
	public static final String CATEGORYID="CATEGORYID";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutus);
		setMainHeadTitle(getString(R.string.aboutus));
		
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			String id=bundle.getString(QUESTIONID);
			String categoryid=bundle.getString(CATEGORYID);
			getHandlerContext().makeTextShort(id+"评"+categoryid);
		}else{
			finish();
		}
 	}
}
