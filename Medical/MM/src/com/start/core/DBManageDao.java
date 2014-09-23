/**    
 * @author maomy  
 * @Description: 所有DAO的父类
 * @Package com.ciapc.tzd.common.db   
 * @Title: BaseDBManageDao.java   
 * @date 2014-6-25 上午19:34:58   
 * @version V1.0 
 * @说明  代码版权归 杭州反盗版中心有限公司 所有
 */ 
package com.start.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public  class DBManageDao {

	private SQLiteDBHelper mDBHelper;
	private SQLiteDatabase mSQLiteDatabase;
	
	public DBManageDao(Context context) {
		this.mDBHelper = new SQLiteDBHelper(context);
		this.mSQLiteDatabase = this.mDBHelper.getWritableDatabase();
	}
	
	public SQLiteDBHelper getDBHelper() {
		return mDBHelper;
	}

	public SQLiteDatabase getSQLiteDatabase() {
		return mSQLiteDatabase;
	}

}