package com.start.xinkuxue.vocabulary;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.core.Constant;
import com.start.service.AppServer;
import com.start.service.WordService;
import com.start.service.bean.WordItem;
import com.start.xinkuxue.R;

/**
 * 练习测试
 * 
 * @author start
 * 
 */
public class VocabularyTestActivity extends BaseActivity {

	// 测试类型
	public static final String BUNDLE_WORDS = "BUNDLE_TEST_TYPE";
	// 题目列表
	public static final String BUNDLE_ANSWER_ARRAY = "BUNDLE_ANSWER_ARRAY";

	private Random rnTestRandom;
	private WordService mWordService;

	// 题目列表
	protected String[] mAnswerArray;
	// 总答对数
	protected int mRightCount;

	// 测试类型(1:中-英 2:英-中 3:图-英 4:英-图 5:填空)
	private String[] mTestType;
	// 当前单词的ID，单词游标索引
	protected int mCurrentWordId, mAnswerIndex;

	private int mCurrentRightWordItemIndex;
	private WordItem mCurrentRightWordItem;
	private String mTitle, mAName, mBName, mCName, mDName;
	protected Bundle mBundle;
	private ImageView problem_picture;
	private TextView problem_words, problem_sentence;
	private LinearLayout answer_frame_text;
	private RelativeLayout answer_frame_picture;
	private ImageView countdown;

	private TextView frame_text_selector_answer_a,
			frame_text_selector_answer_b, frame_text_selector_answer_c,
			frame_text_selector_answer_d;
	private ImageView frame_imageview_answer_a, frame_imageview_answer_b,
			frame_imageview_answer_c, frame_imageview_answer_d;
	private TextView frame_textview_answer_a, frame_textview_answer_b,
			frame_textview_answer_c, frame_textview_answer_d;
	private LinearLayout frame_picture_selector_answer_a,
			frame_picture_selector_answer_b, frame_picture_selector_answer_c,
			frame_picture_selector_answer_d;
	private TextView frame_text_selector_answer_cannotskip,
			frame_picture_selector_answer_cannotskip;

