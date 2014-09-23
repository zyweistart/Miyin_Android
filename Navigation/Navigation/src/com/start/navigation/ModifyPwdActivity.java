package com.start.navigation;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.start.core.Constant;
import com.start.core.CoreActivity;
import com.start.model.UIRunnable;
import com.start.utils.CommonFn;
import com.start.utils.MD5;

/**
 * 忘记密码
 * @author start
 *
 */

public class ModifyPwdActivity extends CoreActivity implements OnClickListener {

	private EditText etOldPassword;
	private EditText etNewPassword;
	private EditText etReNewPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_pwd);
		setCurrentActivityTitle(R.string.activity_title_modify_pwd);
		
		etOldPassword=(EditText)findViewById(R.id.modify_et_old_password);
		etNewPassword=(EditText)findViewById(R.id.modify_et_new_password);
		etReNewPassword=(EditText)findViewById(R.id.modify_et_re_new_password);
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(!getAppContext().isLogin()){
			new AlertDialog.Builder(this)
			.setIcon(android.R.drawable.ic_dialog_info)
			.setMessage(R.string.msg_not_login)
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					startActivity(new Intent(ModifyPwdActivity.this,LoginActivity.class));
				}
			}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					finish();
				}
			}).show();
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.modify_pwd_btn_ok){
			final String oldPassword=String.valueOf(etOldPassword.getText());
			final String newPassword=String.valueOf(etNewPassword.getText());
			final String reNewPassword=String.valueOf(etReNewPassword.getText());
			if(TextUtils.isEmpty(oldPassword)){
				makeTextLong(R.string.msg_old_password_not_empty);
			}else if(TextUtils.isEmpty(newPassword)){
				makeTextLong(R.string.msg_password_not_empty);
			}else if(!newPassword.equals(reNewPassword)){
				makeTextLong(R.string.msg_two_password_not_diff);
			}else{
				Map<String,String> requestParams=new HashMap<String,String>();
				requestParams.put("pwdo", MD5.md5(oldPassword));
				requestParams.put("pwdn", MD5.md5(newPassword));
				getHttpService().exeNetRequest(Constant.ServerAPI.userpwdMod,requestParams,null,new UIRunnable() {
					
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								
								CommonFn.alertDialog(ModifyPwdActivity.this, R.string.msg_modifypwd_success,new DialogInterface.OnClickListener(){

									@Override
									public void onClick(DialogInterface dialog,int which) {
										//如果为自动登录则更新密码
										if(getAppContext().getSharedPreferencesUtils().getBoolean(Constant.SharedPreferences.LOGIN_AUTOLOGIN, false)){
											getAppContext().getSharedPreferencesUtils().putString(Constant.SharedPreferences.LOGIN_PASSWORD,newPassword );
										}
										finish();
									}
									
								}).show();
							}
						});
					}
				});
			}
		}
	}

}