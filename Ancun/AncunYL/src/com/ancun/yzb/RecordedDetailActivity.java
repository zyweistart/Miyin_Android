package com.ancun.yzb;

import java.util.HashMap;
import java.util.Map;

import start.core.AppException;
import start.service.HttpRunnable;
import start.service.HttpServer;
import start.service.Response;
import start.utils.TimeUtils;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ancun.core.BaseActivity;
import com.ancun.core.Constant;
import com.ancun.service.User;
import com.ancun.yzb.adapter.RecordingAdapter;

public class RecordedDetailActivity extends BaseActivity {

	public static final int TAOBAOREQUESTCODE=0xAB001;
	public static final int REMARKRESULTCODE=0xAA001;
	public static final int REMARKMODIFYCODE=0xAA002;
	
	private String fileno;
	private Integer cerflag;
	private Integer accstatus;
	private Boolean isEdit = false;

	private TextView tvrecorded_remark_calling;
	private TextView tvrecorded_remark_called;
	private TextView tvrecorded_remark_start_time;
	private TextView tvrecorded_remark_end_time;
	private TextView tvrecorded_remark_length;
	private EditText etrecorded_remark_edit;
	private TextView recorderInvalidTime ;
	private ImageButton btnrecorded_remark_edit_submit;
	private ImageButton btnrecorded_taobao_appeal;
	private ImageButton btnrecorded_notarization_appeal;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_recording_detail);
		setMainHeadTitle(getString(R.string.recording_detail));
		fileno=getIntent().getExtras().getString("fileno");
		if(fileno!=null){
			
			tvrecorded_remark_calling=(TextView)findViewById(R.id.recorded_remark_calling);
			tvrecorded_remark_called=(TextView)findViewById(R.id.recorded_remark_called);
			tvrecorded_remark_start_time=(TextView)findViewById(R.id.recorded_remark_start_time);
			tvrecorded_remark_end_time=(TextView)findViewById(R.id.recorded_remark_end_time);
			tvrecorded_remark_length=(TextView)findViewById(R.id.recorded_remark_length);
			etrecorded_remark_edit=(EditText)findViewById(R.id.recorded_remark_edit);
			recorderInvalidTime = (TextView)findViewById(R.id.recorded_remark_invalid_time);
			btnrecorded_remark_edit_submit=(ImageButton)findViewById(R.id.recorded_remark_edit_submit);
			btnrecorded_remark_edit_submit.setOnClickListener(this);
			btnrecorded_notarization_appeal=(ImageButton)findViewById(R.id.recorded_notarization_appeal);
			btnrecorded_notarization_appeal.setOnClickListener(this);
			btnrecorded_taobao_appeal=(ImageButton)findViewById(R.id.recorded_taobao_appeal);
			btnrecorded_taobao_appeal.setOnClickListener(this);
			setRemarkEditStatus(isEdit);
			
			HttpServer hServer=new HttpServer(Constant.URL.v4recGet, getHandlerContext());
			Map<String,String> headers=new HashMap<String,String>();
			headers.put("sign", User.ACCESSKEY);
			hServer.setHeaders(headers);
			Map<String,String> params=new HashMap<String,String>();
			params.put("accessid", User.ACCESSID);
			params.put("fileno",fileno);
			params.put("status", "1");
			hServer.setParams(params);
			hServer.get(new HttpRunnable() {
				
				@Override
				public void run(Response response) throws AppException {
					
					final Map<String,String> info=response.getMapData("recinfo");
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							//主叫号码
							tvrecorded_remark_calling.setText(info.get("callerno"));
							//被叫号码
							tvrecorded_remark_called.setText(info.get("calledno"));
							//起始时间
							tvrecorded_remark_start_time.setText(info.get("begintime"));
							//结束时间
							tvrecorded_remark_end_time.setText(info.get("endtime"));
							//录音失效时间
							if(TextUtils.isEmpty(info.get("recendtime"))) {
								recorderInvalidTime.setText(R.string.not_expired);
							} else {
								recorderInvalidTime.setText(info.get("recendtime"));
							}
							//录音时长
							tvrecorded_remark_length.setText(TimeUtils.secondConvertTime(Integer.parseInt((info.get("duration")))));
							//录音备注
							etrecorded_remark_edit.setText(info.get("remark"));
							//公证标记
							cerflag=Integer.parseInt(info.get("cerflag"));
							if(cerflag==1){
								btnrecorded_notarization_appeal.setBackgroundResource(R.drawable.selector_button_notary_request);
							}else{
								btnrecorded_notarization_appeal.setBackgroundResource(R.drawable.selector_button_notary_cancel);
							}
							//申请码状态
							accstatus=Integer.parseInt(info.get("accstatus"));
							if(accstatus==1){
								btnrecorded_taobao_appeal.setBackgroundResource(R.drawable.selector_button_extraction_lookup);
							}else{
								btnrecorded_taobao_appeal.setBackgroundResource(R.drawable.selector_button_extraction_request);
							}
							
						}
						
					});
				}
				
			});
			
		}else{
			finish();
		}
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.recorded_remark_edit_submit:
			if (isEdit) {
				//关闭软键盘
 				if (!getInputMethodManager().isActive()) {
 					getInputMethodManager().hideSoftInputFromWindow(etrecorded_remark_edit.getWindowToken(), 0);
 				}
 				final String remark=String.valueOf(etrecorded_remark_edit.getText());
 				//提交修改备注
 				HttpServer hServer=new HttpServer(Constant.URL.v4recRemark, getHandlerContext());
 				Map<String,String> headers=new HashMap<String,String>();
 				headers.put("sign", User.ACCESSKEY);
 				hServer.setHeaders(headers);
 				Map<String,String> params=new HashMap<String,String>();
 				params.put("accessid", User.ACCESSID);
 				params.put("fileno",fileno);
 				params.put("remark", remark);
 				hServer.setParams(params);
 				hServer.get(new HttpRunnable() {
 					
 					@Override
 					public void run(Response response) throws AppException {
 						
 						Intent intent=new Intent();
 						Bundle bundle=new Bundle();
 						bundle.putString("fileno", fileno);
 						bundle.putInt("accstatus", accstatus);
 						bundle.putInt("cerflag", cerflag);
 						bundle.putString("remark", remark);
 						intent.putExtras(bundle);
 						setResult(REMARKMODIFYCODE,intent);
 						getHandlerContext().makeTextLong(getString(R.string.recording_remark_modify_success));
 						
 					}
 				});
			}else {
				//隐藏软键盘
				if (getInputMethodManager().isActive()) {
					getInputMethodManager().toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
			isEdit=!isEdit;
			setRemarkEditStatus(isEdit);
			break;
		case R.id.recorded_taobao_appeal:
			Bundle bundleTaobao=new Bundle();
			bundleTaobao.putString(RecordingAdapter.RECORDED_FILENO, fileno);
			bundleTaobao.putInt(RecordingAdapter.RECORDED_ACCSTATUS, accstatus);
			Intent intentTaobao;
			if(accstatus==1){
				//有效
				intentTaobao=new Intent(this,ExtractionViewActivity.class);
			}else{
				bundleTaobao.putInt("appeal_type", 1);
				intentTaobao=new Intent(this,ExtractionConfirmActivity.class);
			}
			intentTaobao.putExtras(bundleTaobao);
			startActivityForResult(intentTaobao,TAOBAOREQUESTCODE);
			break;
		case R.id.recorded_notarization_appeal:
			//申请公证
			Bundle bundleNotary=new Bundle();
			bundleNotary.putInt("appeal_type", 2);
			bundleNotary.putString("fileno", fileno);
			bundleNotary.putInt("cerflag", cerflag);
			Intent intentNotary=new Intent(this,ExtractionConfirmActivity.class);
			intentNotary.putExtras(bundleNotary);
			startActivityForResult(intentNotary,1);
			break;
			default:
				super.onClick(v);
				break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data!=null){
			Bundle bundle=data.getExtras();
			if(bundle!=null){
				if(TAOBAOREQUESTCODE==requestCode){
					if(resultCode==ExtractionViewActivity.RecordedAppealTaobaoExtractionCodeResultCode){
						accstatus=bundle.getInt("accstatus");
						if(accstatus==1){
							btnrecorded_taobao_appeal.setBackgroundResource(R.drawable.selector_button_extraction_lookup);
						}else{
							btnrecorded_taobao_appeal.setBackgroundResource(R.drawable.selector_button_extraction_request);
						}
						Intent intent=new Intent();
						Bundle taobaoBundle=new Bundle();
						taobaoBundle.putString("fileno", fileno);
						taobaoBundle.putInt("accstatus", accstatus);
						taobaoBundle.putInt("cerflag", cerflag);
						taobaoBundle.putString("remark", etrecorded_remark_edit.getText().toString());
						intent.putExtras(taobaoBundle);
						setResult(REMARKRESULTCODE,intent);
					}
				}else{
					if(resultCode==3){
						cerflag=bundle.getInt("cerflag");
						if(cerflag==1){
							btnrecorded_notarization_appeal.setBackgroundResource(R.drawable.selector_button_notary_request);
						}else{
							btnrecorded_notarization_appeal.setBackgroundResource(R.drawable.selector_button_notary_cancel);
						}
						Intent intent=new Intent();
						Bundle notaryBundle=new Bundle();
						notaryBundle.putString("fileno", fileno);
						notaryBundle.putInt("cerflag", cerflag);
						notaryBundle.putInt("accstatus", accstatus);
						notaryBundle.putString("remark", etrecorded_remark_edit.getText().toString());
						intent.putExtras(notaryBundle);
						setResult(REMARKRESULTCODE,intent);
					}
				}
			}
		}
	}
	
	/**
	 * 设置编辑框状态
	 */
	private void setRemarkEditStatus(Boolean isEditFlag) {
		if (isEditFlag) {
			//编辑状态
			etrecorded_remark_edit.setEnabled(true);
			etrecorded_remark_edit.setBackgroundResource(R.drawable.bg_edit);
			btnrecorded_remark_edit_submit.setBackgroundResource(R.drawable.selector_button_submit);
		}else {
			//查看状态
			etrecorded_remark_edit.setEnabled(false);
			etrecorded_remark_edit.setBackgroundResource(R.drawable.bg_lookup);
			btnrecorded_remark_edit_submit.setBackgroundResource(R.drawable.selector_button_edit);
		}
	}
	
}
