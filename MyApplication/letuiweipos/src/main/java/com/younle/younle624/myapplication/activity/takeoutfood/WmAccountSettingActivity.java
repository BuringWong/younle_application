package com.younle.younle624.myapplication.activity.takeoutfood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.waimai.SalerInfo;
import com.younle.younle624.myapplication.utils.AlertUtils;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.NetWorks;
import com.younle.younle624.myapplication.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class WmAccountSettingActivity extends Activity implements View.OnClickListener, AlertUtils.OnClickCallBack {
    private RelativeLayout change_mt;
    private RelativeLayout change_elm;
    private RelativeLayout change_bd;
    private RelativeLayout bind_mt;
    private RelativeLayout bind_elm;
    private RelativeLayout bind_bd;
    private LinearLayout ll_cancel;
    private TextView tv_title;
    private boolean openMt;
    private PopupWindow popupWindow;
    private NetWorks netWorks;
    private NetWorks.OnNetCallBack callBack=new NetWorks.OnNetCallBack() {
        @Override
        public void onError(Exception e, int flag) {
            AlertUtils.getInstance().upDateUI(false,null,true);
            LogUtils.Log("饿了么销售error:" + e.toString());
        }

        @Override
        public void onResonse(String response, int flag) {
            parseJson(response);
            LogUtils.Log("饿了么销售信息:" + response.toString());
        }
    };
    private boolean isbind=true;
    private TextView mt_store_name;
    private TextView elm_store_name;
    private TextView bd_store_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wm_account_setting);
        Utils.initToolBarColor(this, "#3f88ce");
        netWorks=new NetWorks(this);
        initView();
        setListener();
    }

    @Override
    protected void onRestart() {
        showWhat();
        super.onRestart();
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
                SalerInfo salerInfo=new SalerInfo(username,com_qq,telephone);
                AlertUtils.getInstance().upDateUI(true,salerInfo,isbind);
            }else {
                AlertUtils.getInstance().upDateUI(false,null,true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.Log("解析错误：" + e.toString());
        }

    }

    private void initView() {
        change_mt = (RelativeLayout)findViewById(R.id.change_mt);
        change_elm = (RelativeLayout)findViewById(R.id.change_elm);
        change_bd = (RelativeLayout)findViewById(R.id.change_bd);
        bind_mt = (RelativeLayout)findViewById(R.id.bind_mt);
        bind_elm = (RelativeLayout)findViewById(R.id.bind_elm);
        bind_bd = (RelativeLayout)findViewById(R.id.bind_bd);
        ll_cancel = (LinearLayout)findViewById(R.id.ll_cancel);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("外卖多平台接单");

        //已绑定的门店名称
        mt_store_name = (TextView)findViewById(R.id.mt_store_name);
        elm_store_name = (TextView)findViewById(R.id.elm_store_name);
        bd_store_name = (TextView)findViewById(R.id.bd_store_name);
        showWhat();
    }

    private void showWhat() {
        //是否开通美团
        if(Constant.WM_STATUS.contains(1)) {
            showAndHide(true, change_mt, bind_mt);
            mt_store_name.setText(Constant.mt_store_name);
        }else {
            showAndHide(false, change_mt, bind_mt);
        }
        //是否开通饿了么
        if(Constant.WM_STATUS.contains(2)) {
            showAndHide(true, change_elm, bind_elm);
            elm_store_name.setText(Constant.elm_store_name);
        }else {
            showAndHide(false, change_elm, bind_elm);
        }
        //是否开通百度
        if(Constant.WM_STATUS.contains(3)) {
            showAndHide(true, change_bd, bind_bd);
            bd_store_name.setText(Constant.bd_store_name);
        }else {
            showAndHide(false, change_bd, bind_bd);
        }
    }

    private void setListener() {
        change_mt.setOnClickListener(this);
        change_elm.setOnClickListener(this);
        change_bd.setOnClickListener(this);
        bind_mt.setOnClickListener(this);
        bind_elm.setOnClickListener(this);
        bind_bd.setOnClickListener(this);
        ll_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            //1.更换账号，
            case R.id.change_mt :
                intent=new Intent(this,BindMtActivity.class);
                intent.putExtra("action", 1);
                startActivity(intent);
                break;
            case R.id.change_bd :
                intent=new Intent(this,BindBDActivity.class);
                startActivity(intent);
                break;
            case R.id.change_elm :
                intent=new Intent(this,BindMtActivity.class);
                intent.putExtra("action",2);
                startActivity(intent);
               /* popupWindow = AlertUtils.getInstance().showBindPup(1, R.string.bind_elm_notice, R.id.bind_wm_setting, this, this);
                //1.显示pupwindow
                //2.发起联网请求
                netWorks.getSalerInfo(callBack,0);
                netWorks.getEpoiBindAccount(callBack,2);*/
                break;
            //2.绑定账号
            case R.id.bind_mt :
                intent=new Intent(this,BindMtActivity.class);
                intent.putExtra("action",0);
                startActivity(intent);
                break;
            case R.id.bind_elm :
                intent=new Intent(this,BindMtActivity.class);
                intent.putExtra("action",2);
                startActivity(intent);
               /* popupWindow = AlertUtils.getInstance().showBindPup(1, R.string.bind_elm_notice, R.id.bind_wm_setting, this, this);

                //1.显示pupwindow
                //2.发起联网请求
                netWorks.getSalerInfo(callBack,0);
                netWorks.getEpoiBindAccount(callBack,2);*/
                break;
            case R.id.bind_bd :
                intent=new Intent(this,BindBDActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_cancel:
                finish();
                break;
        }
    }


    /**
     * pupWindow的点击
     */
    @Override
    public void onClick() {
        if(popupWindow!=null&&popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }
    /**
     * 根据账号状态显示不同控件
     */
    private void showAndHide(boolean open,View changeView,View bindView) {
        if(open) {
            changeView.setVisibility(View.VISIBLE);
            bindView.setVisibility(View.GONE);
        }else {
            changeView.setVisibility(View.GONE);
            bindView.setVisibility(View.VISIBLE);
        }
    }

}
