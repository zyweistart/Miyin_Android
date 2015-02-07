package com.start.xinkuxueoffline.training;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.start.service.AppListAdapter;
import com.start.xinkuxueoffline.R;

public class NoticeAdapter extends AppListAdapter{

	public NoticeAdapter(Activity activity) {
		super(activity);
	}

	public void setItems(List<Map<String,Object>> items){
		this.mItemDatas=items;
		notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holder;
		if (convertView == null) {
			convertView = mActivity.getLayoutInflater().inflate(R.layout.lvitem_notice, null);
			holder = new HolderView();
			holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		Map<String,Object> data=mItemDatas.get(position);
		holder.categoryId=String.valueOf(data.get("ClassId"));
		holder.Id=String.valueOf(data.get("Id"));
		holder.tvName.setText(String.valueOf(data.get("title")));
		return convertView;
	}
	
	public class HolderView{
		public String categoryId,Id;
		private TextView tvName;
	}

}