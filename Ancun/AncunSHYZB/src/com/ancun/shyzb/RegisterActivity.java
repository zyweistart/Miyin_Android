package com.ancun.shyzb;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import start.core.AppConstant;
import start.core.AppException;
import start.service.HttpRunnable;
import start.service.HttpServer;
import start.service.Response;
import start.utils.MD5;
import start.utils.StringUtils;
import start.utils.TimeUtils;
import start.widget.CustomEditText;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ancun.core.BaseActivity;
import com.ancun.core.Constant;
import com.ancun.core.Constant.Handler;
import com.ancun.service.AppService;

/**
 * 注册
 * @author start
 *
 */
public class RegisterActivity extends BaseActivity {
	
	protected String phone;
	protected String authcode;
	protected String password;
	
	protected Button btn_re_get_checksum;
	protected CustomEditText et_phone;
	protected CustomEditText et_checksum;
	protected CustomEditText et_setting_password;
	protected CustomEditText et_setting_repassword;
	protected LinearLayout ll_main_frame;
	protected LinearLayout ll_first_frame;
	protected LinearLayout ll_second_frame;
	protected TextView success_title;
	protected LinearLayout fr_server;
	protected CheckBox cb_agree;
	protected TextView txt_servercontent;
	protected TextView txt_tip;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_reset_password);
		setMainHeadTitle(getString(R.string.register));
		btn_re_get_checksum=(Button)findViewById(R.id.btn_re_get_checksum);
		et_phone=(CustomEditText)findViewById(R.id.et_phone);
		et_checksum=(CustomEditText)findViewById(R.id.et_checksum);
		et_setting_password=(CustomEditText)findViewById(R.id.et_setting_password);
		et_setting_repassword=(CustomEditText)findViewById(R.id.et_setting_repassword);
		ll_main_frame=(LinearLayout)findViewById(R.id.ll_main_frame);
		ll_first_frame=(LinearLayout)findViewById(R.id.ll_first_frame);
		ll_second_frame=(LinearLayout)findViewById(R.id.ll_second_frame);
		success_title=(TextView)findViewById(R.id.success_title);
		success_title.setText(R.string.register_success);
		
		fr_server=(LinearLayout)findViewById(R.id.fr_server);
		fr_server.setVisibility(View.VISIBLE);
		cb_agree=(CheckBox)findViewById(R.id.cb_agree);
		txt_servercontent=(TextView)findViewById(R.id.txt_servercontent);
		txt_servercontent.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		
		txt_tip=(TextView)findViewById(R.id.txttip);
		setBillingText();
		txt_tip.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onProcessMessage(Message msg) throws AppException {
		switch (msg.what) {
		case Handler.REGISTER_RESET_PASSWORD_STEP1:
			ll_main_frame.setVisibility(View.VISIBLE);
			btn_re_get_checksum.setEnabled(false);
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					int sec=60;
					while(sec>0){
						sec--;
						final int n=sec;
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								if(n==0){
									btn_re_get_checksum.setText(R.string.regetauthcode);
									btn_re_get_checksum.setEnabled(true);
								}else{
									btn_re_get_checksum.setText(n+"秒,后可重新获取验证码");
									btn_re_get_checksum.setEnabled(false);
								}
							}
							
						});
						
						TimeUtils.sleep(1000);
					}
					
				}
				
			}).start();
			
			break;
		case Handler.REGISTER_RESET_PASSWORD_STEP2:
			ll_first_frame.setVisibility(View.GONE);
			ll_second_frame.setVisibility(View.VISIBLE);
			break;
		default:
			super.onProcessMessage(msg);
			break;
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.txt_servercontent){
			Bundle bundle=new Bundle();
			bundle.putInt(WebPageActivity.TYPE, WebPageActivity.TYPE_SERVICE);
			Intent intent=new Intent(this, WebPageActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		} else if(v.getId()==R.id.btn_re_get_checksum){
			phone=String.valueOf(et_phone.getText());
			if(StringUtils.isEmpty(phone)){
				getHandlerContext().makeTextLong(getString(R.string.phoneemptytip));
				return;
			}
			if(!cb_agree.isChecked()){
				getHandlerContext().makeTextLong(getString(R.string.servercontenttip));
				return;
			}
			getAuthCode(1);
		}else if(v.getId()==R.id.btn_next){
			phone=String.valueOf(et_phone.getText());
			if(StringUtils.isEmpty(phone)){
				getHandlerContext().makeTextLong(getString(R.string.phoneemptytip));
				return;
			}
			if(!cb_agree.isChecked()){
				getHandlerContext().makeTextLong(getString(R.string.servercontenttip));
				return;
			}
			authcode=String.valueOf(et_checksum.getText());
			if(StringUtils.isEmpty(authcode)){
				getHandlerContext().makeTextLong(getString(R.string.autocodeemptytip));
				return;
			}
			password=String.valueOf(et_setting_password.getText());
			if(StringUtils.isEmpty(password)){
				getHandlerContext().makeTextLong(getString(R.string.pwdemptytip));
				return ;
			}
			if(!password.equals(String.valueOf(et_setting_repassword.getText()))){
				getHandlerContext().makeTextLong(getString(R.string.twopwddifftip));
				return;
			}
			if(!AppService.passwordCheck(password)){
				getHandlerContext().makeTextLong(getString(R.string.passwordformaterror));
				return;
			}
			HttpServer hServer=new HttpServer(Constant.URL.userSignup, getHandlerContext());
			Map<String,String> headers=new HashMap<String,String>();
			headers.put("sign", AppConstant.EMPTYSTR);
			hServer.setHeaders(headers);
			Map<String,String> params=new HashMap<String,String>();
			params.put("userTel",phone);
			params.put("password", MD5.md5(password));
			params.put("type","1");
			params.put("authcode", authcode);
			params.put("signupsource", "3");
			hServer.setParams(params);
			hServer.get(new HttpRunnable() {
				
				@Override
				public void run(Response response) {
					getHandlerContext().getHandler().sendEmptyMessage(Handler.REGISTER_RESET_PASSWORD_STEP2);
				}
				
			});
		}else if(v.getId()==R.id.btn_done){
			getAppContext().currentUser().addCacheUser(phone, MD5.md5(password), true);
			goLogin(true);
		}else if(v.getId()==R.id.tvHaveAccount){
			goLogin(false);
		}else{
			super.onClick(v);
		}
	}
	
	/**
	 * 获取验证码
	 */
	public void getAuthCode(int type){
		HttpServer hServer=new HttpServer(Constant.URL.authcodeGet, getHandlerContext());
		Map<String,String> headers=new HashMap<String,String>();
		headers.put("sign", AppConstant.EMPTYSTR);
		hServer.setHeaders(headers);
		Map<String,String> params=new HashMap<String,String>();
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
	
	/**
	 * 设置计费文案
	 */
	public void setBillingText(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int year=calendar.get(Calendar.YEAR);
		int month=calendar.get(Calendar.MONTH)+1;
		int day=calendar.get(Calendar.DATE);
		if(day<10){
			//1<R<10 次月1号扣费
			day=TimeUtils.getLastDayOfMonth(calendar.getTime());
		}else{
			//10<=R<=月底 次次月1号扣费
			calendar.set(Calendar.MONTH, month);
			year=calendar.get(Calendar.YEAR);
			month=calendar.get(Calendar.MONTH)+1;
			day=TimeUtils.getLastDayOfMonth(calendar.getTime());
		}
		txt_tip.setText(StringUtils.ToDBC("欢迎使用"+getString(R.string.app_name)+"！对您商务和日常生活中的重要电话进行录音保全，必要时还可以向公证机关申办公证。现在开通"+year+"年"+month+"月"+day+"日前不收取功能费，次月1日起正式收费，每月30元，通话费按原有资费标准收取。"));
	}
	
}