package start.widget;

import android.content.Context;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;
/**
 * 浮动视图
 * @author Start
 */
public class DialFloatView extends LinearLayout {
    private float mTouchStartX;
    private float mTouchStartY;
    private float x;
    private float y;
    
    //此wmParams变量为获取的全局变量，用以保存悬浮窗口的属性
    private WindowManager.LayoutParams wmParams;

	public WindowManager.LayoutParams getMywmParams(){
		if(wmParams==null){
			wmParams=new WindowManager.LayoutParams();
		}
		return wmParams;
	}
    
    private WindowManager wm=(WindowManager)getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

	public DialFloatView(Context context) {
		super(context);		
	}
	
	 @Override
	 public boolean onTouchEvent(MotionEvent event) {
	     //获取相对屏幕的坐标，即以屏幕左上角为原点		 
	     x = event.getRawX();   
	     y = event.getRawY()-25;   //25是系统状态栏的高度
	     switch (event.getAction()) {
	        case MotionEvent.ACTION_DOWN:    //捕获手指触摸按下动作
	            //获取相对View的坐标，即以此View左上角为原点
	            mTouchStartX =  event.getX();  
                mTouchStartY =  event.getY();                
	            break;
	        case MotionEvent.ACTION_MOVE:	//捕获手指触摸移动动作            
	            updateViewPosition();
	            break;
	        case MotionEvent.ACTION_UP:    //捕获手指触摸离开动作
	            updateViewPosition();
	            mTouchStartX=mTouchStartY=0;
	            break;
	        }
	        return true;
		}
	 
	 private void updateViewPosition(){
	    //更新浮动窗口位置参数
		 getMywmParams().x=(int)( x-mTouchStartX);
		 getMywmParams().y=(int) (y-mTouchStartY);
	    //刷新显示 
	    wm.updateViewLayout(this, getMywmParams());
	 }

}
