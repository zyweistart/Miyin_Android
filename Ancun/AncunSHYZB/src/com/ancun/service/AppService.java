package com.ancun.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import start.core.AppException;
import start.core.HandlerContext;
import start.service.HttpRunnable;
import start.service.HttpServer;
import start.service.Response;
import start.utils.StringUtils;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.ancun.core.BaseActivity;
import com.ancun.core.Constant;
import com.ancun.core.Constant.Preferences;
import com.ancun.shyzb.BaseContext;
import com.ancun.shyzb.LockSetupActivity;
import com.ancun.shyzb.R;
import com.ancun.shyzb.layout.CallRecordsContentView;
import com.ancun.shyzb.layout.DialContentView;
import com.ancun.shyzb.layout.RecordingContentView;

/**
 * 应用服务类
 * @author start
 *
 */
public class AppService {
	
	public static void inAppDial(final BaseActivity activity,String dial){
		if(TextUtils.isEmpty(dial)){
			return;
		}
		final String phone=StringUtils.phoneFormat(dial);
		if (Constant.noCall.contains(phone)) {
			call(activity, phone);
		} else {
			HttpServer hServer=new HttpServer(Constant.URL.phoneCall, activity.getHandlerContext());
			Map<String,String> headers=new HashMap<String,String>();
			headers.put("sign", User.ACCESSKEY);
			hServer.setHeaders(headers);
			Map<String,String> params=new HashMap<String,String>();
			params.put("accessid", User.ACCESSID);
			params.put("userTel", activity.getAppContext().currentUser().getPhone());
			params.put("oppno",phone);
			hServer.setParams(params);
			hServer.get(new HttpRunnable() {
				
				@Override
				public void run(Response response) throws AppException {
					Map<String,String> info=response.getMapData("serverinfo");
					RecordingContentView.isRefreshData=true;
					BaseContext.getSharedPreferences().putString(Constant.Preferences.SP_CALL_DIAL,phone);
					call(activity, info.get("serverno"));
					activity.getRecentDaoImpl().insertCallLog(phone);
				}
				
			});
			
		}
	}
	
	public static void call(BaseActivity activity,String phone){
		if(TextUtils.isEmpty(phone)){
			return;
		}
		DialContentView.isRefreshData=true;
		CallRecordsContentView.isRefreshData=true;
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +phone));
		activity.startActivity(intent);
	}
	
	public static void cleanrecording(final Context context,final HandlerContext handlercontext){
		new AlertDialog.Builder(context)
		.setMessage(R.string.cleanrecording_sure)
		.setPositiveButton(R.string.cancle, null)
		.setNegativeButton(R.string.sure,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int whichButton) {
						final ProgressDialog pDialog = new ProgressDialog(context);
						pDialog.setMessage(context.getString(R.string.wait));
						pDialog.setIndeterminate(true);
						pDialog.setCancelable(false);
						pDialog.show();
						try {
							File dirFile = new File(BaseContext.getInstance().getStorageDirectory(Constant.RECORDDIRECTORY));
							if (dirFile.exists()&& dirFile.isDirectory()) {
								File recordFiles[] = dirFile.listFiles(); // 声明目录下所有的文件// files[];
								for (int i = 0; i < recordFiles.length; i++) {// 遍历目录下所有的文件
									recordFiles[i].delete();// 删除
								}
							}
							handlercontext.makeTextLong(context.getString(R.string.cleanrecording_success));
						} catch (Exception e) {
							handlercontext.makeTextLong(context.getString(R.string.cleanrecording_fail));
						} finally {
							pDialog.dismiss();
						}
					}
				}).show();
	}
	
	/**
	 * 检测应用更新
	 */
	public static void checkAppUpdate(BaseActivity activity,Boolean status){
		UpdateApplication updateApplication = new UpdateApplication(activity);
		updateApplication.startCheck(status);
	}

	/**
	 * 重置手势密码
	 */
	public static void resetGesture(BaseActivity activity){
		if(BaseContext.getSharedPreferences().getBoolean(Preferences.SP_IS_RESET_LOCK_KEY, false)){
			activity.startActivity(new Intent(activity, LockSetupActivity.class));
			BaseContext.getSharedPreferences().putBoolean(Preferences.SP_IS_RESET_LOCK_KEY, false);
		}
	}
	
	/**
	 * 密码规则检测
	 */
	public static boolean passwordCheck(String password){
		if(TextUtils.isEmpty(password)){
			return false;
		}
		if(password.length()<6||password.length()>16){
			return false;
		}
		//不能全为数字
		Pattern pattern = Pattern.compile("[0-9]*"); 
	    if(pattern.matcher(password).matches()){
	    	return false;
	    }
	    //不能全为字母
	    if(password.matches("^[a-zA-Z]*")){
	    	return false;
	    }
		return true;
	}
	
}
