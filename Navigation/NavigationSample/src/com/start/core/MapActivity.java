package com.start.core;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.mapsforge.android.maps.Projection;
import org.mapsforge.android.maps.overlay.ArrayItemizedOverlay;
import org.mapsforge.android.maps.overlay.ArrayWayOverlay;
import org.mapsforge.android.maps.overlay.Overlay;
import org.mapsforge.core.GeoPoint;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.start.model.MapData;
import com.start.model.Room;
import com.start.model.Vertex;
import com.start.model.nav.EndPoint;
import com.start.model.nav.IndoorEndPoint;
import com.start.model.nav.MyLocation;
import com.start.model.nav.PathSearchResult;
import com.start.model.overlay.POI;
import com.start.navigation.AppContext;
import com.start.navigation.R;
import com.start.service.MapDataAdapter;
import com.start.service.tasks.PathSearchTask;
import com.start.service.tasks.PathSearchTask.PathSearchListener;
import com.start.utils.CommonFn;
import com.start.utils.Utils;

public abstract class MapActivity extends MapManager implements OnClickListener,PathSearchListener {

	protected static final String BUNDLEDATA_DATA = "data";
	protected AppContext appContext;
	protected long lastPressTime;
	protected ListView mMapIndexListView;//地图索引视图列表
	protected MapDataAdapter mMapDataAdapter;//地图索引适配器
	protected Map<String, List<Room>> mRooms;//地图上房间的集合
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		appContext = AppContext.getInstance();
		
		// 地图数据加载完毕后才把地图视图显示到页面上
		((ViewGroup) findViewById(R.id.module_main_frame_map_contentll)).addView(getMapView(), 0);

		mMapDataAdapter = new MapDataAdapter(getLayoutInflater());

