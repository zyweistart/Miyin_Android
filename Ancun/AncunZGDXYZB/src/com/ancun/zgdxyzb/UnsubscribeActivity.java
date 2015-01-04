package com.ancun.zgdxyzb;

import java.util.List;

import start.core.AppException;
import start.core.AppManager;
import start.utils.MD5;
import start.utils.TimeUtils;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.telephony.gsm.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ancun.core.BaseActivity;
import com.ancun.service.ButtonTextWatcher;

@SuppressWarnings("deprecation")
public class UnsubscribeActivity extends BaseActivity {

	private static final String MESSAGE1="您已成功取消信元无线提供的音证宝";
	public static final int SENDMESSAGETIMEOUT=0x4732847;
	
	private EditText et_pwd;
	private Button btn_submit;
	
	private LinearLayout ll_first_frame,ll_second_frame;
	
	private ProgressDialog mPDialog;
	private SmsObserver mObserver;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsubscribe);
		setMainHeadTitle(getString(R.string.unsubscribe));
		et_pwd =(EditText)findViewById(R.id.et_pwd);
		btn_submit =(Button)findViewById(R.id.btn_submit);
		et_pwd.addTextChangedListener(new ButtonTextWatcher(btn_submit));
		
		ll_first_frame =(LinearLayout)findViewById(R.id.ll_first_frame);
		ll_second_frame =(LinearLayout)findViewById(R.id.ll_second_frame);
		
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
			mPDialog = new ProgressDialog(this);
			mPDialog.setMessage(getString(R.string.wait));
			mPDialog.setIndeterminate(true);
			mPDialog.setCancelable(false);
			mPDialog.show();
			sendMessage("106598789","td");
			getHandlerContext().sendEmptyMessage(SENDMESSAGETIMEOUT);
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
		this.getContentResolver().unregisterContentObserver(mObserver);
		super.onDestroy();
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
					if(_smsInfo.smsBody.contains(MESSAGE1)){
						ll_first_frame.setVisibility(View.GONE);
						ll_second_frame.setVisibility(View.VISIBLE);
						if(mPDialog!=null) {
							mPDialog.dismiss();
							mPDialog=null;
						}
						getAppContext().currentUser().clearCacheUser();
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