package com.start.xinkuxue.training;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.xinkuxue.R;

/**
 * 设置
 * @author zhenyao
 *
 */
public class SettingActivity extends BaseActivity{

	private int type;
	private TextView tvTitle,tvaboutus,tvpersonalinfo,tvothersetting;
	private LinearLayout personal_frame;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		tvTitle=(TextView)findViewById(R.id.tvTitle);
		tvTitle.setText("设置");
		tvaboutus=(TextView)findViewById(R.id.tvaboutus);
		tvpersonalinfo=(TextView)findViewById(R.id.tvpersonalinfo);
		tvothersetting=(TextView)findViewById(R.id.tvothersetting);
		personal_frame=(LinearLayout)findViewById(R.id.personal_frame);
		type=0;
		setEnabledByIndex();
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.tvaboutus){
			type=0;
			setEnabledByIndex();
		}else if(v.getId()==R.id.tvpersonalinfo){
			type=1;
			setEnabledByIndex();
		}else if(v.getId()==R.id.tvothersetting){
			type=2;
			setEnabledByIndex();
		}
	}
	
	public void setEnabledByIndex(){
		tvaboutus.setEnabled(type==0?false:true);
//		xlv_listview_1.setVisibility(type==0?View.VISIBLE:View.GONE);
		tvpersonalinfo.setEnabled(type==1?false:true);
		personal_frame.setVisibility(type==1?View.VISIBLE:View.GONE);
		tvothersetting.setEnabled(type==2?false:true);
//		xlv_listview_3.setVisibility(type==2?View.VISIBLE:View.GONE);
	}
	
}
