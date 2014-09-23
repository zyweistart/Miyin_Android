package com.start.navigation;

import java.util.List;
import java.util.Map;
import java.util.Random;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.start.core.Constant;
import com.start.model.Vertex;
import com.start.model.nav.MyLocation;
import com.start.model.nav.PathSearchResult;
import com.start.service.EdgeService;
import com.start.service.MapDataService;
import com.start.service.RoomAreaService;
import com.start.service.RoomService;
import com.start.service.VertexService;
import com.start.utils.SharedPreferencesUtils;

/**
 * 应用必须继承该Application类
 * @author start
 *
 */
public class AppContext extends Application {
	
	private static AppContext mInstance;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private PathSearchResult pathSearchResult;
    
    private MapDataService mapDataService;
    private RoomService roomService;
    private VertexService vertexService;
    private RoomAreaService roomAreaService;
    private EdgeService edgeService;

	@Override
    public void onCreate() {
	    super.onCreate();
		mInstance = this;
	}
	
	public static AppContext getInstance() {
		return mInstance;
	}
	
	public SharedPreferencesUtils getSharedPreferencesUtils() {
		if(sharedPreferencesUtils==null){
			sharedPreferencesUtils=new SharedPreferencesUtils(this);
		}
		return sharedPreferencesUtils;
	}
	
	public PathSearchResult getPathSearchResult() {
		return pathSearchResult;
	}

	public void setPathSearchResult(PathSearchResult pathSearchResult) {
		this.pathSearchResult = pathSearchResult;
	}

	public MapDataService getMapDataService() {
		if(mapDataService==null){
			mapDataService=new MapDataService(this);
		}
		return mapDataService;
	}
	
	public RoomService getRoomService() {
		if(roomService==null){
			roomService=new RoomService(this);
		}
		return roomService;
	}
	
	public VertexService getVertexService() {
		if(vertexService==null){
			vertexService=new VertexService(this);
		}
		return vertexService;
	}

	public RoomAreaService getRoomAreaService() {
		if(roomAreaService==null){
			roomAreaService=new RoomAreaService(this);
		}
		return roomAreaService;
	}
	
	public EdgeService getEdgeService() {
		if(edgeService==null){
			edgeService=new EdgeService(this);
		}
		return edgeService;
	}
	
	/**
	 * 当前用户是否已经登录
	 * @return
	 */
	public Boolean isLogin(){
		return userInfo!=null;
	}
	
	private Map<String,Map<String,String>> userInfo;
	
	/**
	 * 获取当前用户信息
	 * @return
	 */
	public Map<String,String> getUserInfoByKey(String key){
		if(userInfo!=null){
			return userInfo.get(key);
		}
		return null;
	}
	
	public void initUserInfo(Map<String, Map<String, String>> userInfo) {
		this.userInfo = userInfo;
	}
	
	/**
	 * 获取当前登录账户的唯一码
	 * @return
	 */
	public String getMyID(){
		if(userInfo!=null){
			Map<String,String> data=getUserInfoByKey("userinfo");
			if(data!=null){
				return data.get("email");
			}
		}
		return Constant.EMPTYSTR;
	}
	
	public String getAccessID(){
		if(userInfo!=null){
			Map<String,String> data=getUserInfoByKey("userinfo");
			if(data!=null){
				return data.get("accessid");
			}
		}
		return Constant.EMPTYSTR;
	}

	public String getAccessKEY(){
		if(userInfo!=null){
			Map<String,String> data=getUserInfoByKey("userinfo");
			if(data!=null){
				return data.get("accesskey");
			}
		}
		return Constant.EMPTYSTR;
	}
	
	/**
	 * 获取位置信息
	 * @return
	 */
	public MyLocation getMyLocation(){
		String data=getSharedPreferencesUtils().getString(Constant.SharedPreferences.USERLOCATION, Constant.EMPTYSTR);
		if(Constant.EMPTYSTR.equals(data)){
			return locate();
		}else{
			String[] info=data.split(";");
			return new MyLocation(info[0], info[1],info[2]);
		}
	}
	
	public MyLocation locate(){
		//TODO:定位代码需修改目前为随机获取
		Random random=new Random();
		List<Vertex> vertexs=getVertexService().findAll();
		Vertex v=vertexs.get(random.nextInt(vertexs.size()));
		String mapId=v.getMapId();
		String latitude=v.getLatitude();
		String longitude=v.getLongitude();
		getSharedPreferencesUtils().putString(Constant.SharedPreferences.USERLOCATION, mapId+";"+latitude+";"+longitude);
		return new MyLocation(mapId, latitude,longitude);
	}
	
	public void makeTextShort(int resId) {
		sendMessage(Toast.LENGTH_SHORT,getString(resId));
	}
	
	public void makeTextShort(String text) {
		if(!TextUtils.isEmpty(text)){
			sendMessage(Toast.LENGTH_SHORT,text);
		}
	}

	public void makeTextLong(int resId) {
		sendMessage(Toast.LENGTH_LONG,getString(resId));
	}
	
	public void makeTextLong(String text) {
		if(!TextUtils.isEmpty(text)){
			sendMessage(Toast.LENGTH_LONG,text);
		}
	}
	
	public void sendMessage(int what, Object obj) {
		Message msg = new Message();
		msg.what = what;
		msg.obj = obj;
		sendMessage(msg);
	}
	
	public void sendMessage(Message msg) {
		handler.sendMessage(msg);
	}

	public void sendEmptyMessage(int what) {
		handler.sendEmptyMessage(what);
	}
	
	public Handler handler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case Toast.LENGTH_SHORT:
				Toast.makeText(getApplicationContext(), String.valueOf(msg.obj), Toast.LENGTH_SHORT).show();
				break;
			case Toast.LENGTH_LONG:
				Toast.makeText(getApplicationContext(), String.valueOf(msg.obj), Toast.LENGTH_LONG).show();
				break;
			}
		}
		
	};
	
    /**
     * 当前的使用数据编号
     * @return
     */
	public String getCurrentDataNo(){
		return getSharedPreferencesUtils().getString(Constant.SharedPreferences.CURRENTDATAFILENO, Constant.EMPTYSTR);
	}
	
}