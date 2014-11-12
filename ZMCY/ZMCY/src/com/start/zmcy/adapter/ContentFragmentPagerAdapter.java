package com.start.zmcy.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.start.core.BaseFragment;

public class ContentFragmentPagerAdapter extends FragmentPagerAdapter {

	private List<BaseFragment> mFragments;
	
	public ContentFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	public ContentFragmentPagerAdapter(FragmentManager fm,List<BaseFragment> fragments) {
		super(fm);
		this.mFragments = fragments;
	}

	@Override
	public Fragment getItem(int index) {
		return this.mFragments.get(index);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return mFragments.get(position).getTitle();
	}

}
