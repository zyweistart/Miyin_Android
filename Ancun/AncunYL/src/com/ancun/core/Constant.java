package com.ancun.core;


public final class Constant {

	/**
	 * true:测试环境 false:正式环境
	 */
	public static final boolean ISSYSTEMTEST =true;

	public static final String RESTURL = ISSYSTEMTEST ?
			"http://192.168.0.221:8888/accore/http/HttpService":"http://account.chinacloudapp.cn:81/pwyl/http/HttpService";

	/**
	 * 用户接口
	 */
	public final class URL {
		/**
		 * 校验码获取
		 */
		public static final String v4scodeGet="v4scodeGet";
		/**
		 * 注册
		 */
		public static final String v4Signup="v4Signup";
		/**
		 * 密码重置
		 */
		public static final String v4pwdReset="v4pwdReset";
		/**
		 * 登录
		 */
		public static final String v4Login="v4Login";
		/**
		 * 用户信息获取
		 */
		public static final String v4infoGet="v4infoGet";
		/**
		 * 呼叫请求
		 */
		public static final String v4Call  ="v4Call ";
		/**
		 * 录音查询
		 */
		public static final String v4recQry="v4recQry";
		/**
		 * 录音信息获取
		 */
		public static final String v4recGet ="v4recGet ";
		/**
		 * 录音下载
		 */
		public static final String v4recDown="v4recDown";
		/**
		 * 录音备注修改
		 */
		public static final String v4recRemark ="v4recRemark";
		/**
		 * 录音状态变更
		 */
		public static final String v4recAlter="v4recAlter";
		/**
		 * 录音提取码
		 */
		public static final String v4recAcccode="v4recAcccode";
		/**
		 * 录音出证
		 */
		public static final String v4recCer="v4recCer";
		/**
		 * 密码修改
		 */
		public static final String v4pwdModify ="v4pwdModify";
		/**
		 * 销户
		 */
		public static final String v4Canserv  ="v4Canserv";
		/**
		 * 退出
		 */
		public static final String v4Logout  ="v4Logout";
		/**
		 * 意见反馈
		 */
		public static final String v4Feedback  ="v4Feedback";
		/**
		 * 终端版本信息获取
		 */
		public static final String versioninfoGet   ="versioninfoGet";
		
	}
	
	public final class Handler {
		/**
		 * 注册重置密码步骤1
		 */
		public static final int REGISTER_RESET_PASSWORD_STEP1=0x1119;
		/**
		 * 注册重置密码步骤2
		 */
		public static final int REGISTER_RESET_PASSWORD_STEP2=0x1120;
		
	}

	public final class ResultCode{
		/**
		 * 登录信息过期，请重新登录
		 */
		public static final int NOLOGIN=414;
		/**
		 * 用户名或密码有误
		 */
		public static final int USERNAMEORPASSWORDERROR=110036;
		
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