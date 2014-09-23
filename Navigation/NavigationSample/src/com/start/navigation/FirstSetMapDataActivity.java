package com.start.navigation;

import java.io.File;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;

import com.start.core.Constant;
import com.start.core.CoreActivity;
import com.start.service.tasks.DecompressTask;
import com.start.utils.CommonFn;

/**
 * 首次设置地图数据页面
 * @author start
 *
 */
public class FirstSetMapDataActivity extends CoreActivity implements OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first_set_map_data);
		setCurrentActivityTitle("初始设置");
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.activity_first_set_map_data_btn_set){
			String fileno="56f346fd4895ec22818a37a8bc0d9ed7";
			//注意：必须设置一个唯一的全局编号代表当前地图的数据
			getAppContext().getSharedPreferencesUtils().putString(Constant.SharedPreferences.CURRENTDATAFILENO, fileno);
			//获取当前数据文件的路径
			String externalStorageDirectory=Environment.getExternalStorageDirectory().getPath();
			File tempDirFile=new File(new File(externalStorageDirectory+Constant.TMPDIRFILE),fileno);
			if(tempDirFile.exists()){
				//清除SQLite中的全部数据
				AppContext.getInstance().getMapDataService().clearAll(fileno);
				//导入数据navigation/tmp/目录下文件
				new DecompressTask(this,fileno).execute();
				
				CommonFn.alertDialog(this, "数据导入成功后再按确认键直接主界面", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						startActivity(new Intent(FirstSetMapDataActivity.this, MainActivity.class));
						finish();
					}
				}).show();
				
			}else{
				CommonFn.alertDialog(this, "数据文件不存在，请确认当前的数据文件路径为:"+tempDirFile.getAbsolutePath(), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
			}
		}
	}
	
}