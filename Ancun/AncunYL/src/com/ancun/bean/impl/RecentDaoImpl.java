package com.ancun.bean.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import start.utils.TimeUtils;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.ContactsContract;

import com.ancun.bean.RecentModel;
import com.ancun.core.DBManageDao;

public class RecentDaoImpl extends DBManageDao {

	private List<String> noCall;
	
	public RecentDaoImpl(Context context) {
		super(context);
		noCall=new ArrayList<String>();
		noCall.add("95105856");
	}

	/**
	 * 保存
	 */
	public void save(RecentModel recentModel) {
		ContentValues values = new ContentValues();
		values.put("phone", recentModel.getPhone());
		values.put("calltime", TimeUtils.getSysTime());
		values.put("status", recentModel.getStatus());
		getSQLiteDatabase().insert(RecentModel.TABLENAME, null, values);
	}

	/**
	 * 删除
	 */
	public void delete(Integer id) {
		getSQLiteDatabase().delete(RecentModel.TABLENAME, "recent_id=?",
				new String[] { id.toString() });
	}

	/**
	 * 删除
	 */
	public void deleteByPhone(String phone) {
		getSQLiteDatabase().delete(RecentModel.TABLENAME, "phone=?",
				new String[] { phone });
	}

	/**
	 * 清空
	 */
	public void deleteAll() {
		getSQLiteDatabase().delete(RecentModel.TABLENAME, null, null);
	}

	/**
	 * 根据主键ID查找对象
	 */
	public RecentModel find(Integer id) {
		RecentModel recentModel = null;
		Cursor cursor = getSQLiteDatabase()
				.query(RecentModel.TABLENAME,
						new String[] { "recent_id", "calltime", "phone",
								"status" }, "recent_id=?",
						new String[] { id.toString() }, null, null, null);
		try {
			if (cursor.moveToFirst()) {
				recentModel = new RecentModel();
				recentModel.setRecent_id(cursor.getInt(cursor
						.getColumnIndex("recent_id")));
				recentModel.setCalltime(cursor.getString(cursor
						.getColumnIndex("calltime")));
				recentModel.setPhone(cursor.getString(cursor
						.getColumnIndex("phone")));
				recentModel.setStatus(cursor.getInt(cursor
						.getColumnIndex("status")));
			}
		} finally {
			cursor.close();
		}
		return recentModel;
	}

