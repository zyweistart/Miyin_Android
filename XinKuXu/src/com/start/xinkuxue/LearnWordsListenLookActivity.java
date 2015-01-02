package com.start.xinkuxue;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.start.core.BaseActivity;

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
		
//		Bundle bundle=getIntent().getExtras();
//		if(bundle!=null){
//			startIndex=bundle.getInt(BUNDLE_LEARN_WORDS_START_INDEX);
//			endIndex=bundle.getInt(BUNDLE_LEARN_WORDS_END_INDEX);
			startIndex=1;
			endIndex=5;
			if(endIndex>startIndex){
				currentIndex=startIndex;
				showWordDetail();
				return;
			}
//		}
//		finish();
		
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
				btn_previous.setVisibility(View.VISIBLE);
				currentIndex++;
				showWordDetail();
			}else{
				//TODO:跳转至结束页面
				getHandlerContext().makeTextLong("单词复习结束了");
			}
		}else{
			super.onClick(v);
		}
	}
	
	public void showWordDetail(){
		//TODO:根据索引查找单词
		iv_word.setImageResource(R.drawable.default_words);
		txt_englishName.setText("Message"+currentIndex);
		txt_phoneticSymbols.setText("[MAASD]");
		txt_chineseSignificance.setText("pron.我的");
		txt_exampleEnglish.setText("Please leave a message with my receptionist.");
		txt_exampleChinese.setText("请给我的接待员留言。");
		txt_memoryMethodA.setText("记忆法1：re重复、重新+cept拿、抓、握住+ion表名词+ist从事某种职业的人->n.接待员：传达员");
		txt_memoryMethodB.setText("记忆法2：re重复、重新+cept拿、抓、握住+ion表名词+ist从事某种职业的人->n.接待员：传达员");
	}
	
}
