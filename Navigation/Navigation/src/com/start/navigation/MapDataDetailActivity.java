package com.start.navigation;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.start.core.Constant;
import com.start.core.CoreActivity;
import com.start.model.UIRunnable;
import com.start.service.tasks.DownloadTask;
import com.start.service.tasks.ReDownloadTask;
import com.start.utils.NetConnectManager;
import com.start.utils.Utils;

/**
 * 导航
 * @author start
 *
 */
public class MapDataDetailActivity extends CoreActivity implements OnClickListener {

	private String fileno=null;
	private Button btnDownload;
	private Button btnUse;
	private Button btnReDownload;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_data_detail);
		btnDownload=(Button)findViewById(R.id.activity_map_data_download);
		btnReDownload=(Button)findViewById(R.id.activity_map_data_re_download);
		btnUse=(Button)findViewById(R.id.activity_map_data_use);
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			fileno=bundle.getString(MapDataListActivity.RECORDNO);
			Map<String,String> requestParams=new HashMap<String,String>();
			requestParams.put("accessid",Constant.ACCESSID_LOCAL);
			requestParams.put("recordno",fileno);
			Map<String,String> headerParams=new HashMap<String,String>();
			headerParams.put("sign", Constant.ACCESSKEY_LOCAL);
			getHttpService().exeNetRequest(Constant.ServerAPI.hospitalDetail,requestParams,headerParams,new UIRunnable(){
				@Override
				public void run() {
					
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Map<String,String> data=getContent().get("hospitalinfo");
							setCurrentActivityTitle(data.get("name"));
							TextView tv=(TextView)findViewById(R.id.activity_map_data_description_txt);
							tv.setText(data.get("desc"));
							File dataFile=Utils.getFile(MapDataDetailActivity.this, fileno);
							if(dataFile.exists()){
								if(!getAppContext().getCurrentDataNo().equals(fileno)){
									btnDownload.setVisibility(View.GONE);
									btnUse.setVisibility(View.VISIBLE);
								}
								btnReDownload.setVisibility(View.VISIBLE);
							}else{
								btnDownload.setVisibility(View.VISIBLE);
								btnReDownload.setVisibility(View.GONE);
								btnUse.setVisibility(View.GONE);
							}
						}
						
					});
				}
			});
			
		}else{
			finish();
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.activity_map_data_download){
			if(NetConnectManager.isMobilenetwork(MapDataDetailActivity.this)){
				new AlertDialog.Builder(MapDataDetailActivity.this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setMessage(R.string.msg_use_mobile_data)
				.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				}).setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						new DownloadTask(MapDataDetailActivity.this,fileno).execute();
					}
				}).show();
			}else{
				new DownloadTask(MapDataDetailActivity.this,fileno).execute();
			}
		}else if(v.getId()==R.id.activity_map_data_re_download){
			new AlertDialog.Builder(MapDataDetailActivity.this)
			.setIcon(android.R.drawable.ic_dialog_info)
			.setMessage(R.string.msg_use_mobile_data_re_download)
			.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();
				}
			}).setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					if(NetConnectManager.isMobilenetwork(MapDataDetailActivity.this)){
						new AlertDialog.Builder(MapDataDetailActivity.this)
						.setIcon(android.R.drawable.ic_dialog_info)
						.setMessage(R.string.msg_use_mobile_data)
						.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								dialog.dismiss();
							}
						}).setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								new ReDownloadTask(MapDataDetailActivity.this,fileno).execute();
							}
						}).show();
					}else{
						new ReDownloadTask(MapDataDetailActivity.this,fileno).execute();
					}
				}
			}).show();
		}else if(v.getId()==R.id.activity_map_data_use){
			if(!getAppContext().getCurrentDataNo().equals(fileno)){
				new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setMessage(R.string.msg_sure_switch_current_data)
				.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				}).setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						//使用当前数据
						getAppContext().getSharedPreferencesUtils().putString(Constant.SharedPreferences.CURRENTDATAFILENO, fileno);
						makeTextLong(R.string.msg_switching_datafile_success);
						getAppContext().getSharedPreferencesUtils().putBoolean(Constant.SharedPreferences.SWITCHMAPDATAFLAG, true);
						handler.sendEmptyMessage(Constant.Handler.HANDLERUPDATEMAINTHREAD);
					}
				}).show();
			}else{
				makeTextLong(R.string.msg_current_useing_data);
			}
		}
	}
	
	@Override
	protected void onMainUpdate(int what){
		if(what==Constant.Handler.HANDLERUPDATEMAINTHREAD){
			File dataFile=Utils.getFile(MapDataDetailActivity.this, fileno);
			if(dataFile.exists()){
				btnReDownload.setVisibility(View.VISIBLE);
			}else{
				btnReDownload.setVisibility(View.GONE);
			}
			if(!getAppContext().getCurrentDataNo().equals(fileno)){
				btnDownload.setVisibility(View.GONE);
				btnUse.setVisibility(View.VISIBLE);
			}else{
				btnDownload.setVisibility(View.GONE);
				btnUse.setVisibility(View.GONE);
			}
		}
	}

}