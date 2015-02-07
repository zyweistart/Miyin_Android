package com.start.service.bean;


/** 
 * 生词本统计
 *  */
public class StrangeWordStatisticsItem {
	
	/**
	 * 用户ID
	 */
	public String userName;
	/**
	 * 加入时间
	 */
	public String joinTime;
	/**
	 * 单词统计
	 */
	public String wordCount;
	
	private String type;
	
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
	
	public String getWordCount() {
		return wordCount;
	}
	
	public void setWordCount(String wordCount) {
		this.wordCount = wordCount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}