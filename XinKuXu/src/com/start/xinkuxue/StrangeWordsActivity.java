package com.start.xinkuxue;

import java.util.List;

import android.os.Bundle;
import android.widget.ListView;

import com.start.core.BaseActivity;
import com.start.service.bean.StrangeWordStatisticsItem;

/**
 * 生词本
 * @author zhenyao
 *
 */
public class StrangeWordsActivity extends BaseActivity{
	
	private ListView mListView;
	private StrangeWordsAdapter mAdapter;
	private List<StrangeWordStatisticsItem> mStrangeWordStatisticsItems;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_strange_words);
		
		mListView=(ListView)findViewById(R.id.listview_strange_words);
		
		mStrangeWordStatisticsItems=BaseContext.getDBManager().findStrangeWordStatistic(getAppContext().currentUser().getCacheAccount());
		mAdapter=new StrangeWordsAdapter(this);
		mAdapter.setStrangeWordStatisticsItems(mStrangeWordStatisticsItems);
		mListView.setAdapter(mAdapter);
	}
	
}
