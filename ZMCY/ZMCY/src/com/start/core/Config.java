package com.start.core;

import start.core.AppContext;

public class Config {

	public static final String ServerURL=AppContext.getInstance().getServerURL()+"/Ajax/API/%s.html?signature=%s&timestamp=%s&nonce=%s";
	
	// 微信
	public static final String WXAPPID = "wxffa3cdeb705edd48";
	public static final String WXAPPSECRET = "930bb8937c42109377e73bea7b92f88c";
	// QQ
	public static final String QQAPPID = "100424468";
	public static final String QQAPPKEY = "c7394704798a158208a74ab60104f0ba";

	//传统光源
	public static final String NLIGHTSOURCEURL=AppContext.getInstance().getServerURL()+"/59/ios_qiye_list.html";
	//LED光源
	public static final String LIGHTSOURCEURL=AppContext.getInstance().getServerURL()+"/75/ios_qiye_list.html";
	//商城
	public static final String MallURL=AppContext.getInstance().getServerURL()+"/156/ios_shop_list.html";
	//展会
	public static final String ExhibitionURL=AppContext.getInstance().getServerURL()+"/60/ios_zhanhui.html";
	//能人
	public static final String EXPERTSURL1=AppContext.getInstance().getServerURL()+"/157/ios_nengren_list.html";
	//自荐
	public static final String EXPERTSURL=AppContext.getInstance().getServerURL()+"/65/ios_zijian.html";
	// 应用
	public static final String APPURL=AppContext.getInstance().getServerURL()+"/66/ios_yingyong.html";

}
