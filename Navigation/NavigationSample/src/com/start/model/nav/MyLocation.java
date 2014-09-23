package com.start.model.nav;

import org.mapsforge.core.GeoPoint;

import com.start.model.overlay.POI;

public class MyLocation implements POI {

	private static final long serialVersionUID = 81813808830727223L;

	private String mapId;
	private String latitude;
	private String longitude;
	private GeoPoint indoorPoint;

	public MyLocation(String targetMapId, String latitude,String longitude) {
		this.mapId = targetMapId;
		this.latitude=latitude;
		this.longitude=longitude;
	}

	public boolean locateIn(String targetMapId) {
		return mapId.equals(targetMapId);
	}
	
	/**
	 * 判断是否在室内
	 * @return
	 */
	public boolean isIndoor(){
		return true;
	}
	
	public GeoPoint getGeoPoint() {
		if(indoorPoint==null){
			indoorPoint=new GeoPoint(Double.parseDouble(latitude), Double.parseDouble(longitude));
		}
		return indoorPoint;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	@Override
	public String getVertexId() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean inside(GeoPoint p) {
		return false;
	}

	@Override
	public String getMapId() {
		return mapId;
	}

	@Override
	public String getId() {
		return null;
	}

}