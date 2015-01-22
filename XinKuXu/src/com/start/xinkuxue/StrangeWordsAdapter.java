package com.start.xinkuxue;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.start.service.bean.StrangeWordStatisticsItem;

public class StrangeWordsAdapter extends BaseAdapter{

	private Activity mActivity;
	private List<StrangeWordStatisticsItem> mItemDatas;
	
	public StrangeWordsAdapter(Activity activity){
		this.mActivity=activity;
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
		holder.tvTitle.setText("标题");
		holder.tvDate.setText("日期");
		holder.btnReview.setText("复习啦");
		holder.btnTest.setText("测试啦");
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
	
}