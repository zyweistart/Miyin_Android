package com.start.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.start.model.Department;
import com.start.model.DepartmentHasRoom;
import com.start.model.Doctor;
import com.start.model.Edge;
import com.start.model.FriendHistory;
import com.start.model.Introduction;
import com.start.model.MapData;
import com.start.model.Room;
import com.start.model.RoomArea;
import com.start.model.Vertex;

public class SQLiteDBHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME="navigation.db";
	
	public SQLiteDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(Department.CREATE_TABLE_SQL);
		db.execSQL(DepartmentHasRoom.CREATE_TABLE_SQL);
		db.execSQL(Doctor.CREATE_TABLE_SQL);
		db.execSQL(Edge.CREATE_TABLE_SQL);
		db.execSQL(FriendHistory.CREATE_TABLE_SQL);
		db.execSQL(Introduction.CREATE_TABLE_SQL);
		db.execSQL(MapData.CREATE_TABLE_SQL);
		db.execSQL(Room.CREATE_TABLE_SQL);
		db.execSQL(RoomArea.CREATE_TABLE_SQL);
		db.execSQL(Vertex.CREATE_TABLE_SQL);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + Department.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + DepartmentHasRoom.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + Doctor.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + Edge.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + FriendHistory.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + Introduction.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + MapData.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + Room.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + RoomArea.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + Vertex.TABLE_NAME);
		onCreate(db);
	}
	
}