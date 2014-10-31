package start.service;

import java.io.File;

import start.core.AppException;

public interface DownloadRunnable{
	
	public void run(File file) throws AppException;
	
}
