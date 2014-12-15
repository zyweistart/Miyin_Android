package com.start.zmcy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.start.core.BaseActivity;

/**
 * 专家详细页
 */
public class ExpertsDetailActivity extends BaseActivity{
	
	private TextView mHeadChildTitle;
	private Button mHeadMore;
	
	public static final String EXPERTSID="EXPERTSID";
	public static final String CATEGORYID="CATEGORYID";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_experts_detail);
		setMainHeadTitle(getString(R.string.newsdetail));
		mHeadMore=(Button)findViewById(R.id.head_more);
		mHeadMore.setVisibility(View.VISIBLE);
		mHeadChildTitle=(TextView)findViewById(R.id.head_child_title);
		mHeadChildTitle.setVisibility(View.VISIBLE);
		
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			String id=bundle.getString(EXPERTSID);
			String categoryid=bundle.getString(CATEGORYID);
			
//			HttpServer hServer = new HttpServer(Constant.URL.GetInfo,getHandlerContext());
//			Map<String, String> params = new HashMap<String, String>();
//			params.put("Id", id);
//			params.put("classId",categoryid);
//			hServer.setParams(params);
//			hServer.get(new HttpRunnable() {
//
//				@Override
//				public void run(Response response) throws AppException {
//					try {
//						JSONArray jsonArray=(JSONArray)response.getData("Table");
//						JSONObject jo=jsonArray.getJSONObject(0);
//						shareContent="友盟社会化组件（SDK）让移动应用快速整合社交分享功能，http://www.umeng.com/social";
//						shareImageUrl="http://www.umeng.com/images/pic/banner_module_social.png";
//						final String evaluation=jo.getString("hit");
//						runOnUiThread(new Runnable() {
//							public void run() {
//								mHeadChildTitle.setText(evaluation+"评");
//							}
//						});
//					} catch (JSONException e) {
//						throw AppException.json(e);
//					}
//				}
//				
//			});
			
		}else{
			finish();
		}
 	}
	
}