package com.ancun.service;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

public class ButtonTextWatcher implements TextWatcher {

	private Button mButton;
	
	public ButtonTextWatcher(Button button){
		this.mButton=button;
	}
	
	
	public void beforeTextChanged(CharSequence s, int start, int count,int after) {
		
	}

	public void afterTextChanged(Editable s) {
		
	}

	public void onTextChanged(CharSequence s, int start, int before,int count) {
		if (start + count > 0) {
			this.mButton.setTextColor(Color.BLACK);
			this.mButton.setEnabled(true);
		} else {
			this.mButton.setTextColor(Color.GRAY);
			this.mButton.setEnabled(false);
		}
	}
	
}