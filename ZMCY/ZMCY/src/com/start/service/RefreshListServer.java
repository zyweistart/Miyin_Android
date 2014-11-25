package com.start.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import start.core.AppConstant;
import start.core.AppConstant.Handler;
import start.core.AppConstant.ResultCode;
import start.core.AppContext;
import start.core.AppException;
import start.core.HandlerContext;
import start.core.HandlerContext.HandleContextListener;
import start.utils.TimeUtils;
import start.widget.xlistview.XListView;
import start.widget.xlistview.XListView.IXListViewListener;
import android.content.Context;
import android.os.Message;
import android.text.TextUtils;

public class RefreshListServer implements IXListViewListener,HandleContextListener {
	
	public static final String CURRENTPAGE="PageIndex";
	public static final String PAGESIZE="Size";

	private Boolean isDataLoadDone,isHideLoadMore;
	private int mCurrentPage;
	private String cacheTag;
	private Context mContext;
	private XListView mCurrentListView;
	private HandlerContext mExternalHandlerContext;
	private HandlerContext mHandlerContext;
	private RefreshListServerListener mRefreshListServerListener;
	private AppListAdapter mBaseListAdapter;
	private List<Map<String,Object>> mItemDatas = new ArrayList<Map<String,Object>>();
	
	public RefreshListServer(Context activity,HandlerContext handlerContext,XListView listView,AppListAdapter listAdapter){
		this.mContext=activity;
		this.mExternalHandlerContext=handlerContext;
		this.mCurrentListView=listView;
		this.mCurrentListView.setXListViewListener(this);
		this.mCurrentListView.setPullRefreshEnable(true);
		this.mBaseListAdapter=listAdapter;
		this.mBaseListAdapter.setItemDatas(getItemDatas());
		this.mCurrentListView.setAdapter(this.mBaseListAdapter);
	}
	
	/**
	 * 初始化加载
	 */
	public void initLoad(){
		getHandlerContext().getHandler().sendEmptyMessage(Handler.LOAD_INIT_DATA);
	}
	
	@Override
	public void onProcessMessage(Message msg) throws AppException {
		switch (msg.what) {
			case Handler.LOAD_INIT_DATA:
				if(!TextUtils.isEmpty(getCacheTag())){
					String responseString=AppContext.getSharedPreferences().getString(getCacheTag(),AppConstant.EMPTYSTR);
					if(!AppConstant.EMPTYSTR.equals(responseString)){
						Response response=new Response(null);
						response.setResponseString(responseString);
						response.resolveJson();
						resolve(response);
					}
				}else{
					getItemDatas().clear();
				}
				//如果缓存数据为空则加载网络数据
				if(getItemDatas().isEmpty()){
					setCurrentPage(0);
					getCurrentListView().startLoadMore();
				}else{
					getHandlerContext().getHandler().sendEmptyMessage(Handler.LOAD_END_MORE_DATA);
				}
				break;
			case Handler.LOAD_START_PULLDOWN_REFRESH_DATA:
				setCurrentPage(0);
				this.mRefreshListServerListener.onLoading(Handler.LOAD_END_PULLDOWN_REFRESH_DATA);
				break;
			case Handler.LOAD_START_MORE_DATA:
				this.mRefreshListServerListener.onLoading(Handler.LOAD_END_MORE_DATA);
				break;
			case Handler.LOAD_DATA_FAIL_CLEAR_DATA:
				setCurrentPage(0);
				getItemDatas().clear();
			case Handler.LOAD_END_PULLDOWN_REFRESH_DATA:
			case Handler.LOAD_END_MORE_DATA:
				if(isDataLoadDone){
					//数据加载完毕
					this.mCurrentListView.setPullLoadEnable(false);
				}else{
					if(isHideLoadMore){
						this.mCurrentListView.setPullLoadEnable(false);
					}else{
						this.mCurrentListView.setPullLoadEnable(true);
					}
					this.mBaseListAdapter.setItemDatas(new ArrayList<Map<String,Object>>(getItemDatas()	));
					this.mBaseListAdapter.notifyDataSetChanged();
				}
				getHandlerContext().getHandler().sendEmptyMessage(Handler.LOAD_END);
				break;
			case Handler.LOAD_END:
				getCurrentListView().stopRefresh();
				getCurrentListView().stopLoadMore();
				getCurrentListView().setRefreshTime(TimeUtils.getSysTime());
				break;
			case ResultCode.NODATA:
				if(getCurrentPage()==0){
					if(!TextUtils.isEmpty(getCacheTag())){
						AppContext.getSharedPreferences().putString(getCacheTag(), AppConstant.EMPTYSTR);
					}
					getItemDatas().clear();
					this.mBaseListAdapter.setItemDatas(new ArrayList<Map<String,Object>>(getItemDatas()	));
					this.mBaseListAdapter.notifyDataSetChanged();
				}
			default:
				getHandlerContext().getHandler().sendEmptyMessage(Handler.LOAD_END);
				//其它消息发送至外部Activity消息队列中
				Message message=new Message();
				message.what=msg.what;
				message.obj=msg.obj;
				if(mExternalHandlerContext!=null){
					mExternalHandlerContext.getHandler().sendMessage(message);
				}
				break;
		}
	}

