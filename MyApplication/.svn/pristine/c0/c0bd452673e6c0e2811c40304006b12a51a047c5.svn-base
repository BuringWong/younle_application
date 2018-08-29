package com.younle.younle624.myapplication.activity.manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.manager.orderpager.entity.EntityEcActivity;
import com.younle.younle624.myapplication.activity.manager.orderpager.pos.PosOrderActivity;
import com.younle.younle624.myapplication.activity.manager.orderpager.service.ServiceEcActivity;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.utils.Utils;

public class OrderManagerActivity extends Activity implements View.OnClickListener {
    private TextView tv_title;
    private ImageView iv_title;
    private RelativeLayout rl_pos;
    private RelativeLayout rl_ec;
    private RelativeLayout rl_sv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_manager_layout);
        Utils.initToolBarState(this);

        initView();
        setListener();
    }

    private void initView() {
        rl_pos = (RelativeLayout) findViewById(R.id.rl_pos);
        rl_ec = (RelativeLayout) findViewById(R.id.rl_ec);
        rl_sv = (RelativeLayout) findViewById(R.id.rl_sv);
        //1.顶部初始化
        tv_title= (TextView) findViewById(R.id.tv_title);
        tv_title.setText("订单管理");
        iv_title = (ImageView)findViewById(R.id.iv_title);
        iv_title.setVisibility(View.VISIBLE);
    }
    private void setListener() {
        rl_pos.setOnClickListener(this);
        rl_ec.setOnClickListener(this);
        rl_sv.setOnClickListener(this);
        iv_title.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_pos ://pos机收款
                Constant.IS_FIRST_SELECT_TIME = false;
                startActivity(new Intent(this, PosOrderActivity.class));
                break;
            case R.id.rl_sv://服务类电商
                startActivity(new Intent(this, ServiceEcActivity.class));
                break;
            case R.id.rl_ec://实物类电商
                startActivity(new Intent(this, EntityEcActivity.class));
                break;
            case R.id.iv_title:
                finish();
                break;
        }
    }
}
