package com.ancun.yzb;

import start.core.AppConstant;
import start.core.AppContext;
import start.widget.DialFloatView;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.ancun.core.BaseActivity;
import com.ancun.core.Constant;

public class PhoneReceiver extends BroadcastReceiver {

	private TelephonyManager manager;
	private String SP_CALL_DIAL;
	
    private BaseActivity currentActivity;
    
    public PhoneReceiver(BaseActivity activity){
    	this.currentActivity=activity;
    }
	
	@Override
	public void onReceive(final Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
			SP_CALL_DIAL = AppContext.getSharedPreferences().getString(Constant.Preferences.SP_CALL_DIAL,AppConstant.EMPTYSTR);
			if (!TextUtils.isEmpty(SP_CALL_DIAL)) {
				manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
				manager.listen(stateListener, PhoneStateListener.LISTEN_CALL_STATE);
			}
		}
	}

	/**
	 * 电话状态监听
	 */
	private PhoneStateListener stateListener = new PhoneStateListener() {

		public Boolean IDLE=false;
		private DialFloatView myFV;
		private WindowManager wm;

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_OFFHOOK:
				IDLE=true;
				if (myFV == null) {
					wm = (WindowManager)currentActivity.getAppContext().getSystemService(Context.WINDOW_SERVICE);
					View view = LayoutInflater.from(currentActivity.getAppContext()).inflate(R.layout.common_diallistener, null);
					ImageView ivClose = (ImageView) view.findViewById(R.id.dial_listener_btn_close);
					ivClose.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							// 关闭悬浮窗口
							if (wm != null && myFV != null) {
								wm.removeView(myFV);
								myFV = null;
							}
						}
					});
					TextView tvMessage = (TextView) view.findViewById(R.id.dial_listener_text);
					tvMessage.setText("您正在通过音证宝与"+SP_CALL_DIAL+"录音通话中…");
					myFV = new DialFloatView(currentActivity.getAppContext());
					myFV.addView(view);
					// 设置LayoutParams(全局变量）相关参数
					WindowManager.LayoutParams wmParams = myFV.getMywmParams();
					// 设置window type
					wmParams.type = LayoutParams.TYPE_PHONE;
					// 设置图片格式，效果为背景透明
					wmParams.format = PixelFormat.RGBA_8888;
					// 设置Window flag
					wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
					// 调整悬浮窗口至左上角
					wmParams.gravity = Gravity.LEFT | Gravity.TOP;
					// 以屏幕左上角为原点，设置x、y初始值
					wmParams.x = 0;
					wmParams.y = 0;
					// 设置悬浮窗口长宽数据
					wmParams.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
					wmParams.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
					// 显示myFloatView图像
					wm.addView(myFV, wmParams);
				}
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				//拨打电话状态顺序1.CALL_STATE_IDLE 2.CALL_STATE_OFFHOOK 3.CALL_STATE_IDLE由于只需要取最后一个CALL_STATE_IDLE所以在这里需要一个逻辑判断
				if(IDLE){
					IDLE=false;
					//停止监听手机通话状态
					if(manager!=null){
						manager.listen(stateListener,PhoneStateListener.LISTEN_NONE);
					}
					// 通话结束后关闭悬浮窗口
					if (wm != null && myFV != null) {
						wm.removeView(myFV);
						myFV = null;
					}
					//设置拨打的号码为空
					AppContext.getSharedPreferences().putString(Constant.Preferences.SP_CALL_DIAL,AppConstant.EMPTYSTR);
					ActivityManager am = (ActivityManager)currentActivity.getSystemService(Context.ACTIVITY_SERVICE);
					ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
					if (!cn.getClassName().equals(MainActivity.class.getName())) {
						// Android4.0以上系统默认打完电话后会跳转到
						Intent intent = new Intent(currentActivity,MainActivity.class);						
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						currentActivity.startActivity(intent);
					}
				}
				break;
			}
		}
		
	};
}