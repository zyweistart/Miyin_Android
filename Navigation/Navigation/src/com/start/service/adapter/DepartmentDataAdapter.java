package com.start.service.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.start.model.Department;
import com.start.navigation.R;

public class DepartmentDataAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<Department> departments;
	
	public DepartmentDataAdapter(Context context){
		this.mContext=context;
	}
	
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
			convertView = LayoutInflater.from(this.mContext).inflate(R.layout.lvitem_department, null);
			holder=new ViewHolder();
			holder.tvName=(TextView)convertView.findViewById(R.id.lvitem_department_name);
			
			convertView.setTag(holder);
		}
		
		Department department=departments.get(position);
		holder.department=department;
		holder.tvName.setText(department.getName());
		return convertView;
	}
	
	public class ViewHolder{
		
		public TextView tvName;
		
		public Department department;
		
	}
	
}
