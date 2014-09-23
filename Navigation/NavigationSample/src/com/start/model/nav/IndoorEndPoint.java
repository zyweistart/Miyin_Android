package com.start.model.nav;

import org.mapsforge.core.GeoPoint;



public class IndoorEndPoint implements EndPoint {

	private String mapId;
	private String vertex;
	private GeoPoint geoPoint;
	
	public IndoorEndPoint(String mapId, GeoPoint p) {
		this.mapId=mapId;
		this.geoPoint = p;
	}
	
	public IndoorEndPoint(String mapId, GeoPoint p, String vertex) {
		this.mapId=mapId;
		this.geoPoint = p;
		this.vertex = vertex;
	}
	
	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	public String getVertex() {
		return vertex;
	}

	public void setVertex(String vertex) {
		this.vertex = vertex;
	}

	public GeoPoint getGeoPoint() {
		return geoPoint;
	}

	public void setGeoPoint(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}

}
