package com.start.xinkuxue.training;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.start.xinkuxue.R;

public class TrainingAdapter extends BaseAdapter{

	private Activity mActivity;
	private List<String> mItemDatas;
	
	public TrainingAdapter(Activity activity,List<String> items){
		this.mActivity=activity;
		setItems(items);
	}

	public void setItems(List<String> items){
		this.mItemDatas=items;
		notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holder;
		if (convertView == null) {
			convertView = mActivity.getLayoutInflater().inflate(R.layout.lvitem_training, null);
			holder = new HolderView();
			holder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
			holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		String data=mItemDatas.get(position);
		if(position==0){
			holder.ivImage.setImageResource(R.drawable.ic_my);
		}else if(position==1){
			holder.ivImage.setImageResource(R.drawable.ic_notice);
		}else if(position==2){
			holder.ivImage.setImageResource(R.drawable.ic_scoreboard);
		}else if(position==3){
			holder.ivImage.setImageResource(R.drawable.ic_message);
		}else if(position==4){
			holder.ivImage.setImageResource(R.drawable.ic_proccess);
		}else if(position==5){
			holder.ivImage.setImageResource(R.drawable.ic_setting);
		}
		holder.tvName.setText(data);
		return convertView;
	}
	
	public class HolderView{
		private ImageView ivImage;
		private TextView tvName;
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