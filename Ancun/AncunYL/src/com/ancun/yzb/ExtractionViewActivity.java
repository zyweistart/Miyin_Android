package com.ancun.yzb;

import java.util.HashMap;
import java.util.Map;

import start.core.AppException;
import start.service.HttpRunnable;
import start.service.HttpServer;
import start.service.Response;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ancun.core.BaseActivity;
import com.ancun.core.Constant;
import com.ancun.service.User;
import com.ancun.yzb.layout.RecordingAdapter;

/**
 * 提取申请码
 * @author Start
 */
@SuppressWarnings("deprecation")
public class ExtractionViewActivity extends BaseActivity implements OnClickListener {
	
	public static final int RecordedAppealTaobaoExtractionCodeResultCode=0xAC0012;

	private String fileno;
	
	private ClipboardManager clipboard;
	
	private TextView tv_recorded_appeal_taobao_code_url;
	private TextView tv_recorded_appeal_taobao_code;
	private TextView tv_recorded_appeal_taobao_limit_time;
	private Button btn_recorded_appeal_taobao_btn_copy;
	private Button btn_recorded_appeal_taobao_btn_send_to_mobile;
	private Button btn_recorded_appeal_taobao_btn_cancel;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_extraction_view);
		clipboard=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
		tv_recorded_appeal_taobao_code_url=(TextView)findViewById(R.id.recorded_appeal_taobao_code_url);
		tv_recorded_appeal_taobao_code=(TextView)findViewById(R.id.recorded_appeal_taobao_code);
		tv_recorded_appeal_taobao_limit_time=(TextView)findViewById(R.id.recorded_appeal_taobao_limit_time);
		btn_recorded_appeal_taobao_btn_copy=(Button)findViewById(R.id.recorded_appeal_taobao_btn_copy);
		btn_recorded_appeal_taobao_btn_copy.setOnClickListener(this);
		btn_recorded_appeal_taobao_btn_send_to_mobile=(Button)findViewById(R.id.recorded_appeal_taobao_btn_send_to_mobile);
		btn_recorded_appeal_taobao_btn_send_to_mobile.setOnClickListener(this);
		btn_recorded_appeal_taobao_btn_cancel=(Button)findViewById(R.id.recorded_appeal_taobao_btn_cancel);
		btn_recorded_appeal_taobao_btn_cancel.setOnClickListener(this);
		fileno=getIntent().getExtras().getString("fileno");
		if(fileno!=null){
			Integer accstatus=getIntent().getExtras().getInt(RecordingAdapter.RECORDED_ACCSTATUS);
			if(accstatus==1){
				setMainHeadTitle(getString(R.string.view_extractioncode));
				//有效
				getDataTask(2);
			}else{
				setMainHeadTitle(getString(R.string.reg_extractioncode));
				//生成
				getDataTask(1);
			}
		}
	}
	
	public void getDataTask(final Integer status){
		HttpServer hServer=new HttpServer(Constant.URL.v4recAcccode, getHandlerContext());
		Map<String,String> headers=new HashMap<String,String>();
		headers.put("sign", User.ACCESSKEY);
		hServer.setHeaders(headers);
		Map<String,String> params=new HashMap<String,String>();
		params.put("accessid", User.ACCESSID);
		params.put("fileno",fileno);
		params.put("acccodeact", String.valueOf(status));
		params.put("vtime","10");
		hServer.setParams(params);
		hServer.get(new HttpRunnable() {
			
			@Override
			public void run(Response response) throws AppException {
				
				final Map<String,String> info=response.getMapData("acccodeinfo");
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Bundle resultBundle=new Bundle();
						resultBundle.putString(RecordingAdapter.RECORDED_FILENO, fileno);
						if(status==1||status==2){
							tv_recorded_appeal_taobao_code_url.setText(info.get("url"));
							tv_recorded_appeal_taobao_code.setText(info.get("acccode"));
							tv_recorded_appeal_taobao_limit_time.setText(info.get("endtime"));
							resultBundle.putInt(RecordingAdapter.RECORDED_ACCSTATUS,1);
							Intent resultIntent=new Intent();
							resultIntent.putExtras(resultBundle);
							setResult(RecordedAppealTaobaoExtractionCodeResultCode,resultIntent);
						}else if(status==3){
							resultBundle.putInt(RecordingAdapter.RECORDED_ACCSTATUS,2);
							Intent resultIntent=new Intent();
							resultIntent.putExtras(resultBundle);
							setResult(RecordedAppealTaobaoExtractionCodeResultCode,resultIntent);
							getHandlerContext().makeTextLong(getString(R.string.undo_success));
							finish();
						}
					}
				});
				
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		String strContent=String.valueOf(tv_recorded_appeal_taobao_code_url.getText())+
				String.valueOf(tv_recorded_appeal_taobao_code.getText());
		if(v.getId()==R.id.recorded_appeal_taobao_btn_copy){
			clipboard.setText(strContent);
			btn_recorded_appeal_taobao_btn_copy.setEnabled(false);
			getHandlerContext().makeTextShort(getString(R.string.copy_success));
		}else if(v.getId()==R.id.recorded_appeal_taobao_btn_send_to_mobile){
			 Uri smsToUri = Uri.parse("smsto:");
	            Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO, smsToUri); 
	            String strContent1="您申请的录音提取码为："+strContent+" ，凭该提取码可在官网公开查询、下载本条通话录音，请妥善保管。客服电话:"+getString(R.string.app_phone)+"【"+getString(R.string.app_compayname)+"】";
	            mIntent.putExtra("sms_body",strContent1);
	            startActivity(mIntent); 
		}else if(v.getId()==R.id.recorded_appeal_taobao_btn_cancel){
			new AlertDialog.Builder(this)
			.setIcon(android.R.drawable.ic_dialog_info).
			setMessage(R.string.undo_extraction_code_sure).
			setPositiveButton(R.string.cancle, null)
			.setNeutralButton(R.string.sure, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					getDataTask(3);
				}
			}).show();
		}else{
			super.onClick(v);
		}
	}
}