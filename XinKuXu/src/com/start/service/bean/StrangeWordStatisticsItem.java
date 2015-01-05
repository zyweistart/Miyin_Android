package com.start.service.bean;

import java.io.Serializable;

/** 
 * 生词本统计
 *  */
public class StrangeWordStatisticsItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6465237897027410019L;
	
	public static final String TABLENAME="StrangeWordStatistics";
	/**
	 * 序号
	 */
	public String id;
	/**
	 * 用户ID
	 */
	public String userName;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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
		strBuilder.append("userName varchar,");
		strBuilder.append("joinTime varchar");
		strBuilder.append(")");
		return strBuilder.toString();
	}
	
}