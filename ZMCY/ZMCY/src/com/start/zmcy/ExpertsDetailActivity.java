package com.start.zmcy;

import java.util.HashMap;
import java.util.Map;

import start.core.AppContext;
import start.core.AppException;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.core.Constant;
import com.start.service.BitmapManager;
import com.start.service.HttpRunnable;
import com.start.service.HttpServer;
import com.start.service.Response;

/**
 * 专家详细页
 */
public class ExpertsDetailActivity extends BaseActivity{
	
	public static final String EXPERTSID="EXPERTSID";
	public static final String EXPERTSCATEGORYID="EXPERTSCATEGORYID";
	public static final String EXPERTSIMAGE="EXPERTSIMAGE";
	public static final String EXPERTSNAME="EXPERTSNAME";
	public static final String EXPERTSPRO="EXPERTSPRO";
	public static final String EXPERTSDESCRIPTION="EXPERTSDESCRIPTION";
	
	private ImageView experts_head;
	private TextView experts_name,experts_pro,experts_description;
	private Button experts_consultation;
	
	private String expertsId,expertsCategoryId,expertsImage,expertsName,expertsPro,expertsDescription;
	
	private static BitmapManager mExpertsBitmapManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_experts_detail);
		setMainHeadTitle(getString(R.string.experts_list));
		
		experts_head=(ImageView)findViewById(R.id.experts_head);
		experts_name=(TextView)findViewById(R.id.experts_name);
		experts_pro=(TextView)findViewById(R.id.experts_pro);
		experts_description=(TextView)findViewById(R.id.experts_description);
		experts_consultation=(Button)findViewById(R.id.experts_consultation);
		experts_consultation.setOnClickListener(this);
		
		if(mExpertsBitmapManager==null){
			mExpertsBitmapManager = new BitmapManager(BitmapFactory.decodeResource(getResources(), R.drawable.default_experts));
		}
		
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			expertsId=bundle.getString(EXPERTSID);
			expertsCategoryId=bundle.getString(EXPERTSCATEGORYID);
			expertsImage=bundle.getString(EXPERTSIMAGE);
			expertsName=bundle.getString(EXPERTSNAME);
			expertsPro=bundle.getString(EXPERTSPRO);
			expertsDescription=bundle.getString(EXPERTSDESCRIPTION);
			
			if(!TextUtils.isEmpty(expertsImage)){
				mExpertsBitmapManager.loadBitmap(expertsImage, experts_head);
			}
			experts_name.setText(expertsName);
			experts_pro.setText(expertsPro);
			
			HttpServer hServer = new HttpServer(Constant.URL.GetInfo,getHandlerContext());
			Map<String, String> params = new HashMap<String, String>();
			params.put("Id", expertsId);
			params.put("classId",expertsCategoryId);
			hServer.setParams(params);
			hServer.get(new HttpRunnable() {

				@Override
				public void run(Response response) throws AppException {
					final String description=String.valueOf(response.getData("description"));
					runOnUiThread(new Runnable() {
						public void run() {
							experts_description.setText(description);
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
		if(v.getId()==R.id.head_back){
			finish();
		}else if(v.getId()==R.id.experts_consultation){
			Bundle bundle=new Bundle();
			bundle.putString(ConsultationActivity.CONSOLTATIONID, expertsId);
			Intent intent=new Intent(this,ConsultationActivity.class);
			intent.putExtras(bundle);
			if(!getAppContext().currentUser().isLogin()){
				goLogin(intent,getString(R.string.nologin));
				return;
			}
			startActivity(intent);
		}
	}
}