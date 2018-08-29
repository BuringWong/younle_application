package com.younle.younle624.myapplication.activity.orderguide;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.domain.membercharge.ChargeItem;

public class UpgradeAccountFailActivity extends Activity implements View.OnClickListener {

    private TextView tv_title;
    private LinearLayout ll_cancel;
    private RelativeLayout title_all;
    private Button btn_pay_again;
    private ChargeItem whichModularOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_account_fail);
        initView();
        setListener();
        getIntentData();
    }

    private void getIntentData() {
        ChargeItem pay_item = (ChargeItem) getIntent().getSerializableExtra("pay_item");
        if(pay_item!=null){
            whichModularOpen = pay_item;
        }
    }

    private void initView() {
        title_all = (RelativeLayout) findViewById(R.id.title_all);
        title_all.setBackgroundColor(Color.parseColor("#6e6e6e"));
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("支付失败");
        ll_cancel = (LinearLayout) findViewById(R.id.ll_cancel);
        btn_pay_again = (Button) findViewById(R.id.btn_pay_again);
    }

    private void setListener() {
        ll_cancel.setOnClickListener(this);
        btn_pay_again.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_cancel:
                finish();
                break;
            case R.id.btn_pay_again://重新发起支付
                Intent intent = new Intent(this, UpgradeAccountActivity.class);
                intent.putExtra("pay_item", whichModularOpen);
                startActivity(intent);
                finish();
                break;
        }
    }
}
