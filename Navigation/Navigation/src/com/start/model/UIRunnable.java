package com.start.model;

import java.util.Map;

public abstract class UIRunnable{
	
	private Map<String,Map<String,String>> content;

	public Map<String, Map<String, String>> getContent() {
		return content;
	}

	public void setContent(Map<String, Map<String, String>> content) {
		this.content = content;
	}

	/**
	 * not on main thread
	 */
	public abstract void run();
	
}