package com.start.model.nav;




public class PathSearchResult {

	public enum Type {NORMAL, IN_BUILDING, BETWEEN_BUILDING, OUTDOOR_INDOOR, OUTDOOR }
	public NavRoute indoorRouteStart;
	public NavRoute indoorRouteEnd;
	public volatile boolean outdoorSearchFinished = true;
	public Type type;
	public EndPoint start, end;

	public PathSearchResult(EndPoint start, EndPoint end) {
		this.start = start;
		this.end = end;
		
	}

	public Type getType() {
		return type == null ? Type.NORMAL : type;
	}
	
	public NavRoute getRoute() {
		if (indoorRouteStart != null) {
			return indoorRouteStart;
		} else if (indoorRouteEnd != null) {
			return indoorRouteEnd;
		} else {
			return null;
		}
	}
	
	public EndPoint getStartPoint() {
		return start;
	}
	
	public EndPoint getEndPoint() {
		return end;
	}
	
	public boolean isValid() {
		boolean valid = false;
		switch (type) {
		case IN_BUILDING:
			valid = indoorRouteEnd != null;
			break;
		default:
			valid = false;
		}
		
		return valid;
	}
}
