package com.start.service.bean;

import java.io.Serializable;

public class AdvertisingItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6465237897027410019L;
	
	public static final String TABLENAME="AdvertisingItem";
	
	private String fileName;
	
	private String url;
	
	private String startDay;
	
	private String endDay;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getStartDay() {
		return startDay;
	}

	public void setStartDay(String startDay) {
		this.startDay = startDay;
	}

	public String getEndDay() {
		return endDay;
	}

	public void setEndDay(String endDay) {
		this.endDay = endDay;
	}
	
	
	public static String getCreateTableSQL(){
		StringBuilder strBuilder=new StringBuilder();
		strBuilder.append("CREATE TABLE ");
		strBuilder.append(TABLENAME);
		strBuilder.append("(");
		strBuilder.append("id integer primary key autoincrement,");
		strBuilder.append("fileName varchar,");
		strBuilder.append("url varchar,");
		strBuilder.append("startDay varchar,");
		strBuilder.append("endDay varchar");
		strBuilder.append(")");
		return strBuilder.toString();
	}
	
}
