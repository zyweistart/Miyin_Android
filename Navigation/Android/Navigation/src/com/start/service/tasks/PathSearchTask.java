package com.start.service.tasks;

import org.mapsforge.core.GeoPoint;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.widget.Toast;

import com.start.model.Vertex;
import com.start.model.nav.EndPoint;
import com.start.model.nav.Graph;
import com.start.model.nav.IndoorEndPoint;
import com.start.model.nav.NavRoute;
import com.start.model.nav.PathSearchResult;
import com.start.navigation.AppContext;
import com.start.navigation.R;

public class PathSearchTask extends AsyncTask<EndPoint, Void, PathSearchResult> {

	protected Context context;
	protected ProgressDialog dialog;
	protected PathSearchListener listener;
	
	public PathSearchTask(Context context) {
		this.context = context;
		if (context instanceof PathSearchListener) {
			this.listener = (PathSearchListener) context;
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		// Show a loading progress dialog
		dialog = ProgressDialog.show(this.context, null, context.getString(R.string.msg_path_searching), true, true, new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				PathSearchTask.this.cancel(true);
			}
			
		});
		
	}

	@Override
	protected PathSearchResult doInBackground(EndPoint... params) {
		PathSearchResult result = new PathSearchResult(params[0], params[1]);

		IndoorEndPoint ep = (IndoorEndPoint) params[1];
		
		if (params[0] instanceof IndoorEndPoint) {
			
			IndoorEndPoint sp = (IndoorEndPoint) params[0];
			
			Graph g = new Graph();
			g.init();
			
			NavRoute r;
			if(ep.getVertex()!=null){
				r = searchInMap(g, sp.getGeoPoint(), ep.getMapId()+ep.getVertex(), sp.getMapId());
			}else{
				Vertex v = g.getClosestVertex(ep.getGeoPoint(),ep.getMapId());
				r = searchInMap(g, sp.getGeoPoint(), ep.getMapId()+v.getId(), sp.getMapId());
			}
			
			result.indoorRouteEnd = r;
			result.type = PathSearchResult.Type.IN_BUILDING;

		}

		return result;
	}

	@Override
	protected void onPostExecute(PathSearchResult result) {
		super.onPostExecute(result);
		
		dialog.dismiss();

		if (listener != null) {
			
			if (result.isValid()) {
				AppContext.getInstance().setPathSearchResult(result);
				listener.onGetResult(result);
			} else {
				Toast.makeText(context, "暂无结果", Toast.LENGTH_SHORT).show();
			}

		}
	}

	protected NavRoute searchInMap(Graph g, GeoPoint startPoint, String endVertex, String mapId) {
		if (startPoint == null || endVertex == null || mapId == null) {
			throw new IllegalArgumentException("null parameters");
		}

		Vertex v = g.getClosestVertex(startPoint,mapId);
		if (v == null) {
			return null;
		}
		NavRoute route = searchInMap(g, v.getMapId()+v.getId(), endVertex);
		return route;
	}

	protected NavRoute searchInMap(Graph g, final String startVertex, final String endVertex) {
		if (startVertex == null || endVertex == null) {
			throw new IllegalArgumentException("null parameters");
		}

		NavRoute route = g.findPath(startVertex, endVertex);
		return route;
	}

	public interface PathSearchListener {
		
		void onGetResult(PathSearchResult result);
		
	}
	
}