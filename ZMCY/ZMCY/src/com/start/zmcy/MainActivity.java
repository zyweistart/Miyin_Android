package com.start.zmcy;

import java.util.ArrayList;
import java.util.List;

import start.widget.SlidingLayout;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.start.core.BaseFragment;
import com.start.core.BaseFragmentActivity;
import com.start.service.bean.NewsCategory;
import com.start.zmcy.adapter.ContentFragmentPagerAdapter;
import com.start.zmcy.content.NewsContentFragment;


public class MainActivity  extends BaseFragmentActivity implements OnClickListener {

    List<BaseFragment> nBaseFragments = new ArrayList<BaseFragment>();

    private SlidingLayout mSlidingLayout;
    
    private TextView txtNews;
    private TextView txtResources;
    private TextView txtActivities;
    private TextView txtExperts;
    private TextView txtApp;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setMainHeadTitle(getString(R.string.app_name));
        
        txtNews = (TextView)findViewById(R.id.txtNews);
        txtResources = (TextView)findViewById(R.id.txtResources);
        txtActivities = (TextView)findViewById(R.id.txtActivities);
        txtExperts = (TextView)findViewById(R.id.txtExperts);
        txtApp = (TextView)findViewById(R.id.txtApp);
        
        setSlidingLeftButtonEnabled(0);
        
        ViewPager vp = (ViewPager)findViewById(R.id.mViewPager);
        mSlidingLayout = (SlidingLayout) findViewById(R.id.main_slidingLayout);
//		mSlidingLayout.setScrollEvent(vp);
        PagerTabStrip mPagerTabStrip = (PagerTabStrip)findViewById(R.id.mPagerTabStrip);
        mPagerTabStrip.setTextColor(getResources().getColor(R.color.new_title));
		mPagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.new_title));
        mPagerTabStrip.setBackgroundColor(getResources().getColor(R.color.new_title_bg));
       
        NewsCategory nc=new NewsCategory();
		nc.setTitle("头条");
        nBaseFragments.add(new NewsContentFragment(nc));
		nc=new NewsCategory();
		nc.setTitle("资讯");
        nBaseFragments.add(new NewsContentFragment(nc));
        nc=new NewsCategory();
		nc.setTitle("会讯");
        nBaseFragments.add(new NewsContentFragment(nc));
        nc=new NewsCategory();
		nc.setTitle("政策法规");
        nBaseFragments.add(new NewsContentFragment(nc));
        nc=new NewsCategory();
		nc.setTitle("标准检测");
        nBaseFragments.add(new NewsContentFragment(nc));
		nc=new NewsCategory();
		nc.setTitle("国内展");
        nBaseFragments.add(new NewsContentFragment(nc));
		nc=new NewsCategory();
		nc.setTitle("国外展");
        nBaseFragments.add(new NewsContentFragment(nc));
		nc=new NewsCategory();
		nc.setTitle("工程招标");
        nBaseFragments.add(new NewsContentFragment(nc));
        vp.setAdapter(new ContentFragmentPagerAdapter(getSupportFragmentManager(),nBaseFragments));
    }

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.main_head_sliding){
			if (mSlidingLayout.isLeftLayoutVisible()) {
				mSlidingLayout.scrollToRightLayout();
			} else {
				mSlidingLayout.scrollToLeftLayout();
			}
		}else if(v.getId()==R.id.txtNews){
			//新闻
		}else if(v.getId()==R.id.txtResources){
			//资源
		}else if(v.getId()==R.id.txtActivities){
			//活动
		}else if(v.getId()==R.id.txtExperts){
			//专家
		}else if(v.getId()==R.id.txtApp){
			//应用 
		}else if(v.getId()==R.id.txtMember){
			//会员
		}
	}
	
	public void setSlidingLeftButtonEnabled(int index){
		txtNews.setEnabled(index==0?false:true);
		txtResources.setEnabled(index==1?false:true);
		txtActivities.setEnabled(index==2?false:true);
		txtExperts.setEnabled(index==3?false:true);
		txtApp.setEnabled(index==4?false:true);
	}
    
}
