package com.start.medical.main;

import java.util.ArrayList;
import java.util.List;

import start.widget.SlidingLayout;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.medical.R;
import com.start.medical.department.DepartmentActivity;
import com.start.medical.information.HealthInformationActivity;
import com.start.medical.more.MoreActivity;
import com.start.medical.navigation.NavigationActivity;
import com.start.medical.personal.PersonalCenterActivity;
import com.start.medical.registered.RegisteredActivity;
import com.start.medical.report.TakeReportActivity;
import com.start.medical.wikipedia.HealthWikipediaActivity;

/**
 * 主界面
 * @author start
 *
 */
public class MainActivity extends BaseActivity{

	private SlidingLayout mSlidingLayout;
	
	private List<ImageView> imageViews;
	private int[] imageResId = new int[] {
			R.drawable.default_banner_1,
			R.drawable.default_banner_2,
			R.drawable.default_banner_3,
			R.drawable.default_banner_4,
			R.drawable.default_banner_5 };
	
	private RelativeLayout left_head_nologin_frame;
	private RelativeLayout left_head_login_frame;
	private TextView txt_current_user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setMainHeadTitle(getString(R.string.app_name));
		mSlidingLayout = (SlidingLayout) findViewById(R.id.main_slidingLayout);
		mSlidingLayout.setScrollEvent(findViewById(R.id.main_content_sv));
		
		left_head_nologin_frame=(RelativeLayout)findViewById(R.id.left_head_nologin_frame);
		left_head_login_frame=(RelativeLayout)findViewById(R.id.left_head_login_frame);
		txt_current_user=(TextView)findViewById(R.id.txt_current_user);
		
		//设置Left功能区
		LinearLayout more_item_1=(LinearLayout)findViewById(R.id.more_item_1);
		setSlidingLeftModule(more_item_1,1);
		LinearLayout more_item_2=(LinearLayout)findViewById(R.id.more_item_2);
		setSlidingLeftModule(more_item_2,2);
		LinearLayout more_item_3=(LinearLayout)findViewById(R.id.more_item_3);
		setSlidingLeftModule(more_item_3,3);
		LinearLayout more_item_4=(LinearLayout)findViewById(R.id.more_item_4);
		setSlidingLeftModule(more_item_4,4);
		LinearLayout more_item_5=(LinearLayout)findViewById(R.id.more_item_5);
		setSlidingLeftModule(more_item_5,5);
		//设置主功能区
		LinearLayout main_function_1=(LinearLayout)findViewById(R.id.main_function_1);
		setMainFunctionModule(main_function_1, 1);
		LinearLayout main_function_2=(LinearLayout)findViewById(R.id.main_function_2);
		setMainFunctionModule(main_function_2, 2);
		LinearLayout main_function_3=(LinearLayout)findViewById(R.id.main_function_3);
		setMainFunctionModule(main_function_3, 3);
		LinearLayout main_function_4=(LinearLayout)findViewById(R.id.main_function_4);
		setMainFunctionModule(main_function_4, 4);
		LinearLayout main_function_5=(LinearLayout)findViewById(R.id.main_function_5);
		setMainFunctionModule(main_function_5, 5);
		LinearLayout main_function_6=(LinearLayout)findViewById(R.id.main_function_6);
		setMainFunctionModule(main_function_6, 6);
		LinearLayout main_function_7=(LinearLayout)findViewById(R.id.main_function_7);
		setMainFunctionModule(main_function_7, 7);
		LinearLayout main_function_8=(LinearLayout)findViewById(R.id.main_function_8);
		setMainFunctionModule(main_function_8, 8);
		
		imageViews = new ArrayList<ImageView>();
		for (int i = 0; i < imageResId.length; i++) {
			ImageView imageView = new ImageView(MainActivity.this);
			imageView.setImageResource(imageResId[i]);
			imageViews.add(imageView);
		}
		
