package com.ancun.yzb;

import start.core.AppConstant;
import start.core.AppContext;
import start.utils.GetMarketUri;
import start.utils.StringUtils;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ancun.core.BaseActivity;
import com.ancun.core.Constant;
import com.ancun.service.AppService;

public class SettingActivity extends BaseActivity {

	//退出应用返回码
	public static final int RESULTQUITAPP=0xABC991;
	
	//手势密码设置
	private Boolean gestureState;
	private Drawable ic_gesture;
	private Drawable img_off;
	private Drawable img_on;
	private TextView setting_gesturesetting;
	private TextView setting_gesturemodify;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		setMainHeadTitle(getString(R.string.setting));
		
		//手势密码设置
		setting_gesturesetting=(TextView)findViewById(R.id.setting_gesturesetting);
		setting_gesturemodify=(TextView)findViewById(R.id.setting_gesturemodify);
		Resources res=getResources();
		ic_gesture= res.getDrawable(R.drawable.ic_share);
		ic_gesture.setBounds(0, 0, ic_gesture.getMinimumWidth(), ic_gesture.getMinimumHeight());
		img_off= res.getDrawable(R.drawable.state_off);
		img_off.setBounds(0, 0, img_off.getMinimumWidth(), img_off.getMinimumHeight());
		img_on= res.getDrawable(R.drawable.state_no);
		img_on.setBounds(0, 0, img_on.getMinimumWidth(), img_on.getMinimumHeight());
		String key = AppContext.getSharedPreferences().getString(Constant.Preferences.SP_LOCK_KEY_DATA, AppConstant.EMPTYSTR);
		if (StringUtils.isEmpty(key)) {
			gestureState=false;
			 setting_gesturesetting.setCompoundDrawables(ic_gesture, null, img_off, null);
			 setting_gesturemodify.setVisibility(View.GONE);
		}else{
			gestureState=true;
			 setting_gesturesetting.setCompoundDrawables(ic_gesture, null, img_on, null);
			 setting_gesturemodify.setVisibility(View.VISIBLE);
		}
		
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.setting_tip) {
			// 小贴士
			startActivity(new Intent(this, TipActivity.class));
		} else if (v.getId() == R.id.setting_feedback) {
			// 意见反馈
			startActivity(new Intent(this, FeedbackActivity.class));
		} else if (v.getId() == R.id.setting_versioncheck) {
			// 新版本检测
			AppService.checkAppUpdate(this, false);
		} else if (v.getId() == R.id.setting_softwareshare) {
			// 软件分享
			getHandlerContext().makeTextLong("软件分享");
		} else if (v.getId() == R.id.setting_comment) {
			// 给个好评吧
			Intent iMarket = GetMarketUri.getIntent(this);
			if (!GetMarketUri.judge(this, iMarket)) {
				startActivity(iMarket);
			}
		} else if (v.getId() == R.id.setting_follow) {
			// 关注安存语录
			getHandlerContext().makeTextLong("关注安存语录");
		} else if (v.getId() == R.id.setting_aboutus) {
			// 关于我们
			startActivity(new Intent(this, AboutusActivity.class));
		} else if (v.getId() == R.id.setting_cleanrecording) {
			// 清理本地录音缓存
			AppService.cleanrecording(this,getHandlerContext());
		} else if (v.getId() == R.id.setting_modifypassword) {
			// 修改密码
			startActivity(new Intent(this, ModifyPasswordActivity.class));
		} else if (v.getId() == R.id.setting_gesturesetting) {
			// 手势密码设置
			String key = AppContext.getSharedPreferences().getString(Constant.Preferences.SP_LOCK_KEY_DATA, AppConstant.EMPTYSTR);
			if (StringUtils.isEmpty(key)) {
				if(gestureState=!gestureState){
					setting_gesturesetting.setCompoundDrawables(ic_gesture, null, img_on, null);
					setting_gesturemodify.setVisibility(View.VISIBLE);
					 startActivity(new Intent(this, LockSetupActivity.class));
					 return;
				}
			}
			//clear old gesture password
			AppContext.getSharedPreferences().putString(Constant.Preferences.SP_LOCK_KEY_DATA, AppConstant.EMPTYSTR);
			setting_gesturesetting.setCompoundDrawables(ic_gesture, null, img_off, null);
			setting_gesturemodify.setVisibility(View.GONE);
		} else if (v.getId() == R.id.setting_gesturemodify) {
			// 手势密码修改
			startActivity(new Intent(this,LockSetupActivity.class));
		} else if (v.getId() == R.id.setting_exitlogin) {
			// 退出
			new AlertDialog.Builder(this)
			.setMessage(R.string.exitlogin_sure)
			.setPositiveButton(R.string.cancle, null)
			.setNegativeButton(R.string.sure,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int whichButton) {
							setResult(RESULTQUITAPP);
							finish();
						}
			}).show();
		} else {
			super.onClick(v);
		}
	}

}