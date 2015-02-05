package com.start.xinkuxue.training;

import java.util.HashMap;
import java.util.Map;

import start.core.AppException;
import start.widget.xlistview.XListView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.core.Constant;
import com.start.service.HttpRunnable;
import com.start.service.HttpServer;
import com.start.service.RefreshListServer;
import com.start.service.RefreshListServer.RefreshListServerListener;
import com.start.service.Response;
import com.start.service.User;
import com.start.xinkuxue.R;
import com.start.xinkuxue.training.NoticeAdapter.HolderView;

/**
 * 公告栏
 * @author zhenyao
 *
 */
public class NoticeActivity extends BaseActivity implements RefreshListServerListener {

	private int type = 1;
	
	private boolean load2,load3;
	
	private TextView tvTitle,tvNotice1,tvNotice2,tvNotice3;
	private XListView xlv_listview_1,xlv_listview_2,xlv_listview_3;
	private RefreshListServer mRefreshListServer1,mRefreshListServer2,mRefreshListServer3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice);
		tvTitle=(TextView)findViewById(R.id.tvTitle);
		tvTitle.setText("公告栏");
		tvNotice1=(TextView)findViewById(R.id.tvnotice1);
		tvNotice2=(TextView)findViewById(R.id.tvnotice2);
		tvNotice3=(TextView)findViewById(R.id.tvnotice3);
		
		xlv_listview_1=(XListView)findViewById(R.id.xlv_listview_1);
		xlv_listview_1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				HolderView hv = (HolderView) view.getTag();
				NoticeActivity.gotoNotice(NoticeActivity.this,hv.categoryId, hv.Id);
			}

		});
		xlv_listview_2=(XListView)findViewById(R.id.xlv_listview_2);
		xlv_listview_2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				if (id > 0) {
					HolderView hv = (HolderView) view.getTag();
					NoticeActivity.gotoNotice(NoticeActivity.this,hv.categoryId, hv.Id);
				} else {
					mRefreshListServer2.getCurrentListView().startLoadMore();
				}
			}

		});
		xlv_listview_3=(XListView)findViewById(R.id.xlv_listview_3);
		xlv_listview_3.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				if (id > 0) {
					HolderView hv = (HolderView) view.getTag();
					NoticeActivity.gotoNotice(NoticeActivity.this,hv.categoryId, hv.Id);
				} else {
					mRefreshListServer3.getCurrentListView().startLoadMore();
				}
			}

		});
		
		mRefreshListServer1 = new RefreshListServer(NoticeActivity.this, getHandlerContext(),
				xlv_listview_1, new NoticeAdapter(NoticeActivity.this));
		mRefreshListServer1.setCacheTag(TAG+"mRefreshListServer1");
		mRefreshListServer1.setRefreshListServerListener(this);
		
		mRefreshListServer2 = new RefreshListServer(NoticeActivity.this, getHandlerContext(),
				xlv_listview_2, new NoticeAdapter(NoticeActivity.this));
		mRefreshListServer2.setCacheTag(TAG+"mRefreshListServer2");
		mRefreshListServer2.setRefreshListServerListener(this);
		
		mRefreshListServer3 = new RefreshListServer(NoticeActivity.this, getHandlerContext(),
				xlv_listview_3, new NoticeAdapter(NoticeActivity.this));
		mRefreshListServer3.setCacheTag(TAG+"mRefreshListServer3");
		mRefreshListServer3.setRefreshListServerListener(this);
		
		type=0;
		setEnabledByIndex();
		
		mRefreshListServer1.initLoad();
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.tvnotice1){
			type=0;
			setEnabledByIndex();
		}else if(v.getId()==R.id.tvnotice2){
			type=1;
			setEnabledByIndex();
			if(!load2){
				mRefreshListServer2.initLoad();
				load2=true;
			}
		}else if(v.getId()==R.id.tvnotice3){
			type=2;
			setEnabledByIndex();
			if(!load3){
				mRefreshListServer3.initLoad();
				load3=true;
			}
		}else{
			super.onClick(v);
		}
	}
	
	public void setEnabledByIndex(){
		tvNotice1.setEnabled(type==0?false:true);
		xlv_listview_1.setVisibility(type==0?View.VISIBLE:View.GONE);
		tvNotice2.setEnabled(type==1?false:true);
		xlv_listview_2.setVisibility(type==1?View.VISIBLE:View.GONE);
		tvNotice3.setEnabled(type==2?false:true);
		xlv_listview_3.setVisibility(type==2?View.VISIBLE:View.GONE);
	}

	@Override
	public void onLoading(final int HANDLER) {
		HttpServer hServer = new HttpServer(Constant.URL.GetListALL,getRefreshListServer().getHandlerContext());
		Map<String, String> params = new HashMap<String, String>();
		params.put("Id", getCategoryId());
		if(type==2&&getAppContext().currentUser().isLogin()){
			params.put("access_token", User.ACCESSKEY);
		}
		params.put("index",String.valueOf(getRefreshListServer().getCurrentPage() + 1));
//		params.put("size", String.valueOf(AppConstant.PAGESIZE));
		hServer.setParams(params);
		hServer.get(new HttpRunnable() {

			@Override
			public void run(Response response) throws AppException {
				getRefreshListServer().resolve(response);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						getRefreshListServer().getHandlerContext().getHandler().sendEmptyMessage(HANDLER);
					}
				});
			}

		}, false);
	}
	
	public String getCategoryId(){
		if(type==0){
			return "9";
		}else if(type==1){
			return "9";
		}else{
			return "9";
		}
	}
	
	public RefreshListServer getRefreshListServer() {
		if(type==0){
			return mRefreshListServer1;
		}else if(type==1){
			return mRefreshListServer2;
		}else{
			return mRefreshListServer3;
		}
	}
	
	public static void gotoNotice(Activity activity,String categoryId,String newsId){
		Bundle bundle=new Bundle();
		bundle.putString(NoticeDetailActivity.CATEGORYID, categoryId);
		bundle.putString(NoticeDetailActivity.NEWSID, newsId);
		Intent intent=new Intent(activity,NoticeDetailActivity.class);
		intent.putExtras(bundle);
		activity.startActivity(intent);
	}
	
}
