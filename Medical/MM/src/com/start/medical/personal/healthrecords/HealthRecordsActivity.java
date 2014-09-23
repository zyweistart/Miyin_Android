package com.start.medical.personal.healthrecords;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.start.core.BaseActivity;
import com.start.medical.R;
import com.start.medical.personal.healthrecords.discomfortrecord.DiscomfortRecordActivity;
import com.start.medical.personal.healthrecords.healthtracking.HealthTrackingActivity;
import com.start.medical.personal.healthrecords.inspect.InspectActivity;
import com.start.medical.personal.healthrecords.medicalhistory.MedicalHistoryActivity;
import com.start.medical.personal.healthrecords.medication.MedicationActivity;
import com.start.medical.personal.healthrecords.medicationreminder.MedicationReminderActivity;

/**
 * 健康档案
 * @author start
 *
 */
public class HealthRecordsActivity extends BaseActivity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_healthrecords);
		setMainHeadTitle("健康档案");
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_medical_recor){
			//病历
			startActivity(new Intent(this,MedicalHistoryActivity.class));
		}else if(v.getId()==R.id.btn_check){
			//检查
			startActivity(new Intent(this,InspectActivity.class));
		}else if(v.getId()==R.id.btn_medication){
			//服药
			startActivity(new Intent(this,MedicationActivity.class));
		}else if(v.getId()==R.id.btn_discomfort_record){
			//不适记录
			startActivity(new Intent(this,DiscomfortRecordActivity.class));
		}else if(v.getId()==R.id.btn_medication_reminder){
			//用药提醒
			startActivity(new Intent(this,MedicationReminderActivity.class));
		}else if(v.getId()==R.id.btn_health_tracking){
			//健康跟踪
			startActivity(new Intent(this,HealthTrackingActivity.class));
		}else{
			super.onClick(v);
		}
	}
	
}