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
	 * 用户ID
	 */
	public String userName;
	/**
	 * 加入时间
	 */
	public String joinTime;
	/**
	 * 类型1:生词本2：错词本
	 */
	private String type;
	/**
	 * 临时变量
	 */
	public String temp1;
	
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTemp1() {
		return temp1;
	}

	public void setTemp1(String temp1) {
		this.temp1 = temp1;
	}

	public static String getCreateTableSQL(){
		StringBuilder strBuilder=new StringBuilder();
		strBuilder.append("CREATE TABLE ");
		strBuilder.append(TABLENAME);
		strBuilder.append("(");
		strBuilder.append("id integer primary key autoincrement,");
		strBuilder.append("pindex varchar,");
		strBuilder.append("userName varchar,");
		strBuilder.append("joinTime varchar,");
		strBuilder.append("type varchar");
		strBuilder.append(")");
		return strBuilder.toString();
	}
	
}