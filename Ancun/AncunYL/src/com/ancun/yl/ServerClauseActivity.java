package com.ancun.yl;

import start.core.AppConstant;
import android.os.Bundle;
import android.webkit.WebView;

import com.ancun.core.BaseActivity;
/**
 * 服务条款
 * @author Start
 */
public class ServerClauseActivity extends BaseActivity {
	
	private static final String WEBURL = "file:///android_asset/html/serverclause.html";

	private WebView mWebView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tip);
		setMainHeadTitle("服务条款");
		mWebView=(WebView)findViewById(R.id.wvcontent);
		mWebView.getSettings().setDefaultTextEncodingName(AppConstant.ENCODE);
		mWebView.loadUrl(WEBURL);
	}
}
