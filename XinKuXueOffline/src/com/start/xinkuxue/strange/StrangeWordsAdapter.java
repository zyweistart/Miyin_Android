package com.start.xinkuxue.strange;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import start.utils.TimeUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.start.service.bean.StrangeWordItem;
import com.start.service.bean.StrangeWordStatisticsItem;
import com.start.xinkuxue.BaseContext;
import com.start.xinkuxue.R;

public class StrangeWordsAdapter extends BaseAdapter{

	private Activity mActivity;
	private List<StrangeWordStatisticsItem> mItemDatas;
	private String type;
	
	public StrangeWordsAdapter(Activity activity,String type){
		this.mActivity=activity;
		this.type=type;
	}

	public void setStrangeWordStatisticsItems(List<StrangeWordStatisticsItem> items){
		this.mItemDatas=items;
		notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holder;
		if (convertView == null) {
			convertView = mActivity.getLayoutInflater().inflate(R.layout.lvitem_strange_words, null);
			holder = new HolderView();
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
			holder.btnReview = (Button) convertView.findViewById(R.id.btnReview);
			holder.btnTest = (Button) convertView.findViewById(R.id.btnTest);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		StrangeWordStatisticsItem data=mItemDatas.get(position);
		
		SimpleDateFormat formatF = new SimpleDateFormat(TimeUtils.yyyyMMdd_F);
		SimpleDateFormat formatC = new SimpleDateFormat(TimeUtils.yyyyMMdd_C);
		try {
			holder.tvDate.setText(formatC.format( formatF.parse(data.joinTime)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		holder.tvTitle.setText("有"+data.wordCount+"个生词");
		holder.btnReview.setTag(data);
		holder.btnReview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				StrangeWordStatisticsItem item=(StrangeWordStatisticsItem)v.getTag();
				goListenLook(item);
			}
		});
		holder.btnTest.setTag(data);
		holder.btnTest.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				StrangeWordStatisticsItem item=(StrangeWordStatisticsItem)v.getTag();
				goTest(item);
			}
		});
		return convertView;
	}
	
	public class HolderView{
		private TextView tvTitle,tvDate;
		private Button btnReview,btnTest;
	}

	@Override
	public int getCount() {
		return mItemDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mItemDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void goListenLook(StrangeWordStatisticsItem sws){
		List<StrangeWordItem> words=BaseContext.getDBManager().findAllByStrangeWordItem(sws.getUserName(),sws.getJoinTime(),this.type);
		List<String> ids=new ArrayList<String>();
		for(StrangeWordItem s :words){
			ids.add(s.getIndex());
		}
		Bundle bundle=new Bundle();
		bundle.putString(StrangeWordsTestTypeSwitchActivity.BUNDLE_TYPE, sws.getType());
		bundle.putString(StrangeWordsTestTypeSwitchActivity.BUNDLE_JOINTIME, sws.getJoinTime());
		bundle.putStringArray(StrangeWordsListenLookLearnActivity.BUNDLE_ANSWER_ARRAY, ids.toArray(new String[ids.size()]));
		Intent intent=new Intent(mActivity,StrangeWordsListenLookLearnActivity.class);
		intent.putExtras(bundle);
		mActivity.startActivity(intent);
	}
	
	public void goTest(StrangeWordStatisticsItem sws){
		Bundle bundle=new Bundle();
		bundle.putString(StrangeWordsTestTypeSwitchActivity.BUNDLE_TYPE, sws.getType());
		bundle.putString(StrangeWordsTestTypeSwitchActivity.BUNDLE_JOINTIME, sws.getJoinTime());
		Intent intent=new Intent(mActivity,StrangeWordsTestTypeSwitchActivity.class);
		intent.putExtras(bundle);
		mActivity.startActivity(intent);
	}
	
}