package com.ancun.yl.layout;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import start.core.AppConstant;
import start.core.AppException;
import start.service.DownloadRunnable;
import start.service.HttpRunnable;
import start.service.HttpServer;
import start.service.RefreshListServer;
import start.service.RefreshListServer.RefreshListServerListener;
import start.service.Response;
import start.utils.NetConnectManager;
import start.widget.CustomEditText;
import start.widget.xlistview.XListView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;

import com.ancun.core.BaseActivity;
import com.ancun.core.BaseScrollContent;
import com.ancun.core.Constant;
import com.ancun.service.User;
import com.ancun.widget.PlayerView;
import com.ancun.yl.BaseContext;
import com.ancun.yl.R;
import com.ancun.yl.RecordedDetailActivity;
import com.ancun.yl.adapter.RecordingAdapter;
import com.ancun.yl.adapter.RecordingAdapter.HolderView;

public class RecordingContentView extends BaseScrollContent implements RefreshListServerListener,OnClickListener {

	//是否刷新数据
	public static Boolean isRefreshData=false;
	
	private XListView mListView;
	private RefreshListServer mRefreshListServer;
	private RecordingAdapter mRecordingAdapter;
	private PlayerView playerView;
	private ImageButton ib_search;
	private CustomEditText et_content;

	public RecordingContentView(BaseActivity activity) {
		super(activity, R.layout.module_scroll_recording);
		et_content=(CustomEditText)findViewById(R.id.et_content);
		et_content.setHint(R.string.searchbarhint2);
		ib_search=(ImageButton)findViewById(R.id.ib_search);
		ib_search.setOnClickListener(this);
		
		playerView=(PlayerView)findViewById(R.id.playerview);
		mListView = (XListView) findViewById(R.id.xlv_listview);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				if(getCurrentActivity().getInputMethodManager().isActive()){
					getCurrentActivity().getInputMethodManager().hideSoftInputFromWindow(getCurrentActivity().getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				}
				if (id >= 0) {
					int i=position-1;
					mRecordingAdapter.setLastPosition(i);
					HolderView v=(HolderView)view.getTag();
					if(v.file.exists()){
						playerView.initPlayerFile(v.file.getAbsolutePath());
						playerView.startPlayer();
					}else{
						final String fileno=v.fileno;
						if(NetConnectManager.isMobilenetwork(getCurrentActivity())){
							new AlertDialog.Builder(getCurrentActivity())
							.setIcon(android.R.drawable.ic_dialog_info)
							.setMessage(R.string.mobiledownloadtip)
							.setPositiveButton(R.string.cancle, null)
							.setNegativeButton(R.string.sure, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									download(fileno);
								}
							}).show();
						}else{
							download(fileno);
						}
					}
				} else {
					mRefreshListServer.getCurrentListView().startLoadMore();
				}
			}
		});
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,int position, long id) {
				if (id >= 0) {
					int i=position-1;
					mRecordingAdapter.setLastPosition(i);
					HolderView v=(HolderView)view.getTag();
					final String fileno=v.fileno;
					new AlertDialog.Builder(getCurrentActivity())
					.setMessage(R.string.suredeleterecording)
					.setPositiveButton(R.string.cancle, null)
					.setNegativeButton(R.string.sure, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							
							HttpServer hServer=new HttpServer(Constant.URL.ylcnrecAlter, getCurrentActivity().getHandlerContext());
                    		Map<String,String> headers=new HashMap<String,String>();
                    		headers.put("sign", User.ACCESSKEY);
                    		hServer.setHeaders(headers);
                    		Map<String,String> params=new HashMap<String,String>();
                    		params.put("accessid", User.ACCESSID);
                    		params.put("ownerno",getCurrentActivity().getAppContext().currentUser().getPhone());
                    		params.put("fileno", fileno);
                    		params.put("alteract", "1");
                    		hServer.setParams(params);
                    		hServer.get(new HttpRunnable() {
                    			
                    			@Override
                    			public void run(Response response) throws AppException {
                					for(Map<String,String> content:mRefreshListServer.getItemDatas()){
    									if(fileno.equals(content.get(RecordingAdapter.RECORDED_FILENO))){
    										mRefreshListServer.getItemDatas().remove(content);
    										mRefreshListServer.getBaseListAdapter().getItemDatas().remove(content);
    										getCurrentActivity().runOnUiThread(new Runnable() {
    											@Override
    											public void run() {
    												mRecordingAdapter.notifyDataSetChanged();
    											}
    										});
    										break;
    									}
    								}
                    			}
                    			
                    		});
                    		
						}
					}).show();
				}
				return false;
			}
		});
		
		mRecordingAdapter=new RecordingAdapter(getCurrentActivity());
		mRefreshListServer = new RefreshListServer(getCurrentActivity(), mListView,mRecordingAdapter);
		mRefreshListServer.setCacheTag(getAppContext().currentUser().getPhone()+TAG);
		mRefreshListServer.setListTag("reclist");
		mRefreshListServer.setInfoTag("recinfo");
		mRefreshListServer.setRefreshListServerListener(this);
	}

	public RefreshListServer getRefreshListServer(){
		return mRefreshListServer;
	}
	
	@Override
	public void onLoading(final int HANDLER) {
		isRefreshData=false;
		HttpServer hServer = new HttpServer(Constant.URL.ylcnrecQry,mRefreshListServer.getHandlerContext());
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("sign", User.ACCESSKEY);
		hServer.setHeaders(headers);
		Map<String, String> params = new HashMap<String, String>();
		params.put("accessid",User.ACCESSID);
		params.put("ownerno",getCurrentActivity().getAppContext().currentUser().getPhone());
		params.put("calledno",String.valueOf(et_content.getText()));
		params.put("cpdelflg", "2");
		params.put("currentpage",String.valueOf(mRefreshListServer.getCurrentPage() + 1));
		params.put("pagesize", String.valueOf(AppConstant.PAGESIZE));
		hServer.setParams(params);
		hServer.get(new HttpRunnable() {

			@Override
			public void run(Response response) throws AppException {
				mRefreshListServer.resolve(response);
				mRefreshListServer.getHandlerContext().getHandler().sendEmptyMessage(HANDLER);
			}

		}, false);
	}

	public void download(String fileNo){
		HttpServer hServer=new HttpServer(Constant.URL.ylcnrecDown, getCurrentActivity().getHandlerContext());
		Map<String,String> headers=new HashMap<String,String>();
		headers.put("sign", User.ACCESSKEY);
		hServer.setHeaders(headers);
		Map<String,String> params=new HashMap<String,String>();
		params.put("accessid", User.ACCESSID);
		params.put("ownerno",getCurrentActivity().getAppContext().currentUser().getPhone());
		params.put("fileno", fileNo);
		hServer.setParams(params);
		hServer.download(new DownloadRunnable() {
			
			@Override
			public void run(final File file) throws AppException {
				getCurrentActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mRecordingAdapter.notifyDataSetChanged();
						playerView.initPlayerFile(file.getAbsolutePath());
						playerView.startPlayer();
					}
				});
			}
			
		},BaseContext.getInstance().getStorageDirectory(Constant.RECORDDIRECTORY),fileNo);
	}
	
	public void onPause() {
		playerView.pause();
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data!=null&&mRecordingAdapter!=null){
			if(requestCode==RecordingAdapter.REMARKREQUESTCODE){
				if(resultCode==RecordedDetailActivity.REMARKRESULTCODE){
					Bundle bundle=data.getExtras();
					if(bundle!=null){
						String fileno=bundle.getString(RecordingAdapter.RECORDED_FILENO);
						Integer cerflag=bundle.getInt(RecordingAdapter.RECORDED_CEFFLAG);
						Integer accstatus=bundle.getInt(RecordingAdapter.RECORDED_ACCSTATUS);
						String remark=bundle.getString(RecordingAdapter.RECORDED_REMARK);
						for(Map<String,String> content:mRefreshListServer.getItemDatas()){
							if(content.get(RecordingAdapter.RECORDED_FILENO).equals(fileno)){
								content.put(RecordingAdapter.RECORDED_FILENO, fileno);
								content.put(RecordingAdapter.RECORDED_CEFFLAG, cerflag+"");
								content.put(RecordingAdapter.RECORDED_ACCSTATUS, accstatus+"");
								content.put(RecordingAdapter.RECORDED_REMARK, remark);
								mRecordingAdapter.notifyDataSetChanged();
								break;
							}
						}
					}
				}else if(resultCode==RecordedDetailActivity.REMARKMODIFYCODE){
					Bundle bundle=data.getExtras();
					if(bundle!=null){
						String fileno=bundle.getString(RecordingAdapter.RECORDED_FILENO);
						String remark=bundle.getString(RecordingAdapter.RECORDED_REMARK);
						for(Map<String,String> content:mRefreshListServer.getItemDatas()){
							if(content.get(RecordingAdapter.RECORDED_FILENO).equals(fileno)){
								content.put(RecordingAdapter.RECORDED_FILENO, fileno);
								content.put(RecordingAdapter.RECORDED_REMARK, remark);
								mRecordingAdapter.notifyDataSetChanged();
								break;
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.ib_search){
			refreshData();
		}
	}
	
	public void refreshData(){
		if(getCurrentActivity().getInputMethodManager().isActive()){
			getCurrentActivity().getInputMethodManager().hideSoftInputFromWindow(getCurrentActivity().getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		mRefreshListServer.setCurrentPage(0);
		mRefreshListServer.getCurrentListView().startLoadMore();
	}
	
}