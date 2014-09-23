package com.start.model.process;

public class Option {

	private String title;
	private String nextNodeId;
	
	public Option(String title,String nextNodeId){
		this.title=title;
		this.nextNodeId=nextNodeId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getNextNodeId() {
		return nextNodeId;
	}
	
}