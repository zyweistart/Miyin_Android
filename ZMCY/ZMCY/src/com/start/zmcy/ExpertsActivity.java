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
import com.start.service.User;
import com.start.zmcy.adapter.ExpertsListAdapter;
import com.start.zmcy.adapter.ExpertsListAdapter.ExpertsHolderView;
import com.start.zmcy.adapter.ExpertsQuestionAdapter;
import com.start.zmcy.adapter.ExpertsQuestionAdapter.QuestionHolderView;

/**
 * 专家
 */
public class ExpertsActivity extends BaseActivity implements
		RefreshListServerListener {

	public static final int REQUEST_LOGIN_CODE=111;
	
	private Button main_head_1;
	private Button main_head_2;
	private Button main_head_3;
	private Button main_head_4;

	private XListView mExpertsListView;
	private RefreshListServer mExpertsRefreshListServer;

	private XListView mQuestionListView;
	private RefreshListServer mQuestionRefreshListServer;

	private WebView mWebView1,mWebView2;

	private int type = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_experts);

		main_head_1 = (Button) findViewById(R.id.head_1);
		main_head_1.setText(getString(R.string.experts_list));
		main_head_1.setVisibility(View.VISIBLE);
		main_head_2 = (Button) findViewById(R.id.head_2);
		main_head_2.setText(getString(R.string.experts_answer));
		main_head_2.setVisibility(View.VISIBLE);
		main_head_3 = (Button) findViewById(R.id.head_3);
		main_head_3.setText(getString(R.string.experts_ableperson));
		main_head_3.setVisibility(View.VISIBLE);
		main_head_4 = (Button) findViewById(R.id.head_4);
		main_head_4.setText(getString(R.string.experts_cover));
		main_head_4.setVisibility(View.VISIBLE);

		mExpertsListView = (XListView) findViewById(R.id.xlv_listview);
		mExpertsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
//				if(id>0){
					ExpertsHolderView hv=(ExpertsHolderView)view.getTag();
					Bundle bundle=new Bundle();
					bundle.putString(ExpertsDetailActivity.EXPERTSID, hv.id);
					bundle.putString(ExpertsDetailActivity.EXPERTSCATEGORYID, hv.categoryid);
					bundle.putString(ExpertsDetailActivity.EXPERTSIMAGE, hv.head);
					bundle.putString(ExpertsDetailActivity.EXPERTSNAME, hv.name);
					bundle.putString(ExpertsDetailActivity.EXPERTSPRO, hv.pro);
					bundle.putString(ExpertsDetailActivity.EXPERTSDESCRIPTION, hv.description);
					Intent intent=new Intent(ExpertsActivity.this,ExpertsDetailActivity.class);
					intent.putExtras(bundle);
					startActivity(intent);
//				}else{
//					mExpertsRefreshListServer.getCurrentListView().startLoadMore();
//				}
			}
		});
		mExpertsRefreshListServer = new RefreshListServer(this, getHandlerContext(),
				mExpertsListView, new ExpertsListAdapter(this));
		mExpertsRefreshListServer.setCacheTag(TAG+"ExpertsList");
		mExpertsRefreshListServer.setRefreshListServerListener(this);

		mQuestionListView = (XListView) findViewById(R.id.xlv_listview_question);
		mQuestionListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
//				if(id>0){
					QuestionHolderView hv=(QuestionHolderView)view.getTag();
					Bundle bundle=new Bundle();
					bundle.putString(ExpertsQuestionDetailActivity.QUESTIONID, hv.question_id);
					bundle.putString(ExpertsQuestionDetailActivity.CATEGORYID, hv.question_categoryid);
					Intent intent=new Intent(ExpertsActivity.this,ExpertsQuestionDetailActivity.class);
					intent.putExtras(bundle);
					startActivity(intent);
//				}else{
//					mQuestionRefreshListServer.getCurrentListView().startLoadMore();
//				}
			}
		});
		mQuestionRefreshListServer = new RefreshListServer(this,
				getHandlerContext(), mQuestionListView,
				new ExpertsQuestionAdapter(this));
		mQuestionRefreshListServer.setCacheTag(TAG+"Question");
		mQuestionRefreshListServer.setRefreshListServerListener(this);

		mWebView1 = (WebView) findViewById(R.id.wvcontent1);
		mWebView1.getSettings().setJavaScriptEnabled(true);

		mWebView1.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				view.loadUrl(url);
				return true;
			}
		});
		
		mWebView2 = (WebView) findViewById(R.id.wvcontent2);
		mWebView2.getSettings().setJavaScriptEnabled(true);

		mWebView2.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				view.loadUrl(url);
				return true;
			}
		});
		
		setHeadButtonEnabled(type);
		mExpertsRefreshListServer.initLoad();
		if(getAppContext().currentUser().isLogin()){
			mQuestionRefreshListServer.initLoad();
		}
		mWebView1.loadUrl(Config.EXPERTSURL1);
		mWebView2.loadUrl(Config.EXPERTSURL);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.head_1) {
			// 专家
			type = 1;
			setHeadButtonEnabled(type);
		} else if (v.getId() == R.id.head_2) {
			// 回答 
			if(getAppContext().currentUser().isLogin()){
				type = 2;
				setHeadButtonEnabled(type);
			}else{
				goLoginResult(REQUEST_LOGIN_CODE,getString(R.string.nologin));
			}
		} else if (v.getId() == R.id.head_3) {
			// 能人
			type=3;
			setHeadButtonEnabled(type);
		} else if (v.getId() == R.id.head_4) {
			// 自荐
			type=4;
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
		main_head_4.setEnabled(index == 4 ? false : true);
		mExpertsListView.setVisibility(!main_head_1.isEnabled() ?View.VISIBLE:View.GONE);
		mQuestionListView.setVisibility(!main_head_2.isEnabled() ?View.VISIBLE:View.GONE);
		mWebView1.setVisibility(!main_head_3.isEnabled()?View.VISIBLE:View.GONE);
		mWebView2.setVisibility(!main_head_4.isEnabled()?View.VISIBLE:View.GONE);
	}

	public RefreshListServer getRefreshListServer() {
		if(type==1){
			return mExpertsRefreshListServer;
		}else if(type==2){
			return mQuestionRefreshListServer;
		}else{
			return null;
		}
	}

	@Override
	public void onLoading(final int HANDLER) {
		HttpServer hServer = new HttpServer(Constant.URL.GetListALL,getRefreshListServer().getHandlerContext());
		Map<String, String> params = new HashMap<String, String>();
		params.put("Id", getCategoryId());
		if(type==2&&getAppContext().currentUser().isLogin()){
			params.put("access_token", User.ACCESSKEY);
		}
		params.put("index",String.valueOf(mExpertsRefreshListServer.getCurrentPage() + 1));
//		params.put("size", String.valueOf(AppConstant.PAGESIZE));
		hServer.setParams(params);
		hServer.get(new HttpRunnable() {

			@Override
			public void run(Response response) throws AppException {
				getRefreshListServer().resolve(response);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						getRefreshListServer().getHandlerContext().getHandler().sendEmptyMessage(HANDLER);
					}
				});
			}

		}, false);
	}

	public String getCategoryId(){
		return type==1?"9":"10";
	}
	
}