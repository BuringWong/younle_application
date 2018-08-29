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
public class CancelFail extends Activity implements View.OnClickListener {

    private Button BtCancelAgain;
    private TextView TvTile;
    private TextView tv_error_msg;
    private LinearLayout ll_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vouchers_verification_fail);
        Utils.initToolBarState(this);

        String error_msg = getIntent().getStringExtra("error_msg");
        ll_cancel = (LinearLayout)findViewById(R.id.ll_cancel);
        ll_cancel.setVisibility(View.VISIBLE);
        ll_cancel.setOnClickListener(this);
        tv_error_msg = (TextView)findViewById(R.id.tv_error_msg);
        tv_error_msg.setText(error_msg);

        TvTile = (TextView) findViewById(R.id.tv_title);
        TvTile.setText("核销结果");
        BtCancelAgain = (Button) findViewById(R.id.bt_cancel_again);
        BtCancelAgain.setOnClickListener(new View.OnClickListener() {
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
