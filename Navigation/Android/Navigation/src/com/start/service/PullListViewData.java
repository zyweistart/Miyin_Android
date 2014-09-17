package com.start.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.start.model.UIRunnable;
import com.start.navigation.R;
import com.start.service.HttpService.LoadMode;
import com.start.widget.PullToRefreshListView;

public class PullListViewData {

	private Activity activity;
	
	private View layoutView;
	
	private PullToRefreshListView pulllistview;
	private View listview_footer;
	private TextView listview_footer_more;
	private ProgressBar listview_footer_progress;
	private BaseAdapter adapter;
	private List<Map<String,String>> dataItemList=new ArrayList<Map<String,String>>();
	
	private OnLoadDataListener onLoadDataListener;
	
	private HttpService httpService;
	
	public PullListViewData(Activity activity){
		this.activity=activity;
		httpService=new HttpService(activity);
	}
	
	public void start(int pulllistviewId,DataAdapter adapter){
		start(pulllistviewId, adapter, null);
	}
	
	public void start(int pulllistviewId,DataAdapter adapter,final OnItemClickListener itemClick){
		
		this.start(pulllistviewId, adapter, itemClick,LoadMode.INIT);
		
	}
	
	public void start(int pulllistviewId,DataAdapter adapter,final OnItemClickListener itemClick,LoadMode mode){
		this.adapter=adapter;
		listview_footer = activity.getLayoutInflater().inflate(R.layout.widget_pull_to_refresh_footer, null);
		listview_footer_more = (TextView)listview_footer.findViewById(R.id.listview_foot_more);
		listview_footer_progress = (ProgressBar)listview_footer.findViewById(R.id.listview_foot_progress);
		listview_footer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getOnLoadDataListener().LoadData(LoadMode.FOOT);
			}
		});
		if(getLayoutView()!=null){
			pulllistview=(PullToRefreshListView)getLayoutView().findViewById(pulllistviewId);
		}else{
			pulllistview=(PullToRefreshListView)activity.findViewById(pulllistviewId);
		}
		pulllistview.addFooterView(listview_footer);
		pulllistview.setAdapter(adapter);
		if(itemClick!=null){
			pulllistview.setOnItemClickListener(
				new AdapterView.OnItemClickListener() {
	
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
							//点击头部、底部栏无效
					    		if(position == 0 || view == getListview_footer()) return;
					    		getAdapter().notifyDataSetInvalidated();
					    		itemClick.onItemClick(parent, view, position, id);
						}
					
				}
			);
		}
		pulllistview.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            public void onRefresh() {
            		getOnLoadDataListener().LoadData(LoadMode.HEAD);
	        }
	    });
		if(mode!=null){
			getOnLoadDataListener().LoadData(mode);
		}
	}
	
	//接口
	public interface OnLoadDataListener{
		void LoadData(LoadMode loadMode);
	}
	
	public interface OnItemClickListener{
		void onItemClick(AdapterView<?> parent, View view, int position, long id);
	}
	
	public abstract class DataAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			return getDataItemList().size();
		}

		@Override
		public Object getItem(int position) {
			return  getDataItemList().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
	}
	
	//GET方法
	public PullToRefreshListView getPulllistview() {
		return pulllistview;
	}

	public View getListview_footer() {
		return listview_footer;
	}

	public TextView getListview_footer_more() {
		return listview_footer_more;
	}

	public ProgressBar getListview_footer_progress() {
		return listview_footer_progress;
	}

	public BaseAdapter getAdapter() {
		return this.adapter;
	}

	public List<Map<String, String>> getDataItemList() {
		return dataItemList;
	}

	public void setDataItemList(List<Map<String, String>> dataItemList) {
		this.dataItemList=dataItemList;
	}
	
	public OnLoadDataListener getOnLoadDataListener() {
		return onLoadDataListener;
	}
	
	public View getLayoutView() {
		return layoutView;
	}

	public void setLayoutView(View layoutView) {
		this.layoutView = layoutView;
	}

	//SET方法
	public void setOnLoadDataListener(OnLoadDataListener onLoadDataListener) {
		this.onLoadDataListener = onLoadDataListener;
	}
	
	//方法
	public void sendPullToRefreshListViewNetRequest(
			final LoadMode loadMode,
			final String Url,
			final Map<String,String> params,
			final Map<String,String> headerParams,
			final UIRunnable uiRunnable,
			final String DATALIST,
			final String TAGName){
		httpService.sendPullToRefreshListViewNetRequest(
				getDataItemList(), 
				getPulllistview(),
				getListview_footer(), 
				getListview_footer_more(),
				getListview_footer_progress(),
				loadMode, 
				Url, 
				params, 
				headerParams, 
				uiRunnable, 
				DATALIST, 
				TAGName);
	}
	
}