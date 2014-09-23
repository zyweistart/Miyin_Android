package com.start.navigation;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.start.core.Constant;
import com.start.core.CoreActivity;
import com.start.model.UIRunnable;
import com.start.navigation.MapDataListActivity.MapDataPullListAdapter.MapDataViewHolder;
import com.start.service.HttpService.LoadMode;
import com.start.service.PullListViewData;
import com.start.service.PullListViewData.OnLoadDataListener;
import com.start.service.tasks.DownloadTask;
import com.start.utils.NetConnectManager;
import com.start.utils.Utils;

/**
 * 地图数据列表
 * @author start
 *
 */
public class MapDataListActivity extends CoreActivity implements OnClickListener{

	private PullListViewData mapDataPullListData;
	
	public static final String RECORDNO="recordno";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_data_list);
		setCurrentActivityTitle(R.string.activity_title_map_data_list);
		
		mapDataPullListData=new PullListViewData(this);
		mapDataPullListData.setOnLoadDataListener(
				new OnLoadDataListener(){

					@Override
					public void LoadData(LoadMode loadMode) {
						
						Map<String,String> requestParams=new HashMap<String,String>();
						requestParams.put("accessid",Constant.ACCESSID_LOCAL);
						requestParams.put("name","");
						Map<String,String> headerParams=new HashMap<String,String>();
						headerParams.put("sign", Constant.ACCESSKEY_LOCAL);
						mapDataPullListData.sendPullToRefreshListViewNetRequest(loadMode,Constant.ServerAPI.hospitalQuery,requestParams,headerParams,new UIRunnable(){
							@Override
							public void run() {
								mapDataPullListData.getAdapter().notifyDataSetChanged();
							} 
						},"hospitallist","hospitallist"+TAG);
						
					}
					
				});
		mapDataPullListData.start(R.id.activity_map_data_pulllistview, 
				new MapDataPullListAdapter(mapDataPullListData));
		
	}

	public class MapDataPullListAdapter extends PullListViewData.DataAdapter{
		
		public MapDataPullListAdapter(PullListViewData pullListViewData) {
			pullListViewData.super();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MapDataViewHolder holder;
			if (convertView != null && convertView.getId() == R.id.lvitem_mapdata_content) {
				holder = (MapDataViewHolder) convertView.getTag();
			}else{
				convertView = getLayoutInflater().inflate(R.layout.lvitem_mapdata, null);
				holder = new MapDataViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.lvitem_mapdata_name);
				holder.description = (TextView) convertView.findViewById(R.id.lvitem_mapdata_description);
				holder.download = (ImageButton) convertView.findViewById(R.id.lvitem_mapdata_btn_download);
				holder.use = (ImageButton) convertView.findViewById(R.id.lvitem_mapdata_btn_use);
				holder.detail = (ImageButton) convertView.findViewById(R.id.lvitem_mapdata_btn_detail);
				convertView.setTag(holder);
			}
			Map<String,String> data=mapDataPullListData.getDataItemList().get(position);
			holder.fileno=data.get(RECORDNO);
			holder.name.setText(data.get("name"));
			String filesize=data.get("filesize");
			DecimalFormat df=new DecimalFormat("#.##");
			Float bquota=Float.parseFloat(filesize);
			Float bq=(float)(bquota/1024/1024);
			holder.description.setText(String.format(getString(R.string.msg_current_data_file_size), df.format(bq)));
			File dataFile=Utils.getFile(MapDataListActivity.this, holder.fileno);
			if(dataFile.exists()){
				holder.download.setVisibility(View.GONE);
				if(getAppContext().getCurrentDataNo().equals(holder.fileno)){
					holder.use.setVisibility(View.GONE);
				}else{
					holder.use.setVisibility(View.VISIBLE);
				}
			}else{
				holder.download.setVisibility(View.VISIBLE);
				holder.use.setVisibility(View.GONE);
			}
			holder.download.setTag(holder);
			holder.use.setTag(holder);
			holder.detail.setTag(holder);
			return convertView;
		}
		
		public class MapDataViewHolder {
			String fileno;
			TextView name;
			TextView description;
			ImageButton download;
			ImageButton use;
			ImageButton detail;
		}
		
	}

	@Override
	public void onClick(View v) {
		final MapDataViewHolder vh=(MapDataViewHolder)v.getTag();
		if(vh==null)return;
		if(v.getId()==R.id.lvitem_mapdata_btn_download){
			if(NetConnectManager.isMobilenetwork(MapDataListActivity.this)){
				new AlertDialog.Builder(MapDataListActivity.this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setMessage(R.string.msg_use_mobile_data)
				.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				}).setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						new DownloadTask(MapDataListActivity.this,vh.fileno).execute();
					}
				}).show();
			}else{
				new DownloadTask(MapDataListActivity.this,vh.fileno).execute();
			}
		}else if(v.getId()==R.id.lvitem_mapdata_btn_use){
			if(!getAppContext().getCurrentDataNo().equals(vh.fileno)){
				new AlertDialog.Builder(MapDataListActivity.this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setMessage(R.string.msg_sure_switch_current_data)
				.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				}).setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						//使用当前数据
						getAppContext().getSharedPreferencesUtils().putString(Constant.SharedPreferences.CURRENTDATAFILENO, vh.fileno);
						makeTextLong(R.string.msg_switching_datafile_success);
						getAppContext().getSharedPreferencesUtils().putBoolean(Constant.SharedPreferences.SWITCHMAPDATAFLAG, true);
						handler.sendEmptyMessage(Constant.Handler.HANDLERUPDATEMAINTHREAD);
					}
				}).show();
			}else{
				makeTextLong(R.string.msg_current_useing_data);
			}
		}else if(v.getId()==R.id.lvitem_mapdata_btn_detail){
			Bundle bundle=new Bundle();
			bundle.putString(RECORDNO, vh.fileno);
			Intent intent=new Intent(this,MapDataDetailActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}
	
	@Override
	protected void onMainUpdate(int what){
		if(what==Constant.Handler.HANDLERUPDATEMAINTHREAD){
			mapDataPullListData.getAdapter().notifyDataSetChanged();
		}
	}
	
}