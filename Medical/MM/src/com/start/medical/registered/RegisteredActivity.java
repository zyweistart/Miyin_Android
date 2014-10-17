package com.start.medical.registered;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.start.core.BaseActivity;
import com.start.medical.R;
import com.start.medical.registered.appointment.AppointmentOfExpertsActivity;
import com.start.medical.registered.outpatient.OutpatientRegistrationActivity;
import com.start.medical.registered.station.StationToStationQueryActivity;

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
		setContentView(R.layout.activity_registered);
		setMainHeadTitle(getString(R.string.mainfunctiontxt1));
		cb_agree=(CheckBox)findViewById(R.id.cb_agree);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_outpatient_registration||
				v.getId()==R.id.btn_appointment_of_experts||
					v.getId()==R.id.btn_station_to_station_query){
			if(!cb_agree.isChecked()){
				getHandlerContext().makeTextLong("请先阅读并同意声明");
				return;
			}
		}
		if(v.getId()==R.id.btn_outpatient_registration){
			//门诊挂号
			startActivity(new Intent(this,OutpatientRegistrationActivity.class));
			finish();
		}else if(v.getId()==R.id.btn_appointment_of_experts){
			//专家预约
			startActivity(new Intent(this,AppointmentOfExpertsActivity.class));
			finish();
		}else if(v.getId()==R.id.btn_station_to_station_query){
			//叫号查询
			startActivity(new Intent(this,StationToStationQueryActivity.class));
			finish();
		}else{
			super.onClick(v);
		}
	}
	
}
