package com.start.xinkuxue;

import java.io.File;
import java.util.Random;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.service.WordService;
import com.start.service.bean.WordItem;

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
	private String[] mTestType;
	//当前单词的ID，单词游标索引，总测试单词数，开始测试的ID，结束测试的ID，几选1数，总答对数
	private int mCurrentWordId,mAnswerIndex,mAnswerCount,mStartWordID,mEndWordID,mRandomNumber,mRightCount;
	
	private Random rnTestRandom,rnRandom;
	
	private WordService mWordService;
	private WordItem mWordItem;
	
	private ImageView problem_picture;
	private TextView problem_words,problem_sentence;
	private LinearLayout answer_frame_text;
	private RelativeLayout answer_frame_picture;
	
	private TextView frame_text_selector_answer_a,frame_text_selector_answer_b,frame_text_selector_answer_c,frame_text_selector_answer_d;
	private TextView frame_picture_selector_answer_a,frame_picture_selector_answer_b,frame_picture_selector_answer_c,frame_picture_selector_answer_d;
	private TextView frame_text_selector_answer_cannotskip,frame_picture_selector_answer_cannotskip;
	
	private String mTitle,mAName,mBName,mCName,mDName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_words_page);
		problem_picture=(ImageView)findViewById(R.id.problem_picture);
		problem_words=(TextView)findViewById(R.id.problem_words);
		problem_sentence=(TextView)findViewById(R.id.problem_sentence);
		answer_frame_text=(LinearLayout)findViewById(R.id.answer_frame_text);
		answer_frame_picture=(RelativeLayout)findViewById(R.id.answer_frame_picture);
		
		frame_text_selector_answer_a=(TextView)findViewById(R.id.frame_text_selector_answer_a);
		frame_text_selector_answer_b=(TextView)findViewById(R.id.frame_text_selector_answer_b);
		frame_text_selector_answer_c=(TextView)findViewById(R.id.frame_text_selector_answer_c);
		frame_text_selector_answer_d=(TextView)findViewById(R.id.frame_text_selector_answer_d);
		frame_text_selector_answer_cannotskip=(TextView)findViewById(R.id.frame_text_selector_answer_cannotskip);
		frame_picture_selector_answer_a=(TextView)findViewById(R.id.frame_picture_selector_answer_a);
		frame_picture_selector_answer_b=(TextView)findViewById(R.id.frame_picture_selector_answer_b);
		frame_picture_selector_answer_c=(TextView)findViewById(R.id.frame_picture_selector_answer_c);
		frame_picture_selector_answer_d=(TextView)findViewById(R.id.frame_picture_selector_answer_d);
		frame_picture_selector_answer_cannotskip=(TextView)findViewById(R.id.frame_picture_selector_answer_cannotskip);
		
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			mStartWordID=bundle.getInt(BUNDLE_LEARN_WORDS_START_INDEX);
			mEndWordID=bundle.getInt(BUNDLE_LEARN_WORDS_END_INDEX);
			mTestType=bundle.getStringArray(BUNDLE_WORDS);
			mRandomNumber=bundle.getInt(BUNDLE_RANDOM_NUMBER);
			
			rnTestRandom=new Random();
			rnRandom=new Random();
			
			mAnswerCount=(mEndWordID-mStartWordID+1)/mRandomNumber;
			
			mWordService=new WordService("");
			
			mAnswerIndex=0;
			currentWord();
		}else{
			finish();
		}
		
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.frame_text_selector_answer_a){
			setStyleTextViewRight(frame_text_selector_answer_a);
		}else if(v.getId()==R.id.frame_text_selector_answer_b){
			setStyleTextViewError(frame_text_selector_answer_b);
		}else if(v.getId()==R.id.frame_text_selector_answer_c){
			
		}else if(v.getId()==R.id.frame_text_selector_answer_d){
			
		}else if(v.getId()==R.id.frame_text_selector_answer_cannotskip){
			joinWords();
		}else if(v.getId()==R.id.frame_picture_selector_answer_a){
			
		}else if(v.getId()==R.id.frame_picture_selector_answer_b){
			
		}else if(v.getId()==R.id.frame_picture_selector_answer_c){
			
		}else if(v.getId()==R.id.frame_picture_selector_answer_d){

		}else if(v.getId()==R.id.frame_picture_selector_answer_cannotskip){
			joinWords();
		}
		frame_text_selector_answer_a.setEnabled(false);
		frame_text_selector_answer_b.setEnabled(false);
		frame_text_selector_answer_c.setEnabled(false);
		frame_text_selector_answer_d.setEnabled(false);
		frame_text_selector_answer_cannotskip.setEnabled(false);
		frame_picture_selector_answer_a.setEnabled(false);
		frame_picture_selector_answer_b.setEnabled(false);
		frame_picture_selector_answer_c.setEnabled(false);
		frame_picture_selector_answer_d.setEnabled(false);
		frame_picture_selector_answer_cannotskip.setEnabled(false);
		getHandlerContext().getHandler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				if(mAnswerIndex>=mAnswerCount){
					Intent intent=new Intent(TestWordsPageActivity.this,TestWordsPageDoneActivity.class);
					startActivity(intent);
					finish();
				}else{
					frame_text_selector_answer_cannotskip.setEnabled(true);
					frame_picture_selector_answer_cannotskip.setEnabled(true);
					currentWord();
				}
			}
		}, 1500);
	}
	
	public void currentWord(){
		mCurrentWordId=mStartWordID+mAnswerIndex*mRandomNumber+rnRandom.nextInt(mRandomNumber);
		
		mWordItem=mWordService.findById(mCurrentWordId);
		int type=4;
//		int type=Integer.parseInt(mTestType[rnTestRandom.nextInt(mTestType.length)]);
		if(type==1){
			mTitle=mWordItem.getChineseSignificance();
			mAName="A、"+mWordItem.getEnglishName();
			mBName="B、"+mWordItem.getEnglishName();
			mCName="C、"+mWordItem.getEnglishName();
			mDName="D、"+mWordItem.getEnglishName();
			wordToText();
		}else if(type==2){
			mTitle=mWordItem.getEnglishName();
			mAName="A、"+mWordItem.getChineseSignificance();
			mBName="B、"+mWordItem.getChineseSignificance();
			mCName="C、"+mWordItem.getChineseSignificance();
			mDName="D、"+mWordItem.getChineseSignificance();
			wordToText();
		}else if(type==3){
			mTitle=mWordItem.getId();
			mAName="A、"+mWordItem.getEnglishName();
			mBName="B、"+mWordItem.getEnglishName();
			mCName="C、"+mWordItem.getEnglishName();
			mDName="D、"+mWordItem.getEnglishName();
			pictureToText();
		}else if(type==4){
			mTitle=mWordItem.getEnglishName();
			mAName=mWordItem.getId();
			mBName="2";
			mCName="3";
			mDName="4";
			wordToPicture();
		}else if(type==5){
			mTitle=mWordItem.getFillProblem();
			mAName="A、"+mWordItem.getFillAnswer();
			mBName="B、"+mWordItem.getFillAnswer();
			mCName="C、"+mWordItem.getFillAnswer();
			mDName="D、"+mWordItem.getFillAnswer();
			textToText();
		}
		mAnswerIndex++;
	}
	
	public void wordToText(){
		//显示隐藏
		problem_picture.setVisibility(View.GONE);
		problem_words.setVisibility(View.VISIBLE);
		problem_sentence.setVisibility(View.GONE);
		answer_frame_text.setVisibility(View.VISIBLE);
		answer_frame_picture.setVisibility(View.GONE);
		//设置样式
		setStyleTextViewNormal(frame_text_selector_answer_a);
		setStyleTextViewNormal(frame_text_selector_answer_b);
		setStyleTextViewNormal(frame_text_selector_answer_c);
		setStyleTextViewNormal(frame_text_selector_answer_d);
		//显示内容
		problem_words.setText(mTitle);
		frame_text_selector_answer_a.setText(mAName);
		frame_text_selector_answer_b.setText(mBName);
		frame_text_selector_answer_c.setText(mCName);
		frame_text_selector_answer_d.setText(mDName);
	}
	
	@SuppressWarnings("deprecation")
	public void wordToPicture(){
		//显示隐藏
		problem_picture.setVisibility(View.GONE);
		problem_words.setVisibility(View.VISIBLE);
		problem_sentence.setVisibility(View.GONE);
		answer_frame_text.setVisibility(View.GONE);
		answer_frame_picture.setVisibility(View.VISIBLE);
		//设置样式、显示内容
		problem_words.setText(mTitle);
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		String mAPicturePath = mWordService.getExampleImagePath(mAName);
		if(new File(mAPicturePath).exists()){
			setStyleTextViewPictureNormal(frame_picture_selector_answer_a,new BitmapDrawable(BitmapFactory.decodeFile(mAPicturePath, options)));
		}
		String mBPicturePath = mWordService.getExampleImagePath(mAName);
		if(new File(mBPicturePath).exists()){
			setStyleTextViewPictureNormal(frame_picture_selector_answer_b,new BitmapDrawable(BitmapFactory.decodeFile(mBPicturePath, options)));
		}
		String mCPicturePath = mWordService.getExampleImagePath(mAName);
		if(new File(mCPicturePath).exists()){
			setStyleTextViewPictureNormal(frame_picture_selector_answer_c,new BitmapDrawable(BitmapFactory.decodeFile(mCPicturePath, options)));
		}
		String mDPicturePath = mWordService.getExampleImagePath(mAName);
		if(new File(mDPicturePath).exists()){
			setStyleTextViewPictureNormal(frame_picture_selector_answer_d,new BitmapDrawable(BitmapFactory.decodeFile(mDPicturePath, options)));
		}
	}
	
	public void textToText(){
		//显示隐藏
		problem_picture.setVisibility(View.GONE);
		problem_words.setVisibility(View.GONE);
		problem_sentence.setVisibility(View.VISIBLE);
		answer_frame_text.setVisibility(View.VISIBLE);
		answer_frame_picture.setVisibility(View.GONE);
		//设置样式
		setStyleTextViewNormal(frame_text_selector_answer_a);
		setStyleTextViewNormal(frame_text_selector_answer_b);
		setStyleTextViewNormal(frame_text_selector_answer_c);
		setStyleTextViewNormal(frame_text_selector_answer_d);
		//显示内容
		problem_sentence.setText(mTitle);
		frame_text_selector_answer_a.setText(mAName);
		frame_text_selector_answer_b.setText(mBName);
		frame_text_selector_answer_c.setText(mCName);
		frame_text_selector_answer_d.setText(mDName);
	}
	
	public void pictureToText(){
		//显示隐藏
		problem_picture.setVisibility(View.VISIBLE);
		problem_words.setVisibility(View.GONE);
		problem_sentence.setVisibility(View.GONE);
		answer_frame_text.setVisibility(View.VISIBLE);
		answer_frame_picture.setVisibility(View.GONE);
		//设置样式
		setStyleTextViewNormal(frame_text_selector_answer_a);
		setStyleTextViewNormal(frame_text_selector_answer_b);
		setStyleTextViewNormal(frame_text_selector_answer_c);
		setStyleTextViewNormal(frame_text_selector_answer_d);
		//显示内容
		String myJpgPath = mWordService.getExampleImagePath(mTitle);
		File dbFile=new File(myJpgPath);
		if(dbFile.exists()){
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			problem_picture.setImageBitmap(BitmapFactory.decodeFile(myJpgPath, options));
		}
		frame_text_selector_answer_a.setText(mAName);
		frame_text_selector_answer_b.setText(mBName);
		frame_text_selector_answer_c.setText(mCName);
		frame_text_selector_answer_d.setText(mDName);
	}
	
	@SuppressWarnings("deprecation")
	public void setStyleTextViewNormal(TextView tv){
		tv.setEnabled(true);
		tv.setTextAppearance(this, R.style.test_words_page_text_selector);
		tv.setCompoundDrawables(null, null, null, null);
		int _pL = tv.getPaddingLeft();
		int _pT = tv.getPaddingTop();
		int _pR = tv.getPaddingRight();
		int _pB = tv.getPaddingBottom();
		tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_button_3));
		tv.setPadding(_pL, _pT, _pR, _pB);
	}

	public void setStyleTextViewPictureNormal(TextView tv,Drawable pA){
		tv.setEnabled(true);
		pA.setBounds(0, 0, pA.getMinimumWidth(), pA.getMinimumHeight());
		tv.setCompoundDrawables(null, pA, null, null);
	}
	
	@SuppressWarnings("deprecation")
	public void setStyleTextViewRight(TextView tv){
		mRightCount++;
		tv.setTextAppearance(this, R.style.test_words_page_text_right_selector);
		Drawable right= getResources().getDrawable(R.drawable.ic_right);
		right.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight());
		tv.setCompoundDrawables(null, null, right, null);
		tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_button_right));
	}
	
	@SuppressWarnings("deprecation")
	public void setStyleTextViewError(TextView tv){
		joinWords();
		tv.setTextAppearance(this, R.style.test_words_page_text_error_selector);
		Drawable right= getResources().getDrawable(R.drawable.ic_error);
		right.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight());
		tv.setCompoundDrawables(null, null, right, null);
		tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_button_error));
	}
	
	public void joinWords(){
		getHandlerContext().makeTextShort("已加入生词本");
	}
	
}
