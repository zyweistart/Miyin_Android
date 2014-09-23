//package com.start.medical.personal;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import start.core.AppException;
//import start.service.HttpServer;
//import start.service.Response;
//import start.service.UIRunnable;
//import start.widget.CustomEditText;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.RadioGroup;
//
//import com.start.core.BaseActivity;
//import com.start.core.Constant;
//import com.start.medical.R;
//import com.start.service.User;
//
///**
// * 编辑个人信息
// * @author start
// *
// */
//public class EditPersonalActivity extends BaseActivity {
//
//	private CustomEditText et_name;
//	private CustomEditText et_mobile;
//	private RadioGroup et_sex;
//	private CustomEditText et_icard;
//	private CustomEditText et_hcno;
//	private CustomEditText et_mcno;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
////		if(!getAppContext().currentUser().isLogin()){
////			goLogin(getString(R.string.not_login_message));
////			return;
////		}
//		setContentView(R.layout.activity_edit_personal);
//		setMainHeadTitle("编辑个人资料");
//		et_name=(CustomEditText)findViewById(R.id.et_name);
//		et_mobile=(CustomEditText)findViewById(R.id.et_mobile);
//		et_sex=(RadioGroup)findViewById(R.id.et_sex);
//		et_icard=(CustomEditText)findViewById(R.id.et_icard);
//		et_hcno=(CustomEditText)findViewById(R.id.et_hcno);
//		et_mcno=(CustomEditText)findViewById(R.id.et_mcno);
//	}
//	
//	@Override
//	public void onClick(View v) {
//		if(v.getId()==R.id.btn_update){
//			String name=String.valueOf(et_name.getText());
//			if(!TextUtils.isEmpty(name)){
//				getHandlerContext().makeTextLong("请输入姓名");
//				return;
//			}
//			String mobile=String.valueOf(et_mobile.getText());
//			if(!TextUtils.isEmpty(name)){
//				return;
//			}
//			String icard=String.valueOf(et_icard.getText());
//			if(!TextUtils.isEmpty(name)){
//				return;
//			}
//			String hcno=String.valueOf(et_hcno.getText());
//			if(!TextUtils.isEmpty(name)){
//				return;
//			}
//			String mcno=String.valueOf(et_mcno.getText());
//			if(!TextUtils.isEmpty(mcno)){
//				return;
//			}
//			
//			HttpServer hServer=new HttpServer(Constant.URL.hisinfoBind, getHandlerContext());
//			Map<String,String> headers=new HashMap<String,String>();
//			headers.put("sign", User.ACCESSKEY);
//			hServer.setHeaders(headers);
//			Map<String, String> params = new HashMap<String, String>();
//			params.put("accessid", User.ACCESSID);
//			params.put("name", name);
//			params.put("pmobile", mobile);
//			params.put("sex", mobile);
//			params.put("idno", icard);
//			params.put("hcno", hcno);
//			params.put("mcno", mobile);
//			hServer.setParams(params);
//			hServer.get(new UIRunnable() {
//				
//				@Override
//				public void run(Response response) throws AppException {
//					
//					getHandlerContext().makeTextLong("还好");
//					
//				}
//				
//			});
//			
//		}else{
//			super.onClick(v);
//		}
//	}
//	
//}