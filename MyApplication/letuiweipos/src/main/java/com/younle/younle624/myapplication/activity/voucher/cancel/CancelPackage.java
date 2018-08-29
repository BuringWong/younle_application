package com.younle.younle624.myapplication.activity.voucher.cancel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.constant.UrlConstance;
import com.younle.younle624.myapplication.domain.PayCardinfoBean;
import com.younle.younle624.myapplication.domain.VoucherCardInfo;
import com.younle.younle624.myapplication.domain.VoucherRead;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.MD5;

import okhttp3.Call;


/**
 * Created by Administrator on 2016/5/20.
 * 代金券详情展示以及核销界面
 */
public class

CancelPackage extends Activity implements View.OnClickListener {

    private static final int SUCCESS = 0;
    private static final String TAG = "CancelPackage";
    //private static final int FAIL=1;
    private ListView lv;
    private Button btn_click_to_use;
    private TextView tvTitle;
    private LinearLayout ll_cancel;
    private VoucherRead voucherReadBean;
    private VoucherCardInfo voucherCardInfo;
    private int from_where = -1;

    private LinearLayout ll_voucher_function;
    private TextView tv_voucher_function;
    private TextView tv_package;
    private TextView tv_content;
    private TextView tv_validity_period;
    private TextView tv_status;
    private AlertDialog alertDialog;
    private String voucherCode = "";
    private String card_type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voucher_verification_package);
        Utils.initToolBarState(this);
        getInfoFromIntent();
        initView();
        initData();
        setListener();
    }

    private void getInfoFromIntent() {
        voucherReadBean = (VoucherRead) getIntent().getSerializableExtra(Constant.VOUCHER_READ_BEAN);
        voucherCardInfo = (VoucherCardInfo) getIntent().getSerializableExtra(Constant.VOUCHER_CARD_BEAN);
        from_where = getIntent().getIntExtra(Constant.FROME_WHERE, -1);
        LogUtils.Log("vouchercode=="+voucherCode);
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("核销内容");
        ll_cancel = (LinearLayout) findViewById(R.id.ll_cancel);
        ll_cancel.setVisibility(View.VISIBLE);
        btn_click_to_use = (Button) findViewById(R.id.btn_confirm);

        ll_voucher_function = (LinearLayout) findViewById(R.id.ll_voucher_function);
        tv_voucher_function = (TextView)findViewById(R.id.tv_voucher_function);
        tv_package = (TextView)findViewById(R.id.tv_package);
        tv_content = (TextView)findViewById(R.id.tv_content);
        tv_validity_period = (TextView)findViewById(R.id.tv_validity_period);
        tv_status = (TextView)findViewById(R.id.tv_status);
    }

    private void initData() {
        switch (from_where){
            case 0://H5信息
                if(voucherReadBean != null) {
                    card_type = "0";
                    voucherCode = voucherReadBean.getMsg().getSecmark();//核销码
                    tv_package.setText(voucherReadBean.getMsg().getServer_name());
                    tv_validity_period.setText(voucherReadBean.getMsg().getVhs_start() +"至"+ voucherReadBean.getMsg().getVhs_end());
                    String vhs_info = voucherReadBean.getMsg().getVhs_info();
                    Spanned spanned = Html.fromHtml(Html.fromHtml(vhs_info).toString());
                    LogUtils.Log("spanned" + spanned);
                    tv_content.setText(spanned);
                    switch (voucherReadBean.getMsg().getStatus()) {
                        case "核销" :
                            tv_status.setVisibility(View.GONE);
                            btn_click_to_use.setVisibility(View.VISIBLE);
                            break;
                        default:
                            tv_status.setVisibility(View.VISIBLE);
                            tv_status.setText(voucherReadBean.getMsg().getStatus());
                            btn_click_to_use.setVisibility(View.GONE);
                            break;
                    }
                }else {
                    Utils.showToast(this,"信息有误，重新输入或扫描");
                    finish();
                }
                break;
            case 1://卡券信息
                if(voucherCardInfo != null) {
                    card_type = "1";
                    tv_package.setText(voucherCardInfo.getMsg().getTitle());
                    tv_validity_period.setText(voucherCardInfo.getMsg().getUsetime());
                    tv_content.setText(voucherCardInfo.getMsg().getInfo());
                    btn_click_to_use.setVisibility(View.VISIBLE);
                    if(voucherCardInfo.getMsg().getCardmsg()!=null&&!"".equals(voucherCardInfo.getMsg().getCardmsg())){
                        ll_voucher_function.setVisibility(View.VISIBLE);
                        tv_voucher_function.setText(voucherCardInfo.getMsg().getCardmsg());
                    }
                }else{
                    Utils.showToast(this,"信息有误，重新输入或扫描");
                    finish();
                }
                break;
        }
    }

    private void setListener() {
        ll_cancel.setOnClickListener(this);
        btn_click_to_use.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_cancel :
                finish();
                break;
            case R.id.btn_confirm://核销，传入门店id和核销码，对代金券进行核销
                hxVoucher();
                break;
        }
    }

    private void hxVoucher() {
        alertDialog = Utils.wybDialog(this, false, R.layout.voucher_hexiao_dialog, R.id.tv_voucher_search, 400, 240, "核销中");
        String currentTime = Utils.getCurrentTime();
        String token = Utils.getToken(currentTime,this);
        String cardinfoJson = getCardinfoJson();

        LogUtils.e(TAG,"account_id="+Constant.ACCOUNT_ID);
        LogUtils.e(TAG,"advid="+Constant.ADV_ID);
        LogUtils.e(TAG,"storeid="+Constant.STORE_ID);
        LogUtils.e(TAG,"code="+voucherCode);
        LogUtils.e(TAG,"card_type="+card_type);
        LogUtils.e(TAG,"cardinfo="+cardinfoJson);

        OkHttpUtils.post()
                .url(UrlConstance.HX_VOUCHER_URL)
                .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                .addParams("userkey", MD5.md5(MD5.md5(Constant.PASSWORD)))//密码
                .addParams("account_id", Constant.ACCOUNT_ID)
                .addParams("storename", Constant.STORE_M)
                .addParams("advid", Constant.ADV_ID)
                .addParams("storeid",Constant.STORE_ID)
                .addParams("code", voucherCode)
                .addParams("card_type", card_type)
                .addParams("cardinfo", cardinfoJson)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        alertDialog.dismiss();
                        Toast.makeText(CancelPackage.this, "连接超时，请检查网络后重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtils.Log("核销返回："+response);
                        boolean toNextStep = Utils.checkSaveToken(CancelPackage.this, response);
                        alertDialog.dismiss();
                        if (toNextStep){
                            alertDialog.dismiss();
                            PraseJson(response);
                        }
                    }
                });

    }

    private void PraseJson(String json) {
        try {
            JSONObject jsonObject=new JSONObject(json);
            int code = jsonObject.getInt("code");
            String msg = jsonObject.getString("msg");
            switch (code) {
                case 200 :
                    startActivity(new Intent(CancelPackage.this,CancelSuccess.class));
                    finish();
                    break;
                case 1001:
                case 1002:
                case 1003:
                    Toast.makeText(CancelPackage.this, msg, Toast.LENGTH_SHORT).show();
                    break;
                case 1006:
                case 1007:
                case 1008:
                    Intent intent = new Intent(CancelPackage.this,CancelFail.class);
                    intent.putExtra("error_msg",msg);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    Intent intent2 = new Intent(CancelPackage.this,CancelFail.class);
                    intent2.putExtra("error_msg",msg);
                    startActivity(intent2);
                    finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getCardinfoJson() {
        if(voucherCardInfo != null){
            PayCardinfoBean payCardinfoBean = new PayCardinfoBean();
            LogUtils.e(TAG,"getCardinfoJson Code="+voucherCardInfo.getMsg().getCode());
            payCardinfoBean.setCode(voucherCardInfo.getMsg().getCode());

            LogUtils.e(TAG,"getCardinfoJson Cardid="+voucherCardInfo.getMsg().getCardid());
            payCardinfoBean.setCardid(voucherCardInfo.getMsg().getCardid());

            LogUtils.e(TAG,"getCardinfoJson Cardtype="+voucherCardInfo.getMsg().getCard_type());
            payCardinfoBean.setCardtype(voucherCardInfo.getMsg().getCard_type());

            LogUtils.e(TAG,"getCardinfoJson Least_cost="+voucherCardInfo.getMsg().getLeast_cost());
            payCardinfoBean.setLeast_cost(String.valueOf(voucherCardInfo.getMsg().getLeast_cost()));

            LogUtils.e(TAG,"getCardinfoJson Reduce_cost="+voucherCardInfo.getMsg().getReduce_cost());
            payCardinfoBean.setReduce_cost(String.valueOf(voucherCardInfo.getMsg().getReduce_cost()));

            LogUtils.e(TAG,"getCardinfoJson Vipcardid="+voucherCardInfo.getMsg().getVipcardid());
            payCardinfoBean.setVipcardid(String.valueOf(voucherCardInfo.getMsg().getVipcardid()));

            LogUtils.e(TAG,"getCardinfoJson Info="+voucherCardInfo.getMsg().getInfo());
            payCardinfoBean.setInfo(voucherCardInfo.getMsg().getInfo());
            return new Gson().toJson(payCardinfoBean);
        }else{
            return "";
        }
    }
}
