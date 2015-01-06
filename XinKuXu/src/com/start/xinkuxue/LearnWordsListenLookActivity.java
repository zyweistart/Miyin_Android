package com.start.xinkuxue;

import java.io.File;
import java.io.IOException;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.service.WordService;
import com.start.service.bean.WordItem;

/**
 * 边听边看学单词
 * @author start
 *
 */
public class LearnWordsListenLookActivity extends BaseActivity{
	
	public static final String BUNDLE_ANSWER_ARRAY="BUNDLE_ANSWER_ARRAY";
	
	private int currentIndex;
	private String[] mAnswerArray;
	
	private ImageView iv_word,iv_memory_method;
	private TextView txt_englishName,txt_phoneticSymbols,txt_chineseSignificance,txt_exampleEnglish,txt_exampleChinese,txt_memoryMethodA,txt_memoryMethodB;
	private ImageButton btn_previous;
	private TextView txt_learn_count;
	private WordService mWordService;
	private WordItem mWordItem;
	
	private RelativeLayout frame_learn;
	private LinearLayout frame_done;
	
	private MediaPlayer mMediaPlayer;
	
	protected Bundle mBundle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_learn_words_listen_look);
		iv_word=(ImageView)findViewById(R.id.iv_word);
		iv_memory_method=(ImageView)findViewById(R.id.iv_memory_method);
		txt_englishName=(TextView)findViewById(R.id.txt_englishName);
		txt_phoneticSymbols=(TextView)findViewById(R.id.txt_phoneticSymbols);
		txt_chineseSignificance=(TextView)findViewById(R.id.txt_chineseSignificance);
		txt_exampleEnglish=(TextView)findViewById(R.id.txt_exampleEnglish);
		txt_exampleChinese=(TextView)findViewById(R.id.txt_exampleChinese);
		txt_memoryMethodA=(TextView)findViewById(R.id.txt_memoryMethodA);
		txt_memoryMethodB=(TextView)findViewById(R.id.txt_memoryMethodB);
		btn_previous=(ImageButton)findViewById(R.id.btn_previous);
		txt_learn_count=(TextView)findViewById(R.id.txt_learn_count);
		frame_learn=(RelativeLayout)findViewById(R.id.frame_learn);
		frame_done=(LinearLayout)findViewById(R.id.frame_done);
		frame_learn.setVisibility(View.VISIBLE);
		frame_done.setVisibility(View.GONE);
		try{
			mWordService=new WordService(this);
		}catch(Exception e){
			getHandlerContext().makeTextLong(e.getMessage());
			finish();
			return;
		}
		mBundle=getIntent().getExtras();
		if(mBundle!=null){
			mAnswerArray=mBundle.getStringArray(BUNDLE_ANSWER_ARRAY);
			txt_learn_count.setText(String.valueOf(mAnswerArray.length));
			currentIndex=0;
			showWordDetail();
		}else{
			finish();
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_player){
			closeAudio();
			String mAudioPath = mWordService.getAudioPath(mWordItem.getId());
			if(new File(mAudioPath).exists()){
				try {
					mMediaPlayer=new MediaPlayer();
					mMediaPlayer.setDataSource(mAudioPath);
					mMediaPlayer.prepare();
					mMediaPlayer.start();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				getHandlerContext().makeTextShort(getString(R.string.word_data_not_audio));
			}
		}else if(v.getId()==R.id.btn_previous){
			closeAudio();
			currentIndex--;
			if(currentIndex<1){
				btn_previous.setVisibility(View.GONE);
			}
			showWordDetail();
		}else if(v.getId()==R.id.btn_next){
			closeAudio();
			if(mAnswerArray.length-1>currentIndex){
				currentIndex++;
				btn_previous.setVisibility(View.VISIBLE);
				showWordDetail();
			}else{
				frame_learn.setVisibility(View.GONE);
				frame_done.setVisibility(View.VISIBLE);
			}
		}else if(v.getId()==R.id.immediatetest){
			Bundle bundle=new Bundle();
			bundle.putInt(LearnWordsSwitchTestActivity.TESTSWITCHTYPE, 1);
			Intent intent=new Intent(this,LearnWordsSwitchTestActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
		}else if(v.getId()==R.id.waitagain){
			finish();
		}else{
			super.onClick(v);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		closeAudio();
	}

	public void showWordDetail(){
		mWordItem=mWordService.findById(Integer.parseInt(mAnswerArray[currentIndex]));
		if(mWordItem==null){
			return;
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		String mImagePath = mWordService.getExampleImagePath(mWordItem.getId());
		if(new File(mImagePath).exists()){
			iv_word.setImageBitmap(BitmapFactory.decodeFile(mImagePath, options));
		}else{
			iv_word.setImageDrawable(getResources().getDrawable(R.drawable.default_words));
		}
		String mMemoryPath = mWordService.getMemoryImagePath(mWordItem.getId());
		if(new File(mMemoryPath).exists()){
			iv_memory_method.setVisibility(View.VISIBLE);
			iv_memory_method.setImageBitmap(BitmapFactory.decodeFile(mMemoryPath, options));
		}else{
			iv_memory_method.setVisibility(View.GONE);
		}
		txt_englishName.setText(mWordItem.getEnglishName());
		txt_phoneticSymbols.setText(mWordItem.getPhoneticSymbols());
		txt_chineseSignificance.setText(mWordItem.getChineseSignificance());
		txt_exampleEnglish.setText(mWordItem.getExampleEnglish());
		txt_exampleChinese.setText(mWordItem.getExampleChinese());
		txt_memoryMethodA.setText(mWordItem.getMemoryMethodA());
		txt_memoryMethodB.setText(mWordItem.getMemoryMethodB());
	}
	
	public void closeAudio(){
		if(mMediaPlayer!=null){
			if(mMediaPlayer.isPlaying()){
				mMediaPlayer.stop();
			}
			mMediaPlayer.release();
			mMediaPlayer=null;
		}
	}
	
}
