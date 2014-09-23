package com.start.model;

import java.util.List;

import org.mapsforge.core.GeoPoint;

import com.start.core.CoreModel;
import com.start.model.nav.Lasso;
import com.start.model.overlay.POI;
import com.start.navigation.AppContext;

public class Room extends CoreModel implements POI {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static String TABLE_NAME="ST_ROOM";
	
	public static final String COLUMN_NAME_MAPID="mapId";
	public static final String COLUMN_NAME_NAME="name";
	public static final String COLUMN_NAME_VERTEXID="vertextId";
	
	public static final String CREATE_TABLE_SQL = 
			"CREATE TABLE " + TABLE_NAME + " ("
			+ COLUMN_NAME_ID + " TEXT,"
			+ COLUMN_NAME_NO + " TEXT,"
			+ COLUMN_NAME_MAPID + " TEXT,"
			+ COLUMN_NAME_NAME + " TEXT,"
			+ COLUMN_NAME_VERTEXID + " TEXT"
			+ ");";
	
	/**
	 * 房间名字
	 */
	private String name;
	/**
	 * 所属地图编号
	 */
	private String mapId;
	/**
	 * 所属科室编号（可选）
	 */
	private String departmentId;
	/**
	 * 导航中心点（Vertex对象）
	 */
	private String vertexId;
	/**
	 * 坐标区域
	 */
	private double[][] area;
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getMapId() {
		return mapId;
	}


	public void setMapId(String mapId) {
		this.mapId = mapId;
	}


	public String getDepartmentId() {
		return departmentId;
	}


	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getVertexId() {
		return vertexId;
	}


	public void setVertexId(String vertexId) {
		this.vertexId = vertexId;
	}


	public double[][] getArea() {
		return area;
	}

	public void setArea(double[][] area) {
		this.area = area;
	}


	@Override
	public GeoPoint getGeoPoint() {
		Vertex vertex=AppContext.getInstance().getVertexService().findById(getVertexId());
		if(vertex!=null){
			return new GeoPoint(Double.parseDouble(vertex.getLatitude()), Double.parseDouble(vertex.getLongitude()));
		}
		return null;
	}

	@Override
	public boolean inside(GeoPoint p) {
		List<RoomArea> ras=AppContext.getInstance().getRoomAreaService().findAllByRoomId(getId());
		double[] mPolyX=new double[ras.size()];
		double[] mPolyY=new double[ras.size()];
		for(int i=0;i<ras.size();i++){
			mPolyX[i]=Double.parseDouble(ras.get(i).getLatitude());
			mPolyY[i]=Double.parseDouble(ras.get(i).getLongitude());
		}
		Lasso lasso=new Lasso(mPolyX,mPolyY,ras.size());
		return lasso.contains(p.getLatitude(), p.getLongitude());
	}

}