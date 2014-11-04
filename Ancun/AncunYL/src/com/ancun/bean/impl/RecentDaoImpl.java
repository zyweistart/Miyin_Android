package com.ancun.bean.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import start.utils.StringUtils;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.ContactsContract;

import com.ancun.core.Constant;
import com.ancun.core.DBManageDao;
import com.ancun.yzb.adapter.CallRecordsAdapter;

public class RecentDaoImpl extends DBManageDao {

	public RecentDaoImpl(Context context) {
		super(context);
	}

	/**
	 * 获取外部的全部通话记录 需要android.permission.READ_CALL_LOG权限
	 */
	public List<Map<String,String>> findCallRecords() {
		List<Map<String,String>> recents=new ArrayList<Map<String,String>>();
		ContentResolver cr = getContext().getContentResolver();
		SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuilder filterName=new StringBuilder();
		for(int i=0;i<Constant.noCall.size();i++){
			filterName.append("?");
			if(Constant.noCall.size()-1>i){
				filterName.append(",");
			}
		}
		String[] filterCall=new String[Constant.noCall.size()];
		Constant.noCall.toArray(filterCall);
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
					Map<String,String> data=new HashMap<String,String>();
					data.put(CallRecordsAdapter.STRID, String.valueOf(cursor.getInt(0)));
					data.put(CallRecordsAdapter.STRNAME, cursor.getString(1));
					data.put(CallRecordsAdapter.STRPHONE, StringUtils.phoneFormat(cursor.getString(2)));
					data.put(CallRecordsAdapter.STRSTATUS, String.valueOf(cursor.getInt(3)));
					data.put(CallRecordsAdapter.STRCALLTIME, sfd.format(new Date(Long.parseLong(cursor.getString(4)))));
					recents.add(data);
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