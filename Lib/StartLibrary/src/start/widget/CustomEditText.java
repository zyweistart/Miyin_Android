package start.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * 
 * @author xuh
 * @Description: 自定义文件输入框，自带文件清除按钮
 * @ClassName: CustomEditText.java
 * @date 2013年10月24日 下午2:14:49
 * @说明 代码版权归 杭州反盗版中心有限公司 所有
 */
public class CustomEditText extends EditText {

	/**
	 * 右侧图片控件
	 */
	private Drawable rightDrawable;

	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CustomEditText(Context context) {
		super(context);
		init();
	}

	public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		setDrawable();
		// 增加文本监听器
		addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				setDrawable();
			}
		});
	}

	/**
	 * @Author: xuh
	 * @Description: 输入框右边的删除图标的显示控制
	 * @Date:2013年10月24日下午2:28:49
	 * @return void
	 * @说明 代码版权归 杭州反盗版中心有限公司 所有
	 */
	private void setDrawable() {
		if (length() == 0) {
			setCompoundDrawables(null, null, null, null);
		} else {
			setCompoundDrawables(null, null, rightDrawable, null);
		}
	}

	@Override
	public void setCompoundDrawables(Drawable left, Drawable top,
			Drawable right, Drawable bottom) {
		if (rightDrawable == null) {
			rightDrawable = right;
		}
		super.setCompoundDrawables(left, top, right, bottom);
	}

	/**
	 * 输入事件处理
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {		
		if(this.isEnabled()){
			if (rightDrawable != null && event.getAction() == MotionEvent.ACTION_UP) {
				int eventX = (int) event.getX();
				int width = getWidth();
				int right1 = getTotalPaddingRight();
				int right2 = getPaddingRight();
				if (eventX > (width - right1) && eventX < width - right2) {
					setText("");
					event.setAction(MotionEvent.ACTION_CANCEL);
				}
			}
		}		
		return super.onTouchEvent(event);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		this.rightDrawable = null;
	}
}
