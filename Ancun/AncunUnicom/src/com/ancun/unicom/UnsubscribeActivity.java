package com.ancun.unicom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import start.core.AppException;
import start.core.AppManager;
import start.service.HttpRunnable;
import start.service.HttpServer;
import start.service.Response;
import start.utils.MD5;
import start.utils.TimeUtils;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.telephony.gsm.SmsManager;
import android.telephony.gsm.SmsMessage;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ancun.core.BaseActivity;
import com.ancun.service.ButtonTextWatcher;
import com.ancun.service.User;

@SuppressWarnings("deprecation")
public class UnsubscribeActivity extends BaseActivity {

	private static final String MESSAGE1="您定制联通宽带公司的安存语录1业务已取消！";
	public static final int SENDMESSAGETIMEOUT=0x4732847;
	
	private EditText et_pwd;
	private Button btn_submit,btn_return;
	
	private SMSRecever mSMSRecever;
	private ProgressDialog mPDialog;
	private SmsObserver mObserver;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsubscribe);
		setMainHeadTitle(getString(R.string.unsubscribe));
		et_pwd =(EditText)findViewById(R.id.et_pwd);
		btn_submit =(Button)findViewById(R.id.btn_submit);
		btn_return =(Button)findViewById(R.id.btn_return);
		et_pwd.addTextChangedListener(new ButtonTextWatcher(btn_submit));
		
//		mSMSRecever=new SMSRecever();
//        IntentFilter filter2=new IntentFilter();
//        filter2.addAction("android.provider.Telephony.SMS_RECEIVED");
//        registerReceiver(mSMSRecever,filter2);
		
		ContentResolver resolver = getContentResolver();
		mObserver = new SmsObserver(resolver, new SmsHandler(this));
		resolver.registerContentObserver(Uri.parse("content://sms"), true,mObserver);
    }

    @Override
	public void onProcessMessage(Message msg) throws AppException {
		switch(msg.what){
			case SENDMESSAGETIMEOUT:
			
			new Thread(new Runnable() {

				@Override
				public void run() {
					//设置超时时间
					int sec = 60;
					while (sec > 0) {
						sec--;
						final int n = sec;
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								if (n == 0) {
									if(mPDialog!=null){
										mPDialog.dismiss();
										mPDialog=null;
									}
									if(mSMSRecever!=null){
										unregisterReceiver(mSMSRecever);
										mSMSRecever=null;
									}
								}
							}

						});

						TimeUtils.sleep(1000);
					}

				}

			}).start();
			break;
		default:
			super.onProcessMessage(msg);
			break;
		}
	}
    
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_submit){
			String password=String.valueOf(et_pwd.getText());
			if(TextUtils.isEmpty(password)){
				getHandlerContext().makeTextLong(getString(R.string.passwordhint));
				return;
			}
			String cp=getAppContext().currentUser().getCachePassword();
			if(!TextUtils.isEmpty(cp)){
				if(!cp.equals(MD5.md5(password))){
					getHandlerContext().makeTextLong("密码不匹配");
					return;
				}
			}
			HttpServer hServer=new HttpServer("unicomWebSmsCancel", getHandlerContext());
			Map<String,String> headers=new HashMap<String,String>();
			headers.put("sign", User.ACCESSKEY);
			hServer.setHeaders(headers);
			Map<String,String> params=new HashMap<String,String>();
			params.put("accessid", User.ACCESSID);
//			params.put("password", MD5.md5(password));
			hServer.setParams(params);
			hServer.get(new HttpRunnable() {
				
				@Override
				public void run(Response response) throws AppException {
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
//							btn_submit.setVisibility(View.GONE);
//							btn_return.setVisibility(View.VISIBLE);
							getHandlerContext().sendEmptyMessage(SENDMESSAGETIMEOUT);
							mPDialog = new ProgressDialog(UnsubscribeActivity.this);
							mPDialog.setMessage(getString(R.string.wait));
							mPDialog.setIndeterminate(true);
							mPDialog.setCancelable(false);
							mPDialog.show();
							
						}
					});
					
				}
				
			});
