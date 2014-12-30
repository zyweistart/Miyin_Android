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
	 * 栏目对应ID
	 *  */
	public Integer id;
	/** 
	 * 栏目对应NAME
	 *  */
	public String name;
	/** 
	 * 栏目在整体中的排序顺序  rank
	 *  */
	public Integer orderId;
	/** 
	 * 栏目是否选中
	 *  */
	public Integer selected;

	public WordItem() {
	}

	public WordItem(int id, String name, int orderId,int selected) {
		this.id = Integer.valueOf(id);
		this.name = name;
		this.orderId = Integer.valueOf(orderId);
		this.selected = Integer.valueOf(selected);
	}

	public int getId() {
		return this.id.intValue();
	}

	public String getName() {
		return this.name;
	}

	public int getOrderId() {
		return this.orderId.intValue();
	}

	public Integer getSelected() {
		return this.selected;
	}

	public void setId(int paramInt) {
		this.id = Integer.valueOf(paramInt);
	}

	public void setName(String paramString) {
		this.name = paramString;
	}

	public void setOrderId(int paramInt) {
		this.orderId = Integer.valueOf(paramInt);
	}

	public void setSelected(Integer paramInteger) {
		this.selected = paramInteger;
	}

	
	public static String getCreateTableSQL(){
		StringBuilder strBuilder=new StringBuilder();
		strBuilder.append("CREATE TABLE ");
		strBuilder.append(TABLENAME);
		strBuilder.append("(");
		strBuilder.append("id integer,");
		strBuilder.append("name varchar,");
		strBuilder.append("orderId integer,");
		strBuilder.append("selected integer");
		strBuilder.append(")");
		return strBuilder.toString();
	}
	
	public String toString() {
		return "ChannelItem [id=" + this.id + ", name=" + this.name
				+ ", selected=" + this.selected + "]";
	}
}