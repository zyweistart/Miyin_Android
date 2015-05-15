package com.ancun.shyzb;

import start.utils.LogUtils;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

import com.ancun.core.BaseActivity;

public class AboutusActivity extends BaseActivity {

	private TextView setting_about_us_version;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutus);
		setMainHeadTitle(getString(R.string.aboutus));
		setting_about_us_version=(TextView)findViewById(R.id.setting_about_us_version);
		try {
			PackageManager packageManager = getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
			int versionCode=packInfo.versionCode;
			ApplicationInfo appInfo = this.getPackageManager().getApplicationInfo(getPackageName(),PackageManager.GET_META_DATA);
			setting_about_us_version.setText("版本:"+versionCode+"\n渠道:"+appInfo.metaData.getString("UMENG_CHANNEL"));
			LogUtils.logInfo("当前版本:"+versionCode+"\t友盟渠道:"+appInfo.metaData.getString("UMENG_CHANNEL"));
		} catch (NameNotFoundException e) {
			LogUtils.logError(e);
		}
	}
}
