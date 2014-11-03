package com.ancun.yzb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ancun.core.BaseActivity;

public class SettingActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		setMainHeadTitle(getString(R.string.setting));
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.setting_tip){
			//小贴士
			startActivity(new Intent(this,TipActivity.class));
		}else if(v.getId()==R.id.setting_feedback){
			//意见反馈
			startActivity(new Intent(this,FeedbackActivity.class));
		}else if(v.getId()==R.id.setting_versioncheck){
			//新版本检测
		}else if(v.getId()==R.id.setting_softwareshare){
			//软件分享
		}else if(v.getId()==R.id.setting_comment){
			//给个好评吧
		}else if(v.getId()==R.id.setting_follow){
			//关注安存语录
		}else if(v.getId()==R.id.setting_aboutus){
			//关于我们
			startActivity(new Intent(this,AboutusActivity.class));
		}else if(v.getId()==R.id.setting_cleanrecording){
			//清理本地录音缓存
		}else if(v.getId()==R.id.setting_modifypassword){
			//修改密码
			startActivity(new Intent(this,ModifyPasswordActivity.class));
		}else if(v.getId()==R.id.setting_gesturesetting){
			//手势密码设置
		}else if(v.getId()==R.id.setting_gesturemodify){
			//手势密码修改
		}else if(v.getId()==R.id.setting_exitlogin){
			//退出
		}else{
			super.onClick(v);
		}
	}
	
}