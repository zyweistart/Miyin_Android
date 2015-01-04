package com.start.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.start.service.bean.WordItem;
import com.start.xinkuxue.BaseContext;

public class WordService {
	
	public final String DATABASE_PATH = "englishdb";
	public static final String AUDIOPATH="/audio_word/";
	public static final String EXAMPLEIMAGEPATH="/image_example/";
	public static final String MEMORYIMAGEPATH="/image_memory/";
	
	private String mDirName;
	private SQLiteDatabase mSQLiteDatabase;

	public WordService(String dirName) {
		this.mDirName=dirName;
		String dbFileName = BaseContext.getInstance().getStorageDirectory(DATABASE_PATH)+"words.db";
		File dbFile=new File(dbFileName);
		if(dbFile.exists()){
			mSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFileName, null);
		}
	}
	
	//获取单词讲解音频路径
	public String getAudioPath(String name){
		return BaseContext.getInstance().getStorageDirectory(DATABASE_PATH) + this.mDirName+AUDIOPATH+name;
	}
	
	//获取例句（图片）路径
	public String getExampleImagePath(String name){
		return BaseContext.getInstance().getStorageDirectory(DATABASE_PATH) + this.mDirName+EXAMPLEIMAGEPATH+name;
	}
	
	//获取图形记忆法（图片）路径
	public String getMemoryImagePath(String name){
		return BaseContext.getInstance().getStorageDirectory(DATABASE_PATH) + this.mDirName+MEMORYIMAGEPATH+name;
	}
	
	public WordItem findById(int id) {
		Cursor cursor = mSQLiteDatabase.query(WordItem.TABLENAME,
				new String[] { "id", "englishName", "phoneticSymbols",
				"chineseSignificance", "exampleEnglish", "exampleChinese",
				"fillProblem", "fillAnswer", "memoryMethodA",
				"memoryMethodB"},"id=?", new String[]{String.valueOf(id)}, null, null, null);
		try {
			if (cursor.moveToFirst()) {
				do {
					WordItem ci = new WordItem();
					ci.setId(cursor.getString(cursor.getColumnIndex("id")));
					ci.setEnglishName(cursor.getString(cursor.getColumnIndex("englishName")));
					ci.setPhoneticSymbols(cursor.getString(cursor.getColumnIndex("phoneticSymbols")));
					ci.setChineseSignificance(cursor.getString(cursor.getColumnIndex("chineseSignificance")));
					ci.setExampleEnglish(cursor.getString(cursor.getColumnIndex("exampleEnglish")));
					ci.setExampleChinese(cursor.getString(cursor.getColumnIndex("exampleChinese")));
					ci.setFillProblem(cursor.getString(cursor.getColumnIndex("fillProblem")));
					ci.setFillAnswer(cursor.getString(cursor.getColumnIndex("fillAnswer")));
					ci.setMemoryMethodA(cursor.getString(cursor.getColumnIndex("memoryMethodA")));
					ci.setMemoryMethodB(cursor.getString(cursor.getColumnIndex("memoryMethodB")));
//					ci.setExampleImage(cursor.getString(cursor.getColumnIndex("exampleImage")));
//					ci.setMemoryImage(cursor.getString(cursor.getColumnIndex("memoryImage")));
//					ci.setEnglishAudio(cursor.getString(cursor.getColumnIndex("englishAudio")));
					return ci;
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}
		return null;
	}
	
	public List<WordItem> findAll() {
		List<WordItem> channelItems = new ArrayList<WordItem>();
		Cursor cursor = mSQLiteDatabase.query(WordItem.TABLENAME,
				new String[] { "id", "englishName", "phoneticSymbols",
				"chineseSignificance", "exampleEnglish", "exampleChinese",
				"fillProblem", "fillAnswer", "memoryMethodA",
				"memoryMethodB"},null, null, null, null, null);
		try {
			if (cursor.moveToFirst()) {
				do {
					WordItem ci = new WordItem();
					ci.setId(cursor.getString(cursor.getColumnIndex("id")));
					ci.setEnglishName(cursor.getString(cursor.getColumnIndex("englishName")));
					ci.setPhoneticSymbols(cursor.getString(cursor.getColumnIndex("phoneticSymbols")));
					ci.setChineseSignificance(cursor.getString(cursor.getColumnIndex("chineseSignificance")));
					ci.setExampleEnglish(cursor.getString(cursor.getColumnIndex("exampleEnglish")));
					ci.setExampleChinese(cursor.getString(cursor.getColumnIndex("exampleChinese")));
					ci.setFillProblem(cursor.getString(cursor.getColumnIndex("fillProblem")));
					ci.setFillAnswer(cursor.getString(cursor.getColumnIndex("fillAnswer")));
					ci.setMemoryMethodA(cursor.getString(cursor.getColumnIndex("memoryMethodA")));
					ci.setMemoryMethodB(cursor.getString(cursor.getColumnIndex("memoryMethodB")));
//					ci.setExampleImage(cursor.getString(cursor.getColumnIndex("exampleImage")));
//					ci.setMemoryImage(cursor.getString(cursor.getColumnIndex("memoryImage")));
//					ci.setEnglishAudio(cursor.getString(cursor.getColumnIndex("englishAudio")));
					channelItems.add(ci);
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}
		return channelItems;
	}
	
}
