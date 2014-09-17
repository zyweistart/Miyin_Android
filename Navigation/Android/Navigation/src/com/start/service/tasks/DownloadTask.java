package com.start.service.tasks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Environment;

import com.start.core.Constant;
import com.start.core.CoreActivity;
import com.start.navigation.AppContext;
import com.start.navigation.R;
import com.start.utils.HttpUtils;
import com.start.utils.MD5;
import com.start.utils.StringUtils;
import com.start.utils.XMLUtils;

public class DownloadTask extends AsyncTask<Void, Float, File> {

	private CoreActivity mContext;
	private AppContext mAppContext;
	private ProgressDialog pDialog;
	private String fileno;
	
	public DownloadTask(CoreActivity context,String fileno){
		this.mContext=context;
		this.fileno=fileno;
		this.mAppContext=AppContext.getInstance();
	}

	@Override
	protected void onPreExecute() {
		// 创建ProgressDialog对象  
		pDialog = new ProgressDialog(mContext);  
		// 设置进度条风格，风格为圆形，旋转的  
		pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
		// 设置ProgressDialog 标题  
		pDialog.setTitle(R.string.prompt);  
		// 设置ProgressDialog提示信息  
		pDialog.setMessage(mContext.getString(R.string.msg_downloading_datafile));  
		// 设置ProgressDialog标题图标  
//        pDialog.setIcon(R.drawable.img2);  
		// 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确  
		pDialog.setIndeterminate(false);  
		// 设置ProgressDialog 进度条进度  
		pDialog.setProgress(100);
		// 设置ProgressDialog 是否可以按退回键取消  
		pDialog.setCancelable(true);
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
	protected File doInBackground(Void... params) {
		String dxternalStorageDirectory=Environment.getExternalStorageDirectory().getPath();
		File tempDirFile=new File(dxternalStorageDirectory+Constant.TMPDIRFILE);
		if(!tempDirFile.exists()){
			tempDirFile.mkdirs();
		}
		File downFile=new File(tempDirFile,fileno);
		if(!downFile.exists()){
			try{
				Map<String,String> pars=new HashMap<String,String>();
				pars.put("accessid",Constant.ACCESSID_LOCAL);
				pars.put("recordno",fileno);
				String requestContent = XMLUtils.builderRequestXml(Constant.ServerAPI.hospitalDownload, pars);
				Map<String,String> requestHeader=new HashMap<String,String>();
				requestHeader.put("sign",StringUtils.signatureHmacSHA1(MD5.md5(requestContent),Constant.ACCESSKEY_LOCAL));
				HttpResponse response=HttpUtils.requestDownServer(requestHeader, requestContent);
				if(response!=null&&response.getStatusLine().getStatusCode()==200){
					int len=-1;
					long reallen=0;
					//获取下载文件的总大小
					long totallen=response.getEntity().getContentLength();
					InputStream is=null;
					FileOutputStream fos=null;
					byte[] buffer=new byte[1024*8];
					File tmpFile=new File(tempDirFile,fileno+".tmp");
					if(tmpFile.exists()){
						tmpFile.delete();
					}
					try {
						is=response.getEntity().getContent();
						fos = new FileOutputStream(tmpFile);
						while((len=is.read(buffer))!=-1&&!isCancelled()){
							fos.write(buffer,0,len);
							reallen+=len;
							publishProgress((float)reallen/totallen);
						}
					} catch (Exception e) {
						mAppContext.makeTextLong(e.getMessage());
					}finally{
						if(fos!=null){
							try {
								fos.flush();
							} catch (IOException e) {
								mAppContext.makeTextLong(e.getMessage());
							}finally{
								try {
									fos.close();
								} catch (IOException e) {
									mAppContext.makeTextLong(e.getMessage());
								}finally{
									fos=null;
								}
							}
						}
						buffer=null;
						if(isCancelled()){
							tmpFile.delete();
						}else{
							if(tmpFile.length()==totallen){
								tmpFile.renameTo(downFile);
							}else{
								//文件大小不一致
								mAppContext.makeTextLong(R.string.msg_error_please_try_again);
								tmpFile.delete();
							}
						}
					}
				}else{
					//文件下载失败
					mAppContext.makeTextLong(R.string.msg_error_please_try_again);
				}
			}catch(Exception e){
				mAppContext.makeTextLong(R.string.msg_error_please_try_again);
			}
		}
		return downFile;
	}

	@Override
	protected void onProgressUpdate(Float... values) {
		//更新下载进度条
		pDialog.setProgress(Math.round(values[0]*100f)); 
	}

	@Override
	protected void onPostExecute(File result) {
		pDialog.dismiss();
		if(result.exists()){
			new DecompressTask(mContext,fileno).execute();
		}
	}
}
