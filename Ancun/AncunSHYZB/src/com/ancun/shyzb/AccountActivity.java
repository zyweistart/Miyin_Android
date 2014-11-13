package com.ancun.shyzb;

import java.util.HashMap;
import java.util.Map;

import start.core.AppConstant;
import start.core.AppException;
import start.service.HttpRunnable;
import start.service.HttpServer;
import start.service.RefreshListServer;
import start.service.RefreshListServer.RefreshListServerListener;
import start.service.Response;
import start.utils.StringUtils;
import start.utils.TimeUtils;
import start.widget.xlistview.XListView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ancun.core.BaseActivity;
import com.ancun.core.Constant;
import com.ancun.service.User;
import com.ancun.shyzb.adapter.UseRecordAdapter;

public class AccountActivity extends BaseActivity implements RefreshListServerListener {
	
	private Button btnUnsubscribe;

	private TextView txtPhone;
	private TextView txtRecordingCount;
	private TextView txtTimeLong;
	private TextView txtStorageInfo;
	
	private XListView mListView;
	private RefreshListServer mRefreshListServer;
	private UseRecordAdapter mUseRecordAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		setMainHeadTitle(getString(R.string.myaccount));
		
		//TODO:退订
		btnUnsubscribe = (Button) findViewById(R.id.btn_right_unsubscribe);
		btnUnsubscribe.setOnClickListener(this);
//		activity_myaccount_btn_RightTitle.setVisibility(View.VISIBLE);
		
		txtPhone = (TextView) findViewById(R.id.activity_myaccount_phone);
		txtRecordingCount = (TextView) findViewById(R.id.activity_myaccount_recordingcount);
		txtTimeLong = (TextView) findViewById(R.id.activity_myaccount_timelong);
		txtStorageInfo = (TextView) findViewById(R.id.activity_myaccount_storageinfo);
		
		mListView = (XListView) findViewById(R.id.xlv_listview);
		
		mUseRecordAdapter=new UseRecordAdapter(this);
		mRefreshListServer = new RefreshListServer(this, mListView,mUseRecordAdapter);
		mRefreshListServer.setCacheTag(getAppContext().currentUser().getPhone()+TAG);
		mRefreshListServer.setListTag("reclist");
		mRefreshListServer.setInfoTag("recinfo");
		mRefreshListServer.setRefreshListServerListener(this);

		showhUserInfo();
		mRefreshListServer.initLoad();
		refreshUserInfo();
		
	}

	@Override
	public void onLoading(final int HANDLER) {
		HttpServer hServer = new HttpServer(Constant.URL.ylcnrecQry,mRefreshListServer.getHandlerContext());
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("sign", User.ACCESSKEY);
		hServer.setHeaders(headers);
		Map<String, String> params = new HashMap<String, String>();
		params.put("accessid",User.ACCESSID);
		params.put("ownerno",getAppContext().currentUser().getPhone());
		params.put("ordersort","desc");
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
		if (btnUnsubscribe == v) {
			startActivity(new Intent(this,UnsubscribeActivity.class));
		}else{
			super.onClick(v);
		}
	}
	
	/**
	 * 刷新用户信息
	 */
	public void refreshUserInfo(){
		HttpServer hServer=new HttpServer(Constant.URL.userinfoGet, getHandlerContext());
		Map<String,String> headers=new HashMap<String,String>();
		headers.put("sign", User.ACCESSKEY);
		hServer.setHeaders(headers);
		Map<String,String> params=new HashMap<String,String>();
		params.put("accessid", User.ACCESSID);
		params.put("userTel", getAppContext().currentUser().getPhone());
		hServer.setParams(params);
		hServer.get(new HttpRunnable() {
			
			@Override
			public void run(Response response) throws AppException {
				getAppContext().currentUser().resolve(response.getMapData("userinfo"));
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						showhUserInfo();
					}
				});
				
			}
			
		});
	}
	
	public void showhUserInfo(){
		String recordNumber=getAppContext().currentUser().getInfo().get("recordNumber");
		String usedingStore=getAppContext().currentUser().getInfo().get("usedingStore");
		String recordTime=getAppContext().currentUser().getInfo().get("recordTime");
		txtPhone.setText("当前账户："+ getAppContext().currentUser().getPhone());
		txtRecordingCount.setText("录音数量："+recordNumber+"个");
		txtTimeLong.setText("已用空间："+usedingStore);
		txtStorageInfo.setText("已用时长："+TimeUtils.secondConvertTime(StringUtils.toInt(recordTime, 0)));
	}

}
