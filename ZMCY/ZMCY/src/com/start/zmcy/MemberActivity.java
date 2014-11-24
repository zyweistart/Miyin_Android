package com.start.zmcy;

import start.core.AppContext;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.core.Constant.Preferences;

/**
 * 成员
 */
public class MemberActivity extends BaseActivity{
	
	private TextView txt_account_info;
	private TextView txtMode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member);
		setMainHeadTitle(getString(R.string.member));
		txt_account_info=(TextView)findViewById(R.id.txt_account_info);
		txtMode=(TextView)findViewById(R.id.txtMode);
		setModeText();
 	}
	
	@Override
	protected void onResume() {
		super.onResume();
		loginStatusText();
	}
	
	public void loginStatusText(){
		if(getAppContext().currentUser().isLogin()){
			txt_account_info.setText(R.string.exitlogin);
		}else{
			txt_account_info.setText(R.string.gotologin);
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.txt_account_info){
			if(getAppContext().currentUser().isLogin()){
				getAppContext().currentUser().clearCacheUser();
				loginStatusText();
			}else{
				getAppContext().getCacheActivity().setIntent(new Intent(this,MemberActivity.class));
				startActivity(new Intent(this,LoginActivity.class));
			}
		}else if(v.getId()==R.id.txtSigin){
			if(!getAppContext().currentUser().isLogin()){
				goLogin(new Intent(this,MemberActivity.class),getString(R.string.nologin));
				return;
			}
		}else if(v.getId()==R.id.txtCollect){
			Intent intent=new Intent(this,CollectActivity.class);
			if(!getAppContext().currentUser().isLogin()){
				goLogin(intent,getString(R.string.nologin));
				return;
			}
			startActivity(intent);
		}else if(v.getId()==R.id.txtFollow){
			Intent intent=new Intent(this,FollowActivity.class);
			if(!getAppContext().currentUser().isLogin()){
				goLogin(intent,getString(R.string.nologin));
				return;
			}
			startActivity(intent);
		}else if(v.getId()==R.id.txtOffLine){
			getHandlerContext().makeTextLong("离线阅读");
		}else if(v.getId()==R.id.txtMode){
			Boolean mode=AppContext.getSharedPreferences().getBoolean(Preferences.SP_READ_MODE, false);
			AppContext.getSharedPreferences().putBoolean(Preferences.SP_READ_MODE, !mode);
			setModeText();
		}else if(v.getId()==R.id.txtTextMode){
			getHandlerContext().makeTextLong("文字模式");
		}else if(v.getId()==R.id.txtSearch){
			startActivity(new Intent(this,NewsSearchActivity.class));
		}else if(v.getId()==R.id.txtSetting){
			startActivity(new Intent(this,SettingActivity.class));
		}else{
			super.onClick(v);
		}
	}
	
	public Boolean setModeText(){
		Boolean mode=AppContext.getSharedPreferences().getBoolean(Preferences.SP_READ_MODE, false);
		if(mode){
			txtMode.setText(R.string.daymode);
		}else{
			txtMode.setText(R.string.nightmode);
		}
		return mode;
	}
	
}
