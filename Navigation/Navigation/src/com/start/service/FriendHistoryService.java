package com.start.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.start.core.CoreService;
import com.start.model.FriendHistory;

public class FriendHistoryService extends CoreService {

	public FriendHistoryService(Context context) {
		super(context);
	}

	public List<FriendHistory> findAllByMyId(String myId){
		List<FriendHistory> fhs = new ArrayList<FriendHistory>();
		Cursor cursor = getDbHelper().getReadableDatabase().query(FriendHistory.TABLE_NAME, 
				new String[]{
				FriendHistory.COLUMN_NAME_ID,
				FriendHistory.COLUMN_NAME_MYID,
				FriendHistory.COLUMN_NAME_FRIENDID,
				FriendHistory.COLUMN_NAME_UPDATETIME},
					FriendHistory.COLUMN_NAME_MYID+"=? AND "+
						FriendHistory.COLUMN_NAME_NO+"=?",
						new String[]{myId,getCurrentDataNo()}, null, null, null);
		try{
			if(cursor.moveToFirst()){
				do {
					FriendHistory fh = new FriendHistory();
					fh.setId(cursor.getString(cursor.getColumnIndex(FriendHistory.COLUMN_NAME_ID)));
					fh.setMyId(cursor.getString(cursor.getColumnIndex(FriendHistory.COLUMN_NAME_MYID)));
					fh.setFriendId(cursor.getString(cursor.getColumnIndex(FriendHistory.COLUMN_NAME_FRIENDID)));
					fh.setCreateTime(cursor.getString(cursor.getColumnIndex(FriendHistory.COLUMN_NAME_UPDATETIME)));
					fhs.add(fh);
				} while (cursor.moveToNext());
			}
		}finally{
			cursor.close();
		}
		return fhs;
	}
	
	public FriendHistory findByMyIdAndFriendId(String myId,String friendId){
		Cursor cursor = getDbHelper().getReadableDatabase().query(FriendHistory.TABLE_NAME, 
				new String[]{
				FriendHistory.COLUMN_NAME_ID,
				FriendHistory.COLUMN_NAME_MYID,
				FriendHistory.COLUMN_NAME_FRIENDID,
				FriendHistory.COLUMN_NAME_UPDATETIME},
				FriendHistory.COLUMN_NAME_MYID+"=? AND "+
						FriendHistory.COLUMN_NAME_FRIENDID+"=? AND "+
						FriendHistory.COLUMN_NAME_NO+"=?",
						new String[]{myId,friendId,getCurrentDataNo()}, null, null, null);
		try{
			if(cursor.moveToFirst()){
				FriendHistory fh = new FriendHistory();
				fh.setId(cursor.getString(cursor.getColumnIndex(FriendHistory.COLUMN_NAME_ID)));
				fh.setMyId(cursor.getString(cursor.getColumnIndex(FriendHistory.COLUMN_NAME_MYID)));
				fh.setFriendId(cursor.getString(cursor.getColumnIndex(FriendHistory.COLUMN_NAME_FRIENDID)));
				fh.setCreateTime(cursor.getString(cursor.getColumnIndex(FriendHistory.COLUMN_NAME_UPDATETIME)));
				return fh;
			}
		}finally{
			cursor.close();
		}
		return null;
	}
	
}