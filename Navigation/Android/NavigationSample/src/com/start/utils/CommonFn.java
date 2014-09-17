package com.start.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

import com.start.navigation.R;
import com.start.widget.POIDialog;

public class CommonFn {

	public static ProgressDialog progressDialog(Context context,String message){
		ProgressDialog dialog = new ProgressDialog(context);
		if(message!=null){
			dialog.setMessage(message);
		}
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		return dialog;
	}
	
	/**
	 * 弹出网络设置对话框
	 */
	public static void settingNetwork(final Context context){
		AlertDialog.Builder aDialog = new AlertDialog.Builder(context);
		aDialog.
		setIcon(android.R.drawable.ic_dialog_info).
		setMessage(R.string.msg_network_connecterror).
		setCancelable(false).
		setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).setNeutralButton(R.string.settings, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent netIntent=new Intent(Settings.ACTION_SETTINGS);
				context.startActivity(netIntent);
			}
		}).show();
	}
	
	/**
	 * 检测网络连接
	 */
	public static boolean checkNet(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	public static AlertDialog alertDialog(Context context, int resId, DialogInterface.OnClickListener positiveButtonListener) {
		return alertDialog(context, context.getString(resId), positiveButtonListener);
	}
	
	public static AlertDialog alertDialog(Context context, String message, DialogInterface.OnClickListener positiveButtonListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setCancelable(false);
		builder.setPositiveButton(context.getString(R.string.ok), positiveButtonListener);
		return builder.create();
	}
	
	public static Dialog createPOIDialog(Context context) {
		POIDialog poiDialog = new POIDialog(context, R.style.dialog_poi);
		Window win = poiDialog.getWindow();
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		win.setAttributes(params);
		poiDialog.setCanceledOnTouchOutside(true);
		return poiDialog;
	}
	
	public static Bitmap convertToBitmap(File file){
		InputStream is=null;
		try {
			is = new FileInputStream(file);
			return BitmapFactory.decodeStream(is);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}finally{
			close(is);
		}
	}
	
	public static void close(Closeable closable) {
		if (closable != null) {
			try {
				closable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				closable=null;
			}
		}
	}
	
}