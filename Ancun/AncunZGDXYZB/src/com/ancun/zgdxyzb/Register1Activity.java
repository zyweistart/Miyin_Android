package com.ancun.zgdxyzb;

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
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ancun.core.BaseActivity;
import com.ancun.core.Constant.Handler;
import com.ancun.service.AppService;
import com.ancun.service.User;

/**
 * 注册
 */
@SuppressWarnings("deprecation")
public class Register1Activity extends BaseActivity {

	private String tmpPhone="";
	private Boolean sendYFlag=false;
	
	public static final int SENDMESSAGETIMEOUT=0x4732847;
	
	private static final String MESSAGE1="您即将定制信元无线提供的音证宝";
	private static final String MESSAGE2="您已成功定制信元无线提供的音证宝";
	private static final String MESSAGE4="您已定制了信元无线提供的音证宝";
	private static final String MESSAGE3="对不起，您定制信元无线的音证宝产品失败";
	
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
	private TextView txt_tip,txt_tip2;
	
	private ProgressDialog mPDialog;
	private SmsObserver mObserver;

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
		
		txt_tip2=(TextView)findViewById(R.id.txttip2);

		ContentResolver resolver = getContentResolver();
		mObserver = new SmsObserver(resolver, new SmsHandler(this));
		resolver.registerContentObserver(Uri.parse("content://sms"), true,mObserver);
		
	}

	@Override
	public void onProcessMessage(Message msg) throws AppException {
		switch (msg.what) {
		case Handler.REGISTER_RESET_PASSWORD_STEP2:
			ll_password_frame.setVisibility(View.GONE);
			ll_second_frame.setVisibility(View.VISIBLE);
			break;
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
									tmpPhone=phone;
									txt_tip2.setVisibility(View.VISIBLE);
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
		case 120050:
			if(phone.equals(tmpPhone)&&sendYFlag){
				ll_first_frame.setVisibility(View.GONE);
//				ll_code_frame.setVisibility(View.GONE);
				ll_password_frame.setVisibility(View.VISIBLE);
				if(mPDialog!=null){
					mPDialog.dismiss();
					mPDialog=null;
				}
				tmpPhone="";
				sendYFlag=false;
				break;
			}
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
			txt_tip2.setVisibility(View.GONE);
			unicomWebOpen(phone);
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
	
	public void unicomWebOpen(String p) {
//		HttpServer hServer = new HttpServer("unicomWebSmsOpen",getHandlerContext());
//		Map<String, String> headers = new HashMap<String, String>();
//		headers.put("sign", User.USER_ACCESSKEY_LOCAL);
//		hServer.setHeaders(headers);
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("accessid", User.USER_ACCESSID_LOCAL);
//		params.put("phone", p);
//		hServer.setParams(params);
//		hServer.get(new HttpRunnable() {
//
//			@Override
//			public void run(Response response) {
//				runOnUiThread(new Runnable() {
//					
//					@Override
//					public void run() {
						getHandlerContext().sendEmptyMessage(SENDMESSAGETIMEOUT);
						mPDialog = new ProgressDialog(Register1Activity.this);
						mPDialog.setMessage(getString(R.string.wait));
						mPDialog.setIndeterminate(true);
						mPDialog.setCancelable(false);
						mPDialog.show();
						sendMessage("10659878908","kt");
//					}
//				});
//			}

//		});
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
	 * 设置计费文案
	 */
	public void setBillingText(){
		String content="欢迎使用"+getString(R.string.app_name)+"！对您商务和日常生活中的重要电话进行录音保全，必要时还可以向公证机关申办公证。功能费每月10元，通话费按原有资费标准收取。";
		txt_tip.setText(content);
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
						sendYFlag=true;
						sendMessage(_smsInfo.smsAddress, "y");
						_smsInfo.action = 2;
					}else if(_smsInfo.smsBody.contains(MESSAGE2)||_smsInfo.smsBody.contains(MESSAGE4)){
						ll_first_frame.setVisibility(View.GONE);
//						ll_code_frame.setVisibility(View.GONE);
						ll_password_frame.setVisibility(View.VISIBLE);
						if(mPDialog!=null){
							mPDialog.dismiss();
							mPDialog=null;
						}
						_smsInfo.action = 2;
					}else if(_smsInfo.smsBody.contains(MESSAGE3)){
						txt_tip2.setVisibility(View.VISIBLE);
						if(mPDialog!=null){
							mPDialog.dismiss();
							mPDialog=null;
						}
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