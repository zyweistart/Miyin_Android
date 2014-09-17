package com.start.service;

import android.content.Context;
import android.database.Cursor;

import com.start.core.CoreService;
import com.start.model.Introduction;

public class IntroductionService extends CoreService {

	public IntroductionService(Context context) {
		super(context);
	}
	
	public Introduction findCurrentIntroduction(){
		Cursor cursor = getDbHelper().getReadableDatabase().query(Introduction.TABLE_NAME, 
				new String[]{
					Introduction.COLUMN_NAME_ID,
					Introduction.COLUMN_NAME_CONTENT},
					Introduction.COLUMN_NAME_NO+" = ?",
					new String[]{getCurrentDataNo()}, null, null, null);
		try{
			if(cursor.moveToFirst()){
				do {
					Introduction introduction=new Introduction();
					introduction.setId(cursor.getString(cursor.getColumnIndex(Introduction.COLUMN_NAME_ID)));
					introduction.setContent(cursor.getString(cursor.getColumnIndex(Introduction.COLUMN_NAME_CONTENT)));
					return introduction;
				} while (cursor.moveToNext());
			}
		}finally{
			cursor.close();
		}
		return null;
	}
	
}
