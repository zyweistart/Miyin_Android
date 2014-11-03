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

	private ClipboardManager clipboard;
	
	private TextView tv_recorded_appeal_taobao_code_url;
	private TextView tv_recorded_appeal_taobao_code;
	private TextView tv_recorded_appeal_taobao_limit_time;
	private Button btn_recorded_appeal_taobao_btn_copy;
	private Button btn_recorded_appeal_taobao_btn_send_to_mobile;
	private Button btn_recorded_appeal_taobao_btn_cancel;
	
	private String fileno;
	
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
				
				final Map<String,String> info=response.getMapData("recinfo");
				
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
							getHandlerContext().makeTextLong("撤销成功！");
							finish();
						}
					}
				});
				
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		final String strContent=tv_recorded_appeal_taobao_code_url.getText().toString()+tv_recorded_appeal_taobao_code.getText().toString();
		switch(v.getId()){
		case R.id.recorded_appeal_taobao_btn_copy:
			//复制内容
			clipboard.setText(strContent);
			btn_recorded_appeal_taobao_btn_copy.setEnabled(false);
			getHandlerContext().makeTextLong("复制成功");
			break;
		case R.id.recorded_appeal_taobao_btn_send_to_mobile:
//			 boolean SP_ALIYUN_SENDMESSAGE_MESSAGE=getAppContext().getSharedPreferencesUtils().getBoolean(Constant.SharedPreferencesConstant.SP_ALIYUN_SENDMESSAGE_MESSAGE,true);
//     		if(SP_ALIYUN_SENDMESSAGE_MESSAGE){
//     			final CheckBox cb=new CheckBox(this);
//     			cb.setText("不再显示提醒");
//     			new AlertDialog.Builder(this)
//     			.setIcon(android.R.drawable.ic_dialog_info)
//     			.setCancelable(false)
//     			.setMessage("该条录音提取码信息将启用手机短信生成功能并以手机短信形式发给对方，短信费用由运营商收取，确认以手机短信形式发送给对方？")
//     			.setView(cb)
//     			.setPositiveButton("否", new DialogInterface.OnClickListener() {
//     				@Override
//     				public void onClick(DialogInterface dialog, int which) {
//     					dialog.dismiss();
//     				}
//     			}).setNegativeButton("是", new DialogInterface.OnClickListener() {
//     				@Override
//     				public void onClick(DialogInterface dialog, int which) {
//     					if(cb.isChecked()){
//     						getAppContext().getSharedPreferencesUtils().putBoolean(Constant.SharedPreferencesConstant.SP_ALIYUN_SENDMESSAGE_MESSAGE,false);
//     					}
//     					 // 联系人地址 
//     		             Uri smsToUri = Uri.parse("smsto:");
//     		             Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO, 
//     		             smsToUri); 
//     		             
//     		             String strContent1="您申请的录音提取码为："+strContent+" ，凭该提取码可在官网公开查询、下载本条通话录音，请妥善保管。客服电话:968682【安存科技】";
//     		             
//     		             // 短信内容 
//     		             mIntent.putExtra("sms_body",strContent1);
//     		             startActivity(mIntent); 
//     				}
//     			}).show();
//     		}else{
//     			 // 联系人地址 
//                Uri smsToUri = Uri.parse("smsto:");
//                Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO, 
//                smsToUri); 
//                
//                String strContent1="您申请的录音提取码为："+strContent+" ，凭该提取码可在官网公开查询、下载本条通话录音，请妥善保管。客服电话:968682【安存科技】";
//                
//                // 短信内容 
//                mIntent.putExtra("sms_body",strContent1);
//                startActivity(mIntent); 
//     		}
     		 // 联系人地址 
            Uri smsToUri = Uri.parse("smsto:");
            Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO, 
            smsToUri); 
            
            String strContent1="您申请的录音提取码为："+strContent+" ，凭该提取码可在官网公开查询、下载本条通话录音，请妥善保管。客服电话:968682【安存科技】";
            
            // 短信内容 
            mIntent.putExtra("sms_body",strContent1);
            startActivity(mIntent); 
			break;
		case R.id.recorded_appeal_taobao_btn_cancel:
			AlertDialog.Builder aDialog = new AlertDialog.Builder(this);
			aDialog.
			setIcon(android.R.drawable.ic_dialog_info).
			setMessage("是否撤销提取？").
			setPositiveButton(R.string.cancle, null)
			.setNeutralButton(R.string.sure, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					getDataTask(3);
				}
			}).show();
			break;
		}
	}
}