package com.start.model;

import com.start.core.CoreModel;


public class Doctor extends CoreModel {
	
	public static final String TABLE_NAME="ST_DOCTOR";
	
	public static final String COLUMN_NAME_NAME="name";
	public static final String COLUMN_NAME_SEX="sex";
	public static final String COLUMN_NAME_TITLE="title";
	public static final String COLUMN_NAME_SPECIALTY="specialty";
	public static final String COLUMN_NAME_INTRODUCTION="introduction";
	public static final String COLUMN_NAME_DEPARTMENTID="departmentId";
	
	public static final String CREATE_TABLE_SQL = 
			"CREATE TABLE " + TABLE_NAME + " ("
			+ COLUMN_NAME_ID + " TEXT,"
			+ COLUMN_NAME_NO + " TEXT,"
			+ COLUMN_NAME_NAME + " TEXT,"
			+ COLUMN_NAME_SEX + " TEXT,"
			+ COLUMN_NAME_TITLE + " TEXT,"
			+ COLUMN_NAME_SPECIALTY + " TEXT,"
			+ COLUMN_NAME_INTRODUCTION + " TEXT,"
			+ COLUMN_NAME_DEPARTMENTID + " TEXT"
			+ ");";
	
	/**
	 * 医生名字
	 */
	private String name;
	private String sex;
	private String title;
	private String specialty;
	/**
	 * 医生简介
	 */
	private String introduction;
	/**
	 * 医生所属科室
	 */
	private String departmentId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSpecialty() {
		return specialty;
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}
	
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	@Override
	public String toString() {
		return name;
	}

}
