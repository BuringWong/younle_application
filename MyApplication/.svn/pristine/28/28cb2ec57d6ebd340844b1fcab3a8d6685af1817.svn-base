package com.younle.younle624.myapplication.activity.takeoutfood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.waimai.SalerInfo;
import com.younle.younle624.myapplication.utils.AlertUtils;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.NetWorks;
import com.younle.younle624.myapplication.utils.SpUtils;
import com.younle.younle624.myapplication.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class BandNumActivity extends Activity implements View.OnClickListener,AlertUtils.OnClickCallBack {

    private TextView tv_band_mt_account;
    private TextView tv_band_eleme_account;
    private TextView tv_band_baidu_account;
    private TextView tv_title;
    private TextView tv_cancel;
    private LinearLayout ll_cancel;
    private Button btn_start_to_use;
    private TextView tv_notices;
    private PopupWindow popupWindow;
    private NetWorks netWorks;
    private RelativeLayout change_mt;
    private RelativeLayout change_elm;
    private RelativeLayout change_bd;
    private TextView mt_store_name;
    private TextView elm_store_name;
    private TextView bd_store_name;
    private NetWorks.OnNetCallBack callBack=new NetWorks.OnNetCallBack() {
        @Override
        public void onError(Exception e, int flag) {
            if(flag==0) {
                AlertUtils.getInstance().upDateUI(false,null,true);
                LogUtils.Log("饿了么销售error:" + e.toString());
            }else if(flag==1) {
                changeButtonStatus();
                Toast.makeText(BandNumActivity.this, "网络异常，请检查网络后重试！", Toast.LENGTH_SHORT).show();
            }else if(flag==2) {
                LogUtils.Log("点击饿了么绑定jpushid失败");
            }
        }

        @Override
        public void onResonse(String response, int flag) {
            if(flag==0) {
                parseJson(response);
                LogUtils.Log("饿了么销售信息:" + response.toString());
            }else if(flag==1) {
                changeButtonStatus();
                praseOpenJieDanJson(response);
            }else if(flag==2) {
                LogUtils.Log("点饿了么绑定jpushid的response=="+response.toString());
            }
        }
    };
//    private boolean is_dead;

    private void changeButtonStatus() {
        btn_start_to_use.setText("开始使用");
        btn_start_to_use.setEnabled(true);
    }

    private void praseOpenJieDanJson(String json) {
        try {
            JSONObject jsonObject=new JSONObject(json);
            int code = jsonObject.getInt("code");
            if(code==200) {
                SpUtils.getInstance(this).save(Constant.IS_JIEDAN_OPEN, true);
                Intent intent=new Intent(this,WaiMaiActivity.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(BandNumActivity.this, "网络异常，请检查网络后重试！", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                AlertUtils.getInstance().upDateUI(true,salerInfo,true);
            }else {
                AlertUtils.getInstance().upDateUI(false,null,true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.Log("解析错误：" + e.toString());
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_num);
        Utils.initToolBarColor(this, "#3f88ce");
//        is_dead = SpUtils.getInstance(this).getBoolean(Constant.WM_DEAD, true);
        netWorks = new NetWorks(this);
        initView();
        setListener();
    }
    @Override
    protected void onResume() {
        initView();
        super.onResume();
    }
    private void initView() {
        ll_cancel = (LinearLayout) findViewById(R.id.ll_cancel);
        ll_cancel.setVisibility(View.VISIBLE);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("外卖多平台接单");
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_cancel.setText("管理");
        tv_band_mt_account = (TextView) findViewById(R.id.tv_band_mt_account);
        tv_band_eleme_account = (TextView) findViewById(R.id.tv_band_eleme_account);
        tv_band_baidu_account = (TextView) findViewById(R.id.tv_band_baidu_account);
        btn_start_to_use = (Button) findViewById(R.id.btn_start_to_use);
        tv_notices = (TextView)findViewById(R.id.tv_notices);
        change_mt = (RelativeLayout)findViewById(R.id.change_mt);
        change_elm = (RelativeLayout)findViewById(R.id.change_elm);
        change_bd = (RelativeLayout)findViewById(R.id.change_bd);
        //已绑定的门店名称
        mt_store_name = (TextView)findViewById(R.id.mt_store_name);
        elm_store_name = (TextView)findViewById(R.id.elm_store_name);
        bd_store_name = (TextView)findViewById(R.id.bd_store_name);


        shouldShow();
    }
    private void shouldShow() {
//        if(Constant.OPEN_WM) {//已经开通
            tv_notices.setVisibility(View.GONE);
            btn_start_to_use.setText("开始使用");
            if(Constant.WM_STATUS.contains(1)) {
                change_mt.setVisibility(View.VISIBLE);
                tv_band_mt_account.setVisibility(View.GONE);
                mt_store_name.setText(Constant.mt_store_name);
            }
            if(Constant.WM_STATUS.contains(2)) {
                change_elm.setVisibility(View.VISIBLE);
                tv_band_eleme_account.setVisibility(View.GONE);
                elm_store_name.setText(Constant.elm_store_name);
            }
            if(Constant.WM_STATUS.contains(3)) {
                change_bd.setVisibility(View.VISIBLE);
                tv_band_baidu_account.setVisibility(View.GONE);
                elm_store_name.setText(Constant.bd_store_name);
            }
        /*}else {//未开通
            tv_notices.setVisibility(View.VISIBLE);
            tv_band_baidu_account.setBackgroundResource(R.drawable.unopen_takeoutfood_total__background);
            tv_band_eleme_account.setBackgroundResource(R.drawable.unopen_takeoutfood_total__background);
            tv_band_mt_account.setBackgroundResource(R.drawable.unopen_takeoutfood_total__background);
            tv_band_baidu_account.setTextColor(Color.rgb(196,196,196));
            tv_band_eleme_account.setTextColor(Color.rgb(196,196,196));
            tv_band_mt_account.setTextColor(Color.rgb(196,196,196));
            tv_band_baidu_account.setEnabled(false);
            tv_band_eleme_account.setEnabled(false);
            tv_band_mt_account.setEnabled(false);
            btn_start_to_use.setText("开通外卖多平台接单");
        }*/
    }

    private void setListener() {
        tv_band_mt_account.setOnClickListener(this);
        tv_band_eleme_account.setOnClickListener(this);
        tv_band_baidu_account.setOnClickListener(this);
        btn_start_to_use.setOnClickListener(this);
        ll_cancel.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.ll_cancel:
                finish();
                break;
            case R.id.tv_band_mt_account:
                LogUtils.Log("constant.epoiid==" + Constant.EPOIID);
                intent=new Intent(this,BindMtActivity.class);
                intent.putExtra("action",0);
                startActivity(intent);
//                Constant.bind_wm_fromwhere=0;
                break;
            case R.id.tv_band_eleme_account:
                intent=new Intent(this,BindMtActivity.class);
                intent.putExtra("action",2);
                startActivity(intent);

              /*  popupWindow = AlertUtils.getInstance().showBindPup(1, R.string.bind_elm_notice, R.id.bind_wm, this, this);

                //1.显示pupwindow
                //2.发起联网请求
                netWorks.getSalerInfo(callBack,0);
                netWorks.getEpoiBindAccount(callBack,2);*/
                break;
            case R.id.tv_band_baidu_account:
                intent=new Intent(this,BindBDActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_start_to_use://开始使用
                startToUse();
                break;
            case R.id.tv_sure:
                if(this.popupWindow !=null&& this.popupWindow.isShowing()) {
                    this.popupWindow.dismiss();
                }
                break;
        }
    }

    /**
     * 1.引导充值
     * 2.提示绑定平台
     * 3.开始使用
     */
    private void startToUse() {
        Intent intent;
//        if(Constant.OPEN_WM) {//已经开通外卖版块,并且没有过期
            if(Constant.WM_STATUS!=null&& Constant.WM_STATUS.size()>0) {//已经绑定至少一个外卖商家
                btn_start_to_use.setText("自动接单开启中...");
                btn_start_to_use.setEnabled(false);
                netWorks.switchWmStatus(callBack, 1, 1);
            }else {
                popupWindow= AlertUtils.getInstance().showBindPup(0, R.string.bind_none, R.id.bind_wm,this,this);
            }
        /*}else {//引导充值
            intent=new Intent(this, OpenOrderActivity.class);
            intent.putExtra("which_module",1);
            startActivity(intent);
        }*/
    }

    /**
     * 绑定完成重新回到界面
     * 根据绑定回推，更新ui
     */
    @Override
    protected void onRestart() {
        if(Constant.OPEN_WM) {
            if(Constant.WM_STATUS.contains(1)) {
                change_mt.setVisibility(View.VISIBLE);
                tv_band_mt_account.setVisibility(View.GONE);
            }
            if(Constant.WM_STATUS.contains(2)) {
                change_elm.setVisibility(View.VISIBLE);
                tv_band_eleme_account.setVisibility(View.GONE);
            }
            if(Constant.WM_STATUS.contains(3)) {
                change_bd.setVisibility(View.VISIBLE);
                tv_band_baidu_account.setVisibility(View.GONE);
            }
        }

        super.onRestart();
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
}
