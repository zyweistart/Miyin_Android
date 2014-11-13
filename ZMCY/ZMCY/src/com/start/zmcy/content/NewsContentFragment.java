package com.start.zmcy.content;

import java.util.ArrayList;
import java.util.List;

import start.widget.StartViewPager;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.start.core.BaseFragment;
import com.start.service.bean.NewsCategory;
import com.start.zmcy.R;

public class NewsContentFragment  extends BaseFragment {

	private List<ImageView> imageViews;
	
	private int[] imageResId = new int[] {
			R.drawable.default_banner_1,
			R.drawable.default_banner_2,
			R.drawable.default_banner_3,
			R.drawable.default_banner_4,
			R.drawable.default_banner_5 };
	
	private Activity mActivity;
	private NewsCategory mCategory;
	private View mCurrentView;
	private StartViewPager mBannerPager;
	private NewsBannerAdapter mNewsBannerAdapter;
	
    public NewsContentFragment(Activity activity,NewsCategory category){
    	super();
    	this.mActivity=activity;
    	this.mCategory=category;
    	setTitle(this.mCategory.getTitle());
    	imageViews = new ArrayList<ImageView>();
		for (int i = 0; i < imageResId.length; i++) {
			ImageView imageView = new ImageView(this.mActivity);
			imageView.setImageResource(imageResId[i]);
			imageViews.add(imageView);
		}
    }
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		mCurrentView = inflater.inflate(R.layout.fragment_news,(ViewGroup)getActivity().findViewById(R.id.mViewPager), false);
		mBannerPager=(StartViewPager)mCurrentView.findViewById(R.id.news_banner);
		mBannerPager.setOffscreenPageLimit(imageResId.length);
		mNewsBannerAdapter=new NewsBannerAdapter(this.mActivity);
		mNewsBannerAdapter.setItemDatas(imageViews);
		mBannerPager.setAdapter(mNewsBannerAdapter);
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		if(mCurrentView==null){
//			mCurrentView=inflater.inflate(R.layout.fragment_news, null);
////			mCurrentView=inflater.inflate(R.layout.fragment_news, container, false);
//			mBannerPager=(StartViewPager)mCurrentView.findViewById(R.id.news_banner);
//			mBannerPager.setOffscreenPageLimit(imageResId.length);
//			mNewsBannerAdapter=new NewsBannerAdapter(this.mActivity);
//			mNewsBannerAdapter.setItemDatas(imageViews);
//			mBannerPager.setAdapter(mNewsBannerAdapter);
//        }
		//缓存的rootView需要判断是否已经被加过parent,如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup)mCurrentView.getParent();
        if (parent != null) {
            parent.removeView(mCurrentView);
        }
        return mCurrentView;
    }
    
}