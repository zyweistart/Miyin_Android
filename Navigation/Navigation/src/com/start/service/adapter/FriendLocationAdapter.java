package com.start.service.adapter;

import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.start.navigation.AppContext;
import com.start.navigation.MainActivity;
import com.start.navigation.R;
import com.start.service.PullListViewData;

public class FriendLocationAdapter extends PullListViewData.DataAdapter{
		
		private MainActivity mActivity;
		private PullListViewData mPullListViewData;
		private AppContext mAppContext;
	
		public FriendLocationAdapter(MainActivity activity,PullListViewData pullListViewData) {
			pullListViewData.super();
			this.mActivity=activity;
			this.mPullListViewData=pullListViewData;
			this.mAppContext=AppContext.getInstance();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FriendRelationViewHolder holder;
			if (convertView != null && convertView.getId() == R.id.lvitem_friend_content) {
				holder = (FriendRelationViewHolder) convertView.getTag();
			}else{
				convertView = mActivity.getLayoutInflater().inflate(R.layout.lvitem_friend, null);
				holder = new FriendRelationViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.lvitem_friend_name);
				holder.btnLocation = (ImageButton) convertView.findViewById(R.id.lvitem_friend_location);
				holder.btnLocation.setTag(holder);
				holder.btnLocation.setVisibility(View.VISIBLE);
				holder.btnLocation.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						final FriendRelationViewHolder vh=(FriendRelationViewHolder)v.getTag();
						if(vh!=null){
							
							new AlertDialog.Builder(mActivity)
							.setIcon(android.R.drawable.ic_dialog_info)
							.setMessage(R.string.msg_sure_navigation_to_friend_location)
							.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									Map<String,String> data=vh.data;
									String mapId=data.get("submapno");
									String latitude=data.get("latitude");
									String longitude=data.get("longitude");
									if(!TextUtils.isEmpty(mapId)&&!TextUtils.isEmpty(latitude)&&!TextUtils.isEmpty(longitude)){
										mActivity.location(mapId,latitude,longitude);
									}else{
										mAppContext.makeTextLong(R.string.msg_location_unavailable);
									}
								}
							}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									dialog.dismiss();
								}
							}).show();
							
						}
					}
					
				});
				convertView.setTag(holder);
			}
			holder.data=mPullListViewData.getDataItemList().get(position);
			holder.name.setText(holder.data.get("email"));
			return convertView;
		}
		
		public class FriendRelationViewHolder {
			Map<String,String> data;
			TextView name;
			ImageButton btnLocation;
		}
		
	}