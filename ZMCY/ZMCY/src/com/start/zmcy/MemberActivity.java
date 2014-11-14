package com.start.zmcy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.start.core.BaseActivity;

/**
 * 成员
 */
public class MemberActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member);
		setMainHeadTitle(getString(R.string.member));
 	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.txt_account_info){
			startActivity(new Intent(this,LoginActivity.class));
		}else if(v.getId()==R.id.txtSigin){
			getHandlerContext().makeTextLong("签到");
		}else if(v.getId()==R.id.txtCollect){
			startActivity(new Intent(this,CollectActivity.class));
		}else if(v.getId()==R.id.txtFollow){
			startActivity(new Intent(this,FollowActivity.class));
		}else if(v.getId()==R.id.txtOffLine){
			getHandlerContext().makeTextLong("离线阅读");
		}else if(v.getId()==R.id.txtMode){
			getHandlerContext().makeTextLong("夜间模式");
		}else if(v.getId()==R.id.txtTextMode){
			getHandlerContext().makeTextLong("文字模式");
		}else if(v.getId()==R.id.txtSearch){
			getHandlerContext().makeTextLong("搜索");
		}else if(v.getId()==R.id.txtSetting){
			startActivity(new Intent(this,SettingActivity.class));
		}else{
			super.onClick(v);
		}
	}
	
}
