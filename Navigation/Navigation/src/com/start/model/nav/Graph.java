package com.start.model.nav;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.mapsforge.core.GeoPoint;

import android.util.Log;

import com.start.model.Edge;
import com.start.model.Vertex;
import com.start.navigation.AppContext;

/**
 * A Graph represents all the vertexes and edges consist the map.
 */
public class Graph extends ArrayList<LinkedList<Short>> {

	private static final long serialVersionUID = -1310931483972347772L;

	private HashMap<String, Vertex> vertexMap = new HashMap<String, Vertex>();
	private ArrayList<String> vertexIds = new ArrayList<String>();

	/**
	 * Add a vertex into the graph
	 * @param v the vertex to add
	 */
	public synchronized void addVertex(Vertex v) {
		String key=v.getMapId()+v.getId();
		vertexMap.put(key, v);
		vertexIds.add(key);
		add(new LinkedList<Short>());
	}

	/**
	 * Add an edge into the graph
	 * 
	 * @param aVertexName the name of the end point of edge
	 * @param bVertexName the name of the end point of edge
	 */
	public synchronized void addEdge(String aVertexName, String bVertexName) {
		int aVertexIdx = vertexIds.indexOf(aVertexName);
		int bVertexIdx = vertexIds.indexOf(bVertexName);

		if (aVertexIdx == -1 || bVertexIdx == -1) {
			Log.e("error", "wrong edge: " + aVertexName + ", " + bVertexName);
			return;
		}
		get(aVertexIdx).add((short) bVertexIdx);
		get(bVertexIdx).add((short) aVertexIdx);
	}
	
	private boolean hasEdge(int a, int b) {
		for (Short s : get(a)) {
			if (s == b) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Find the nearest vertex to a point in the map
	 *  
	 * @param geoPoint
	 * @return the nearest vertex
	 */
	public Vertex getClosestVertex(GeoPoint geoPoint,String mapId) {
		double minDist = Double.MAX_VALUE;
		double longitude = geoPoint.getLongitude(), latitude = geoPoint.getLatitude();

		Vertex target = null;
		for (Vertex v : vertexMap.values()) {
			if(v.getMapId().equals(mapId)){
				double dist = Math.abs(longitude - Double.parseDouble(v.getLongitude())) + Math.abs(latitude - Double.parseDouble(v.getLatitude()));
				if (dist < minDist) {
					target = v;
					minDist = dist;
				}
			}
		}
		return target;
	}
	
	/*
	 * Shortest path algorithm
	 */
	private ArrayList<Vertex> searchPath(short start, short end) {
		Queue<Short> q = new LinkedList<Short>();
		int n = vertexIds.size();
		boolean[] visit = new boolean[n];
		int[] pre = new int[n];
		q.add(start);
		pre[start] = -1;
		visit[start] = true;
		while (!q.isEmpty()) {
			int cur = q.peek();
			if (cur == end)
				break;
			q.remove();
			
			for (Short i : get(cur)) {
				if (hasEdge(cur, i)/*graph[cur][i] == 1*/ && !visit[i]) {
					q.add((short) i);
					pre[i] = cur;
					visit[i] = true;
				}
			}
		}
		ArrayList<Vertex> p = new ArrayList<Vertex>();
		if (!q.isEmpty()) {
			for (int i = end; i != -1; i = pre[i]) {
				p.add(vertexMap.get(vertexIds.get(i)));
			}
		}
		return p;
	}

	/**
	 * Find the shortest path between two vertex
	 * 
	 * @param startName the start of the path
	 * @param endName the end of the path
	 * 
	 * @return The shortest path
	 */
	public NavRoute findPath(String startName, String endName) {
		int start = vertexIds.indexOf(startName), end = vertexIds.indexOf(endName);
		ArrayList<Vertex> pathVertexes = searchPath((short) start, (short) end);
		return dividePath(pathVertexes);
	}
	
	/*
	 * Divide the path into segments by vertex' floor.
	 */
	private NavRoute dividePath(ArrayList<Vertex> path) {
		if (path == null || path.size() < 1) {
			return null;
		}
		
		int len = path.size();
		NavRoute route = new NavRoute();

		Vertex start = path.get(len - 1);
		NavStep step = new NavStep(start.getMapId());
		step.setStart(start);
		step.add(start.getGeoPoint());
		route.add(step);
		if (len == 1) {
			return route;
		}

		int i = len - 2;
		while (i >= 0) {
			Vertex v = path.get(i);
			if(v.getMapId().equals(step.getMapId())){
				step.add(v.getGeoPoint());
				step.setEnd(v);
			} else {
				step = new NavStep(v.getMapId());
				step.setStart(v);
				step.add(v.getGeoPoint());
				route.add(step);
			}
			i--;
		}
		return route;
	}

	/**
	 * Init a graph for a building
	 * 
	 * @param buildingName building name
	 */
	public void init() {
		List<Vertex> vertexs=AppContext.getInstance().getVertexService().findAll();
		for(Vertex v:vertexs){
			addVertex(v);
		}
		List<Edge> edges=AppContext.getInstance().getEdgeService().findAll();
		for(Edge e:edges){
			addEdge(e.getVertexStartId(), e.getVertexEndId());
		}
	}
}
