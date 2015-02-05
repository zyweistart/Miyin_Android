package com.start.xinkuxue.training;

import java.util.HashMap;
import java.util.Map;

import start.core.AppException;
import start.widget.xlistview.XListView;
import android.os.Bundle;
import android.view.View;
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

/**
 * 积分榜
 * @author zhenyao
 *
 */
public class ScoreboardActivity extends BaseActivity implements RefreshListServerListener{

	private int type;
	private TextView tvTitle,tvscoreboard1,tvscoreboard2,tv1,tv2,tv3;
	private XListView xlv_listview_1,xlv_listview_2;
	private RefreshListServer mRefreshListServer1,mRefreshListServer2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scoreboard);
		tvTitle=(TextView)findViewById(R.id.tvTitle);
		tvTitle.setText("积分榜");
		tvscoreboard1=(TextView)findViewById(R.id.tvscoreboard1);
		tvscoreboard2=(TextView)findViewById(R.id.tvscoreboard2);
		tv1=(TextView)findViewById(R.id.tv1);
		tv2=(TextView)findViewById(R.id.tv2);
		tv3=(TextView)findViewById(R.id.tv3);
		
		xlv_listview_1=(XListView)findViewById(R.id.xlv_listview_1);
		xlv_listview_2=(XListView)findViewById(R.id.xlv_listview_2);
		
		mRefreshListServer1 = new RefreshListServer(this, getHandlerContext(),
				xlv_listview_1, new ScoreboardAdapter(this));
		mRefreshListServer1.setCacheTag(TAG+"mRefreshListServer1");
		mRefreshListServer1.setRefreshListServerListener(this);
		
		mRefreshListServer2 = new RefreshListServer(this, getHandlerContext(),
				xlv_listview_2, new ScoreboardAdapter(this));
		mRefreshListServer2.setCacheTag(TAG+"mRefreshListServer2");
		mRefreshListServer2.setRefreshListServerListener(this);
		
		type=0;
		setEnabledByIndex();
		mRefreshListServer1.initLoad();
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.tvscoreboard1){
			type=0;
			setEnabledByIndex();
		}else if(v.getId()==R.id.tvscoreboard2){
			type=1;
			setEnabledByIndex();
		}
	}
	
	public void setEnabledByIndex(){
		tvscoreboard1.setEnabled(type==0?false:true);
		xlv_listview_1.setVisibility(type==0?View.VISIBLE:View.GONE);
		tvscoreboard2.setEnabled(type==1?false:true);
		xlv_listview_2.setVisibility(type==1?View.VISIBLE:View.GONE);
		if(type==0){
			tv1.setText("姓名");
			tv2.setText("原由");
			tv3.setText("积分");
		}else{
			tv1.setText("名次");
			tv2.setText("团队名称");
			tv3.setText("积分");
		}
		
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
		}else{
			return "9";
		}
	}
	
	public RefreshListServer getRefreshListServer() {
		if(type==0){
			return mRefreshListServer1;
		}else{
			return mRefreshListServer2;
		}
	}
	
}