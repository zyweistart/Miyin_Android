package com.start.xinkuxue;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
		for(StrangeWordStatisticsItem i : mStrangeWordStatisticsItems){
			List<StrangeWordItem> datas=BaseContext.getDBManager().findAllByStrangeWordItem(i.getJoinTime());
			System.out.println(datas.size());
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_login){
			Intent intent=new Intent(this,MainActivity.class);
			startActivity(intent);
			finish();
		}else if(v.getId()==R.id.btn_register){
			getHandlerContext().makeTextLong("暂无注册功能");
		}else{
			super.onClick(v);
		}
	}
	
}
