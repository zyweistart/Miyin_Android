package start.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import start.core.AppContext;
import start.core.AppException;
import start.core.AppActivity;
import start.core.AppListAdapter;
import start.core.AppConstant;
import start.core.HandlerContext;
import start.core.AppConstant.Handler;
import start.core.AppConstant.ResultCode;
import start.core.HandlerContext.HandleContextListener;
import start.utils.TimeUtils;
import start.widget.xlistview.XListView;
import start.widget.xlistview.XListView.IXListViewListener;
import android.os.Message;
import android.text.TextUtils;

public class RefreshListServer implements IXListViewListener,HandleContextListener {

	private Boolean isDataLoadDone;
	private int mCurrentPage;
	private String cacheTag;
	private AppActivity mActivity;
	private XListView mCurrentListView;
	private HandlerContext mHandlerContext;
	private RefreshListServerListener mRefreshListServerListener;
	private AppListAdapter mBaseListAdapter;
	private List<Map<String,String>> mItemDatas = new ArrayList<Map<String,String>>();
	private String listTag,infoTag;
	
	public RefreshListServer(AppActivity activity,XListView listView,AppListAdapter listAdapter){
		this.mActivity=activity;
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
			case ResultCode.NODATA:
				if(getCurrentPage()==0){
					if(!TextUtils.isEmpty(getCacheTag())){
						AppContext.getSharedPreferences().putString(getCacheTag(), AppConstant.EMPTYSTR);
					}
					getItemDatas().clear();
				}
				getHandlerContext().getHandler().sendEmptyMessage(Handler.LOAD_END);
//				getHandlerContext().makeTextShort(String.valueOf(msg.obj));
				break;
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
				}else{
					this.mBaseListAdapter.setItemDatas(new ArrayList<Map<String,String>>(getItemDatas()	));
					this.mBaseListAdapter.notifyDataSetChanged();
				}
				getHandlerContext().getHandler().sendEmptyMessage(Handler.LOAD_END);
				break;
			case Handler.LOAD_END:
				getCurrentListView().stopRefresh();
				getCurrentListView().stopLoadMore();
				getCurrentListView().setRefreshTime(TimeUtils.getSysTime());
				break;
			default:
				getHandlerContext().getHandler().sendEmptyMessage(Handler.LOAD_END);
				//其它消息发送至外部Activity消息队列中
				Message message=new Message();
				message.what=msg.what;
				message.obj=msg.obj;
				mActivity.getHandlerContext().getHandler().sendMessage(message);
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
		int temp=Integer.parseInt(response.getPageInfoMapData().get("currentpage"));
		isDataLoadDone=(temp==getCurrentPage());
		setCurrentPage(temp);
		getItemDatas().addAll(response.getListMapData(listTag,infoTag));
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

	public List<Map<String, String>> getItemDatas() {
		return mItemDatas;
	}

	public void setItemDatas(List<Map<String, String>> mItemDatas) {
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
			mHandlerContext=new HandlerContext(this.mActivity);
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

	public void setListTag(String listTag) {
		this.listTag = listTag;
	}

	public void setInfoTag(String infoTag) {
		this.infoTag = infoTag;
	}
	
}
