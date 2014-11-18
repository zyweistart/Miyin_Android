package com.ancun.unicom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import start.core.AppException;
import start.service.HttpRunnable;
import start.service.HttpServer;
import start.service.Response;
import start.utils.MD5;
import start.utils.StringUtils;
import start.utils.TimeUtils;
import start.widget.CustomEditText;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Message;
import android.telephony.gsm.SmsManager;
import android.telephony.gsm.SmsMessage;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ancun.core.BaseActivity;
import com.ancun.core.Constant;
import com.ancun.core.Constant.Handler;
import com.ancun.service.AppService;
import com.ancun.service.User;

/**
 * 注册
 * 
 * @author start 获取校验码 发送ac1至10655598301 提交校验码 发送y 收到开通成功进入设置密码
 *         您申请的联通宽带公司的安存语录1业务（10元/月），请直接回复Y生效。
 *         您已成功定制联通宽带公司(10655598301)的安存语录1业务，发送tdac1到10655598301退订业务，查询热线10010。
 */
@SuppressWarnings("deprecation")
public class Register1Activity extends BaseActivity {

	private static final String MESSAGE1="您申请的联通宽带公司的安存语录1业务（10元/月），请直接回复Y生效。";
	private static final String MESSAGE2="您已成功定制联通宽带公司(10655598301)的安存语录1业务，发送tdac1到10655598301退订业务，查询热线10010。";
	private static final String MESSAGE3="【安存网络】用户注册，您正在免费注册安存语录全国首个录音公证电话，验证码";
	private static final String MESSAGE4="请及时输入。如非本人操作，请致电95105857";
	private String checksum;
	protected String phone;
	protected String authcode;
	protected String password;

	protected Button btn_re_get_checksum;
	protected Button btn_zre_get_checksum;
	protected Button btn_submit_checksum;
	protected CustomEditText et_phone;
	protected CustomEditText et_checksum;
	protected CustomEditText et_setting_password;
	protected CustomEditText et_setting_repassword;
	protected LinearLayout ll_first_frame;
	protected LinearLayout ll_second_frame;
	protected LinearLayout ll_code_frame;
	protected LinearLayout ll_password_frame;

	protected TextView success_title;
	protected LinearLayout fr_server;
	protected CheckBox cb_agree;
	protected TextView txt_servercontent;
	private TextView txt_tip;
	
	private SMSRecever mSMSRecever;
	
