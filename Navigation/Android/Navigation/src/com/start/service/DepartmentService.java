package com.start.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.start.core.CoreService;
import com.start.model.Department;

public class DepartmentService extends CoreService {

	public DepartmentService(Context context) {
		super(context);
	}

	/**
	 * 获取所有记录
	 * @return
	 */
	public List<Department> findAll(){
		List<Department> departments = new ArrayList<Department>();
		Cursor cursor = getDbHelper().getReadableDatabase().query(Department.TABLE_NAME, 
				new String[]{
				Department.COLUMN_NAME_ID,
				Department.COLUMN_NAME_NAME,
				Department.COLUMN_NAME_INTRODUCTION,
				Department.COLUMN_NAME_MAJORROOMID},
				Department.COLUMN_NAME_NO+" = ?",
				new String[]{getCurrentDataNo()}, null, null, null);
		try{
			if(cursor.moveToFirst()){
				do {
					Department department = new Department();
					department.setId(cursor.getString(cursor.getColumnIndex(Department.COLUMN_NAME_ID)));
					department.setName(cursor.getString(cursor.getColumnIndex(Department.COLUMN_NAME_NAME)));
					department.setIntroduction(cursor.getString(cursor.getColumnIndex(Department.COLUMN_NAME_INTRODUCTION)));
					department.setMajorRoomId(cursor.getString(cursor.getColumnIndex(Department.COLUMN_NAME_MAJORROOMID)));
					departments.add(department);
				} while (cursor.moveToNext());
			}
		}finally{
			cursor.close();
		}
		return departments;
	}
	
	public Department findById(String Id){
		Department department = null;
		Cursor cursor = getDbHelper().getReadableDatabase().query(Department.TABLE_NAME, 
				new String[]{
				Department.COLUMN_NAME_ID,
				Department.COLUMN_NAME_NAME,
				Department.COLUMN_NAME_INTRODUCTION,
				Department.COLUMN_NAME_MAJORROOMID},
				Department.COLUMN_NAME_ID+" = ? AND "+Department.COLUMN_NAME_NO+" = ?",
				new String[]{Id,getCurrentDataNo()}, null, null, null);
		try{
			if(cursor.moveToFirst()){
				do {
					department = new Department();
					department.setId(cursor.getString(cursor.getColumnIndex(Department.COLUMN_NAME_ID)));
					department.setName(cursor.getString(cursor.getColumnIndex(Department.COLUMN_NAME_NAME)));
					department.setIntroduction(cursor.getString(cursor.getColumnIndex(Department.COLUMN_NAME_INTRODUCTION)));
					department.setMajorRoomId(cursor.getString(cursor.getColumnIndex(Department.COLUMN_NAME_MAJORROOMID)));
				} while (cursor.moveToNext());
			}
		}finally{
			cursor.close();
		}
		return department;
	}
	
}