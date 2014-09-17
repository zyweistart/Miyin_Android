package com.start.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.start.core.Constant;
import com.start.core.CoreActivity;

/**
 * 导航
 * @author start
 *
 */
public class NavigationActivity extends CoreActivity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);
		setCurrentActivityTitle(R.string.activity_title_navigation);
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.navigation_hospital){
			if(Constant.EMPTYSTR.equals(getAppContext().getCurrentDataNo())){
				startActivity(new Intent(this, FirstSetMapDataActivity.class));
			}else{
				startActivity(new Intent(this, MainActivity.class));
			}
			finish();
		}else if(v.getId()==R.id.navigation_library){
			makeTextLong("即将上线");
		}
	}

}
