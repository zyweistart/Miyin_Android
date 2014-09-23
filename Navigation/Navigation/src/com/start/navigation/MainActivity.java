package com.start.navigation;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mapsforge.android.maps.Projection;
import org.mapsforge.android.maps.overlay.ArrayItemizedOverlay;
import org.mapsforge.android.maps.overlay.ArrayWayOverlay;
import org.mapsforge.android.maps.overlay.Overlay;
import org.mapsforge.core.GeoPoint;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.start.core.Constant;
import com.start.core.MapManager;
import com.start.model.Department;
import com.start.model.DepartmentHasRoom;
import com.start.model.Doctor;
import com.start.model.MapData;
import com.start.model.Room;
import com.start.model.UIRunnable;
import com.start.model.Vertex;
import com.start.model.nav.EndPoint;
import com.start.model.nav.IndoorEndPoint;
import com.start.model.nav.MyLocation;
import com.start.model.nav.PathSearchResult;
import com.start.model.overlay.POI;
import com.start.model.overlay.POIMarker;
import com.start.model.process.Junction;
import com.start.model.process.Junction.NodeType;
import com.start.model.process.ProcessService;
import com.start.model.process.ProcessService.ProcessListener;
import com.start.service.HttpService;
import com.start.service.HttpService.LoadMode;
import com.start.service.PullListViewData;
import com.start.service.PullListViewData.OnLoadDataListener;
import com.start.service.UserLocationReport;
import com.start.service.adapter.FriendLocationAdapter;
import com.start.service.adapter.MapDataAdapter;
import com.start.service.tasks.PathSearchTask;
import com.start.service.tasks.PathSearchTask.PathSearchListener;
import com.start.utils.CommonFn;
import com.start.utils.Utils;
import com.umeng.analytics.MobclickAgent;

/**
 * 主界面
 * 
 * @author start
 * 
 */
