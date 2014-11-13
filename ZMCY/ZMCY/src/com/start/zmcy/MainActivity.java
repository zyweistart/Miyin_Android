package com.start.zmcy;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;
import android.widget.TextView;

import com.start.core.BaseFragment;
import com.start.core.BaseFragmentActivity;
import com.start.service.bean.NewsCategory;
import com.start.zmcy.adapter.ContentFragmentPagerAdapter;
import com.start.zmcy.content.NewsContentFragment;

public class MainActivity extends BaseFragmentActivity implements
		OnClickListener {

	List<BaseFragment> nBaseFragments = new ArrayList<BaseFragment>();

	private ScrollView mMainMenu;

	private TextView txtNews;
	private TextView txtResources;
	private TextView txtActivities;
	private TextView txtExperts;
	private TextView txtApp;

	private TranslateAnimation mShowAction, mHiddenAction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setMainHeadTitle(getString(R.string.app_name));

		mMainMenu = (ScrollView) findViewById(R.id.main_menu);

		txtNews = (TextView) findViewById(R.id.txtNews);
		txtResources = (TextView) findViewById(R.id.txtResources);
		txtActivities = (TextView) findViewById(R.id.txtActivities);
		txtExperts = (TextView) findViewById(R.id.txtExperts);
		txtApp = (TextView) findViewById(R.id.txtApp);
		setSlidingLeftButtonEnabled(0);

		PagerTabStrip mPagerTabStrip = (PagerTabStrip) findViewById(R.id.mPagerTabStrip);
		mPagerTabStrip.setTextColor(getResources().getColor(R.color.new_title));
		mPagerTabStrip.setTabIndicatorColor(getResources().getColor(
				R.color.new_title));
		mPagerTabStrip.setBackgroundColor(getResources().getColor(
				R.color.new_title_bg));

		NewsCategory nc = new NewsCategory();
		nc.setTitle("头条");
		nBaseFragments.add(new NewsContentFragment(this, nc));
		nc = new NewsCategory();
		nc.setTitle("资讯");
		nBaseFragments.add(new NewsContentFragment(this, nc));
		nc = new NewsCategory();
		nc.setTitle("会讯");
		nBaseFragments.add(new NewsContentFragment(this, nc));
		nc = new NewsCategory();
		nc.setTitle("政策法规");
		nBaseFragments.add(new NewsContentFragment(this, nc));
		nc = new NewsCategory();
		nc.setTitle("标准检测");
		nBaseFragments.add(new NewsContentFragment(this, nc));
		nc = new NewsCategory();
		nc.setTitle("国内展");
		nBaseFragments.add(new NewsContentFragment(this, nc));
		nc = new NewsCategory();
		nc.setTitle("国外展");
		nBaseFragments.add(new NewsContentFragment(this, nc));
		nc = new NewsCategory();
		nc.setTitle("工程招标");
		nBaseFragments.add(new NewsContentFragment(this, nc));
		ViewPager vp = (ViewPager) findViewById(R.id.mViewPager);
		vp.setAdapter(new ContentFragmentPagerAdapter(
				getSupportFragmentManager(), nBaseFragments));

		//显示动画
		mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		mShowAction.setDuration(500);

		//隐藏动画
		mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f);
		mHiddenAction.setDuration(500);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.main_head_sliding) {
			if (mMainMenu.isShown()) {
				mMainMenu.startAnimation(mHiddenAction);   
				mMainMenu.setVisibility(View.GONE);
			} else {
				mMainMenu.startAnimation(mShowAction);   
				mMainMenu.setVisibility(View.VISIBLE);
			}
		} else if (v.getId() == R.id.main_head_login) {
			// 登录
			startActivity(new Intent(this, LoginActivity.class));
		} else if (v.getId() == R.id.txtResources) {
			// 资源
			startActivity(new Intent(this, ResourceActivity.class));
		} else if (v.getId() == R.id.txtActivities) {
			// 活动
			startActivity(new Intent(this, ActivitiesActivity.class));
		} else if (v.getId() == R.id.txtExperts) {
			// 专家
			startActivity(new Intent(this, ExpertsActivity.class));
		} else if (v.getId() == R.id.txtApp) {
			// 应用
			startActivity(new Intent(this, AppActivity.class));
		} else if (v.getId() == R.id.txtMember) {
			// 会员
			startActivity(new Intent(this, MemberActivity.class));
		}
	}

	public void setSlidingLeftButtonEnabled(int index) {
		txtNews.setEnabled(index == 0 ? false : true);
		txtResources.setEnabled(index == 1 ? false : true);
		txtActivities.setEnabled(index == 2 ? false : true);
		txtExperts.setEnabled(index == 3 ? false : true);
		txtApp.setEnabled(index == 4 ? false : true);
	}

}
