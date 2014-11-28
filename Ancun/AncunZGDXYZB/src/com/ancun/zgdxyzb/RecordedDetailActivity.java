package com.ancun.zgdxyzb;

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
import com.ancun.zgdxyzb.adapter.RecordingAdapter;

public class RecordedDetailActivity extends BaseActivity {

	public static final int TAOBAOREQUESTCODE=0xAB001;
	public static final int REMARKRESULTCODE=0xAA001;
	public static final int REMARKMODIFYCODE=0xAA002;
	
	private String fileno;
	private String cerflag;
	private String accstatus;
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
		fileno=getIntent().getExtras().getString(RecordingAdapter.RECORDED_FILENO);
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
			
			HttpServer hServer=new HttpServer(Constant.URL.ylcnrecQrySingle, getHandlerContext());
			Map<String,String> headers=new HashMap<String,String>();
			headers.put("sign", User.ACCESSKEY);
			hServer.setHeaders(headers);
			Map<String,String> params=new HashMap<String,String>();
			params.put("accessid", User.ACCESSID);
			params.put("ownerno",getAppContext().currentUser().getPhone());
			params.put("fileno",fileno);
			hServer.setParams(params);
			hServer.get(new HttpRunnable() {
				
				@Override
				public void run(Response response) throws AppException {
					
					final Map<String,String> info=response.getMapData("recordinfo");
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							//主叫号码
							tvrecorded_remark_calling.setText(info.get(RecordingAdapter.RECORDED_CALLERNO));
							//被叫号码
							tvrecorded_remark_called.setText(info.get(RecordingAdapter.RECORDED_CALLEDNO));
							//起始时间
							tvrecorded_remark_start_time.setText(info.get(RecordingAdapter.RECORDED_BEGINTIME));
							//结束时间
							tvrecorded_remark_end_time.setText(info.get(RecordingAdapter.RECORDED_ENDTIME));
							//录音失效时间
							String recendtime=info.get(RecordingAdapter.RECORDED_RECENDTIME);
							if(TextUtils.isEmpty(recendtime)) {
								recorderInvalidTime.setText(R.string.not_expired);
							} else {
								recorderInvalidTime.setText(recendtime);
							}
							//录音时长
							tvrecorded_remark_length.setText(TimeUtils.secondConvertTime(Integer.parseInt((info.get(RecordingAdapter.RECORDED_DURATION)))));
							//录音备注
							etrecorded_remark_edit.setText(info.get(RecordingAdapter.RECORDED_REMARK));
							//公证标记
							setNotaryStatus(info.get(RecordingAdapter.RECORDED_CEFFLAG));
							//申请码状态
							setExtractionStatus(info.get(RecordingAdapter.RECORDED_ACCSTATUS));
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
			//关闭软键盘
			if(getInputMethodManager().isActive()){
				getInputMethodManager().hideSoftInputFromWindow(etrecorded_remark_edit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
			if (isEdit) {
 				final String remark=String.valueOf(etrecorded_remark_edit.getText());
 				//提交修改备注
 				HttpServer hServer=new HttpServer(Constant.URL.ylcnrecRemark, getHandlerContext());
 				Map<String,String> headers=new HashMap<String,String>();
 				headers.put("sign", User.ACCESSKEY);
 				hServer.setHeaders(headers);
 				Map<String,String> params=new HashMap<String,String>();
 				params.put("accessid", User.ACCESSID);
 				params.put("ownerno",getAppContext().currentUser().getPhone());
 				params.put("fileno",fileno);
 				params.put("remark", remark);
 				hServer.setParams(params);
 				hServer.get(new HttpRunnable() {
 					
 					@Override
 					public void run(Response response) throws AppException {
 						
 						getHandlerContext().makeTextLong(getString(R.string.recording_remark_modify_success));
 						
 					}
 				});
			}
			isEdit=!isEdit;
			setRemarkEditStatus(isEdit);
			break;
		case R.id.recorded_taobao_appeal:
			Bundle bundleTaobao=new Bundle();
			bundleTaobao.putString(RecordingAdapter.RECORDED_FILENO, fileno);
			bundleTaobao.putString(RecordingAdapter.RECORDED_ACCSTATUS, accstatus);
			Intent intentTaobao;
			if("1".equals(accstatus)){
				//有效
				intentTaobao=new Intent(this,ExtractionViewActivity.class);
			}else{
				bundleTaobao.putInt(ExtractionConfirmActivity.STRAPPEALTYPE, 1);
				intentTaobao=new Intent(this,ExtractionConfirmActivity.class);
			}
			intentTaobao.putExtras(bundleTaobao);
			startActivityForResult(intentTaobao,TAOBAOREQUESTCODE);
			break;
		case R.id.recorded_notarization_appeal:
			//申请公证
			Bundle bundleNotary=new Bundle();
			bundleNotary.putInt(ExtractionConfirmActivity.STRAPPEALTYPE, 2);
			bundleNotary.putString(RecordingAdapter.RECORDED_FILENO, fileno);
			bundleNotary.putString(RecordingAdapter.RECORDED_CEFFLAG, cerflag);
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
						setExtractionStatus(bundle.getString(RecordingAdapter.RECORDED_ACCSTATUS));
					}
				}else{
					if(resultCode==3){
						setNotaryStatus(bundle.getString(RecordingAdapter.RECORDED_CEFFLAG));
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
	
	private void setExtractionStatus(String status){
		accstatus=status;
		if("1".equals(accstatus)){
			//已提取
			btnrecorded_taobao_appeal.setBackgroundResource(R.drawable.selector_button_extraction_lookup);
		}else{
			//未提取
			btnrecorded_taobao_appeal.setBackgroundResource(R.drawable.selector_button_extraction_request);
		}
	}
	
	private void setNotaryStatus(String status){
		cerflag=status;
		if("1".equals(cerflag)){
			//已出证
			btnrecorded_notarization_appeal.setBackgroundResource(R.drawable.selector_button_notary_cancel);
		}else{
			//未出证
			btnrecorded_notarization_appeal.setBackgroundResource(R.drawable.selector_button_notary_request);
		}
	}
	
}
