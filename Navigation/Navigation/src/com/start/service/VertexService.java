package com.start.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.start.core.CoreService;
import com.start.model.Vertex;

public class VertexService extends CoreService {

	public VertexService(Context context) {
		super(context);
	}
	
	/**
	 * 根据ID获取节点
	 */
	public Vertex findById(String id){
		Vertex vertex=null;
		Cursor cursor = getDbHelper().getReadableDatabase().query(Vertex.TABLE_NAME, 
				new String[]{
					Vertex.COLUMN_NAME_ID,
					Vertex.COLUMN_NAME_MAPID,
					Vertex.COLUMN_NAME_LATITUDE,
					Vertex.COLUMN_NAME_LONGITUDE},
					Vertex.COLUMN_NAME_ID+" = ? AND "+Vertex.COLUMN_NAME_NO+" = ?",
					new String[]{id,getCurrentDataNo()}, null, null, null);
		try{
			if(cursor.moveToFirst()){
				do {
					vertex = new Vertex();
					vertex.setId(cursor.getString(cursor.getColumnIndex(Vertex.COLUMN_NAME_ID)));
					vertex.setMapId(cursor.getString(cursor.getColumnIndex(Vertex.COLUMN_NAME_MAPID)));
					vertex.setLatitude(cursor.getString(cursor.getColumnIndex(Vertex.COLUMN_NAME_LATITUDE)));
					vertex.setLongitude(cursor.getString(cursor.getColumnIndex(Vertex.COLUMN_NAME_LONGITUDE)));
				} while (cursor.moveToNext());
			}
		}finally{
			cursor.close();
		}
		return vertex;
	}
	
	/**
	 * 获取所有的点
	 * @return
	 */
	public List<Vertex> findAll(){
		List<Vertex> vertexs = new ArrayList<Vertex>();
		Cursor cursor = getDbHelper().getReadableDatabase().query(Vertex.TABLE_NAME, 
				new String[]{
					Vertex.COLUMN_NAME_ID,
					Vertex.COLUMN_NAME_MAPID,
					Vertex.COLUMN_NAME_LATITUDE,
					Vertex.COLUMN_NAME_LONGITUDE},
					Vertex.COLUMN_NAME_NO+" = ?",
					new String[]{getCurrentDataNo()},
					null, null, null);
		try{
			if(cursor.moveToFirst()){
				do {
					Vertex vertex = new Vertex();
					vertex.setId(cursor.getString(cursor.getColumnIndex(Vertex.COLUMN_NAME_ID)));
					vertex.setMapId(cursor.getString(cursor.getColumnIndex(Vertex.COLUMN_NAME_MAPID)));
					vertex.setLatitude(cursor.getString(cursor.getColumnIndex(Vertex.COLUMN_NAME_LATITUDE)));
					vertex.setLongitude(cursor.getString(cursor.getColumnIndex(Vertex.COLUMN_NAME_LONGITUDE)));
					vertexs.add(vertex);
				} while (cursor.moveToNext());
			}
		}finally{
			cursor.close();
		}
		return vertexs;
	}
	/**
	 * 获了当前地图上所有的点
	 * @return
	 */
	public List<Vertex> findAllByMapId(String mapId){
		List<Vertex> vertexs = new ArrayList<Vertex>();
		Cursor cursor = getDbHelper().getReadableDatabase().query(Vertex.TABLE_NAME, 
				new String[]{
					Vertex.COLUMN_NAME_ID,
					Vertex.COLUMN_NAME_MAPID,
					Vertex.COLUMN_NAME_LATITUDE,
					Vertex.COLUMN_NAME_LONGITUDE},
					Vertex.COLUMN_NAME_MAPID+" = ? AND "+Vertex.COLUMN_NAME_NO+" = ?",
					new String[]{mapId,getCurrentDataNo()}, null, null, null);
		try{
			if(cursor.moveToFirst()){
				do {
					Vertex vertex = new Vertex();
					vertex.setId(cursor.getString(cursor.getColumnIndex(Vertex.COLUMN_NAME_ID)));
					vertex.setMapId(cursor.getString(cursor.getColumnIndex(Vertex.COLUMN_NAME_MAPID)));
					vertex.setLatitude(cursor.getString(cursor.getColumnIndex(Vertex.COLUMN_NAME_LATITUDE)));
					vertex.setLongitude(cursor.getString(cursor.getColumnIndex(Vertex.COLUMN_NAME_LONGITUDE)));
					vertexs.add(vertex);
				} while (cursor.moveToNext());
			}
		}finally{
			cursor.close();
		}
		return vertexs;
	}
	
}
