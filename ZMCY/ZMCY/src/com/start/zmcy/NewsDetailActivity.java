package com.start.zmcy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.service.SocialService;
import com.umeng.socialize.controller.UMSocialService;

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
			// 首先在您的Activity中添加如下成员变量
			UMSocialService mController = SocialService.socialShare(this, 
					"友盟社会化组件（SDK）让移动应用快速整合社交分享功能，http://www.umeng.com/social",
					"http://www.umeng.com/images/pic/banner_module_social.png");
			 // 是否只有已登录用户才能打开分享选择页
	        mController.openShare(this, false);
		}else{
			super.onClick(v);
		}
	}
	
}
