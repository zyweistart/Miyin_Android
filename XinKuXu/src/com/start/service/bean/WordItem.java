package com.start.service.bean;

import java.io.Serializable;

/** 
 * 单词类
 *  */
public class WordItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6465237897027410019L;
	
	public static final String TABLENAME="WordItem";
	/**
	 * 序号
	 */
	public String id;
	/**
	 * 英文
	 */
	public String englishName;
	/**
	 * 音标
	 */
	public String phoneticSymbols;
	/**
	 * 中文词性词义
	 */
	public String chineseSignificance;
	/**
	 * 例句（英）
	 */
	public String exampleEnglish;
	/**
	 * 例句（中译）
	 */
	public String exampleChinese;
	/**
	 * 填空（问题）
	 */
	public String fillProblem;
	/**
	 * 填空（答案）
	 */
	public String fillAnswer;
	/**
	 * 记忆方法1
	 */
	public String memoryMethodA;
	/**
	 * 记忆方法2
	 */
	public String memoryMethodB;
	/**
	 * 例句（图片）
	 */
	public String exampleImage;
	/**
	 * 图形记忆法（图片）
	 */
	public String memoryImage;
	/**
	 * 单词讲解音频
	 */
	public String englishAudio;

	public WordItem() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getPhoneticSymbols() {
		return phoneticSymbols;
	}

	public void setPhoneticSymbols(String phoneticSymbols) {
		this.phoneticSymbols = phoneticSymbols;
	}

	public String getChineseSignificance() {
		return chineseSignificance;
	}

	public void setChineseSignificance(String chineseSignificance) {
		this.chineseSignificance = chineseSignificance;
	}

	public String getExampleEnglish() {
		return exampleEnglish;
	}

	public void setExampleEnglish(String exampleEnglish) {
		this.exampleEnglish = exampleEnglish;
	}

	public String getExampleChinese() {
		return exampleChinese;
	}

	public void setExampleChinese(String exampleChinese) {
		this.exampleChinese = exampleChinese;
	}

	public String getFillProblem() {
		return fillProblem;
	}

	public void setFillProblem(String fillProblem) {
		this.fillProblem = fillProblem;
	}




	public String getFillAnswer() {
		return fillAnswer;
	}




	public void setFillAnswer(String fillAnswer) {
		this.fillAnswer = fillAnswer;
	}




	public String getMemoryMethodA() {
		return memoryMethodA;
	}




	public void setMemoryMethodA(String memoryMethodA) {
		this.memoryMethodA = memoryMethodA;
	}




	public String getMemoryMethodB() {
		return memoryMethodB;
	}




	public void setMemoryMethodB(String memoryMethodB) {
		this.memoryMethodB = memoryMethodB;
	}




	public String getExampleImage() {
		return exampleImage;
	}




	public void setExampleImage(String exampleImage) {
		this.exampleImage = exampleImage;
	}




	public String getMemoryImage() {
		return memoryImage;
	}




	public void setMemoryImage(String memoryImage) {
		this.memoryImage = memoryImage;
	}




	public String getEnglishAudio() {
		return englishAudio;
	}




	public void setEnglishAudio(String englishAudio) {
		this.englishAudio = englishAudio;
	}




	public static String getCreateTableSQL(){
		StringBuilder strBuilder=new StringBuilder();
		strBuilder.append("CREATE TABLE ");
		strBuilder.append(TABLENAME);
		strBuilder.append("(");
		strBuilder.append("id varchar,");
		strBuilder.append("englishName varchar,");
		strBuilder.append("phoneticSymbols varchar,");
		strBuilder.append("chineseSignificance varchar,");
		strBuilder.append("exampleEnglish varchar,");
		strBuilder.append("exampleChinese varchar,");
		strBuilder.append("fillProblem varchar,");
		strBuilder.append("fillAnswer varchar,");
		strBuilder.append("memoryMethodA varchar,");
		strBuilder.append("memoryMethodB varchar,");
		strBuilder.append("exampleImage varchar,");
		strBuilder.append("memoryImage varchar,");
		strBuilder.append("englishAudio varchar");
		strBuilder.append(")");
		return strBuilder.toString();
	}
	
}