package com.start.zmcy;

import java.util.HashMap;
import java.util.Map;

import start.core.AppConstant;
import start.core.AppException;
import start.widget.xlistview.XListView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.start.core.BaseActivity;
import com.start.service.HttpRunnable;
import com.start.service.HttpServer;
import com.start.service.RefreshListServer;
import com.start.service.RefreshListServer.RefreshListServerListener;
import com.start.service.Response;
import com.start.service.User;
import com.start.zmcy.adapter.NewsListAdapter;
import com.start.zmcy.adapter.NewsListAdapter.HolderView;

/**
 * 新闻列表
 */
public class NewsListActivity extends BaseActivity implements RefreshListServerListener{
	
	private XListView mListView;
	private RefreshListServer mRefreshListServer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_list);
		setMainHeadTitle(getString(R.string.news));
		
		mListView = (XListView)findViewById(R.id.xlv_listview);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				HolderView hv=(HolderView)view.getTag();
				NewsListActivity.gotoNews(NewsListActivity.this,hv.recordno);
			}
			
		});
		mRefreshListServer = new RefreshListServer(this,getHandlerContext(), mListView,new NewsListAdapter(this));
		mRefreshListServer.setCacheTag("NewsContentFragment");
		mRefreshListServer.setRefreshListServerListener(this);

		mRefreshListServer.initLoad();
 	}
	
	@Override
	public void onLoading(final int HANDLER) {
		HttpServer hServer = new HttpServer("htinfonewsQuery",mRefreshListServer.getHandlerContext());
		Map<String,String> headers=new HashMap<String,String>();
		headers.put("sign", User.USER_ACCESSKEY_LOCAL);
		hServer.setHeaders(headers);
		Map<String, String> params = new HashMap<String, String>();
		params.put("accessid", User.USER_ACCESSID_LOCAL);
		params.put("currentpage",String.valueOf(mRefreshListServer.getCurrentPage() + 1));
		params.put("pagesize", String.valueOf(AppConstant.PAGESIZE));
		params.put("type", AppConstant.EMPTYSTR);
		params.put("title", AppConstant.EMPTYSTR);
		params.put("content", AppConstant.EMPTYSTR);
		params.put("ordersort", AppConstant.EMPTYSTR);
		hServer.setParams(params);
		hServer.get(new HttpRunnable() {

			@Override
			public void run(Response response) throws AppException {
				mRefreshListServer.resolve(response);
				mRefreshListServer.getHandlerContext().getHandler().sendEmptyMessage(HANDLER);
			}

		}, false);
	}
    
	public static void gotoNews(Activity activity,String recordno){
		Bundle bundle=new Bundle();
		bundle.putString(NewsDetailActivity.NEWSID, recordno);
		Intent intent=new Intent(activity,NewsDetailActivity.class);
		intent.putExtras(bundle);
		activity.startActivity(intent);
	}
}
