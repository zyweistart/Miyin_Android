package com.start.core;


/**
 * 常量
 * @author Start
 */
public final class Constant {
	/**
	 * true:测试环境
	 * false:正式环境
	 */
	public static final boolean ISTEST=true;

	public static final String RESTURL=ISTEST?
			"http://account.chinacloudapp.cn:81/http/HttpService"://公司外网
			"http://account.chinacloudapp.cn:81/http/HttpService";//正式地址
	
	public static final String ACCESSID_LOCAL=ISTEST?
			"e75cf0bee6850378bdb606e13172018e":
			"e75cf0bee6850378bdb606e13172018e";
	public static final String ACCESSKEY_LOCAL=ISTEST?
			"NDBmOTE2NWZlYTdhZWNlNTE0OTQyYmYxMWNjNGI0NTg=":
			"NDBmOTE2NWZlYTdhZWNlNTE0OTQyYmYxMWNjNGI0NTg=";
	
	public static final int PAGESIZE=8;
	public static final String EMPTYSTR="";
	public static final String ENCODE="UTF-8";
	
	//数据主目录
	public static final String DATADIRFILE="/navigation/data/";
	//临时目录
	public static final String TMPDIRFILE="/navigation/tmp/";

	public final class ActivityResultCode{
		public static final int EXITAPP=9991;
	}
	
	public final class Bundle{
		public static final String RELOGINMESSAGE="RELOGINMESSAGE";
	}
	
	public final class ServerAPI{
		/**
		 * 验证码获取
		 */
		public static final String uacodeGet="uacodeGet";
		/**
		 * 注册
		 */
		public static final String userReg="userReg";
		/**
		 * 密码重置
		 */
		public static final String userpwdReset="userpwdReset";
		/**
		 * 退出
		 */
		public static final String userLogout="userLogout";
		/**
		 * 登录
		 */
		public static final String userLogin="userLogin";
		/**
		 * 密码修改
		 */
		public static final String userpwdMod="userpwdMod";
		/**
		 * 我开放位置权限的好友查询
		 */
		public static final String ufriendoQuery="ufriendoQuery";
		/**
		 * 对好友开放位置权限管理
		 */
		public static final String ufriendoDeal="ufriendoDeal";
		/**
		 * 医院查询
		 */
		public static final String hospitalQuery="hospitalQuery";
		/**
		 * 医院详细
		 */
		public static final String hospitalDetail="hospitalDetail";
		/**
		 * 数据文件下载
		 */
		public static final String hospitalDownload="hospitalDownload";
		/**
		 * 检测版本更新
		 */
		public static final String appverGet="appverGet";
		/**
		 * 用户位置上报
		 */
		public static final String userposReport="userposReport";
		/**
		 * 向我开放位置权限的好友查询
		 */
		public static final String ufriendiQuery="ufriendiQuery";
		
	}
	
	public final class SharedPreferences{
		//当前使用的数据文件编号
		public static final String CURRENTDATAFILENO="CURRENTDATAFILENO";
		//是否切换了地图数据包
		public static final String SWITCHMAPDATAFLAG="SWITCHMAPDATAFLAG";
		//当前版本号
		public static final String CURRENTVERSIONCODE="CURRENTVERSIONCODE";
		//自动登录
		public static final String LOGIN_AUTOLOGIN="LOGIN_AUTOLOGIN";
		//当前登录的账号
		public static final String LOGIN_ACCOUNT="LOGIN_ACCOUNT";
		//当前登录的密码
		public static final String LOGIN_PASSWORD="LOGIN_PASSWORD";
		public static final String USERLOCATION="USERLOCATION";
	}
	
	public final class Handler{
		public static final int HANDLERUPDATEMAINTHREAD=0x438217;
	}
	
}