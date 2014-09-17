package com.start.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.start.core.CoreService;
import com.start.model.Edge;

public class EdgeService extends CoreService {

	public EdgeService(Context context) {
		super(context);
	}

	/**
	 * 获取所有记录
	 * @return
	 */
	public List<Edge> findAll(){
		List<Edge> edges = new ArrayList<Edge>();
		Cursor cursor = getDbHelper().getReadableDatabase().query(Edge.TABLE_NAME, 
				new String[]{
				Edge.COLUMN_NAME_ID,
				Edge.COLUMN_NAME_VERTEXSTARTID,
				Edge.COLUMN_NAME_VERTEXENDID},
				Edge.COLUMN_NAME_NO+" = ?",
				new String[]{getCurrentDataNo()}, null, null, null);
		try{
			if(cursor.moveToFirst()){
				do {
					Edge edge = new Edge();
					edge.setId(cursor.getString(cursor.getColumnIndex(Edge.COLUMN_NAME_ID)));
					edge.setVertexStartId(cursor.getString(cursor.getColumnIndex(Edge.COLUMN_NAME_VERTEXSTARTID)));
					edge.setVertexEndId(cursor.getString(cursor.getColumnIndex(Edge.COLUMN_NAME_VERTEXENDID)));
					edges.add(edge);
				} while (cursor.moveToNext());
			}
		}finally{
			cursor.close();
		}
		return edges;
	}
	
}