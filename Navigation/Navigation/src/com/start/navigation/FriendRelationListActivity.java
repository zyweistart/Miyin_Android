package com.start.navigation;

import java.util.HashMap;
import java.util.Map;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.start.core.Constant;
import com.start.core.CoreActivity;
import com.start.model.UIRunnable;
import com.start.service.HttpService.LoadMode;
import com.start.service.HttpService;
import com.start.service.PullListViewData;
import com.start.service.PullListViewData.OnLoadDataListener;
import com.start.service.adapter.FriendRelationAdapter;
import com.start.utils.CommonFn;

/**
 * 好友关系列表
 * @author start
 *
 */
public class FriendRelationListActivity extends CoreActivity implements OnClickListener {

	private Button mModuleMainHeaderContentAdd;
	
	private PullListViewData friendRelationData;
	
	public static final int REQUEST_CODE_LOGIN_REFRESH=222;
	public static final int REQUEST_CODE_SET_REFRESH=333;
	public static final int RESULT_CODE_SET_REFRESH=444;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_relation_list);
		setCurrentActivityTitle(R.string.activity_title_friend_relation_list);
		
		mModuleMainHeaderContentAdd = (Button) findViewById(R.id.module_main_header_content_click);
		mModuleMainHeaderContentAdd.setVisibility(View.VISIBLE);
		
		
		friendRelationData=new PullListViewData(this);
		friendRelationData.setOnLoadDataListener(
				new OnLoadDataListener(){

					@Override
					public void LoadData(LoadMode loadMode) {
						if(!getAppContext().isLogin()){
							
							friendRelationData.getPulllistview().setTag(HttpService.LISTVIEW_DATA_MORE);
							friendRelationData.getListview_footer_more().setText(R.string.load_more);
							friendRelationData.getListview_footer_progress().setVisibility(View.GONE);
							friendRelationData.getPulllistview().onRefreshComplete();
							
							CommonFn.alertDialog(FriendRelationListActivity.this, R.string.msg_not_login, new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									startActivityForResult(new Intent(FriendRelationListActivity.this,LoginActivity.class), REQUEST_CODE_LOGIN_REFRESH);
								}
								
							}).show();
							
						}else{
							Map<String,String> requestParams=new HashMap<String,String>();
							requestParams.put("orderby","2");
							friendRelationData.sendPullToRefreshListViewNetRequest(loadMode,Constant.ServerAPI.ufriendoQuery,requestParams,null,new UIRunnable(){
								@Override
								public void run() {
									friendRelationData.getAdapter().notifyDataSetChanged();
								} 
							},"friendlist","friendlist"+TAG);
						}
						
					}
					
				});
		friendRelationData.start(R.id.activity_friend_relation_pulllistview, 
				new FriendRelationAdapter(this,friendRelationData));
	}
	
	@Override
	public void onClick(View v) {
		startActivityForResult(new Intent(this,FriendRelationSetActivity.class), REQUEST_CODE_SET_REFRESH);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(getAppContext().isLogin()){
			if(requestCode==REQUEST_CODE_LOGIN_REFRESH){
				friendRelationData.getOnLoadDataListener().LoadData(LoadMode.INIT);
			}else if(requestCode==REQUEST_CODE_SET_REFRESH){
				if(resultCode==RESULT_CODE_SET_REFRESH){
					friendRelationData.getPulllistview().clickRefresh();
				}
			}
		}
	}

}