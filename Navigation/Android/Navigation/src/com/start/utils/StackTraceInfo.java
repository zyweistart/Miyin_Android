package com.start.utils;

/**
 * 获取当前方法执行的信息，用于异常时保存信息
 * @author Start
 */
public class StackTraceInfo {

	public static String getTraceInfo() {
		StringBuffer sb = new StringBuffer();
		StackTraceElement stackTrace = new Throwable().getStackTrace()[1];
		sb.append("{");
		sb.append("ClassName:").append(stackTrace.getClassName());
		sb.append(";FileName:").append(stackTrace.getFileName());
		sb.append(";MethodName:").append(stackTrace.getMethodName());
		sb.append(";LineNumber:").append(stackTrace.getLineNumber());
		sb.append("}");
		return sb.toString();
	}

}
