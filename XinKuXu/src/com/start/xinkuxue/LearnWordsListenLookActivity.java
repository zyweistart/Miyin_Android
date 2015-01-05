package com.start.xinkuxue;

import java.io.File;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
	
	public static final String BUNDLE_LEARN_WORDS_START_INDEX="BUNDLE_LEARN_WORDS_START_INDEX";
	public static final String BUNDLE_LEARN_WORDS_END_INDEX="BUNDLE_LEARN_WORDS_END_INDEX";
	
	private ImageView iv_word;
	private TextView txt_englishName,txt_phoneticSymbols,txt_chineseSignificance,txt_exampleEnglish,txt_exampleChinese,txt_memoryMethodA,txt_memoryMethodB;
	private Button btn_previous,btn_next;
	
	private WordService mWordService;
	
	private int currentIndex,startIndex,endIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_learn_words_listen_look);
		iv_word=(ImageView)findViewById(R.id.iv_word);
		txt_englishName=(TextView)findViewById(R.id.txt_englishName);
		txt_phoneticSymbols=(TextView)findViewById(R.id.txt_phoneticSymbols);
		txt_chineseSignificance=(TextView)findViewById(R.id.txt_chineseSignificance);
		txt_exampleEnglish=(TextView)findViewById(R.id.txt_exampleEnglish);
		txt_exampleChinese=(TextView)findViewById(R.id.txt_exampleChinese);
		txt_memoryMethodA=(TextView)findViewById(R.id.txt_memoryMethodA);
		txt_memoryMethodB=(TextView)findViewById(R.id.txt_memoryMethodB);
		btn_previous=(Button)findViewById(R.id.btn_previous);
		btn_next=(Button)findViewById(R.id.btn_next);
		mWordService=new WordService();
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			startIndex=bundle.getInt(BUNDLE_LEARN_WORDS_START_INDEX);
			endIndex=bundle.getInt(BUNDLE_LEARN_WORDS_END_INDEX);
			if(endIndex>startIndex){
				currentIndex=startIndex;
				showWordDetail();
				return;
			}
		}
		finish();
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_player){
			
		}else if(v.getId()==R.id.btn_previous){
			if(currentIndex>startIndex){
				currentIndex--;
				if(currentIndex==startIndex){
					btn_previous.setVisibility(View.GONE);
				}
				showWordDetail();
			}
		}else if(v.getId()==R.id.btn_next){
			if(endIndex>currentIndex){
				currentIndex++;
				btn_previous.setVisibility(View.VISIBLE);
				showWordDetail();
				if(endIndex==currentIndex){
					btn_previous.setVisibility(View.GONE);
					btn_next.setText("结束练习");
				}
			}else{
				finish();
			}
		}else{
			super.onClick(v);
		}
	}
	
	public void showWordDetail(){
		WordItem wordItem=mWordService.findById(currentIndex);
		if(wordItem==null){
			return;
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		String mImagePath = mWordService.getExampleImagePath(wordItem.getId());
		if(new File(mImagePath).exists()){
			iv_word.setImageBitmap(BitmapFactory.decodeFile(mImagePath, options));
		}
		txt_englishName.setText(wordItem.getEnglishName());
		txt_phoneticSymbols.setText(wordItem.getPhoneticSymbols());
		txt_chineseSignificance.setText(wordItem.getChineseSignificance());
		txt_exampleEnglish.setText(wordItem.getExampleEnglish());
		txt_exampleChinese.setText(wordItem.getExampleChinese());
		txt_memoryMethodA.setText(wordItem.getMemoryMethodA());
		txt_memoryMethodB.setText(wordItem.getMemoryMethodB());
	}
	
}
