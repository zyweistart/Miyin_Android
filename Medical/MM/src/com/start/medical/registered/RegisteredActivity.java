package com.start.medical.registered;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.start.core.BaseActivity;
import com.start.medical.R;

/**
 * 挂号
 * @author start
 *
 */
public class RegisteredActivity extends BaseActivity {
	
	private CheckBox cb_agree;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(!getAppContext().currentUser().isLogin()){
			goLogin(getString(R.string.not_login_message));
			return;
		}
		setContentView(R.layout.activity_registered);
		setMainHeadTitle(getString(R.string.mainfunctiontxt1));
		cb_agree=(CheckBox)findViewById(R.id.cb_agree);
	}
	
	@Override
	public void onClick(View v) {
		if(cb_agree.isChecked()){
			if(v.getId()==R.id.btn_outpatient_registration){
				//门诊挂号
				
			}else if(v.getId()==R.id.btn_appointment_of_experts){
				//专家预约
				
			}else if(v.getId()==R.id.btn_station_to_station_query){
				//叫号查询
				
			}
		}else{
			getHandlerContext().makeTextLong("请先阅读并同意声明");
		}
	}
	
}
