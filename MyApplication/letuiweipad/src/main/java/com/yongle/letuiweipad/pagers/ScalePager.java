package com.yongle.letuiweipad.pagers;


import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.yongle.letuiweipad.basepager.BasePager;

/**
 * Created by 我是奋斗 on 2016/5/11.
 * 微信/e-mail:tt090423@126.com
 */
public class ScalePager extends BasePager implements View.OnClickListener {

    public ScalePager() {
        super();
    }

    @Override
    public View initView() {
        TextView textView=new TextView(mActivity);
        textView.setText("称重");
        textView.setTextSize(50);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }


    @Override
    public void onClick(View v) {
    }
}