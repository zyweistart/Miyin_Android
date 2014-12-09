package com.start.zmcy;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.start.core.BaseActivity;
import com.start.core.Config;

/**
 * 资源
 */
public class ResourceActivity extends BaseActivity{
	
	private Button main_head_1;
	private Button main_head_2;
	
	private WebView mWebView1;
	private WebView mWebView2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resource);
		setMainHeadTitle(getString(R.string.resources));
		
		main_head_1=(Button)findViewById(R.id.head_1);
		main_head_1.setText(getString(R.string.product_normal));
		main_head_1.setVisibility(View.VISIBLE);
		main_head_2=(Button)findViewById(R.id.head_2);
		main_head_2.setText(getString(R.string.product_led));
		main_head_2.setVisibility(View.VISIBLE);
		
		mWebView1 = (WebView) findViewById(R.id.wvcontent1);
		mWebView1.getSettings().setJavaScriptEnabled(true);
		mWebView2 = (WebView) findViewById(R.id.wvcontent2);
		mWebView2.getSettings().setJavaScriptEnabled(true);

		mWebView1.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				view.loadUrl(url);
				return true;
			}
		});

		mWebView2.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				view.loadUrl(url);
				return true;
			}
		});

		setHeadButtonEnabled(0);
		
		mWebView1.loadUrl(Config.NLIGHTSOURCEURL);
		mWebView2.loadUrl(Config.LIGHTSOURCEURL);
		
 	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.head_1){
			setHeadButtonEnabled(0);
		}else if(v.getId()==R.id.head_2){
			setHeadButtonEnabled(1);
		}else{
			super.onClick(v);
		}
	}
	
	public void setHeadButtonEnabled(int index){
		main_head_1.setEnabled(index==0?false:true);
		mWebView1.setVisibility(index==0?View.VISIBLE:View.GONE);
		main_head_2.setEnabled(index==1?false:true);
		mWebView2.setVisibility(index==1?View.VISIBLE:View.GONE);
	}
}
