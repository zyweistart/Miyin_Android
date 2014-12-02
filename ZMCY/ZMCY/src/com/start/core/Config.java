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

	// TODO:传统光源
	public static final String NLIGHTSOURCEURL="http://www.qq.com";
	// TODO: LED光源
	public static final String LIGHTSOURCEURL="http://www.163.com";
	// 商城
	public static final String MallURL=AppContext.getInstance().getServerURL()+"/38/shop_list.html ";
	// TODO: 展会
	public static final String ExhibitionURL="http://www.sina.com";
	// 专家自荐
	public static final String EXPERTSURL=AppContext.getInstance().getServerURL()+"/63/zijian.html";
	// 应用
	public static final String APPURL=AppContext.getInstance().getServerURL()+"/40/yingyong.html";

}
