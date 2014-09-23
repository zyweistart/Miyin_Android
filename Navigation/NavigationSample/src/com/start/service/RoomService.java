package com.start.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;

import com.start.core.CoreService;
import com.start.model.Room;

public class RoomService extends CoreService {

	public RoomService(Context context) {
		super(context);
	}

	/**
	 * 获取所有记录
	 * @return
	 */
	public List<Room> findAll(){
		List<Room> rooms = new ArrayList<Room>();
		Cursor cursor = getDbHelper().getReadableDatabase().query(Room.TABLE_NAME, 
				new String[]{
					Room.COLUMN_NAME_ID,
					Room.COLUMN_NAME_MAPID,
					Room.COLUMN_NAME_NAME,
					Room.COLUMN_NAME_VERTEXID},
					Room.COLUMN_NAME_NO+" = ?",
					new String[]{getCurrentDataNo()}, null, null, null);
		try{
			if(cursor.moveToFirst()){
				do {
					Room room = new Room();
					room.setId(cursor.getString(cursor.getColumnIndex(Room.COLUMN_NAME_ID)));
					room.setMapId(cursor.getString(cursor.getColumnIndex(Room.COLUMN_NAME_MAPID)));
					room.setName(cursor.getString(cursor.getColumnIndex(Room.COLUMN_NAME_NAME)));
					room.setVertexId(cursor.getString(cursor.getColumnIndex(Room.COLUMN_NAME_VERTEXID)));
					rooms.add(room);
				} while (cursor.moveToNext());
			}
		}finally{
			cursor.close();
		}
		return rooms;
	}
	
	/**
	 * 获取所有记录
	 * @return
	 */
	public Room findById(String id){
		Cursor cursor = getDbHelper().getReadableDatabase().query(Room.TABLE_NAME, 
				new String[]{
					Room.COLUMN_NAME_ID,
					Room.COLUMN_NAME_MAPID,
					Room.COLUMN_NAME_NAME,
					Room.COLUMN_NAME_VERTEXID},
					Room.COLUMN_NAME_ID+" = ? AND "+Room.COLUMN_NAME_NO+" = ?",
					new String[]{id,getCurrentDataNo()}, null, null, null);
		try{
			if(cursor.moveToFirst()){
				do {
					Room room = new Room();
					room.setId(cursor.getString(cursor.getColumnIndex(Room.COLUMN_NAME_ID)));
					room.setMapId(cursor.getString(cursor.getColumnIndex(Room.COLUMN_NAME_MAPID)));
					room.setName(cursor.getString(cursor.getColumnIndex(Room.COLUMN_NAME_NAME)));
					room.setVertexId(cursor.getString(cursor.getColumnIndex(Room.COLUMN_NAME_VERTEXID)));
					return room;
				} while (cursor.moveToNext());
			}
		}finally{
			cursor.close();
		}
		return null;
	}
	
	/**
	 * 以Map为key封装所有房间
	 * @return
	 */
	public Map<String,List<Room>> findAllPullMap(){
		Map<String,List<Room>> maps=new HashMap<String,List<Room>>();
		
		List<Room> rooms=findAll();
		for(Room r:rooms){
			List<Room> rs=maps.get(r.getMapId());
			if(rs==null){
				rs=new ArrayList<Room>();
			}
			rs.add(r);
			maps.put(r.getMapId(), rs);
		}
		
		return maps;
	}
	
}
