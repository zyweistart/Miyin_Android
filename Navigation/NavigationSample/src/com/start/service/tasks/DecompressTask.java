package com.start.service.tasks;

import java.io.File;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;

import com.start.core.Constant;
import com.start.core.CoreActivity;
import com.start.navigation.R;
import com.start.utils.CommonFn;
import com.start.utils.ZipUtils;

public class DecompressTask extends AsyncTask<Void, Float, Boolean> {

	private String fileno;
	private CoreActivity mContext;
	private ProgressDialog pDialog;
	
	public DecompressTask(CoreActivity context,String fileno){
		this.mContext=context;
		this.fileno=fileno;
	}

	@Override
	protected void onPreExecute() {
		pDialog=CommonFn.progressDialog(mContext, mContext.getString(R.string.msg_decompressing_datafile));
		pDialog.show();
		pDialog.setCancelable(false);
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		String externalStorageDirectory=Environment.getExternalStorageDirectory().getPath();
		File decompressFile=new File(new File(externalStorageDirectory+Constant.TMPDIRFILE),fileno);
		if(decompressFile.exists()){
			try {
				String folderPath=externalStorageDirectory+Constant.DATADIRFILE+fileno+"/";
				ZipUtils.UnZipFolder(decompressFile.getAbsolutePath(), folderPath);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				decompressFile.delete();
			}
		}
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		pDialog.dismiss();
		if(result){
			new ImportDataFileTask(mContext,fileno).execute();
		}
	}
	
}