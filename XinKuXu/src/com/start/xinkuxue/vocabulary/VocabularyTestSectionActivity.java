package com.start.xinkuxue.vocabulary;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import com.start.xinkuxue.R;

/**
 * 单词区间选择界面
 * @author start
 *
 */
public class VocabularyTestSectionActivity extends BaseActivity{
	
	private LinearLayout frame_test_start1,frame_test_start2;

	private CustomEditText et_test_start_index,et_test_end_index,et_section;
	private CheckBox cb_switch_a,cb_switch_b,cb_switch_c,cb_switch_d,cb_switch_e,cb_switch_f;
	
	private WordService mWordService;
	private Integer mWordCount;
	
	protected TextView tip2;
	protected LinearLayout frame_et_section;
	
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
		frame_test_start1=(LinearLayout)findViewById(R.id.frame_test_start1);
		frame_test_start2=(LinearLayout)findViewById(R.id.frame_test_start2);
		tip2=(TextView)findViewById(R.id.tip2);
		frame_et_section=(LinearLayout)findViewById(R.id.frame_et_section);
		et_test_start_index=(CustomEditText)findViewById(R.id.et_test_start_index);
		et_test_start_index.addTextChangedListener(new CustomTextWatcher());
		et_test_end_index=(CustomEditText)findViewById(R.id.et_test_end_index);
		et_test_end_index.addTextChangedListener(new CustomTextWatcher());
		cb_switch_a=(CheckBox)findViewById(R.id.cb_switch_a);
		cb_switch_b=(CheckBox)findViewById(R.id.cb_switch_b);
		cb_switch_c=(CheckBox)findViewById(R.id.cb_switch_c);
		cb_switch_d=(CheckBox)findViewById(R.id.cb_switch_d);
		cb_switch_e=(CheckBox)findViewById(R.id.cb_switch_e);
		cb_switch_f=(CheckBox)findViewById(R.id.cb_switch_f);
		et_section=(CustomEditText)findViewById(R.id.et_section);
		et_section.addTextChangedListener(new CustomTextWatcher());
		frame_test_start1.setVisibility(View.GONE);
		frame_test_start2.setVisibility(View.VISIBLE);
		et_test_start_index.setText("1");
		et_test_end_index.setText(String.valueOf(mWordCount));
		et_section.setText("1");
//		tip2.setVisibility(View.GONE);
//		frame_et_section.setVisibility(View.GONE);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_test_start2){
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
			if(cb_switch_f.isChecked()){
				indexs.add("6");
			}
			if(indexs.size()<3){
				getHandlerContext().makeTextLong(getString(R.string.testmethodtip));
				return;
			}
			Random rnRandom=new Random();
			//几选一
			Integer se=Integer.parseInt(section);
			//答案总数
			int mAnswerCount=(e-s+1)/se;
			if(se>(e-s+1)){
				getHandlerContext().makeTextLong("几选一不能大于点题目数量");
				return;
			}
			List<String> ids=new ArrayList<String>();
			for(int i=0;i<mAnswerCount;i++){
				ids.add(String.valueOf(s+i*se+rnRandom.nextInt(se)));
			}
			goTestPage(indexs,ids);
		}else{
			super.onClick(v);
		}
	}
	
	public void goTestPage(List<String> indexs,List<String> ids){
		Bundle bundle=new Bundle();
		bundle.putStringArray(VocabularyTestActivity.BUNDLE_WORDS, indexs.toArray(new String[indexs.size()]));
		bundle.putStringArray(VocabularyTestActivity.BUNDLE_ANSWER_ARRAY, ids.toArray(new String[ids.size()]));
		Intent intent=new Intent(this,VocabularyTestActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
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
