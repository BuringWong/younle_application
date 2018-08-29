package com.younle.younle624.myapplication.activity.orderguide;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.utils.NetWorks;
import com.younle.younle624.myapplication.utils.SpUtils;
import com.younle.younle624.myapplication.utils.Utils;

import org.json.JSONObject;

public class OpenOrderActivity extends Activity implements View.OnClickListener {

    private LinearLayout ll_cancel;
    private TextView tv_title;
    private TextView tv_open_what;
    private int which_module;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.openzh_guide_pager);
        which_module = getIntent().getIntExtra("which_module",-1);
        if(which_module==1) {
            closeWm();
        }
        initView();
        setListener();
    }

    /**
     * 关闭外卖自动接单
     */
    private void closeWm() {
        NetWorks netWorks=new NetWorks(this);
        netWorks.switchWmStatus(new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                Utils.showToast(OpenOrderActivity.this,"网络异常,关闭自动接单失败,请联系乐推微",2000);
            }

            @Override
            public void onResonse(String response, int flag) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if(code==200) {
                        Boolean is_jiedan_open = SpUtils.getInstance(OpenOrderActivity.this).getBoolean(Constant.IS_JIEDAN_OPEN, false);
                        if(is_jiedan_open) {
                            SpUtils.getInstance(OpenOrderActivity.this).save(Constant.IS_JIEDAN_OPEN,false);
                        }
                    }else {
                        Utils.showToast(OpenOrderActivity.this,"网络异常,关闭自动接单失败,请联系乐推微",2000);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 0);
    }
    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        ll_cancel = (LinearLayout) findViewById(R.id.ll_cancel);
        tv_open_what = (TextView)findViewById(R.id.tv_open_what);
        if(which_module==0) {
            tv_title.setText("会员营销");
            tv_open_what.setText("开通“智慧门店”模块才可使用本功能");
        }else if(which_module==1) {
            tv_title.setText("外卖多平台接单");
            tv_open_what.setText("开通“外卖多平台接单”模块才可使用本功能");
        }
        findViewById(R.id.tv_showfunction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenOrderActivity.this.startActivity(new Intent(OpenOrderActivity.this,FunctionDetailActivit.class));
            }
        });
    }

    private void setListener() {
        ll_cancel.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_cancel:
                finish();
                break;
        }
    }
}
