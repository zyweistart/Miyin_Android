package com.start.zmcy;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

import com.igexin.sdk.PushManager;
import com.start.core.BaseFragment;
import com.start.core.BaseFragmentActivity;
import com.start.service.bean.NewsCategory;
import com.start.zmcy.adapter.ContentFragmentPagerAdapter;
import com.start.zmcy.content.NewsContentFragment;

public class MainActivity extends BaseFragmentActivity implements OnClickListener {

	public static final int REQUEST_LOGIN_CODE=111;
	
	private static Map<String,String> mNewsCategoryes=new LinkedHashMap<String,String>();
	private List<BaseFragment> nBaseFragments = new ArrayList<BaseFragment>();

	private ScrollView mMainMenu;
	private TranslateAnimation mShowAction, mHiddenAction;

	static{
		mNewsCategoryes.put("1", "头条");
		mNewsCategoryes.put("2", "资讯");
		mNewsCategoryes.put("3", "会讯");
		mNewsCategoryes.put("4", "政策法规");
		mNewsCategoryes.put("5", "标准检测");
		mNewsCategoryes.put("6", "国内展");
		mNewsCategoryes.put("7", "国外展");
		mNewsCategoryes.put("8", "工程招标");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setMainHeadTitle(getString(R.string.app_name));

		mMainMenu = (ScrollView) findViewById(R.id.main_menu);

		PagerTabStrip mPagerTabStrip = (PagerTabStrip) findViewById(R.id.mPagerTabStrip);
		mPagerTabStrip.setTextColor(getResources().getColor(R.color.new_title));
		mPagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.new_title));
		mPagerTabStrip.setBackgroundColor(getResources().getColor(R.color.new_title_bg));
		
		for(String key : mNewsCategoryes.keySet()){
			NewsCategory nc=new NewsCategory();
			nc.setKey(key);
			nc.setTitle(mNewsCategoryes.get(key));
			nBaseFragments.add(new NewsContentFragment(this, nc));
		}
		ViewPager vp = (ViewPager) findViewById(R.id.mViewPager);
		vp.setAdapter(new ContentFragmentPagerAdapter(getSupportFragmentManager(), nBaseFragments));

		//显示动画从左向右滑
		mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		mShowAction.setDuration(500);

		//隐藏动画从右向左滑
		mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0.0f, 
				Animation.RELATIVE_TO_SELF, -1.0f,Animation.RELATIVE_TO_SELF, 
				0.0f, Animation.RELATIVE_TO_SELF,0.0f);
		mHiddenAction.setDuration(500);
		
		PushManager.getInstance().initialize(this.getApplicationContext());
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.head_sliding) {
			if (mMainMenu.isShown()) {
				mMainMenu.startAnimation(mHiddenAction);   
				mMainMenu.setVisibility(View.GONE);
			} else {
				mMainMenu.startAnimation(mShowAction);   
				mMainMenu.setVisibility(View.VISIBLE);
			}
		} else if (v.getId() == R.id.head_login) {
			// 登录
			if(getAppContext().currentUser().isLogin()){
				startActivity(new Intent(this, MemberActivity.class));
			}else{
				goLoginResult(REQUEST_LOGIN_CODE,getString(R.string.nologin));
			}
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==REQUEST_LOGIN_CODE){
			if(resultCode==LoginActivity.RESULT_LOGIN_SUCCESS){
				
			}
		}
	}

}