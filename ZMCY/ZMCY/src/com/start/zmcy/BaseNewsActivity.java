package com.start.zmcy;

import java.util.HashMap;
import java.util.Map;

import start.core.AppException;
import start.widget.xlistview.XListView;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.start.core.BaseActivity;
import com.start.core.Constant;
import com.start.service.HttpRunnable;
import com.start.service.HttpServer;
import com.start.service.RefreshListServer;
import com.start.service.Response;
import com.start.service.RefreshListServer.RefreshListServerListener;
import com.start.zmcy.adapter.NewsListAdapter;
import com.start.zmcy.adapter.NewsListAdapter.HolderView;
import com.start.zmcy.content.NewsContentFragment;

public class BaseNewsActivity extends BaseActivity implements RefreshListServerListener{
	
	protected String id,keyword;
	protected XListView mListView;
	protected RefreshListServer mRefreshListServer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collect);
		
		mListView = (XListView)findViewById(R.id.xlv_listview);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				if(id>0){
					HolderView hv=(HolderView)view.getTag();
					NewsContentFragment.gotoNews(BaseNewsActivity.this,hv.id);
				}else{
					mRefreshListServer.getCurrentListView().startLoadMore();
				}
			}
			
		});
		mRefreshListServer = new RefreshListServer(this,getHandlerContext(), mListView,new NewsListAdapter(this));
		mRefreshListServer.setCacheTag(TAG);
		mRefreshListServer.setRefreshListServerListener(this);
 	}
	
	@Override
	public void onLoading(final int HANDLER) {
		HttpServer hServer = new HttpServer(Constant.URL.GetListALL,mRefreshListServer.getHandlerContext());
		Map<String, String> params = new HashMap<String, String>();
		params.put("Id", id);
		if(!TextUtils.isEmpty(keyword)){
			params.put("keyword", keyword);
		}
		params.put("index",String.valueOf(mRefreshListServer.getCurrentPage() + 1));
//		params.put("size", String.valueOf(AppConstant.PAGESIZE));
		hServer.setParams(params);
		hServer.get(new HttpRunnable() {

			@Override
			public void run(Response response) throws AppException {
				mRefreshListServer.resolve(response);
				mRefreshListServer.getHandlerContext().getHandler().sendEmptyMessage(HANDLER);
			}

		}, false);
	}
	
}