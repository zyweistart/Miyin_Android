package com.start.medical.registered.station;

import java.util.HashMap;
import java.util.Map;

import start.core.AppException;
import start.service.HttpServer;
import start.service.ListServer;
import start.service.ListServer.RefreshListServerListener;
import start.service.Response;
import start.service.UIRunnable;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.start.core.BaseActivity;
import com.start.core.Constant;
import com.start.medical.R;
import com.start.medical.registered.outpatient.OutpatientRegistrationDetailActivity;
import com.start.service.User;

/**
 * 叫号查询
 * @author start
 *
 */
public class StationToStationQueryActivity extends BaseActivity implements RefreshListServerListener {

	private ListView mListView;
	private ListServer mListServer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_station_to_station_query);
		setMainHeadTitle("叫号查询");
		mListView = (ListView) findViewById(R.id.xlv_listview);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				Map<String, String> data = mListServer.getBaseListAdapter().getItem(position);
				Bundle bundle = new Bundle();
				bundle.putString(OutpatientRegistrationDetailActivity.RECORDNO,data.get(OutpatientRegistrationDetailActivity.RECORDNO));
				Intent intent = new Intent(getAppContext(),OutpatientRegistrationDetailActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		mListServer = new ListServer(this, mListView,new StationToStationQueryAdapter(this));
		mListServer.setCacheTag(TAG);
		mListServer.setListTag("reglist");
		mListServer.setInfoTag("reginfo");
		mListServer.setRefreshListServerListener(this);

		mListServer.initLoad();
	}

	@Override
	public void onLoading(final int HANDLER) {
		HttpServer hServer = new HttpServer(Constant.URL.hisregQuery,mListServer.getHandlerContext());
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("sign", User.ACCESSKEY);
		hServer.setHeaders(headers);
		Map<String, String> params = new HashMap<String, String>();
		params.put("accessid", User.ACCESSID);
		hServer.setParams(params);
		hServer.get(new UIRunnable() {

			@Override
			public void run(Response response) throws AppException {
				mListServer.resolve(response);
				mListServer.getHandlerContext().getHandler().sendEmptyMessage(HANDLER);
			}

		}, true);
	}

}

