package com.start.model;

import com.start.core.CoreModel;

public class Edge extends CoreModel {

	public static final String TABLE_NAME="ST_EDGE";
	
	public static final String COLUMN_NAME_VERTEXSTARTID="vertexStartId";
	public static final String COLUMN_NAME_VERTEXENDID="vertexEndId";
	
	public static final String CREATE_TABLE_SQL = 
			"CREATE TABLE " + TABLE_NAME + " ("
			+ _ID + " TEXT,"
			+ COLUMN_NAME_NO + " TEXT,"
			+ COLUMN_NAME_VERTEXSTARTID + " TEXT,"
			+ COLUMN_NAME_VERTEXENDID + " TEXT"
			+ ");";

	private String vertexStartId;
	
	private String vertexEndId;

	public String getVertexStartId() {
		return vertexStartId;
	}

	public void setVertexStartId(String vertexStartId) {
		this.vertexStartId = vertexStartId;
	}

	public String getVertexEndId() {
		return vertexEndId;
	}

	public void setVertexEndId(String vertexEndId) {
		this.vertexEndId = vertexEndId;
	}
	
}