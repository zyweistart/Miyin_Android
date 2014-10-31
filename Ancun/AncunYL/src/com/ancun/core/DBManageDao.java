package com.ancun.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public  class DBManageDao {

	private Context mContext;
	private SQLiteDBHelper mDBHelper;
	private SQLiteDatabase mSQLiteDatabase;
	
	public DBManageDao(Context context) {
		this.mContext=context;
		this.mDBHelper = new SQLiteDBHelper(context);
		this.mSQLiteDatabase = this.mDBHelper.getWritableDatabase();
	}
	
	public Context getContext(){
		return mContext;
	}
	
	public SQLiteDBHelper getDBHelper() {
		return mDBHelper;
	}

	public SQLiteDatabase getSQLiteDatabase() {
		return mSQLiteDatabase;
	}

}