	/**
	 * 获取所有记录
	 */
	public List<RecentModel> findAll() {
		List<RecentModel> recentModels = new ArrayList<RecentModel>();
		Cursor cursor = getSQLiteDatabase().query(RecentModel.TABLENAME,
				new String[] { "recent_id", "calltime", "phone", "status" },
				null, null, "phone", null, "calltime desc");
		try {
			if (cursor.moveToFirst()) {
				do {
					RecentModel recent = new RecentModel();
					recent.setRecent_id(cursor.getInt(cursor
							.getColumnIndex("recent_id")));
					recent.setCalltime(cursor.getString(cursor
							.getColumnIndex("calltime")));
					recent.setPhone(cursor.getString(cursor
							.getColumnIndex("phone")));
					recent.setStatus(cursor.getInt(cursor
							.getColumnIndex("status")));
					recentModels.add(recent);
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}
		return recentModels;
	}

	/**
	 * 获取所有记录
	 */
	public List<RecentModel> findAllByPhone(String phone) {
		List<RecentModel> recentModels = new ArrayList<RecentModel>();
		Cursor cursor = getSQLiteDatabase().query(RecentModel.TABLENAME,
				new String[] { "recent_id", "calltime", "phone", "status" },
				"phone = ? ", new String[] { phone }, null, null, "calltime desc");
		try {
			if (cursor.moveToFirst()) {
				do {
					RecentModel recent = new RecentModel();
					recent.setRecent_id(cursor.getInt(cursor
							.getColumnIndex("recent_id")));
					recent.setCalltime(cursor.getString(cursor
							.getColumnIndex("calltime")));
					recent.setPhone(cursor.getString(cursor
							.getColumnIndex("phone")));
					recent.setStatus(cursor.getInt(cursor
							.getColumnIndex("status")));
					recentModels.add(recent);
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}
		return recentModels;
	}

	/**
	 * 获取外部的全部通话记录 需要android.permission.READ_CALL_LOG权限
	 */
	public List<RecentModel> findCallRecords() {
		List<RecentModel> recents=new ArrayList<RecentModel>();
		ContentResolver cr = getContext().getContentResolver();
		SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuilder filterName=new StringBuilder();
		for(int i=0;i<noCall.size();i++){
			filterName.append("?");
			if(noCall.size()-1>i){
				filterName.append(",");
			}
		}
		String[] filterCall=new String[noCall.size()];
		noCall.toArray(filterCall);
		Cursor cursor=null;
		try {
			  cursor = cr.query(CallLog.Calls.CONTENT_URI, 
					new String[] {
					CallLog.Calls._ID,
					CallLog.Calls.CACHED_NAME,
					CallLog.Calls.NUMBER, 
					CallLog.Calls.TYPE, 
					CallLog.Calls.DATE }, 
					CallLog.Calls.NUMBER+" not in("+filterName.toString()+")", 
					filterCall,
					CallLog.Calls.DEFAULT_SORT_ORDER);
			if (cursor.moveToFirst()) {
				do {
					RecentModel recentModel = new RecentModel();
					recentModel.setRecent_id(cursor.getInt(0));
					recentModel.setName(cursor.getString(1));
					recentModel.setPhone(cursor.getString(2));
					recentModel.setStatus(cursor.getInt(3));
					recentModel.setCalltime(sfd.format(new Date(Long.parseLong(cursor.getString(4)))));
					recents.add(recentModel);
				} while (cursor.moveToNext());
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if(null!=cursor){
				cursor.close();
			}
		}
		return recents;
	}
	
	public Set<String> findAllRecentPhone() {
		Set<String> phones=new LinkedHashSet<String>();
		StringBuilder filterName=new StringBuilder();
		for(int i=0;i<noCall.size();i++){
			filterName.append("?");
			if(noCall.size()-1>i){
				filterName.append(",");
			}
		}
		String[] filterCall=new String[noCall.size()];
		noCall.toArray(filterCall);
		ContentResolver cr = getContext().getContentResolver();
		Cursor cursor=null;
		try {
			  cursor = cr.query(CallLog.Calls.CONTENT_URI, 
					new String[] {
					CallLog.Calls.CACHED_NAME,
					CallLog.Calls.NUMBER, 
					CallLog.Calls.TYPE, 
					CallLog.Calls.DATE }, 
					CallLog.Calls.NUMBER+" not in("+filterName.toString()+")", 
					filterCall,
					CallLog.Calls.DEFAULT_SORT_ORDER);
			if (cursor.moveToFirst()) {
				do {
					phones.add(cursor.getString(1));
				} while (cursor.moveToNext());
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(null!=cursor){
				cursor.close();
			}
		}
		return phones;
	}
	
	public void insertCallLog(final String telNum){
		ContentResolver cr = getContext().getContentResolver();
		Cursor mCursor = null;
		String name =null;
		try {
			mCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME}, ContactsContract.CommonDataKinds.Phone.NUMBER+" = ?", new String[]{telNum}, null);
			if (null != mCursor) {
				if(mCursor.moveToFirst()){
					name = mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(mCursor != null){
				mCursor.close();
			}
		}
		ContentValues values = new ContentValues(); 
	    values.put(CallLog.Calls.NUMBER,telNum);
	    values.put(CallLog.Calls.DATE, System.currentTimeMillis());
	    values.put(CallLog.Calls.NEW, 1);//0已看1未看
	    values.put(CallLog.Calls.CACHED_NAME, name);
	    //呼入:1 呼出:2
	    values.put(CallLog.Calls.TYPE,2);
	    cr.insert(CallLog.Calls.CONTENT_URI, values);
	}
	
}