	private ProgressDialog mPDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registertwo_reset_password);
		setMainHeadTitle(getString(R.string.register));
		btn_re_get_checksum = (Button) findViewById(R.id.btn_re_get_checksum);
		btn_zre_get_checksum = (Button) findViewById(R.id.btn_zre_get_checksum);
		btn_submit_checksum = (Button) findViewById(R.id.btn_submit_checksum);
		et_phone = (CustomEditText) findViewById(R.id.et_phone);
		et_checksum = (CustomEditText) findViewById(R.id.et_checksum);
		et_setting_password = (CustomEditText) findViewById(R.id.et_setting_password);
		et_setting_repassword = (CustomEditText) findViewById(R.id.et_setting_repassword);
		ll_first_frame = (LinearLayout) findViewById(R.id.ll_first_frame);
		ll_second_frame = (LinearLayout) findViewById(R.id.ll_second_frame);
		ll_code_frame = (LinearLayout) findViewById(R.id.ll_code_frame);
		ll_password_frame = (LinearLayout) findViewById(R.id.ll_password_frame);
		success_title = (TextView) findViewById(R.id.success_title);
		success_title.setText(R.string.register_success);

		fr_server = (LinearLayout) findViewById(R.id.fr_server);
		fr_server.setVisibility(View.VISIBLE);
		cb_agree = (CheckBox) findViewById(R.id.cb_agree);
		txt_servercontent = (TextView) findViewById(R.id.txt_servercontent);
		txt_servercontent.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		
		txt_tip=(TextView)findViewById(R.id.txttip);
		setBillingText();
		txt_tip.setVisibility(View.VISIBLE);
		
		mSMSRecever=new SMSRecever();
        IntentFilter filter2=new IntentFilter();
        filter2.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(mSMSRecever,filter2);
	}

	@Override
	public void onProcessMessage(Message msg) throws AppException {
		switch (msg.what) {
		case Handler.REGISTER_RESET_PASSWORD_STEP1:
			ll_first_frame.setVisibility(View.GONE);
			ll_code_frame.setVisibility(View.VISIBLE);
			btn_zre_get_checksum.setEnabled(false);

			new Thread(new Runnable() {

				@Override
				public void run() {
					int sec = 60;
					while (sec > 0) {
						sec--;
						final int n = sec;
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								if (n == 0) {
									btn_zre_get_checksum
											.setText(R.string.regetauthcode);
									btn_zre_get_checksum.setEnabled(true);
								} else {
									btn_zre_get_checksum.setText(n
											+ "秒,后可重新获取验证码");
									btn_zre_get_checksum.setEnabled(false);
								}
							}

						});

						TimeUtils.sleep(1000);
					}

				}

			}).start();

			break;
		case Handler.REGISTER_RESET_PASSWORD_STEP2:
			ll_password_frame.setVisibility(View.GONE);
			ll_second_frame.setVisibility(View.VISIBLE);
			break;
		default:
			super.onProcessMessage(msg);
			break;
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.txt_servercontent) {
			Bundle bundle = new Bundle();
			bundle.putInt(WebPageActivity.TYPE, WebPageActivity.TYPE_SERVICE);
			Intent intent = new Intent(this, WebPageActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		} else if (v.getId() == R.id.btn_re_get_checksum) {
			phone = String.valueOf(et_phone.getText());
			if (StringUtils.isEmpty(phone)) {
				getHandlerContext().makeTextLong(
						getString(R.string.phoneemptytip));
				return;
			}
			if (!cb_agree.isChecked()) {
				getHandlerContext().makeTextLong(
						getString(R.string.servercontenttip));
				return;
			}
			unicomWebOpen(phone);
//			getAuthCode(1);
		} else if (v.getId() == R.id.btn_zre_get_checksum) {
			getAuthCode(1);
		} else if (v.getId() == R.id.btn_submit_checksum) {
			authcode = String.valueOf(et_checksum.getText());
			if (StringUtils.isEmpty(authcode)) {
				getHandlerContext().makeTextLong(
						getString(R.string.autocodeemptytip));
				return;
			}
			if(!authcode.equals(checksum)){
				getHandlerContext().makeTextLong("验证码不正确");
				return;
			}
			mPDialog = new ProgressDialog(this);
			mPDialog.setMessage(getString(R.string.wait));
			mPDialog.setIndeterminate(true);
			mPDialog.setCancelable(false);
			mPDialog.show();
			sendMessage("10655598301","ac1");
		} else if (v.getId() == R.id.btn_next) {
			password = String.valueOf(et_setting_password.getText());
			if (StringUtils.isEmpty(password)) {
				getHandlerContext().makeTextLong(
						getString(R.string.pwdemptytip));
				return;
			}
			if (!password
					.equals(String.valueOf(et_setting_repassword.getText()))) {
				getHandlerContext().makeTextLong(
						getString(R.string.twopwddifftip));
				return;
			}
			if (!AppService.passwordCheck(password)) {
				getHandlerContext().makeTextLong(
						getString(R.string.passwordformaterror));
				return;
			}
			HttpServer hServer = new HttpServer("ylcnuserPwdSet_app",getHandlerContext());
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("sign", User.USER_ACCESSKEY_LOCAL);
			hServer.setHeaders(headers);
			Map<String, String> params = new HashMap<String, String>();
			params.put("accessid", User.USER_ACCESSID_LOCAL);
			params.put("phone", phone);
			params.put("password", MD5.md5(password));
			hServer.setParams(params);
			hServer.get(new HttpRunnable() {

				@Override
				public void run(Response response) {
					getHandlerContext().getHandler().sendEmptyMessage(Handler.REGISTER_RESET_PASSWORD_STEP2);
				}

			});
		} else if (v.getId() == R.id.btn_done) {
			getAppContext().currentUser().addCacheUser(phone,
					MD5.md5(password), true);
			goLogin(true);
		} else if (v.getId() == R.id.tvHaveAccount) {
			goLogin(false);
		} else {
			super.onClick(v);
		}
	}

	/**
	 * 获取验证码
	 */
	public void getAuthCode(int type) {
		HttpServer hServer = new HttpServer(Constant.URL.authcodeGet,getHandlerContext());
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("sign", User.USER_ACCESSKEY_LOCAL);
		hServer.setHeaders(headers);
		Map<String, String> params = new HashMap<String, String>();
		params.put("accessid", User.USER_ACCESSID_LOCAL);
		params.put("userTel", phone);
		params.put("actype", String.valueOf(type));
		hServer.setParams(params);
		hServer.get(new HttpRunnable() {

			@Override
			public void run(Response response) {
				getHandlerContext().getHandler().sendEmptyMessage(Handler.REGISTER_RESET_PASSWORD_STEP1);
			}

		});
	}
	
	public void unicomWebOpen(String p) {
		HttpServer hServer = new HttpServer("unicomWebSmsOpen",getHandlerContext());
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("sign", User.USER_ACCESSKEY_LOCAL);
		hServer.setHeaders(headers);
		Map<String, String> params = new HashMap<String, String>();
		params.put("accessid", User.USER_ACCESSID_LOCAL);
		params.put("phone", p);
		hServer.setParams(params);
		hServer.get(new HttpRunnable() {

			@Override
			public void run(Response response) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						mPDialog = new ProgressDialog(Register1Activity.this);
						mPDialog.setMessage(getString(R.string.wait));
						mPDialog.setIndeterminate(true);
						mPDialog.setCancelable(false);
						mPDialog.show();
					}
				});
			}

		});
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
				final String str = sm.getMessageBody();
				//得到手机地址
				if(MESSAGE1.equals(str)){
					String address = sm.getOriginatingAddress();
					sendMessage(address, "y");
					abortBroadcast();
				}else if(MESSAGE2.equals(str)){
					runOnUiThread(new Runnable() {
						public void run() {
							ll_first_frame.setVisibility(View.GONE);
//							ll_code_frame.setVisibility(View.GONE);
							ll_password_frame.setVisibility(View.VISIBLE);
							if(mPDialog!=null){
								mPDialog.dismiss();
							}
						}
					});
					abortBroadcast();
				}else{
					if(str.contains(MESSAGE3)&&str.contains(MESSAGE4)){
				    	
				    	runOnUiThread(new Runnable() {
							public void run() {
								checksum=str.substring(MESSAGE3.length(),MESSAGE3.length()+6);
								et_checksum.setText(checksum);
							}
				    	});
				    	
				    }
				}
			}
		}

	}
	
	/**
	 * 设置计费文案
	 */
	public void setBillingText(){
		String content="欢迎使用"+getString(R.string.app_name)+"！对您商务和日常生活中的重要电话进行录音保全，必要时还可以向公证机关申办公证。功能费每月10元，通话费按原有资费标准收取。";
		txt_tip.setText(StringUtils.ToDBC(content));
	}

}