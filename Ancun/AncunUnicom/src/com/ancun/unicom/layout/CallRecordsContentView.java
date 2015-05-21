package com.ancun.unicom.layout;

import java.util.List;
import java.util.Map;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ancun.core.BaseActivity;
import com.ancun.core.BaseScrollContent;
import com.ancun.unicom.R;
import com.ancun.unicom.adapter.CallRecordsAdapter;
import com.ancun.unicom.adapter.CallRecordsAdapter.HolderView;

public class CallRecordsContentView extends BaseScrollContent implements OnItemClickListener{
	
	//是否刷新数据
	public static Boolean isRefreshData=true;
	
	private ListView mListview;
	private CallRecordsAdapter mAdapter;
	private List<Map<String,String>> mListDataItems;

	public CallRecordsContentView(BaseActivity activity) {
		super(activity, R.layout.module_scroll_call_records);
		mListview = (ListView) findViewById(R.id.listview);
		mListview.setOnItemClickListener(this);
	}
	
	public void loadData(){
		new Thread() {
			
			public void run() {
				
				mListDataItems = getCurrentActivity().getRecentDaoImpl().findAllCallRecords();
				
				getCurrentActivity().runOnUiThread(new Runnable() {
					public void run() {
						if (mAdapter==null) {
							mAdapter = new CallRecordsAdapter(getCurrentActivity());
							mAdapter.setItemDatas(mListDataItems);
							mListview.setAdapter(mAdapter);
						} else {
							mAdapter.setItemDatas(mListDataItems);
							mAdapter.notifyDataSetChanged();
						}
						isRefreshData=false;
					}
				});
				
			};
			
		}.start();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		HolderView v=(HolderView)view.getTag();
		v.dial_frame.setVisibility(View.VISIBLE);
		mAdapter.setLastPosition(position);
	}
	
}