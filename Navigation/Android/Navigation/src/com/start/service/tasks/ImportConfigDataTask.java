package com.start.service.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.start.core.AppConfig;
import com.start.core.CoreService;
import com.start.model.Department;
import com.start.model.DepartmentHasRoom;
import com.start.model.Doctor;
import com.start.model.Edge;
import com.start.model.MapData;
import com.start.model.Room;
import com.start.model.RoomArea;
import com.start.model.Vertex;
import com.start.utils.Utils;

public class ImportConfigDataTask extends AsyncTask<Void, Void, Boolean> {

	private static final String DEBUG_TAG = "ImportConfigDataTask";
	
	private String message;
	
	private Context mContext;
	private AssetManager mAssetManager;
	private CoreService mCoreService;
	
	public ImportConfigDataTask(Context context) {
		this.mContext = context;
		this.mAssetManager = context.getAssets();
		this.mCoreService=new CoreService(mContext);
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			for (String fileName : mAssetManager.list(AppConfig.CONFIG_DATA_PATH_MEDMAP)) {
				switch (AppConfig.getFileType(fileName)) {
				case AppConfig.TYPE_DEPARTMENT:
					importDepartment(fileName);
					break;
				case AppConfig.TYPE_DEPARTMENTHASROOM:
					importDepartmentHasRoom(fileName);
					break;
				case AppConfig.TYPE_DOCTOR:
					importDoctor(fileName);
					break;
				case AppConfig.TYPE_EDGE:
					importEdge(fileName);
					break;
				case AppConfig.TYPE_MAPDATA:
					importMapData(fileName);
					break;
				case AppConfig.TYPE_ROOM:
					importRoom(fileName);
					break;
				case AppConfig.TYPE_ROOMAREA:
					importRoomArea(fileName);
					break;
				case AppConfig.TYPE_VERTEX:
					importVertex(fileName);
					break;
				case AppConfig.TYPE_MAP:
					importMap(fileName);
				}
			}
			return true;
		} catch (IOException e) {
			message = "Failed to import config data.";
			Log.e(DEBUG_TAG, e.getMessage());
			return false;
		}
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (result) {
			Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
			editor.putBoolean("init", true);
			editor.commit();
		} else {
			Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
		}
	}
	
	private void importDepartment(String fileName) throws IOException {
		String filePath = String.format("%1$s/%2$s", AppConfig.CONFIG_DATA_PATH_MEDMAP, fileName);
		List<String[]> datas=readFileData(filePath);
		if(datas!=null){
			for(String[] data:datas){
				if(data.length==4){
					ContentValues values=new ContentValues();
					values.put(Department._ID, data[0]);
					values.put(Department.COLUMN_NAME_NAME, data[1]);
					values.put(Department.COLUMN_NAME_INTRODUCTION, data[2]);
					values.put(Department.COLUMN_NAME_MAJORROOMID, data[3]);
					mCoreService.insert(Department.TABLE_NAME,values);
				}
			}
		}
	}
	
	private void importDepartmentHasRoom(String fileName) throws IOException {
		String filePath = String.format("%1$s/%2$s", AppConfig.CONFIG_DATA_PATH_MEDMAP, fileName);
		List<String[]> datas=readFileData(filePath);
		if(datas!=null){
			for(String[] data:datas){
				if(data.length==2){
					ContentValues values=new ContentValues();
					values.put(DepartmentHasRoom.COLUMN_NAME_DEPARTMENTID, data[0]);
					values.put(DepartmentHasRoom.COLUMN_NAME_ROOMID, data[1]);
					mCoreService.insert(DepartmentHasRoom.TABLE_NAME,values);
				}
			}
		}
	}
	
	private void importRoom(String fileName) throws IOException {
		String filePath = String.format("%1$s/%2$s", AppConfig.CONFIG_DATA_PATH_MEDMAP, fileName);
		List<String[]> datas=readFileData(filePath);
		if(datas!=null){
			for(String[] data:datas){
				if(data.length==4){
					ContentValues values=new ContentValues();
					values.put(Room.COLUMN_NAME_MAPID, data[0]);
					values.put(Room._ID, data[1]);
					values.put(Room.COLUMN_NAME_NAME, data[2]);
					values.put(Room.COLUMN_NAME_VERTEXID, data[3]);
					mCoreService.insert(Room.TABLE_NAME,values);
				}
			}
		}
	}
	
	private void importRoomArea(String fileName) throws IOException{
		String filePath = String.format("%1$s/%2$s", AppConfig.CONFIG_DATA_PATH_MEDMAP, fileName);
		List<String[]> datas=readFileData(filePath);
		if(datas!=null){
			for(String[] data:datas){
				if(data.length==3){
					ContentValues values=new ContentValues();
					values.put(RoomArea.COLUMN_NAME_ROOMID, data[0]);
					values.put(RoomArea.COLUMN_NAME_LATITUDE, data[1]);
					values.put(RoomArea.COLUMN_NAME_LONGITUDE, data[2]);
					mCoreService.insert(RoomArea.TABLE_NAME,values);
				}
			}
		}
	}
	
	private void importDoctor(String fileName) throws IOException {
		String filePath = String.format("%1$s/%2$s", AppConfig.CONFIG_DATA_PATH_MEDMAP, fileName);
		List<String[]> datas=readFileData(filePath);
		if(datas!=null){
			for(String[] data:datas){
				if(data.length==7){
					ContentValues values=new ContentValues();
					values.put(Doctor._ID, data[0]);
					values.put(Doctor.COLUMN_NAME_NAME, data[1]);
					values.put(Doctor.COLUMN_NAME_SEX, data[2]);
					values.put(Doctor.COLUMN_NAME_TITLE, data[3]);
					values.put(Doctor.COLUMN_NAME_SPECIALTY, data[4]);
					values.put(Doctor.COLUMN_NAME_INTRODUCTION, data[5]);
					values.put(Doctor.COLUMN_NAME_DEPARTMENTID, data[6]);
					mCoreService.insert(Doctor.TABLE_NAME,values);
				}
			}
		}
	}
	
	private void importVertex(String fileName) throws IOException {
		String filePath = String.format("%1$s/%2$s", AppConfig.CONFIG_DATA_PATH_MEDMAP, fileName);
		List<String[]> datas=readFileData(filePath);
		if(datas!=null){
			for(String[] data:datas){
				if(data.length==4){
					ContentValues values=new ContentValues();
					values.put(Vertex.COLUMN_NAME_MAPID, data[0]);
					values.put(Vertex.COLUMN_NAME_ID, data[1]);
					values.put(Vertex.COLUMN_NAME_LATITUDE, data[2]);
					values.put(Vertex.COLUMN_NAME_LONGITUDE, data[3]);
					mCoreService.insert(Vertex.TABLE_NAME,values);
				}
			}
		}
	}
	
	private void importMapData(String fileName) throws IOException {
		String filePath = String.format("%1$s/%2$s", AppConfig.CONFIG_DATA_PATH_MEDMAP, fileName);
		List<String[]> datas=readFileData(filePath);
		if(datas!=null){
			for(String[] data:datas){
				if(data.length==2){
					ContentValues values=new ContentValues();
					values.put(MapData._ID, data[0]);
					values.put(MapData.COLUMN_NAME_NAME, data[1]);
					mCoreService.insert(MapData.TABLE_NAME,values);
				}
			}
		}
	}
	
	private void importEdge(String fileName) throws IOException {
		String filePath = String.format("%1$s/%2$s", AppConfig.CONFIG_DATA_PATH_MEDMAP, fileName);
		List<String[]> datas=readFileData(filePath);
		if(datas!=null){
			for(String[] data:datas){
				if(data.length==3){
					ContentValues values=new ContentValues();
					values.put(Edge._ID, data[0]);
					values.put(Edge.COLUMN_NAME_VERTEXSTARTID, data[1]);
					values.put(Edge.COLUMN_NAME_VERTEXENDID, data[2]);
					mCoreService.insert(Edge.TABLE_NAME,values);
				}
			}
		}
	}
	
	private void importMap(String mapFileName) throws IOException {
		String filePath = String.format("%1$s/%2$s", AppConfig.CONFIG_DATA_PATH_MEDMAP, mapFileName);
		InputStream is = null;
		try {
			is = mAssetManager.open(filePath);
			Utils.writeStreamToExternalStorage(mContext, is, filePath);
		} finally {
			Utils.closeInputStream(is);
		}
	}
	
	private List<String[]> readFileData(String fullFilePath) throws IOException{
		InputStream is = null;
		try {
			String line = null;
			is = mAssetManager.open(fullFilePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			List<String[]> datas=new ArrayList<String[]>();
			while ((line = br.readLine()) != null) {
				String[] tokens=line.split(";");
				if(tokens==null){
					continue;
				}
				datas.add(tokens);
			}
			return datas;
		} finally {
			if(is!=null){
				try {
					is.close();
				} finally {
					is=null;
				}
			}
		}
	}
	
}
