package com.start.xinkuxue;

import start.utils.StringUtils;
import start.widget.CustomEditText;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.start.core.BaseActivity;
import com.start.service.AppServer;


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
		et_login_account=(CustomEditText)findViewById(R.id.et_login_account);
		et_login_password=(CustomEditText)findViewById(R.id.et_login_password);
		
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			
			String message=bundle.getString(BUNLE_MESSAGE);
			if(!StringUtils.isEmpty(message)){
				getHandlerContext().makeTextLong(message);
				return;
			}
			
		}
		
		Boolean autoLogin=getAppContext().currentUser().getCacheAutoLogin();
		if(autoLogin){
			String account=getAppContext().currentUser().getCacheAccount();
			String password=getAppContext().currentUser().getCachePassword();
			if(!StringUtils.isEmpty(account)&&!StringUtils.isEmpty(password)){
				et_login_account.setText(account);
				et_login_password.setText(password);
				login(account, password, autoLogin);
				return;
			}
		}
		
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_login){
			if(getInputMethodManager().isActive()){
				getInputMethodManager().hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
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
			login(account,AppServer.MD5(password),true);
		}else if(v.getId()==R.id.btn_register){
			getHandlerContext().makeTextLong("暂无注册功能，账号由后台统一开通");
		}else{
			super.onClick(v);
		}
	}
	
	/**
	 * 登录
	 * @param account
	 * @param password MD5加密后的密码
	 * @param autoLogin  
	 */
	private void login(final String account,final String password,final Boolean autoLogin){
//		HttpServer hServer = new HttpServer(Constant.URL.UserLogin,getHandlerContext());
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("userName",account);
//		params.put("pwd", password);
//		hServer.setParams(params);
//		hServer.get(new HttpRunnable() {
//
//			@Override
//			public void run(Response response) throws AppException {
//				
//				try{
//					User.ACCESSKEY=String.valueOf(response.getData("access_token"));
//					Map<String, String> datas = new HashMap<String, String>();
//					JSONObject current=(JSONObject)response.getData("userInfo");
//					JSONArray names = current.names();
//					for (int j = 0; j < names.length(); j++) {
//						String name = names.getString(j);
//						datas.put(name, String.valueOf(current.get(name)));
//					}
//					getAppContext().currentUser().resolve(datas);
//					loginSuccess(account,password,autoLogin);
//				}catch(JSONException e){
//					throw AppException.json(e);
//				}
//				
//			}
//			
//		});
		loginSuccess(account,password,autoLogin);
	}
	
	/**
	 * 登陆成功
	 */
	private void loginSuccess(String account,String password,Boolean autoLogin){
		getAppContext().currentUser().addCacheUser(account, password, autoLogin);
		Intent intent=new Intent(this,MainActivity.class);
		startActivity(intent);
		finish();
	}
	
}
