package com.start.core;

import start.core.AppException;
import start.core.HandlerContext;
import start.core.HandlerContext.HandleContextListener;
import start.core.R;
import android.content.Intent;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;

import com.start.core.Constant.ResultCode;
import com.start.xinkuxue.BaseContext;

/**
 * 主体内容区核心父类
 * @author Start
 */
public abstract class BaseScrollContent implements HandleContextListener {
	
	protected final String TAG = this.getClass().getSimpleName();
	
	private View mLayoutView;
	private BaseActivity mActivity;
	private LayoutInflater mLayoutInflater;
	private HandlerContext mHandlerContext;
	
	public BaseScrollContent(BaseActivity activity, int resourceID) {
		this.mActivity = activity;
		mLayoutInflater = LayoutInflater.from(getCurrentActivity());
		mLayoutView = mLayoutInflater.inflate(resourceID, null);
	}
	
	public View getLayoutView() {
		return mLayoutView;
	}
	
	public LayoutInflater getLayoutInflater() {
		return mLayoutInflater;
	}

	public BaseActivity getCurrentActivity(){
		return mActivity;
	}
	
	public BaseContext getAppContext(){
		return (BaseContext)getCurrentActivity().getAppContext();
	}
	
	public View findViewById(int id) {
		return getLayoutView().findViewById(id);
	}

	public void startActivity(Intent intent) {
		getCurrentActivity().startActivity(intent);
	}
	
	public HandlerContext getHandlerContext() {
		if(mHandlerContext==null){
			mHandlerContext=new HandlerContext(getCurrentActivity());
			mHandlerContext.setListener(this);
		}
		return mHandlerContext;
	}

	@Override
	public void onProcessMessage(Message msg) throws AppException {
		switch(msg.what){
		case ResultCode.NOLOGIN:
			getCurrentActivity().goLogin(null,String.valueOf(msg.obj));
			break;
		default:
			Object message=msg.obj;
			if(message!=null){
				getHandlerContext().makeTextShort(String.valueOf(message));
			}else{
				getHandlerContext().makeTextShort(getCurrentActivity().getString(R.string.error_try_again));
			}
			break;
		}
	}
	
}