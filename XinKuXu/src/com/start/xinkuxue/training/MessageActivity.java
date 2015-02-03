package com.start.xinkuxue.training;

import android.os.Bundle;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.xinkuxue.R;

/**
 * 留言板
 * @author zhenyao
 *
 */
public class MessageActivity extends BaseActivity{

	private TextView tvTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		tvTitle=(TextView)findViewById(R.id.tvTitle);
		tvTitle.setText("留言板");
	}
	
}
