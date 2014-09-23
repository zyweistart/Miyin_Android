package com.start.bean.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.start.bean.HealthInformationCategoryBean;
import com.start.core.DBManageDao;

public class HealthInformationCategoryDao extends DBManageDao {

	public HealthInformationCategoryDao(Context context) {
		super(context);
	}
	
	public void save(HealthInformationCategoryBean bean){
		ContentValues values = new ContentValues();
		values.put(HealthInformationCategoryBean.IDFIELD, bean.getId());
		values.put(HealthInformationCategoryBean.ICONFIELD, bean.getIcon());
		values.put(HealthInformationCategoryBean.NAMEFIELD, bean.getName());
		values.put(HealthInformationCategoryBean.INDEXFIELD, bean.getIndex());
		if(bean.getId()>0){
			getSQLiteDatabase().update(HealthInformationCategoryBean.TABLENAME, values, HealthInformationCategoryBean.IDFIELD+"=?", new String[]{String.valueOf(bean.getId())});
		}else{
			getSQLiteDatabase().insert(HealthInformationCategoryBean.TABLENAME, null, values);
		}
	}
	
	public void deleteAll(){
		getSQLiteDatabase().delete(HealthInformationCategoryBean.TABLENAME, null,null);
	}
	
	public List<HealthInformationCategoryBean> getAll(){
		List<HealthInformationCategoryBean> sbs=new ArrayList<HealthInformationCategoryBean>();
		Cursor cursor = getSQLiteDatabase().query(HealthInformationCategoryBean.TABLENAME,
				new String[] {
				HealthInformationCategoryBean.IDFIELD,
				HealthInformationCategoryBean.ICONFIELD, 
				HealthInformationCategoryBean.NAMEFIELD, 
				HealthInformationCategoryBean.INDEXFIELD }, null,null, null, null, null);
		try {
			if (cursor.moveToFirst()) {
				do {
					HealthInformationCategoryBean sb = new HealthInformationCategoryBean();
					sb.setId(cursor.getInt(cursor.getColumnIndex(HealthInformationCategoryBean.IDFIELD)));
					sb.setIcon(cursor.getString(cursor.getColumnIndex(HealthInformationCategoryBean.ICONFIELD)));
					sb.setName(cursor.getString(cursor.getColumnIndex(HealthInformationCategoryBean.NAMEFIELD)));
					sb.setIndex(cursor.getString(cursor.getColumnIndex(HealthInformationCategoryBean.INDEXFIELD)));
					sbs.add(sb);
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}
		return sbs;
	}
	
}