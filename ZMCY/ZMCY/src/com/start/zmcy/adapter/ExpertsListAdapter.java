package com.start.zmcy.adapter;

import java.util.Map;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.service.AppListAdapter;
import com.start.zmcy.R;

public class ExpertsListAdapter extends AppListAdapter{

	public ExpertsListAdapter(BaseActivity activity) {
		super(activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holder;
		if (convertView == null) {
			convertView = mActivity.getLayoutInflater().inflate(R.layout.listitem_experts_list, null);
			holder = new HolderView();
			holder.listitem_main_title = (TextView) convertView.findViewById(R.id.listitem_child_title);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		Map<String,Object> data=mItemDatas.get(position);
		holder.listitem_main_title.setText("楞dlsk困要要");
		return convertView;
	}
	
	private class HolderView {
		private TextView listitem_main_title;
	}
	
}