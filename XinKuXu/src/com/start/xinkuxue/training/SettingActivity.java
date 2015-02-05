package com.start.xinkuxue.training;

import start.widget.CustomEditText;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.start.core.BaseActivity;
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
		
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.tvaboutus){
			type=0;
			setEnabledByIndex();
		}else if(v.getId()==R.id.tvpersonalinfo){
			type=1;
			setEnabledByIndex();
		}else if(v.getId()==R.id.tvothersetting){
			type=2;
			setEnabledByIndex();
		}else if(v.getId()==R.id.btn_submit){
			String account=String.valueOf(et_account.getText());
			String password=String.valueOf(et_password.getText());
			String age=String.valueOf(et_age.getText());
			String classes=String.valueOf(et_classes.getText());
			String englishlevel=String.valueOf(et_englishlevel.getText());
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
