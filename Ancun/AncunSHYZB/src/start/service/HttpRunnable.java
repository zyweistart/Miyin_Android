package start.service;

import start.core.AppException;

public interface HttpRunnable{
	
	public void run(Response response) throws AppException;
	
}
