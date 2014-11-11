package com.ancun.yzb;

import start.core.AppContext;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.ancun.core.DBManageDao;
import com.ancun.service.User;

/**
 * @author Start   
 * @Description: 全局application 
 * @ClassName: BaseContext.java   
 * @date 2014年6月30日 上午9:08:15      
 * @说明  代码版权归 杭州反盗版中心有限公司 所有
 */
public class BaseContext extends AppContext {

	private User mUser;
    private static DBManageDao dbManager;
    private static  SQLiteDatabase mSQLiteDatabase;
	
    @Override
    public void onCreate() {
        super.onCreate();
        getDBManager();
    }
    
	@Override
	public Boolean isTestEnvironmental() {
		return true;
	}

	@Override
	public String getServerURL() {
		return isTestEnvironmental() ?
				"http://192.168.0.223:2230/http/HttpService":
					"http://192.168.0.221:8888/accore/http/HttpService";
	}
	
	@Override
	public String getStorageDirectory(String name){
		return Environment.getExternalStorageDirectory().getPath()+"/ancun/"+name+"/";
	 }

	/**
     * 获取数据库管理对象
     */
    public static DBManageDao getDBManager(){
		if(dbManager == null){
			dbManager = new DBManageDao(getContext());
		}
		return dbManager;
	}
    
    /**
     * 获取数据库操作类 
     */
    public static SQLiteDatabase getSQLiteDatabase() {
    	if(null == mSQLiteDatabase){
    		mSQLiteDatabase = getDBManager().getSQLiteDatabase();
    	}
    	return mSQLiteDatabase;
	}
	
	/**
     * 获取当前用户信息
     */
    public User currentUser(){
    	if(mUser==null){
    		mUser=User.getInstance();
    	}
    	return mUser;
    }
	
}
