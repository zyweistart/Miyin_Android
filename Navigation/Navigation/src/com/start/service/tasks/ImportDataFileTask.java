package com.start.service.tasks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.start.core.AppConfig;
import com.start.core.Constant;
import com.start.core.CoreActivity;
import com.start.core.CoreService;
import com.start.model.Department;
import com.start.model.DepartmentHasRoom;
import com.start.model.Doctor;
import com.start.model.Edge;
import com.start.model.Introduction;
import com.start.model.MapData;
import com.start.model.Room;
import com.start.model.RoomArea;
import com.start.model.Vertex;
import com.start.navigation.AppContext;
import com.start.navigation.R;
import com.start.utils.CommonFn;
import com.start.utils.Utils;

public class ImportDataFileTask extends AsyncTask<Void, Void, Boolean> {

	private static final String DEBUG_TAG = "ImportDataFileTask";
	
	private String message;
	private ProgressDialog pDialog;
	private  CoreActivity mContext;
	private AppContext mAppContext;
	private CoreService mCoreService;
	private String fileno;
	
	public ImportDataFileTask(CoreActivity context,String fileno) {
		this.mContext=context;
		this.fileno=fileno;
		this.mAppContext = AppContext.getInstance();
		this.mCoreService=new CoreService(mAppContext);
		
	}
	
	@Override
	protected void onPreExecute() {
		pDialog=CommonFn.progressDialog(mContext, mContext.getString(R.string.msg_importing_datafile));
		pDialog.show();
		pDialog.setCancelable(false);
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		String externalStorageDirectory=Environment.getExternalStorageDirectory().getPath();
		String folderPath=externalStorageDirectory+Constant.DATADIRFILE+fileno+"/";
		File dataDir=new File(folderPath+"mapdata/");
		Boolean flag=false;
		if(dataDir.exists()){
			try {
				for (String fileName : dataDir.list()) {
					switch (AppConfig.getFileType(fileName)) {
					case AppConfig.TYPE_DEPARTMENT:
						importDepartment(dataDir,fileno,fileName);
						break;
					case AppConfig.TYPE_DEPARTMENTHASROOM:
						importDepartmentHasRoom(dataDir,fileno,fileName);
						break;
					case AppConfig.TYPE_DOCTOR:
						importDoctor(dataDir,fileno,fileName);
						break;
					case AppConfig.TYPE_EDGE:
						importEdge(dataDir,fileno,fileName);
						break;
					case AppConfig.TYPE_MAPDATA:
						importMapData(dataDir,fileno,fileName);
						break;
					case AppConfig.TYPE_ROOM:
						importRoom(dataDir,fileno,fileName);
						break;
					case AppConfig.TYPE_ROOMAREA:
						importRoomArea(dataDir,fileno,fileName);
						break;
					case AppConfig.TYPE_VERTEX:
						importVertex(dataDir,fileno,fileName);
						break;
					case AppConfig.TYPE_INTRODUCTION:
						importIntroduction(dataDir,fileno,fileName);
						break;
					}
				}
				List<MapData> mds=mAppContext.getMapDataService().findAll(fileno);
				for(MapData md:mds){
					File mapDataFile=new File(dataDir,md.getId()+".map");
					if(mapDataFile.exists()){
						importMap(mapDataFile,fileno+"/mapdata/"+md.getId()+".map");
					}
				}
				flag=true;
			} catch (Exception e) {
				flag=false;
				message = "Failed to import data.";
				Log.e(DEBUG_TAG, e.getMessage());
			}
		}
		File doctorPic=new File(dataDir,"doctor_head");
		if(doctorPic.exists()){
			try {
				for (String fileName : doctorPic.list()) {
					importData(new File(doctorPic,fileName),fileno+"/doctor_head/"+fileName);
				}
				flag=true;
			}catch(Exception e){
				flag=false;
				message = "Failed to import doctor head.";
				Log.e(DEBUG_TAG, e.getMessage());
			}
		}
		File processDir=new File(folderPath+"process/");
		if(processDir.exists()){
			try {
				for (String fileName : processDir.list()) {
					importData(new File(processDir,fileName),fileno+"/process/"+fileName);
				}
				flag=true;
			}catch(Exception e){
				flag=false;
				message = "Failed to import process.";
				Log.e(DEBUG_TAG, e.getMessage());
			}
		}
		return flag;
	}
	
	
	@Override
	protected void onPostExecute(Boolean result) {
		pDialog.dismiss();
		if (result) {
			mContext.handler.sendEmptyMessage(Constant.Handler.HANDLERUPDATEMAINTHREAD);
		}else{
			mAppContext.makeTextShort(message);
		}
	}
	
	private void importDepartment(File dataDir,String fileno,String fileName) throws IOException {
		List<String[]> datas=readFileData(new File(dataDir,fileName));
		if(datas!=null){
			for(String[] data:datas){
				if(data.length==4){
					ContentValues values=new ContentValues();
					values.put(Department.COLUMN_NAME_NO, fileno);
					values.put(Department._ID, data[0]);
					values.put(Department.COLUMN_NAME_NAME, data[1]);
					values.put(Department.COLUMN_NAME_INTRODUCTION, data[2]);
					values.put(Department.COLUMN_NAME_MAJORROOMID, data[3]);
					mCoreService.insert(Department.TABLE_NAME,values);
				}
			}
		}
	}
	