//			mPDialog = new ProgressDialog(this);
//			mPDialog.setMessage(getString(R.string.wait));
//			mPDialog.setIndeterminate(true);
//			mPDialog.setCancelable(false);
//			mPDialog.show();
//			sendMessage("10655598301","tdac1");
		}else if(v.getId()==R.id.btn_return){
			AppManager.getInstance().finishAllActivity();
		}else{
			super.onClick(v);
		}
	}
    
	public void sendMessage(String phone, String message) {
		// 直接调用短信接口发短信
		SmsManager smsManager = SmsManager.getDefault();
		List<String> divideContents = smsManager.divideMessage(message);
		for (String text : divideContents) {
			smsManager.sendTextMessage(phone, null, text, null, null);
		}
	}
	
	@Override
	protected void onDestroy() {
		if(mSMSRecever!=null){
			unregisterReceiver(mSMSRecever);
		}
		this.getContentResolver().unregisterContentObserver(mObserver);
		super.onDestroy();
	}

	public class SMSRecever extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 获取到短信
			Object[] obj = (Object[]) intent.getExtras().get("pdus");
			for (Object object : obj) {
				SmsMessage sm = SmsMessage.createFromPdu((byte[]) object);
				// 得到信息内容
				String str = sm.getMessageBody();
				if(MESSAGE1.equals(str)){
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							btn_submit.setVisibility(View.GONE);
							btn_return.setVisibility(View.VISIBLE);
							if(mPDialog!=null) {
								mPDialog.dismiss();
								mPDialog=null;
							}
							getAppContext().currentUser().clearCacheUser();
							getHandlerContext().makeTextLong("退订成功");
						}
					});
					abortBroadcast();
				}
			}
		}

	}
	
	
	/**
	 * 短消息观察
	 */
	public class SmsObserver extends ContentObserver {

		private ContentResolver mResolver;
		public SmsHandler smsHandler;

		public SmsObserver(ContentResolver mResolver, SmsHandler handler) {
			super(handler);
			this.mResolver = mResolver;
			this.smsHandler = handler;
		}

		@Override
		public void onChange(boolean selfChange) {
			Cursor mCursor = mResolver.query(Uri.parse("content://sms/inbox"),
					new String[] { "_id", "address", "read", "body","thread_id" }, "read=?", new String[] { "0" },"date desc");
			if (mCursor == null) {
				return;
			} else {
				while (mCursor.moveToNext()) {
					SmsInfo _smsInfo = new SmsInfo();
					int _inIndex = mCursor.getColumnIndex("_id");
					if (_inIndex != -1) {
						_smsInfo._id = mCursor.getString(_inIndex);
					}
					int thread_idIndex = mCursor.getColumnIndex("thread_id");
					if (thread_idIndex != -1) {
						_smsInfo.thread_id = mCursor.getString(thread_idIndex);
					}
					int addressIndex = mCursor.getColumnIndex("address");
					if (addressIndex != -1) {
						_smsInfo.smsAddress = mCursor.getString(addressIndex);
					}
					int bodyIndex = mCursor.getColumnIndex("body");
					if (bodyIndex != -1) {
						_smsInfo.smsBody = mCursor.getString(bodyIndex);
					}
					int readIndex = mCursor.getColumnIndex("read");
					if (readIndex != -1) {
						_smsInfo.read = mCursor.getString(readIndex);
					}
					// 根据你的拦截策略，判断是否不对短信进行操作;将短信设置为已读;将短信删除
					Message msg = smsHandler.obtainMessage();
					// 0不对短信进行操作;1将短信设置为已读;2将短信删除
					_smsInfo.action = 0;
					if(MESSAGE1.equals(_smsInfo.smsBody)){
						btn_submit.setVisibility(View.GONE);
						btn_return.setVisibility(View.VISIBLE);
						if(mPDialog!=null) {
							mPDialog.dismiss();
							mPDialog=null;
						}
						getAppContext().currentUser().clearCacheUser();
						getHandlerContext().makeTextLong("退订成功");
						_smsInfo.action = 2;
					}
					msg.obj = _smsInfo;
					smsHandler.sendMessage(msg);
				}
			}
			if (mCursor != null) {
				mCursor.close();
				mCursor = null;
			}
		}
	}

	public class SmsHandler extends android.os.Handler {
		
		private Context mcontext;

		public SmsHandler(Context context) {
			this.mcontext = context;
		}

		@Override
		public void handleMessage(Message msg) {
			SmsInfo smsInfo = (SmsInfo) msg.obj;
			if (smsInfo.action == 1) {
				ContentValues values = new ContentValues();
				values.put("read", "1");
				mcontext.getContentResolver().update(Uri.parse("content://sms/inbox"), values,"thread_id=?", new String[] { smsInfo.thread_id });
			} else if (smsInfo.action == 2) {
				Uri mUri = Uri.parse("content://sms/");
				mcontext.getContentResolver().delete(mUri, "_id=?",new String[] {smsInfo._id});
			}
		}
	}

	public class SmsInfo {
		public String _id = "";
		public String thread_id = "";
		public String smsAddress = "";
		public String smsBody = "";
		public String read = "";
		public int action = 0;// 1代表设置为已读，2表示删除短信
	}
	
}