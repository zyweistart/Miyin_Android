package com.start.zmcy.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.start.core.BaseFragment;

public class ContentFragmentPagerAdapter extends FragmentPagerAdapter {

	private FragmentManager mFragmentManager;
	private List<BaseFragment> mFragments;

	public ContentFragmentPagerAdapter(FragmentManager fm,
			List<BaseFragment> fragments) {
		super(fm);
		this.mFragmentManager = fm;
		this.mFragments = new ArrayList<BaseFragment>(fragments);
	}

	public void setFramentsNotifyDataSetChanged(List<BaseFragment> fragments) {
		if (this.mFragments != null) {
			FragmentTransaction ft = mFragmentManager.beginTransaction();
			for (Fragment f : this.mFragments) {
				ft.remove(f);
			}
			ft.commit();
			ft = null;
			mFragmentManager.executePendingTransactions();
		}
		this.mFragments = new ArrayList<BaseFragment>(fragments);
		notifyDataSetChanged();
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public Fragment getItem(int index) {
		return this.mFragments.get(index);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

}
