package com.start.core;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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