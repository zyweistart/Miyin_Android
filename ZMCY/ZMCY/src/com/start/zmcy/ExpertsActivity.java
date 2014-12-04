package com.start.zmcy;

import java.util.HashMap;
import java.util.Map;

import start.core.AppException;
import start.widget.xlistview.XListView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.start.core.BaseActivity;
import com.start.core.Config;
import com.start.core.Constant;
import com.start.service.HttpRunnable;
import com.start.service.HttpServer;
import com.start.service.RefreshListServer;
import com.start.service.RefreshListServer.RefreshListServerListener;
import com.start.service.Response;
import com.start.zmcy.adapter.ExpertsListAdapter;
import com.start.zmcy.adapter.ExpertsQuestionAdapter;

/**
 * 专家
 */
public class ExpertsActivity extends BaseActivity implements
		RefreshListServerListener {

	public static final int REQUEST_LOGIN_CODE=111;
	
	private Button main_head_1;
	private Button main_head_2;
	private Button main_head_3;

	private XListView mListView;
	private RefreshListServer mRefreshListServer;

	private XListView mQuestionListView;
	private RefreshListServer mQuestionRefreshListServer;

	private WebView mWebView;

	private int type = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_experts);
		setMainHeadTitle(getString(R.string.experts));

		main_head_1 = (Button) findViewById(R.id.head_1);
		main_head_1.setText(getString(R.string.experts_list));
		main_head_1.setVisibility(View.VISIBLE);
		main_head_2 = (Button) findViewById(R.id.head_2);
		main_head_2.setText(getString(R.string.experts_answer));
		main_head_2.setVisibility(View.VISIBLE);
		main_head_3 = (Button) findViewById(R.id.head_3);
		main_head_3.setText(getString(R.string.experts_cover));
		main_head_3.setVisibility(View.VISIBLE);
		

		mListView = (XListView) findViewById(R.id.xlv_listview);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});
		mRefreshListServer = new RefreshListServer(this, getHandlerContext(),
				mListView, new ExpertsListAdapter(this));
		mRefreshListServer.setCacheTag(TAG);
		mRefreshListServer.setRefreshListServerListener(this);

		mQuestionListView = (XListView) findViewById(R.id.xlv_listview_question);
		mQuestionListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});
		mQuestionRefreshListServer = new RefreshListServer(this,
				getHandlerContext(), mQuestionListView,
				new ExpertsQuestionAdapter(this));
		mQuestionRefreshListServer.setCacheTag(TAG+"Question");
		mQuestionRefreshListServer.setRefreshListServerListener(this);

		mWebView = (WebView) findViewById(R.id.wvcontent);

		mWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				view.loadUrl(url);
				return true;
			}
		});
		
		setHeadButtonEnabled(type);
		mRefreshListServer.initLoad();
		if(getAppContext().currentUser().isLogin()){
			mQuestionRefreshListServer.initLoad();
		}
		mWebView.loadUrl(Config.EXPERTSURL);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.head_1) {
			// 专家列表
			type = 1;
			setHeadButtonEnabled(type);
		} else if (v.getId() == R.id.head_2) {
			// 专家解答
			if(getAppContext().currentUser().isLogin()){
				type = 2;
				setHeadButtonEnabled(type);
			}else{
				goLoginResult(REQUEST_LOGIN_CODE,getString(R.string.nologin));
			}
		} else if (v.getId() == R.id.head_3) {
			// 专家自荐
			type=3;
			setHeadButtonEnabled(type);
		} else {
			super.onClick(v);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==REQUEST_LOGIN_CODE){
			if(resultCode==LoginActivity.RESULT_LOGIN_SUCCESS){
				type=2;
				setHeadButtonEnabled(type);
				mQuestionRefreshListServer.initLoad();
			}
		}
	}

	public void setHeadButtonEnabled(int index) {
		main_head_1.setEnabled(index == 1 ? false : true);
		main_head_2.setEnabled(index == 2 ? false : true);
		main_head_3.setEnabled(index == 3 ? false : true);
		mListView.setVisibility(!main_head_1.isEnabled() ?View.VISIBLE:View.GONE);
		mQuestionListView.setVisibility(!main_head_2.isEnabled() ?View.VISIBLE:View.GONE);
		mWebView.setVisibility(!main_head_3.isEnabled()?View.VISIBLE:View.GONE);
	}

	public RefreshListServer getRefreshListServer() {
		if(type==1){
			return mRefreshListServer;
		}else if(type==2){
			return mQuestionRefreshListServer;
		}else{
			return null;
		}
	}

	@Override
	public void onLoading(final int HANDLER) {
		HttpServer hServer = new HttpServer(Constant.URL.GetListALL,mRefreshListServer.getHandlerContext());
		Map<String, String> params = new HashMap<String, String>();
		params.put("Id", type==1?"6":"8");
		params.put("index",String.valueOf(mRefreshListServer.getCurrentPage() + 1));
//		params.put("size", String.valueOf(AppConstant.PAGESIZE));
		hServer.setParams(params);
		hServer.get(new HttpRunnable() {

			@Override
			public void run(Response response) throws AppException {
				getRefreshListServer().resolve(response);
				getRefreshListServer().getHandlerContext().getHandler().sendEmptyMessage(HANDLER);
			}

		}, false);
	}

}