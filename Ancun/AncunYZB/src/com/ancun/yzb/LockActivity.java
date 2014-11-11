package com.ancun.yzb;

import java.util.List;

import start.core.AppConstant;
import start.utils.StringUtils;
import start.widget.LockPatternView;
import start.widget.LockPatternView.Cell;
import start.widget.LockPatternView.DisplayMode;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.ancun.core.BaseActivity;
import com.ancun.core.Constant;
import com.ancun.core.Constant.Preferences;

/**
 * @author Start   
 * @Description: 手势密码解锁
 */
public class LockActivity extends BaseActivity implements LockPatternView.OnPatternListener{

    private List<Cell> lockPattern;
    private LockPatternView lockPatternView;
    private int errornum;
    private TextView txt_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String account=BaseContext.getSharedPreferences().getString(Constant.Preferences.SP_ACCOUNT_CONTENT_DATA, AppConstant.EMPTYSTR);
        String password=BaseContext.getSharedPreferences().getString(Constant.Preferences.SP_PASSWORD_CONTENT_DATA, AppConstant.EMPTYSTR);
        Boolean autoLogin=BaseContext.getSharedPreferences().getBoolean(Preferences.SP_AUTOLOGIN_CONTENT_DATA, false);
        String patternString = BaseContext.getSharedPreferences().getString(Constant.Preferences.SP_LOCK_KEY_DATA, AppConstant.EMPTYSTR);
		if (StringUtils.isEmpty(account)||StringUtils.isEmpty(password)||StringUtils.isEmpty(patternString)||!autoLogin) {
			goLogin(true);
            return;
        }
        lockPattern = LockPatternView.stringToPattern(patternString);
        setContentView(R.layout.activity_lock);
        lockPatternView = (LockPatternView) findViewById(R.id.lock_pattern);
        lockPatternView.setOnPatternListener(this);
        errornum=0;
        txt_account=(TextView)findViewById(R.id.txt_account);
        txt_account.setText(account);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPatternStart() {
    	
    }

    @Override
    public void onPatternCleared() {
    	
    }

    @Override
    public void onPatternCellAdded(List<Cell> pattern) {
    	
    }

    @Override
    public void onPatternDetected(List<Cell> pattern) {
        if (pattern.equals(lockPattern)) {
        	//手势解锁成功
        	goLogin(true);
        } else {
        	//手势解锁失败
            lockPatternView.setDisplayMode(DisplayMode.Wrong);
//            getHandlerContext().makeTextLong(getString(R.string.lockpattern_error));
            
            errornum++;
            
            if(errornum>3){
            	getAppContext().currentUser().clearCacheUser();
            	goLogin(false);
            	return;
            }
            
            new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					lockPatternView.clearPattern();
		            lockPatternView.enableInput();
				}
            	
            },1500);
            
        }
    }

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.txt_forgetgesutepassword){
			getAppContext().currentUser().clearCachePassword();
			BaseContext.getSharedPreferences().putBoolean(Preferences.SP_IS_RESET_LOCK_KEY, true);
			goLogin(false);
		}else if(v.getId()==R.id.txt_otheraccountlogin){
			goLogin(false);
		}else{
			super.onClick(v);
		}
	}
    
}