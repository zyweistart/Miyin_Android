package com.start.xinkuxueoffline.strange;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.start.core.BaseActivity;
import com.start.service.bean.StrangeWordItem;
import com.start.xinkuxueoffline.BaseContext;
import com.start.xinkuxueoffline.R;

/**
 * 生词本测试方法选择
 * @author zhenyao
 *
 */
public class StrangeWordsTestTypeSwitchActivity extends BaseActivity{

	public static final String BUNDLE_TYPE="BUNDLE_TYPE";
	public static final String BUNDLE_JOINTIME="BUNDLE_JOINTIME";
	
	protected Bundle mBundle;
	
	private String mJoinTime,mType;
	
	private CheckBox cb_switch_a,cb_switch_b,cb_switch_c,cb_switch_d,cb_switch_e;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_strange_words_test_type_switch);
		cb_switch_a=(CheckBox)findViewById(R.id.cb_switch_a);
		cb_switch_b=(CheckBox)findViewById(R.id.cb_switch_b);
		cb_switch_c=(CheckBox)findViewById(R.id.cb_switch_c);
		cb_switch_d=(CheckBox)findViewById(R.id.cb_switch_d);
		cb_switch_e=(CheckBox)findViewById(R.id.cb_switch_e);
		mBundle=getIntent().getExtras();
		if(mBundle!=null){
			mJoinTime=mBundle.getString(BUNDLE_JOINTIME);
			mType=mBundle.getString(BUNDLE_TYPE);
		}else{
			finish();
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_test_start){
			List<String> indexs=new ArrayList<String>();
			if(cb_switch_a.isChecked()){
				indexs.add("1");
			}
			if(cb_switch_b.isChecked()){
				indexs.add("2");
			}
			if(cb_switch_c.isChecked()){
				indexs.add("3");
			}
			if(cb_switch_d.isChecked()){
				indexs.add("4");
			}
			if(cb_switch_e.isChecked()){
				indexs.add("5");
			}
			if(indexs.size()<3){
				getHandlerContext().makeTextLong(getString(R.string.testmethodtip));
				return;
			}
			List<StrangeWordItem> words=BaseContext.getDBManager().findAllByStrangeWordItem(getAppContext().currentUser().getCacheAccount(),mJoinTime,mType);
			List<String> ids=new ArrayList<String>();
			for(StrangeWordItem s :words){
				ids.add(s.getIndex());
			}
			Bundle bundle=new Bundle();
			bundle.putString(StrangeWordsTestActivity.BUNDLE_TYPE, mType);
			bundle.putStringArray(StrangeWordsTestActivity.BUNDLE_WORDS, indexs.toArray(new String[indexs.size()]));
			bundle.putStringArray(StrangeWordsTestActivity.BUNDLE_ANSWER_ARRAY, ids.toArray(new String[ids.size()]));
			Intent intent=new Intent(this,StrangeWordsTestActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
		}else{
			super.onClick(v);
		}
	}
	
}
