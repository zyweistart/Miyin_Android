package com.start.model;

import org.mapsforge.core.GeoPoint;

import com.start.core.CoreModel;


public class Vertex extends CoreModel {
	
	public static final String TABLE_NAME="ST_VERTEX";
	
	public static final String COLUMN_NAME_MAPID="mapId";
	public static final String COLUMN_NAME_LATITUDE="latitude";
	public static final String COLUMN_NAME_LONGITUDE="longitude";
	
	public static final String CREATE_TABLE_SQL = 
			"CREATE TABLE " + TABLE_NAME + " ("
			+ COLUMN_NAME_ID + " TEXT,"
			+ COLUMN_NAME_NO + " TEXT,"
			+ COLUMN_NAME_MAPID + " TEXT,"
			+ COLUMN_NAME_LATITUDE + " TEXT,"
			+ COLUMN_NAME_LONGITUDE + " TEXT"
			+ ");";
	
	/**
	 * 所属地图编号
	 */
	private String mapId;
	/**
	 * 纬度
	 */
	private String latitude;
	/**
	 * 经度
	 */
	private String longitude;
	
	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public GeoPoint getGeoPoint(){
		return new GeoPoint(Double.parseDouble(getLatitude()), Double.parseDouble(getLongitude()));
	}
	
}
