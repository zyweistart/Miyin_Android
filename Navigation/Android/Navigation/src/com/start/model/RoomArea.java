package com.start.model;

import com.start.core.CoreModel;

public class RoomArea extends CoreModel {

	public static final String TABLE_NAME="ST_ROOMAREA";
	
	public static final String COLUMN_NAME_ROOMID="roomId";
	public static final String COLUMN_NAME_LATITUDE="latitude";
	public static final String COLUMN_NAME_LONGITUDE="longitude";
	
	public static final String CREATE_TABLE_SQL = 
			"CREATE TABLE " + TABLE_NAME + " ("
			+ _ID + " INTEGER PRIMARY KEY,"
			+ COLUMN_NAME_NO + " TEXT,"
			+ COLUMN_NAME_ROOMID + " TEXT,"
			+ COLUMN_NAME_LATITUDE + " TEXT,"
			+ COLUMN_NAME_LONGITUDE + " TEXT"
			+ ");";

	private String roomId;

	private String latitude;
	
	private String longitude;
	

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

}