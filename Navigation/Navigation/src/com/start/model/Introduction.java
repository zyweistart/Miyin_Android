package com.start.model;

import com.start.core.CoreModel;

public class Introduction extends CoreModel {

	public static final String TABLE_NAME="ST_INTRODUCTION";
	
	public static final String COLUMN_NAME_CONTENT="content";
	
	public static final String CREATE_TABLE_SQL = 
			"CREATE TABLE " + TABLE_NAME + " ("
			+ _ID + " TEXT,"
			+ COLUMN_NAME_NO + " TEXT,"
			+ COLUMN_NAME_CONTENT + " TEXT"
			+ ");";
	
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}