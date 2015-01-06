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
	
	public void joinToStrangeWord(String wordId,String userName){
		//一个单词一个用户仅添加一次
		String str="select count(*)  from "+StrangeWordItem.TABLENAME+" where pindex='"+wordId+"' and userName='"+userName+"'";
        Cursor cursor = mSQLiteDatabase.rawQuery(str,null);
        cursor.moveToFirst();
        if(cursor.getLong(0)==0){
        	ContentValues values = new ContentValues();
    		values.put("pindex", wordId);
    		values.put("userName", userName);
    		values.put("joinTime", TimeUtils.getSysdate());
    		getSQLiteDatabase().insert(StrangeWordItem.TABLENAME, null, values);
        }
	}
	
	/**
	 * 根据加入时间获取所对应的所有生词
	 */
	public List<StrangeWordStatisticsItem> findStrangeWordStatistic(String userName) {
		List<StrangeWordStatisticsItem> channelItems = new ArrayList<StrangeWordStatisticsItem>();
		Cursor cursor = mSQLiteDatabase.query(StrangeWordItem.TABLENAME,
				new String[] { "count(*) xcount", "joinTime"},"userName=?", new String[]{userName}, "joinTime", null, "joinTime desc");
		try {
			if (cursor.moveToFirst()) {
				do {
					StrangeWordStatisticsItem ci = new StrangeWordStatisticsItem();
					ci.setUserName(userName);
					ci.setJoinTime(cursor.getString(cursor.getColumnIndex("joinTime")));
					ci.setWordCount(cursor.getString(cursor.getColumnIndex("xcount")));
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
	public List<StrangeWordItem> findAllByStrangeWordItem(String userName,String joinTime) {
		List<StrangeWordItem> channelItems = new ArrayList<StrangeWordItem>();
		Cursor cursor = mSQLiteDatabase.query(StrangeWordItem.TABLENAME,
				new String[] { "id", "pindex","userName", "joinTime"},"userName=? and joinTime=?", new String[]{userName,joinTime}, null, null, "id desc");
		try {
			if (cursor.moveToFirst()) {
				do {
					StrangeWordItem ci = new StrangeWordItem();
					ci.setId(cursor.getString(cursor.getColumnIndex("id")));
					ci.setIndex(cursor.getString(cursor.getColumnIndex("pindex")));
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

}