package com.start.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONException;
import org.json.JSONObject;

import start.core.AppConstant;
import start.core.AppConstant.Handler;
import start.core.AppConstant.ResultCode;
import start.core.AppContext;
import start.core.AppException;
import start.core.HandlerContext;
import start.core.R;
import start.service.UIHelper;
import start.utils.NetConnectManager;
import start.utils.StringUtils;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

import com.start.core.Config;

/**
 * @author Start   
 * @Description: 网络请求执行类
 */
@SuppressLint("DefaultLocale")
public class HttpServer {
	
	private String mUrl;
	private HandlerContext mHandler;
	private Context mContext;
	private Map<String,String> mHeaders;
	private Map<String,String>mParams;
	private ProgressDialog mPDialog;
	
	private String downloadDirectory;
	private String downloadFileName;
	
	public HttpServer(String mUrl, HandlerContext mHandler) {
		this.mUrl = mUrl;
		this.mHandler = mHandler;
		this.mContext = mHandler.getContext();
	}
	
	public void setHeaders(Map<String, String> mHeaders) {
		this.mHeaders = mHeaders;
	}

	public void setParams(Map<String, String> mParams) {
		this.mParams = mParams;
	}

	public void get(final HttpRunnable runnable){
		get(runnable,true);
	}
	
