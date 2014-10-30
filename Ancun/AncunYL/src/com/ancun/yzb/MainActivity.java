package com.ancun.yzb;

import start.core.AppException;
import start.widget.ScrollLayout;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ancun.core.BaseActivity;
import com.ancun.core.Constant;
import com.ancun.yzb.layout.CallRecordsContentView;
import com.ancun.yzb.layout.ContactsContentView;
import com.ancun.yzb.layout.DialContentView;
import com.ancun.yzb.layout.MyRecordingContentView;

public class MainActivity extends BaseActivity implements ScrollLayout.OnViewChangeListener {
	
	private int mLastViewIndex;
	private long mLastPressTime;
	
	private TextView mTab1, mTab2, mTab3, mTab4;
	private ImageView mTabBottomBar;
	private Button mBtnNetworkConnection;
	private ScrollLayout mScrollLayout;
	
	private DialContentView mDialContentView;
	private CallRecordsContentView mCallRecordsContentView;
	private ContactsContentView mContactsContentView;
	private MyRecordingContentView mMyRecordingContentView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mTab1 = (TextView) findViewById(R.id.tab1);
		mTab2 = (TextView) findViewById(R.id.tab2);
		mTab3 = (TextView) findViewById(R.id.tab3);
		mTab4 = (TextView) findViewById(R.id.tab4);
		mTabBottomBar = (ImageView) findViewById(R.id.tab_bottom_bar);
		
		mBtnNetworkConnection = (Button) findViewById(R.id.btn_networkconnected);
		mBtnNetworkConnection.setOnClickListener(this);
		
		mScrollLayout = (ScrollLayout) findViewById(R.id.main_scrolllayout);
		mScrollLayout.SetOnViewChangeListener(this);
		
