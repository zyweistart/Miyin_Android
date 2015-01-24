package com.start.xinkuxue;

import java.io.File;
import java.io.IOException;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.service.WordService;
import com.start.service.bean.WordItem;

public class Word123Activity extends BaseActivity{
	
public static final String BUNDLE_ANSWER_ARRAY="BUNDLE_ANSWER_ARRAY";
	
	private int currentIndex;
	private String[] mAnswerArray;
	
	private ImageView iv_word,iv_memory_method;
	private TextView txt_englishName,txt_phoneticSymbols,txt_chineseSignificance,txt_exampleEnglish,txt_exampleChinese,txt_memoryMethodA,txt_memoryMethodB;
	private WordService mWordService;
	private WordItem mWordItem;
	
	private MediaPlayer mMediaPlayer;
	
	protected Bundle mBundle;
	private int step;
	private LinearLayout frame_step_one;
	private RelativeLayout frame_step_two;
	private TextView txt_current_word_index,txt_current_word_process,txt_current_word_name,txt_current_word_exampleenglish,txt_current_word_chinesesignificance;
	private ImageView txt_current_word_image;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_word_123);
		frame_step_one=(LinearLayout)findViewById(R.id.frame_step_one);
		frame_step_two=(RelativeLayout)findViewById(R.id.frame_step_two);
		
		txt_current_word_index=(TextView)findViewById(R.id.txt_current_word_index);
		txt_current_word_process=(TextView)findViewById(R.id.txt_current_word_process);
		txt_current_word_name=(TextView)findViewById(R.id.txt_current_word_name);
		txt_current_word_exampleenglish=(TextView)findViewById(R.id.txt_current_word_exampleenglish);
		txt_current_word_chinesesignificance=(TextView)findViewById(R.id.txt_current_word_chinesesignificance);
		txt_current_word_image=(ImageView)findViewById(R.id.txt_current_word_image);
		
		iv_word=(ImageView)findViewById(R.id.iv_word);
		iv_memory_method=(ImageView)findViewById(R.id.iv_memory_method);
		txt_englishName=(TextView)findViewById(R.id.txt_englishName);
		txt_phoneticSymbols=(TextView)findViewById(R.id.txt_phoneticSymbols);
		txt_chineseSignificance=(TextView)findViewById(R.id.txt_chineseSignificance);
		txt_exampleEnglish=(TextView)findViewById(R.id.txt_exampleEnglish);
		txt_exampleChinese=(TextView)findViewById(R.id.txt_exampleChinese);
		txt_memoryMethodA=(TextView)findViewById(R.id.txt_memoryMethodA);
		txt_memoryMethodB=(TextView)findViewById(R.id.txt_memoryMethodB);
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
			currentIndex=0;
			initWord();
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
		}else if(v.getId()==R.id.btn_next||v.getId()==R.id.btn_understanding){
			closeAudio();
			if(mAnswerArray.length-1>currentIndex){
				currentIndex++;
				initWord();
			}else{
				getHandlerContext().makeTextLong("单词练习结束了");
			}
		}else if(v.getId()==R.id.immediatetest){
			Bundle bundle=new Bundle();
			bundle.putInt(LearnWordsSwitchTestActivity.TESTSWITCHTYPE, 1);
			Intent intent=new Intent(this,LearnWordsSwitchTestActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
		}else if(v.getId()==R.id.btn_prompt){
			step++;
			showWord();
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
		int current=currentIndex+1;
		txt_current_word_index.setText("当前第"+(currentIndex+1)+"个单词");
		txt_current_word_process.setText("测试进度："+String.valueOf(current+"/"+mAnswerArray.length));
		mWordItem=mWordService.findById(Integer.parseInt(mAnswerArray[currentIndex]));
		if(mWordItem==null){
			return;
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		String mImagePath = mWordService.getExampleImagePath(mWordItem.getId());
		if(new File(mImagePath).exists()){
			iv_word.setImageBitmap(BitmapFactory.decodeFile(mImagePath, options));
			txt_current_word_image.setImageBitmap(BitmapFactory.decodeFile(mImagePath, options));
		}else{
			iv_word.setImageDrawable(getResources().getDrawable(R.drawable.default_words));
			txt_current_word_image.setImageDrawable(getResources().getDrawable(R.drawable.default_words));
		}
		String mMemoryPath = mWordService.getMemoryImagePath(mWordItem.getId());
		if(new File(mMemoryPath).exists()){
			iv_memory_method.setVisibility(View.VISIBLE);
			iv_memory_method.setImageBitmap(BitmapFactory.decodeFile(mMemoryPath, options));
		}else{
			iv_memory_method.setVisibility(View.GONE);
		}
		txt_current_word_name.setText(mWordItem.getEnglishName());
		txt_current_word_exampleenglish.setText(mWordItem.getExampleEnglish());
		txt_current_word_chinesesignificance.setText(mWordItem.getChineseSignificance());
		
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
	
	public void showWord(){
		frame_step_one.setVisibility(step!=4?View.VISIBLE:View.GONE);
		frame_step_two.setVisibility(step==4?View.VISIBLE:View.GONE);
		txt_current_word_exampleenglish.setVisibility(step==1?View.VISIBLE:View.GONE);
		txt_current_word_image.setVisibility(step==2?View.VISIBLE:View.GONE);
		txt_current_word_chinesesignificance.setVisibility(step==3?View.VISIBLE:View.GONE);
	}

	public void initWord(){
		step=0;
		showWord();
		showWordDetail();
	}
	
}