	public void get(final HttpRunnable runnable,Boolean dialog){
		
		if(NetConnectManager.isNetworkConnected(mContext)){
			//HTTP请求
			if(dialog){
				mPDialog = new ProgressDialog(mContext);
				mPDialog.setMessage(mContext.getString(R.string.wait));
				mPDialog.setIndeterminate(true);
				mPDialog.setCancelable(false);
				mPDialog.show();
			}
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					
					try{
						HttpResponse httpResponse=getResponse(setBuildRequestContent());
						Response response=new Response(httpResponse);
//						try {
//							BufferedReader in = new BufferedReader(new InputStreamReader(mContext.getAssets().open("testjson.txt")));
//							String line =null;
//							StringBuffer buffer = new StringBuffer();
//							while ((line = in.readLine()) != null) {
//								buffer.append(line);
//							}
//							response.setResponseString(buffer.toString());
//						} catch (Exception e) {
//							throw AppException.http(e);
//						}
						response.resolveJson();
						if(ResultCode.SUCCESS.equals(response.getCode())){
							runnable.run(response);
						}else{
							mHandler.sendMessage(StringUtils.toInt(response.getCode()),response.getMsg());
						}
					}catch(AppException e){
						mHandler.sendMessage(Handler.HTTP_APPEXCEPTION_MESSAGE, e.getErrorString(mContext));
					}finally{
						if (mPDialog != null) {
							mPDialog.dismiss();
							mPDialog=null;
						}
					}
					
				}}).start();
		}else{
			UIHelper.goSettingNetwork(mContext);
		}
		
	}
	
	public void download(DownloadRunnable runnable,String downloadDirectory,String downloadFileName){
		if(NetConnectManager.isNetworkConnected(mContext)){
			this.downloadDirectory = downloadDirectory;
			this.downloadFileName = downloadFileName;
			new DownloadTask(runnable).execute();
		}else{
			UIHelper.goSettingNetwork(mContext);
		}
	}
	
	public String setBuildRequestContent() throws AppException{
		String requestContent=buildRequestContentByStringJson();
		if(mHeaders==null){
			mHeaders=new HashMap<String,String>();
		}
//		mHeaders.put("sign",AppConstant.EMPTYSTR.equals(mHeaders.get("sign"))?
//				MD5.md5(requestContent):
//					StringUtils.signatureHmacSHA1(MD5.md5(requestContent),mHeaders.get("sign")));
		return requestContent;
	}
	
	/**
	 * 生成请求内容
	 */
	public String buildRequestContentByStringJson() throws AppException{
//		JSONObject commonObject = new JSONObject();
		JSONObject contentObject = new JSONObject();
//		JSONObject requestObject = new JSONObject();
//		JSONObject mainObject = new JSONObject();
	    try {
//	    	commonObject.put("action",mUrl);
//	    	commonObject.put("reqtime",TimeUtils.getSysTimeLong());
//	    	requestObject.put("common", commonObject);
//	    	
//	    	if(mParams!=null){
//	    		for(String key:mParams.keySet()){
//	    			contentObject.put(key,mParams.get(key));
////	    			contentObject.put(key.toLowerCase(),mParams.get(key));
//	    		}
//	    	}
//	    	requestObject.put("content", contentObject);
//	    	
//	    	mainObject.put("request", requestObject);
//			return mainObject.toString();
			if(mParams!=null){
	    		for(String key:mParams.keySet()){
	    			contentObject.put(key,mParams.get(key));
//	    			contentObject.put(key.toLowerCase(),mParams.get(key));
	    		}
	    	}
			return contentObject.toString();
		} catch (JSONException e) {
			throw AppException.json(e);
		}  
	}
	
	/**
	 * 请求并获取响应的HTTP对象
	 */
	public HttpResponse getResponse(String requestContent) throws AppException {
		HttpClient client = new DefaultHttpClient();
		ProtocolVersion protocolVersion =new ProtocolVersion("HTTP", 1, 0);
		client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, protocolVersion);
		client.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, AppConstant.ENCODE);
		// 设置超时时间为30秒
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,30*1000);
		//时间戳
		String timestamp=String.valueOf(System.currentTimeMillis());
		//随机数
		String nonce=String.valueOf(new Random().nextInt(1000));
		//组合成数组
		String[] arrs={Config.ACCESS_TOKEN,timestamp,nonce};
		//升序排列
		Arrays.sort(arrs);
		String signature=arrs[0]+arrs[1]+arrs[2];
		String url=String.format(AppContext.getInstance().getServerURL(), this.mUrl,SHA1(signature),timestamp,nonce);
		HttpPost post = new HttpPost(url);
		try {
			post.addHeader("format", "json");
			post.addHeader("reqlength", StringUtils.encode(String.valueOf(requestContent.getBytes(AppConstant.ENCODE).length)));
			if (mHeaders != null) {
				for (String key : mHeaders.keySet()) {
					// 循环遍历添加请求头对请求头内容进行URL编码
					post.addHeader(key,StringUtils.encode(mHeaders.get(key)));
				}
			}
			post.setEntity(new ByteArrayEntity(requestContent.getBytes(AppConstant.ENCODE)));
			return client.execute(post);
		} catch (Exception e) {
			throw AppException.http(e);
		}
	}
	
	private class DownloadTask extends AsyncTask<Void, Float, File> {

		private ProgressDialog pDialog;
		
		private DownloadRunnable mRunnable;
		
		public DownloadTask(DownloadRunnable runnable){
			this.mRunnable=runnable;
		}
		
		@Override
		protected void onPreExecute() {
			// 创建ProgressDialog对象  
			pDialog = new ProgressDialog(mContext);  
			// 设置进度条风格，风格为圆形，旋转的  
			pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
			// 设置ProgressDialog提示信息  
			pDialog.setMessage(mContext.getString(R.string.downloading));  
			// 设置ProgressDialog标题图标  
//            pDialog.setIcon(R.drawable.img2);  
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
			File dirFile=new File(downloadDirectory);
			if(!dirFile.exists()){
				dirFile.mkdirs();
			}
			File downFile=new File(dirFile,downloadFileName);
			if(!downFile.exists()){
				try{
					File tmpFile=new File(dirFile,downloadFileName+".tmp");
					HttpResponse httpResponse=getResponse(setBuildRequestContent());
					if(httpResponse!=null&&httpResponse.getStatusLine().getStatusCode()==200){
						Response response=new Response(httpResponse);
						int len=-1;
						long reallen=0;
						//获取下载文件的总大小
						long totallen=httpResponse.getEntity().getContentLength();
						InputStream is=null;
						FileOutputStream fos=null;
						byte[] buffer=new byte[1024*8];
						try {
							is=response.getInputStream();
							fos = new FileOutputStream(tmpFile);
							while((len=is.read(buffer))!=-1&&!isCancelled()){
								fos.write(buffer,0,len);
								reallen+=len;
								publishProgress((float)reallen/totallen);
							}
						} catch (AppException e) {
							mHandler.sendMessage(Handler.HTTP_APPEXCEPTION_MESSAGE, e.getMessage());
						} catch (FileNotFoundException e) {
							mHandler.sendMessage(Handler.HTTP_APPEXCEPTION_MESSAGE, e.getMessage());
						} catch (IOException e) {
							mHandler.sendMessage(Handler.HTTP_APPEXCEPTION_MESSAGE, e.getMessage());
						}finally{
							if(fos!=null){
								try {
									fos.flush();
								} catch (IOException e) {
									mHandler.sendMessage(Handler.HTTP_APPEXCEPTION_MESSAGE, e.getMessage());
								}finally{
									try {
										fos.close();
									} catch (IOException e) {
										mHandler.sendMessage(Handler.HTTP_APPEXCEPTION_MESSAGE, e.getMessage());
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
									mHandler.sendMessage(Handler.HTTP_APPEXCEPTION_MESSAGE, "文件大小不一致");
									tmpFile.delete();
								}
							}
						}
					}else{
						mHandler.sendMessage(Handler.HTTP_APPEXCEPTION_MESSAGE,"文件下载失败");
					}
				}catch(AppException e){
					mHandler.sendMessage(Handler.HTTP_APPEXCEPTION_MESSAGE, e.getErrorString(mContext));
				}
			}
			return downFile;
		}

		@Override
		protected void onProgressUpdate(Float... values) {
			pDialog.setProgress(Math.round(values[0]*100f)); 
		}

		@Override
		protected void onPostExecute(File result) {
			if(result.exists()){
				try {
					mRunnable.run(result);
				} catch (AppException e) {
					mHandler.sendMessage(Handler.HTTP_APPEXCEPTION_MESSAGE, e.getErrorString(mContext));
				}
			}
			if(pDialog!=null){
				pDialog.dismiss();
				mPDialog=null;
			}
		}
		
	}
	
	public String SHA1(String inStr) {
        MessageDigest md = null;
        String outStr = null;
        try {
            md = MessageDigest.getInstance("SHA-1");     //选择SHA-1，也可以选择MD5
            byte[] digest = md.digest(inStr.getBytes());       //返回的是byet[]，要转化为String存储比较方便
            outStr = bytetoString(digest);
        }catch(NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        }
        return outStr;
    }
	
	public String bytetoString(byte[] digest) {
        String str = "";
        String tempStr = "";
        for (int i = 1; i < digest.length; i++) {
            tempStr = (Integer.toHexString(digest[i] & 0xff));
            if (tempStr.length() == 1) {
                str = str + "0" + tempStr;
            } else {
                str = str + tempStr;
            }
        }
        return str.toLowerCase();
    }
	
}