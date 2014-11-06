package com.ancun.yzb;

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
import android.widget.Button;
import android.widget.TextView;

import com.ancun.core.BaseActivity;
import com.ancun.core.Constant;
import com.ancun.service.User;
import com.ancun.yzb.adapter.UseRecordAdapter;

public class AccountActivity extends BaseActivity implements RefreshListServerListener {
	
	public static final int REQUESTCODEMyAccountActivity = 0;
	public static final int RESULTREFRESHCODEMyAccountActivity = 1;

	private Button activity_myaccount_btn_RightTitle;

	private UseRecordAdapter mUseRecordAdapter;
	private RefreshListServer mRefreshListServer;
	
	private XListView mListView;
	private TextView activity_myaccount_phone;
	private TextView activity_myaccount_recordingcount;
	private TextView activity_myaccount_timelong;
	private TextView activity_myaccount_storageinfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		setMainHeadTitle(getString(R.string.myaccount));
		// 刷新用户信息
//		getAppService().refreshUserInfo();
		// 账户充值按钮
		activity_myaccount_btn_RightTitle = (Button) findViewById(R.id.btn_right_unsubscribe);
		activity_myaccount_btn_RightTitle.setOnClickListener(this);
		activity_myaccount_btn_RightTitle.setVisibility(View.VISIBLE);
		
		String userTel=getAppContext().currentUser().getInfo().get("userTel");
		String recordNumber=getAppContext().currentUser().getInfo().get("recordNumber");
		String usedingStore=getAppContext().currentUser().getInfo().get("usedingStore");
		String recordTime=getAppContext().currentUser().getInfo().get("recordTime");
		
		// 当前账户
		activity_myaccount_phone = (TextView) findViewById(R.id.activity_myaccount_phone);
		activity_myaccount_phone.setText("当前账户："+ userTel);
		activity_myaccount_recordingcount = (TextView) findViewById(R.id.activity_myaccount_recordingcount);
		activity_myaccount_recordingcount.setText("录音数量："+recordNumber);
		activity_myaccount_timelong = (TextView) findViewById(R.id.activity_myaccount_timelong);
		activity_myaccount_timelong.setText("已用空间："+usedingStore);
		activity_myaccount_storageinfo = (TextView) findViewById(R.id.activity_myaccount_storageinfo);
		activity_myaccount_storageinfo.setText("已用时长："+recordTime);
		
		mListView = (XListView) findViewById(R.id.xlv_listview);
		
		mUseRecordAdapter=new UseRecordAdapter(this);
		mRefreshListServer = new RefreshListServer(this, mListView,mUseRecordAdapter);
		mRefreshListServer.setCacheTag(TAG);
		mRefreshListServer.setListTag("reclist");
		mRefreshListServer.setInfoTag("recinfo");
		mRefreshListServer.setRefreshListServerListener(this);

		mRefreshListServer.initLoad();

	}

	@Override
	public void onLoading(final int HANDLER) {
		HttpServer hServer = new HttpServer(Constant.URL.ylcnrecQry,mRefreshListServer.getHandlerContext());
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("sign", User.ACCESSKEY);
		hServer.setHeaders(headers);
		Map<String, String> params = new HashMap<String, String>();
		params.put("accessid",User.ACCESSID);
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
	
	@Override
	public void onClick(View v) {
		if (activity_myaccount_btn_RightTitle == v) {
			startActivity(new Intent(this,UnsubscribeActivity.class));
		}else{
			super.onClick(v);
		}
	}

}
