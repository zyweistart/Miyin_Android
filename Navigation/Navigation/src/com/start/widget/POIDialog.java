package com.start.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.start.navigation.R;

public class POIDialog extends AlertDialog implements View.OnClickListener {
	private View.OnClickListener listener;

	public POIDialog(Context context, int theme) {
		super(context, theme);
		if (context instanceof View.OnClickListener) {
			listener = (View.OnClickListener) context;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widget_dialog_poi);
		findViewById(R.id.direction).setOnClickListener(this);
		findViewById(R.id.poiName).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (listener != null) {
			listener.onClick(v);
		}
		dismiss();
	}
}
