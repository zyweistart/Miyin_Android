package com.start.zmcy;

import org.json.JSONException;
import org.json.JSONObject;

import start.core.AppContext;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.igexin.sdk.PushConsts;
import com.start.core.Constant.Preferences;

public class PushNewsReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		int cmd_action=bundle.getInt(PushConsts.CMD_ACTION);
		switch (cmd_action) {
		case PushConsts.GET_MSG_DATA:
			// 获取透传（payload）数据
			byte[] payload = bundle.getByteArray("payload");
			if (payload != null) {
				try {
					/**
					 * JSON格式如下
					 * {
						    "title": "这是标题",
						    "description": "描述内容",
						    "newsid": "新闻的ID"
						}
					 */
					JSONObject jo = new JSONObject(new String(payload));
					
					String title=jo.getString("title");
					String description=jo.getString("description");
					String newsid=jo.getString("newsid");
					
					// 单击通知后会跳转到NewsDetailActivity类  
					Bundle nBundle=new Bundle();
					nBundle.putString(NewsDetailActivity.NEWSID, newsid);
					Intent nIntent=new Intent(context,NewsDetailActivity.class);
					nIntent.putExtras(nBundle);
					
					goNotification(context, title, description, nIntent);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			break;
		case PushConsts.GET_CLIENTID:
			String cid = bundle.getString("clientid");
			AppContext.getSharedPreferences().putString(Preferences.SP_GETUICLIENTID, cid);
			break;
		default:
			break;
		}
	}
	
	@SuppressWarnings("deprecation")
	public void goNotification(Context context,String title,String description,Intent intent){
		NotificationManager nManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();
		// 通知提示
		String tickerText = context.getString(R.string.pushtip); 
		// 显示时间
		long when = System.currentTimeMillis();
		notification.icon = R.drawable.ic_launcher;// 设置通知的图标
		notification.tickerText = tickerText; // 显示在状态栏中的文字
		notification.when = when; // 设置来通知时的时间
		notification.sound = Uri.parse("android.resource://com.sun.alex/raw/dida"); // 自定义声音
		notification.flags = Notification.FLAG_NO_CLEAR; // 点击清除按钮时就会清除消息通知,但是点击通知栏的通知时不会消失
		notification.flags = Notification.FLAG_ONGOING_EVENT; // 点击清除按钮不会清除消息通知,可以用来表示在正在运行
		notification.flags |= Notification.FLAG_AUTO_CANCEL; // 点击清除按钮或点击通知后会自动消失
		notification.flags |= Notification.FLAG_INSISTENT; // 一直进行，比如音乐一直播放，知道用户响应
		notification.defaults = Notification.DEFAULT_SOUND; // 调用系统自带声音
		notification.defaults = Notification.DEFAULT_SOUND;// 设置默认铃声
		notification.defaults = Notification.DEFAULT_VIBRATE;// 设置默认震动
		notification.defaults = Notification.DEFAULT_ALL; // 设置铃声震动
		notification.defaults = Notification.DEFAULT_ALL; // 把所有的属性设置成默认
        // 获取PendingIntent,点击时发送该Intent  
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);  
        // 设置通知的标题和内容  
        notification.setLatestEventInfo(context, title,description, pIntent);  
        // 发出通知  
        nManager.notify(1, notification);
	}
}