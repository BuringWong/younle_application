package com.younle.younle624.myapplication.activity.voucher.cancel;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.utils.Utils;

/**
 * Created by Administrator on 2016/5/21.
 */
public class CancelSuccess extends Activity implements View.OnClickListener {

    private Button button;
    private TextView tvtile;
    private LinearLayout ll_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vouchers_verification_success);
        Utils.initToolBarState(this);
        ll_cancel = (LinearLayout)findViewById(R.id.ll_cancel);
        ll_cancel.setVisibility(View.VISIBLE);
        ll_cancel.setOnClickListener(this);
        tvtile = (TextView) findViewById(R.id.tv_title);
        tvtile.setText("核销结果");
        button = (Button) findViewById(R.id.verification_other);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_cancel :
                finish();
                break;
        }
    }
}