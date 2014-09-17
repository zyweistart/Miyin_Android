package com.start.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.start.core.Constant;
import com.start.model.UIRunnable;
import com.start.navigation.AppContext;
import com.start.navigation.LoginActivity;
import com.start.navigation.R;
import com.start.utils.CommonFn;
import com.start.utils.HttpUtils;
import com.start.utils.LogUtils;
import com.start.utils.MD5;
import com.start.utils.NetConnectManager;
import com.start.utils.StringUtils;
import com.start.utils.XMLUtils;
import com.start.widget.PullToRefreshListView;

public class HttpService {
	
	public final static int LISTVIEW_DATA_MORE = 0x01;
	public final static int LISTVIEW_DATA_LOADING = 0x02;
	public final static int LISTVIEW_DATA_FULL = 0x03;
	public final static int LISTVIEW_DATA_EMPTY = 0x04;
	
	private Activity mActivity;
	private AppContext mAppContext;
	
	private ProgressDialog mProgressDialog;
	
	public HttpService(Activity activity){
		this.mActivity=activity;
		this.mAppContext=AppContext.getInstance();
	}

	public void exeNetRequest(final String Url,final Map<String,String> params,final Map<String,String> headerParams,final UIRunnable uiRunnable){
		this.exeNetRequest(mAppContext.getString(R.string.msg_please_wait), Url, params, headerParams, uiRunnable);
	}
	
	public void exeNetRequest(final String tipMsg,final String Url,final Map<String,String> params,final Map<String,String> headerParams,final UIRunnable uiRunnable){
		if(NetConnectManager.isNetWorkAvailable(mActivity)){
			if(tipMsg!=null){
				mProgressDialog=CommonFn.progressDialog(mActivity, tipMsg);
				mProgressDialog.show();
				mProgressDialog.setCancelable(false);
			}
			new Thread(new Runnable() {
				@Override
				public void run() {
					try{
						//请求头内容
						Map<String,String> requestHeader=new HashMap<String,String>();
						if(headerParams!=null){
							requestHeader.putAll(headerParams);
						}
						if(!requestHeader.containsKey("sign")){
							params.put("accessid", mAppContext.getAccessID());
						}
						String requestContent = XMLUtils.builderRequestXml(Url, params);
						//签名为特殊处理key为不存在时默认用ACCESSKEY签名为""用MD5否则用传入的值进行签名
						if(!requestHeader.containsKey("sign")){
							requestHeader.put("sign",StringUtils.signatureHmacSHA1(MD5.md5(requestContent),mAppContext.getAccessKEY()));
						}else{
							requestHeader.put("sign","".equals(requestHeader.get("sign"))?
									MD5.md5(requestContent):
										StringUtils.signatureHmacSHA1(MD5.md5(requestContent),requestHeader.get("sign")));
						}
						String xmlContent=HttpUtils.requestServerByXmlContent(requestHeader, requestContent);
						final Map<String, Map<String, String>> mapXML=XMLUtils.xmlResolve(xmlContent);
						if(!mapXML.isEmpty()){
							Map<String,String> infoHead=mapXML.get(XMLUtils.RequestXmLConstant.INFO);
							String code=infoHead.get(XMLUtils.RequestXmLConstant.CODE);
							if(XMLUtils.RequestXmLConstant.SUCCESSCODE.equals(code)){
								uiRunnable.setContent(mapXML);
								uiRunnable.run();
							}else if("110036".equals(code)){
								//120020:用户不存在
								//110036:签名不匹配或密码不正确
								mAppContext.getSharedPreferencesUtils().putBoolean(Constant.SharedPreferences.LOGIN_AUTOLOGIN, false);
								mAppContext.getSharedPreferencesUtils().putString(Constant.SharedPreferences.LOGIN_ACCOUNT, Constant.EMPTYSTR);
								mAppContext.getSharedPreferencesUtils().putString(Constant.SharedPreferences.LOGIN_PASSWORD, Constant.EMPTYSTR);
								mAppContext.makeTextLong(R.string.msg_login_error);
							}else if(infoHead.get(XMLUtils.RequestXmLConstant.CODE).equals(XMLUtils.RequestXmLConstant.LOGINERROR)){
								reLogin(R.string.msg_login_timeout);
							}else{
								mAppContext.makeTextLong(infoHead.get(XMLUtils.RequestXmLConstant.MSG));
							}
						}else{
							mAppContext.makeTextLong(R.string.msg_error_please_try_again);
						}
					}catch(Exception e){
						LogUtils.logError(e);
						mAppContext.makeTextLong(R.string.msg_error_please_try_again);
					}finally{
						if (mProgressDialog != null) {
							mProgressDialog.dismiss();
							mProgressDialog=null;
						}
					}
				}}).start();
		}else{
			CommonFn.settingNetwork(mActivity);
		}
	}
	
