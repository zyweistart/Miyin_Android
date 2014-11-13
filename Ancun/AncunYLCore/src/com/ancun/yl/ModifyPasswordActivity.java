package com.ancun.yl;

import java.util.HashMap;
import java.util.Map;

import start.core.AppException;
import start.service.HttpRunnable;
import start.service.HttpServer;
import start.service.Response;
import start.utils.MD5;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ancun.core.BaseActivity;
import com.ancun.core.Constant;
import com.ancun.service.ButtonTextWatcher;
import com.ancun.service.User;

public class ModifyPasswordActivity extends BaseActivity {

	private EditText et_old_pwd,et_new_pwd,et_new_repwd;;
	private Button btn_submit;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modifypassword);
		setMainHeadTitle(getString(R.string.modifypassword));
		et_old_pwd =(EditText)findViewById(R.id.et_old_pwd);
		et_new_pwd =(EditText)findViewById(R.id.et_new_pwd);
		et_new_repwd =(EditText)findViewById(R.id.et_new_repwd);
		btn_submit =(Button)findViewById(R.id.btn_submit);
		et_old_pwd.addTextChangedListener(new ButtonTextWatcher(btn_submit));
    }

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_submit){
			String password=String.valueOf(et_old_pwd.getText());
			if(TextUtils.isEmpty(password)){
				getHandlerContext().makeTextLong(getString(R.string.passwordhint));
				return;
			}
			String newpassword=String.valueOf(et_new_pwd.getText());
			if(TextUtils.isEmpty(newpassword)){
				getHandlerContext().makeTextLong(getString(R.string.newpasswordhint));
				return;
			}
			String newrepassword=String.valueOf(et_new_repwd.getText());
			if(TextUtils.isEmpty(newrepassword)){
				getHandlerContext().makeTextLong(getString(R.string.repasswordhint));
				return;
			}
			if(!newpassword.equals(newrepassword)){
				et_new_pwd.setText(R.string.empty);
				et_new_repwd.setText(R.string.empty);
				getHandlerContext().makeTextLong(getString(R.string.twopwddifftip));
				return;
			}
			final String pwd=MD5.md5(newpassword);
			HttpServer hServer=new HttpServer(Constant.URL.ylcnuserpwdMod, getHandlerContext());
			Map<String,String> headers=new HashMap<String,String>();
			headers.put("sign", User.ACCESSKEY);
			hServer.setHeaders(headers);
			Map<String,String> params=new HashMap<String,String>();
			params.put("accessid", User.ACCESSID);
			params.put("phone", getAppContext().currentUser().getPhone());
			params.put("passwordold", MD5.md5(password));
			params.put("passwordnew",pwd);
			hServer.setParams(params);
			hServer.get(new HttpRunnable() {
				
				@Override
				public void run(Response response) throws AppException {
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							new AlertDialog.Builder(ModifyPasswordActivity.this)
							.setMessage(R.string.modify_password_success_tip)
							.setCancelable(false)
							.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									getAppContext().currentUser().changeCacheUser(pwd);
									finish();
								}
							}).show();
						}
						
					});
					
				}
				
			});
		}else{
			super.onClick(v);
		}
	}
    
}
