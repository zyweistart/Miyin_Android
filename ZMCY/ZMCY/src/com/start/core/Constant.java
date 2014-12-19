package com.start.core;



public final class Constant {

	/**
	 * 临时目录
	 */
	public static final String TEMPDIRECTORY="temp";
	
	/**
	 * 用户接口
	 */
	public final class URL {
		public static final String GetInfo="GetInfo";
		public static final String GetListALL="GetListALL";
		public static final String UserLogin="UserLogin";
		public static final String RegUser="RegUser";
		public static final String OAuthLogin="OAuthLogin";
		public static final String FocusOrCollection="FocusOrCollection";
		public static final String VersionCheck="VersionCheck";
		public static final String UserSignIn="UserSignIn";
		public static final String GetPushState="GetPushState";
		public static final String ChangePushState="ChangePushState";
		public static final String AskExperts="AskExperts";
		//登录
		//注册
		//第三方应用注册登录(微信，QQ)
		//收藏列表
		//关注列表
		//签到接口
		//资讯列表接口
		//专家列表接口
		//专家咨询接口
		//专家解答列表接口
		//推送设置
		//推送当前设置状态获取
		//资讯搜索
		//版本检测接口
	}
	
	public final class Handler {
		/**
		 * 注册重置密码步骤1
		 */
		public static final int REGISTER_RESET_PASSWORD_STEP1=0x1119333;
		/**
		 * 注册重置密码步骤2
		 */
		public static final int REGISTER_RESET_PASSWORD_STEP2=0x1122230;
		/**
		 * 第三方QQ登陆
		 */
		public static final int HANDLERTHIRDPARTYLANDINGQQ=0x1123334;
		/**
		 * 第三方微信登陆
		 */
		public static final int HANDLERTHIRDPARTYLANDINGWX=0x1123335;
		
	}

	public final class ResultCode{
		/**
		 * 登录信息过期，请重新登录
		 */
		public static final int NOLOGIN=-15;
		/**
		 * 用户名或密码有误
		 */
		public static final int USERNAMEORPASSWORDERROR=-20;
		
	}
	
	public final class Preferences {
		/**
		 * 用户账户
		 */
		public static final String SP_ACCOUNT_CONTENT_DATA = "SP_ACCOUNT_DATA";
		/**
		 * 用户密码
		 */
		public static final String SP_PASSWORD_CONTENT_DATA = "SP_PASSWORD_DATA";
		/**
		 * 自动登录
		 */
		public static final String SP_AUTOLOGIN_CONTENT_DATA = "SP_AUTOLOGIN_CONTENT_DATA";
		/**
		 * 手势密码
		 */
		public static final String SP_LOCK_KEY_DATA = "SP_LOCK_KEY_DATA";
		/**
		 * 是否需要重置手势密码
		 */
		public static final String SP_IS_RESET_LOCK_KEY = "SP_IS_RESET_LOCK_KEY";
		/**
		 * 当前版本号
		 */
		public static final String SP_CURRENTVERSIONCODE = "SP_CURRENTVERSIONCODE";
		/**
		 * 版本最后检测日期
		 */
		public static final String SP_VERSION_DATA = "SP_VERSION_DATA";
		
		/**
		 * 字体设置
		 */
		public static final String SP_SIZE_SETTING = "SP_SIZE_SETTING";
		/**
		 * 阅读模式 日间，夜间
		 */
		public static final String SP_READ_MODE = "SP_READ_MODE";
		/**
		 * 个推Clientid
		 */
		public static final String SP_GETUICLIENTID = "SP_GETUICLIENTID";
		
	}
	
}