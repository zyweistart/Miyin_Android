package com.start.service.bean;

import java.io.Serializable;

/** 
 * 单词类
 *  */
public class WordTestItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6465237897027410019L;
	
	public static final String TABLENAME="WordTestItem";
	/**
	 * 序号
	 */
	public String id;
	/**
	 * 测试标题
	 */
	public String testIndex;
	/**
	 * 答案
	 */
	public String answerIndex;
	/**
	 * 答案A
	 */
	public String aIndex;
	/**
	 * 答案B
	 */
	public String bIndex;
	/**
	 * 答案C
	 */
	public String cIndex;
	/**
	 * 答案D
	 */
	public String dIndex;
	
}