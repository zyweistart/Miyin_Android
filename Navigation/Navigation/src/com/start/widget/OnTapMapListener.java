package com.start.widget;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class OnTapMapListener extends SimpleOnGestureListener {

	private OnTapMapClickListener mListener;

	public OnTapMapListener(OnTapMapClickListener listener) {
		this.mListener = listener;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		mListener.onClickAt(e.getX(), e.getY());
		return true;
	}
	
	public interface OnTapMapClickListener {
		void onClickAt(float x, float y);
	}

}
