package com.start.model.overlay;

import org.mapsforge.android.maps.overlay.ArrayItemizedOverlay;
import org.mapsforge.android.maps.overlay.OverlayItem;

import android.graphics.drawable.Drawable;

public class POIMarker extends OverlayItem {

	protected POI poi;

	public POIMarker(POI obj, Drawable drawable) {
		super(obj.getGeoPoint(),null, null, ArrayItemizedOverlay.boundCenter(drawable));
		this.poi = obj;
	}
	
	public POI getPOI() {
		return poi;
	}
	
}
