package com.start.zmcy.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import start.core.AppContext;
import start.widget.StartViewPager;
import start.widget.StartViewPager.OnSingleTouchListener;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.service.AppListAdapter;
import com.start.service.AppServer;
import com.start.service.BitmapManager;
import com.start.zmcy.ConsultationActivity;
import com.start.zmcy.R;

public class ExpertsListAdapter extends AppListAdapter{
	
	public static final String DATA="Data";
	public static final String TYPE="key";
	public static final String ID="Id";
	public static final String CATEGORYID="ClassId";
	public static final String URL="links";
	public static final String NAME="Name";
	public static final String JOBTITLE="jobTitle";
	public static final String DESCRIPTION="content";
	public static final String IMAGEURL="images";
	
	private static BitmapManager mBannerBitmapManager;
	private static BitmapManager mExpertsBitmapManager;
	
	public ExpertsListAdapter(Activity activity) {
		super(activity);
		if(mBannerBitmapManager ==null){
			mBannerBitmapManager = new BitmapManager(BitmapFactory.decodeResource(activity.getResources(), R.drawable.default_banner));
		}
		if(mExpertsBitmapManager==null){
			mExpertsBitmapManager = new BitmapManager(BitmapFactory.decodeResource(activity.getResources(), R.drawable.default_experts));
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ExpertsHolderView holder;
		if (convertView == null) {
			convertView = mActivity.getLayoutInflater().inflate(R.layout.listitem_experts_list, null);
			holder = new ExpertsHolderView();
			holder.startViewPager = (StartViewPager) convertView.findViewById(R.id.banner_item);
			holder.experts_item = (LinearLayout) convertView.findViewById(R.id.experts_item);
			holder.experts_head = (ImageView) convertView.findViewById(R.id.experts_head);
			holder.experts_name = (TextView) convertView.findViewById(R.id.experts_name);
			holder.experts_pro = (TextView) convertView.findViewById(R.id.experts_pro);
			holder.experts_description = (TextView) convertView.findViewById(R.id.experts_description);
			holder.experts_consultation = (Button) convertView.findViewById(R.id.experts_consultation);
			convertView.setTag(holder);
		} else {
			holder = (ExpertsHolderView) convertView.getTag();
		}
		Map<String,Object> data=mItemDatas.get(position);
		String type=String.valueOf(data.get(TYPE));
		if("banner".equals(type)){
			setItemVisibility(holder,1);
			try {
				JSONArray listo=(JSONArray)data.get(DATA);
				List<Map<String,String>>mListMapData=new ArrayList<Map<String,String>>();
				for(int i=0;i<listo.length();i++){
					JSONObject current = listo.getJSONObject(i);
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
					BannerHolder bh=new BannerHolder();
					bh.id=String.valueOf(mListMapData.get(i).get(ID));
					bh.url=String.valueOf(mListMapData.get(i).get(URL));
					ImageView imageView = new ImageView(this.mActivity);
					String url=AppContext.getInstance().getServerURL()+mListMapData.get(i).get(IMAGEURL);
					mBannerBitmapManager.loadBitmap(url, imageView);
					imageView.setTag(bh);
					imageViews.add(imageView);
				}
				holder.startViewPager.setOffscreenPageLimit(mListMapData.size());
				NewsBannerAdapter mNewsBannerAdapter=new NewsBannerAdapter(this.mActivity);
				mNewsBannerAdapter.setItemDatas(imageViews);
				holder.startViewPager.setAdapter(mNewsBannerAdapter);
				holder.startViewPager.setOnSingleTouchListener(new OnSingleTouchListener() {
					
					@Override
					public void onSingleTouch(View view) {
						BannerHolder hv=(BannerHolder)view.getTag();
						Intent intent = new Intent();
				        intent.setAction("android.intent.action.VIEW");
				        intent.setData(Uri.parse(hv.url));
				        mActivity.startActivity(intent);
					}
					
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			setItemVisibility(holder,2);
			holder.id=String.valueOf(data.get(ID));
			holder.categoryid=String.valueOf(data.get(CATEGORYID));
			
			String url=AppContext.getInstance().getServerURL()+String.valueOf(data.get(IMAGEURL));
			if(TextUtils.isEmpty(url)){
				holder.experts_head.setBackgroundResource(R.drawable.default_experts);
			}else{
				mExpertsBitmapManager.loadBitmap(url, holder.experts_head);
			}
			holder.experts_name.setText(String.valueOf(data.get(NAME)));
			holder.experts_pro.setText(String.valueOf(data.get(JOBTITLE)));
			holder.experts_description.setText(AppServer.html2Text(String.valueOf(data.get(DESCRIPTION))));
			holder.experts_consultation.setTag(holder);
			holder.experts_consultation.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					BannerHolder hv=(BannerHolder)v.getTag();
					BaseActivity bActivity=(BaseActivity)mActivity;
					Bundle bundle=new Bundle();
					bundle.putString(ConsultationActivity.CONSOLTATIONID, hv.id);
					Intent intent=new Intent(mActivity,ConsultationActivity.class);
					intent.putExtras(bundle);
					if(!bActivity.getAppContext().currentUser().isLogin()){
						bActivity.goLogin(intent,bActivity.getString(R.string.nologin));
						return;
					}
					bActivity.startActivity(intent);
				}
			});
		}
		return convertView;
	}
	
	public class BannerHolder{
		public int position;
		public String id;
		public String categoryid;
		public String url;
	}
	
	public class ExpertsHolderView extends BannerHolder {
		private LinearLayout experts_item;
		private StartViewPager startViewPager;
		private ImageView experts_head;
		private TextView experts_name;
		private TextView experts_pro;
		private TextView experts_description;
		private Button experts_consultation;
	}
	
	public void setItemVisibility(ExpertsHolderView holder,int index){
		holder.startViewPager.setVisibility(index==1?View.VISIBLE:View.GONE);
		holder.experts_item.setVisibility(index==2?View.VISIBLE:View.GONE);
	}
	
}