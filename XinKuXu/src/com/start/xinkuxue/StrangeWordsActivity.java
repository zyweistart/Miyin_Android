package com.start.xinkuxue;

import java.util.List;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.service.bean.StrangeWordItem;
import com.start.service.bean.StrangeWordStatisticsItem;

/**
 * 生词本
 * @author zhenyao
 *
 */
public class StrangeWordsActivity extends BaseActivity{
	
	public static final String BUNDLE_TYPE="BUNDLE_TYPE";
	
	private ListView mListView;
	private StrangeWordsAdapter mAdapter;
	private List<StrangeWordStatisticsItem> mStrangeWordStatisticsItems;
	private String type;
	private TextView word_title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_strange_words);
		
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			type=bundle.getString(BUNDLE_TYPE);
			word_title=(TextView)findViewById(R.id.word_title);
			if(type.equals(StrangeWordItem.CATEGORY_ERROR)){
				word_title.setText(R.string.errorwordcategory);
			}else{
				word_title.setText(R.string.wordcategory);
			}
			
			mListView=(ListView)findViewById(R.id.listview_strange_words);
			
			mStrangeWordStatisticsItems=BaseContext.getDBManager().findStrangeWordStatistic(getAppContext().currentUser().getCacheAccount(),type);
			mAdapter=new StrangeWordsAdapter(this,type);
			mAdapter.setStrangeWordStatisticsItems(mStrangeWordStatisticsItems);
			mListView.setAdapter(mAdapter);
		}else{
			finish();
		}
	}
	
}
