package com.younle.younle624.myapplication.activity.takeoutfood;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.NetWorks;
import com.younle.younle624.myapplication.view.SelfLinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 绑定百度外卖的activity
 */
public class BindBDActivity extends Activity implements View.OnClickListener {
    private TextView tv_title;
    private LinearLayout ll_cancel;
    private TextView tv_cancel;
    private SelfLinearLayout ll_loading;
    private ImageView iv_jiazai_filure;
    private ProgressBar pb_loading;
    private TextView tv_loading;
    private TextView tv_saler_info;
    private NetWorks.OnNetCallBack callBack=new NetWorks.OnNetCallBack() {
        @Override
        public void onError(Exception e, int flag) {
            LogUtils.Log("饿了么销售error:" + e.toString());
            upDateUI(false);
        }

        @Override
        public void onResonse(String response, int flag) {
            parseJson(response);
            LogUtils.Log("饿了么销售信息:" + response.toString());
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_bd);
//        Utils.initToolBarColor(this, "#3f88ce");
        NetWorks netWorks=new NetWorks(this);
        netWorks.getSalerInfo(callBack,0);
        initView();
        setListener();
    }

    private void setListener() {
        ll_cancel.setOnClickListener(this);
    }

    /**
     * 解析数据
     * @param json
     */
    private void parseJson(String json) {
        try {
            JSONObject jsonObject=new JSONObject(json);
            int code = jsonObject.getInt("code");
            if(code==200) {
                JSONObject msg = jsonObject.getJSONObject("msg");
                String com_qq = msg.getString("com_qq");
                String telephone = msg.getString("telephone");
                String username = msg.getString("username");
                upDateUI(true);
                tv_saler_info.setText("您的客户经理：" + username + '\n' + "电话：" + telephone + '\n' + "QQ：" + com_qq);
            }else {
                upDateUI(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.Log("解析错误：" + e.toString());
        }

    }

    private void upDateUI(boolean success) {
        if(success) {
            ll_loading.setVisibility(View.GONE);
        }else {
            pb_loading.setVisibility(View.GONE);
            iv_jiazai_filure.setVisibility(View.VISIBLE);
            tv_loading.setText("信息加载失败，请稍后重试！");
        }
    }

    private void initView() {
        ll_cancel = (LinearLayout)findViewById(R.id.ll_cancel);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("百度外卖");
        tv_cancel = (TextView)findViewById(R.id.tv_cancel);
        tv_cancel.setText("外卖管理");
        //加载中
        ll_loading = (SelfLinearLayout)findViewById(R.id.ll_loading);
        iv_jiazai_filure = (ImageView)findViewById(R.id.iv_jiazai_filure);
        pb_loading = (ProgressBar)findViewById(R.id.pb_loading);
        tv_loading = (TextView)findViewById(R.id.tv_loading);
        tv_loading.setText("信息加载中,请耐心等待！");
        tv_saler_info = (TextView)findViewById(R.id.tv_saler_info);
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