public class MainActivity extends MapManager implements 
		OnClickListener,OnEditorActionListener, OnFocusChangeListener, PathSearchListener,ProcessListener,OnItemClickListener {

	private static final String TAG="HospitalMainActivity";
	private static final String BUNDLEDATA_DATA = "data";
	public static final int REQUEST_CODE_REFRESH_FRIEND_LOCATION=111;
	
	public static boolean ShowMainData=false;
	public static String currentLocationDepartmentId;

	private AppContext appContext;
	
	private long lastPressTime;
	private int mCurSel;
	private int mFrameViewCount;
	private RadioButton rboIntroduction;
	private RadioButton rboMap;
	private RadioButton rboProcess;
	private RadioButton rboFriend;
	private ImageView imMore;
	private RadioButton[] mButtons;
	private LinearLayout mMainContentLayout;
	private View mModuleMainFrameMapContent;
	private View mModuleMainFrameIntroductionContent;
	private View mModuleMainFrameProcessContent;
	private View mModuleMainFrameFriendContent;
	
	private TextView mModuleMainHeaderContentTitle;
	private Button mModuleMainHeaderContentPark;
	private Button mModuleMainHeaderContentLocation;
	
	private List<POIMarker> mPoiMarkers;//当前选重的房间标记集合
	private ListView mMapIndexListView;//地图索引视图列表
	private MapDataAdapter mMapDataAdapter;//地图索引适配器
	private Map<String, List<Room>> mRooms;//地图上房间的集合
	private EditText mapQuery;//搜索框
	private Button mapButtonCancel;//搜索取消按钮
	private LinearLayout mapLLQueryContentContainer;
	private Boolean isTabDepartment=false;
	private ArrayAdapter<Doctor> doctorArrayAdapter;
	private ArrayAdapter<Department> departmentArrayAdapter;
	private ListView mModuleMainFrameMapQueryList;
	private TextView mModuleMainFrameMapQueryContentTabDepartment;
	private TextView mModuleMainFrameMapQueryContentTabDoctor;
	
	private ProcessService process;
	private Button mModuleMainFrameProcessNext;
	private ImageView mModuleMainFrameProcessImage;
	
	private PullListViewData friendLocationPullListData;
	
	private UpdateLocation mUpdateLocation;
	private UserLocationReport mUserLocation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		appContext = AppContext.getInstance();
		
		this.initHeaderView();
		this.initFooterView();
		this.initMainFrameView();

		if(!appContext.getSharedPreferencesUtils().getBoolean(Constant.SharedPreferences.SWITCHMAPDATAFLAG, false)){
			this.loadData();
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(!Constant.ISTEST){
			MobclickAgent.onResume(this);
		}
		//搜索框重置
		mapButtonCancel.setVisibility(View.GONE);
		mapLLQueryContentContainer.setVisibility(View.GONE);
		mapQuery.clearFocus();
		
		//是否重新加载切换数据包后的数据
		if(appContext.getSharedPreferencesUtils().getBoolean(Constant.SharedPreferences.SWITCHMAPDATAFLAG, false)){
			//消除导航数据
			appContext.setPathSearchResult(null);
			this.loadData();
			updateOverlay();
			appContext.getSharedPreferencesUtils().putBoolean(Constant.SharedPreferences.SWITCHMAPDATAFLAG, false);
		}
		
		//显示园区地图
		if(ShowMainData){
			//切换至平面图视图
			if(mCurSel!=1){
				setCurPoint(1);
			}
			this.loadMainMapData();
			ShowMainData=false;
		}
		
		if(currentLocationDepartmentId!=null){
			if(mCurSel!=1){
				setCurPoint(1);
			}
			mPoiMarkers=new ArrayList<POIMarker>();
			Department department=appContext.getDepartmentService().findById(currentLocationDepartmentId);
			List<DepartmentHasRoom> departmentHasRooms=appContext.getDepartmentHasRoomService().findByDepartmentId(currentLocationDepartmentId);
			for(DepartmentHasRoom dhr:departmentHasRooms){
				Room room=appContext.getRoomService().findById(dhr.getRoomId());
				if(room!=null){
					mPoiMarkers.add(new POIMarker(room, getResources().getDrawable(R.drawable.icon_node)));
					if(department!=null){
						if(department.getMajorRoomId().equals(room.getId())){
							
							setCurrentMapData(appContext.getMapDataService().findById(room.getMapId()));
							
							setMapFile();
							tapPOI(room);
							break;
						}
					}
				}
			}
			currentLocationDepartmentId=null;
		}
		
		if (mCurSel == 0 && !rboIntroduction.isChecked()) {
			rboIntroduction.setChecked(true);
			rboMap.setChecked(false);
			rboProcess.setChecked(false);
			rboFriend.setChecked(false);
			mCurSel = 0;
			setCurPoint(-1);
		}
		
		mUpdateLocation=new UpdateLocation();
		mUpdateLocation.start();
		
		mUserLocation=new UserLocationReport(this);
		mUserLocation.start();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(!Constant.ISTEST){
			MobclickAgent.onPause(this);
		}
		
		mUpdateLocation.setFlag(false);
		mUserLocation.setFlag(false);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==REQUEST_CODE_REFRESH_FRIEND_LOCATION){
			if(mCurSel==3){
				if(appContext.isLogin()){
					friendLocationPullListData.getPulllistview().clickRefresh();
				}
			}
		}
		if(resultCode==Constant.ActivityResultCode.EXITAPP){
			finish();
		}
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
		case Utils.DLG_SEARCH_OPTION:
			// dialog = createSearchOptionDialog();
			break;
		case Utils.DLG_POI:
			dialog = CommonFn.createPOIDialog(this);
			break;
		case Utils.DLG_EXIT_NAVIGATION:
			dialog = CommonFn.alertDialog(this, R.string.msg_exit_navigation,
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
		if (v.getId() == R.id.module_main_frame_introduction_btn_hospital) {
			// 医院介绍
			Intent intent = new Intent(this, HospitalDetailActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.module_main_frame_introduction_btn_department) {
			// 部门介绍
			Intent intent = new Intent(this, DepartmentListActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.module_main_frame_introduction_btn_doctor) {
			// 医生介绍
			Intent intent = new Intent(this, DoctorListActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.module_main_frame_process_next) {
			mModuleMainFrameProcessNext.setText(R.string.frame_process_next_step);
			if(process.isProcessEnd()){
				process.init();
				mModuleMainFrameProcessImage.setImageBitmap(CommonFn.convertToBitmap(process.getProcessImageFile()));
			}else{
				process.execute();
			}
		} else if (v.getId() == R.id.direction) {
			POI r = (POI) v.getTag();
			Vertex vertex=AppContext.getInstance().getVertexService().findById(r.getVertexId());
			if(vertex!=null){
				location(getCurrentMapData().getId(),vertex.getLatitude(),vertex.getLongitude());
			}
		} else if (v.getId() == R.id.poiName) {
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
					DepartmentHasRoom departmentHasRoom=appContext.getDepartmentHasRoomService().findByRoomId(r.getId());
					if(departmentHasRoom!=null){
						Bundle bundle=new Bundle();
						bundle.putString(Department.COLUMN_NAME_ID, departmentHasRoom.getDepartmentId());
						Intent intent=new Intent(this,DepartmentDetailActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				}
			}
		} else if (v.getId() == R.id.module_main_header_content_park) {
			//切换至园区平面图
			this.loadMainMapData();
		} else if (v.getId() == R.id.module_main_header_content_location) {
//			appContext.makeTextLong(R.string.msg_locationing);
			final MyLocation myLocation = appContext.locate();
			
			setCurrentMapData(appContext.getMapDataService().findById(myLocation.getMapId()));
			
			setMapFile();
			
		} else if (v.getId() == R.id.module_main_frame_map_query_content_tab_department) {
			((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mapQuery.getWindowToken(), 0);
			if(isTabDepartment){
				isTabDepartment=false;
				if(departmentArrayAdapter == null){
					List<Department> departments=appContext.getDepartmentService().findAll();
					departmentArrayAdapter = new ArrayAdapter<Department>(this, R.layout.lvitem_department, R.id.lvitem_department_name, departments);
				}
				v.setBackgroundResource(R.drawable.tabs_bar_left_on);
				mModuleMainFrameMapQueryContentTabDoctor.setBackgroundResource(R.drawable.tabs_bar_right_off);
				mModuleMainFrameMapQueryList.setAdapter(departmentArrayAdapter);
				departmentArrayAdapter.getFilter().filter(mapQuery.getText());
			}
		} else if (v.getId() == R.id.module_main_frame_map_query_content_tab_doctor) {
			((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mapQuery.getWindowToken(), 0);
			if(!isTabDepartment){
				isTabDepartment=true;
				if(doctorArrayAdapter == null){
					List<Doctor> doctors=appContext.getDoctorService().findAll();
					doctorArrayAdapter = new ArrayAdapter<Doctor>(this, R.layout.lvitem_doctor, R.id.lvitem_doctor_name, doctors);
				}
				v.setBackgroundResource(R.drawable.tabs_bar_right_on);
				mModuleMainFrameMapQueryContentTabDepartment.setBackgroundResource(R.drawable.tabs_bar_left_off);
				mModuleMainFrameMapQueryList.setAdapter(doctorArrayAdapter);
				doctorArrayAdapter.getFilter().filter(mapQuery.getText());
			}
		} else if (v.getId() == R.id.module_main_frame_map_query_content_tab_restroom) {
			appContext.makeTextLong(R.string.msg_tab_restroom);
		} else if (v.getId() == R.id.module_main_frame_map_query_content_tab_drinking_water) {
			appContext.makeTextLong(R.string.msg_tab_drinking_water);
		}
	}

	@Override
	public void onClickAt(float xPixel, float yPixel) {

		Projection projection = getMapView().getProjection();
		if (projection == null) {
			return;
		}

		GeoPoint g = projection.fromPixels((int) xPixel, (int) yPixel);

		if (mPoiMarkers != null) {
			for(POIMarker marker:mPoiMarkers){
				POI poi = marker.getPOI();
				if (poi.inside(g)) {
					tapPOI(poi);
					return;
				}
			}
		}

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
		mPoiMarkers=null;
		if (result.getType() == PathSearchResult.Type.IN_BUILDING) {
//			IndoorEndPoint ep=(IndoorEndPoint)result.getEndPoint();
			IndoorEndPoint sp=(IndoorEndPoint)result.getStartPoint();
			
			setCurrentMapData(appContext.getMapDataService().findById(sp.getMapId()));
			
			setMapFile();
		} else if (result.getType() == PathSearchResult.Type.BETWEEN_BUILDING) {
			// NavRoute route = result.indoorRouteStart;

		} else if (result.getType() == PathSearchResult.Type.OUTDOOR_INDOOR) {
			finish();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onBackPressed() {
		if (mCurSel==1&&appContext.getPathSearchResult() != null) {
			showDialog(Utils.DLG_EXIT_NAVIGATION);
		} else if (mPoiMarkers != null) {
			mPoiMarkers = null;
			updateOverlay();
			return;
		} else {
			if ((System.currentTimeMillis() - lastPressTime) > 2000) {
				appContext.makeTextShort(R.string.msg_press_again_to_exit);
				lastPressTime = System.currentTimeMillis();
			} else {
				super.onBackPressed();
			}
		}
	}

	@Override
	public void result(Junction jun) {
		if(process.isProcessEnd()){
			mModuleMainFrameProcessNext.setText(R.string.frame_process_reset);
		}else{
			mModuleMainFrameProcessNext.setText(R.string.frame_process_next_step);
		}
		if(jun.getNodeType()!=NodeType.SWITCH&&jun.getNodeType()!=NodeType.END){
			File dataFile=new File(Utils.getFile(this,appContext.getCurrentDataNo()),"process/"+jun.getImage());
			mModuleMainFrameProcessImage.setImageBitmap(CommonFn.convertToBitmap(dataFile));
		}
	}
	
	@Override
	public void location(String mapId, String vertexId) {
		Vertex vertex=AppContext.getInstance().getVertexService().findById(vertexId);
		if(vertex!=null){
			location(mapId,vertex.getLatitude(),vertex.getLongitude());
		}
	}
	
	@Override
	public void location(String mapId, String latitude,String longitude) {

		if(mCurSel!=1){
			setCurPoint(1);
		}
		
		MyLocation myLocation = appContext.getMyLocation();

		if (myLocation != null) {
			PathSearchTask search = new PathSearchTask(this);
			EndPoint sp = new IndoorEndPoint(myLocation.getMapId(),
					myLocation.getGeoPoint());
			EndPoint ep = new IndoorEndPoint(mapId,
					new GeoPoint(Double.parseDouble(latitude), 
							Double.parseDouble(longitude)));
			search.execute(sp, ep);
		} else {
			Toast.makeText(this, R.string.msg_location_unavailable, Toast.LENGTH_SHORT).show();
		}
		
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(hasFocus){
			if(departmentArrayAdapter == null){
				isTabDepartment=false;
				List<Department> departments=appContext.getDepartmentService().findAll();
				departmentArrayAdapter = new ArrayAdapter<Department>(this, R.layout.lvitem_department, R.id.lvitem_department_name, departments);
				mModuleMainFrameMapQueryContentTabDepartment.setBackgroundResource(R.drawable.tabs_bar_left_on);
				mModuleMainFrameMapQueryContentTabDoctor.setBackgroundResource(R.drawable.tabs_bar_right_off);
				mModuleMainFrameMapQueryList.setAdapter(departmentArrayAdapter);
				departmentArrayAdapter.getFilter().filter(mapQuery.getText());
			}
			mapButtonCancel.setVisibility(View.VISIBLE);
			mapLLQueryContentContainer.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if(actionId == EditorInfo.IME_ACTION_SEARCH){
			((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mapQuery.getWindowToken(), 0);
			
			return true;
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		if(isTabDepartment){
			Doctor doctor=(Doctor)adapter.getItemAtPosition(position);
			Bundle bundle=new Bundle();
			bundle.putString(Doctor._ID, doctor.getId());
			Intent intent=new Intent(this,DoctorDetailActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}else{
			Department department=(Department)adapter.getItemAtPosition(position);
			Bundle bundle=new Bundle();
			bundle.putString(Department.COLUMN_NAME_ID, department.getId());
			Intent intent=new Intent(this,DepartmentDetailActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}
	
	private void setCurPoint(int index) {
		if (mCurSel != index) {
			if(index==-1){
				index=0;
			}
			if (index >= 0 && mFrameViewCount > index) {
				// 点击当前项刷新
				if (index == 0) {
					mModuleMainHeaderContentTitle.setText(R.string.frame_introduction_title);
					mModuleMainHeaderContentPark.setVisibility(View.GONE);
					mModuleMainHeaderContentLocation.setVisibility(View.GONE);
					mModuleMainFrameIntroductionContent.setVisibility(View.VISIBLE);
					mModuleMainFrameMapContent.setVisibility(View.GONE);
					mModuleMainFrameProcessContent.setVisibility(View.GONE);
					mModuleMainFrameFriendContent.setVisibility(View.GONE);
				} else if (index == 1) {
					mModuleMainHeaderContentTitle.setText(getCurrentMapData().getName());
					mModuleMainHeaderContentPark.setVisibility(View.VISIBLE);
					mModuleMainHeaderContentLocation.setVisibility(View.VISIBLE);
					mModuleMainFrameIntroductionContent.setVisibility(View.GONE);
					mModuleMainFrameMapContent.setVisibility(View.VISIBLE);
					mModuleMainFrameProcessContent.setVisibility(View.GONE);
					mModuleMainFrameFriendContent.setVisibility(View.GONE);
				} else if (index == 2) {
					File dataFile=new File(Utils.getFile(MainActivity.this,appContext.getCurrentDataNo()),"process");
					if(dataFile.exists()){
						mModuleMainHeaderContentTitle.setText(R.string.frame_process_title);
						mModuleMainHeaderContentPark.setVisibility(View.GONE);
						mModuleMainHeaderContentLocation.setVisibility(View.GONE);
						mModuleMainFrameIntroductionContent.setVisibility(View.GONE);
						mModuleMainFrameMapContent.setVisibility(View.GONE);
						mModuleMainFrameProcessContent.setVisibility(View.VISIBLE);
						mModuleMainFrameFriendContent.setVisibility(View.GONE);
					}else{
						appContext.makeTextLong(R.string.frame_process_not_definitions);
					}
				} else if (index == 3) {
					mModuleMainHeaderContentTitle.setText(R.string.frame_friend_title);
					mModuleMainHeaderContentPark.setVisibility(View.GONE);
					mModuleMainHeaderContentLocation.setVisibility(View.GONE);
					mModuleMainFrameIntroductionContent.setVisibility(View.GONE);
					mModuleMainFrameMapContent.setVisibility(View.GONE);
					mModuleMainFrameProcessContent.setVisibility(View.GONE);
					mModuleMainFrameFriendContent.setVisibility(View.VISIBLE);
				}
				mButtons[mCurSel].setChecked(false);
				mButtons[index].setChecked(true);
				mCurSel = index;
			}
		}
	}
	
	/**
	 * 初始化头部视图
	 */
	private void initHeaderView() {
		mModuleMainHeaderContentTitle = (TextView) findViewById(R.id.module_main_header_content_title);
		mModuleMainHeaderContentPark = (Button) findViewById(R.id.module_main_header_content_park);
		mModuleMainHeaderContentPark.setOnClickListener(this);
		mModuleMainHeaderContentLocation = (Button) findViewById(R.id.module_main_header_content_location);
		mModuleMainHeaderContentLocation.setOnClickListener(this);
	}

	/**
	 * 初始化底部视图
	 */
	private void initFooterView() {
		rboIntroduction = (RadioButton) findViewById(R.id.main_footbar_introduction);
		rboMap = (RadioButton) findViewById(R.id.main_footbar_map);
		rboProcess = (RadioButton) findViewById(R.id.main_footbar_process);
		rboFriend = (RadioButton) findViewById(R.id.main_footbar_friend);

		imMore = (ImageView) findViewById(R.id.main_footbar_more);
		imMore.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent(MainActivity.this,MoreActivity.class);
				startActivityForResult(intent,0);

			}
		});

		mMainContentLayout = (LinearLayout) findViewById(R.id.main_content_layout);
		mModuleMainFrameMapContent = (View) findViewById(R.id.module_main_frame_map_content);

		mapQuery=(EditText)findViewById(R.id.module_main_frame_map_query);
		mapQuery.setOnFocusChangeListener(this);
		mapQuery.setOnEditorActionListener(this);
		mapQuery.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable s) {
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,int count) {
				if(s!=null){
					if(isTabDepartment){
						doctorArrayAdapter.getFilter().filter(s);;
					}else{
						departmentArrayAdapter.getFilter().filter(s);
					}
				}
			}
			
		});
		mapButtonCancel=(Button)findViewById(R.id.module_main_frame_map_button_cancel);
		mapButtonCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mapQuery.setText(R.string.empty);
				mModuleMainFrameMapContent.requestFocus();
				mapButtonCancel.setVisibility(View.GONE);
				mapLLQueryContentContainer.setVisibility(View.GONE);
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mapQuery.getWindowToken(), 0);
			}
		});
		mapLLQueryContentContainer=(LinearLayout)findViewById(R.id.module_main_frame_map_query_content_container);
		
		mModuleMainFrameIntroductionContent = (View) findViewById(R.id.module_main_frame_introduction_content);
		mModuleMainFrameProcessContent = (View) findViewById(R.id.module_main_frame_process_content);
		mModuleMainFrameFriendContent = (View) findViewById(R.id.module_main_frame_friend_content);

		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.module_main_footer_content);
		mFrameViewCount = mMainContentLayout.getChildCount();
		mButtons = new RadioButton[mFrameViewCount];

		for (int i = 0; i < mFrameViewCount; i++) {
			mButtons[i] = (RadioButton) linearLayout.getChildAt(i * 2);
			mButtons[i].setTag(i);
			mButtons[i].setChecked(false);
			mButtons[i].setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					setCurPoint((Integer) (v.getTag()));
				}
			});
		}

	}

	/**
	 * 初始化主体框架视图
	 */
	private void initMainFrameView() {
		// introduction
		
		// map
		mModuleMainFrameMapQueryContentTabDepartment=(TextView)findViewById(R.id.module_main_frame_map_query_content_tab_department);
		mModuleMainFrameMapQueryContentTabDoctor=(TextView)findViewById(R.id.module_main_frame_map_query_content_tab_doctor);
		mModuleMainFrameMapQueryList=(ListView)findViewById(R.id.module_main_frame_map_query_list);
		mModuleMainFrameMapQueryList.setOnItemClickListener(this);
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
		
		// process
		mModuleMainFrameProcessImage = (ImageView) findViewById(R.id.module_main_frame_process_image);
		mModuleMainFrameProcessNext = (Button) findViewById(R.id.module_main_frame_process_next);
		
		// friend
		friendLocationPullListData=new PullListViewData(this);
		friendLocationPullListData.setOnLoadDataListener(
				new OnLoadDataListener(){

					@Override
					public void LoadData(LoadMode loadMode) {
						if(appContext.isLogin()){
							Map<String,String> requestParams=new HashMap<String,String>();
							requestParams.put("maprecordno",appContext.getCurrentDataNo());
							requestParams.put("orderby","2");
							friendLocationPullListData.sendPullToRefreshListViewNetRequest(loadMode,Constant.ServerAPI.ufriendiQuery,requestParams,null,new UIRunnable(){
								@Override
								public void run() {
									friendLocationPullListData.getAdapter().notifyDataSetChanged();
								} 
							},"friendlist","friendlist"+TAG);
						}else{
							
							friendLocationPullListData.getPulllistview().setTag(HttpService.LISTVIEW_DATA_MORE);
							friendLocationPullListData.getListview_footer_more().setText(R.string.load_more);
							friendLocationPullListData.getListview_footer_progress().setVisibility(View.GONE);
							friendLocationPullListData.getPulllistview().onRefreshComplete();
							
							if(mCurSel==3){
								new AlertDialog.Builder(MainActivity.this).
								setMessage(R.string.msg_not_login).
								setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										startActivityForResult(new Intent(MainActivity.this,LoginActivity.class), 
												REQUEST_CODE_REFRESH_FRIEND_LOCATION);
									}
									
								}).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
									
								}).show();
							}
							
						}
					}
					
				});
		friendLocationPullListData.start(R.id.module_main_frame_friend_location_pulllistview, 
				new FriendLocationAdapter(this,friendLocationPullListData));
	}
	
	private void loadData(){
		mRooms=appContext.getRoomService().findAllPullMap();
		this.loadMainMapData();
		process=new ProcessService(this,this);
		process.init();
		mModuleMainFrameProcessImage.setImageBitmap(CommonFn.convertToBitmap(process.getProcessImageFile()));
	}
	
	/**
	 * 加载园区地图平面图
	 */
	private void loadMainMapData(){
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
	private void setMapFile() {
		
		if(mCurSel==1){
			mModuleMainHeaderContentTitle.setText(getCurrentMapData().getName());
		}
		
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
		File dataFile=new File(Utils.getFile(MainActivity.this,appContext.getCurrentDataNo()),path);
		if (setMapFile(dataFile)) {
			updateOverlay();
		}else{
			return;
		}
	}

	/**
	 * 更新地图上的覆盖图、路线、位置等信息
	 */
	private void updateOverlay() {

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
		
		if(mPoiMarkers!=null){
			Boolean isSetCenterPoint=true;
			Drawable d=getResources().getDrawable(R.drawable.icon_node);
			ArrayItemizedOverlay poiOverlays=new ArrayItemizedOverlay(d);
			for(POIMarker marker:mPoiMarkers){
				if(getCurrentMapData().getId().equals(marker.getPOI().getMapId())){
					poiOverlays.addItem(marker);
					if(isSetCenterPoint){
						// 设置当前第一个目标位置点为中心点
						getMapView().setCenter(marker.getPOI().getGeoPoint());
						isSetCenterPoint=false;
					}
				}
			}
			itemList.add(poiOverlays);
		}
		
	}

	@SuppressWarnings("deprecation")
	private void tapPOI(POI poi) {
		Bundle data = new Bundle();
		data.putSerializable(BUNDLEDATA_DATA, poi);
		showDialog(Utils.DLG_POI, data);

		getMapView().setCenter(poi.getGeoPoint());
	}
	
	private class UpdateLocation extends Thread{
		
		private Boolean flag;
		
		public UpdateLocation(){
			flag=true;
		}
		
		public void setFlag(Boolean flag) {
			this.flag = flag;
		}
		
		@Override
		public void run() {
			while(true&&flag){
				if(getCurrentMapData()!=null){
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							if(mCurSel==1){
								
								MyLocation myLocation=appContext.locate();
								addMyLocMarker(myLocation);
								//TODO：路线定位
								PathSearchResult psr=appContext.getPathSearchResult();
								if(psr!=null){
									if (myLocation != null) {
										PathSearchTask search = new PathSearchTask(MainActivity.this);
										EndPoint sp = new IndoorEndPoint(myLocation.getMapId(),
												myLocation.getGeoPoint());
										search.execute(sp, psr.getEndPoint());
									} else {
										appContext.makeTextLong( R.string.msg_location_unavailable);
									}
								}
							}
						}
					});
				}
				try {
					//每3秒定位一次
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
}