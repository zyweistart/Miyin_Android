package com.ancun.bean;

public class RecentModel {
	/**
	 * 表名
	 */
	public final static String TABLENAME="AC_RECENT";
	
	public RecentModel(){}
	/**
	 * 主键
	 */
	private Integer recent_id;
	/**
	 * 显示名称
	 */
	private String name;
	/**
	 * 自己的手机号码
	 */
	private String oppo;
	/**
	 * 对方号码
	 */
	private String phone;
	/**
	 * 通过时间
	 */
	private String calltime;
	/**
	 * 状态
	 * 1:呼入
	 * 2:呼出
	 * 3:未接通
	 */
	private Integer status;

	public Integer getRecent_id() {
		return recent_id;
	}

	public void setRecent_id(Integer recent_id) {
		this.recent_id = recent_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOppo() {
		return oppo;
	}

	public void setOppo(String oppo) {
		this.oppo = oppo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCalltime() {
		return calltime;
	}

	public void setCalltime(String calltime) {
		this.calltime = calltime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
