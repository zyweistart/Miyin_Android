package com.start.navigation;

import java.util.List;

import org.mapsforge.android.maps.Projection;
import org.mapsforge.core.GeoPoint;

import android.os.Bundle;
import android.view.View;

import com.start.core.Constant;
import com.start.core.MapActivity;
import com.start.model.MapData;
import com.start.model.Room;
import com.start.model.nav.MyLocation;
import com.start.model.overlay.POI;

/**
 * 主界面
 * 
 */
public class MainActivity extends MapActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//此处不能调用setContentView(int)函数设置视图界面
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Bundle extras=getIntent().getExtras();
		if(extras!=null){
			String roomid=extras.getString("ROOMID");
			if(roomid!=null){
				Room room=appContext.getRoomService().findById(roomid);
				if(room!=null){
					setCurrentMapData(appContext.getMapDataService().findById(room.getMapId()));
					setMapFile();
					tapPOI(room);
				}
			}
		}
	}

	@Override
	public void onClickRoomName(View v) {
		POI r = (POI) v.getTag();
		if(r!=null){
			MapData mainMapData=appContext.getMapDataService().findById(r.getMapId());
			//如果点击的名称为园区平面图上的名称则进入该楼房中
			if(mainMapData.isMain()){
				List<MapData> mapDatas=appContext.getMapDataService().findByMainId(mainMapData.getId());
				if(!mapDatas.isEmpty()){
					mMapDataAdapter.setData(mapDatas);
					MyLocation myLocation=appContext.getMyLocation();
					for(int i=0;i<mapDatas.size();i++){
						setCurrentMapData(mapDatas.get(i));
						if(myLocation.getMapId().equals(mapDatas.get(i).getId())){
							setCurrentMapData(mMapDataAdapter.getItem(mMapDataAdapter.getMapDataPositionByMapId(myLocation.getMapId())));
							break;
						}
					}
					setMapFile();
				}
			}else{
				//进入房间的详细介绍
				appContext.makeTextLong("进入详情，房间ID:"+r.getId());
			}
		}
	}
	
	@Override
	public void onLongClickAt(float xPixel, float yPixel) {
		//点击地图上的点判断是否在房间的区域内
		Projection projection = getMapView().getProjection();
		if (projection == null) {
			return;
		}
		GeoPoint g = projection.fromPixels((int) xPixel, (int) yPixel);
		appContext.getSharedPreferencesUtils().putString(Constant.SharedPreferences.USERLOCATION, getCurrentMapData().getId()+";"+g.getLatitude()+";"+g.getLongitude());
		updateOverlay();
	}
	
}