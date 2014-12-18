package com.start.zmcy;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import start.core.AppException;
import android.os.Bundle;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.core.Constant;
import com.start.service.HttpRunnable;
import com.start.service.HttpServer;
import com.start.service.Response;

/**
 * 问题详细
 */
public class ExpertsQuestionDetailActivity extends BaseActivity{
	
	public static final String QUESTIONID="QUESTIONID";
	public static final String CATEGORYID="CATEGORYID";
	
	private TextView question_title,question_description;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_detail);
		setMainHeadTitle(getString(R.string.experts_answer));
		question_title=(TextView)findViewById(R.id.question_title);
		question_description=(TextView)findViewById(R.id.question_description);
		
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			String id=bundle.getString(QUESTIONID);
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
						final String title=jo.getString("question");
						final String description=jo.getString("content");
						runOnUiThread(new Runnable() {
							public void run() {
								question_title.setText(title);
								question_description.setText(description);
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
}
