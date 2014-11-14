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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.start.service.AppListAdapter;
import com.start.zmcy.R;

public class ExpertsListAdapter extends AppListAdapter{

	public ExpertsListAdapter(Activity activity) {
		super(activity);
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
		String recordno=String.valueOf(data.get("recordno"));
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
		}else if("2".equals(type)||"3".equals(type)){
			setItemVisibility(holder,2);
			holder.experts_head.setBackgroundResource(R.drawable.experts_default);
			holder.experts_name.setText("魏大侠"+recordno);
			holder.experts_pro.setText("研究院");
			holder.experts_description.setText("要skl果园果园查轻歌曼舞轻歌曼舞困s要skl果园果园查轻歌曼舞轻歌曼舞困s要skl果园果园查轻歌曼舞轻歌曼舞困s要skl果园果园查轻歌曼舞轻歌曼舞困s要skl果园果园查轻歌曼舞轻歌曼舞困s");
			holder.experts_consultation.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
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