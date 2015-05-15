package start.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import start.core.AppActivity;
import start.core.AppConstant;
import start.core.AppConstant.Handler;
import start.core.AppConstant.ResultCode;
import start.core.AppContext;
import start.core.AppException;
import start.core.AppListAdapter;
import start.core.HandlerContext;
import start.core.HandlerContext.HandleContextListener;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ListView;

public class ListServer implements HandleContextListener {

	private String cacheTag;
	private AppActivity mActivity;
	private ListView mCurrentListView;
	private HandlerContext mHandlerContext;
	private RefreshListServerListener mRefreshListServerListener;
	private AppListAdapter mBaseListAdapter;
	private List<Map<String,String>> mItemDatas = new ArrayList<Map<String,String>>();
	private String listTag,infoTag;
	
	public ListServer(AppActivity activity,ListView listView,AppListAdapter listAdapter){
		this.mActivity=activity;
		this.mCurrentListView=listView;
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
				}
				if(getItemDatas().isEmpty()){
					getHandlerContext().getHandler().sendEmptyMessage(Handler.LOAD_START_PULLDOWN_REFRESH_DATA);
				}else{
					getHandlerContext().getHandler().sendEmptyMessage(Handler.LOAD_END_MORE_DATA);
				}
				break;
			case Handler.LOAD_START_PULLDOWN_REFRESH_DATA:
				this.mRefreshListServerListener.onLoading(Handler.LOAD_END_PULLDOWN_REFRESH_DATA);
				break;
			case Handler.LOAD_START_MORE_DATA:
				this.mRefreshListServerListener.onLoading(Handler.LOAD_END_MORE_DATA);
				break;
			case Handler.LOAD_DATA_FAIL_CLEAR_DATA:
				getItemDatas().clear();
			case Handler.LOAD_END_PULLDOWN_REFRESH_DATA:
			case Handler.LOAD_END_MORE_DATA:
				this.mBaseListAdapter.setItemDatas(new ArrayList<Map<String,String>>(getItemDatas()	));
				this.mBaseListAdapter.notifyDataSetChanged();
				break;
			case ResultCode.NODATA:
				if(!TextUtils.isEmpty(getCacheTag())){
					AppContext.getSharedPreferences().putString(getCacheTag(), AppConstant.EMPTYSTR);
				}
				getItemDatas().clear();
			default:
				//其它消息发送至外部Activity消息队列中
				Message message=new Message();
				message.what=msg.what;
				message.obj=msg.obj;
				mActivity.getHandlerContext().getHandler().sendMessage(message);
				break;
		}
	}

	/**
	 * JSON解析
	 */
	public void resolve(Response response) throws AppException{
		if(!TextUtils.isEmpty(getCacheTag())){
			AppContext.getSharedPreferences().putString(getCacheTag(), response.getResponseString());
		}else{
			getItemDatas().clear();
		}
		getItemDatas().addAll(response.getListMapData2(listTag,infoTag));
	}
	
	public interface RefreshListServerListener{
		/**
		 * 数据加载中
		 * @param HANDLER  加载的类型区分是下拉刷新还是上拉加载更多
		 */
		public void onLoading(final int HANDLER);
		
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

	public ListView getCurrentListView() {
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
