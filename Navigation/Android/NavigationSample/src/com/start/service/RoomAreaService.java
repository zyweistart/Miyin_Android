package com.start.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.start.core.CoreService;
import com.start.model.RoomArea;

public class RoomAreaService extends CoreService {

	public RoomAreaService(Context context) {
		super(context);
	}

	/**
	 * 根据roomId获取房间区域内的所有点
	 */
	public List<RoomArea> findAllByRoomId(String roomId){
		List<RoomArea> roomAreas = new ArrayList<RoomArea>();
		Cursor cursor = getDbHelper().getReadableDatabase().query(RoomArea.TABLE_NAME, 
				new String[]{
					RoomArea.COLUMN_NAME_ID,
					RoomArea.COLUMN_NAME_ROOMID,
					RoomArea.COLUMN_NAME_LATITUDE,
					RoomArea.COLUMN_NAME_LONGITUDE},
					RoomArea.COLUMN_NAME_ROOMID+" = ? AND "+RoomArea.COLUMN_NAME_NO+" = ?",
					new String[]{roomId,getCurrentDataNo()}, null, null, null);
		try{
			if(cursor.moveToFirst()){
				do {
					RoomArea roomArea = new RoomArea();
					roomArea.setId(cursor.getString(cursor.getColumnIndex(RoomArea.COLUMN_NAME_ID)));
					roomArea.setRoomId(cursor.getString(cursor.getColumnIndex(RoomArea.COLUMN_NAME_ROOMID)));
					roomArea.setLatitude(cursor.getString(cursor.getColumnIndex(RoomArea.COLUMN_NAME_LATITUDE)));
					roomArea.setLongitude(cursor.getString(cursor.getColumnIndex(RoomArea.COLUMN_NAME_LONGITUDE)));
					roomAreas.add(roomArea);
				} while (cursor.moveToNext());
			}
		}finally{
			cursor.close();
		}
		return roomAreas;
	}
	
}
