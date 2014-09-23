package com.start.model.overlay;

import java.io.Serializable;

import org.mapsforge.core.GeoPoint;

public interface POI extends Serializable {

	String getId();
	
	GeoPoint getGeoPoint();
	
	String getName();
	
	String getMapId();
	
	String getVertexId();
	
	boolean inside(GeoPoint p);
	
}
