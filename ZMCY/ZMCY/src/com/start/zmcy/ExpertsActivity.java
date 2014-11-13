package com.start.zmcy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.start.core.BaseActivity;

/**
 * 专家
 */
public class ExpertsActivity extends BaseActivity{
	
	private Button main_head_1;
	private Button main_head_2;
	private Button main_head_3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_experts);
		setMainHeadTitle(getString(R.string.experts));
		
		main_head_1=(Button)findViewById(R.id.main_head_1);
		main_head_1.setText(getString(R.string.experts_list));
		main_head_1.setVisibility(View.VISIBLE);
		main_head_2=(Button)findViewById(R.id.main_head_2);
		main_head_2.setText(getString(R.string.experts_answer));
		main_head_2.setVisibility(View.VISIBLE);
		main_head_3=(Button)findViewById(R.id.main_head_3);
		main_head_3.setText(getString(R.string.experts_cover));
		main_head_3.setVisibility(View.VISIBLE);
		setHeadButtonEnabled(0);
		
 	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.main_head_1){
			//专家列表
			setHeadButtonEnabled(0);
		}else if(v.getId()==R.id.main_head_2){
			//专家解答
			setHeadButtonEnabled(1);
		}else if(v.getId()==R.id.main_head_3){
			//专家自荐
			setHeadButtonEnabled(2);
		}else{
			super.onClick(v);
		}
	}
	
	public void setHeadButtonEnabled(int index){
		main_head_1.setEnabled(index==0?false:true);
		main_head_2.setEnabled(index==1?false:true);
		main_head_3.setEnabled(index==2?false:true);
	}
	
}
