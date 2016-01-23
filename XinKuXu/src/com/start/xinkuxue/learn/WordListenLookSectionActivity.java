package com.start.xinkuxue.learn;

import java.util.ArrayList;
import java.util.List;

import start.widget.CustomEditText;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.start.core.BaseActivity;
import com.start.service.WordService;
import com.start.xinkuxue.R;

/**
 * 单词区间选择界面
 * @author start
 *
 */
public class WordListenLookSectionActivity extends BaseActivity{
	
	public static final String TESTSWITCHTYPE="TESTSWITCHTYPE";
	public static final String BUNDLE_START_BUTTON_TITLE="BUNDLE_START_BUTTON_TITLE";

	private int mWordCount,mSwitchType;
	private WordService mWordService;
	
	private LinearLayout frame_test_start1,frame_test_start2;
	private CustomEditText et_word_start_index,et_word_end_index;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_word_section);
		try{
			mWordService=new WordService(this);
		}catch(Exception e){
			getHandlerContext().makeTextLong(e.getMessage());
			finish();
			return;
		}
		mWordCount=mWordService.getWordCount();
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			frame_test_start1=(LinearLayout)findViewById(R.id.frame_test_start1);
			frame_test_start2=(LinearLayout)findViewById(R.id.frame_test_start2);
			mSwitchType=bundle.getInt(TESTSWITCHTYPE);
			et_word_start_index=(CustomEditText)findViewById(R.id.et_word_start_index);
			et_word_end_index=(CustomEditText)findViewById(R.id.et_word_end_index);
			frame_test_start1.setVisibility(View.VISIBLE);
			frame_test_start2.setVisibility(View.GONE);
			et_word_start_index.setText("1");
			et_word_end_index.setText(String.valueOf(mWordCount));
			String name=bundle.getString(BUNDLE_START_BUTTON_TITLE);
			if(name!=null){
				Button startTest=(Button)findViewById(R.id.btn_test_start1);
				startTest.setText(name);
			}
		}else{
			finish();
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_test_start1){
			String s=String.valueOf(et_word_start_index.getText());
			String e=String.valueOf(et_word_end_index.getText());
			if(TextUtils.isEmpty(s)||TextUtils.isEmpty(e)){
				getHandlerContext().makeTextLong("区间不能为空");
				return;
			}
			Integer start=Integer.parseInt(s);
			Integer end=Integer.parseInt(e);
			if(end<=start){
				getHandlerContext().makeTextLong("结束区间必须大于开始区间");
				return;
			}
			if(start<1){
				et_word_start_index.setText("1");
				getHandlerContext().makeTextLong("开始区间必须大于或等于1");
				return;
			}
			if(mWordCount<end){
				et_word_end_index.setText(String.valueOf(mWordCount));
				getHandlerContext().makeTextLong("结束区间不能大于总单词数："+mWordCount);
				return;
			}
			List<String> ids=new ArrayList<String>();
			for(int i=start;i<=end;i++){
				ids.add(String.valueOf(i));
			}
			Bundle bundle=new Bundle();
			bundle.putStringArray(ListenLookWordActivity.BUNDLE_ANSWER_ARRAY, ids.toArray(new String[ids.size()]));
			Intent intent;
			if(mSwitchType==0){
				intent=new Intent(this,ListenLookWordActivity.class);
			}else{
				intent=new Intent(this,Word123Activity.class);
			}
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
		}else{
			super.onClick(v);
		}
	}
	
}
