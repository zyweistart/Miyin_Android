package com.start.service.adapter;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.start.core.Constant;
import com.start.core.CoreActivity;
import com.start.model.UIRunnable;
import com.start.navigation.R;
import com.start.service.PullListViewData;

public class FriendRelationAdapter extends PullListViewData.DataAdapter{
	
	private CoreActivity mActivity;
	private PullListViewData mPullListViewData;
	
	public FriendRelationAdapter(CoreActivity activity,PullListViewData pullListViewData) {
		pullListViewData.super();
		this.mActivity=activity;
		this.mPullListViewData=pullListViewData;
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
			holder.btnRemove = (ImageButton) convertView.findViewById(R.id.lvitem_friend_remove);
			holder.btnRemove.setTag(holder);
			holder.btnRemove.setVisibility(View.VISIBLE);
			holder.btnRemove.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					final FriendRelationViewHolder vh=(FriendRelationViewHolder)v.getTag();
					if(vh!=null){
						
						new AlertDialog.Builder(mActivity)
						.setIcon(android.R.drawable.ic_dialog_info)
						.setMessage("解除关系后，好友将无法定位到你的位置")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								
								Map<String,String> requestParams=new HashMap<String,String>();
								requestParams.put("account", vh.data.get("email"));
								requestParams.put("flag", "2");
								mActivity.getHttpService().exeNetRequest(Constant.ServerAPI.ufriendoDeal,requestParams,null,new UIRunnable() {
									
									@Override
									public void run() {
										
										mActivity.makeTextLong(R.string.msg_relieve_friend_relation_success);
										
										mActivity.runOnUiThread(new Runnable() {
											
											@Override
											public void run() {
												
												mPullListViewData.getPulllistview().clickRefresh();
												
											}
											
										});
										
									}
									
								});
								
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
		ImageButton btnRemove;
	}
	
}
