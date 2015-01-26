package start.core;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * @author Start   
 * @Description: Handle容器处理类
 * @ClassName: HandleContext.java   
 * @date 2014年6月30日 上午9:16:59      
 * @说明  代码版权归 杭州反盗版中心有限公司 所有
 */
public class HandlerContext {
	
	private Context mContext;
	private HandleContextListener mListener;
	
	public HandlerContext(Context context){
		this.mContext=context;
	}

	public void setListener(HandleContextListener mListener) {
		this.mListener = mListener;
	}

	public void makeTextShort(String text) {
		if(!TextUtils.isEmpty(text)){
			sendMessage(Toast.LENGTH_SHORT,text);
		}
	}

	public void makeTextLong(String text) {
		if(!TextUtils.isEmpty(text)){
			sendMessage(Toast.LENGTH_LONG,text);
		}
	}
	
	public void sendMessage(int what, Object obj) {
		Message msg = new Message();
		msg.what = what;
		msg.obj = obj;
		sendMessage(msg);
	}
	
	public void sendMessage(Message msg) {
		mHandler.sendMessage(msg);
	}

	public void sendEmptyMessage(int what) {
		mHandler.sendEmptyMessage(what);
	}
	
	private Handler mHandler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case Toast.LENGTH_SHORT:
				Toast.makeText(mContext, String.valueOf(msg.obj), Toast.LENGTH_SHORT).show();
				break;
			case Toast.LENGTH_LONG:
				Toast.makeText(mContext, String.valueOf(msg.obj), Toast.LENGTH_LONG).show();
				break;
			default:
				if(mListener!=null){
					try {
						mListener.onProcessMessage(msg);
					} catch (AppException e) {
						makeTextShort(e.getErrorString(mContext));
					}
				}
				break;
			}
		}
		
	};

	public Context getContext() {
		return mContext;
	}

	public Handler getHandler() {
		return mHandler;
	}

	public interface HandleContextListener{
		
		public void onProcessMessage(Message msg) throws AppException;
		
	}
	
}