package com.younle.younle624.myapplication.activity.manager.orderpager.pos;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.constant.Constant;

public class HowToUseZfbWxActivity extends Activity {

    private ImageView iv_back;
    private ImageView iv_how_to_use_zfb_wx;
    private int fromeWhere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  //全屏
        setContentView(R.layout.activity_how_to_use_zfb_wx);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_how_to_use_zfb_wx = (ImageView) findViewById(R.id.iv_how_to_use_zfb_wx);
        fromeWhere = getIntent().getIntExtra(Constant.FROME_WHERE, -1);
        String payway = getIntent().getStringExtra("payway");
        if(fromeWhere == Constant.MEMBER_DIRECT_PAY){//从直接收银页面到此
            if("1".equals(payway)){//支付宝
                iv_how_to_use_zfb_wx.setBackgroundResource(R.drawable.zfb_how_to_use);
            }else if("0".equals(payway)){
                iv_how_to_use_zfb_wx.setBackgroundResource(R.drawable.wx_how_to_use);
            }else{
                //怎么找到会员卡码图片待做
                iv_how_to_use_zfb_wx.setBackgroundResource(R.drawable.card_voucher_guide);
            }
        }else if(fromeWhere == Constant.MEMBER_CUSTOMER_CONFIRM||fromeWhere == Constant.MEMBER_CHARGE||fromeWhere == Constant.MEMBER_CARDS_VOUCHER){
            iv_how_to_use_zfb_wx.setBackgroundResource(R.drawable.card_voucher_guide);
        }else{
            if("1".equals(payway)){//支付宝
                iv_how_to_use_zfb_wx.setBackgroundResource(R.drawable.zfb_how_to_use);
            }else if("0".equals(payway)){
                iv_how_to_use_zfb_wx.setBackgroundResource(R.drawable.wx_how_to_use);
            }
        }
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