		mMapIndexListView = (ListView) findViewById(R.id.module_main_frame_map_content_mapdataindexlist);
		mMapIndexListView.setAdapter(mMapDataAdapter);
		mMapIndexListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MapData md = (MapData) view.getTag();
				// 只有当点击的索引不同时才会进行切换
				if (!md.getId().equals(getCurrentMapData().getId())) {
					setCurrentMapData(md);
					setMapFile();
				}
			}

		});

		mRooms=appContext.getRoomService().findAllPullMap();
		this.loadMainMapData();
		
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
		if (id == Utils.DLG_POI) {
			POI poi = (POI) args.getSerializable(BUNDLEDATA_DATA);
			if(getCurrentMapData().isMain()){
				((TextView) dialog.findViewById(R.id.poiName)).setText("进入 "+poi
						.getName());
			}else{
				((TextView) dialog.findViewById(R.id.poiName)).setText(poi
						.getName());
			}
			dialog.findViewById(R.id.direction).setTag(poi);
			dialog.findViewById(R.id.poiName).setTag(poi);
//			if (mPOIMarker != null) {
//				dialog.getWindow().getAttributes().y = -50;
//			} else {
				dialog.getWindow().getAttributes().y = 0;
//			}
			return;
		} else {
			super.onPrepareDialog(id, dialog, args);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case Utils.DLG_POI:
			dialog = CommonFn.createPOIDialog(this);
			break;
		case Utils.DLG_EXIT_NAVIGATION:
			dialog = CommonFn.alertDialog(this, "退出导航",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							appContext.setPathSearchResult(null);
							updateOverlay();
						}
					});
			break;
		default:
			dialog = super.onCreateDialog(id);
		}
		return dialog;
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.direction) {
			POI r = (POI) v.getTag();
			Vertex vertex=AppContext.getInstance().getVertexService().findById(r.getVertexId());
			if(vertex!=null){
				//定位导航
				MyLocation myLocation = appContext.getMyLocation();

				if (myLocation != null) {
					PathSearchTask search = new PathSearchTask(this);
					EndPoint sp = new IndoorEndPoint(myLocation.getMapId(),
							myLocation.getGeoPoint());
					EndPoint ep = new IndoorEndPoint(getCurrentMapData().getId(),
							new GeoPoint(Double.parseDouble(vertex.getLatitude()), 
									Double.parseDouble(vertex.getLongitude())));
					search.execute(sp, ep);
				} else {
					Toast.makeText(this, R.string.msg_location_unavailable, Toast.LENGTH_SHORT).show();
				}
				
			}
		} else if (v.getId() == R.id.poiName) {
			onClickRoomName(v);
		} else if (v.getId() == R.id.module_main_header_content_park) {
			//切换至园区平面图
			this.loadMainMapData();
		} else if (v.getId() == R.id.module_main_header_content_location) {
			//定位到当前用户的位置
//			appContext.makeTextLong(R.string.msg_locationing);
			final MyLocation myLocation = appContext.locate();
			
			setCurrentMapData(appContext.getMapDataService().findById(myLocation.getMapId()));
			
			setMapFile();
			
		}
	}
	
	/**
	 *点击房间的名称时处理 R.id.poiName
	 */
	public abstract void onClickRoomName(View v);

	@Override
	public void onClickAt(float xPixel, float yPixel) {
		//点击地图上的点判断是否在房间的区域内
		Projection projection = getMapView().getProjection();
		if (projection == null) {
			return;
		}

		GeoPoint g = projection.fromPixels((int) xPixel, (int) yPixel);

		if (mRooms.isEmpty()) {
			return;
		}

		List<Room> rooms = mRooms.get(getCurrentMapData().getId());
		for (Room r : rooms) {
			if (r.inside(g)) {
				tapPOI(r);
				return;
			}
		}
	}

	@Override
	public void onGetResult(PathSearchResult result) {
		//路线搜索回调类
		if (result.getType() == PathSearchResult.Type.IN_BUILDING) {
			IndoorEndPoint sp=(IndoorEndPoint)result.getStartPoint();
			
			setCurrentMapData(appContext.getMapDataService().findById(sp.getMapId()));
			
			setMapFile();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onBackPressed() {
		if (appContext.getPathSearchResult() != null) {
			showDialog(Utils.DLG_EXIT_NAVIGATION);
		} else {
			if ((System.currentTimeMillis() - lastPressTime) > 2000) {
				appContext.makeTextShort("再按一次退出应用");
				lastPressTime = System.currentTimeMillis();
			} else {
				super.onBackPressed();
			}
		}
	}
	
	/**
	 * 加载园区地图平面图
	 */
	public void loadMainMapData(){
		if(getCurrentMapData()==null||!getCurrentMapData().isMain()){
			List<MapData> mds=appContext.getMapDataService().findMainData();
			mMapDataAdapter.setData(mds);
			if(mds.size()>0){
				setCurrentMapData(mds.get(0));
				setMapFile();
			}
		}
	}
	
	/**
	 * 设置视图的地图数据文件
	 */
	public void setMapFile() {
		
		if(getCurrentMapData().isMain()){
			mMapIndexListView.setVisibility(View.GONE);
		}else{
			List<MapData> mapDatas=appContext.getMapDataService().findByMainId(getCurrentMapData().getMainid());
			mMapDataAdapter.setData(mapDatas);
			// 设置当前地图索引选重状态
			mMapIndexListView.setItemChecked(mMapDataAdapter
					.getMapDataPositionByMapId(getCurrentMapData().getId()), true);
			mMapIndexListView.setVisibility(View.VISIBLE);
		}
		
		String path = String.format("mapdata/%1$s.map",getCurrentMapData().getId());
		File dataFile=new File(Utils.getFile(MapActivity.this,appContext.getCurrentDataNo()),path);
		if (setMapFile(dataFile)) {
			updateOverlay();
		}else{
			return;
		}
	}

	/**
	 * 更新地图上的覆盖图、路线、位置等信息
	 */
	public void updateOverlay() {

		List<Overlay> itemList = getMapView().getOverlays();
		itemList.clear();
		
		//添加我的位置覆盖点
		MyLocation myLocation = appContext.getMyLocation();
		
		if (getCurrentMapData().getId().equals(myLocation.getMapId())) {
			addMyLocMarker(myLocation,itemList);
		}
		
		PathSearchResult res = appContext.getPathSearchResult();
		
		//覆盖点
		ArrayItemizedOverlay overlay=getArrayItemizedOverlay(res);
		if (overlay != null) {
			itemList.add(overlay);
		}
		
		//路线
		ArrayWayOverlay ways=getWayOverlay(res);
		if (ways != null) {
			itemList.add(ways);
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public void tapPOI(POI poi) {
		Bundle data = new Bundle();
		data.putSerializable(BUNDLEDATA_DATA, poi);
		showDialog(Utils.DLG_POI, data);

		getMapView().setCenter(poi.getGeoPoint());
	}

}
