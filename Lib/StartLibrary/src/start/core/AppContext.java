package start.core;

import start.utils.SharedPreferencesUtils;
import android.app.Application;
import android.content.Context;

/**
 * 
 * @author Start   
 * @Description: 全局application 
 * @ClassName: AppContext.java   
 * @date 2014年6月30日 上午9:08:15      
 * @说明  代码版权归 杭州反盗版中心有限公司 所有
 */
public abstract class AppContext extends Application {
    
    private static AppContext mAppContext;
    private static SharedPreferencesUtils sharedPreferences; 
	
    @Override
    public void onCreate() {
        super.onCreate();
        if(!isTestEnvironmental()){
        	//注册App异常崩溃处理器
         	Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());
        }
        mAppContext=this;
    	sharedPreferences=new SharedPreferencesUtils(this);
    }
    
    public static Context getContext(){
        return getInstance().getApplicationContext();
    }
    
    public static AppContext getInstance(){
        return mAppContext;
    }
    
    public static SharedPreferencesUtils getSharedPreferences() {
		return sharedPreferences;
	}
    
    /**
     * 当前环境是否为测试环境true是,false否
     */
    public abstract Boolean isTestEnvironmental();
    
    /**
     * 服务端的URL
     */
    public abstract String getServerURL();
    
}