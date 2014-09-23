package com.start.core;

import java.util.HashMap;

public class AppConfig {

	// Configuration directory
	public static final String CONFIG_DATA_PATH_MEDMAP = "med_data";
	
	//地图文件
	public static final String F_MAPMAIN="main.map";
	public static final String F_MAP0101="0101.map";
	public static final String F_MAP0102="0102.map";
	public static final String F_MAP0201="0201.map";
	public static final String F_MAP0202="0202.map";
	//数据文件
	public static final String F_DEPARTMENT="department.txt";
	public static final String F_DEPARTMENTHASROOM="department_has_room.txt";
	public static final String F_DOCTOR="doctor.txt";
	public static final String F_EDGE="edge.txt";
	public static final String F_MAPDATA="mapdata.txt";
	public static final String F_ROOM="room.txt";
	public static final String F_ROOMAREA="roomarea.txt";
	public static final String F_VERTEX="vertex.txt";
	public static final String F_INTRODUCTION="introduction.txt";
	
	// Configuration file type
	public static final int TYPE_UNKNOWN = -1;
	public static final int TYPE_DEPARTMENT = 0;
	public static final int TYPE_ROOM = 1;
	public static final int TYPE_MAPDATA = 2;
	public static final int TYPE_VERTEX = 3;
	public static final int TYPE_DEPARTMENTHASROOM = 4;
	public static final int TYPE_EDGE = 5;
	public static final int TYPE_DOCTOR =6;
	public static final int TYPE_ROOMAREA =7;
	public static final int TYPE_MAP = 8;
	public static final int TYPE_INTRODUCTION = 9;

	// File extension matcher
	private static final HashMap<String, Integer> mFileExtMatcher = new HashMap<String, Integer>();

	static {
		//地图文件
		mFileExtMatcher.put(F_MAPMAIN, TYPE_MAP);
		mFileExtMatcher.put(F_MAP0101, TYPE_MAP);
		mFileExtMatcher.put(F_MAP0102, TYPE_MAP);
		mFileExtMatcher.put(F_MAP0201, TYPE_MAP);
		mFileExtMatcher.put(F_MAP0202, TYPE_MAP);
		//数据文件
		mFileExtMatcher.put(F_DEPARTMENT, TYPE_DEPARTMENT);
		mFileExtMatcher.put(F_DEPARTMENTHASROOM, TYPE_DEPARTMENTHASROOM);
		mFileExtMatcher.put(F_DOCTOR, TYPE_DOCTOR);
		mFileExtMatcher.put(F_EDGE, TYPE_EDGE);
		mFileExtMatcher.put(F_MAPDATA, TYPE_MAPDATA);
		mFileExtMatcher.put(F_ROOM, TYPE_ROOM);
		mFileExtMatcher.put(F_ROOMAREA, TYPE_ROOMAREA);
		mFileExtMatcher.put(F_VERTEX, TYPE_VERTEX);
		mFileExtMatcher.put(F_INTRODUCTION, TYPE_INTRODUCTION);
	}

	public static int getFileType(String fileName) {
		if (mFileExtMatcher.containsKey(fileName)) {
			return mFileExtMatcher.get(fileName);
		} else {
			return TYPE_UNKNOWN;
		}
	}
	
}