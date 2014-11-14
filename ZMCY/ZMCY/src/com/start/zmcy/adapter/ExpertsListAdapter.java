package com.start.zmcy.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import start.widget.StartViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
			holder.experts_item = (LinearLayout) convertView.findViewById(R.id.experts_item);
			holder.advertising_item = (RelativeLayout) convertView.findViewById(R.id.advertising_item);
			
			holder.startViewPager = (StartViewPager) convertView.findViewById(R.id.news_banner);
			holder.listitem_main_title = (TextView) convertView.findViewById(R.id.listitem_child_title);
			holder.news_advertising = (ImageView) convertView.findViewById(R.id.news_advertising);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		Map<String,Object> data=mItemDatas.get(position);
		String type=String.valueOf(data.get("type"));
		String recordno=String.valueOf(data.get("recordno"));
		if("1".equals(type)){
			setItemVisibility(holder,1);
			List<ImageView> imageViews= new ArrayList<ImageView>();
			int[] imageResId = new int[] {
					R.drawable.default_banner_1,
					R.drawable.default_banner_2,
					R.drawable.default_banner_3,
					R.drawable.default_banner_4,
					R.drawable.default_banner_5 };
			for (int i = 0; i < imageResId.length; i++) {
				ImageView imageView = new ImageView(this.mActivity);
				imageView.setImageResource(imageResId[i]);
				imageViews.add(imageView);
			}
			holder.startViewPager.setOffscreenPageLimit(imageResId.length);
			NewsBannerAdapter mNewsBannerAdapter=new NewsBannerAdapter(this.mActivity);
			mNewsBannerAdapter.setItemDatas(imageViews);
			holder.startViewPager.setAdapter(mNewsBannerAdapter);
		}else if("2".equals(type)){
			setItemVisibility(holder,2);
			
			holder.listitem_main_title.setText("这是普通信息条"+recordno);
		}else if("3".equals(type)){
			setItemVisibility(holder,3);
			holder.news_advertising.setBackgroundResource(R.drawable.default_banner_1);
		}
		return convertView;
	}
	
	private class HolderView {
		private LinearLayout experts_item;
		private RelativeLayout advertising_item;
		
		private StartViewPager startViewPager;
		private ImageView news_advertising;
		private TextView listitem_main_title;
	}
	
	public void setItemVisibility(HolderView holder,int index){
		holder.startViewPager.setVisibility(index==1?View.VISIBLE:View.GONE);
		holder.experts_item.setVisibility(index==2?View.VISIBLE:View.GONE);
		holder.advertising_item .setVisibility(index==3?View.VISIBLE:View.GONE);
	}
	
}