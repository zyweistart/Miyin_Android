package com.ancun.yl.adapter;

import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ancun.core.BaseActivity;
import com.ancun.core.BaseCallListAdapter;
import com.ancun.service.AppService;
import com.ancun.yl.R;

public class ContactAdapter extends BaseCallListAdapter{

	public static final String STRID="id";
	public static final String STRNAME="name";
	public static final String STRPHONE="phone";
	public static final String STRPHONEID="phoneid";
	public static final String STRLOOKUPKEY="LookupKey";
	
	public ContactAdapter(BaseActivity activity) {
		super(activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ContactViewHolder holder;
		if (convertView == null) {
			convertView = mActivity.getLayoutInflater().inflate(R.layout.lvitem_content_contact,null);
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
		Map<String,String> info = getItemDatas().get(position);
		Long id=Long.parseLong(info.get(STRID));
		String name=info.get(STRNAME);
		int phoneid=Integer.parseInt(info.get(STRPHONEID));
		holder.name.setText(name);
		if (phoneid > 0) {
			holder.photo.setImageBitmap(((BaseActivity)mActivity).getContactDaoImpl().loadContactPhoto(id));
		}else{
			holder.photo.setImageResource(R.drawable.ic_head);
		}
		holder.dial_recording.setTag(info);
		holder.dial_recording.setOnClickListener(new OnClickListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				Map<String,String> data = (Map<String,String>)v.getTag();
				dial(data,1);
			}
		});
		holder.dial_normal.setTag(info);
		holder.dial_normal.setOnClickListener(new OnClickListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				Map<String,String> data = (Map<String,String>)v.getTag();
				dial(data,2);
			}
		});
		holder.dial_frame.setVisibility(getLastPosition()==position?View.VISIBLE:View.GONE);
		return convertView;
	}
	
	public class ContactViewHolder {
		public ImageView photo;
		public TextView name;
		public LinearLayout dial_frame;
		public Button dial_recording;
		public Button dial_normal;
		
	}

	private void dial(Map<String,String> data,final int type){
		List<String>phones=((BaseActivity)mActivity).getContactDaoImpl().getContactAllPhone(data.get(STRLOOKUPKEY));
		if(phones.size()>1){
			final String[] phonearr=phones.toArray(new String[phones.size()]);
			new AlertDialog.Builder(mActivity)
			.setSingleChoiceItems(phonearr,1,new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,final int which) {
					String phone=phonearr[which];
					if(type==1){
						AppService.inAppDial((BaseActivity)mActivity,phone);
					}else{
						AppService.call((BaseActivity)mActivity, phone);
					}
					dialog.dismiss();
				}
			}).create().show();
		}else if(phones.size()==1){
			String phone=phones.get(0);
			if(type==1){
				AppService.inAppDial((BaseActivity)mActivity,phone);
			}else{
				AppService.call((BaseActivity)mActivity, phone);
			}
		}else{
			mActivity.getHandlerContext().makeTextLong(mActivity.getString(R.string.nocontactphone));
		}
	}
	
}