package com.start.zmcy.content;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.start.core.BaseFragment;
import com.start.zmcy.R;

public class NewsContentFragment  extends BaseFragment {

    private TextView tv = null;

    public NewsContentFragment(String title){
    	super();
    	setTitle(title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        tv = (TextView)v.findViewById(R.id.txtname);
        tv.setText(getTitle());
        return v;
    }
    
}