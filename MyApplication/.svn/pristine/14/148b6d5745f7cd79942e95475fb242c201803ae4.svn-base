package com.younle.younle624.myapplication.activity.takeoutfood;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;

public class OutFoodManageActivity extends Activity {

    private TextView tv_title;
    private TextView tv_cancel;
    private LinearLayout ll_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_food_manage);
        initView();
    }

    private void initView() {
        ll_cancel = (LinearLayout) findViewById(R.id.ll_cancel);
        ll_cancel.setVisibility(View.VISIBLE);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("外卖管理");
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_cancel.setText("管理");
    }

}