	protected Boolean isCountdowning=true;
	private MediaPlayer mMPRight,mMPError;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vocabulary_test);
		problem_picture = (ImageView) findViewById(R.id.problem_picture);
		problem_words = (TextView) findViewById(R.id.problem_words);
		problem_sentence = (TextView) findViewById(R.id.problem_sentence);
		answer_frame_text = (LinearLayout) findViewById(R.id.answer_frame_text);
		answer_frame_picture = (RelativeLayout) findViewById(R.id.answer_frame_picture);
		countdown=(ImageView)findViewById(R.id.countdown);
		if(isCountdowning){
			countdown.setVisibility(View.VISIBLE);
		}

		frame_text_selector_answer_a = (TextView) findViewById(R.id.frame_text_selector_answer_a);
		frame_text_selector_answer_b = (TextView) findViewById(R.id.frame_text_selector_answer_b);
		frame_text_selector_answer_c = (TextView) findViewById(R.id.frame_text_selector_answer_c);
		frame_text_selector_answer_d = (TextView) findViewById(R.id.frame_text_selector_answer_d);
		frame_text_selector_answer_cannotskip = (TextView) findViewById(R.id.frame_text_selector_answer_cannotskip);
		frame_imageview_answer_a = (ImageView) findViewById(R.id.frame_imageview_answer_a);
		frame_imageview_answer_b = (ImageView) findViewById(R.id.frame_imageview_answer_b);
		frame_imageview_answer_c = (ImageView) findViewById(R.id.frame_imageview_answer_c);
		frame_imageview_answer_d = (ImageView) findViewById(R.id.frame_imageview_answer_d);
		frame_textview_answer_a = (TextView) findViewById(R.id.frame_textview_answer_a);
		frame_textview_answer_b = (TextView) findViewById(R.id.frame_textview_answer_b);
		frame_textview_answer_c = (TextView) findViewById(R.id.frame_textview_answer_c);
		frame_textview_answer_d = (TextView) findViewById(R.id.frame_textview_answer_d);
		frame_picture_selector_answer_a = (LinearLayout) findViewById(R.id.frame_picture_selector_answer_a);
		frame_picture_selector_answer_b = (LinearLayout) findViewById(R.id.frame_picture_selector_answer_b);
		frame_picture_selector_answer_c = (LinearLayout) findViewById(R.id.frame_picture_selector_answer_c);
		frame_picture_selector_answer_d = (LinearLayout) findViewById(R.id.frame_picture_selector_answer_d);
		frame_picture_selector_answer_cannotskip = (TextView) findViewById(R.id.frame_picture_selector_answer_cannotskip);

		mMPRight = MediaPlayer.create(VocabularyTestActivity.this,R.raw.right);
		mMPError = MediaPlayer.create(VocabularyTestActivity.this,R.raw.error);
		
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			rnTestRandom = new Random();
			mTestType = mBundle.getStringArray(BUNDLE_WORDS);
			mAnswerArray = mBundle.getStringArray(BUNDLE_ANSWER_ARRAY);
			try {
				mWordService = new WordService(this);
			} catch (Exception e) {
				getHandlerContext().makeTextLong(e.getMessage());
				finish();
				return;
			}
			mAnswerIndex = 0;
		} else {
			finish();
		}

	}
	
	@Override
	protected void onStart(){
		super.onStart();
		currentWord();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(isCountdowning){
			mCountDownTimer.cancel();
		}
	}
	
	@Override
	public void onClick(View v) {
		if(isCountdowning){
			mCountDownTimer.cancel();
		}
		if (v.getId() == R.id.frame_text_selector_answer_a) {
			if (mCurrentRightWordItemIndex == 0) {
				playerRight();
				setStyleTextViewRight(frame_text_selector_answer_a);
			} else {
				setStyleTextViewError(frame_text_selector_answer_a);
			}
		} else if (v.getId() == R.id.frame_text_selector_answer_b) {
			if (mCurrentRightWordItemIndex == 1) {
				playerRight();
				setStyleTextViewRight(frame_text_selector_answer_b);
			} else {
				setStyleTextViewError(frame_text_selector_answer_b);
			}
		} else if (v.getId() == R.id.frame_text_selector_answer_c) {
			if (mCurrentRightWordItemIndex == 2) {
				playerRight();
				setStyleTextViewRight(frame_text_selector_answer_c);
			} else {
				setStyleTextViewError(frame_text_selector_answer_c);
			}
		} else if (v.getId() == R.id.frame_text_selector_answer_d) {
			if (mCurrentRightWordItemIndex == 3) {
				playerRight();
				setStyleTextViewRight(frame_text_selector_answer_d);
			} else {
				setStyleTextViewError(frame_text_selector_answer_d);
			}
		} else if (v.getId() == R.id.frame_text_selector_answer_cannotskip) {
			playerError();
			joinWords();
			if (mCurrentRightWordItemIndex == 0) {
				setStyleTextViewRight(frame_text_selector_answer_a);
			} else if (mCurrentRightWordItemIndex == 1) {
				setStyleTextViewRight(frame_text_selector_answer_b);
			} else if (mCurrentRightWordItemIndex == 2) {
				setStyleTextViewRight(frame_text_selector_answer_c);
			} else if (mCurrentRightWordItemIndex == 3) {
				setStyleTextViewRight(frame_text_selector_answer_d);
			}
		} else if (v.getId() == R.id.frame_picture_selector_answer_a) {
			if (mCurrentRightWordItemIndex == 0) {
				playerRight();
				setStyleTextViewRight(frame_picture_selector_answer_a,
						frame_textview_answer_a);
			} else {
				setStyleTextViewError(frame_picture_selector_answer_a,
						frame_textview_answer_a);
			}
		} else if (v.getId() == R.id.frame_picture_selector_answer_b) {
			if (mCurrentRightWordItemIndex == 1) {
				playerRight();
				setStyleTextViewRight(frame_picture_selector_answer_b,
						frame_textview_answer_b);
			} else {
				setStyleTextViewError(frame_picture_selector_answer_b,
						frame_textview_answer_b);
			}
		} else if (v.getId() == R.id.frame_picture_selector_answer_c) {
			if (mCurrentRightWordItemIndex == 2) {
				playerRight();
				setStyleTextViewRight(frame_picture_selector_answer_c,
						frame_textview_answer_c);
			} else {
				setStyleTextViewError(frame_picture_selector_answer_c,
						frame_textview_answer_c);
			}
		} else if (v.getId() == R.id.frame_picture_selector_answer_d) {
			if (mCurrentRightWordItemIndex == 3) {
				playerRight();
				setStyleTextViewRight(frame_picture_selector_answer_d,
						frame_textview_answer_d);
			} else {
				setStyleTextViewError(frame_picture_selector_answer_d,
						frame_textview_answer_d);
			}
		} else if (v.getId() == R.id.frame_picture_selector_answer_cannotskip) {
			playerError();
			joinWords();
			if (mCurrentRightWordItemIndex == 0) {
				setStyleTextViewRight(frame_picture_selector_answer_a,
						frame_textview_answer_a);
			} else if (mCurrentRightWordItemIndex == 1) {
				setStyleTextViewRight(frame_picture_selector_answer_b,
						frame_textview_answer_b);
			} else if (mCurrentRightWordItemIndex == 2) {
				setStyleTextViewRight(frame_picture_selector_answer_c,
						frame_textview_answer_c);
			} else if (mCurrentRightWordItemIndex == 3) {
				setStyleTextViewRight(frame_picture_selector_answer_d,
						frame_textview_answer_d);
			}
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
				if (mAnswerIndex >= mAnswerArray.length) {
					mCountDownTimer.cancel();
					toDonePage();
				} else {
					frame_text_selector_answer_cannotskip.setEnabled(true);
					frame_picture_selector_answer_cannotskip.setEnabled(true);
					currentWord();
				}
			}
		}, 1500);
	}

	public void currentWord() {
		if(isCountdowning){
			mCountDownTimer.cancel();
			mCountDownTimer.start();
		}
		mCurrentWordId = Integer.parseInt(mAnswerArray[mAnswerIndex]);
		mCurrentRightWordItem = mWordService.findById(mCurrentWordId);
		// 加载随机答案前3后3中随机查找
		List<WordItem> answerWordItems = getRandomWordItem();
		List<WordItem> sortWordItems = sortWordItem(answerWordItems);
		// 获取正确的答案
		for (int i = 0; i < sortWordItems.size(); i++) {
			WordItem tmpWord = sortWordItems.get(i);
			if (mCurrentRightWordItem.getId().equals(tmpWord.getId())) {
				mCurrentRightWordItemIndex = i;
				break;
			}
		}
		int type = Integer.parseInt(mTestType[rnTestRandom
				.nextInt(mTestType.length)]);
		if (type == 1) {
			mTitle = mCurrentRightWordItem.getChineseSignificance();
			mAName = "A、" + sortWordItems.get(0).getEnglishName();
			mBName = "B、" + sortWordItems.get(1).getEnglishName();
			mCName = "C、" + sortWordItems.get(2).getEnglishName();
			mDName = "D、" + sortWordItems.get(3).getEnglishName();
			wordToText();
		} else if (type == 2) {
			mTitle = mCurrentRightWordItem.getEnglishName();
			mAName = "A、" + sortWordItems.get(0).getChineseSignificance();
			mBName = "B、" + sortWordItems.get(1).getChineseSignificance();
			mCName = "C、" + sortWordItems.get(2).getChineseSignificance();
			mDName = "D、" + sortWordItems.get(3).getChineseSignificance();
			wordToText();
		} else if (type == 3) {
			mTitle = mCurrentRightWordItem.getId();
			mAName = "A、" + sortWordItems.get(0).getEnglishName();
			mBName = "B、" + sortWordItems.get(1).getEnglishName();
			mCName = "C、" + sortWordItems.get(2).getEnglishName();
			mDName = "D、" + sortWordItems.get(3).getEnglishName();
			pictureToText();
		} else if (type == 4) {
			mTitle = mCurrentRightWordItem.getEnglishName();
			mAName = sortWordItems.get(0).getId();
			mBName = sortWordItems.get(1).getId();
			mCName = sortWordItems.get(2).getId();
			mDName = sortWordItems.get(3).getId();
			wordToPicture();
		} else if (type == 5) {
			mTitle = mCurrentRightWordItem.getFillProblem();
			mAName = "A、" + sortWordItems.get(0).getFillAnswer();
			mBName = "B、" + sortWordItems.get(1).getFillAnswer();
			mCName = "C、" + sortWordItems.get(2).getFillAnswer();
			mDName = "D、" + sortWordItems.get(3).getFillAnswer();
			textToText();
		}
		mAnswerIndex++;
	}

	public void wordToText() {
		// 显示隐藏
		problem_picture.setVisibility(View.GONE);
		problem_words.setVisibility(View.VISIBLE);
		problem_sentence.setVisibility(View.GONE);
		answer_frame_text.setVisibility(View.VISIBLE);
		answer_frame_picture.setVisibility(View.GONE);
		// 设置样式
		setStyleTextViewNormal(frame_text_selector_answer_a);
		setStyleTextViewNormal(frame_text_selector_answer_b);
		setStyleTextViewNormal(frame_text_selector_answer_c);
		setStyleTextViewNormal(frame_text_selector_answer_d);
		// 显示内容
		problem_words.setText(AppServer.ToDBC(mTitle));
		frame_text_selector_answer_a.setText(mAName);
		frame_text_selector_answer_b.setText(mBName);
		frame_text_selector_answer_c.setText(mCName);
		frame_text_selector_answer_d.setText(mDName);
	}

	public void wordToPicture() {
		// 显示隐藏
		problem_picture.setVisibility(View.GONE);
		problem_words.setVisibility(View.VISIBLE);
		problem_sentence.setVisibility(View.GONE);
		answer_frame_text.setVisibility(View.GONE);
		answer_frame_picture.setVisibility(View.VISIBLE);
		// 设置样式、显示内容
		problem_words.setText(AppServer.ToDBC(mTitle));

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		String mAPicturePath = mWordService.getExampleImagePath(mAName);
		if (new File(mAPicturePath).exists()) {
			setStyleTextViewPictureNormal(frame_picture_selector_answer_a,
					frame_imageview_answer_a, frame_textview_answer_a,
					BitmapFactory.decodeFile(mAPicturePath, options));
		} else {
			setStyleTextViewPictureNormal(frame_picture_selector_answer_a,
					frame_imageview_answer_a, frame_textview_answer_a,
					BitmapFactory.decodeResource(getResources(),
							R.drawable.default_words));
		}
		String mBPicturePath = mWordService.getExampleImagePath(mBName);
		if (new File(mBPicturePath).exists()) {
			setStyleTextViewPictureNormal(frame_picture_selector_answer_b,
					frame_imageview_answer_b, frame_textview_answer_b,
					BitmapFactory.decodeFile(mBPicturePath, options));
		} else {
			setStyleTextViewPictureNormal(frame_picture_selector_answer_b,
					frame_imageview_answer_b, frame_textview_answer_b,
					BitmapFactory.decodeResource(getResources(),
							R.drawable.default_words));
		}
		String mCPicturePath = mWordService.getExampleImagePath(mCName);
		if (new File(mCPicturePath).exists()) {
			setStyleTextViewPictureNormal(frame_picture_selector_answer_c,
					frame_imageview_answer_c, frame_textview_answer_c,
					BitmapFactory.decodeFile(mCPicturePath, options));
		} else {
			setStyleTextViewPictureNormal(frame_picture_selector_answer_c,
					frame_imageview_answer_c, frame_textview_answer_c,
					BitmapFactory.decodeResource(getResources(),
							R.drawable.default_words));
		}
		String mDPicturePath = mWordService.getExampleImagePath(mDName);
		if (new File(mDPicturePath).exists()) {
			setStyleTextViewPictureNormal(frame_picture_selector_answer_d,
					frame_imageview_answer_d, frame_textview_answer_d,
					BitmapFactory.decodeFile(mDPicturePath, options));
		} else {
			setStyleTextViewPictureNormal(frame_picture_selector_answer_d,
					frame_imageview_answer_d, frame_textview_answer_d,
					BitmapFactory.decodeResource(getResources(),
							R.drawable.default_words));
		}
	}

	public void textToText() {
		// 显示隐藏
		problem_picture.setVisibility(View.GONE);
		problem_words.setVisibility(View.GONE);
		problem_sentence.setVisibility(View.VISIBLE);
		answer_frame_text.setVisibility(View.VISIBLE);
		answer_frame_picture.setVisibility(View.GONE);
		// 设置样式
		setStyleTextViewNormal(frame_text_selector_answer_a);
		setStyleTextViewNormal(frame_text_selector_answer_b);
		setStyleTextViewNormal(frame_text_selector_answer_c);
		setStyleTextViewNormal(frame_text_selector_answer_d);
		// 显示内容
		problem_sentence.setText(AppServer.ToDBC(mTitle));
		frame_text_selector_answer_a.setText(mAName);
		frame_text_selector_answer_b.setText(mBName);
		frame_text_selector_answer_c.setText(mCName);
		frame_text_selector_answer_d.setText(mDName);
	}

	public void pictureToText() {
		// 显示隐藏
		problem_picture.setVisibility(View.VISIBLE);
		problem_words.setVisibility(View.GONE);
		problem_sentence.setVisibility(View.GONE);
		answer_frame_text.setVisibility(View.VISIBLE);
		answer_frame_picture.setVisibility(View.GONE);
		// 设置样式
		setStyleTextViewNormal(frame_text_selector_answer_a);
		setStyleTextViewNormal(frame_text_selector_answer_b);
		setStyleTextViewNormal(frame_text_selector_answer_c);
		setStyleTextViewNormal(frame_text_selector_answer_d);
		// 显示内容
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		String myJpgPath = mWordService.getExampleImagePath(mTitle);
		File dbFile = new File(myJpgPath);
		if (dbFile.exists()) {
			problem_picture.setImageBitmap(BitmapFactory.decodeFile(myJpgPath,
					options));
		} else {
			problem_picture.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.default_words));
		}
		frame_text_selector_answer_a.setText(mAName);
		frame_text_selector_answer_b.setText(mBName);
		frame_text_selector_answer_c.setText(mCName);
		frame_text_selector_answer_d.setText(mDName);
	}

	@SuppressWarnings("deprecation")
	public void setStyleTextViewNormal(TextView tv) {
		tv.setEnabled(true);
		tv.setTextAppearance(this, R.style.test_words_page_text_selector);
		tv.setCompoundDrawables(null, null, null, null);
		int _pL = tv.getPaddingLeft();
		int _pT = tv.getPaddingTop();
		int _pR = tv.getPaddingRight();
		int _pB = tv.getPaddingBottom();
		tv.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.selector_button_color_lightgrey));
		tv.setPadding(_pL, _pT, _pR, _pB);
	}

	@SuppressWarnings("deprecation")
	public void setStyleTextViewRight(TextView tv) {
		tv.setTextAppearance(this, R.style.test_words_page_text_right_selector);
		Drawable right = getResources().getDrawable(R.drawable.ic_right);
		right.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight());
		tv.setCompoundDrawables(null, null, right, null);
		tv.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.bg_color_green));
	}

	@SuppressWarnings("deprecation")
	public void setStyleTextViewError(TextView tv) {
		playerError();
		joinWords();
		tv.setTextAppearance(this, R.style.test_words_page_text_error_selector);
		Drawable right = getResources().getDrawable(R.drawable.ic_error);
		right.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight());
		tv.setCompoundDrawables(null, null, right, null);
		tv.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.bg_color_red));
		if (mCurrentRightWordItemIndex == 0) {
			setStyleTextViewRight(frame_text_selector_answer_a);
		} else if (mCurrentRightWordItemIndex == 1) {
			setStyleTextViewRight(frame_text_selector_answer_b);
		} else if (mCurrentRightWordItemIndex == 2) {
			setStyleTextViewRight(frame_text_selector_answer_c);
		} else if (mCurrentRightWordItemIndex == 3) {
			setStyleTextViewRight(frame_text_selector_answer_d);
		}
	}

	public void setStyleTextViewPictureNormal(LinearLayout ll, ImageView iv,
			TextView tv, Bitmap bitmap) {
		ll.setEnabled(true);
		iv.setImageBitmap(bitmap);
		tv.setTextColor(0xff000000);
		ll.setBackgroundResource(R.color.transparent);
	}

	@SuppressWarnings("deprecation")
	public void setStyleTextViewRight(LinearLayout ll, TextView tv) {
		tv.setTextColor(0xffffffff);
		ll.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.bg_color_green));
	}

	@SuppressWarnings("deprecation")
	public void setStyleTextViewError(LinearLayout ll, TextView tv) {
		playerError();
		joinWords();
		tv.setTextColor(0xffffffff);
		ll.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.bg_color_red));
		if (mCurrentRightWordItemIndex == 0) {
			setStyleTextViewRight(frame_picture_selector_answer_a,
					frame_textview_answer_a);
		} else if (mCurrentRightWordItemIndex == 1) {
			setStyleTextViewRight(frame_picture_selector_answer_b,
					frame_textview_answer_b);
		} else if (mCurrentRightWordItemIndex == 2) {
			setStyleTextViewRight(frame_picture_selector_answer_c,
					frame_textview_answer_c);
		} else if (mCurrentRightWordItemIndex == 3) {
			setStyleTextViewRight(frame_picture_selector_answer_d,
					frame_textview_answer_d);
		}
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
				if (answerWordItems.size() >= 4) {
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

	public void toDonePage() {
		Bundle bundle = new Bundle();
		bundle.putAll(mBundle);
		bundle.putInt(VocabularyTestGoResultsActivity.RIGHTCOUNT, mRightCount);
		bundle.putInt(VocabularyTestGoResultsActivity.ANSWERCOUNT,
				mAnswerArray.length);
		Intent intent = new Intent(this, VocabularyTestGoResultsActivity.class);
		intent.addCategory(Intent.CATEGORY_HOME);  
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}

	/**
	 * 加入生词本
	 */
	public void joinWords() {

	}

	public void playerRight() {
		mRightCount++;
		mMPRight.start();
	}
	
	public void playerError() {
		mMPError.start();
	}

	private CountDownTimer mCountDownTimer=new CountDownTimer(Constant.TOTALSECOND,Constant.SECOND) {
		
		@Override
		public void onTick(long millisUntilFinished) {
			int n=(int)millisUntilFinished / Constant.SECOND;
			countdown.setImageResource(mCountdownimg[n]);
		}
		
		@Override
		public void onFinish() {
			if (mAnswerIndex >= mAnswerArray.length) {
				toDonePage();
			} else {
				frame_text_selector_answer_cannotskip.setEnabled(true);
				frame_picture_selector_answer_cannotskip.setEnabled(true);
				currentWord();
			}
		}
	};
	
}