package com.start.xinkuxue;

import java.util.ArrayList;
import java.util.List;

import start.utils.StringUtils;
import start.widget.CustomEditText;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.service.WordService;

/**
 * 单词测试选择界面
 * @author start
 *
 */
public class LearnWordsSwitchTestActivity extends BaseActivity{
	
	public static final String TESTSWITCHTYPE="TESTSWITCHTYPE";
	
	private LinearLayout frame_test_start1,frame_test_start2;

	private CustomEditText et_word_start_index,et_word_end_index,et_test_start_index,et_test_end_index,et_section;
	private CheckBox cb_switch_a,cb_switch_b,cb_switch_c,cb_switch_d,cb_switch_e;
	
	private WordService mWordService;
	private Long mWordCount;
	
	private TextView tip2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_learn_words_switch_test);
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
			if(bundle.getInt(TESTSWITCHTYPE)==0){
				et_word_start_index=(CustomEditText)findViewById(R.id.et_word_start_index);
				et_word_end_index=(CustomEditText)findViewById(R.id.et_word_end_index);
				frame_test_start1.setVisibility(View.VISIBLE);
				frame_test_start2.setVisibility(View.GONE);
				et_word_start_index.setText("1");
				et_word_end_index.setText(String.valueOf(mWordCount));
			}else{
				tip2=(TextView)findViewById(R.id.tip2);
				et_test_start_index=(CustomEditText)findViewById(R.id.et_test_start_index);
				et_test_start_index.addTextChangedListener(new CustomTextWatcher());
				et_test_end_index=(CustomEditText)findViewById(R.id.et_test_end_index);
				et_test_end_index.addTextChangedListener(new CustomTextWatcher());
				cb_switch_a=(CheckBox)findViewById(R.id.cb_switch_a);
				cb_switch_b=(CheckBox)findViewById(R.id.cb_switch_b);
				cb_switch_c=(CheckBox)findViewById(R.id.cb_switch_c);
				cb_switch_d=(CheckBox)findViewById(R.id.cb_switch_d);
				cb_switch_e=(CheckBox)findViewById(R.id.cb_switch_e);
				et_section=(CustomEditText)findViewById(R.id.et_section);
				et_section.addTextChangedListener(new CustomTextWatcher());
				frame_test_start1.setVisibility(View.GONE);
				frame_test_start2.setVisibility(View.VISIBLE);
				et_test_start_index.setText("1");
				et_test_end_index.setText(String.valueOf(mWordCount));
				et_section.setText("1");
			}
		}else{
			finish();
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_test_start1){
			Integer start=Integer.parseInt(String.valueOf(et_word_start_index.getText()));
			Integer end=Integer.parseInt(String.valueOf(et_word_end_index.getText()));
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
			Bundle bundle=new Bundle();
			bundle.putInt(LearnWordsListenLookActivity.BUNDLE_LEARN_WORDS_START_INDEX, start);
			bundle.putInt(LearnWordsListenLookActivity.BUNDLE_LEARN_WORDS_END_INDEX, end);
			Intent intent=new Intent(this,LearnWordsListenLookActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
		}else if(v.getId()==R.id.btn_test_start2){
			String start=String.valueOf(et_test_start_index.getText());
			String end=String.valueOf(et_test_end_index.getText());
			if(TextUtils.isEmpty(start)||TextUtils.isEmpty(end)){
				getHandlerContext().makeTextLong("区间不能为空");
				return;
			}
			int s=Integer.parseInt(start);
			int e=Integer.parseInt(end);
			if(s>=e){
				getHandlerContext().makeTextLong("结束区间必须大于开始区间");
				return;
			}
			if(s<1){
				et_test_start_index.setText("1");
				getHandlerContext().makeTextLong("开始区间必须大于或等于1");
				return;
			}
			if(mWordCount<e){
				et_test_end_index.setText(String.valueOf(mWordCount));
				getHandlerContext().makeTextLong("结束区间不能大于总单词数："+mWordCount);
				return;
			}
			String section=String.valueOf(et_section.getText());
			if(TextUtils.isEmpty(section)){
				et_section.setText("1");
				getHandlerContext().makeTextLong("几选1不能为空");
				return;
			}
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
				getHandlerContext().makeTextLong("测试方法必须大于等于3种");
				return;
			}
			Bundle bundle=new Bundle();
			bundle.putInt(TestWordsPageActivity.BUNDLE_LEARN_WORDS_START_INDEX, Integer.parseInt(start));
			bundle.putInt(TestWordsPageActivity.BUNDLE_LEARN_WORDS_END_INDEX, Integer.parseInt(end));
			bundle.putStringArray(TestWordsPageActivity.BUNDLE_WORDS, indexs.toArray(new String[indexs.size()]));
			bundle.putInt(TestWordsPageActivity.BUNDLE_RANDOM_NUMBER, Integer.parseInt(section));
			Intent intent=new Intent(this,TestWordsPageActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
		}else{
			super.onClick(v);
		}
	}
	
	private class CustomTextWatcher implements TextWatcher {

		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			
		}

		public void afterTextChanged(Editable s) {
			
		}

		public void onTextChanged(CharSequence s, int start, int before,int count) {
			Integer ss=StringUtils.toInt(String.valueOf(et_test_start_index.getText()), 1);
			if(ss<1){
				return;
			}
			Integer ee=StringUtils.toInt(String.valueOf(et_test_end_index.getText()), 1);
			if(ee<1){
				return;
			}
			Integer section=StringUtils.toInt(String.valueOf(et_section.getText()), 1);
			if(section<1){
				return;
			}
			Integer num=ee-ss+1;
			tip2.setText("测试自动挑选"+num+"个词汇量里面，以"+section+"选1，随机产生"+(num/section)+"道测试，检测你的单词掌握量");
		}

	}
	
}
