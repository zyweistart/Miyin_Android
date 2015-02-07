package com.start.core;

import start.core.AppContext;

public class Config {

	public static final String ServerURL=AppContext.getInstance().getServerURL()+"/Ajax/API/%s.html?signature=%s&timestamp=%s&nonce=%s";
	
}
