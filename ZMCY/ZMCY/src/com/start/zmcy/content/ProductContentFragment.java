package com.start.zmcy.content;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.start.core.BaseFragment;
import com.start.zmcy.R;

public class ProductContentFragment  extends BaseFragment {

    private TextView tv = null;

    public ProductContentFragment(String title){
    	super();
    	setTitle(title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_products, container, false);
        tv = (TextView)v.findViewById(R.id.txtname);
        tv.setText(getTitle());
        return v;
    }
    
}
