package com.start.zmcy.adapter;

import java.util.List;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class NewsBannerAdapter extends PagerAdapter {
	
	protected Activity mActivity;
	protected List<ImageView> mItemDatas;
	
	public NewsBannerAdapter(Activity activity){
		this.mActivity=activity;
	}
	
	public void setItemDatas(List<ImageView> mItemDatas) {
		this.mItemDatas = mItemDatas;
	}
	
	@Override
	public int getCount() {
		return mItemDatas.size();
	}

	@Override
	public Object instantiateItem(View view, int index) {
		ImageView im = mItemDatas.get(index);
	    im.setScaleType(ScaleType.CENTER_CROP);
	    ((ViewPager)view).addView(im);
	    return im;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
}