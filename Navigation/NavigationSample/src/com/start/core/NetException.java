package com.start.core;

/**
 * 网络异常
 * @author Start
 */
public class NetException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public NetException(Throwable e){
		super(e);
	}

}