package com.start.navigation;

import java.io.File;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.start.core.CoreActivity;
import com.start.model.Department;
import com.start.model.Doctor;
import com.start.navigation.R;
import com.start.utils.Utils;

/**
 * 医生详细
 * @author start
 *
 */
public class DoctorDetailActivity extends CoreActivity implements OnClickListener{

	private Doctor doctor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor_detail);
		
		String doctorId=getIntent().getExtras().getString(Doctor.COLUMN_NAME_ID);
		doctor=getAppContext().getDoctorService().findById(doctorId);
		if(doctor!=null){
			setCurrentActivityTitle(doctor.getName());
			
			ImageView head=(ImageView)findViewById(R.id.activity_doctor_detail_head);
			File dataFile=Utils.getFile(this, getAppContext().getCurrentDataNo()+"/doctor_head/"+doctor.getId()+".png");
			if(dataFile.exists()){
				head.setImageBitmap(BitmapFactory.decodeFile(dataFile.getAbsolutePath()));
			}else{
				head.setBackgroundResource(R.drawable.ic_doctor);
			}
			
			TextView tv=(TextView)findViewById(R.id.activity_doctor_detail_content);
			tv.setText(doctor.getIntroduction());
		}else{
			finish();
		}

	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.doctor_detail_department){
			Bundle bundle=new Bundle();
			bundle.putString(Department.COLUMN_NAME_ID, doctor.getDepartmentId());
			Intent intent=new Intent(this,DepartmentDetailActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}

}
