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
		public static final String VersionCheck="VersionCheck";
		public static final String UpdateUser="UpdateUser";
		public static final String RefreshUser="RefreshUser";
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
		
	}
	
}