	private void importDepartmentHasRoom(File dataDir,String fileno,String fileName) throws IOException {
		List<String[]> datas=readFileData(new File(dataDir,fileName));
		if(datas!=null){
			for(String[] data:datas){
				if(data.length==2){
					ContentValues values=new ContentValues();
					values.put(DepartmentHasRoom.COLUMN_NAME_NO, fileno);
					values.put(DepartmentHasRoom.COLUMN_NAME_DEPARTMENTID, data[0]);
					values.put(DepartmentHasRoom.COLUMN_NAME_ROOMID, data[1]);
					mCoreService.insert(DepartmentHasRoom.TABLE_NAME,values);
				}
			}
		}
	}
	
	private void importRoom(File dataDir,String fileno,String fileName) throws IOException {
		List<String[]> datas=readFileData(new File(dataDir,fileName));
		if(datas!=null){
			for(String[] data:datas){
				if(data.length==4){
					ContentValues values=new ContentValues();
					values.put(Room.COLUMN_NAME_NO, fileno);
					values.put(Room.COLUMN_NAME_MAPID, data[0]);
					values.put(Room._ID, data[1]);
					values.put(Room.COLUMN_NAME_NAME, data[2]);
					values.put(Room.COLUMN_NAME_VERTEXID, data[3]);
					mCoreService.insert(Room.TABLE_NAME,values);
				}
			}
		}
	}
	
	private void importRoomArea(File dataDir,String fileno,String fileName) throws IOException {
		List<String[]> datas=readFileData(new File(dataDir,fileName));
		if(datas!=null){
			for(String[] data:datas){
				if(data.length==3){
					ContentValues values=new ContentValues();
					values.put(RoomArea.COLUMN_NAME_NO, fileno);
					values.put(RoomArea.COLUMN_NAME_ROOMID, data[0]);
					values.put(RoomArea.COLUMN_NAME_LATITUDE, data[1]);
					values.put(RoomArea.COLUMN_NAME_LONGITUDE, data[2]);
					mCoreService.insert(RoomArea.TABLE_NAME,values);
				}
			}
		}
	}
	
	private void importDoctor(File dataDir,String fileno,String fileName) throws IOException {
		List<String[]> datas=readFileData(new File(dataDir,fileName));
		if(datas!=null){
			for(String[] data:datas){
				if(data.length==7){
					ContentValues values=new ContentValues();
					values.put(Doctor.COLUMN_NAME_NO, fileno);
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
	
	private void importVertex(File dataDir,String fileno,String fileName) throws IOException {
		List<String[]> datas=readFileData(new File(dataDir,fileName));
		if(datas!=null){
			for(String[] data:datas){
				if(data.length==4){
					ContentValues values=new ContentValues();
					values.put(Vertex.COLUMN_NAME_NO, fileno);
					values.put(Vertex.COLUMN_NAME_MAPID, data[0]);
					values.put(Vertex.COLUMN_NAME_ID, data[1]);
					values.put(Vertex.COLUMN_NAME_LATITUDE, data[2]);
					values.put(Vertex.COLUMN_NAME_LONGITUDE, data[3]);
					mCoreService.insert(Vertex.TABLE_NAME,values);
				}
			}
		}
	}
	
	private void importIntroduction(File dataDir,String fileno,String fileName) throws IOException {
		
		InputStream is = null;
		BufferedReader br=null;
		try {
			String line = null;
			is = new FileInputStream(new File(dataDir,fileName));
			br = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb=new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			ContentValues values=new ContentValues();
			values.put(Introduction.COLUMN_NAME_NO, fileno);
			values.put(Introduction.COLUMN_NAME_CONTENT, sb.toString());
			mCoreService.insert(Introduction.TABLE_NAME,values);
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
	
	private void importMapData(File dataDir,String fileno,String fileName) throws IOException {
		List<String[]> datas=readFileData(new File(dataDir,fileName));
		if(datas!=null){
			for(String[] data:datas){
				if(data.length==5){
					ContentValues values=new ContentValues();
					values.put(MapData.COLUMN_NAME_NO, fileno);
					values.put(MapData._ID, data[0]);
					values.put(MapData.COLUMN_NAME_NAME, data[1]);
					values.put(MapData.COLUMN_NAME_DISPLAY, data[2]);
					values.put(MapData.COLUMN_NAME_MAIN, data[3]);
					values.put(MapData.COLUMN_NAME_MAINID, data[4]);
					mCoreService.insert(MapData.TABLE_NAME,values);
				}
			}
		}
	}
	
	private void importEdge(File dataDir,String fileno,String fileName) throws IOException {
		List<String[]> datas=readFileData(new File(dataDir,fileName));
		if(datas!=null){
			for(String[] data:datas){
				if(data.length==3){
					ContentValues values=new ContentValues();
					values.put(Edge.COLUMN_NAME_NO, fileno);
					values.put(Edge._ID, data[0]);
					values.put(Edge.COLUMN_NAME_VERTEXSTARTID, data[1]);
					values.put(Edge.COLUMN_NAME_VERTEXENDID, data[2]);
					mCoreService.insert(Edge.TABLE_NAME,values);
				}
			}
		}
	}
	
	private void importMap(File mapData,String absolutePath) throws IOException {
		InputStream is = null;
		try {
			is = new FileInputStream(mapData);
			Utils.writeStreamToExternalStorage(mAppContext, is, absolutePath);
		} finally {
			Utils.closeInputStream(is);
		}
	}
	
	private void importData(File mapData,String absolutePath) throws IOException {
		InputStream is = null;
		try {
			is = new FileInputStream(mapData);
			Utils.writeStreamToExternalStorage(mAppContext, is, absolutePath);
		} finally {
			Utils.closeInputStream(is);
		}
	}
	
	private List<String[]> readFileData(File file) throws IOException{
		InputStream is = null;
		try {
			String line = null;
			is = new FileInputStream(file);
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