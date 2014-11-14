package com.start.zmcy.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import start.widget.StartViewPager;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.start.service.AppListAdapter;
import com.start.zmcy.R;

public class NewsListAdapter extends AppListAdapter{

	public NewsListAdapter(Activity activity) {
		super(activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holder;
		if (convertView == null) {
			convertView = mActivity.getLayoutInflater().inflate(R.layout.listitem_news_list, null);
			holder = new HolderView();
			holder.startViewPager = (StartViewPager) convertView.findViewById(R.id.banner_item);
			holder.news_item = (LinearLayout) convertView.findViewById(R.id.news_item);
			holder.news_pic = (ImageView) convertView.findViewById(R.id.news_pic);
			holder.news_title = (TextView) convertView.findViewById(R.id.news_title);
			holder.news_description = (TextView) convertView.findViewById(R.id.news_description);
			holder.advertising_item = (RelativeLayout) convertView.findViewById(R.id.advertising_item);
			holder.advertising_close = (ImageView) convertView.findViewById(R.id.advertising_close);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		Map<String,Object> data=mItemDatas.get(position);
		String type=String.valueOf(data.get("type"));
		String recordno=String.valueOf(data.get("recordno"));
		holder.position=position;
		if("1".equals(type)){
			setItemVisibility(holder,1);
			try {
				JSONArray listo=(JSONArray)data.get("bannerlist");
				List<Map<String,String>>mListMapData=new ArrayList<Map<String,String>>();
				for(int i=0;i<listo.length();i++){
					JSONObject current = listo.getJSONObject(i).getJSONObject("bannerinfo");
					Map<String,String> datas=new HashMap<String,String>();
					JSONArray names=current.names();
					for(int j=0;j<names.length();j++){
						String name=names.getString(j);
						datas.put(name, current.getString(name));
					}
					mListMapData.add(datas);
				}
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if("2".equals(type)){
			setItemVisibility(holder,2);
			holder.news_pic.setBackgroundResource(R.drawable.news_default);
			holder.news_title.setText("魏大侠"+recordno);
			holder.news_description.setText("要skl果园果园查轻歌曼舞轻歌曼舞困s要skl果园果园查轻歌曼舞轻歌曼舞困s要skl果园果园查轻歌曼舞轻歌曼舞困s要skl果园果园查轻歌曼舞轻歌曼舞困s要skl果园果园查轻歌曼舞轻歌曼舞困s");
		}else if("3".equals(type)){
			setItemVisibility(holder,3);
			holder.advertising_item.setBackgroundResource(R.drawable.default_banner_5);
			holder.advertising_close.setTag(holder);
			holder.advertising_close.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					HolderView hv=(HolderView)v.getTag();
					getItemDatas().remove(hv.position);
					notifyDataSetChanged();
				}
			});
		}
		return convertView;
	}
	
	public class HolderView {
		public int position;
		
		public LinearLayout news_item;
		public RelativeLayout advertising_item;
		
		public StartViewPager startViewPager;
		public ImageView news_pic;
		public TextView news_title;
		public TextView news_description;
		public ImageView advertising_close;
	}
	
	public void setItemVisibility(HolderView holder,int index){
		holder.startViewPager.setVisibility(index==1?View.VISIBLE:View.GONE);
		holder.news_item.setVisibility(index==2?View.VISIBLE:View.GONE);
		holder.advertising_item .setVisibility(index==3?View.VISIBLE:View.GONE);
	}
	
}