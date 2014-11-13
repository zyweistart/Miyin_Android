package com.ancun.unicom;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.ancun.core.BaseActivity;
import com.ancun.unicom.layout.RecordingContentView;

public class SearchRecordedActivity extends BaseActivity{

	private EditText etPhone;
	private EditText etRemark;
	private EditText etStartDay;
	private EditText etEndDay;
	private ImageButton btnStartDay;
	private ImageButton btnEndDay;
	private Button btnSearch;

	private String phoneContent;
	private String remarkContent;
	private String startDayContent;
	private String endDayContent;
	
	private int startYear;
	private int startMonth;
	private int startDay;
	private int endYear;
	private int endMonth;
	private int endDay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchrecorded);

		etPhone = (EditText) findViewById(R.id.search_content_phone);
		etRemark = (EditText) findViewById(R.id.search_content_remark);
		etStartDay = (EditText) findViewById(R.id.search_content_startday);
		etEndDay = (EditText) findViewById(R.id.search_content_endday);
		
		btnStartDay = (ImageButton) findViewById(R.id.search_content_btn_startday);
		btnStartDay.setOnClickListener(this);
		btnEndDay = (ImageButton) findViewById(R.id.search_content_btn_endday);
		btnEndDay.setOnClickListener(this);
		btnSearch = (Button) findViewById(R.id.search_content_btnSearch);
		btnSearch.setOnClickListener(this);
		
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			phoneContent=bundle.getString(RecordingContentView.SEARCH_CONTENT_FIELD_PHONE);
			etPhone.setText(phoneContent);
			
			remarkContent=bundle.getString(RecordingContentView.SEARCH_CONTENT_FIELD_REMARK);
			etRemark.setText(remarkContent);
			
			startDayContent=bundle.getString(RecordingContentView.SEARCH_CONTENT_FIELD_STARTDAY);
			if("".equals(startDayContent)){
				Calendar c = Calendar.getInstance();
				startYear = c.get(Calendar.YEAR);
				startMonth = c.get(Calendar.MONTH)+1;
				startDay = c.get(Calendar.DAY_OF_MONTH);
			}else{
				startYear=Integer.parseInt(startDayContent.substring(0,4));
				startMonth=Integer.parseInt(startDayContent.substring(4,6));
				startDay=Integer.parseInt(startDayContent.substring(6,8));
				etStartDay.setText(getDateDisplayFormat(startYear,startMonth,startDay,"-"));
			}
			
			endDayContent=bundle.getString(RecordingContentView.SEARCH_CONTENT_FIELD_ENDDAY);
			if("".equals(endDayContent)){
				Calendar c = Calendar.getInstance();
				endYear = c.get(Calendar.YEAR);
				endMonth = c.get(Calendar.MONTH)+1;
				endDay = c.get(Calendar.DAY_OF_MONTH);
			}else{
				endYear=Integer.parseInt(endDayContent.substring(0,4));
				endMonth=Integer.parseInt(endDayContent.substring(4,6));
				endDay=Integer.parseInt(endDayContent.substring(6,8));
				etEndDay.setText(getDateDisplayFormat(endYear,endMonth,endDay,"-"));
			}
			
		}
		
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.search_content_btn_startday){
			DatePickerDialog dialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					
					String enddy=String.valueOf(etEndDay.getText());
					if(!"".equals(enddy)){
						int endY=Integer.parseInt(enddy.substring(0,4));
						int endM=Integer.parseInt(enddy.substring(5,7));
						int endD=Integer.parseInt(enddy.substring(8,10));
						long sd=Long.parseLong(getDateDisplayFormat(year,monthOfYear+1,dayOfMonth,""));
						long ed=Long.parseLong(getDateDisplayFormat(endY,endM,endD,""));
						if(sd>ed){
							getHandlerContext().makeTextLong("开始日期不能大于结束日期");
							etStartDay.setText("");
							return;
						}
					}
					startYear = year;
					startMonth = monthOfYear+1;
					startDay = dayOfMonth;
					etStartDay.setText(getDateDisplayFormat(startYear,startMonth,startDay,"-"));
				}
				
			}, startYear, startMonth-1,startDay);
			dialog.show();
		}else if(v.getId()==R.id.search_content_btn_endday){
			DatePickerDialog dialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					
					String startdy=String.valueOf(etStartDay.getText());
					if(!"".equals(startdy)){
						int startY=Integer.parseInt(startdy.substring(0,4));
						int startM=Integer.parseInt(startdy.substring(5,7));
						int startD=Integer.parseInt(startdy.substring(8,10));
						long sd=Long.parseLong(getDateDisplayFormat(startY,startM,startD,""));
						long ed=Long.parseLong(getDateDisplayFormat(year,monthOfYear+1,dayOfMonth,""));
						if(sd>ed){
							getHandlerContext().makeTextLong("开始日期不能大于结束日期");
							etEndDay.setText("");
							return;
						}
					}
					endYear = year;
					endMonth = monthOfYear+1;
					endDay = dayOfMonth;
					etEndDay.setText(getDateDisplayFormat(endYear,endMonth,endDay,"-"));
				}
			}, endYear, endMonth-1,endDay);
			dialog.show();
		}else if(v.getId()==R.id.search_content_btnSearch){
			
			String tphone=String.valueOf(etPhone.getText());
			String tremark=String.valueOf(etRemark.getText());
			String startdy=String.valueOf(etStartDay.getText());
			String enddy=String.valueOf(etEndDay.getText());
			
			getInputMethodManager().toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
			
			Bundle bundle=new Bundle();
			bundle.putString(RecordingContentView.SEARCH_CONTENT_FIELD_PHONE, String.valueOf(tphone));
			bundle.putString(RecordingContentView.SEARCH_CONTENT_FIELD_REMARK, String.valueOf(tremark));
			if("".equals(startdy)){
				bundle.putString(RecordingContentView.SEARCH_CONTENT_FIELD_STARTDAY, "");
			}else{
				startYear=Integer.parseInt(startdy.substring(0,4));
				startMonth=Integer.parseInt(startdy.substring(5,7));
				startDay=Integer.parseInt(startdy.substring(8,10));
				bundle.putString(RecordingContentView.SEARCH_CONTENT_FIELD_STARTDAY, getDateDisplayFormat(startYear,startMonth,startDay,""));
			}
			if("".equals(enddy)){
				bundle.putString(RecordingContentView.SEARCH_CONTENT_FIELD_ENDDAY, "");
			}else{
				endYear=Integer.parseInt(enddy.substring(0,4));
				endMonth=Integer.parseInt(enddy.substring(5,7));
				endDay=Integer.parseInt(enddy.substring(8,10));
				bundle.putString(RecordingContentView.SEARCH_CONTENT_FIELD_ENDDAY, getDateDisplayFormat(endYear,endMonth,endDay,""));
			}
			Intent data=new Intent();
			data.putExtras(bundle);
			setResult(RecordingContentView.RESULTCODE_SEARCHREUSLT,data);
			finish();
		}
	}
	
	private String getDateDisplayFormat(int year,int month,int day,String split){
		return new StringBuilder().append(year).append(split)
				.append((month ) < 10 ? "0" + (month ) : (month ))
				.append(split).append((day < 10) ? "0" + day : day).toString();
	}

}