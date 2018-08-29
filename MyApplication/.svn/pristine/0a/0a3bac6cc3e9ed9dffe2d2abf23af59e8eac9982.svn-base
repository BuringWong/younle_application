package com.younle.younle624.myapplication.activity.manager.orderpager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.utils.Utils;

public class AboutH5Activity extends Activity implements View.OnClickListener {

    private TextView tv_title;
    private ImageView iv_title;
    private ImageView iv_show_icon;
    private int fromwhere = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_h5);
        Utils.initToolBarState(this);
        initData();
        initView();
        showData();
    }

    private void initData() {
        fromwhere = getIntent().getIntExtra("fromwhere", 0);
    }

    private void initView() {
        tv_title = (TextView)findViewById(R.id.tv_title);
        iv_title = (ImageView)findViewById(R.id.iv_title);
        iv_show_icon = (ImageView)findViewById(R.id.iv_show_icon);
    }

    private void showData() {
        switch (fromwhere){
            case 0:
                tv_title.setText("关于H5电商系统");
                iv_title.setVisibility(View.VISIBLE);
                iv_show_icon.setBackgroundResource(R.drawable.abouth5);
                iv_title.setOnClickListener(this);
                break;
            case 1:
                tv_title.setText("关于微信卡券");
                iv_title.setVisibility(View.VISIBLE);
                iv_show_icon.setBackgroundResource(R.drawable.card_voucher_guide);
                iv_title.setOnClickListener(this);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
