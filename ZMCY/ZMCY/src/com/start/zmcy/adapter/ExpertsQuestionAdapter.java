package com.start.zmcy.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import start.widget.StartViewPager;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.start.service.AppListAdapter;
import com.start.zmcy.ConsultationActivity;
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
		holder.question_title.setText("");
		holder.question_description.setText("");
		return convertView;
	}
	
	private class HolderView {
		private TextView question_title;
		private TextView question_description;
	}
	
}