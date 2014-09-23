package com.start.service.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.start.model.MapData;
import com.start.navigation.R;

public class MapDataAdapter extends BaseAdapter {
	
	private List<MapData> mapDatas=new ArrayList<MapData>();

	private LayoutInflater inflater;

	public MapDataAdapter(LayoutInflater layoutInflater) {
		this.inflater = layoutInflater;
	}
	
	@Override
	public int getCount() {
		return mapDatas.size();
	}

	@Override
	public MapData getItem(int position) {
		return mapDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.lvitem_mapdata_index, parent, false);
		}
		MapData md=mapDatas.get(position);
		((CheckedTextView) convertView).setText(md.getDisplay());
		convertView.setTag(md);
		return convertView;
	}
	
	public void setData(List<MapData> mapDatas){
		if(mapDatas!=null&&!mapDatas.isEmpty()){
			this.mapDatas=mapDatas;
			this.notifyDataSetChanged();
		}
	}
	/**
	 * 根据地图编号获取索引
	 * @param mapId
	 * @return
	 */
	public int getMapDataPositionByMapId(String mapId){
		if(mapDatas!=null){
			for(int i=0;i<mapDatas.size();i++){
				if(mapId.equals(mapDatas.get(i).getId())){
					return i;
				}
			}
			throw new IllegalArgumentException("mapId error ! value = "+mapId);
		}else{
			throw new NullPointerException("mapDatas is null");
		}
		
	}

}