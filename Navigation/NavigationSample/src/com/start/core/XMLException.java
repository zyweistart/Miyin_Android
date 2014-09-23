package com.start.core;

/**
 * XML异常
 * @author Start
 */
public class XMLException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public XMLException(Throwable e){
		super(e);
	}

	public XMLException(String message){
		super(message);
	} 
	
}