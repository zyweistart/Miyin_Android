package com.start.core;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
	
	public void saveChannelItem(WordItem ci){
		ContentValues values = new ContentValues();
		values.put("id", ci.getId());
		values.put("name", ci.getName());
		values.put("orderId", ci.getOrderId());
		values.put("selected", ci.getSelected());
		getSQLiteDatabase().insert(WordItem.TABLENAME, null, values);
	}
	
	public List<WordItem> findChannelItemAll(int selected) {
		List<WordItem> channelItems = new ArrayList<WordItem>();
		Cursor cursor = getSQLiteDatabase().query(WordItem.TABLENAME,
				new String[] { "id", "name", "orderId" },"selected=?", new String[]{selected+""}, null, null, "orderId asc");
		try {
			if (cursor.moveToFirst()) {
				do {
					WordItem ci = new WordItem();
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