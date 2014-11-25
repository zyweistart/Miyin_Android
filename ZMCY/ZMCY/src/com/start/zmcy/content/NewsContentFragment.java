package com.start.zmcy.content;

import java.util.HashMap;
import java.util.Map;

import start.core.AppConstant;
import start.core.AppException;
import start.widget.xlistview.XListView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.start.core.BaseFragment;
import com.start.core.BaseFragmentActivity;
import com.start.service.HttpRunnable;
import com.start.service.HttpServer;
import com.start.service.RefreshListServer;
import com.start.service.RefreshListServer.RefreshListServerListener;
import com.start.service.Response;
import com.start.service.bean.NewsCategory;
import com.start.zmcy.NewsDetailActivity;
import com.start.zmcy.R;
import com.start.zmcy.adapter.NewsListAdapter;
import com.start.zmcy.adapter.NewsListAdapter.HolderView;

public class NewsContentFragment  extends BaseFragment implements RefreshListServerListener {

	private NewsCategory mCategory;
	private BaseFragmentActivity mActivity;
	
	private View mCurrentView;
	private XListView mListView;
	private RefreshListServer mRefreshListServer;
	
    public NewsContentFragment(BaseFragmentActivity activity,NewsCategory category){
    	super();
    	this.mActivity=activity;
    	this.mCategory=category;
    	setTitle(this.mCategory.getTitle());
    }
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		mCurrentView = inflater.inflate(R.layout.fragment_news,null);
		
		mListView = (XListView)mCurrentView.findViewById(R.id.xlv_listview);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				if(id>0){
					HolderView hv=(HolderView)view.getTag();
					NewsContentFragment.gotoNews(mActivity,hv.recordno);
				}else{
					mRefreshListServer.getCurrentListView().startLoadMore();
				}
			}
			
		});
		mRefreshListServer = new RefreshListServer(mActivity,mActivity.getHandlerContext(), mListView,new NewsListAdapter(mActivity));
		mRefreshListServer.setCacheTag("NewsContentFragment");
		mRefreshListServer.setRefreshListServerListener(this);

		mRefreshListServer.initLoad();
		
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//缓存的rootView需要判断是否已经被加过parent,如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup)mCurrentView.getParent();
        if (parent != null) {
            parent.removeView(mCurrentView);
        }
        return mCurrentView;
    }
	
	@Override
	public void onLoading(final int HANDLER) {
		HttpServer hServer = new HttpServer("GetListPage",mRefreshListServer.getHandlerContext());
		Map<String, String> params = new HashMap<String, String>();
		params.put("classId", "11,12");
		params.put("currentpage",String.valueOf(mRefreshListServer.getCurrentPage() + 1));
		params.put("size", String.valueOf(AppConstant.PAGESIZE));
		hServer.setParams(params);
		hServer.get(new HttpRunnable() {

			@Override
			public void run(Response response) throws AppException {
				mRefreshListServer.resolve(response);
				mRefreshListServer.getHandlerContext().getHandler().sendEmptyMessage(HANDLER);
			}

		}, false);
	}
    
	public static void gotoNews(Activity activity,String recordno){
		Bundle bundle=new Bundle();
		bundle.putString(NewsDetailActivity.NEWSID, recordno);
		Intent intent=new Intent(activity,NewsDetailActivity.class);
		intent.putExtras(bundle);
		activity.startActivity(intent);
	}
	
}