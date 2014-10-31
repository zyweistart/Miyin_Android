package com.ancun.yzb.layout;

import java.util.List;

import start.utils.TimeUtils;
import android.content.Intent;
import android.net.Uri;
import android.provider.CallLog;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ancun.bean.ContactModel;
import com.ancun.bean.RecentModel;
import com.ancun.core.BaseActivity;
import com.ancun.core.BaseScrollContent;
import com.ancun.service.AppService;
import com.ancun.yzb.R;

public class CallRecordsContentView extends BaseScrollContent implements OnItemClickListener{

	private int lastPosition=-1;
	
	private ListView listview;
	private DataAdapter adapter;
	private List<RecentModel> mListDataItems;

	public CallRecordsContentView(BaseActivity activity) {
		super(activity, R.layout.module_scroll_call_records);
		listview = (ListView) findViewById(R.id.recent_contacts_listview);
		listview.setOnItemClickListener(this);
		
		loadData(true);
	}

	public void loadData(final Boolean flag){
		new Thread() {
			
			public void run() {
				
				mListDataItems = getCurrentActivity().getRecentDaoImpl().findCallRecords();
				getCurrentActivity().runOnUiThread(new Runnable() {
					public void run() {
						if (flag||adapter==null) {
							adapter = new DataAdapter();
							listview.setAdapter(adapter);
						} else {
							adapter.notifyDataSetChanged();
						}
					}
				});
				
			};
			
		}.start();
	}
	
	public class DataAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mListDataItems.size();
		}

		@Override
		public Object getItem(int position) {
			return mListDataItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView,ViewGroup parent) {
			HolderView holder;
			if (convertView == null) {
				holder = new HolderView();
				convertView = getLayoutInflater().inflate(R.layout.lvitem_recent, null);
				holder.call_flag = (ImageView) convertView.findViewById(R.id.iv_call_flag);
				holder.name = (TextView) convertView.findViewById(R.id.tv_name);
				holder.phone = (TextView) convertView.findViewById(R.id.tv_phone);
				holder.phonem = (TextView) convertView.findViewById(R.id.tv_phone_main);
				holder.time = (TextView) convertView.findViewById(R.id.tv_time);
				holder.dial_frame = (LinearLayout) convertView.findViewById(R.id.dial_frame);
				holder.dial_recording = (Button)convertView.findViewById(R.id.dial_recording);
				holder.dial_normal = (Button)convertView.findViewById(R.id.dial_normal);
				convertView.setTag(holder);
			} else {
				holder = (HolderView) convertView.getTag();
			}
			RecentModel recent = mListDataItems.get(position);
			if(recent.getStatus()==CallLog.Calls.INCOMING_TYPE){
				//呼入
				holder.call_flag.setImageResource(R.drawable.ic_call_in);
			}else if(recent.getStatus()==CallLog.Calls.OUTGOING_TYPE){
				//呼出
				holder.call_flag.setImageResource(R.drawable.ic_call_out);
			}else{
				//呼入未接通
				holder.call_flag.setImageResource(R.drawable.ic_call_noin);
			}
			ContactModel contactModel = getCurrentActivity().getContactDaoImpl().getContactModelByPhone(recent.getPhone());
			if(contactModel!=null&&!TextUtils.isEmpty(contactModel.getName())){
				holder.name.setText(contactModel.getName());
				holder.phone.setText(recent.getPhone());
				holder.name.setVisibility(View.VISIBLE);
				holder.phone.setVisibility(View.VISIBLE);
				holder.phonem.setVisibility(View.GONE);
			} else {
				holder.phonem.setText(recent.getPhone());
				holder.name.setVisibility(View.GONE);
				holder.phone.setVisibility(View.GONE);
				holder.phonem.setVisibility(View.VISIBLE);
			}
			holder.time.setText(TimeUtils.customerTimeConvert(recent.getCalltime()));
			holder.dial_recording.setTag(recent);
			holder.dial_recording.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					RecentModel recent=(RecentModel)v.getTag();
//					SharedPreferencesUtils.setCallType(getContext(), 2);
					AppService.inAppDial(getCurrentActivity(),recent.getPhone());
				}
			});
			holder.dial_normal.setTag(recent);
			holder.dial_normal.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					RecentModel recent=(RecentModel)v.getTag();
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ recent.getPhone()));
					getCurrentActivity().startActivity(intent);
				}
			});
			holder.dial_frame.setVisibility(lastPosition==position?View.VISIBLE:View.GONE);
			return convertView;
		}
		
	}
	
	private class HolderView {
		private ImageView call_flag;
		private TextView name;
		private TextView phone;
		private TextView phonem;
		private TextView time;
		private LinearLayout dial_frame;
		private Button dial_recording;
		private Button dial_normal;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		HolderView v=(HolderView)view.getTag();
		v.dial_frame.setVisibility(View.VISIBLE);
		if(lastPosition!=position){
			lastPosition=position;
		}else{
			lastPosition=-1;
		}
		adapter.notifyDataSetChanged();
	}
	
}