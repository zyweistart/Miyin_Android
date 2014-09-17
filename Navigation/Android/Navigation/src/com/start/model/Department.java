package com.start.model;

import com.start.core.CoreModel;


public class Department extends CoreModel {

	public static final String TABLE_NAME="ST_DEPARTMENT";
	
	public static final String COLUMN_NAME_NAME="name";
	public static final String COLUMN_NAME_INTRODUCTION="introduction";
	public static final String COLUMN_NAME_MAJORROOMID="majorroomid";
	
	public static final String CREATE_TABLE_SQL = 
			"CREATE TABLE " + TABLE_NAME + " ("
			+ COLUMN_NAME_ID + " TEXT,"
			+ COLUMN_NAME_NO + " TEXT,"
			+ COLUMN_NAME_NAME + " TEXT,"
			+ COLUMN_NAME_INTRODUCTION + " TEXT,"
			+ COLUMN_NAME_MAJORROOMID + " TEXT"
			+ ");";
	
	/**
	 * 科室所在地图编号
	 */
	private String mapId;
	/**
	 * 科室名字
	 */
	private String name;
	/**
	 * 科室简介
	 */
	private String introduction;
	
	private String majorRoomId;
	/**
	 * 科室所在所有房间编号
	 */
	private Room[] rooms;

	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getMajorRoomId() {
		return majorRoomId;
	}

	public void setMajorRoomId(String majorRoomId) {
		this.majorRoomId = majorRoomId;
	}

	public Room[] getRooms() {
		return rooms;
	}

	public void setRooms(Room[] rooms) {
		this.rooms = rooms;
	}

	@Override
	public String toString() {
		return name;
	}

}
