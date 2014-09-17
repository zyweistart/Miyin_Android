package com.start.medical;

import java.io.File;
import java.util.List;

import start.utils.FileUtils;
import start.utils.TimeUtils;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;

import com.start.core.BaseActivity;
import com.start.medical.main.MainActivity;

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
//				goLogin(true);
				Intent intent=new Intent(StartActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
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
