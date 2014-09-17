package com.start.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.start.core.CoreService;
import com.start.model.DepartmentHasRoom;

public class DepartmentHasRoomService extends CoreService {

	public DepartmentHasRoomService(Context context) {
		super(context);
	}

	public List<DepartmentHasRoom> findAll(){
		List<DepartmentHasRoom> departmentHasRooms = new ArrayList<DepartmentHasRoom>();
		Cursor cursor = getDbHelper().getReadableDatabase().query(DepartmentHasRoom.TABLE_NAME, 
				new String[]{
				DepartmentHasRoom.COLUMN_NAME_ID,
				DepartmentHasRoom.COLUMN_NAME_DEPARTMENTID,
				DepartmentHasRoom.COLUMN_NAME_ROOMID},
				DepartmentHasRoom.COLUMN_NAME_NO+" = ?",
				new String[]{getCurrentDataNo()}, null, null, null);
		try{
			if(cursor.moveToFirst()){
				do {
					DepartmentHasRoom departmentHasRoom = new DepartmentHasRoom();
					departmentHasRoom.setId(cursor.getString(cursor.getColumnIndex(DepartmentHasRoom.COLUMN_NAME_ID)));
					departmentHasRoom.setDepartmentId(cursor.getString(cursor.getColumnIndex(DepartmentHasRoom.COLUMN_NAME_DEPARTMENTID)));
					departmentHasRoom.setRoomId(cursor.getString(cursor.getColumnIndex(DepartmentHasRoom.COLUMN_NAME_ROOMID)));
					departmentHasRooms.add(departmentHasRoom);
				} while (cursor.moveToNext());
			}
		}finally{
			cursor.close();
		}
		return departmentHasRooms;
	}
	
	public List<DepartmentHasRoom> findByDepartmentId(String departmentId){
		List<DepartmentHasRoom> departmentHasRooms = new ArrayList<DepartmentHasRoom>();
		Cursor cursor = getDbHelper().getReadableDatabase().query(DepartmentHasRoom.TABLE_NAME, 
				new String[]{
				DepartmentHasRoom.COLUMN_NAME_ID,
				DepartmentHasRoom.COLUMN_NAME_DEPARTMENTID,
				DepartmentHasRoom.COLUMN_NAME_ROOMID},
				DepartmentHasRoom.COLUMN_NAME_DEPARTMENTID+" = ? AND "+DepartmentHasRoom.COLUMN_NAME_NO+" = ?",
				new String[]{departmentId,getCurrentDataNo()},null, null, null);
		try{
			if(cursor.moveToFirst()){
				do {
					DepartmentHasRoom departmentHasRoom = new DepartmentHasRoom();
					departmentHasRoom.setId(cursor.getString(cursor.getColumnIndex(DepartmentHasRoom.COLUMN_NAME_ID)));
					departmentHasRoom.setDepartmentId(cursor.getString(cursor.getColumnIndex(DepartmentHasRoom.COLUMN_NAME_DEPARTMENTID)));
					departmentHasRoom.setRoomId(cursor.getString(cursor.getColumnIndex(DepartmentHasRoom.COLUMN_NAME_ROOMID)));
					departmentHasRooms.add(departmentHasRoom);
				} while (cursor.moveToNext());
			}
		}finally{
			cursor.close();
		}
		return departmentHasRooms;
	}
	
	public DepartmentHasRoom findByRoomId(String roomId){
		Cursor cursor = getDbHelper().getReadableDatabase().query(DepartmentHasRoom.TABLE_NAME, 
				new String[]{
				DepartmentHasRoom.COLUMN_NAME_ID,
				DepartmentHasRoom.COLUMN_NAME_DEPARTMENTID,
				DepartmentHasRoom.COLUMN_NAME_ROOMID},
				DepartmentHasRoom.COLUMN_NAME_ROOMID+" = ? AND "+DepartmentHasRoom.COLUMN_NAME_NO+" = ?",
				new String[]{roomId,getCurrentDataNo()},null, null, null);
		try{
			if(cursor.moveToFirst()){
				DepartmentHasRoom departmentHasRoom = new DepartmentHasRoom();
				departmentHasRoom.setId(cursor.getString(cursor.getColumnIndex(DepartmentHasRoom.COLUMN_NAME_ID)));
				departmentHasRoom.setDepartmentId(cursor.getString(cursor.getColumnIndex(DepartmentHasRoom.COLUMN_NAME_DEPARTMENTID)));
				departmentHasRoom.setRoomId(cursor.getString(cursor.getColumnIndex(DepartmentHasRoom.COLUMN_NAME_ROOMID)));
				return departmentHasRoom;
			}
		}finally{
			cursor.close();
		}
		return null;
	}
	
}