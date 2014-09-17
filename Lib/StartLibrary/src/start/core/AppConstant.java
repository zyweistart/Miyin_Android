package start.core;

public class AppConstant {

	public static final int PAGESIZE=8;
	public static final String EMPTYSTR = "";
	public static final String ENCODE = "UTF-8";
	
	public final class Handler {
		/**
		 * 加载数据
		 */
		public static final int LOAD_DATA=0x1111;
		/**
		 * 初始化加载
		 */
		public static final int LOAD_INIT_DATA=0x1112;
		/**
		 * 下拉刷新开始
		 */
		public static final int LOAD_START_PULLDOWN_REFRESH_DATA=0x1113;
		/**
		 * 下拉刷新结束
		 */
		public static final int LOAD_END_PULLDOWN_REFRESH_DATA=0x1114;
		/**
		 * 加载更多开始
		 */
		public static final int LOAD_START_MORE_DATA=0x1115;
		/**
		 * 加载更多结束
		 */
		public static final int LOAD_END_MORE_DATA=0x1116;
		/**
		 * 加载结束
		 */
		public static final int LOAD_END=0x1117;
		/**
		 * 无法加载数据时清除数据
		 */
		public static final int LOAD_DATA_FAIL_CLEAR_DATA=0x1118;
		/**
		 * HTTP加载结束出现的出错信息
		 */
		public static final int HTTP_APPEXCEPTION_MESSAGE=0x1119;
		
	}

	public final class ResultCode{
		/**
		 * 成功返回的编码
		 */
		public static final String SUCCESS="100000";
		/**
		 * 列表数据暂无记录返回的编码
		 */
		public static final int NODATA=110042;
		
	}
	
}