package com.ancun.unicom;

import start.core.AppConstant;
import android.os.Bundle;
import android.webkit.WebView;

import com.ancun.core.BaseActivity;
import com.ancun.core.Constant;

public class WebPageActivity extends BaseActivity {
	
	public static final String TYPE="TYPE";
	
	public static final int TYPE_TIP=1;
	public static final int TYPE_SERVICE=2;
	
	private static final String WEBURL_TIP = "file:///android_asset/"+Constant.APPTYPE+"/tip.html";
	private static final String WEBURL_SERVICE = "file:///android_asset/"+Constant.APPTYPE+"/service.html";

	private WebView mWebView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_page);
		mWebView=(WebView)findViewById(R.id.wvcontent);
		mWebView.getSettings().setDefaultTextEncodingName(AppConstant.ENCODE);
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			int type=bundle.getInt(TYPE);
			if(type==TYPE_TIP){
				setMainHeadTitle(getString(R.string.tip));
				mWebView.loadUrl(WEBURL_TIP);
				return;
			}else if(type==TYPE_SERVICE){
				setMainHeadTitle(getString(R.string.serverclause));
				mWebView.loadUrl(WEBURL_SERVICE);
				return;
			}
		}
		finish();
	}
}