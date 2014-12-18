package com.start.zmcy.adapter;

import java.util.Map;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.start.service.AppListAdapter;
import com.start.zmcy.R;

public class ExpertsQuestionAdapter extends AppListAdapter{

	public static final String ID="Id";
	public static final String CATEGORYID="ClassId";
	public static final String TITLE="question";
	public static final String DESCRIPTION="content";
	
	public ExpertsQuestionAdapter(Activity activity) {
		super(activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		QuestionHolderView holder;
		if (convertView == null) {
			convertView = mActivity.getLayoutInflater().inflate(R.layout.listitem_experts_question, null);
			holder = new QuestionHolderView();
			holder.question_title = (TextView)convertView.findViewById(R.id.question_title);
			holder.question_description = (TextView)convertView.findViewById(R.id.question_description);
			convertView.setTag(holder);
		} else {
			holder = (QuestionHolderView) convertView.getTag();
		}
		Map<String,Object> data=mItemDatas.get(position);
		holder.question_id=String.valueOf(data.get(ID));
		holder.question_categoryid=String.valueOf(data.get(CATEGORYID));
		holder.question_title.setText(String.valueOf(data.get(TITLE)));
		holder.question_description.setText(String.valueOf(data.get(DESCRIPTION)));
		return convertView;
	}
	
	public class QuestionHolderView {
		public String question_id;
		public String question_categoryid;
		public TextView question_title;
		public TextView question_description;
	}
	
}