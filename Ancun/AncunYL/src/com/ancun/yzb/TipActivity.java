package com.ancun.yzb;

import start.core.AppConstant;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ancun.core.BaseActivity;

public class TipActivity extends BaseActivity {
	
	private static final String WEBURL = "file:///android_asset/html/tip.html";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LayoutInflater layoutInflater = LayoutInflater.from(this);

		LinearLayout layout = new LinearLayout(this);
		layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		layout.setBackgroundColor(R.color.activity_gb);
		layout.setOrientation(LinearLayout.VERTICAL);

		View navigation = layoutInflater.inflate(R.layout.module_header, null);
		layout.addView(navigation);
		((TextView) layout.findViewById(R.id.main_head_title)).setText(R.string.tip);

		WebView webView = new WebView(this);
		webView.getSettings().setDefaultTextEncodingName(AppConstant.ENCODE);
		webView.loadUrl(WEBURL);
		layout.addView(webView, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		setContentView(layout);
	}
}