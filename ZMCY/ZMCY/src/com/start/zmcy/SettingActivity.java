package com.start.zmcy;

import java.io.File;

import start.core.AppContext;
import start.utils.LogUtils;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.core.Constant.Preferences;

/**
 * 设置
 */
public class SettingActivity extends BaseActivity {

	private TextView mTxtSizeSetting;
	private TextView mTxtVersion;

	private String[] mFontSizeType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		setMainHeadTitle(getString(R.string.setting));
		
		mFontSizeType=getResources().getStringArray(R.array.fontsizetype);
		
		mTxtSizeSetting = (TextView) findViewById(R.id.txtSizeSetting);
		mTxtVersion = (TextView) findViewById(R.id.txtVersion);

		mTxtSizeSetting.setText(getString(R.string.sizesetting) + "   " + mFontSizeType[AppContext.getSharedPreferences().getInteger(Preferences.SP_SIZE_SETTING, 0)]);
		try {
			PackageManager packageManager = getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
			mTxtVersion.setText(getString(R.string.version) + "   v"+ packInfo.versionName);
		} catch (NameNotFoundException e) {
			LogUtils.logError(e);
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.txtBindShared) {
			startActivity(new Intent(this, BindSharedActivity.class));
		} else if (v.getId() == R.id.txtSizeSetting) {
			new AlertDialog.Builder(this).setSingleChoiceItems(mFontSizeType, AppContext.getSharedPreferences().getInteger(Preferences.SP_SIZE_SETTING, 0),
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							AppContext.getSharedPreferences().putInteger(Preferences.SP_SIZE_SETTING, which);
							mTxtSizeSetting.setText(getString(R.string.sizesetting)+ "   " + mFontSizeType[which]);
							dialog.dismiss();
						}
					}).show();
		} else if (v.getId() == R.id.txtPushSetting) {
			Intent intent=new Intent(this,PushSettingActivity.class);
			if(!getAppContext().currentUser().isLogin()){
				goLogin(intent,getString(R.string.nologin));
				return;
			}
			startActivity(intent);
		} else if (v.getId() == R.id.txtCleanCache) {
			new AlertDialog.Builder(this)
			.setMessage(R.string.cleanfile_sure)
			.setPositiveButton(R.string.cancle, null)
			.setNegativeButton(R.string.sure,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int whichButton) {
							ProgressDialog pDialog = new ProgressDialog(SettingActivity.this);
							pDialog.setMessage(getString(R.string.wait));
							pDialog.setIndeterminate(true);
							pDialog.setCancelable(false);
							pDialog.show();
							try {
								File dirFile = new File(BaseContext.getInstance().getStorageDirectory("temp"));
								if (dirFile.exists()&& dirFile.isDirectory()) {
									File recordFiles[] = dirFile.listFiles(); // 声明目录下所有的文件// files[];
									for (int i = 0; i < recordFiles.length; i++) {// 遍历目录下所有的文件
										recordFiles[i].delete();// 删除
									}
								}
								getHandlerContext().makeTextLong(getString(R.string.cleanfile_success));
							} catch (Exception e) {
								getHandlerContext().makeTextLong(getString(R.string.cleanfile_fail));
							} finally {
								pDialog.dismiss();
							}
						}
					}).show();
		} else if (v.getId() == R.id.txtVersion) {
			getHandlerContext().makeTextLong("版本检测");
		} else if (v.getId() == R.id.txtAboutus) {
			startActivity(new Intent(this, AboutUsActivity.class));
		} else {
			super.onClick(v);
		}
	}

}