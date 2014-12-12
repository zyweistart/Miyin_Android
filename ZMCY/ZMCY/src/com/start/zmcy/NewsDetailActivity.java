package com.start.zmcy;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import start.core.AppException;
import android.os.Bundle;
import android.view.View;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_detail);
		setMainHeadTitle(getString(R.string.newsdetail));
		mHeadMore=(Button)findViewById(R.id.head_more);
		mHeadMore.setVisibility(View.VISIBLE);
		mHeadChildTitle=(TextView)findViewById(R.id.head_child_title);
		mHeadChildTitle.setVisibility(View.VISIBLE);
		
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			String id=bundle.getString(NEWSID);
			String categoryid=bundle.getString(CATEGORYID);
			
			HttpServer hServer = new HttpServer(Constant.URL.GetInfo,getHandlerContext());
			Map<String, String> params = new HashMap<String, String>();
			params.put("Id", id);
			params.put("classId",categoryid);
			hServer.setParams(params);
			hServer.get(new HttpRunnable() {

				@Override
				public void run(Response response) throws AppException {
					try {
						JSONArray jsonArray=(JSONArray)response.getData("Table");
						JSONObject jo=jsonArray.getJSONObject(0);
						shareContent="友盟社会化组件（SDK）让移动应用快速整合社交分享功能，http://www.umeng.com/social";
						shareImageUrl="http://www.umeng.com/images/pic/banner_module_social.png";
						final String evaluation=jo.getString("hit");
						runOnUiThread(new Runnable() {
							public void run() {
								mHeadChildTitle.setText(evaluation+"评");
							}
						});
					} catch (JSONException e) {
						throw AppException.json(e);
					}
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