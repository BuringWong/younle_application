package com.younle.younle624.myapplication.activity.orderguide;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.MainActivity;
import com.younle.younle624.myapplication.activity.takeoutfood.BandNumActivity;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.utils.SpUtils;

public class UpgradeAccountSuccessActivity extends Activity implements View.OnClickListener {

    private TextView tv_title;
    private TextView tv_account_info;
    private TextView tv_time_duration;
    private Button btn_use_now;
    private LinearLayout ll_cancel;
    private int accty;
    private String endtime;
    private String accname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_account_success);
        SpUtils.getInstance(this).save("dead_line",true);
        initView();
        setListener();
        setData();
        setShowView();
    }

    private void setData() {
        Constant.IS_JUST_NOW_OPEN_ORDER = true;
        SpUtils.getInstance(this).save(Constant.ALERT,"");
        int accty_extra = getIntent().getIntExtra("accty",-1);
        String endtime_extra = getIntent().getStringExtra("endtime");
        String accname_extra = getIntent().getStringExtra("accname");
        if (accty_extra != -1) {
            accty = accty_extra;
        }
        if (endtime_extra != null) {
            endtime = endtime_extra;
        }
        if (accname_extra != null) {
            accname = accname_extra;
        }
        switch (accty_extra) {
            case 1 :
                if(!Constant.OPENED_PERMISSIONS.contains("4")) {
                    Constant.OPENED_PERMISSIONS.add("4");
                }
                break;
            case 2 :
                Constant.OPEN_WM=true;
                if(!Constant.OPENED_PERMISSIONS.contains("7")) {
                    Constant.OPENED_PERMISSIONS.add("7");
                }
                break;
            case 3 :
                if(!Constant.OPENED_PERMISSIONS.contains("2")) {
                    Constant.OPENED_PERMISSIONS.add("2");
                }
                break;
        }

    }

    private void setShowView() {
        String modular_name = "";
        switch (accty) {
            case 1://智慧门店
                modular_name = "智慧门店";
                break;
            case 2://外卖
                modular_name = "外卖";
                break;
            case 3:
                modular_name = "下单";
                break;
        }
        tv_account_info.setText("账号" + accname + "已开通" + modular_name + "功能");
        tv_time_duration.setText("使用期限至：" + endtime);
    }

    private void initView() {
        tv_account_info = (TextView) findViewById(R.id.tv_account_info);
        tv_time_duration = (TextView) findViewById(R.id.tv_time_duration);
        btn_use_now = (Button) findViewById(R.id.btn_use_now);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("开通成功");
        ll_cancel = (LinearLayout) findViewById(R.id.ll_cancel);
    }

    private void setListener() {
        ll_cancel.setOnClickListener(this);
        btn_use_now.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_cancel:
            case R.id.btn_use_now:
                goToUse();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToUse();
    }

    private void goToUse() {
        Intent intent=null;
        switch (accty) {
            case 1://智慧门店
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("position", 1);//暂时打开会员营销页面
                break;
            case 2://外卖
                intent=new Intent(this, BandNumActivity.class);
                break;
            case 3:
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("position", 1);
                break;
        }
        startActivity(intent);
        finish();
    }
}
