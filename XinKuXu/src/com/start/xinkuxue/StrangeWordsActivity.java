package com.start.xinkuxue;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.start.core.BaseActivity;
import com.start.service.bean.StrangeWordItem;
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
		
//		for(int i=0;i<mStrangeWordStatisticsItems.size();i++){
//			StrangeWordStatisticsItem sws=mStrangeWordStatisticsItems.get(0);
//			if(i==0){
//				btn_strange_words1.setVisibility(View.VISIBLE);
//				btn_strange_words1.setText(sws.getJoinTime()+"-"+sws.getWordCount()+"个生词");
//			}else if(i==1){
//				btn_strange_words2.setVisibility(View.VISIBLE);
//				btn_strange_words2.setText(sws.getJoinTime()+"-"+sws.getWordCount()+"个生词");
//			}
//		}
		
//		for(StrangeWordStatisticsItem sws : mStrangeWordStatisticsItems){
//			Log.v(TAG,""+sws);
////			List<StrangeWordItem> datas=BaseContext.getDBManager().findAllByStrangeWordItem(sws.getUserName(),sws.getJoinTime());
////			System.out.println(datas.size());
////			List<String> models=new ArrayList<String>();
////			models.add("1");
////			models.add("2");
////			models.add("3");
////			models.add("4");
////			models.add("5");
////			//答案总数
////			int mAnswerCount=datas.size();
////			List<String> ids=new ArrayList<String>();
////			for(int i=0;i<mAnswerCount;i++){
////				ids.add(datas.get(i).getIndex());
////			}
////			Bundle bundle=new Bundle();
////			
////			bundle.putStringArray(StrangeWordsListenLookActivity.BUNDLE_ANSWER_ARRAY, ids.toArray(new String[ids.size()]));
////			Intent intent=new Intent(this,StrangeWordsListenLookActivity.class);
////			
//////			bundle.putStringArray(StrangeWordsTestPageActivity.BUNDLE_WORDS, models.toArray(new String[models.size()]));
//////			bundle.putStringArray(StrangeWordsTestPageActivity.BUNDLE_ANSWER_ARRAY, ids.toArray(new String[ids.size()]));
//////			Intent intent=new Intent(this,StrangeWordsTestPageActivity.class);
////			intent.putExtras(bundle);
////			startActivity(intent);
////			finish();
////			break;
//		}
	}
	
//	@Override
//	public void onClick(View v) {
//		if(v.getId()==R.id.btn_strange_words1){
//			StrangeWordStatisticsItem sws=mStrangeWordStatisticsItems.get(0);
//			//去复习
//			goListenLook(sws);
//			//去测试
////			goTest(sws);
//		}else if(v.getId()==R.id.btn_strange_words2){
//			
//		}else if(v.getId()==R.id.btn_strange_words3){
//			
//		}else if(v.getId()==R.id.btn_strange_words4){
//			
//		}
//	}
	
	public void goListenLook(StrangeWordStatisticsItem sws){
		List<StrangeWordItem> words=BaseContext.getDBManager().findAllByStrangeWordItem(sws.getUserName(),sws.getJoinTime());
		List<String> ids=new ArrayList<String>();
		for(StrangeWordItem s :words){
			ids.add(s.getIndex());
		}
		Bundle bundle=new Bundle();
		bundle.putString(StrangeWordsSwitchTestActivity.BUNDLE_JOINTIME, sws.getJoinTime());
		bundle.putStringArray(StrangeWordsListenLookActivity.BUNDLE_ANSWER_ARRAY, ids.toArray(new String[ids.size()]));
		Intent intent=new Intent(this,StrangeWordsListenLookActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	public void goTest(StrangeWordStatisticsItem sws){
		Bundle bundle=new Bundle();
		bundle.putString(StrangeWordsSwitchTestActivity.BUNDLE_JOINTIME, sws.getJoinTime());
		Intent intent=new Intent(this,StrangeWordsSwitchTestActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
}
