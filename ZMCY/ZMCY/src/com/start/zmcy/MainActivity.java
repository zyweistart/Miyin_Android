package com.start.zmcy;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.start.core.BaseFragment;
import com.start.core.BaseFragmentActivity;
import com.start.service.bean.ChannelItem;
import com.start.widget.ColumnHorizontalScrollView;
import com.start.zmcy.adapter.ContentFragmentPagerAdapter;
import com.start.zmcy.content.NewsContentFragment;

public class MainActivity extends BaseFragmentActivity implements OnClickListener {

	public static final int REQUEST_LOGIN_CODE=111;
	public static final int CHANNELRESULT=123;
	
	private static List<ChannelItem> mChannelItems=new ArrayList<ChannelItem>();
	private List<BaseFragment> mBaseFragments = new ArrayList<BaseFragment>();

	private ColumnHorizontalScrollView mColumnHorizontalScrollView;
	private LinearLayout mRadioGroup_content;
	private LinearLayout ll_more_columns;
	private RelativeLayout rl_column;
	/** 当前选中的栏目*/
	private int columnSelectIndex = 0;
	/** 左阴影部分*/
	public ImageView shade_left;
	/** 右阴影部分 */
	public ImageView shade_right;
	public ImageView button_more_columns;
	/** 屏幕宽度 */
	private int mScreenWidth = 0;
	
	private ViewPager mViewPager;
	private ScrollView mMainMenu;
	private LinearLayout mMainMenuFrame;
	private TranslateAnimation mShowAction, mHiddenAction;
	private ContentFragmentPagerAdapter mContentFragmentPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setMainHeadTitle(getString(R.string.app_name));

