package com.start.medical;

import start.core.AppContext;
import android.database.sqlite.SQLiteDatabase;

import com.start.core.Constant;
import com.start.core.DBManageDao;
import com.start.service.User;

/**
 * @author Start   
 * @Description: 全局application 
 * @ClassName: AppContext.java   
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
		return Constant.ISSYSTEMTEST;
	}

	@Override
	public String getServerURL() {
		return Constant.RESTURL;
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
