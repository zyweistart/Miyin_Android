package com.start.zmcy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import start.core.AppConstant;
import start.core.AppException;
import start.utils.StringUtils;
import start.widget.CustomEditText;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.start.core.BaseActivity;
import com.start.core.Constant;
import com.start.core.Constant.Handler;
import com.start.core.Constant.ResultCode;
import com.start.service.HttpRunnable;
import com.start.service.HttpServer;
import com.start.service.Response;
import com.start.service.SocialService;
import com.start.service.User;


public class LoginActivity extends BaseActivity{
	
	public static final int RESULT_LOGIN_SUCCESS=222;
	
	public static final int QQ_LOGIN=1;
	public static final int WX_LOGIN=2;
	
	/**
	 * 提示信息
	 */
	public static final String BUNLE_MESSAGE="BUNLE_MESSAGE";
	/**
	 * 自动登录标记
	 */
	public static final String BUNLE_AUTOLOGINFLAG="BUNLE_AUTOLOGINFLAG";
	
	private CustomEditText et_login_account;
	private CustomEditText et_login_password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setMainHeadTitle(getString(R.string.login));
		et_login_account=(CustomEditText)findViewById(R.id.et_login_account);
		et_login_password=(CustomEditText)findViewById(R.id.et_login_password);
		
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			
			if(bundle.getBoolean(BUNLE_AUTOLOGINFLAG)){
				String account=getAppContext().currentUser().getCacheAccount();
				if(StringUtils.isEmpty(account)){
					return;
				}
				et_login_account.setText(account);
				Boolean autoLogin=getAppContext().currentUser().getCacheAutoLogin();
				if(autoLogin){
					String password=getAppContext().currentUser().getCachePassword();
					if(StringUtils.isEmpty(password)){
						return;
					}
					et_login_password.setText(password);
					login(account, password, autoLogin);
				}
			}else{
				
				String message=bundle.getString(BUNLE_MESSAGE);
				if(!StringUtils.isEmpty(message)){
					getHandlerContext().makeTextLong(message);
					return;
				}
				
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_login){
			String account=String.valueOf(et_login_account.getText());
			if(StringUtils.isEmpty(account)){
				getHandlerContext().makeTextLong(getString(R.string.accounthint));
				return;
			}
			String password=String.valueOf(et_login_password.getText());
			if(StringUtils.isEmpty(password)){
				getHandlerContext().makeTextLong(getString(R.string.passwordhint));
				return;
			}
			login(account,password,true);
		}else if(v.getId()==R.id.qq_login){
			SocialService.socialQQLogin(this);
		}else if(v.getId()==R.id.wx_login){
			SocialService.socialWexinLogin(this);
		}else if(v.getId()==R.id.txtRegister){
			Intent intent=new Intent(this,RegisterActivity.class);
			startActivityForResult(intent, 0);
		}else{
			super.onClick(v);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void onProcessMessage(Message msg) throws AppException {
		switch(msg.what){
		case ResultCode.USERNAMEORPASSWORDERROR:
			getAppContext().currentUser().clearCachePassword();
			et_login_password.setText(AppConstant.EMPTYSTR);
			getHandlerContext().makeTextShort(String.valueOf(msg.obj));
			break;
		case Handler.HANDLERTHIRDPARTYLANDINGQQ:
			//QQ登陆
			this.registerLogin(QQ_LOGIN,(Map<String,Object>)msg.obj);
			break;
		case Handler.HANDLERTHIRDPARTYLANDINGWX:
			//WX登陆
			this.registerLogin(WX_LOGIN,(Map<String,Object>)msg.obj);
			break;
		default:
			super.onProcessMessage(msg);
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RegisterActivity.LOGINSUCCESS){
			Bundle bundle=data.getExtras();
			String type=bundle.getString(RegisterActivity.REGISTERTYPE);
			if(RegisterActivity.REGISTERTYPE_ACCOUNT.equals(type)){
				String account=bundle.getString(RegisterActivity.STR_ACCOUNT);
				String password=bundle.getString(RegisterActivity.STR_PASSWORD);
				login(account, password, true);
			}else if(RegisterActivity.REGISTERTYPE_QQ.equals(type)){
				this.registerLogin(QQ_LOGIN,bundle);
			}else if(RegisterActivity.REGISTERTYPE_WX.equals(type)){
				this.registerLogin(WX_LOGIN,bundle);
			}
		}
	}

	/**
	 * 登录
	 * @param account
	 * @param password MD5加密后的密码
	 * @param autoLogin  
	 */
	private void login(final String account,final String password,final Boolean autoLogin){
		HttpServer hServer = new HttpServer(Constant.URL.UserLogin,getHandlerContext());
		Map<String, String> params = new HashMap<String, String>();
		params.put("userName",account);
		params.put("pwd", password);
		hServer.setParams(params);
		hServer.get(new HttpRunnable() {

			@Override
			public void run(Response response) throws AppException {
				
				try{
					User.ACCESSKEY=String.valueOf(response.getData("access_token"));
					Map<String, String> datas = new HashMap<String, String>();
					JSONObject current=(JSONObject)response.getData("userInfo");
					JSONArray names = current.names();
					for (int j = 0; j < names.length(); j++) {
						String name = names.getString(j);
						datas.put(name, String.valueOf(current.get(name)));
					}
					getAppContext().currentUser().resolve(datas);
					loginSuccess(account,password,autoLogin);
				}catch(JSONException e){
					throw AppException.json(e);
				}
				
			}
			
		});
	}
	
	/**
	 * 注册登录
	 * @param loginType登录类型1:QQ 2:WX
	 */
	private void registerLogin(int loginType,Map<String,Object> info){
		Bundle bundle=new Bundle();
		Set<String> keys = info.keySet();
		for (String key : keys) {
			bundle.putString(key, String.valueOf(info.get(key)));
		}
		registerLogin(loginType, bundle);
	}
	
	/**
	 * 注册登录
	 * @param loginType登录类型1:QQ 2:WX
	 */
	private void registerLogin(int loginType,Bundle data){
		HttpServer hServer = new HttpServer(Constant.URL.OAuthLogin,getHandlerContext());
		Map<String, String> params = new HashMap<String, String>();
		if(loginType==1){
			params.put("key","LoginQQ");
			params.put("uid",data.getString("uid"));
			params.put("name",data.getString("screen_name"));
			params.put("gender",data.getString("gender"));
			params.put("profile_image_url",data.getString("profile_image_url"));
			params.put("openid",data.getString("openid"));
			params.put("verified",data.getString("verified"));
			params.put("access_token",data.getString("access_token"));
		}else if(loginType==2){
			params.put("key","LoginWX");
			params.put("uid",data.getString("unionid"));
			params.put("name",data.getString("nickname"));
			params.put("sex",data.getString("sex"));
			params.put("headimgurl",data.getString("headimgurl"));
			params.put("openid",data.getString("openid"));
			params.put("language",data.getString("language"));
			params.put("country",data.getString("country"));
			params.put("province",data.getString("province"));
			params.put("city",data.getString("city"));
		}
		hServer.setParams(params);
		hServer.get(new HttpRunnable() {

			@Override
			public void run(Response response) throws AppException {
				try{
					User.ACCESSKEY=String.valueOf(response.getData("access_token"));
					Map<String, String> datas = new HashMap<String, String>();
					JSONObject current=(JSONObject)response.getData("userInfo");
					JSONArray names = current.names();
					for (int j = 0; j < names.length(); j++) {
						String name = names.getString(j);
						datas.put(name, String.valueOf(current.get(name)));
					}
					getAppContext().currentUser().resolve(datas);
					String account=getAppContext().currentUser().getInfo().get("userName");
					String password=getAppContext().currentUser().getInfo().get("pwd");
					loginSuccess(account,password,true);
				}catch(JSONException e){
					throw AppException.json(e);
				}
			}
			
		});
	}
	
	/**
	 * 登陆成功
	 */
	private void loginSuccess(String account,String password,Boolean autoLogin){
		getAppContext().currentUser().addCacheUser(account, password, autoLogin);
		if(getInputMethodManager().isActive()){
			getInputMethodManager().hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		getAppContext().currentUser().setLogin(true);
		if(getAppContext().getCacheActivity().isGotoActivity()){
			getAppContext().getCacheActivity().startActivity(this);
		}else{
			setResult(RESULT_LOGIN_SUCCESS);
		}
		finish();
	}
	
}
