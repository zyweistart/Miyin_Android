package com.start.model.nav;

import org.mapsforge.core.GeoPoint;

public class OutdoorEndPoint implements EndPoint {

	private GeoPoint geoPoint;

	public OutdoorEndPoint(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}

	public GeoPoint getGeoPoint() {
		return geoPoint;
	}
	
}
