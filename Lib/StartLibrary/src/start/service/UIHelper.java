package start.service;

import start.core.AppManager;
import start.core.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

/**
 * 应用程序UI工具包：封装UI相关的一些操作
 */
public class UIHelper {
	
	/**
	 * 发送App异常崩溃报告
	 */
	public static void sendAppCrashReport(final Context cont,final String crashReport) {
		AlertDialog.Builder builder = new AlertDialog.Builder(cont);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(R.string.app_error);
		builder.setMessage(R.string.app_error_message);
		builder.setPositiveButton(R.string.submit_report,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// 发送异常报告
						Intent i = new Intent(Intent.ACTION_SEND);
						// i.setType("text/plain"); //模拟器
						i.setType("message/rfc822"); // 真机
						i.putExtra(Intent.EXTRA_EMAIL,
								new String[] { "zhangdeyi@oschina.net" });
						i.putExtra(Intent.EXTRA_SUBJECT,"智能医疗Android客户端 - 错误报告");
						i.putExtra(Intent.EXTRA_TEXT, crashReport);
						cont.startActivity(Intent.createChooser(i, "发送错误报告"));
						// 退出
						AppManager.getInstance().AppExit(cont);
					}
				});
		builder.setNegativeButton(R.string.sure,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// 退出
						AppManager.getInstance().AppExit(cont);
					}
				});
		builder.show();
	}
	
	/**
	 * 弹出网络设置对话框
	 */
	public static void goSettingNetwork(final Context context){
		AlertDialog.Builder aDialog = new AlertDialog.Builder(context);
		aDialog.
		setIcon(android.R.drawable.ic_dialog_info).
		setTitle(R.string.prompt).
		setMessage(R.string.network_not_connected).
		setPositiveButton(R.string.cancle, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).setNeutralButton(R.string.setting, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent netIntent=new Intent(Settings.ACTION_SETTINGS);
				context.startActivity(netIntent);
			}
		}).show();
	}
	
}
