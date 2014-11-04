package com.ancun.yzb.adapter;

import java.util.Map;

import start.core.AppListAdapter;
import start.utils.TimeUtils;
import android.provider.CallLog;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ancun.core.BaseActivity;
import com.ancun.service.AppService;
import com.ancun.yzb.R;

public class CallRecordsAdapter extends AppListAdapter{

	public static final String STRID="id";
	public static final String STRSTATUS="status";
	public static final String STRPHONE="phone";
	public static final String STRCALLTIME="calltime";
	
	private int lastPosition=-1;
	
	public CallRecordsAdapter(BaseActivity activity) {
		super(activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holder;
		if (convertView == null) {
			holder = new HolderView();
			convertView = mActivity.getLayoutInflater().inflate(R.layout.lvitem_recent, null);
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
		Map<String,String> data = getItemDatas().get(position);
		int status=Integer.parseInt(data.get(STRSTATUS));
		String phone=data.get(STRPHONE);
		String calltime=data.get(STRCALLTIME);
		if(status==CallLog.Calls.INCOMING_TYPE){
			//呼入
			holder.call_flag.setImageResource(R.drawable.ic_call_in);
		}else if(status==CallLog.Calls.OUTGOING_TYPE){
			//呼出
			holder.call_flag.setImageResource(R.drawable.ic_call_out);
		}else{
			//呼入未接通
			holder.call_flag.setImageResource(R.drawable.ic_call_noin);
		}
		String name=((BaseActivity)mActivity).getContactDaoImpl().getContactName(phone);
		if(TextUtils.isEmpty(name)){
			holder.phonem.setText(phone);
			holder.name.setVisibility(View.GONE);
			holder.phone.setVisibility(View.GONE);
			holder.phonem.setVisibility(View.VISIBLE);
		} else {
			holder.name.setText(name);
			holder.phone.setText(phone);
			holder.name.setVisibility(View.VISIBLE);
			holder.phone.setVisibility(View.VISIBLE);
			holder.phonem.setVisibility(View.GONE);
		}
		holder.time.setText(TimeUtils.customerTimeConvert(calltime));
		holder.dial_recording.setTag(data);
		holder.dial_recording.setOnClickListener(new OnClickListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				Map<String,String> data = (Map<String,String>)v.getTag();
				AppService.inAppDial((BaseActivity)mActivity,data.get(STRPHONE));
			}
		});
		holder.dial_normal.setTag(data);
		holder.dial_normal.setOnClickListener(new OnClickListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				Map<String,String> data = (Map<String,String>)v.getTag();
				AppService.call((BaseActivity)mActivity, data.get(STRPHONE));
			}
		});
		holder.dial_frame.setVisibility(lastPosition==position?View.VISIBLE:View.GONE);
		return convertView;
	}
	
	public class HolderView {
		public ImageView call_flag;
		public TextView name;
		public TextView phone;
		public TextView phonem;
		public TextView time;
		public LinearLayout dial_frame;
		public Button dial_recording;
		public Button dial_normal;
	}
	
	public int getLastPosition(){
		return lastPosition;
	}
	
	public void setLastPosition(int position){
		if(lastPosition!=position){
			this.lastPosition=position;
		}else{
			this.lastPosition=-1;
		}
		this.notifyDataSetChanged();
	}
	
}