package start.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtils {

	private static Context context;

	private static String dbName = "Ancun";

	private static SharedPreferencesUtils mSharedPreferencesUtils;
	
	public static SharedPreferencesUtils getInstance(Context context){
		if(mSharedPreferencesUtils==null){
			mSharedPreferencesUtils=new SharedPreferencesUtils(context);
		}
		return mSharedPreferencesUtils;
	}
	
	public SharedPreferencesUtils(Context context) {
		SharedPreferencesUtils.context = context;
	}

	public SharedPreferencesUtils(Context context, String dbName) {
		SharedPreferencesUtils.context = context;
		SharedPreferencesUtils.dbName = dbName;
	}

	public void putInteger(String key, Integer value) {
		SharedPreferences preferences = context.getSharedPreferences(dbName,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public int getInteger(String key, Integer defValue) {
		SharedPreferences preferences = context.getSharedPreferences(dbName,
				Context.MODE_PRIVATE);
		return preferences.getInt(key, defValue);
	}

	public void putString(String key, String value) {
		SharedPreferences preferences = context.getSharedPreferences(dbName,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public String getString(String key, String defValue) {
		SharedPreferences preferences = context.getSharedPreferences(dbName,
				Context.MODE_PRIVATE);
		return preferences.getString(key, defValue);
	}

	public void putBoolean(String key, boolean value) {
		SharedPreferences preferences = context.getSharedPreferences(dbName,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public boolean getBoolean(String key, boolean defValue) {
		SharedPreferences preferences = context.getSharedPreferences(dbName,
				Context.MODE_PRIVATE);
		return preferences.getBoolean(key, defValue);
	}
	
}
