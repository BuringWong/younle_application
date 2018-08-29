package com.yongle.letuiweipad.pagers;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.yongle.letuiweipad.basepager.BasePager;
import com.yongle.letuiweipad.utils.LogUtils;


/**
 * Created by 我是奋斗 on 2016/5/9.
 * 微信/e-mail:tt090423@126.com
 */
public class FindOrderPager extends BasePager {

    private String TAG="storePiePager";

    public FindOrderPager() {
    }

    @Override
    public View initView() {
        LogUtils.Log("找单initView");
        TextView textView=new TextView(mActivity);
        textView.setText("找单");
        textView.setTextSize(50);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    public void initData(int index) {
        LogUtils.Log("初始找单页面的数据 index=="+index);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
