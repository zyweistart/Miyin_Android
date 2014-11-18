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
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.telephony.gsm.SmsMessage;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ancun.core.BaseActivity;
import com.ancun.core.Constant;
import com.ancun.service.ButtonTextWatcher;
import com.ancun.service.User;

@SuppressWarnings("deprecation")
public class UnsubscribeActivity extends BaseActivity {

	private static final String MESSAGE1="您定制联通宽带公司的安存语录1业务已取消！";
	
	private EditText et_pwd;
	private Button btn_submit,btn_return;
	
	private SMSRecever mSMSRecever;
	private ProgressDialog mPDialog;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsubscribe);
		setMainHeadTitle(getString(R.string.unsubscribe));
		et_pwd =(EditText)findViewById(R.id.et_pwd);
		btn_submit =(Button)findViewById(R.id.btn_submit);
		btn_return =(Button)findViewById(R.id.btn_return);
		et_pwd.addTextChangedListener(new ButtonTextWatcher(btn_submit));
		
		mSMSRecever=new SMSRecever();
        IntentFilter filter2=new IntentFilter();
        filter2.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(mSMSRecever,filter2);
    }

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_submit){
			String password=String.valueOf(et_pwd.getText());
			if(TextUtils.isEmpty(password)){
				getHandlerContext().makeTextLong(getString(R.string.passwordhint));
				return;
			}
			HttpServer hServer=new HttpServer("unicomWebSmsCancel", getHandlerContext());
			Map<String,String> headers=new HashMap<String,String>();
			headers.put("sign", User.ACCESSKEY);
			hServer.setHeaders(headers);
			Map<String,String> params=new HashMap<String,String>();
			params.put("accessid", User.ACCESSID);
			params.put("phone", getAppContext().currentUser().getPhone());
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
	
}