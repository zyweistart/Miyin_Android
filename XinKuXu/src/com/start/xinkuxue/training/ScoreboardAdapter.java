package com.start.xinkuxue.training;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.start.service.AppListAdapter;
import com.start.xinkuxue.R;

public class ScoreboardAdapter extends AppListAdapter{

	private int bg,white,textcolor;
	
	public ScoreboardAdapter(Activity activity) {
		super(activity);
		bg=Color.parseColor("#eeeeee");
		white=Color.parseColor("#FFFFFF");
		textcolor=Color.parseColor("#5c5c5c");
	}

	public void setItems(List<Map<String,Object>> items){
		this.mItemDatas=items;
		notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holder;
		if (convertView == null) {
			convertView = mActivity.getLayoutInflater().inflate(R.layout.lvitem_scoreboard, null);
			holder = new HolderView();
			holder.scoreboard_item = (LinearLayout) convertView.findViewById(R.id.scoreboard_item);
			holder.tvId = (TextView) convertView.findViewById(R.id.tvId);
			holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			holder.tvScore = (TextView) convertView.findViewById(R.id.tvScore);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		if(position%2==0){
			holder.scoreboard_item.setBackgroundColor(bg);
		}else{
			holder.scoreboard_item.setBackgroundColor(white);
		}
		holder.tvId.setTextColor(textcolor);
		holder.tvName.setTextColor(textcolor);
		holder.tvScore.setTextColor(textcolor);
//		Map<String,Object> data=mItemDatas.get(position);
		holder.tvId.setText("1");
		holder.tvName.setText("jdk晨");
		holder.tvScore.setText("20000");
		return convertView;
	}
	
	public class HolderView{
		private LinearLayout scoreboard_item;
		private TextView tvId;
		private TextView tvName;
		private TextView tvScore;
	}

}