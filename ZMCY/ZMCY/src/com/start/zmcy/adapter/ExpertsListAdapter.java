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
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.start.service.AppListAdapter;
import com.start.service.BitmapManager;
import com.start.zmcy.ConsultationActivity;
import com.start.zmcy.R;

public class ExpertsListAdapter extends AppListAdapter{

	private BitmapManager mBannerBitmapManager;
	private BitmapManager mExpertsBitmapManager;
	
	public ExpertsListAdapter(Activity activity) {
		super(activity);
		this.mBannerBitmapManager = new BitmapManager(BitmapFactory.decodeResource(activity.getResources(), R.drawable.default_banner));
		this.mExpertsBitmapManager = new BitmapManager(BitmapFactory.decodeResource(activity.getResources(), R.drawable.default_experts));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holder;
		if (convertView == null) {
			convertView = mActivity.getLayoutInflater().inflate(R.layout.listitem_experts_list, null);
			holder = new HolderView();
			holder.startViewPager = (StartViewPager) convertView.findViewById(R.id.banner_item);
			holder.experts_item = (LinearLayout) convertView.findViewById(R.id.experts_item);
			holder.experts_head = (ImageView) convertView.findViewById(R.id.experts_head);
			holder.experts_name = (TextView) convertView.findViewById(R.id.experts_name);
			holder.experts_pro = (TextView) convertView.findViewById(R.id.experts_pro);
			holder.experts_description = (TextView) convertView.findViewById(R.id.experts_description);
			holder.experts_consultation = (Button) convertView.findViewById(R.id.experts_consultation);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		Map<String,Object> data=mItemDatas.get(position);
		String type=String.valueOf(data.get("type"));
//		String recordno=String.valueOf(data.get("recordno"));
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
				for (int i = 0; i < mListMapData.size(); i++) {
					ImageView imageView = new ImageView(this.mActivity);
					String url=mListMapData.get(i).get("url");
					mBannerBitmapManager.loadBitmap(url, imageView);
					imageViews.add(imageView);
				}
				holder.startViewPager.setOffscreenPageLimit(mListMapData.size());
				NewsBannerAdapter mNewsBannerAdapter=new NewsBannerAdapter(this.mActivity);
				mNewsBannerAdapter.setItemDatas(imageViews);
				holder.startViewPager.setAdapter(mNewsBannerAdapter);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if("2".equals(type)||"3".equals(type)){
			setItemVisibility(holder,2);
			String url=String.valueOf(data.get("url"));
			if(TextUtils.isEmpty(url)){
				holder.experts_head.setBackgroundResource(R.drawable.default_experts);
			}else{
				mExpertsBitmapManager.loadBitmap(url, holder.experts_head);
			}
			holder.experts_name.setText(String.valueOf(data.get("title")));
			holder.experts_pro.setText("研究院");
			holder.experts_description.setText(String.valueOf(data.get("description")));
			holder.experts_consultation.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mActivity.startActivity(new Intent(mActivity,ConsultationActivity.class));
				}
			});
		}
		return convertView;
	}
	
	private class HolderView {
		private LinearLayout experts_item;
		private StartViewPager startViewPager;
		private ImageView experts_head;
		private TextView experts_name;
		private TextView experts_pro;
		private TextView experts_description;
		private Button experts_consultation;
	}
	
	public void setItemVisibility(HolderView holder,int index){
		holder.startViewPager.setVisibility(index==1?View.VISIBLE:View.GONE);
		holder.experts_item.setVisibility(index==2?View.VISIBLE:View.GONE);
	}
	
}