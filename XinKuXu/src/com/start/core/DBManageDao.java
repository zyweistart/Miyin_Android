package com.start.core;

import java.util.ArrayList;
import java.util.List;

import start.utils.TimeUtils;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.start.service.bean.StrangeWordItem;
import com.start.service.bean.StrangeWordStatisticsItem;
import com.start.service.bean.WordItem;

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
	
	public void deleteAllChannelItem() {
		getSQLiteDatabase().delete(WordItem.TABLENAME, null, null);
	}
	
	/**
	 * 获取单词总数
	 * @return
	 */
	public Long getWordCount(){
        String str="select count(id)  from "+WordItem.TABLENAME;
        Cursor cursor = mSQLiteDatabase.rawQuery(str,null);
        cursor.moveToFirst();
        return cursor.getLong(0);
	}
	
	public void joinToStrangeWord(String id,String userName){
		ContentValues values = new ContentValues();
		values.put("pindex", id);
		values.put("joinTime", TimeUtils.getSysdate());
		getSQLiteDatabase().insert(StrangeWordItem.TABLENAME, null, values);
		joinToStrangeWordStatistic(userName);
	}
	
	private void joinToStrangeWordStatistic(String userName){
		//只有不存在当天记录才进行添加
		String str="select count(*)  from "+StrangeWordStatisticsItem.TABLENAME+" where userName='"+userName+"'";
        Cursor cursor = mSQLiteDatabase.rawQuery(str,null);
        cursor.moveToFirst();
        if(cursor.getLong(0)==0){
        	ContentValues values = new ContentValues();
    		values.put("userName", userName);
    		values.put("joinTime", TimeUtils.getSysdate());
    		getSQLiteDatabase().insert(StrangeWordStatisticsItem.TABLENAME, null, values);
        }
	}
	
	/**
	 * 获取单词总数
	 * @return
	 */
	public Long getStrangeWordCount(String joinTime){
        String str="select count(id)  from "+StrangeWordItem.TABLENAME+" where joinTime='"+joinTime+"'";
        Cursor cursor = mSQLiteDatabase.rawQuery(str,null);
        cursor.moveToFirst();
        return cursor.getLong(0);
	}
	
	/**
	 * 根据当前用户获取所对应的单日统计信息
	 */
	public List<StrangeWordStatisticsItem> findAllByStrangeWordStatistic(String userName) {
		List<StrangeWordStatisticsItem> channelItems = new ArrayList<StrangeWordStatisticsItem>();
		Cursor cursor = mSQLiteDatabase.query(StrangeWordStatisticsItem.TABLENAME,
				new String[] { "id", "userName", "joinTime"},"userName=?", new String[]{userName}, null, null, "id desc");
		try {
			if (cursor.moveToFirst()) {
				do {
					StrangeWordStatisticsItem ci = new StrangeWordStatisticsItem();
					ci.setId(cursor.getString(cursor.getColumnIndex("id")));
					ci.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
					ci.setJoinTime(cursor.getString(cursor.getColumnIndex("joinTime")));
					channelItems.add(ci);
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}
		return channelItems;
	}
	
	/**
	 * 根据加入时间获取所对应的所有生词
	 */
	public List<StrangeWordItem> findAllByStrangeWordItem(String joinTime) {
		List<StrangeWordItem> channelItems = new ArrayList<StrangeWordItem>();
		Cursor cursor = mSQLiteDatabase.query(StrangeWordItem.TABLENAME,
				new String[] { "id", "pindex", "joinTime"},"joinTime=?", new String[]{joinTime}, null, null, "id desc");
		try {
			if (cursor.moveToFirst()) {
				do {
					StrangeWordItem ci = new StrangeWordItem();
					ci.setId(cursor.getString(cursor.getColumnIndex("id")));
					ci.setIndex(cursor.getString(cursor.getColumnIndex("pindex")));
					ci.setJoinTime(cursor.getString(cursor.getColumnIndex("joinTime")));
					channelItems.add(ci);
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}
		return channelItems;
	}

}