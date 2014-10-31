package com.ancun.bean;

import start.utils.HanziToPinyin;


public class ContactModel {

	public static final String TABLENAME="AC_CONTACT";
	/**
	 * 联系人id
	 */
	private Long id;
	/**
	 * 系统头像照片ID
	 */
	private Long photoID;
	/**
	 * 联系人姓名
	 */
	private String name;
	/**
	 * 电话
	 */
	private String phone;
	/**
	 * 联系人查询id 值为一个字符串 例： 0r2-E037D4E01ADCE02196.0r3-E037D4E01ADCE02196
	 */
	private String lookupKey;
	/**
	 * 联系人 拨号时弹窗提醒的记录标志 默认为2
	 * <pre>
	 * 0:自动录音
	 * 1:提示录音
	 * 2:从不录音
	 * </pre>
	 */
	private Integer recordFlag=2;
	/**
	 * 设置标记
	 * 1:默认
	 * 2: 用户已设置过
	 */
	private Integer userSetFlag=1;
	
	private String pinyinName;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getPhotoID() {
		return photoID;
	}
	
	public void setPhotoID(Long photoID) {
		this.photoID = photoID;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLookupKey() {
		return lookupKey;
	}
	
	public void setLookupKey(String lookupKey) {
		this.lookupKey = lookupKey;
	}
	
	public Integer getRecordFlag() {
		return recordFlag;
	}
	
	public void setRecordFlag(Integer recordFlag) {
		this.recordFlag = recordFlag;
	}

	public Integer getUserSetFlag() {
		return userSetFlag;
	}

	public void setUserSetFlag(Integer userSetFlag) {
		this.userSetFlag = userSetFlag;
	}

	public String getPinyinName() {
		if(pinyinName==null){
			if(name!=null&&!"".equals(name)){
				pinyinName=HanziToPinyin.getPinYin(name.toLowerCase());
			}else{
				pinyinName="";
			}
		}
		return pinyinName;
	}

}