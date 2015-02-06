package com.start.xinkuxue.training;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import start.core.AppException;
import start.widget.CustomEditText;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.core.Constant;
import com.start.service.HttpRunnable;
import com.start.service.HttpServer;
import com.start.service.Response;
import com.start.service.User;
import com.start.xinkuxue.R;

/**
 * 设置
 * @author zhenyao
 *
 */
public class SettingActivity extends BaseActivity{

	private int type;
	private TextView tvTitle,tvaboutus,tvpersonalinfo,tvothersetting;
	private LinearLayout personal_frame;
	private WebView mWebView;
	private CustomEditText et_account,et_password,et_age,et_classes,et_englishlevel;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		tvTitle=(TextView)findViewById(R.id.tvTitle);
		tvTitle.setText("设置");
		tvaboutus=(TextView)findViewById(R.id.tvaboutus);
		tvpersonalinfo=(TextView)findViewById(R.id.tvpersonalinfo);
		tvothersetting=(TextView)findViewById(R.id.tvothersetting);
		personal_frame=(LinearLayout)findViewById(R.id.personal_frame);
		
		et_account=(CustomEditText)findViewById(R.id.et_account);
		et_password=(CustomEditText)findViewById(R.id.et_password);
		et_age=(CustomEditText)findViewById(R.id.et_age);
		et_classes=(CustomEditText)findViewById(R.id.et_classes);
		et_englishlevel=(CustomEditText)findViewById(R.id.et_englishlevel);
		
		mWebView = (WebView) findViewById(R.id.wvcontent);
		mWebView.getSettings().setJavaScriptEnabled(true);

		mWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				view.loadUrl(url);
				return true;
			}
		});
		
		type=0;
		setEnabledByIndex();
		mWebView.loadUrl("http://www.baidu.com");
		
		et_account.setText(getAppContext().currentUser().getCacheAccount());
		et_age.setText(getAppContext().currentUser().getInfo().get("age"));
		et_classes.setText(getAppContext().currentUser().getInfo().get("Bclass"));
		et_englishlevel.setText(getAppContext().currentUser().getInfo().get("EnglishLevel"));
		
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.tvaboutus){
			type=0;
			setEnabledByIndex();
		}else if(v.getId()==R.id.tvpersonalinfo){
			type=1;
			setEnabledByIndex();
			HttpServer hServer = new HttpServer(Constant.URL.RefreshUser,getHandlerContext());
			Map<String, String> params = new HashMap<String, String>();
			params.put("access_token", User.ACCESSKEY);
			hServer.setParams(params);
			hServer.get(new HttpRunnable() {

				@Override
				public void run(Response response) throws AppException {
					try{
						Map<String, String> datas = new HashMap<String, String>();
						JSONObject current=(JSONObject)response.getData("userInfo");
						JSONArray names = current.names();
						for (int j = 0; j < names.length(); j++) {
							String name = names.getString(j);
							datas.put(name, String.valueOf(current.get(name)));
						}
						getAppContext().currentUser().getInfo().clear();
						getAppContext().currentUser().resolve(datas);
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								et_account.setText(getAppContext().currentUser().getCacheAccount());
								et_age.setText(getAppContext().currentUser().getInfo().get("age"));
								et_classes.setText(getAppContext().currentUser().getInfo().get("Bclass"));
								et_englishlevel.setText(getAppContext().currentUser().getInfo().get("EnglishLevel"));
							}
						});
					}catch(JSONException e){
						throw AppException.json(e);
					}
				}
			});
		}else if(v.getId()==R.id.tvothersetting){
			type=2;
			setEnabledByIndex();
		}else if(v.getId()==R.id.btn_submit){
			String password=String.valueOf(et_password.getText());
			String age=String.valueOf(et_age.getText());
			String classes=String.valueOf(et_classes.getText());
			String englishlevel=String.valueOf(et_englishlevel.getText());
			HttpServer hServer = new HttpServer(Constant.URL.UpdateUser,getHandlerContext());
			Map<String, String> params = new HashMap<String, String>();
			params.put("access_token", User.ACCESSKEY);
			params.put("password", password);
			params.put("age", age);
			params.put("Bclass", classes);
			params.put("EnglishLevel", englishlevel);
			hServer.setParams(params);
			hServer.get(new HttpRunnable() {

				@Override
				public void run(Response response) throws AppException {
					
					getHandlerContext().makeTextLong("修改信息成功");
					
				}
				
			});
		}
	}
	
	public void setEnabledByIndex(){
		tvaboutus.setEnabled(type==0?false:true);
		mWebView.setVisibility(type==0?View.VISIBLE:View.GONE);
		tvpersonalinfo.setEnabled(type==1?false:true);
		personal_frame.setVisibility(type==1?View.VISIBLE:View.GONE);
		tvothersetting.setEnabled(type==2?false:true);
//		xlv_listview_3.setVisibility(type==2?View.VISIBLE:View.GONE);
	}
	
}
