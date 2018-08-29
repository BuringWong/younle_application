package com.younle.younle624.myapplication.activity.orderguide;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.domain.membercharge.ChargeItem;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.NetWorks;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.view.SelfLinearLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import zxing.encoding.EncodingHandler;

public class QRCodeActivity extends Activity implements View.OnClickListener, SelfLinearLayout.ClickToReload {

    private static final String TAG = "QRCodeActivity";
    private RelativeLayout rl_qrcode;
    private RelativeLayout rl_page_out_time;
    private TextView tv_click_reget;
    private TextView tv_get_pay_result;
    private LinearLayout ll_cancel;
    private TextView tv_title;
    private RelativeLayout title_all;
    private ImageView iv_qrcode;
    private TextView tv_please_scan_code;
    private TextView tv_price_year;
    private String payway;
    private String ccy;
    private String upgradeMoney;
    private ChargeItem chargeItem;
    private String linkstr = "";
    private String upgrade_price;
    private static final int GET_QR_CODE = 0;
    private static final int GET_UPGRADE_RESULT = 1;
    private static final int POLLING_PAY_RESULT = 2;
    private static final int CUT_DOWN_TIMMER = 3;
    private static final int MANUAL_QUERY_PAY_RESULT = 4;
    public ImageView iv_jiazai_filure;
    public SelfLinearLayout ll_loading;
    public TextView tv_loading;
    public ProgressBar pb_loading;
    private static final int TIME_LENGTH = 300;//测试用时间
    private int recLen = TIME_LENGTH;//测试用时间
    private boolean isTimeGone;
    private boolean isReturnPayRes;//限制多次点击
    private boolean isReturnCodeRes;//限制多次点击
    private NetWorks netWorks = new NetWorks(this);
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case POLLING_PAY_RESULT:
                    pollingPayResult();
                    break;
                case CUT_DOWN_TIMMER:
                    cutDownTimer();
                    break;
            }
        }
    };
    private NetWorks.OnNetCallBack OnNetCallBack = new NetWorks.OnNetCallBack() {
        @Override
        public void onError(Exception e, int flag) {
            switch (flag) {
                case GET_QR_CODE:
                    LogUtils.e(TAG, "获取支付码：onError() Exception e=" + e);
                    isReturnCodeRes = false;
                    if (!isTimeGone) {
                        netError();
                    } else {
                        pageOutTime();
                    }
                    break;
                case GET_UPGRADE_RESULT:
                    isReturnPayRes = false;
                    if (!isTimeGone) {
                        LogUtils.e(TAG, "轮询支付结果：onError() Exception e=" + e);
                        handler.sendEmptyMessage(POLLING_PAY_RESULT);
                    }
                    break;
                case MANUAL_QUERY_PAY_RESULT://手动查询支付结果
                    //进入付款失败页面
                    goToUpgrandFail();
                    break;
            }
        }

        @Override
        public void onResonse(String response, int flag) {
            switch (flag) {
                case GET_QR_CODE:
                    LogUtils.e(TAG, "获取支付码：response=" + response);
                    loadingDismiss();
                    isReturnCodeRes = false;
                    boolean b = Utils.checkSaveToken(QRCodeActivity.this, response);
                    if (b) {
                        parseJson(flag, response);
                    }
                    break;
                case GET_UPGRADE_RESULT:
                    LogUtils.e(TAG, "轮询支付结果：response=" + response);
                    isReturnPayRes = false;
                    boolean result = Utils.checkSaveToken(QRCodeActivity.this, response);
                    if (result) {
                        parseJson(flag, response);
                    }
                    break;
                case MANUAL_QUERY_PAY_RESULT://手动查询支付结果
                    boolean result_manual = Utils.checkSaveToken(QRCodeActivity.this, response);
                    if (result_manual) {
                        parseJson(flag, response);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        initView();
        getIntentData();
        initData(GET_QR_CODE);
    }

    private void initView() {
        ll_loading = (SelfLinearLayout) findViewById(R.id.ll_loading);
        iv_jiazai_filure = (ImageView) findViewById(R.id.iv_jiazai_filure);
        tv_loading = (TextView) findViewById(R.id.tv_loading);
        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);

        rl_qrcode = (RelativeLayout) findViewById(R.id.rl_qrcode);
        rl_page_out_time = (RelativeLayout) findViewById(R.id.rl_page_out_time);
        tv_click_reget = (TextView) findViewById(R.id.tv_click_reget);
        tv_get_pay_result = (TextView) findViewById(R.id.tv_get_pay_result);

        ll_cancel = (LinearLayout) findViewById(R.id.ll_cancel);
        tv_title = (TextView) findViewById(R.id.tv_title);
        title_all = (RelativeLayout) findViewById(R.id.title_all);
        title_all.setBackgroundColor(Color.parseColor("#6e6e6e"));
        iv_qrcode = (ImageView) findViewById(R.id.iv_qrcode);
        tv_please_scan_code = (TextView) findViewById(R.id.tv_please_scan_code);
        tv_price_year = (TextView) findViewById(R.id.tv_price_year);

        ll_cancel.setOnClickListener(this);
        ll_loading.setClickToReload(this);
        tv_click_reget.setOnClickListener(this);
        tv_get_pay_result.setOnClickListener(this);
    }

    private void getIntentData() {
        ChargeItem pay_item = (ChargeItem) getIntent().getSerializableExtra("pay_item");
        if (pay_item != null) {
            chargeItem = pay_item;
        }
        String upgradeMoneyExtra = getIntent().getStringExtra("upgrade_money");
        if (upgradeMoneyExtra != null) {
            upgradeMoney = upgradeMoneyExtra;
        }
        String paywayExtra = getIntent().getStringExtra("payway");
        if (paywayExtra != null) {
            payway = paywayExtra;
        }
        String ccyExtra = getIntent().getStringExtra("ccy");
        if (paywayExtra != null) {
            ccy = ccyExtra;
        }
        switch (payway) {
            case "wx":
                tv_title.setText("微信支付");
                tv_please_scan_code.setText("请使用手机微信扫码支付");
                break;
            case "ali":
                tv_title.setText("支付宝支付");
                tv_please_scan_code.setText("请使用手机支付宝扫码支付");
                break;
            case "tbcode":
                tv_title.setText("兑换码升级");
                tv_please_scan_code.setText("请使用手机淘宝扫码购买兑换码");
                tv_price_year.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void ClickToReload() {
        initData(GET_QR_CODE);
    }

    public void reLoad() {
        tv_loading.setText("拼命加载中...");
        pb_loading.setVisibility(View.VISIBLE);
        iv_jiazai_filure.setVisibility(View.GONE);
    }

    public void netError() {
        iv_jiazai_filure.setVisibility(View.VISIBLE);
        iv_jiazai_filure.setImageResource(R.drawable.net_error);
        pb_loading.setVisibility(View.GONE);
        tv_loading.setText("请检查网络后，点击屏幕重新加载！");
    }

    public void noData() {
        ll_loading.setVisibility(View.VISIBLE);
        tv_loading.setText("暂无数据，请点击重试！");
        pb_loading.setVisibility(View.GONE);
        iv_jiazai_filure.setVisibility(View.GONE);
    }

    public void loadingDismiss() {
        ll_loading.setVisibility(View.GONE);
    }

    private void initData(final int code_or_upgrade) {//请求二维码
        reLoad();
        netWorks.getQRCodepollingPayResult(code_or_upgrade, 0,payway, ccy, upgradeMoney, OnNetCallBack);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_cancel:
                backClear();
                finish();
                break;
            case R.id.tv_click_reget://点击重新加载
                if (!isReturnCodeRes) {
                    isReturnCodeRes = true;
                    isTimeGone = false;
                    recLen = TIME_LENGTH;
                    initData(GET_QR_CODE);
                }
                break;
            case R.id.tv_get_pay_result://重新查询付款结果
                if (!isReturnPayRes) {
                    isReturnPayRes = true;
                    ManualQueryPayResult();
                }
                break;
        }
    }

    //轮询支付结果
    private void pollingPayResult() {
        netWorks.getQRCodepollingPayResult(GET_UPGRADE_RESULT,1, payway, ccy, upgradeMoney, OnNetCallBack);
    }
    //手动查询支付结果
    private void ManualQueryPayResult() {
        netWorks.getQRCodepollingPayResult(MANUAL_QUERY_PAY_RESULT,1, payway, ccy, upgradeMoney, OnNetCallBack);
    }

    /**
     * 解析数据：请求二维码或者请求支付结果
     *
     * @param code_or_upgrade
     * @param response
     */
    private void parseJson(int code_or_upgrade, String response) {
        switch (code_or_upgrade) {
            case GET_QR_CODE:
                getQRCodeResult(response);
                break;
            case GET_UPGRADE_RESULT:
                getUpgradeResult(response);
                break;
            case MANUAL_QUERY_PAY_RESULT:
                manulGetUpgradeResult(response);
                break;
        }
    }

    /**
     * 解析二维码
     *
     * @param response
     */
    private void getQRCodeResult(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {      //生成码,做显示
                LogUtils.e(TAG, "200 生成码,做显示或者跳成功");
                switch (payway) {
                    case "ali":
                    case "wx":
                    case "tbcode":
                        int error_code = 0;
                        try {
                            error_code = jsonObject.getJSONObject("msg").getInt("error");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (error_code == 602) {
                            LogUtils.e(TAG, "请求码时候已经开通，直接跳转成功页面！");
                            toSuccessActivity(jsonObject);
                        } else {
                            linkstr = jsonObject.getJSONObject("msg").getString("linkstr");
                            try {
                                upgrade_price = String.valueOf(jsonObject.getJSONObject("msg").getDouble("price"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            showQRCode();
//                            showCode();
                        }
                        break;
                }
            } else {
                noData();
            }
        } catch (JSONException e) {
            LogUtils.e(TAG, "请求微信支付宝淘宝码返回：JSONException e=" + e);
            e.printStackTrace();
        }
    }

    private void showCode() {
        x.image().bind(iv_qrcode,linkstr);
    }

    /**
     * 手动请求结果
     * @param response
     */
    private void manulGetUpgradeResult(String response) {
        LogUtils.Log("手动查询response==" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            switch (code) {
                case 200://升级成功 -> 停止查询
                    LogUtils.e(TAG, "200 升级成功 -> 停止查询");
                case 602://账号已经是高级账号了无需再次升级 ->停止查询
                    LogUtils.e(TAG, "602 账号已经是高级账号了无需再次升级 ->停止查询");
                    handler.removeMessages(POLLING_PAY_RESULT);
                    toSuccessActivity(jsonObject);
                    break;
                default:
                    LogUtils.e(TAG, "手动请求结果:未开通...");
                    goToUpgrandFail();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void toSuccessActivity(JSONObject jsonObject) throws JSONException {
        Intent intent = new Intent(this, UpgradeAccountSuccessActivity.class);
        int accty = jsonObject.getJSONObject("msg").getInt("accty");
        String endtime = jsonObject.getJSONObject("msg").getString("endtime");
        String accname = jsonObject.getJSONObject("msg").getString("accname");
        intent.putExtra("accname", accname);
        intent.putExtra("accty", accty);
        intent.putExtra("endtime", endtime);
        startActivity(intent);
        finish();
    }

    private void goToUpgrandFail() {
        Intent intent = new Intent(this,UpgradeAccountFailActivity.class);
        intent.putExtra("pay_item",chargeItem);
        this.startActivity(intent);
        finish();
    }

    /**
     * 解析支付结果
     *
     * @param response
     */
    private void getUpgradeResult(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            switch (code) {
                case 200://升级成功 -> 停止查询
                    LogUtils.e(TAG, "200 升级成功 -> 停止查询");
                case 602://账号已经是高级账号了无需再次升级 ->停止查询
                    LogUtils.e(TAG, "602 账号已经是高级账号了无需再次升级 ->停止查询");
                    handler.removeMessages(POLLING_PAY_RESULT);
                    handler.removeMessages(CUT_DOWN_TIMMER);
                    toSuccessActivity(jsonObject);
                    break;
                /*case 603://账号暂时还不是高级版账号 -> 接着查
                    break;
                case 3005://暂无符合条件的数据 -> 接着查
                    break;
                case 3006://传递参数有误导致数据保存失败 -> 接着查
                    break;*/
                default:
                    LogUtils.e(TAG, "getUpgradeResult 继续轮询...isTimeGone=" + isTimeGone);
                    if (!isTimeGone) {
                        handler.sendEmptyMessage(POLLING_PAY_RESULT);
                    }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void cutDownTimer() {
        LogUtils.e(TAG, "cutDownTimer():recLen=" + recLen);
        recLen--;
        if (recLen > 0) {
            handler.sendEmptyMessageDelayed(CUT_DOWN_TIMMER, 1000);
            if (recLen <= 5) {
                isTimeGone = true;
            }
        } else {
            //停止二维码的展示
            LogUtils.e(TAG, "到计时结束：停止二维码的展示");
            pageOutTime();
        }
    }

    private void pageOutTime() {
        rl_qrcode.setVisibility(View.GONE);
        rl_page_out_time.setVisibility(View.VISIBLE);
    }

    private void regetQCode() {
        rl_qrcode.setVisibility(View.VISIBLE);
        rl_page_out_time.setVisibility(View.GONE);
    }

    /**
     * 显示二维码信息
     */
    private void showQRCode() {

        if (linkstr != null && (!"".equals(linkstr))) {
            try {
                LogUtils.e(TAG, "showQRCode...");
                if("tbcode".equals(payway)){
                    Bitmap tbcode = EncodingHandler.createQRCode(linkstr, 500);
                    iv_qrcode.setImageBitmap(tbcode);
                }else{
                    showShouldPay();
                    showCode();
                    regetQCode();
                    handler.sendEmptyMessage(POLLING_PAY_RESULT);
                    handler.sendEmptyMessage(CUT_DOWN_TIMMER);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                LogUtils.e(TAG, "Exception e=" + e);
                e.printStackTrace();
            }
        } else {
            LogUtils.e(TAG, "showQRCode(),linkstr==null|| .equals(linkstr))");
        }
    }

    private void showShouldPay() {
        String whichModular = chargeItem.getWhichModular();
        if("wm".equals(whichModular)) {
            tv_price_year.setText("外卖多平台接单：￥" + upgrade_price + "/年");
        }else if("xd".equals(whichModular)) {
            tv_price_year.setText("高效点单：￥" + upgrade_price + "/年");
        }else if("mdp".equals(whichModular)){
            tv_price_year.setText("智慧门店：￥" + upgrade_price + "/年");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();//调用finish()方法
        backClear();
    }

    private void backClear() {
        recLen = 0;
        isTimeGone = true;
        handler = null;
    }
}
