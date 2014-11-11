package com.ancun.yzb;

import java.util.ArrayList;
import java.util.List;

import start.widget.LockPatternView;
import start.widget.LockPatternView.Cell;
import start.widget.LockPatternView.DisplayMode;
import android.os.Bundle;
import android.os.Handler;

import com.ancun.core.BaseActivity;
import com.ancun.core.Constant;

/**
 * @author Start   
 * @Description: 手势密码设置
 * @ClassName: LockSetupActivity.java   
 * @date 2014年7月23日 下午2:28:58      
 * @说明  代码版权归 杭州反盗版中心有限公司 所有
 */
public class LockSetupActivity extends BaseActivity implements LockPatternView.OnPatternListener {
	
    private static final int STEP_1 = 1; // 开始
    private static final int STEP_2 = 2; // 第一次设置手势完成
    private static final int STEP_3 = 3; // 按下继续按钮
    private static final int STEP_4 = 4; // 第二次设置手势完成

    private int step;
    private boolean confirm;
    private List<Cell> choosePattern;
    
    private LockPatternView lockPatternView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_setup);
        setMainHeadTitle(getString(R.string.gestureset));
        
//        this.setBackButton();
        
        lockPatternView = (LockPatternView) findViewById(R.id.lock_pattern);
        lockPatternView.setOnPatternListener(this);
        
        step = STEP_1;
        updateView();
    }

    private void updateView() {
        switch (step) {
        case STEP_1:
            choosePattern = null;
            lockPatternView.clearPattern();
            lockPatternView.enableInput();
            break;
        case STEP_2:
        	getHandlerContext().makeTextShort(getString(R.string.agegestureset));
        	step = STEP_3;
            updateView();
            break;
        case STEP_3:
            lockPatternView.clearPattern();
            lockPatternView.enableInput();
            break;
        case STEP_4:
            if (confirm) {
            	getHandlerContext().makeTextShort(getString(R.string.gesturesetsuccess));
                lockPatternView.disableInput();
                BaseContext.getSharedPreferences().putString(Constant.Preferences.SP_LOCK_KEY_DATA, LockPatternView.patternToString(choosePattern));
                finish();
            } else {
            	getHandlerContext().makeTextShort(getString(R.string.twogesturesetdiff));
                lockPatternView.setDisplayMode(DisplayMode.Wrong);
                lockPatternView.disableInput();
                
                new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						step = STEP_1;
		                updateView();
					}
                	
                },1500);
                
            }
            break;
        }
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
        if (pattern.size() < LockPatternView.MIN_LOCK_PATTERN_SIZE) {
        	getHandlerContext().makeTextShort(getString(R.string.lockpattern_recording_incorrect_too_short));
            lockPatternView.setDisplayMode(DisplayMode.Wrong);
            
            new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					step = STEP_1;
	                updateView();
				}
            	
            },1500);
            
            return;
        }
        if (choosePattern == null) {
            choosePattern = new ArrayList<Cell>(pattern);
            step = STEP_2;
            updateView();
            return;
        }
        confirm = choosePattern.equals(pattern);
        step = STEP_4;
        updateView();
    }

}