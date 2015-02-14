package com.start.xinkuxue.vocabulary;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.core.Constant;
import com.start.service.WordService;
import com.start.service.bean.WordItem;
import com.start.xinkuxue.R;

/**
 * 词汇测试界面
 * 
 * @author start
 *
 */
public class VocabularyLevelTestActivity extends BaseActivity {

	public static final String BUNDLE_ANSWER_ARRAY = "BUNDLE_ANSWER_ARRAY";

	private WordService mWordService;

	private TextView txt_current_word_process;
	private TextView problem_words,problem_tip;
	private ImageView countdown;
	private Random rnTestRandom;
	private int mCurrentWordId;
	private int mCurrentRightWordItemIndex;
	private WordItem mCurrentRightWordItem;

	private int[] countdownimg={
			R.drawable.countdown0,
			R.drawable.countdown1,
			R.drawable.countdown2,
			R.drawable.countdown3,
			R.drawable.countdown4,
			R.drawable.countdown5,
			R.drawable.countdown6,
			R.drawable.countdown7,
			R.drawable.countdown8,
			R.drawable.countdown9,
			R.drawable.countdown10,
			R.drawable.countdown11,
			R.drawable.countdown12};

	//题目列表
	private String[] mAnswerArray;
	//总答对数
	private int mRightCount;
	//当前题目索引
	private int mCurrentWordIndex;
	
	private Button btn_answer_2,btn_answer_3,btn_answer_4,btn_answer_5,btn_answer_6;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vocabulary_level_test);
		txt_current_word_process = (TextView) findViewById(R.id.txt_current_word_process);
		problem_words = (TextView) findViewById(R.id.problem_words);
		problem_tip = (TextView) findViewById(R.id.problem_tip);
		btn_answer_2 = (Button) findViewById(R.id.btn_answer_2);
		btn_answer_3 = (Button) findViewById(R.id.btn_answer_3);
		btn_answer_4 = (Button) findViewById(R.id.btn_answer_4);
		btn_answer_5 = (Button) findViewById(R.id.btn_answer_5);
		btn_answer_6 = (Button) findViewById(R.id.btn_answer_6);
		countdown=(ImageView)findViewById(R.id.countdown);
		try {
			mWordService = new WordService(this);
		} catch (Exception e) {
			getHandlerContext().makeTextLong(e.getMessage());
			finish();
			return;
		}
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			rnTestRandom = new Random();
			mAnswerArray=bundle.getStringArray(BUNDLE_ANSWER_ARRAY);
			mCurrentWordIndex=0;
			showWord();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		mCountDownTimer.cancel();
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_answer_1){
			nextWord(-1);
		}else if(v.getId()==R.id.btn_answer_2){
			nextWord(0);
		}else if(v.getId()==R.id.btn_answer_3){
			nextWord(1);
		}else if(v.getId()==R.id.btn_answer_4){
			nextWord(2);
		}else if(v.getId()==R.id.btn_answer_5){
			nextWord(3);
		}else if(v.getId()==R.id.btn_answer_6){
			nextWord(4);
		}
	}
	
	public void nextWord(int rightIndex){
		if(mCurrentRightWordItemIndex==rightIndex){
			mRightCount++;
		}
		mCurrentWordIndex++;
		if(mCurrentWordIndex>=mAnswerArray.length){
			mCountDownTimer.cancel();
			Bundle bundle=new Bundle();
			bundle.putInt(VocabularyLevelTestGoResultsActivity.BUNDLE_ANSWER_COUNT,mAnswerArray.length);
			bundle.putInt(VocabularyLevelTestGoResultsActivity.BUNDLE_ANSWER_RIGHTCOUNT,mRightCount);
			Intent intent=new Intent(this,VocabularyLevelTestGoResultsActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
		}else{
			showWord();
		}
	}
	
	public void showWord() {
		mCountDownTimer.cancel();
		mCountDownTimer.start();
		txt_current_word_process.setText("测试进度："+(mCurrentWordIndex+1)+"/"+mAnswerArray.length);
		problem_tip.setText("当前第"+(mCurrentWordIndex+1)+"个单词");
		mCurrentWordId=Integer.parseInt(mAnswerArray[mCurrentWordIndex]);
		mCurrentRightWordItem=mWordService.findById(mCurrentWordId);
		problem_words.setText(mCurrentRightWordItem.getEnglishName());
		// 加载随机答案前3后3中随机查找
		List<WordItem> answerWordItems = getRandomWordItem();
		List<WordItem> sortWordItems = sortWordItem(answerWordItems);
		// 获取正确的答案
		for (int i = 0; i < sortWordItems.size(); i++) {
			WordItem tmpWord = sortWordItems.get(i);
			if (mCurrentRightWordItem.getId().equals(tmpWord.getId())) {
				mCurrentRightWordItemIndex=i;
				break;
			}
		}
		btn_answer_2.setText(sortWordItems.get(0).getChineseSignificance());
		btn_answer_3.setText(sortWordItems.get(1).getChineseSignificance());
		btn_answer_4.setText(sortWordItems.get(2).getChineseSignificance());
		btn_answer_5.setText(sortWordItems.get(3).getChineseSignificance());
		btn_answer_6.setText(sortWordItems.get(4).getChineseSignificance());
	}

	/**
	 * 获取随机题目
	 * 
	 * @return
	 */
	public List<WordItem> getRandomWordItem() {
		List<WordItem> answerWordItems = new ArrayList<WordItem>();
		// 正确答案
		answerWordItems.add(mCurrentRightWordItem);
		Integer wordCount = mWordService.getWordCount();
		while (true) {
			int rnvalue = rnTestRandom.nextInt(wordCount) + 1;
			WordItem tmpWord = mWordService.findById(rnvalue);
			if (tmpWord == null) {
				continue;
			}
			boolean flag = false;
			for (WordItem wi : answerWordItems) {
				if (wi.getId().equals(tmpWord.getId())) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				// 不包含则添加
				answerWordItems.add(tmpWord);
				if (answerWordItems.size() >= 5) {
					// 至少加满4题为止
					break;
				}
			}
		}
		return answerWordItems;
	}

	/**
	 * 随机打乱题目顺序
	 * 
	 * @return
	 */
	public List<WordItem> sortWordItem(List<WordItem> answerWordItems) {
		List<WordItem> sortWordItems = new ArrayList<WordItem>();
		while (!answerWordItems.isEmpty()) {
			int index = rnTestRandom.nextInt(answerWordItems.size());
			sortWordItems.add(answerWordItems.get(index));
			answerWordItems.remove(index);
		}
		return sortWordItems;
	}
	
	private CountDownTimer mCountDownTimer=new CountDownTimer(Constant.TOTALSECOND,Constant.SECOND) {
		
		@Override
		public void onTick(long millisUntilFinished) {
			int n=(int)millisUntilFinished / Constant.SECOND;
			countdown.setImageResource(countdownimg[n]);
		}
		
		@Override
		public void onFinish() {
			nextWord(-1);
		}
	};
	
}
