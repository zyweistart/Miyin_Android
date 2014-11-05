package com.ancun.yzb;

import start.core.AppConstant;
import android.os.Bundle;
import android.webkit.WebView;

import com.ancun.core.BaseActivity;

public class TipActivity extends BaseActivity {
	
	private static final String WEBURL = "file:///android_asset/html/tip.html";

	private WebView mWebView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tip);
		setMainHeadTitle(getString(R.string.tip));
		mWebView=(WebView)findViewById(R.id.wvcontent);
		mWebView.getSettings().setDefaultTextEncodingName(AppConstant.ENCODE);
		mWebView.loadUrl(WEBURL);
	}
}