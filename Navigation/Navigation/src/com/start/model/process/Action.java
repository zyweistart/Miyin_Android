package com.start.model.process;

public class Action {

	public enum ActionType{
		LOCATION,//定位
		OPENWEB,//打开浏览器
		DIALOG//弹出对话框
	}
	
	private ActionType type; 
	
	private String message;
	
	private String content;
	
	private String url;

	public ActionType getType() {
		return type;
	}

	public void setType(ActionType type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}