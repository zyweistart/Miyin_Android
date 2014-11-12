package com.start.zmcy;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;

import com.start.core.BaseFragment;
import com.start.core.BaseFragmentActivity;
import com.start.zmcy.adapter.ContentFragmentPagerAdapter;
import com.start.zmcy.content.NewsContentFragment;


public class MainActivity  extends BaseFragmentActivity {

    List<BaseFragment> nBaseFragments = new ArrayList<BaseFragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setMainHeadTitle("照明产业");
        ViewPager vp = (ViewPager)findViewById(R.id.mViewPager);
        PagerTabStrip mPagerTabStrip = (PagerTabStrip)findViewById(R.id.mPagerTabStrip);
        mPagerTabStrip.setTextColor(getResources().getColor(R.color.bg_frame));
		mPagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.black)); 
        nBaseFragments.add(new NewsContentFragment("新闻1"));
        nBaseFragments.add(new NewsContentFragment("新闻2"));
        nBaseFragments.add(new NewsContentFragment("新闻3"));
        vp.setAdapter(new ContentFragmentPagerAdapter(getSupportFragmentManager(),nBaseFragments));
    }
}
