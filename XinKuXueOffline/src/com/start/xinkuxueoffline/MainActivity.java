package com.start.xinkuxueoffline;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.start.core.BaseActivity;
import com.start.core.Constant;
import com.start.xinkuxueoffline.learn.LearnTypeSwitchActivity;
import com.start.xinkuxueoffline.strange.StrangeWordTypeSwitchActivity;
import com.start.xinkuxueoffline.training.MessageActivity;
import com.start.xinkuxueoffline.training.NoticeActivity;
import com.start.xinkuxueoffline.training.ScoreboardActivity;
import com.start.xinkuxueoffline.training.SettingActivity;
import com.start.xinkuxueoffline.training.TrainingAdapter;
import com.start.xinkuxueoffline.vocabulary.VocabularyTypeSwitchActivity;

public class MainActivity extends BaseActivity {

	private LinearLayout mLeftFrame;
	private ListView mLVTraining;
	private List<String> mItemDatas;
	private TranslateAnimation mShowAction, mHiddenAction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mLeftFrame = (LinearLayout) findViewById(R.id.left_frame);
		mLVTraining = (ListView) findViewById(R.id.listview_training);
		// 显示动画从左向右滑
		mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		mShowAction.setDuration(200);

		// 隐藏动画从右向左滑
		mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, -1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f);
		mHiddenAction.setDuration(200);

		mItemDatas = new ArrayList<String>();
		mItemDatas.add(getAppContext().currentUser().getCacheAccount());
		mItemDatas.add("公告栏");
		mItemDatas.add("积分榜");
		mItemDatas.add("留言板");
		mItemDatas.add("设置");
		mItemDatas.add("退出");
		mLVTraining.setAdapter(new TrainingAdapter(MainActivity.this,mItemDatas));
		mLVTraining.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				if(position==1){
					startActivity(new Intent(MainActivity.this,NoticeActivity.class));
				}else if(position==2){
					startActivity(new Intent(MainActivity.this,ScoreboardActivity.class));
				}else if(position==3){
					if(!Constant.ISCURRENTNETWORKVERSION){
						getHandlerContext().makeTextLong(getString(R.string.no_network_version_tip));
						return;
					}
					startActivity(new Intent(MainActivity.this,MessageActivity.class));
				}else if(position==4){
					startActivity(new Intent(MainActivity.this,SettingActivity.class));
				}else if(position==5){
					new AlertDialog.Builder(MainActivity.this)
					 .setMessage(R.string.exittip)
					 .setPositiveButton(R.string.cancle, new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).setNegativeButton(R.string.sure, new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							getAppContext().currentUser().clearCacheUser();
							startActivity(new Intent(MainActivity.this,LoginActivity.class));
							finish();
						}
					}).show();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.learnwords) {
			Intent intent = new Intent(this, LearnTypeSwitchActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.testvocabulary) {
			Intent intent = new Intent(this, VocabularyTypeSwitchActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.newwords) {
			Intent intent = new Intent(this,StrangeWordTypeSwitchActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.specialtraining) {
			if (mLeftFrame.isShown()) {
				mLeftFrame.startAnimation(mHiddenAction);
				mLeftFrame.setVisibility(View.GONE);
			} else {
				mLeftFrame.startAnimation(mShowAction);
				mLeftFrame.setVisibility(View.VISIBLE);
			}
		}
	}

}
