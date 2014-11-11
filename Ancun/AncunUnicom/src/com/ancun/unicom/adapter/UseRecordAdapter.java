package com.ancun.unicom.adapter;

import java.util.Map;

import start.core.AppListAdapter;
import start.utils.TimeUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ancun.core.BaseActivity;
import com.ancun.unicom.R;

public class UseRecordAdapter extends AppListAdapter{

	public UseRecordAdapter(BaseActivity activity) {
		super(activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		UseRecordViewHolder holder;
		if (convertView != null&& convertView.getId() == R.id.lvitem_activity_myaccount_userecord_item_layout) {
			holder = (UseRecordViewHolder) convertView.getTag();
		} else {
			convertView = mActivity.getLayoutInflater().inflate(R.layout.lvitem_account_userecord,null);
			holder = new UseRecordViewHolder();
			holder.time = (TextView) convertView.findViewById(R.id.lvitem_activity_myaccount_userecord_item_txt1);
			holder.remark = (TextView) convertView.findViewById(R.id.lvitem_activity_myaccount_userecord_item_txt2);
			holder.timelong = (TextView) convertView.findViewById(R.id.lvitem_activity_myaccount_userecord_item_txt3);
			convertView.setTag(holder);
		}
		Map<String, String> data = mItemDatas.get(position);
		holder.time.setText(data.get("begintime"));
		holder.remark.setText("录音");
		holder.timelong.setText(TimeUtils.secondConvertTime(Integer.parseInt(data.get("duration"))));
		return convertView;
	}
	
	public class UseRecordViewHolder {
		public TextView time;
		public TextView remark;
		public TextView timelong;
	}
	
}
