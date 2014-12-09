package com.start.zmcy;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.start.core.BaseActivity;
import com.start.core.Config;

/**
 * 应用 
 */
public class AppActivity extends BaseActivity{
	
	private WebView mWebView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app);
		setMainHeadTitle(getString(R.string.app));
		
		mWebView = (WebView) findViewById(R.id.wvcontent);
		mWebView.getSettings().setJavaScriptEnabled(true);

		mWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				view.loadUrl(url);
				return true;
			}
		});
		mWebView.loadUrl(Config.APPURL);
 	}
}
