package com.start.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.annotation.SuppressLint;
import android.os.Environment;

import com.start.core.Constant;

public class LogUtils {

	private final static ConcurrentLinkedQueue<String> logqueue= new ConcurrentLinkedQueue<String>();
	private static final String INFO="INFO";
	private static final String ERROR="ERROR";
	private static final String LOG_PATH=Environment.getExternalStorageDirectory().getPath()+"/ancun/logs/";
	private static final String LOGSUFFIX=".log";
	private static final long LOGMAXFILESIZE=1024l*1024l*1024l;

	static{
		LogThread logThread = new LogThread();
		logThread.setDaemon(true);
		logThread.start();
	}

	private static class LogThread extends Thread {

		@SuppressLint("NewApi")
		public void run() {
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				while (true) {
					if(!logqueue.isEmpty()) {
						String log=logqueue.poll();
						//日志存放路径
						String logPath = LOG_PATH + getTime(TimeUtils.yyyyMMdd)+ "/";
						//日志目录
						File path = new File(logPath);
						if (!path.exists()) {
							path.mkdirs();
						}
						//日志文件
						File logfile = new File(logPath + getTime(TimeUtils.yyyyMMddHH) + LOGSUFFIX);
						if(logfile.exists()) {
							if(logfile.length() >= LOGMAXFILESIZE) {
								File backlogfile = new File(logPath + getTime(TimeUtils.yyyyMMddHHmmsssss) + LOGSUFFIX);
								logfile.renameTo(backlogfile);
								if(backlogfile.canWrite()) {
									backlogfile.setWritable(false);
								}
							}
						}
						RandomAccessFile raf = null;
						try {
							if(!logfile.canWrite()) {
								logfile.setWritable(true);
							}
							raf = new RandomAccessFile(logfile, "rw");
							raf.seek(raf.length());
							raf.write((getTime(TimeUtils.yyyyMMddHHmmsssss_F) + "：" + log + "\r\n").getBytes(Constant.ENCODE));
						} catch (Exception e) {
							printInfo(log);
							printError(e.getMessage()==null?"":e.getMessage().trim());
						} finally {
							if(raf != null) {
								try {
									raf.close();
								} catch (IOException e) {
									LogUtils.logError(StackTraceInfo.getTraceInfo() + e.getMessage());
								}finally{
									raf = null;
								}
							}
						}
					} else {
						TimeUtils.sleep(100);
					}
				}
			}
		}
	}

	public static void log(String str) {
		logqueue.add(str);
	}

	public static void log(String type, String info) {
		log("[" + type + "]==>" + info);
	}

	public static void logInfo(String info) {
		log(INFO, info);
	}

	public static void logError(Exception e) {
		if(Constant.ISTEST){
			e.printStackTrace();
		}
		log(ERROR, e.getLocalizedMessage()+"====>"+e.getMessage());
	}
	
	public static void logError(String info) {
		log(ERROR, info);
	}

	public static void log(boolean flag, String info) {
		log(flag ? INFO : ERROR, info);
	}

	public static void print(String str) {
		System.out.println(getTime(TimeUtils.yyyyMMddHHmmsssss_F) + " " + str);
	}

	public static void printInfo(String info) {
		System.out.println("[INFO]" + getTime(TimeUtils.yyyyMMddHHmmsssss_F) + " " + info);
	}

	public static void printError(String info) {
		System.err.println("[ERROR]" + getTime(TimeUtils.yyyyMMddHHmmsssss_F) + " " + info);
	}

	public static void printLog(String str) {
		print(str);
		log(str);
	}

	public static void printLogInfo(String info) {
		printInfo(info);
		logInfo(info);
	}

	public static void printLogError(String info) {
		printError(info);
		logError(info);
	}

	@SuppressLint("SimpleDateFormat")
	private static String getTime(String formatString) {
		SimpleDateFormat format = new SimpleDateFormat(formatString);
		return format.format(Calendar.getInstance().getTime());
	}

}