package com.start.xinkuxue;

import java.util.Random;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.start.core.BaseActivity;

/**
 * 测试单词页面A
 * @author start
 *
 */
public class TestWordsPageActivity extends BaseActivity{
	
	//选择区间开始
	public static final String BUNDLE_LEARN_WORDS_START_INDEX="BUNDLE_START_INDEX";
	//选择区间结束
	public static final String BUNDLE_LEARN_WORDS_END_INDEX="BUNDLE_END_INDEX";
	//测试类型
	public static final String BUNDLE_WORDS="BUNDLE_TEST_TYPE";
	//几选1
	public static final String BUNDLE_RANDOM_NUMBER="BUNDLE_RANDOM_NUMBER";
	
	//测试类型(1:中-英 2:英-中 3:图-英 4:英-图 5:填空)
	private int[] mTestType;
	//当前单词的ID，单词游标索引，总测试单词数，开始测试的ID，结束测试的ID，几选1数，总答对数
	private int mCurrentWordId,mAnswerIndex,mAnswerCount,mStartWordID,mEndWordID,mRandomNumber,mRightCount;
	
	private Random rnTestRandom,rnRandom;
	
	private RelativeLayout frame_a,frame_b,frame_c;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_words_page);
		frame_a=(RelativeLayout)findViewById(R.id.frame_a);
		frame_b=(RelativeLayout)findViewById(R.id.frame_b);
		frame_c=(RelativeLayout)findViewById(R.id.frame_c);
		
		mStartWordID=1;
		mEndWordID=500;
		mTestType=new int[]{1,2,3};
		mRandomNumber=5;
		
		rnTestRandom=new Random();
		rnRandom=new Random();
		
		mAnswerCount=(mEndWordID-mStartWordID+1)/mRandomNumber;
		
		mAnswerIndex=0;
		currentWord();
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.frame_a_selector_answer_a){
			
		}else if(v.getId()==R.id.frame_b_selector_answer_a){
			
		}else if(v.getId()==R.id.frame_a_selector_c){
			
		}
		if(mAnswerCount<mAnswerCount){
			
		}
		mAnswerIndex++;
		currentWord();
	}
	
	public void currentWord(){
		mCurrentWordId=mStartWordID+mAnswerIndex*mRandomNumber+rnRandom.nextInt(mRandomNumber);
		int type=mTestType[rnTestRandom.nextInt(mTestType.length)];
		if(type==1){
			frame_a.setVisibility(View.VISIBLE);
			frame_b.setVisibility(View.GONE);
			frame_c.setVisibility(View.GONE);
		}else if(type==2){
			frame_a.setVisibility(View.GONE);
			frame_b.setVisibility(View.VISIBLE);
			frame_c.setVisibility(View.GONE);
		}else if(type==3){
			frame_a.setVisibility(View.GONE);
			frame_b.setVisibility(View.GONE);
			frame_c.setVisibility(View.VISIBLE);
		}else if(type==4){
			
		}else if(type==5){
			
		}
	}
	
}