		ViewPager bannerPager=(ViewPager)findViewById(R.id.main_banner);
		bannerPager.setOffscreenPageLimit(imageResId.length);
		BannerAdapter ba=new BannerAdapter(this);
		ba.setItemDatas(imageViews);
		bannerPager.setAdapter(ba);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		this.initMainData();
	}

	/**
	 * 设置Left功能区
	 */
	private void setSlidingLeftModule(LinearLayout layout,int type){
		ImageView ic_more=(ImageView)layout.findViewById(R.id.ic_more);
		TextView txt_more=(TextView)layout.findViewById(R.id.txt_more);
		switch(type){
		case 1:
			ic_more.setBackgroundResource(R.drawable.ic_aboutus);
			txt_more.setText(R.string.personal_center);
			break;
		case 2:
			ic_more.setBackgroundResource(R.drawable.ic_change_password);
			txt_more.setText(R.string.mysubscribe);
			break;
		case 3:
			ic_more.setBackgroundResource(R.drawable.ic_clear_cache);
			txt_more.setText(R.string.electroniccase);
			break;
		case 4:
			ic_more.setBackgroundResource(R.drawable.ic_feedback);
			txt_more.setText(R.string.switchhospital);
			break;
		case 5:
			ic_more.setBackgroundResource(R.drawable.ic_feedback);
			txt_more.setText(R.string.setting);
			break;
		}
	}
	
	/**
	 * 主体功能设置
	 */
	private void setMainFunctionModule(LinearLayout layout,int type){
		ImageView ic_main_function=(ImageView)layout.findViewById(R.id.ic_main_function);
		TextView txt_main_function=(TextView)layout.findViewById(R.id.txt_main_function);
		TextView txt_child_main_function=(TextView)layout.findViewById(R.id.txt_child_main_function);
		switch(type){
		case 1:
			ic_main_function.setBackgroundResource(R.drawable.ic_main_function_1);
			txt_main_function.setText(R.string.mainfunctiontxt1);
			txt_main_function.setTextColor(getResources().getColor(R.color.main_function_1));
			txt_child_main_function.setText(R.string.mainfunctionchiltxt1);
			break;
		case 2:
			ic_main_function.setBackgroundResource(R.drawable.ic_main_function_2);
			txt_main_function.setText(R.string.mainfunctiontxt2);
			txt_main_function.setTextColor(getResources().getColor(R.color.main_function_2));
			txt_child_main_function.setText(R.string.mainfunctionchiltxt2);
			break;
		case 3:
			ic_main_function.setBackgroundResource(R.drawable.ic_main_function_3);
			txt_main_function.setText(R.string.mainfunctiontxt3);
			txt_main_function.setTextColor(getResources().getColor(R.color.main_function_3));
			txt_child_main_function.setText(R.string.mainfunctionchiltxt3);
			break;
		case 4:
			ic_main_function.setBackgroundResource(R.drawable.ic_main_function_4);
			txt_main_function.setText(R.string.mainfunctiontxt4);
			txt_main_function.setTextColor(getResources().getColor(R.color.main_function_4));
			txt_child_main_function.setText(R.string.mainfunctionchiltxt4);
			break;
		case 5:
			ic_main_function.setBackgroundResource(R.drawable.ic_main_function_5);
			txt_main_function.setText(R.string.mainfunctiontxt5);
			txt_main_function.setTextColor(getResources().getColor(R.color.main_function_5));
			txt_child_main_function.setText(R.string.mainfunctionchiltxt5);
			break;
		case 6:
			ic_main_function.setBackgroundResource(R.drawable.ic_main_function_6);
			txt_main_function.setText(R.string.mainfunctiontxt6);
			txt_main_function.setTextColor(getResources().getColor(R.color.main_function_6));
			txt_child_main_function.setText(R.string.mainfunctionchiltxt6);
			break;
		case 7:
			ic_main_function.setBackgroundResource(R.drawable.ic_main_function_7);
			txt_main_function.setText(R.string.mainfunctiontxt7);
			txt_main_function.setTextColor(getResources().getColor(R.color.main_function_7));
			txt_child_main_function.setText(R.string.mainfunctionchiltxt7);
			break;
		case 8:
			ic_main_function.setBackgroundResource(R.drawable.ic_main_function_8);
			txt_main_function.setText(R.string.mainfunctiontxt8);
			txt_main_function.setTextColor(getResources().getColor(R.color.main_function_8));
			txt_child_main_function.setText(R.string.mainfunctionchiltxt8);
			break;
		}
	}
	
	private void initMainData(){
		if(getAppContext().currentUser().isLogin()){
			left_head_nologin_frame.setVisibility(View.GONE);
			left_head_login_frame.setVisibility(View.VISIBLE);
			txt_current_user.setText(getAppContext().currentUser().getInfo().get("mobile"));
		}else{
			left_head_nologin_frame.setVisibility(View.VISIBLE);
			left_head_login_frame.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.module_header_left_back){
			if (mSlidingLayout.isLeftLayoutVisible()) {
				mSlidingLayout.scrollToRightLayout();
			} else {
				mSlidingLayout.scrollToLeftLayout();
			}
		}else if(v.getId()==R.id.more_item_1){
			//个人中心
			if(!getAppContext().currentUser().isLogin()){
				goLogin(getString(R.string.not_login_message));
				return;
			}
			startActivity(new Intent(this,PersonalCenterActivity.class));
		}else if(v.getId()==R.id.more_item_2){
			//我的预约
		}else if(v.getId()==R.id.more_item_3){
			//电子病例
		}else if(v.getId()==R.id.more_item_4){
			//切换医院
		}else if(v.getId()==R.id.more_item_5){
			//更多
			startActivity(new Intent(this,MoreActivity.class));
		}else if(v.getId()==R.id.main_function_1){
			//手机挂号
			if(!getAppContext().currentUser().isLogin()){
				goLogin(getString(R.string.not_login_message));
				return;
			}
			startActivity(new Intent(this,RegisteredActivity.class));
		}else if(v.getId()==R.id.main_function_2){
			//妇保中心
			if(!getAppContext().currentUser().isLogin()){
				goLogin(getString(R.string.not_login_message));
				return;
			}
			getHandlerContext().makeTextLong("妇保中心-即将上线");
		}else if(v.getId()==R.id.main_function_3){
			//报告单
			if(!getAppContext().currentUser().isLogin()){
				goLogin(getString(R.string.not_login_message));
				return;
			}
			startActivity(new Intent(this,TakeReportActivity.class));
		}else if(v.getId()==R.id.main_function_4){
			//疫苗接种
			if(!getAppContext().currentUser().isLogin()){
				goLogin(getString(R.string.not_login_message));
				return;
			}
			getHandlerContext().makeTextLong("疫苗接种-即将上线");
		}else if(v.getId()==R.id.main_function_5){
			//医院导航
			startActivity(new Intent(this,NavigationActivity.class));
		}else if(v.getId()==R.id.main_function_6){
			//科室医生
			startActivity(new Intent(this,DepartmentActivity.class));
		}else if(v.getId()==R.id.main_function_7){
			//健康百科
			startActivity(new Intent(this,HealthWikipediaActivity.class));
		}else if(v.getId()==R.id.main_function_8){
			//健康资讯
			startActivity(new Intent(this,HealthInformationActivity.class));
		}else if(v.getId()==R.id.btn_login){
			goLogin(true);
		}
	}
		
}