package com.start.navigation;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.start.core.CoreActivity;
import com.start.model.Department;
import com.start.navigation.R;

/**
 * 科室列表
 * @author start
 *
 */
public class DepartmentListActivity extends CoreActivity implements OnItemClickListener{
	
	private List<Department> departments;
	
	private ListView departmentListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_department_list);
		setCurrentActivityTitle(R.string.activity_title_department_list);
		
		departments=getAppContext().getDepartmentService().findAll();
		
		DataAdapter adapter=new DataAdapter();
		departmentListView=(ListView)findViewById(R.id.module_main_frame_department_list);
		departmentListView.setAdapter(adapter);
		departmentListView.setOnItemClickListener(this);
		
	}
	
	private class DataAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return departments.size();
		}

		@Override
		public Object getItem(int position) {
			return departments.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView != null && convertView.getId() == R.id.lvitem_department_content) {
				holder = (ViewHolder) convertView.getTag();
			} else {
				convertView = LayoutInflater.from(DepartmentListActivity.this).inflate(R.layout.lvitem_department, null);
				holder=new ViewHolder();
				holder.tvName=(TextView)convertView.findViewById(R.id.lvitem_department_name);
				
				convertView.setTag(holder);
			}
			
			Department department=departments.get(position);
			holder.department=department;
			holder.tvName.setText(department.getName());
			return convertView;
		}
		
	}
	
	private class ViewHolder{
		
		private TextView tvName;
		
		private Department department;
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ViewHolder vh=(ViewHolder)arg1.getTag();
		if(vh!=null){
			Bundle bundle=new Bundle();
			bundle.putString(Department.COLUMN_NAME_ID, vh.department.getId());
			Intent intent=new Intent(this,DepartmentDetailActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}

}
