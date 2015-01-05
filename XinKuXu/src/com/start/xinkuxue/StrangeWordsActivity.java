package com.start.xinkuxue;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;

import com.start.core.BaseActivity;
import com.start.service.bean.StrangeWordItem;
import com.start.service.bean.StrangeWordStatisticsItem;

/**
 * 生词本
 * @author zhenyao
 *
 */
public class StrangeWordsActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_strange_words);
		List<StrangeWordStatisticsItem> mStrangeWordStatisticsItems=BaseContext.getDBManager().findAllByStrangeWordStatistic(getAppContext().currentUser().getCacheAccount());
		for(StrangeWordStatisticsItem sws : mStrangeWordStatisticsItems){
			List<StrangeWordItem> datas=BaseContext.getDBManager().findAllByStrangeWordItem(sws.getJoinTime());
			System.out.println(datas.size());
			List<String> models=new ArrayList<String>();
			models.add("1");
			models.add("2");
			models.add("3");
			models.add("4");
			models.add("5");
			//答案总数
			int mAnswerCount=datas.size();
			List<String> ids=new ArrayList<String>();
			for(int i=0;i<mAnswerCount;i++){
				ids.add(datas.get(i).getIndex());
			}
			Bundle bundle=new Bundle();
			bundle.putStringArray(StrangeWordsTestPageActivity.BUNDLE_WORDS, models.toArray(new String[models.size()]));
			bundle.putStringArray(StrangeWordsTestPageActivity.BUNDLE_ANSWER_ARRAY, ids.toArray(new String[ids.size()]));
			Intent intent=new Intent(this,StrangeWordsTestPageActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
			break;
		}
	}
	
}
