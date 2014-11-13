package com.start.zmcy;

import start.utils.LogUtils;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.start.core.BaseActivity;

/**
 * 设置
 */
public class SettingActivity extends BaseActivity{
	
	private TextView mTxtSizeSetting;
	private TextView mTxtVersion;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		setMainHeadTitle(getString(R.string.setting));
		
		mTxtSizeSetting=(TextView)findViewById(R.id.txtSizeSetting);
		mTxtVersion=(TextView)findViewById(R.id.txtVersion);
		
		mTxtSizeSetting.setText(getString(R.string.sizesetting)+"   中");
		try {
			PackageManager packageManager = getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
			mTxtVersion.setText(getString(R.string.version)+"   v"+packInfo.versionName);
		} catch (NameNotFoundException e) {
			LogUtils.logError(e);
		}
 	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.txtBindShared){
			startActivity(new Intent(this,BindSharedActivity.class));
		}else if(v.getId()==R.id.txtSizeSetting){
			getHandlerContext().makeTextLong("字体设置");
		}else if(v.getId()==R.id.txtPushSetting){
			startActivity(new Intent(this,PushSettingActivity.class));
		}else if(v.getId()==R.id.txtCleanCache){
			getHandlerContext().makeTextLong("清理缓存");
		}else if(v.getId()==R.id.txtVersion){
			getHandlerContext().makeTextLong("版本检测");
		}else if(v.getId()==R.id.txtAboutus){
			startActivity(new Intent(this,AboutUsActivity.class));
		}else{
			super.onClick(v);
		}
	}
	
}