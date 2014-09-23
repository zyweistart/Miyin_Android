package com.start.service;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;

import com.start.core.Constant;
import com.start.navigation.AppContext;
import com.start.utils.HttpUtils;
import com.start.utils.LogUtils;
import com.start.utils.MD5;
import com.start.utils.NetConnectManager;
import com.start.utils.StringUtils;
import com.start.utils.XMLUtils;

public class UserLocationReport extends Thread {

	private Context mContext;
	private AppContext mAppContext;
	private Boolean flag;
	private String lastLocation;
	
	public UserLocationReport(Activity activity){
		mContext=activity;
		mAppContext=AppContext.getInstance();
		flag=true;
		lastLocation=Constant.EMPTYSTR;
	}
	
	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	@Override
	public void run() {
		while(true&&flag){
			if(NetConnectManager.isNetWorkAvailable(mContext)){
				//未登录则不上报位置信息
				if(mAppContext.isLogin()){
					String data=mAppContext.getSharedPreferencesUtils().getString(Constant.SharedPreferences.USERLOCATION, Constant.EMPTYSTR);
					if(!Constant.EMPTYSTR.equals(data)&&!data.equals(lastLocation)){
						
						final String[] info=data.split(";");
						if(info.length==3){
							new Thread(new Runnable() {
								@Override
								public void run() {
									try{
										Map<String,String> requestParams=new HashMap<String,String>();
										requestParams.put("accessid", mAppContext.getAccessID());
										requestParams.put("maprecordno",mAppContext.getCurrentDataNo());
										requestParams.put("submapno", info[0]);
										requestParams.put("latitude", info[1]);
										requestParams.put("longitude", info[2]);
										String requestContent = XMLUtils.builderRequestXml(Constant.ServerAPI.userposReport, requestParams);
										//请求头内容
										Map<String,String> requestHeader=new HashMap<String,String>();
										requestHeader.put("sign",StringUtils.signatureHmacSHA1(MD5.md5(requestContent),mAppContext.getAccessKEY()));
										String xmlContent=HttpUtils.requestServerByXmlContent(requestHeader, requestContent);
										Map<String, Map<String, String>> mapXML=XMLUtils.xmlResolve(xmlContent);
										if(!mapXML.isEmpty()){
											Map<String,String> infoHead=mapXML.get(XMLUtils.RequestXmLConstant.INFO);
											String code=infoHead.get(XMLUtils.RequestXmLConstant.CODE);
											if(XMLUtils.RequestXmLConstant.SUCCESSCODE.equals(code)){
												//成功
											}
										}
									}catch(Exception e){
										LogUtils.logError(e);
									}
								}}).start();
							
						}
						lastLocation=data;
					}
				}
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
