package com.ancun.yzb.adapter;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import start.utils.TimeUtils;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ancun.bean.ContactModel;
import com.ancun.core.BaseActivity;
import com.ancun.core.BaseCallListAdapter;
import com.ancun.yzb.MainActivity;
import com.ancun.yzb.R;
import com.ancun.yzb.RecordedDetailActivity;

public class RecordingAdapter extends BaseCallListAdapter{

	public static final String RECORDED_FILENO="fileno";
	public static final String RECORDED_TIME="begintime";
	public static final String RECORDED_REMARK="remark";
	public static final String RECORDED_DURATION="duration";
	public static final String RECORDED_CEFFLAG="cerflag";
	public static final String RECORDED_ACCSTATUS="accstatus";
	
	public static final int REMARKREQUESTCODE=0xAC003;
	
	public RecordingAdapter(BaseActivity activity) {
		super(activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holder;
		if (convertView != null && convertView.getId() == R.id.recordeditemlayout) {
			holder = (HolderView) convertView.getTag();
		} else {
			convertView = mActivity.getLayoutInflater().inflate(R.layout.lvitem_content_recordedmanager2, null);
			holder = new HolderView();
			holder.head = (ImageView) convertView.findViewById(R.id.recorded_main_head_img);
			holder.name = (TextView) convertView.findViewById(R.id.recorded_main_name);
			holder.phone = (TextView) convertView.findViewById(R.id.recorded_main_phone);
			holder.from = (TextView) convertView.findViewById(R.id.recorded_main_from);
			holder.tvDownloadStatus=(TextView)convertView.findViewById(R.id.recorded_main_download_status);
			holder.tvRecendtime=(TextView)convertView.findViewById(R.id.recorded_main_recendtime);
			holder.btnRemark=(ImageButton)convertView.findViewById(R.id.recorded_main_detail_btn_remark);
			holder.btnRemark.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					HolderView vh=(HolderView)v.getTag();
					Intent intent=new Intent();
					Bundle bundle=new Bundle();
					bundle.putString(RECORDED_FILENO,vh.fileno);
					intent.setClass(mActivity, RecordedDetailActivity.class);
					intent.putExtras(bundle);
					mActivity.startActivityForResult(intent,REMARKREQUESTCODE);
				}
			});
			convertView.setTag(holder);
		}
		Map<String,String> data=mItemDatas.get(position);
		ContactModel contactModel=((MainActivity)mActivity).getContactDaoImpl().getContactModelByPhone(data.get("oppno"));
		if(contactModel!=null){
			holder.name.setTag(contactModel.getName());
			holder.name.setText(contactModel.getName());				
			holder.phone.setText(data.get("oppno"));
			if (contactModel.getPhotoID() > 0) {
				holder.head.setImageBitmap(((MainActivity)mActivity).getContactDaoImpl().loadContactPhoto(contactModel.getId()));
			}else{
				holder.head.setImageResource(R.drawable.ic_head);
			}
		}else{
			holder.name.setText(data.get("oppno"));
			holder.name.setTag(data.get("oppno"));
			holder.phone.setText("");
			holder.head.setImageResource(R.drawable.ic_head);
		}
		holder.phone.setTag(data.get("oppno"));
		holder.from.setText(TimeUtils.customerTimeConvert(data.get(RECORDED_TIME)));
		holder.btnRemark.setTag(holder);
		holder.fileno=data.get(RECORDED_FILENO);
		holder.file=new File(Environment.getExternalStorageDirectory().getPath()+"/ancun/record/"+holder.fileno);
		String recendtime=data.get("recendtime");
		holder.tvRecendtime.setVisibility(View.GONE);
		if(!TextUtils.isEmpty(recendtime)){
			try {
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
				SimpleDateFormat sdf2 = new SimpleDateFormat(TimeUtils.yyyyMMddHHmmss_F);
				Date currentDate = sdf2.parse(TimeUtils.getSysTime());
				Date lastDate = sdf2.parse(recendtime);
				long day = (lastDate.getTime()-currentDate.getTime())/24/60/60/1000;
				if (day<=15) {
					String da=sdf1.format(lastDate);
					holder.tvRecendtime.setText(da);
					holder.tvRecendtime.setVisibility(View.VISIBLE);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(holder.file.exists()){
			holder.tvDownloadStatus.setText(TimeUtils.secondConvertTime(Integer.parseInt(data.get(RECORDED_DURATION))));
		}else{
			holder.tvDownloadStatus.setText(R.string.nodnowload);
		}
		if (getLastPosition() == position) {
			convertView.setBackgroundResource(R.drawable.selector_list_item_p);
			holder.name.setTextColor(mActivity.getResources().getColor(R.color.white));
		} else {
			convertView.setBackgroundResource(R.drawable.selector_list_item_n);   
			holder.name.setTextColor(mActivity.getResources().getColor(R.color.listview_bg_p));
		}
		return convertView;
	}
	
	public class HolderView {
		public ImageView head;
		public TextView name;
		public TextView phone;
		public TextView from;
		public TextView tvDownloadStatus;
		public TextView tvRecendtime;
		public ImageButton btnRemark;
		public File file;
		public String fileno;
	}
	
}