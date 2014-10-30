package com.ancun.yzb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 网络连接广播
 * @author Start
 *
 */
public class NetCheckReceiver extends BroadcastReceiver{
	
//    //android 中网络变化时所发的Intent的名字
//    public static final String NETACTION="android.net.conn.CONNECTIVITY_CHANGE";
//    
//    private CoreActivity currentActivity;
//    
//    public NetCheckReceiver(CoreActivity activity){
//    	this.currentActivity=activity;
//    }
//    
    @Override
    public void onReceive(Context context, Intent intent) {
//        if(NETACTION.equals(intent.getAction())){
//        	//Intent中ConnectivityManager.EXTRA_NO_CONNECTIVITY这个关键字表示着当前是否连接上了网络
//            if(intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)){
//            	//网络未连接
//            	 currentActivity.sendEmptyMessage(Constant.HANDLER.HANDLERNETCHECKMESSAGEWHATNOCONNECT);
//            }else{
//            	//网络已连接
//            	 currentActivity.sendEmptyMessage(Constant.HANDLER.HANDLERNETCHECKMESSAGEWHATCONNECT);
//            	//初始化推送功能
//            	MessageCenterReceiver.initClientIdToHttp(context);
//            }
//        }
    }
}