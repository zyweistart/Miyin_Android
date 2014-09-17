package com.start.model;

import com.start.core.CoreModel;

public class DepartmentHasRoom extends CoreModel {

	public static final String TABLE_NAME="ST_DEPARTMENTHASROOM";
	
	public static final String COLUMN_NAME_DEPARTMENTID="departmentId";
	public static final String COLUMN_NAME_ROOMID="roomId";
	
	public static final String CREATE_TABLE_SQL = 
			"CREATE TABLE " + TABLE_NAME + " ("
			+ _ID + " INTEGER PRIMARY KEY,"
			+ COLUMN_NAME_NO + " TEXT,"
			+ COLUMN_NAME_DEPARTMENTID + " TEXT,"
			+ COLUMN_NAME_ROOMID + " TEXT"
			+ ");";
	
	private String departmentId;
	
	private String roomId;

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

}