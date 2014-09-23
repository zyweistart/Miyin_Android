package com.start.model;

import com.start.core.CoreModel;


public class MapData extends CoreModel {
	
	public static final String TABLE_NAME="ST_MAPDATA";
	
	public static final String COLUMN_NAME_NAME="name";
	public static final String COLUMN_NAME_DISPLAY="display";
	public static final String COLUMN_NAME_MAIN="main";
	public static final String COLUMN_NAME_MAINID="mainid";
	public static final String COLUMN_NAME_VERTEXID="vertextId";
	
	public static final String CREATE_TABLE_SQL = 
			"CREATE TABLE " + TABLE_NAME + " ("
			+ _ID + " TEXT,"
			+ COLUMN_NAME_NO + " TEXT,"
			+ COLUMN_NAME_NAME + " TEXT,"
			+ COLUMN_NAME_DISPLAY + " TEXT,"
			+ COLUMN_NAME_MAIN + " TEXT,"
			+ COLUMN_NAME_MAINID + " TEXT,"
			+ COLUMN_NAME_VERTEXID + " TEXT"
			+ ");";
	
	/**
	 * 地图名称
	 */
	private String name;
	/**
	 * 显示
	 */
	private String display;
	/**
	 * 主
	 */
	private String main;
	/**
	 * 园区ID
	 */
	private String mainid;
	/**
	 * 地图数据的初始点
	 */
	private String vertexId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public Boolean isMain(){
		return "1".equals(getMain());
	}
	
	public String getMain() {
		return main;
	}

	public void setMain(String main) {
		this.main = main;
	}

	public String getMainid() {
		return mainid;
	}

	public void setMainid(String mainid) {
		this.mainid = mainid;
	}

	public String getVertexId() {
		return vertexId;
	}

	public void setVertexId(String vertexId) {
		this.vertexId = vertexId;
	}
	
}