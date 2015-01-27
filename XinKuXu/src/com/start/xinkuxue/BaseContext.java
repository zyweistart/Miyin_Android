package com.start.xinkuxue;

import start.core.AppContext;
import android.database.sqlite.SQLiteDatabase;

import com.start.core.CacheActivity;
import com.start.core.DBManageDao;
import com.start.service.User;

/**
 * @author Start
 * @Description: 全局application
 */
public class BaseContext extends AppContext {

	private User mUser;
	private static DBManageDao dbManager;
	private static SQLiteDatabase mSQLiteDatabase;
	private CacheActivity mCacheActivity;

	@Override
	public void onCreate() {
		super.onCreate();
		getDBManager();
	}

	@Override
	public Boolean isTestEnvironmental() {
		return true;
	}

	@Override
	public String getServerURL() {
		return isTestEnvironmental() ? "http://api.020leader.com":"http://api.020leader.com";
	}

	@Override
	public String getStorageDirectory(String name) {
//		if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
//			return Environment.getExternalStorageDirectory().getPath() + "/xinkuxue/"+ name + "/";
//		}
		return "/storage/emulated/legacy/xinkuxue/"+ name + "/";
	}

	/**
	 * 获取数据库管理对象
	 */
	public static DBManageDao getDBManager() {
		if (dbManager == null) {
			dbManager = new DBManageDao(getContext());
		}
		return dbManager;
	}

	/**
	 * 获取数据库操作类
	 */
	public static SQLiteDatabase getSQLiteDatabase() {
		if (null == mSQLiteDatabase) {
			mSQLiteDatabase = getDBManager().getSQLiteDatabase();
		}
		return mSQLiteDatabase;
	}

	/**
	 * 获取当前用户信息
	 */
	public User currentUser() {
		if (mUser == null) {
			mUser = User.getInstance();
		}
		return mUser;
	}

	public CacheActivity getCacheActivity() {
		if (mCacheActivity == null) {
			mCacheActivity = new CacheActivity();
		}
		return mCacheActivity;
	}

}