		mMainMenu = (ScrollView) findViewById(R.id.main_menu);
		mMainMenuFrame = (LinearLayout) findViewById(R.id.main_menu_frame);
		mScreenWidth = getWindowsWidth(this);
		mColumnHorizontalScrollView =  (ColumnHorizontalScrollView)findViewById(R.id.mColumnHorizontalScrollView);
		mRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);
		ll_more_columns = (LinearLayout) findViewById(R.id.ll_more_columns);
		rl_column = (RelativeLayout) findViewById(R.id.rl_column);
		shade_left = (ImageView) findViewById(R.id.shade_left);
		shade_right = (ImageView) findViewById(R.id.shade_right);
		button_more_columns = (ImageView) findViewById(R.id.button_more_columns);
		button_more_columns.setOnClickListener(this);
		
		this.loadChannelData();
		this.initTabColumn();
		
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);
		mContentFragmentPagerAdapter=new ContentFragmentPagerAdapter(getSupportFragmentManager(), mBaseFragments);
		mViewPager.setAdapter(mContentFragmentPagerAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				mViewPager.setCurrentItem(position);
				selectTab(position);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		//显示动画从左向右滑
		mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		mShowAction.setDuration(200);

		//隐藏动画从右向左滑
		mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0.0f, 
				Animation.RELATIVE_TO_SELF, -1.0f,Animation.RELATIVE_TO_SELF, 
				0.0f, Animation.RELATIVE_TO_SELF,0.0f);
		mHiddenAction.setDuration(200);
		
		PushManager.getInstance().initialize(this.getApplicationContext());
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.head_sliding) {
			if (mMainMenu.isShown()) {
				mMainMenu.startAnimation(mHiddenAction);   
				mMainMenu.setVisibility(View.GONE);
				mMainMenuFrame.setVisibility(View.GONE);
			} else {
				mMainMenuFrame.setVisibility(View.VISIBLE);
				mMainMenu.startAnimation(mShowAction);   
				mMainMenu.setVisibility(View.VISIBLE);
			}
		} else if (v.getId() == R.id.main_menu_frame) {
			if (mMainMenu.isShown()) {
				mMainMenu.startAnimation(mHiddenAction);   
				mMainMenu.setVisibility(View.GONE);
			}
			mMainMenuFrame.setVisibility(View.GONE);
		} else if (v.getId() == R.id.head_login) {
			// 登录
			Intent intent=new Intent(this,MemberActivity.class);
			if(!getAppContext().currentUser().isLogin()){
				goLogin(intent,getString(R.string.nologin));
				return;
			}
			startActivity(intent);
//			if(getAppContext().currentUser().isLogin()){
//				startActivity(new Intent(this, MemberActivity.class));
//			}else{
//				goLoginResult(REQUEST_LOGIN_CODE,getString(R.string.nologin));
//			}
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
		}else if(v.getId()==R.id.button_more_columns){
			startActivityForResult(new Intent(this, ChannelActivity.class),0);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==REQUEST_LOGIN_CODE){
			if(resultCode==LoginActivity.RESULT_LOGIN_SUCCESS){
				
			}
		}else{
			if(resultCode==CHANNELRESULT){
				columnSelectIndex = 0;
				this.loadChannelData();
				this.initTabColumn();
				mContentFragmentPagerAdapter.setFramentsNotifyDataSetChanged(mBaseFragments);
			}
		}
	}

	private void initTabColumn() {
		mRadioGroup_content.removeAllViews();
		int count =  mChannelItems.size();
		mColumnHorizontalScrollView.setParam(this, mScreenWidth, mRadioGroup_content, shade_left, shade_right, ll_more_columns, rl_column);
		for(int i = 0; i< count; i++){
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT , LayoutParams.WRAP_CONTENT);
			params.leftMargin = 5;
			params.rightMargin = 5;
//			TextView localTextView = (TextView) mInflater.inflate(R.layout.column_radio_item, null);
			TextView columnTextView = new TextView(this);
//			columnTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			columnTextView.setTextAppearance(this, R.style.top_category_scroll_view_item_text);
//			localTextView.setBackground(getResources().getDrawable(R.drawable.top_category_scroll_text_view_bg));
			columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
			columnTextView.setGravity(Gravity.CENTER);
			columnTextView.setPadding(5, 5, 5, 5);
			columnTextView.setId(i);
			columnTextView.setText(mChannelItems.get(i).getName());
			columnTextView.setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_day));
			if(columnSelectIndex == i){
				columnTextView.setSelected(true);
			}
			columnTextView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
			          for(int i = 0;i < mRadioGroup_content.getChildCount();i++){
				          View localView = mRadioGroup_content.getChildAt(i);
				          if (localView != v)
				        	  localView.setSelected(false);
				          else{
				        	  localView.setSelected(true);
				        	  mViewPager.setCurrentItem(i);
				          }
			          }
				}
			});
			mRadioGroup_content.addView(columnTextView, i ,params);
		}
	}
	
	/** 获取屏幕的宽度 */
	public  int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
	
	/** 
	 *  选择的Column里面的Tab
	 * */
	private void selectTab(int tab_postion) {
		columnSelectIndex = tab_postion;
		for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
			View checkView = mRadioGroup_content.getChildAt(tab_postion);
			int k = checkView.getMeasuredWidth();
			int l = checkView.getLeft();
			int i2 = l + k / 2 - mScreenWidth / 2;
			mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
		}
		//判断是否选中
		for (int j = 0; j <  mRadioGroup_content.getChildCount(); j++) {
			View checkView = mRadioGroup_content.getChildAt(j);
			boolean ischeck;
			if (j == tab_postion) {
				ischeck = true;
			} else {
				ischeck = false;
			}
			checkView.setSelected(ischeck);
		}
	}
	
	//加载频道数据
	public void loadChannelData(){
		mChannelItems=BaseContext.getDBManager().findChannelItemAll(1);
		if(mChannelItems.isEmpty()){
			setDefaultChannelData();
			mChannelItems=BaseContext.getDBManager().findChannelItemAll(1);
		}
		for(int i=0;i<mChannelItems.size();i++){
			mBaseFragments.add(new NewsContentFragment(this, mChannelItems.get(i)));
		}
	}
	
	//设置默认数据
	public void setDefaultChannelData(){
		BaseContext.getDBManager().saveChannelItem(new ChannelItem(1,"头条",1,1));
		BaseContext.getDBManager().saveChannelItem(new ChannelItem(2,"资讯",2,1));
		BaseContext.getDBManager().saveChannelItem(new ChannelItem(3,"会讯",3,1));
		BaseContext.getDBManager().saveChannelItem(new ChannelItem(4,"政策法规",4,1));
		BaseContext.getDBManager().saveChannelItem(new ChannelItem(5,"标准检测",5,1));
		BaseContext.getDBManager().saveChannelItem(new ChannelItem(6,"国内展",6,1));
		BaseContext.getDBManager().saveChannelItem(new ChannelItem(7,"国外展",7,1));
		BaseContext.getDBManager().saveChannelItem(new ChannelItem(8,"工程招标",8,1));
	}
	
}