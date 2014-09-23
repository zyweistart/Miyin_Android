package com.start.navigation;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.start.core.Constant;
import com.start.core.CoreActivity;
import com.start.model.UIRunnable;

/**
 * 设置
 * 
 * @author start
 * 
 */
public class MoreActivity extends CoreActivity implements OnClickListener {

	
	private LinearLayout llLogin;
	private LinearLayout llLogout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		setCurrentActivityTitle(R.string.activity_title_more);
		
		llLogin=(LinearLayout)findViewById(R.id.more_login);
		llLogout=(LinearLayout)findViewById(R.id.more_logout);
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(getAppContext().isLogin()){
			llLogin.setVisibility(View.GONE);
			llLogout.setVisibility(View.VISIBLE);
		}else{
			llLogin.setVisibility(View.VISIBLE);
			llLogout.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.more_login) {
			//用户登录
			startActivity(new Intent(this,LoginActivity.class));
		} else if (v.getId() == R.id.more_logout) {
			//用户退出
			new AlertDialog.Builder(MoreActivity.this)
			.setIcon(android.R.drawable.ic_dialog_info)
			.setMessage(R.string.msg_you_are_sure_logout)
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					
					Map<String,String> requestParams=new HashMap<String,String>();
					getHttpService().exeNetRequest(Constant.ServerAPI.userLogout,requestParams,null,new UIRunnable() {
						
						@Override
						public void run() {
							
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									Boolean isChecked=getAppContext().getSharedPreferencesUtils().getBoolean(Constant.SharedPreferences.LOGIN_AUTOLOGIN, false);
									if(isChecked){
										getAppContext().getSharedPreferencesUtils().putBoolean(Constant.SharedPreferences.LOGIN_AUTOLOGIN, false);
										getAppContext().getSharedPreferencesUtils().putString(Constant.SharedPreferences.LOGIN_ACCOUNT, Constant.EMPTYSTR);
										getAppContext().getSharedPreferencesUtils().putString(Constant.SharedPreferences.LOGIN_PASSWORD, Constant.EMPTYSTR);
									}
									getAppContext().initUserInfo(null);
									llLogin.setVisibility(View.VISIBLE);
									llLogout.setVisibility(View.GONE);
								}
								
							});
						}
					});
					
				}
			}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();
				}
			}).show();
			
		} else if (v.getId() == R.id.more_friend_relation_manager) {
			//好友管理
			startActivity(new Intent(this,FriendRelationListActivity.class));
		} else if (v.getId() == R.id.more_data_file_manager) {
			//地图管理
			startActivity(new Intent(this,MapDataListActivity.class));
		} else if (v.getId() == R.id.more_new_version_check) {
			//版本检测
			Map<String,String> requestParams=new HashMap<String,String>();
			requestParams.put("accessid",Constant.ACCESSID_LOCAL);
			requestParams.put("termtype","1");
			Map<String,String> headerParams=new HashMap<String,String>();
			headerParams.put("sign",Constant.ACCESSKEY_LOCAL);
			getHttpService().exeNetRequest(Constant.ServerAPI.appverGet,requestParams,headerParams,new UIRunnable() {
				
				@Override
				public void run() {
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							Map<String,String> data=getContent().get("appverinfo");
							final Integer maxVersion=Integer.parseInt(data.get("maxversionno"));
							final Integer minVersion=Integer.parseInt(data.get("minversionno"));
							final String url=data.get("site");
							int currentVersionCode=getAppContext().getCurrentVersionCode();
							if(minVersion>currentVersionCode){
								new AlertDialog.Builder(MoreActivity.this)
								.setMessage(R.string.msg_have_new_version_1)
								.setCancelable(false)
								.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
										Intent fIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
										startActivity(fIntent);
										setResult(Constant.ActivityResultCode.EXITAPP);
										finish();
									}
								}).show();
							}else if(maxVersion>currentVersionCode){
								new AlertDialog.Builder(MoreActivity.this)
								.setIcon(android.R.drawable.ic_dialog_info)
								.setMessage(R.string.msg_have_new_version_2)
								.setPositiveButton(R.string.msg_upgrade_now, new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
										Intent fIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
										startActivity(fIntent);
									}
								}).setNegativeButton(R.string.msg_later, new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
										dialog.dismiss();
									}
								}).show();
							}else{
								makeTextLong(R.string.msg_last_version);
							}
						}
						
					});
				}
			});
		}else if(v.getId()==R.id.more_modifypwd){
			startActivity(new Intent(this,ModifyPwdActivity.class));
		}else if(v.getId()==R.id.more_btn_exit_app){
			
			new AlertDialog.Builder(MoreActivity.this)
			.setIcon(android.R.drawable.ic_dialog_info)
			.setMessage(R.string.msg_sure_exit_app)
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					setResult(Constant.ActivityResultCode.EXITAPP);
					finish();
				}
			}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();
				}
			}).show();
			
		}
	}

}
