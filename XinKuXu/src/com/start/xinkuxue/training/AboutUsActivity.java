package com.start.xinkuxue.training;

import android.os.Bundle;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.xinkuxue.R;

/**
 * 关于我们
 * @author zhenyao
 *
 */
public class AboutUsActivity extends BaseActivity{

	private TextView tvTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutus);
		tvTitle=(TextView)findViewById(R.id.tvTitle);
		tvTitle.setText("关于我们");
	}
	
}
