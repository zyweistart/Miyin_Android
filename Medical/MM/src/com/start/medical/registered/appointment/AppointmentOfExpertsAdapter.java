package com.start.medical.registered.appointment;

import java.util.Map;

import start.core.AppListAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.medical.R;

public class AppointmentOfExpertsAdapter extends AppListAdapter{

	public AppointmentOfExpertsAdapter(BaseActivity activity) {
		super(activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holder;
		if (convertView == null) {
			convertView = mActivity.getLayoutInflater().inflate(R.layout.listitem_appointment_of_experts, null);
			holder = new HolderView();
			holder.listitem_main_title = (TextView) convertView.findViewById(R.id.listitem_main_title);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		Map<String,String> data=mItemDatas.get(position);
		holder.listitem_main_title.setText(data.get("ksmc"));
		return convertView;
	}
	
	private class HolderView {
		private TextView listitem_main_title;
	}
	
}