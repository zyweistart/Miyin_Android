package com.start.model;

import com.start.core.CoreModel;


public class FriendHistory extends CoreModel {
	
	public static final String TABLE_NAME="ST_FRIENDHISTORY";
	
	public static final String COLUMN_NAME_MYID="myId";
	public static final String COLUMN_NAME_FRIENDID="friendId";
	public static final String COLUMN_NAME_UPDATETIME="uTime";
	
	public static final String CREATE_TABLE_SQL = 
			"CREATE TABLE " + TABLE_NAME + " ("
			+ COLUMN_NAME_ID + " TEXT,"
			+ COLUMN_NAME_NO+ " TEXT,"
			+ COLUMN_NAME_MYID + " TEXT,"
			+ COLUMN_NAME_FRIENDID + " TEXT,"
			+ COLUMN_NAME_UPDATETIME + " TEXT"
			+ ");";
	
	private String myId;
	
	private String friendId;
	
	private String createTime;
	
	public String getMyId() {
		return myId;
	}


	public void setMyId(String myId) {
		this.myId = myId;
	}


	public String getFriendId() {
		return friendId;
	}


	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}


	public String getCreateTime() {
		return createTime;
	}


	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}


	@Override
	public String toString() {
		return myId;
	}

}
