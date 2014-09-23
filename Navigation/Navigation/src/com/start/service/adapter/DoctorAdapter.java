package com.start.service.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.start.model.Doctor;
import com.start.navigation.AppContext;
import com.start.navigation.R;
import com.start.utils.Utils;

public class DoctorAdapter extends BaseAdapter{

	private List<Doctor> doctors;
	private Context mContext;
	private AppContext mAppContext;
	
	public DoctorAdapter(Context context,List<Doctor> doctors){
		this.mContext=context;
		this.doctors=doctors;
	}
	
	@Override
	public int getCount() {
		return doctors.size();
	}

	@Override
	public Object getItem(int position) {
		return doctors.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView != null && convertView.getId() == R.id.lvitem_doctor_content) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.lvitem_doctor, null);
			holder=new ViewHolder();
			holder.tvName=(TextView)convertView.findViewById(R.id.lvitem_doctor_name);
			holder.tvHead=(ImageView)convertView.findViewById(R.id.lvitem_doctor_head);
			
			convertView.setTag(holder);
		}
		
		Doctor doctor=doctors.get(position);
		File dataFile=Utils.getFile(mContext, mAppContext.getCurrentDataNo()+"/doctor_head/"+doctor.getId()+".png");
		if(dataFile.exists()){
			holder.tvHead.setImageBitmap(BitmapFactory.decodeFile(dataFile.getAbsolutePath()));
		}else{
			holder.tvHead.setBackgroundResource(R.drawable.ic_doctor);
		}
		holder.doctor=doctor;
		holder.tvName.setText(doctor.getName());
		return convertView;
	}
		

	public class ViewHolder{
		
		public Doctor doctor;
		
		public TextView tvName;
		public ImageView tvHead;
		
	}
}
