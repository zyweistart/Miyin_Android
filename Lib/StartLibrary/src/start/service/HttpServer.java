package start.service;

import java.util.HashMap;
import java.util.Map;

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
import start.utils.MD5;
import start.utils.NetConnectManager;
import start.utils.StringUtils;
import start.utils.TimeUtils;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Message;

/**
 * @author Start   
 * @Description: 网络请求执行类
 * @ClassName: HttpServer.java   
 * @date 2014年6月27日 下午3:45:19      
 * @说明  代码版权归 杭州反盗版中心有限公司 所有
 */
@SuppressLint("DefaultLocale")
public class HttpServer {
	
	private String mUrl;
	private HandlerContext mHandler;
	private Context mContext;
	private Map<String,String> mHeaders;
	private Map<String,String>mParams;
	private ProgressDialog mPDialog;
	private Boolean download;
	
	public HttpServer(String mUrl, HandlerContext mHandler) {
		this.mUrl = mUrl;
		this.mHandler = mHandler;
		this.mContext = mHandler.getContext();
		this.setDownload(false);
	}
	
	public void setHeaders(Map<String, String> mHeaders) {
		this.mHeaders = mHeaders;
	}

	public void setParams(Map<String, String> mParams) {
		this.mParams = mParams;
	}
	
	public Boolean isDownload() {
		return download;
	}

	public void setDownload(Boolean download) {
		this.download = download;
	}

	public void get(final UIRunnable runnable){
		get(runnable,true);
	}
	
	public void get(final UIRunnable runnable,Boolean dialog){
		
		if(NetConnectManager.isNetworkConnected(mContext)){
			
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
						String requestContent=buildRequestContentByStringJson();
						if(mHeaders==null){
							mHeaders=new HashMap<String,String>();
						}
						mHeaders.put("sign",AppConstant.EMPTYSTR.equals(mHeaders.get("sign"))?
								MD5.md5(requestContent):
									StringUtils.signatureHmacSHA1(MD5.md5(requestContent),mHeaders.get("sign")));
						HttpResponse httpResponse=getResponse(requestContent);
						Response response=new Response(httpResponse);
						if(isDownload()){
							//TODO:文件下载处理
						}else{
							response.resolveJson();
							if(ResultCode.SUCCESS.equals(response.getCode())){
								runnable.run(response);
							}else{
								Message msg = new Message();
								msg.what = StringUtils.toInt(response.getCode());
								msg.obj = response.getMsg();
								mHandler.sendMessage(msg);
							}
						}
					}catch(AppException e){
						//发送HTTP请求信息
						Message message=new Message();
						message.what=Handler.HTTP_APPEXCEPTION_MESSAGE;
						message.obj=e.getErrorString(mContext);
						mHandler.sendMessage(message);
					}finally{
						if (mPDialog != null) {
							mPDialog.dismiss();
						}
					}
					
				}}).start();
			
		}else{
			UIHelper.goSettingNetwork(mContext);
		}
		
	}
	
	/**
	 * 生成请求内容
	 */
	public String buildRequestContentByStringJson() throws AppException{
		JSONObject commonObject = new JSONObject();
		JSONObject contentObject = new JSONObject();
		JSONObject requestObject = new JSONObject();
		JSONObject mainObject = new JSONObject();
	    try {
	    	commonObject.put("action",mUrl);
	    	commonObject.put("reqtime",TimeUtils.getSysTimeLong());
	    	requestObject.put("common", commonObject);
	    	
	    	if(mParams!=null){
	    		for(String key:mParams.keySet()){
	    			contentObject.put(key.toLowerCase(),mParams.get(key));
	    		}
	    	}
	    	requestObject.put("content", contentObject);
	    	
	    	mainObject.put("request", requestObject);
			return mainObject.toString();
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
		HttpPost post = new HttpPost(AppContext.getInstance().getServerURL());
		try {
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
	
}