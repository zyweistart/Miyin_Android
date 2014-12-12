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
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.start.service.AppListAdapter;
import com.start.service.AppServer;
import com.start.service.BitmapManager;
import com.start.zmcy.R;
import com.start.zmcy.content.NewsContentFragment;

public class NewsListAdapter extends AppListAdapter{

	public static final String DATA="Data";
	public static final String TYPE="key";
	public static final String ID="Id";
	public static final String CATEGORYID="ClassId";
	public static final String URL="links";
	public static final String TITLE="title";
	public static final String DESCRIPTION="content";
	public static final String IMAGEURL="images";
	
	private static BitmapManager mBannerBitmapManager;
	private static BitmapManager mNewsBitmapManager;
	
	public NewsListAdapter(Activity activity) {
		super(activity);
		if(mBannerBitmapManager ==null){
			mBannerBitmapManager = new BitmapManager(BitmapFactory.decodeResource(activity.getResources(), R.drawable.default_banner));
		}
		if(mNewsBitmapManager==null){
			mNewsBitmapManager = new BitmapManager(BitmapFactory.decodeResource(activity.getResources(), R.drawable.default_news));
		}
	}

	@SuppressWarnings("deprecation")
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
		String type=String.valueOf(data.get(TYPE));
		holder.position=position;
		if("banner".equals(type)){
			//BANNER
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
					ImageView imageView = new ImageView(this.mActivity);
					BannerHolder bh=new BannerHolder();
					bh.id=String.valueOf(mListMapData.get(i).get(ID));
					bh.categoryId=String.valueOf(mListMapData.get(i).get(CATEGORYID));
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
						NewsContentFragment.gotoNews(mActivity,hv.categoryId,hv.id);
					}
					
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if("advert".equals(type)){
			//Advertising
			setItemVisibility(holder,3);
			try{
				JSONObject listo=(JSONObject)data.get(DATA);
				holder.id=String.valueOf(listo.getString(ID));
				holder.categoryId=String.valueOf(listo.getString(CATEGORYID));
				holder.url=String.valueOf(listo.getString(URL));
				
				String url=AppContext.getInstance().getServerURL()+String.valueOf(listo.getString(IMAGEURL));
				if(TextUtils.isEmpty(url)){
					holder.advertising_item.setBackgroundResource(R.drawable.default_banner);
				}else{
					ImageView iv=new ImageView(mActivity);
					mBannerBitmapManager.loadBitmap(url, iv);
					holder.advertising_item.setBackgroundDrawable(iv.getDrawable());
				}
				holder.advertising_item.setTag(holder);
				holder.advertising_item.setClickable(true);
				holder.advertising_item.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						HolderView hv=(HolderView)v.getTag();
						Intent intent = new Intent();
				        intent.setAction("android.intent.action.VIEW");
				        Uri content_url = Uri.parse(hv.url);
				        intent.setData(content_url);
				        mActivity.startActivity(intent);
					}
					
				});
				
				holder.advertising_close.setTag(holder);
				holder.advertising_close.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						HolderView hv=(HolderView)v.getTag();
						getItemDatas().remove(hv.position);
						notifyDataSetChanged();
					}
				});
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			//News
			setItemVisibility(holder,2);
			holder.id=String.valueOf(data.get(ID));
			holder.categoryId=String.valueOf(data.get(CATEGORYID));
			String url=AppContext.getInstance().getServerURL()+String.valueOf(data.get(IMAGEURL));
			if(TextUtils.isEmpty(url)){
				holder.news_pic.setBackgroundResource(R.drawable.default_news);
			}else{
				mNewsBitmapManager.loadBitmap(url, holder.news_pic);
			}
			holder.news_title.setText(String.valueOf(data.get(TITLE)));
			String description=AppServer.html2Text(String.valueOf(data.get(DESCRIPTION)));
			holder.news_description.setText(description);
		}
		return convertView;
	}
	
	public class BannerHolder{
		public int position;
		public String id;
		public String categoryId;
		public String url;
	}
	
	public class HolderView extends BannerHolder {
		
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