		mDialContentView = new DialContentView(this);
		mScrollLayout.addView(mDialContentView.getLayoutView());
		mCallRecordsContentView = new CallRecordsContentView(this);
		mScrollLayout.addView(mCallRecordsContentView.getLayoutView());
		mContactsContentView = new ContactsContentView(this);
		mScrollLayout.addView(mContactsContentView.getLayoutView());
		mMyRecordingContentView = new MyRecordingContentView(this);
		mScrollLayout.addView(mMyRecordingContentView.getLayoutView());
	}
	
	
	@Override
	protected void onStart() {
		super.onStart();
		mScrollLayout.setIsScroll(true);
	}
	
	@Override
	public void onClick(View v) {
		if (v == mTab1.getParent()) {
			mScrollLayout.snapToScreen(0);
		} else if (v == mTab2.getParent()) {
			mScrollLayout.snapToScreen(1);
		} else if (v == mTab3.getParent()) {
			mScrollLayout.snapToScreen(2);
		} else if (v == mTab4.getParent()) {
			mScrollLayout.snapToScreen(3);
		} else if (v.getId() == R.id.main_ibmore) {
			startActivityForResult(new Intent(this, SettingActivity.class), 0);
		} else if (v.getId() == R.id.main_btnacount) {
			startActivity(new Intent(this, AccountActivity.class));
		} else if (v.getId()==R.id.btn_networkconnected){
			startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
		} 
	}

	@Override
	public void OnViewChange(final int currentViewIndex) {
		if (mLastViewIndex != currentViewIndex) {
			switch (currentViewIndex) {
			case 0:
				mTab1.setTextColor(getResources().getColor(R.color.tab_bar_s));
				mTab2.setTextColor(getResources().getColor(R.color.tab_bar_n));
				mTab3.setTextColor(getResources().getColor(R.color.tab_bar_n));
				mTab4.setTextColor(getResources().getColor(R.color.tab_bar_n));
				break;
			case 1:
				mTab1.setTextColor(getResources().getColor(R.color.tab_bar_n));
				mTab2.setTextColor(getResources().getColor(R.color.tab_bar_s));
				mTab3.setTextColor(getResources().getColor(R.color.tab_bar_n));
				mTab4.setTextColor(getResources().getColor(R.color.tab_bar_n));
				break;
			case 2:
				mTab1.setTextColor(getResources().getColor(R.color.tab_bar_n));
				mTab2.setTextColor(getResources().getColor(R.color.tab_bar_n));
				mTab3.setTextColor(getResources().getColor(R.color.tab_bar_s));
				mTab4.setTextColor(getResources().getColor(R.color.tab_bar_n));
				break;
			case 3:
				mTab1.setTextColor(getResources().getColor(R.color.tab_bar_n));
				mTab2.setTextColor(getResources().getColor(R.color.tab_bar_n));
				mTab3.setTextColor(getResources().getColor(R.color.tab_bar_n));
				mTab4.setTextColor(getResources().getColor(R.color.tab_bar_s));
				break;
			}
			//标签过渡动画
			TranslateAnimation animation = null;
			int tab1_x = 0;
			int tab2_x = ((View) mTab2.getParent()).getWidth();
			int tab3_x = ((View) mTab2.getParent()).getWidth() + ((View) mTab3.getParent()).getWidth();
			int tab4_x = ((View) mTab2.getParent()).getWidth() + ((View) mTab3.getParent()).getWidth() + ((View) mTab4.getParent()).getWidth();
			switch (currentViewIndex) {
			case 0:
				if (mLastViewIndex == 1) {
					animation = new TranslateAnimation(tab2_x, tab1_x, 0, 0);
				} else if (mLastViewIndex == 2) {
					animation = new TranslateAnimation(tab3_x, tab1_x, 0, 0);
				} else if (mLastViewIndex == 3) {
					animation = new TranslateAnimation(tab4_x, tab1_x, 0, 0);
				}
				break;
			case 1:
				if (mLastViewIndex == 0) {
					animation = new TranslateAnimation(tab1_x, tab2_x, 0, 0);
				} else if (mLastViewIndex == 2) {
					animation = new TranslateAnimation(tab3_x, tab2_x, 0, 0);
				} else if (mLastViewIndex == 3) {
					animation = new TranslateAnimation(tab4_x, tab2_x, 0, 0);
				}
				break;
			case 2:
				if (mLastViewIndex == 0) {
					animation = new TranslateAnimation(tab1_x, tab3_x, 0, 0);
				} else if (mLastViewIndex == 1) {
					animation = new TranslateAnimation(tab2_x, tab3_x, 0, 0);
				} else if (mLastViewIndex == 3) {
					animation = new TranslateAnimation(tab4_x, tab3_x, 0, 0);
				}
				break;
			case 3:
				if (mLastViewIndex == 0) {
					animation = new TranslateAnimation(tab1_x, tab4_x, 0, 0);
				} else if (mLastViewIndex == 1) {
					animation = new TranslateAnimation(tab2_x, tab4_x, 0, 0);
				} else if (mLastViewIndex == 2) {
					animation = new TranslateAnimation(tab3_x, tab4_x, 0, 0);
				}
				break;
			}
			animation.setDuration(500);
			animation.setInterpolator(AnimationUtils.loadInterpolator(MainActivity.this,android.R.anim.overshoot_interpolator));
			animation.setFillAfter(true);
			animation.setInterpolator(AnimationUtils.loadInterpolator(MainActivity.this,android.R.anim.decelerate_interpolator));
			animation.setAnimationListener(new AnimationListener(){

				@Override
				public void onAnimationStart(Animation animation) {
					
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					//初始化页面加载数据
//							if (currentViewIndex==0){
//								// 跳转到拨号界面,更新消息数显示
////								dialContent.setNoReadCount(SharedPreferencesUtils.getMsgNoReadCount(MainActivity.this));
//							} else if (currentViewIndex == 1) {
//								if (recentContent.isOpenRefreshData) {
//									refreshRecentContent();
//								}
//							} else if (currentViewIndex == 2) {
//								if (contactContent.isOpenRefreshData) {
//									refreshContactContent();
//								}
//							} else if (currentViewIndex == 3) {
//								//判断是否已经设置手势
//								if (TextUtils.isEmpty(SharedPreferencesUtils.getGesturePass(MainActivity.this))) {
//									refreshRecordingContent();
//								}else{
//									startActivityForResult(new Intent(MainActivity.this,LockActivity.class), 0);
//								}
//							}
					mLastViewIndex=currentViewIndex;
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					
				}
				
			});
			mTabBottomBar.startAnimation(animation);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - mLastPressTime) > 2000) {
				getHandlerContext().makeTextShort("再按一次退出程序");
				mLastPressTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onProcessMessage(Message msg) throws AppException {
		switch(msg.what){
		case Constant.Handler.HANDLERNETCHECKMESSAGEWHATCONNECT:
			mBtnNetworkConnection.setVisibility(View.GONE);
			break;
		case Constant.Handler.HANDLERNETCHECKMESSAGEWHATNOCONNECT:
			mBtnNetworkConnection.setVisibility(View.VISIBLE);
			break;
		default:
			super.onProcessMessage(msg);
			break;
		}
	}
	
}
