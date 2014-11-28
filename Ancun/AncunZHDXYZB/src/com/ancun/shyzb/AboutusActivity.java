package com.ancun.shyzb;

import start.utils.LogUtils;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

import com.ancun.core.BaseActivity;

public class AboutusActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutus);
		setMainHeadTitle(getString(R.string.aboutus));
		try {
			PackageManager packageManager = getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
			int versionCode=packInfo.versionCode;
			ApplicationInfo appInfo = this.getPackageManager().getApplicationInfo(getPackageName(),PackageManager.GET_META_DATA);
			LogUtils.logInfo("当前版本:"+versionCode+"\t友盟渠道:"+appInfo.metaData.getString("UMENG_CHANNEL"));
		} catch (NameNotFoundException e) {
			LogUtils.logError(e);
		}
	}
}
