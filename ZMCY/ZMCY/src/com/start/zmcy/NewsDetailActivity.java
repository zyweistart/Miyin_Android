package com.start.zmcy;

import java.util.HashMap;
import java.util.Map;

import start.core.AppContext;
import start.core.AppException;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.core.Constant;
import com.start.service.HttpRunnable;
import com.start.service.HttpServer;
import com.start.service.Response;
import com.start.service.SocialService;
import com.umeng.socialize.controller.UMSocialService;

/**
 * 新闻详细页
 */
public class NewsDetailActivity extends BaseActivity{
	
	private TextView mHeadChildTitle;
	private Button mHeadMore;
	
	public static final String NEWSID="NEWSID";
	public static final String CATEGORYID="CATEGORYID";
	
	private String shareContent;
	private String shareImageUrl;
	
	private WebView mWebView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_detail);
		setMainHeadTitle(getString(R.string.newsdetail));
		mHeadMore=(Button)findViewById(R.id.head_more);
		mHeadMore.setVisibility(View.VISIBLE);
		mHeadChildTitle=(TextView)findViewById(R.id.head_child_title);
		mHeadChildTitle.setVisibility(View.VISIBLE);
		
		mWebView = (WebView) findViewById(R.id.wvcontent);
		mWebView.getSettings().setJavaScriptEnabled(true);

		mWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				view.loadUrl(url);
				return true;
			}
		});
		
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			final String id=bundle.getString(NEWSID);
			final String categoryid=bundle.getString(CATEGORYID);
			
			HttpServer hServer = new HttpServer(Constant.URL.GetInfo,getHandlerContext());
			Map<String, String> params = new HashMap<String, String>();
			params.put("Id", id);
			params.put("classId",categoryid);
			hServer.setParams(params);
			hServer.get(new HttpRunnable() {

				@Override
				public void run(Response response) throws AppException {
					shareContent=String.valueOf(response.getData("ShareContent"));
					shareImageUrl=AppContext.getInstance().getServerURL()+String.valueOf(response.getData("images"));
					final String evaluation=String.valueOf(response.getData("PLNum"));
					runOnUiThread(new Runnable() {
						public void run() {
							mHeadChildTitle.setText(evaluation+"评");
							String url=getAppContext().getServerURL()+"/"+categoryid+"/"+id+"/ios_news_ct.html";
							mWebView.loadUrl(url);
						}
					});
				}
				
			});
			
		}else{
			finish();
		}
 	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.head_more){
			// 首先在您的Activity中添加如下成员变量
			UMSocialService mController = SocialService.socialShare(this, shareContent,shareImageUrl);
			 // 是否只有已登录用户才能打开分享选择页
	        mController.openShare(this, false);
		}else{
			super.onClick(v);
		}
	}
	
}