	@Override
	public void onRefresh() {
		getHandlerContext().getHandler().sendEmptyMessage(Handler.LOAD_START_PULLDOWN_REFRESH_DATA);
	}

	@Override
	public void onLoadMore() {
		getHandlerContext().getHandler().sendEmptyMessage(Handler.LOAD_START_MORE_DATA);
	}

	/**
	 * JSON解析
	 */
	public void resolve(Response response) throws AppException{
		if(getCurrentPage()==0){
			if(!TextUtils.isEmpty(getCacheTag())){
				AppContext.getSharedPreferences().putString(getCacheTag(), response.getResponseString());
			}
			getItemDatas().clear();
		}
		int temp=Integer.parseInt(String.valueOf(response.getData(CURRENTPAGE)));
		isHideLoadMore=isDataLoadDone=(temp==getCurrentPage());
		setCurrentPage(temp);
		List<Map<String,Object>> datas=response.getListMapData();
		getItemDatas().addAll(datas);
		if(!isDataLoadDone){
			if(!getItemDatas().isEmpty()){
				isHideLoadMore=datas.size()<Integer.parseInt(String.valueOf(response.getData(PAGESIZE)));
			}
		}
	}
	
	public interface RefreshListServerListener{
		/**
		 * 数据加载中
		 * @param HANDLER  加载的类型区分是下拉刷新还是上拉加载更多
		 */
		public void onLoading(final int HANDLER);
		
	}

	public int getCurrentPage() {
		return mCurrentPage;
	}

	public void setCurrentPage(int mCurrentPage) {
		this.mCurrentPage = mCurrentPage;
	}

	public List<Map<String, Object>> getItemDatas() {
		return mItemDatas;
	}

	public void setItemDatas(List<Map<String, Object>> mItemDatas) {
		this.mItemDatas = mItemDatas;
	}
	
	public void setRefreshListServerListener(RefreshListServerListener refreshListServerListener) {
		this.mRefreshListServerListener = refreshListServerListener;
	}

	public XListView getCurrentListView() {
		return mCurrentListView;
	}

	public HandlerContext getHandlerContext() {
		if(mHandlerContext==null){
			mHandlerContext=new HandlerContext(this.mContext);
			mHandlerContext.setListener(this);
		}
		return mHandlerContext;
	}

	public String getCacheTag() {
		return cacheTag;
	}

	public void setCacheTag(String cacheTag) {
		this.cacheTag = cacheTag;
	}

	public AppListAdapter getBaseListAdapter() {
		return mBaseListAdapter;
	}
	
}
