package com.start.medical.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.start.core.BaseActivity;
import com.start.medical.R;
import com.start.medical.personal.appointment.MyAppointmentActivity;
import com.start.medical.personal.healthrecords.HealthRecordsActivity;
import com.start.medical.personal.registered.MyRegisteredActivity;
import com.start.medical.personal.report.MyReportActivity;

/**
 * 个人中心
 * @author start
 *
 */
public class PersonalCenterActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		if(!getAppContext().currentUser().isLogin()){
//			goLogin(getString(R.string.not_login_message));
//			return;
//		}
		setContentView(R.layout.activity_personal_center);
		setMainHeadTitle("个人中心");
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_edit_personal){
			//编辑个人中心
			startActivity(new Intent(this,AccountBindActivity.class));
		}else if(v.getId()==R.id.btn_health_records){
			//健康档案
			startActivity(new Intent(this,HealthRecordsActivity.class));
		}else if(v.getId()==R.id.btn_my_registered){
			//我的挂号
			startActivity(new Intent(this,MyRegisteredActivity.class));
		}else if(v.getId()==R.id.btn_my_appointment){
			//我的预约
			startActivity(new Intent(this,MyAppointmentActivity.class));
		}else if(v.getId()==R.id.btn_my_takereport){
			//我的报告单
			startActivity(new Intent(this,MyReportActivity.class));
		}else if(v.getId()==R.id.btn_my_consultation){
			//我的咨询
			getHandlerContext().makeTextLong("即将上线");
		}else{
			super.onClick(v);
		}
	}
	
}
