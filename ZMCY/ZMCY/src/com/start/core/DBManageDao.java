package com.start.core;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.start.service.bean.AdvertisingItem;
import com.start.service.bean.ChannelItem;

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
		getSQLiteDatabase().delete(ChannelItem.TABLENAME, null, null);
	}
	
	/**
	 * 保存广告
	 * @param ci
	 */
	public void saveAdvertisingItem(AdvertisingItem ci){
		String str="select count(*)  from "+AdvertisingItem.TABLENAME+" where fileName='"+ci.getFileName()+"'";
        Cursor cursor = mSQLiteDatabase.rawQuery(str,null);
        cursor.moveToFirst();
        if(cursor.getLong(0)==0){
    		ContentValues values = new ContentValues();
    		values.put("fileName", ci.getFileName());
    		values.put("url", ci.getUrl());
    		values.put("startDay", ci.getStartDay());
    		values.put("endDay", ci.getEndDay());
    		getSQLiteDatabase().insert(AdvertisingItem.TABLENAME, null, values);
        }
	}
	
	/**
	 * 获取当前时间范围内的广告列表
	 * @param currentDay
	 * @return
	 */
	public List<AdvertisingItem> findAdvertisingItemAll(String currentDay) {
		List<AdvertisingItem> channelItems = new ArrayList<AdvertisingItem>();
		Cursor cursor = getSQLiteDatabase().query(AdvertisingItem.TABLENAME,
				new String[] { "fileName", "url","startDay", "endDay" },"startDay<=? and ?<=endDay", new String[]{currentDay,currentDay}, null, null, "id desc");
		try {
			if (cursor.moveToFirst()) {
				do {
					AdvertisingItem ci = new AdvertisingItem();
					ci.setFileName(cursor.getString(cursor.getColumnIndex("fileName")));
					ci.setUrl(cursor.getString(cursor.getColumnIndex("url")));
					ci.setStartDay(cursor.getString(cursor.getColumnIndex("startDay")));
					ci.setEndDay(cursor.getString(cursor.getColumnIndex("endDay")));
					channelItems.add(ci);
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}
		return channelItems;
	}
	
	public void deleteAdvertising(String fileName) {
		getSQLiteDatabase().delete(AdvertisingItem.TABLENAME, "fileName=?", new String[]{fileName});
	}
	
	public void saveChannelItem(ChannelItem ci){
		ContentValues values = new ContentValues();
		values.put("id", ci.getId());
		values.put("name", ci.getName());
		values.put("orderId", ci.getOrderId());
		values.put("selected", ci.getSelected());
		getSQLiteDatabase().insert(ChannelItem.TABLENAME, null, values);
	}
	
	public List<ChannelItem> findChannelItemAll(int selected) {
		List<ChannelItem> channelItems = new ArrayList<ChannelItem>();
		Cursor cursor = getSQLiteDatabase().query(ChannelItem.TABLENAME,
				new String[] { "id", "name", "orderId" },"selected=?", new String[]{selected+""}, null, null, "orderId asc");
		try {
			if (cursor.moveToFirst()) {
				do {
					ChannelItem ci = new ChannelItem();
					ci.setId(cursor.getInt(cursor.getColumnIndex("id")));
					ci.setName(cursor.getString(cursor.getColumnIndex("name")));
					ci.setOrderId(cursor.getInt(cursor.getColumnIndex("orderId")));
					channelItems.add(ci);
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}
		return channelItems;
	}

}