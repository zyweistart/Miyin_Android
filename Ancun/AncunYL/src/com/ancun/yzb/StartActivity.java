package com.ancun.yzb;

import java.io.File;
import java.util.List;

import start.utils.FileUtils;
import start.utils.LogUtils;
import start.utils.TimeUtils;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;

import com.ancun.core.BaseActivity;
import com.ancun.core.Constant.Preferences;


/**
 * 开始页
 * @author start
 *
 */
public class StartActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        final View view = View.inflate(this, R.layout.activity_start, null);
		LinearLayout wellcome = (LinearLayout) view.findViewById(R.id.start_view);
		check(wellcome);
		setContentView(view);
        
		//渐变展示启动屏
		AlphaAnimation aa = new AlphaAnimation(0.3f,1.0f);
		aa.setDuration(3000);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener(){
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationStart(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				try {
					PackageInfo packInfo = getPackageManager().getPackageInfo(getPackageName(),0);
					int curVersionCode=packInfo.versionCode;
					if(curVersionCode>BaseContext.getSharedPreferences().getInteger(Preferences.SP_CURRENTVERSIONCODE,0)){
						startActivity(new Intent(StartActivity.this,GuideActivity.class));
					}else{
						startActivity(new Intent(StartActivity.this, LockActivity.class));
					}
					//更新当前版本号
					BaseContext.getSharedPreferences().putInteger(Preferences.SP_CURRENTVERSIONCODE, curVersionCode);
					finish();
				} catch (NameNotFoundException e) {
					LogUtils.logError(e);
				}
			}
			
		});
 	}

	/**
     * 检查是否需要换图片
     * @param view
     */
	private void check(LinearLayout view) {
	    	String path = FileUtils.getAppCache(this, "wellcomeback");
	    	List<File> files = FileUtils.listPathFiles(path);
	    	if (!files.isEmpty()) {
	    		File f = files.get(0);
	    		long time[] = getTime(f.getName());
	    		long today = TimeUtils.getToday();
	    		if (today >= time[0] && today <= time[1]) {
	    			view.setBackground(Drawable.createFromPath(f.getAbsolutePath()));
	    		}
	    	}
    }
    
    /**
     * 分析显示的时间
     */
    private long[] getTime(String time) {
	    	long res[] = new long[2];
	    	try {
	    		time = time.substring(0, time.indexOf("."));
	        	String t[] = time.split("-");
	        	res[0] = Long.parseLong(t[0]);
	        	if (t.length >= 2) {
	        		res[1] = Long.parseLong(t[1]);
	        	} else {
	        		res[1] = Long.parseLong(t[0]);
	        	}
			} catch (Exception e) {
			}
	    	return res;
    }
}
