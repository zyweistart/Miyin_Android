package com.ancun.yzb.layout;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ancun.bean.ContactModel;
import com.ancun.core.BaseActivity;
import com.ancun.core.BaseScrollContent;
import com.ancun.service.AppService;
import com.ancun.yzb.R;

public class ContactsContentView extends BaseScrollContent implements Filterable,OnItemClickListener {
	//是否刷新数据
	public static Boolean isRefreshData=true;
	
	private FilterContact mFilter; 
	private ListView contactListView;
	private ContactAdapter adapter;
	private EditText etSearch;
	private ImageButton ibSearchClean;
	private ImageButton ibSearchBegin;
	private List<ContactModel> mListDataItems;
	private List<ContactModel> mListDataItemsFilter;
	private int lastPosition=-1;

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
	private InputMethodManager mInputMethodManager;

	public ContactsContentView(BaseActivity activity) {
		super(activity, R.layout.module_scroll_contacts);
		mInputMethodManager=(InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		contactListView = (ListView) findViewById(R.id.contacts_listview);
		contactListView.setOnItemClickListener(this);
		View ContactsSearchBarView = View.inflate(activity, R.layout.module_contact_search_bar, null);  
		//把view对象添加到listView对象的头部，可以随listView一起滑动
		contactListView.addHeaderView(ContactsSearchBarView); 
 
		etSearch=(EditText)ContactsSearchBarView.findViewById(R.id.contact_search_bar_content);
		etSearch.addTextChangedListener(new CustomTextWatcher());
		ibSearchClean=(ImageButton)ContactsSearchBarView.findViewById(R.id.contact_search_bar_clean);
		ibSearchClean.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				etSearch.setText("");
			}
		});
		ibSearchBegin=(ImageButton)ContactsSearchBarView.findViewById(R.id.contact_search_bar_icon);
		ibSearchBegin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//打开软键盘
				if (mInputMethodManager.isActive()) {
					mInputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		});

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
		contactListView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				isShowOverLay=true;
				//关闭输入框弹窗的键盘
				mInputMethodManager.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
				return false;
			}
		});
		contactListView.setOnScrollListener(new OnScrollListener() {
			
			@Override  
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {  
				//显示姓名的第一个字
				if (isShowOverLay&&mReady&&mListDataItemsFilter!=null&&mListDataItemsFilter.size()>0&&overlay!=null) {
					String name=mListDataItemsFilter.get(firstVisibleItem).getName();
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
				//filter listview赋初值
				if(mListDataItemsFilter==null){
					mListDataItemsFilter=new ArrayList<ContactModel>();
				}else{
					mListDataItemsFilter.clear();
				}
				mListDataItemsFilter.addAll(mListDataItems);
				getCurrentActivity().runOnUiThread(new Runnable() {
					public void run() {
						if(adapter==null){
							adapter=new ContactAdapter();
							contactListView.setAdapter(adapter);
						}else{
							getFilter().filter(etSearch.getText());
						}
						isRefreshData=false;
					}
				});
			};
		}.start();
	}
	/**
	 * 联系人适配器
	 */
	public class ContactAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mListDataItemsFilter.size();
		}

		@Override
		public Object getItem(int position) {
			return mListDataItemsFilter.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,ViewGroup parent) {
			ContactViewHolder holder;
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.lvitem_content_contact,null);
				holder = new ContactViewHolder();
				holder.photo = (ImageView) convertView.findViewById(R.id.contact_photo);
				holder.name = (TextView) convertView.findViewById(R.id.contact_name_text);
				holder.dial_frame = (LinearLayout) convertView.findViewById(R.id.dial_frame);
				holder.dial_recording = (Button)convertView.findViewById(R.id.dial_recording);
				holder.dial_normal = (Button)convertView.findViewById(R.id.dial_normal);
				convertView.setTag(holder);
			} else {
				holder = (ContactViewHolder) convertView.getTag();
			}
			ContactModel info = mListDataItemsFilter.get(position);
			holder.name.setText(info.getName());
			if (info.getPhotoID() > 0) {
				holder.photo.setImageBitmap(getCurrentActivity().getContactDaoImpl().loadContactPhoto(info.getId()));
			}else{
				holder.photo.setImageResource(R.drawable.ic_contact_head);
			}
			holder.dial_recording.setTag(info);
			holder.dial_recording.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ContactModel holder=(ContactModel)v.getTag();
					dial(holder,1);
				}
			});
			holder.dial_normal.setTag(info);
			holder.dial_normal.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ContactModel holder=(ContactModel)v.getTag();
					dial(holder,2);
				}
			});
			holder.dial_frame.setVisibility(lastPosition==position?View.VISIBLE:View.GONE);
			return convertView;
		}
		
	}
	/**
	 * 视图辅助类
	 * @author Start
	 */
	public final class ContactViewHolder {
		/**
		 * 照片
		 */
		private ImageView photo;
		/**
		 * 姓名
		 */
		private TextView name;
		
		private LinearLayout dial_frame;
		private Button dial_recording;
		private Button dial_normal;
		
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
	
	/**
	 * 按照姓名查找 的filter类
	 */
	private class FilterContact extends Filter {  

		@Override  
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();
			mListDataItemsFilter.clear();
			if (prefix == null || prefix.length() == 0) {
				//输入为空
				mListDataItemsFilter.addAll(mListDataItems);
			} else {
				for(ContactModel userInfo:mListDataItems){
					String name=userInfo.getName();
					if(TextUtils.isEmpty(name)){
						continue;
					}
					name=name.toLowerCase();
					String pre=prefix.toString().toLowerCase();
					if(name.contains(pre)){
						mListDataItemsFilter.add(userInfo);
					}else if(name.equals(pre)){
						mListDataItemsFilter.add(userInfo);
					}else if(userInfo.getPinyinName().contains(pre)){
						mListDataItemsFilter.add(userInfo);
					}
				}
			}
			results.values = mListDataItemsFilter;
			results.count = mListDataItemsFilter.size();
			return results;  
		}  

		@Override  
		protected void publishResults(CharSequence constraint, FilterResults results) {
			adapter.notifyDataSetChanged();
		}
		
	}
	
	private class CustomTextWatcher implements TextWatcher {

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

	public EditText getEtSearch() {
		return etSearch;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		ContactViewHolder v=(ContactViewHolder)view.getTag();
		v.dial_frame.setVisibility(View.VISIBLE);
		position=position-1;
		if(lastPosition!=position){
			lastPosition=position;
		}else{
			lastPosition=-1;
		}
		adapter.notifyDataSetChanged();
	}

	
	private void dial(ContactModel holder,final int type){
		List<String>phones=getCurrentActivity().getContactDaoImpl().getContactAllPhone(holder.getLookupKey());
		if(phones.size()>1){
			final String[] phonearr=phones.toArray(new String[phones.size()]);
			new AlertDialog.Builder(getCurrentActivity())
			.setIcon(android.R.drawable.ic_dialog_info) 
			.setSingleChoiceItems(phonearr,1,new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,final int which) {
					String phone=phonearr[which];
					if(type==1){
						AppService.inAppDial(getCurrentActivity(),phone);
					}else{
						AppService.call(getCurrentActivity(), phone);
					}
					dialog.dismiss();
				}
			}).create().show();
		}else if(phones.size()==1){
			//如果电话为一个则直接拔号
			String phone=phones.get(0);
			if(type==1){
				AppService.inAppDial(getCurrentActivity(),phone);
			}else{
				AppService.call(getCurrentActivity(), phone);
			}
		}else{
			getHandlerContext().makeTextLong("无任何联系号码");
		}
	}
	
}