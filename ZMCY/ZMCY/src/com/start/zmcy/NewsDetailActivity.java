package com.start.zmcy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.start.core.BaseActivity;

/**
 * 新闻详细页
 */
public class NewsDetailActivity extends BaseActivity{
	
	private TextView mHeadChildTitle;
	private Button mHeadMore;
	
	public static final String NEWSID="NEWSID";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_detail);
		setMainHeadTitle(getString(R.string.newsdetail));
		mHeadMore=(Button)findViewById(R.id.head_more);
		mHeadMore.setVisibility(View.VISIBLE);
		mHeadChildTitle=(TextView)findViewById(R.id.head_child_title);
		mHeadChildTitle.setVisibility(View.VISIBLE);
		
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			String id=bundle.getString(NEWSID);
			mHeadChildTitle.setText(id+"评");
		}else{
			finish();
		}
 	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.head_more){
			
		}else{
			super.onClick(v);
		}
	}
	
}
