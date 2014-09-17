package com.start.bean;

import java.io.Serializable;

public class HealthInformationCategoryBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String TABLENAME="INFORMATIONCATEGORY";
	
	public static final String IDFIELD="ID";
	public static final String ICONFIELD="ICON";
	public static final String NAMEFIELD="NAME";
	public static final String INDEXFIELD="INDEX";
	
	private int id;
	private String icon;
	private String name;
	private String index;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}
	
	public static String createSql(){
		StringBuilder sql=new StringBuilder();
		sql.append("CREATE TABLE ");
		sql.append(HealthInformationCategoryBean.TABLENAME);
		sql.append("(");
		sql.append(IDFIELD+" integer primary key autoincrement,");
		sql.append(ICONFIELD+" varchar,");
		sql.append(NAMEFIELD+" varchar,");
		sql.append(INDEXFIELD+" varchar");
		sql.append(")");
		return sql.toString();
	}
	
}