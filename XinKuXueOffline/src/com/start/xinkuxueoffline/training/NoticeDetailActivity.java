package com.start.xinkuxueoffline.training;

import java.util.HashMap;
import java.util.Map;

import start.core.AppException;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.core.Constant;
import com.start.service.HttpRunnable;
import com.start.service.HttpServer;
import com.start.service.Response;
import com.start.xinkuxueoffline.R;

/**
 * 公告详细
 * @author zhenyao
 *
 */
public class NoticeDetailActivity extends BaseActivity {

	private TextView tvTitle;
	
	public static final String NEWSID="NEWSID";
	public static final String CATEGORYID="CATEGORYID";
	
	private WebView mWebView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice_detail);
		tvTitle=(TextView)findViewById(R.id.tvTitle);
		tvTitle.setText("公告");
		
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
					runOnUiThread(new Runnable() {
						public void run() {
							String url=getAppContext().getServerURL()+"/"+categoryid+"/"+id+"/newsCont.html";
							mWebView.loadUrl(url);
						}
					});
				}
				
			});
			
		}else{
			finish();
		}
 	}
}
