package com.start.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import start.core.AppConstant;
import start.core.AppContext;
import start.core.AppException;
import start.core.HandlerContext;
import start.service.HttpServer;
import start.service.Response;
import start.service.UIHelper;
import start.service.UIRunnable;
import start.utils.LogUtils;
import start.utils.NetConnectManager;
import start.utils.SharedPreferencesUtils;
import start.utils.StringUtils;
import start.utils.TimeUtils;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import com.start.core.BaseActivity;
import com.start.core.Constant;
import com.start.medical.R;

/**
 * 应用更新
 * @author Start
 * 
 */
public class UpdateApplication {

	private BaseActivity currentActivity;
	private SharedPreferencesUtils sharedPreferencesUtil;
	private HandlerContext mHandlerContext;

	public UpdateApplication(BaseActivity activity) {
		currentActivity = activity;
		mHandlerContext=currentActivity.getHandlerContext();
		sharedPreferencesUtil=AppContext.getSharedPreferences();
	}

	/**
	 * 开始检测
	 */
	public void startCheck(final boolean backgroundCheckFlag) {
		if (NetConnectManager.isNetworkConnected(currentActivity)) {
			HttpServer hServer=new HttpServer("", mHandlerContext);
			hServer.get(new UIRunnable() {
				
				@Override
				public void run(Response response) throws AppException {
					try {
						final Integer minver = Integer.parseInt(response.getMapData().get("minver"));
						final Integer nowver =Integer.parseInt(response.getMapData().get("nowver"));
						final String description = response.getMapData().get("description");
						final String url = response.getMapData().get("url");
						PackageInfo pi=currentActivity.getPackageManager().getPackageInfo(currentActivity.getPackageName(), 0); 
						int currentVersionCode =pi.versionCode;
						if (minver > currentVersionCode) {
							mHandlerContext.getHandler().post(new Runnable() {
										@Override
										public void run() {
											new AlertDialog.Builder(currentActivity)
													.setMessage(description)
													.setCancelable(false)
													.setPositiveButton(R.string.sure,
															new DialogInterface.OnClickListener() {
																public void onClick(DialogInterface dialog,int whichButton) {
																	Intent fIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
																	currentActivity.startActivity(fIntent);
																	currentActivity.finish();
																}
															}).show();
										}
									});
						} else if (nowver > currentVersionCode) {
							try {
								if(backgroundCheckFlag){
									SimpleDateFormat sdf = new SimpleDateFormat(TimeUtils.yyyyMMdd_F);
									//当前检测日期
									String currentCheckDateStr=TimeUtils.getSysdate();
									//最后检测日期
									String lastCheckDateStr = sharedPreferencesUtil.getString(Constant.Preferences.SP_VERSION_DATA,AppConstant.EMPTYSTR);
									if(StringUtils.isEmpty(lastCheckDateStr)){
										lastCheckDateStr=currentCheckDateStr;
										sharedPreferencesUtil.putString(Constant.Preferences.SP_VERSION_DATA,lastCheckDateStr);
									}
									long day = (sdf.parse(currentCheckDateStr).getTime()-sdf.parse(lastCheckDateStr).getTime())/24/60/60/1000;
									if (day>3) {
										//设置最后检测的日期
										sharedPreferencesUtil.putString(Constant.Preferences.SP_VERSION_DATA,currentCheckDateStr);
									}else{
										return;
									}
								}
								mHandlerContext.getHandler().post(new Runnable() {
									@Override
									public void run() {
										new AlertDialog.Builder(currentActivity)
												.setIcon(android.R.drawable.ic_dialog_info)
												.setMessage(description)
												.setPositiveButton("以后再说",
														new DialogInterface.OnClickListener() {
															public void onClick(DialogInterface dialog,int whichButton) {
																 dialog.dismiss();
															}
														})
												.setNegativeButton("立即升级",
														new DialogInterface.OnClickListener() {
															public void onClick(DialogInterface dialog,int whichButton) {
																 new DownloadAppTask().execute(url);
															}
														}).show();
									}
								});
							} catch (Exception e) {
								LogUtils.logError(e);
							}
						}else{
							if(!backgroundCheckFlag){
								mHandlerContext.makeTextLong("已经是最新版本了");
							}
						}
					} catch (NameNotFoundException e) {
						throw AppException.io(e);
					}
				}
			});
		}else{
			if(!backgroundCheckFlag){
				UIHelper.goSettingNetwork(currentActivity);
			}
		}
	}

