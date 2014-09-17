package com.start.medical.department;

import java.util.HashMap;
import java.util.Map;

import start.core.AppException;
import start.service.HttpServer;
import start.service.Response;
import start.service.UIRunnable;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.start.core.BaseActivity;
import com.start.core.Constant;
import com.start.medical.R;
import com.start.service.User;

/**
 * 科室医生
 * @author start
 *
 */
public class DoctorsDetailActivity extends BaseActivity {
	
	public static final String RECORDNO="userno";
	
	private String userno;
	
	private ImageView iv_header;
	private TextView txt_name;
	private TextView txt_desc;
	private TextView txt_thefield;
	private TextView txt_outtime;
	private TextView txt_outlocat;
	private TextView txt_regfee;
	private TextView txt_otherinfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor_detail);
		setMainHeadTitle(getString(R.string.mainfunctiontxt6));
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			userno=bundle.getString(RECORDNO);
		}
		if(TextUtils.isEmpty(userno)){
			finish();
		}
		
		iv_header=(ImageView)findViewById(R.id.iv_header);
		txt_name=(TextView)findViewById(R.id.txt_name);
		txt_desc=(TextView)findViewById(R.id.txt_desc);
		txt_thefield=(TextView)findViewById(R.id.txt_thefield);
		txt_outtime=(TextView)findViewById(R.id.txt_outtime);
		txt_outlocat=(TextView)findViewById(R.id.txt_outlocat);
		txt_regfee=(TextView)findViewById(R.id.txt_regfee);
		txt_otherinfo=(TextView)findViewById(R.id.txt_otherinfo);
		
		this.loadData();
		
	}
	
	public void loadData(){
		HttpServer hServer=new HttpServer(Constant.URL.hisdoctorDetail, getHandlerContext());
		Map<String,String> headers=new HashMap<String,String>();
		headers.put("sign", User.COMMON_ACCESSKEY_LOCAL);
		hServer.setHeaders(headers);
		Map<String, String> params = new HashMap<String, String>();
		params.put("accessid", User.COMMON_ACCESSID_LOCAL);
		params.put("userno", userno);
		hServer.setParams(params);
		hServer.get(new UIRunnable() {
			
			@Override
			public void run(Response response) throws AppException {

				final Map<String,String> data=response.getMapData("doctorinfo");
				
				runOnUiThread(new Runnable() {
					
					public void run() {
						iv_header.setBackground(getResources().getDrawable(R.drawable.ic_expression_success));
						txt_name.setText(data.get("name"));
						txt_desc.setText(data.get("desc"));
						txt_thefield.setText(data.get("thefield"));
						txt_outtime.setText(data.get("outtime"));
						txt_outlocat.setText(data.get("outlocat"));
						txt_regfee.setText(data.get("regfee"));
						txt_otherinfo.setText(data.get("otherinfo"));
					}
				});
				
			}
			
		});
	}
	
}
