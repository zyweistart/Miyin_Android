package start.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;

public class CommonFn {

	/**
	 * 获取本机号码
	 */
	public static String getPhoneNumber(Context context) {
		// 创建电话管理器
		TelephonyManager mTelephonyMgr;
		// 获取系统固定号码
		mTelephonyMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		// 返回手机号码
		String phone = mTelephonyMgr.getLine1Number();
		if (phone == null) {
			return "";
		} else if (phone.length() == 11) {
			return phone;
		}
		return "";
	}

	/**
	 * 获取App安装包信息
	 */
	public static PackageInfo getPackageInfo(Context context) {
		PackageInfo info = null;
		try { 
			info =context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {    
			e.printStackTrace(System.err);
		} 
		if(info == null) info = new PackageInfo();
		return info;
	}
	
	/**
	 * 判断应用是否处于后台
	 */
	public static boolean isBackground(Context context) {
		ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

}