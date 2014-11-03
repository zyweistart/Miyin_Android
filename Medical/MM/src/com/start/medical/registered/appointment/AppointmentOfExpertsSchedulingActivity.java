package com.start.medical.registered.appointment;

import java.util.HashMap;
import java.util.Map;

import start.core.AppConstant;
import start.core.AppException;
import start.service.HttpRunnable;
import start.service.HttpServer;
import start.service.RefreshListServer;
import start.service.RefreshListServer.RefreshListServerListener;
import start.service.Response;
import start.widget.xlistview.XListView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.start.core.BaseActivity;
import com.start.core.Constant;
import com.start.medical.R;
import com.start.medical.department.DoctorsActivity;
import com.start.medical.registered.outpatient.OutpatientRegistrationAdapter;
import com.start.service.User;

/**
 * 排班列表
 * @author start
 *
 */
public class AppointmentOfExpertsSchedulingActivity extends BaseActivity implements RefreshListServerListener {

	public static final String RECORDNO="recordno";
	
	private XListView mListView;
	private RefreshListServer mRefreshListServer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_outpatient_registration);
		setMainHeadTitle("排班列表");
		mListView = (XListView) findViewById(R.id.xlv_listview);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				if (id >= 0) {
					Map<String, String> data = mRefreshListServer.getBaseListAdapter().getItem(position - 1);
					Bundle bundle = new Bundle();
					bundle.putString(DoctorsActivity.RECORDNO,data.get(DoctorsActivity.RECORDNO));
					Intent intent = new Intent(getAppContext(),DoctorsActivity.class);
					intent.putExtras(bundle);
					startActivity(intent);
				} else {
					mRefreshListServer.getCurrentListView().startLoadMore();
				}
			}
		});
		mRefreshListServer = new RefreshListServer(this, mListView,new OutpatientRegistrationAdapter(this));
		mRefreshListServer.setCacheTag(TAG);
		mRefreshListServer.setListTag("deptlist");
		mRefreshListServer.setInfoTag("deptinfo");
		mRefreshListServer.setRefreshListServerListener(this);

		mRefreshListServer.initLoad();
	}

	@Override
	public void onLoading(final int HANDLER) {
		HttpServer hServer = new HttpServer(Constant.URL.hispbxxzjyspbList,mRefreshListServer.getHandlerContext());
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("sign", User.ACCESSKEY);
		hServer.setHeaders(headers);
		Map<String, String> params = new HashMap<String, String>();
		params.put("accessid", User.ACCESSID);
		params.put("currentpage",String.valueOf(mRefreshListServer.getCurrentPage() + 1));
		params.put("pagesize", String.valueOf(AppConstant.PAGESIZE));
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