	public void sendPullToRefreshListViewNetRequest(
			final List<Map<String,String>> listDatas,
			final PullToRefreshListView pListView,
			final View mFooterMore,
			final TextView mFooterMoreTitle,
			final ProgressBar mFooterMoreProgressBar,
			final LoadMode loadMode,
			final String Url,
			final Map<String,String> params,
			final Map<String,String> headerParams,
			final UIRunnable uiRunnable,
			final String DATALIST,
			final String TAGName){
		final String CACHE_TAG=String.format("%s,%s",mAppContext.getMyID(),TAGName);
		if(loadMode==LoadMode.INIT){
			String xmlContent=mAppContext.getSharedPreferencesUtils().getString(CACHE_TAG, Constant.EMPTYSTR);
			if(!StringUtils.isEmpty(xmlContent)){
				try{
					Map<String,List<Map<String,String>>> mapXML=XMLUtils.xmlResolvelist(xmlContent);
					if(!mapXML.isEmpty()){
						listDatas.clear();
						for(Map<String,String> content:mapXML.get(DATALIST)){
							listDatas.add(content);
						}
						pListView.setTag(LISTVIEW_DATA_MORE);
						mFooterMoreTitle.setText(R.string.load_more);
						mFooterMoreProgressBar.setVisibility(View.GONE);
						mActivity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								uiRunnable.run();
							}
						});
						return;
					}
				}catch(Exception e){
					mAppContext.getSharedPreferencesUtils().putString(CACHE_TAG, Constant.EMPTYSTR);
				}
			}
		}else if(loadMode==LoadMode.FOOT){
			//点击加载更多时
			//判断是否滚动到底部
			boolean scrollEnd = false;
			try {
				if(pListView.getPositionForView(mFooterMore) == pListView.getLastVisiblePosition())
					scrollEnd = true;
			} catch (Exception e) {
				scrollEnd = false;
			}
			if(scrollEnd&&StringUtils.toInt(pListView.getTag())==LISTVIEW_DATA_MORE){
				pListView.setTag(LISTVIEW_DATA_LOADING);
				mFooterMoreTitle.setText(R.string.load_ing);
				mFooterMoreProgressBar.setVisibility(View.VISIBLE);
			}else{
				return;
			}
		}
		if(NetConnectManager.isNetWorkAvailable(mActivity)){
			new Thread(new Runnable() {
				@Override
				public void run() {
					//当前请求加载的数据列表
					Boolean loadDataFlag=false;
					Integer loadDataNum=0;
					try{
						int currentPage=1;
						if(loadMode==LoadMode.FOOT){
							int size=listDatas.size();
							if(size%Constant.PAGESIZE==0){
								currentPage=size/Constant.PAGESIZE+1;
							}else{
								return;
							}
						}
						Map<String,String> requestParams=new HashMap<String,String>();
						if(params!=null){
							requestParams.putAll(params);
						}
						requestParams.put("currentpage",String.valueOf(currentPage));
						requestParams.put("pagesize", String.valueOf(Constant.PAGESIZE));
						//请求头内容
						Map<String,String> requestHeader=new HashMap<String,String>();
						if(headerParams!=null){
							requestHeader.putAll(headerParams);
						}
						if(!requestHeader.containsKey("sign")){
							requestParams.put("accessid", mAppContext.getAccessID());
						}
						String requestContent = XMLUtils.builderRequestXml(Url, requestParams);
						//签名为特殊处理key为不存在时默认用ACCESSKEY签名为""用MD5否则用传入的值进行签名
						if(!requestHeader.containsKey("sign")){
							requestHeader.put("sign",StringUtils.signatureHmacSHA1(MD5.md5(requestContent),mAppContext.getAccessKEY()));
						}else{
							requestHeader.put("sign","".equals(requestHeader.get("sign"))?
									MD5.md5(requestContent):
										StringUtils.signatureHmacSHA1(MD5.md5(requestContent),requestHeader.get("sign")));
						}
						String xmlContent=HttpUtils.requestServerByXmlContent(requestHeader, requestContent);
						Map<String,List<Map<String,String>>> mapXML=XMLUtils.xmlResolvelist(xmlContent);
						if(!mapXML.isEmpty()){
							Map<String,String> infoHead=mapXML.get(XMLUtils.RequestXmLConstant.INFO).get(0);
							if(XMLUtils.RequestXmLConstant.SUCCESSCODE.equals(infoHead.get(XMLUtils.RequestXmLConstant.CODE))){
								if(loadMode!=LoadMode.FOOT){
									//如果不为尾部刷新加载则清空数据
									listDatas.clear();
								}
								final List<Map<String,String>> contents=mapXML.get(DATALIST);
								loadDataNum=contents.size();
								if(loadDataNum>0){
									loadDataFlag=true;
								}
								if(loadDataFlag){
									//只在成功且有数据时才进行缓存
									if(loadMode==LoadMode.INIT||loadMode==LoadMode.HEAD){
										mAppContext.getSharedPreferencesUtils().putString(CACHE_TAG, xmlContent);
									}
								}
								mActivity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
//										for(Map<String,String> content:contents){
//											listDatas.add(content);
//										}
										listDatas.addAll(contents);
										uiRunnable.run();
									}
								});
							}else if(infoHead.get(XMLUtils.RequestXmLConstant.CODE).equals(XMLUtils.RequestXmLConstant.LOGINERROR)){
								reLogin(R.string.msg_login_timeout);
							}else if("110042".equals(infoHead.get(XMLUtils.RequestXmLConstant.CODE))){
								//暂无记录
								if(loadMode==LoadMode.HEAD){
									mAppContext.getSharedPreferencesUtils().putString(CACHE_TAG, "");
									
									mActivity.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											listDatas.clear();
											uiRunnable.run();
										}
									});
								}
							}else{
								mAppContext.makeTextLong(infoHead.get(XMLUtils.RequestXmLConstant.MSG));
							}
						}else{
							mAppContext.makeTextLong(R.string.msg_error_please_try_again);
						}
					}catch(Exception e){
						LogUtils.logError(e);
						mAppContext.makeTextLong(R.string.msg_error_please_try_again);
					}finally{
						final Boolean flag=loadDataFlag;
						final Integer num=loadDataNum;
						mActivity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								//如果当前为刷新状态获取的数据为空
								if(flag){
									if(num>=Constant.PAGESIZE){
										pListView.setTag(LISTVIEW_DATA_MORE);
										mFooterMoreTitle.setText(R.string.load_more);
									}else{
										//加载完毕
										pListView.setTag(LISTVIEW_DATA_FULL);
										mFooterMoreTitle.setText(R.string.load_full);
									}
								}else{
									if(loadMode==LoadMode.FOOT){
										//加载完毕
										pListView.setTag(LISTVIEW_DATA_FULL);
										mFooterMoreTitle.setText(R.string.load_full);
									}else{
										//数据为空
										pListView.setTag(LISTVIEW_DATA_EMPTY);
										mFooterMoreTitle.setText(R.string.load_empty);
									}
								}
								mFooterMoreProgressBar.setVisibility(View.GONE);
								//刷新模式为下拉刷新时
								if(loadMode==LoadMode.HEAD){
									pListView.onRefreshComplete();
								}
							}
						});
						
					}
				}}).start();
		}else{
			CommonFn.settingNetwork(mActivity);
			pListView.setTag(LISTVIEW_DATA_MORE);
			mFooterMoreTitle.setText(R.string.load_more);
			mFooterMoreProgressBar.setVisibility(View.GONE);
			if(loadMode==LoadMode.HEAD){
				pListView.onRefreshComplete();
			}
		}
	}
	
	/**
	 * 加载模式
	 */
	public enum LoadMode{
		//初始化
		INIT,
		//头部刷新
		HEAD,
		//尾部刷新
		FOOT
	}
	
	/**
	 * 重新登录
	 * @param resId
	 */
	public void reLogin(int resId){
		reLogin(mActivity.getString(resId));
	}
	
	/**
	 * 重新登录
	 * @param message
	 */
	public void reLogin(String message){
		mAppContext.initUserInfo(null);
		mAppContext.getSharedPreferencesUtils().putString(Constant.SharedPreferences.LOGIN_PASSWORD, Constant.EMPTYSTR);
		Bundle bundle=new Bundle();
		bundle.putString(Constant.Bundle.RELOGINMESSAGE, message);
		Intent intent=new Intent(mActivity,LoginActivity.class);
		intent.putExtras(bundle);
		mActivity.startActivity(intent);
	}
	
}