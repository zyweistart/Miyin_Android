package com.ancun.yzb.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import start.utils.HanziToPinyin;
import start.widget.CustomEditText;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ancun.core.BaseActivity;
import com.ancun.core.BaseScrollContent;
import com.ancun.yzb.R;
import com.ancun.yzb.adapter.ContactAdapter;
import com.ancun.yzb.adapter.ContactAdapter.ContactViewHolder;

public class ContactsContentView extends BaseScrollContent implements Filterable,OnItemClickListener {
	
	//是否刷新数据
	public static Boolean isRefreshData=true;
	
	private ImageButton ibSearch;
	private CustomEditText etContent;
	private FilterContact mFilter; 
	private ListView mListView;
	private ContactAdapter mAdapter;
	private List<Map<String,String>> mListDataItems;
	private List<Map<String,String>> mListDataItemsFilter;

	private boolean isShowOverLay=false;
	//联系人滚动时浮动的字
	private TextView overlay;
	//上一次浮动的字
	private String mPrevLetter="";

	private WindowManager mWindowManager;
	//是否显示mDialogText
	private boolean mShowing;
	//mDialogText是否已准备好
	private boolean mReady; 
	private RemoveWindow mRemoveWindow = new RemoveWindow();
	private Handler mHandler = new Handler();

	public ContactsContentView(BaseActivity activity) {
		super(activity, R.layout.module_scroll_contacts);
		mListView = (ListView) findViewById(R.id.listview);
		mListView.setOnItemClickListener(this);
		
		View searchBarView = View.inflate(activity, R.layout.module_search_bar, null);  
		//把view对象添加到listView对象的头部，可以随listView一起滑动
		mListView.addHeaderView(searchBarView); 
		ibSearch=(ImageButton)searchBarView.findViewById(R.id.ib_search);
		ibSearch.setVisibility(View.GONE);
		etContent=(CustomEditText)searchBarView.findViewById(R.id.et_content);
		etContent.setHint(R.string.searchbarhint1);
		etContent.addTextChangedListener(new SearchBarTextWatcher());

		//获取Window窗口管理服务
		mWindowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		overlay = (TextView) View.inflate(activity, R.layout.module_contact_overlay, null);  
		overlay.setVisibility(View.INVISIBLE);

		//将Runnable添加到消息队列中
		mHandler.post(new Runnable() {
			public void run() {
				mReady = true;
				//设置mDialogText的WindowManager.LayoutParams参数
				WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
						WindowManager.LayoutParams.TYPE_APPLICATION,
						WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
						| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
						PixelFormat.TRANSLUCENT);
				mWindowManager.addView(overlay, lp);
			}

		});
		mListView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				isShowOverLay=true;
				//关闭输入框弹窗的键盘
				getCurrentActivity().getInputMethodManager().hideSoftInputFromWindow(etContent.getWindowToken(), 0);
				return false;
			}
		});
		mListView.setOnScrollListener(new OnScrollListener() {
			
			@Override  
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {  
				//显示姓名的第一个字
				if (isShowOverLay&&mReady&&mListDataItemsFilter!=null&&mListDataItemsFilter.size()>0&&overlay!=null) {
					String name=mListDataItemsFilter.get(firstVisibleItem).get(ContactAdapter.STRNAME);
					if(name!=null){
						String firstLetter=name.substring(0, 1);
						//当浮动字 不可见时设置其可见
						if (!mShowing && !firstLetter.equals(mPrevLetter)  ) {
							mShowing = true;
							overlay.setVisibility(View.VISIBLE);
						}
						overlay.setText(firstLetter);  
						//将消息队列中还在等待post的mRemoveWindow清除
						mHandler.removeCallbacks(mRemoveWindow);
						//将Runnable mRemoveWindow添加到消息队列中，并延迟1.5s后运行
						// 1.5s后 设置为不可见
						mHandler.postDelayed(mRemoveWindow, 1000);
						mPrevLetter = firstLetter;
					}
				}
			}  
			@Override  
			public void onScrollStateChanged(AbsListView view, int scrollState) {  
				
			}  

		});
	}
	
	public void onDestroy() {
		if(mWindowManager!=null){
			if(overlay!=null){
				mWindowManager.removeView(overlay);
				overlay=null;
			}
		}
	}
	
	public void loadData(){
		new Thread() {
			public void run() {
				mListDataItems=getCurrentActivity().getContactDaoImpl().loadAllContact();
				if(mListDataItemsFilter==null){
					mListDataItemsFilter=new ArrayList<Map<String,String>>();
				}else{
					mListDataItemsFilter.clear();
				}
				mListDataItemsFilter.addAll(mListDataItems);
				getCurrentActivity().runOnUiThread(new Runnable() {
					public void run() {
						if(mAdapter==null){
							mAdapter=new ContactAdapter(getCurrentActivity());
							mAdapter.setItemDatas(mListDataItemsFilter);
							mListView.setAdapter(mAdapter);
						}else{
							getFilter().filter(etContent.getText());
						}
						isRefreshData=false;
					}
				});
			};
		}.start();
	}
	
	@Override  
	public Filter getFilter() {   
		if (mFilter == null) {  
			mFilter = new FilterContact();  
		}  
		return mFilter;  
	}  
	
	private final class RemoveWindow implements Runnable {
		public void run() {
			//将overlay设置为不可见
			if (mShowing) {
				overlay.setVisibility(View.INVISIBLE);
//				mPrevLetter="";
				mShowing = false;
			}
		}
	}
	
	private class FilterContact extends Filter {  

		@Override  
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();
			mListDataItemsFilter.clear();
			if (prefix == null || prefix.length() == 0) {
				mListDataItemsFilter.addAll(mListDataItems);
			} else {
				for(Map<String,String> data:mListDataItems){
					String name=data.get(ContactAdapter.STRNAME);
					if(TextUtils.isEmpty(name)){
						continue;
					}
					name=name.toLowerCase();
					String pre=prefix.toString().toLowerCase();
					if(name.contains(pre)){
						mListDataItemsFilter.add(data);
					}else if(HanziToPinyin.getPinYin(name).contains(pre)){
						mListDataItemsFilter.add(data);
					}
				}
			}
			results.values = mListDataItemsFilter;
			results.count = mListDataItemsFilter.size();
			return results;  
		}  

		@Override  
		protected void publishResults(CharSequence constraint, FilterResults results) {
			mAdapter.notifyDataSetChanged();
		}
		
	}
	
	private class SearchBarTextWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}
		@Override
		public void afterTextChanged(Editable s) {
			getFilter().filter(s);
			//每次输入框文字变化    需要滚动屏幕才显示浮动姓
			isShowOverLay=false;
		}
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		isShowOverLay=false;
		ContactViewHolder v=(ContactViewHolder)view.getTag();
		v.dial_frame.setVisibility(View.VISIBLE);
		position=position-1;
		mAdapter.setLastPosition(position);
	}
	
}