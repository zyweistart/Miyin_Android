package com.start.service.bean;

import java.io.Serializable;

/** 
 * 生词本索引
 *  */
public class StrangeWordItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6465237897027410019L;
	
	public static final String TABLENAME="StrangeWords";
	/**
	 * 序号
	 */
	public String id;
	/**
	 * 索引
	 */
	public String index;
	/**
	 * 加入时间
	 */
	public String joinTime;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(String joinTime) {
		this.joinTime = joinTime;
	}

	public static String getCreateTableSQL(){
		StringBuilder strBuilder=new StringBuilder();
		strBuilder.append("CREATE TABLE ");
		strBuilder.append(TABLENAME);
		strBuilder.append("(");
		strBuilder.append("id int auto_increment PRIMARY KEY,");
		strBuilder.append("pindex varchar,");
		strBuilder.append("joinTime varchar");
		strBuilder.append(")");
		return strBuilder.toString();
	}
	
}