	/**
	 * 应用更新下载
	 */
	private class DownloadAppTask extends AsyncTask<String, Float, File> {

		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// 创建ProgressDialog对象
			pDialog = new ProgressDialog(currentActivity);
			// 设置进度条风格，风格为圆形，旋转的
			pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			// 设置ProgressDialog 标题
			pDialog.setTitle("提示");
			// 设置ProgressDialog提示信息
			pDialog.setMessage("正在下载中......");
			// 设置ProgressDialog标题图标
			// pDialog.setIcon(R.drawable.img2);
			// 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
			pDialog.setIndeterminate(false);
			// 设置ProgressDialog 进度条进度
			pDialog.setProgress(100);
			// 设置ProgressDialog 是否可以按退回键取消
			pDialog.setCancelable(false);
			pDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					onCancelled();
				}
			});
			pDialog.show();
		}

		@Override
		protected void onCancelled() {
			cancel(true);
		}

		@Override
		protected File doInBackground(String... params) {
			final String url = params[0];
			// 主目录创建
			File dirFile = new File(Environment.getExternalStorageDirectory().getPath() + "/ancun/tmp/");
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			File downFile = new File(dirFile, "ancunyulu");
			if (downFile.exists()) {
				// 如果已经存在则删除原先的文件
				downFile.delete();
			}
			try {
				File tmpFile = new File(dirFile, "ancunyulu" + ".tmp");
				if (tmpFile.exists()) {
					tmpFile.delete();
				}
				HttpClient client = new DefaultHttpClient();
				// 设置超时时间为10秒
				client.getParams().setParameter(
						CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
				HttpPost post = new HttpPost(url);
				HttpResponse response = client.execute(post);
				if (response != null
						&& response.getStatusLine().getStatusCode() == 200) {
					int len = -1;
					long reallen = 0;
					// 获取下载文件的总大小
					long totallen = response.getEntity().getContentLength();
					InputStream is = null;
					FileOutputStream fos = null;
					byte[] buffer = new byte[1024 * 8];
					try {
						is = response.getEntity().getContent();
						fos = new FileOutputStream(tmpFile);
						while ((len = is.read(buffer)) != -1 && !isCancelled()) {
							fos.write(buffer, 0, len);
							reallen += len;
							publishProgress((float) reallen / totallen);
						}
					} catch (Exception e) {
						mHandlerContext.makeTextLong(e.getMessage());
					} finally {
						if (fos != null) {
							try {
								fos.flush();
							} catch (IOException e) {
								mHandlerContext.makeTextLong(e.getMessage());
							} finally {
								try {
									fos.close();
								} catch (IOException e) {
									mHandlerContext.makeTextLong(e.getMessage());
								} finally {
									fos = null;
								}
							}
						}
						buffer = null;
						if (isCancelled()) {
							tmpFile.delete();
						} else {
							if (tmpFile.length() == totallen) {
								tmpFile.renameTo(downFile);
							} else {
								// getAppContext().makeTextLong("文件大小不一致");
								mHandlerContext.makeTextLong("下载发生异常，请重试");
								tmpFile.delete();
							}
						}
					}
				} else {
					// getAppContext().makeTextLong("文件下载失败错误状态码："+response.getStatusLine().getStatusCode());
					mHandlerContext.makeTextLong("下载发生异常，请重试");
				}
			} catch (Exception e) {
				mHandlerContext.makeTextLong("下载发生异常，请重试");
				// getAppContext().makeTextLong("请求出现异常，请稍候再试！");
			}
			return downFile;
		}

		@Override
		protected void onProgressUpdate(Float... values) {
			// 更新下载进度条
			pDialog.setProgress(Math.round(values[0] * 100f));
		}

		@Override
		protected void onPostExecute(File result) {
			pDialog.dismiss();
			if (result.exists()) {
				// 安装应用
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(result),"application/vnd.android.package-archive");
				currentActivity.startActivity(intent);
				currentActivity.finish();
			}
		}
	}
	
}