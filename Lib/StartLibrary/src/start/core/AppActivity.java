package start.core;

import start.core.HandlerContext.HandleContextListener;
import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.View.OnClickListener;

import com.umeng.analytics.MobclickAgent;

public abstract class AppActivity extends Activity implements OnClickListener,HandleContextListener {
	
	protected final String TAG = this.getClass().getSimpleName();
	
	private HandlerContext mHandlerContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 添加Activity到堆栈
		AppManager.getInstance().addActivity(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (!AppContext.getInstance().isTestEnvironmental()) {
			MobclickAgent.onResume(this);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (!AppContext.getInstance().isTestEnvironmental()) {
			MobclickAgent.onPause(this);
		}
	}
	
	@Override
	protected void onDestroy() {
		// 结束Activity&从堆栈中移除
		AppManager.getInstance().finishActivity(this);
		super.onDestroy();
	}
	
	public HandlerContext getHandlerContext() {
		if(mHandlerContext==null){
			mHandlerContext=new HandlerContext(this);
			mHandlerContext.setListener(this);
		}
		return mHandlerContext;
	}
	
	@Override
	public void onProcessMessage(Message msg) throws AppException {
		switch(msg.what){
		default:
			Object message=msg.obj;
			if(message!=null){
				getHandlerContext().makeTextShort(String.valueOf(message));
			}else{
				getHandlerContext().makeTextShort(getString(R.string.error_try_again));
			}
			break;
		}
	}
	
}