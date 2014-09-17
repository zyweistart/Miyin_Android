package com.start.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.start.core.CoreActivity;
import com.start.model.Department;
import com.start.navigation.R;

/**
 * 科室详细
 * 
 * @author start
 *
 */
public class DepartmentDetailActivity extends CoreActivity implements OnClickListener {

	private Department department;
	
	private Button mModuleMainHeaderContentLocation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_department_detail);
		
		mModuleMainHeaderContentLocation = (Button) findViewById(R.id.module_main_header_content_location);
		mModuleMainHeaderContentLocation.setOnClickListener(this);
		mModuleMainHeaderContentLocation.setVisibility(View.VISIBLE);
		
		String departmentId=getIntent().getExtras().getString(Department.COLUMN_NAME_ID);
		department=getAppContext().getDepartmentService().findById(departmentId);
		if(department!=null){
			setCurrentActivityTitle(department.getName());
		}
		
		RelativeLayout btnDoctorList=(RelativeLayout)findViewById(R.id.department_detail_doctorlist);
		btnDoctorList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle bundle=new Bundle();
				bundle.putString(Department.COLUMN_NAME_ID, department.getId());
				Intent intent=new Intent(DepartmentDetailActivity.this,DoctorListActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
		TextView tvDetailContent=(TextView)findViewById(R.id.activity_department_detail_content);
		tvDetailContent.setText(department.getIntroduction());
		
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.module_main_header_content_location){
			MainActivity.currentLocationDepartmentId=department.getId();
			startActivity(new Intent(this,MainActivity.class));
			finish();
		}
	}
	
}