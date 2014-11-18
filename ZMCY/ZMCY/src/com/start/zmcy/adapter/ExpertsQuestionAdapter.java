package com.start.zmcy.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.start.service.AppListAdapter;
import com.start.zmcy.R;

public class ExpertsQuestionAdapter extends AppListAdapter{

	public ExpertsQuestionAdapter(Activity activity) {
		super(activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holder;
		if (convertView == null) {
			convertView = mActivity.getLayoutInflater().inflate(R.layout.listitem_experts_question, null);
			holder = new HolderView();
			holder.question_title = (TextView)convertView.findViewById(R.id.question_title);
			holder.question_description = (TextView)convertView.findViewById(R.id.question_description);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
//		Map<String,Object> data=mItemDatas.get(position);
		holder.question_title.setText("fjlskafjl7932874932ldkjfl jdls 在jlsj困困要 ");
		holder.question_description.setText("困困困困困困困困困困困困困困困困困困困困困困困困困困困困困困困困困困困困困困困困困困困困困");
		return convertView;
	}
	
	private class HolderView {
		private TextView question_title;
		private TextView question_description;
	}
	
}