package com.start.medical.personal.appointment;

import java.util.HashMap;
import java.util.Map;

import start.core.AppConstant;
import start.core.AppException;
import start.service.HttpServer;
import start.service.RefreshListServer;
import start.service.RefreshListServer.RefreshListServerListener;
import start.service.Response;
import start.service.UIRunnable;
import start.widget.xlistview.XListView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.start.core.BaseActivity;
import com.start.core.Constant;
import com.start.medical.R;
import com.start.service.User;

/**
 * 我的预约
 * @author start
 *
 */
public class MyAppointmentActivity extends BaseActivity implements RefreshListServerListener {
	
	private XListView mListView;
	private RefreshListServer mRefreshListServer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_listview);
		setMainHeadTitle("我的预约");
		mListView = (XListView) findViewById(R.id.xlv_listview);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				if(id>=0){
					Map<String,String> data=mRefreshListServer.getBaseListAdapter().getItem(position-1);
					Bundle bundle=new Bundle();
					bundle.putString(MyAppointmentDetailActivity.RECORDNO, data.get(MyAppointmentDetailActivity.RECORDNO));
					Intent intent=new Intent(getAppContext(),MyAppointmentDetailActivity.class);
					intent.putExtras(bundle);
					startActivity(intent);
				}else{
					mRefreshListServer.getCurrentListView().startLoadMore();
				}
			}
		});
		mRefreshListServer = new RefreshListServer(this, mListView,new MyAppointmentAdapter(this));
		mRefreshListServer.setCacheTag(TAG);
		mRefreshListServer.setListTag("newslist");
		mRefreshListServer.setInfoTag("newsinfo");
		mRefreshListServer.setRefreshListServerListener(this);

		mRefreshListServer.initLoad();
	}

	@Override
	public void onLoading(final int HANDLER) {
		HttpServer hServer = new HttpServer(Constant.URL.htinfonewsQuery,mRefreshListServer.getHandlerContext());
		Map<String,String> headers=new HashMap<String,String>();
		headers.put("sign", User.COMMON_ACCESSKEY_LOCAL);
		hServer.setHeaders(headers);
		Map<String, String> params = new HashMap<String, String>();
		params.put("accessid", User.COMMON_ACCESSID_LOCAL);
		params.put("currentpage",String.valueOf(mRefreshListServer.getCurrentPage() + 1));
		params.put("pagesize", String.valueOf(AppConstant.PAGESIZE));
		params.put("type", AppConstant.EMPTYSTR);
		params.put("title", AppConstant.EMPTYSTR);
		params.put("content", AppConstant.EMPTYSTR);
		params.put("ordersort", AppConstant.EMPTYSTR);
		hServer.setParams(params);
		hServer.get(new UIRunnable() {

			@Override
			public void run(Response response) throws AppException {
				mRefreshListServer.resolve(response);
				mRefreshListServer.getHandlerContext().getHandler().sendEmptyMessage(HANDLER);
			}

		}, false);
	}
	
}
