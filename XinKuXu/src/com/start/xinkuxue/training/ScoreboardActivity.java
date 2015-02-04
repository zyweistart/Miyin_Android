package com.start.xinkuxue.training;

import android.os.Bundle;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.xinkuxue.R;

/**
 * 积分榜
 * @author zhenyao
 *
 */
public class ScoreboardActivity extends BaseActivity{

	private int type;
	private TextView tvTitle,tvscoreboard1,tvscoreboard2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scoreboard);
		tvTitle=(TextView)findViewById(R.id.tvTitle);
		tvTitle.setText("积分榜");
		tvscoreboard1=(TextView)findViewById(R.id.tvscoreboard1);
		tvscoreboard2=(TextView)findViewById(R.id.tvscoreboard2);
		type=0;
		setEnabledByIndex();
	}
	
	public void setEnabledByIndex(){
		tvscoreboard1.setEnabled(type==0?false:true);
//		xlv_listview_1.setVisibility(type==0?View.VISIBLE:View.GONE);
		tvscoreboard2.setEnabled(type==1?false:true);
//		xlv_listview_2.setVisibility(type==1?View.VISIBLE:View.GONE);
	}
	
}
