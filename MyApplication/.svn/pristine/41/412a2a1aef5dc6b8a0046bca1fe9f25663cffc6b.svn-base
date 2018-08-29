package com.younle.younle624.myapplication.activity.orderguide;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.domain.membercharge.ChargeItem;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.NetWorks;
import com.younle.younle624.myapplication.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class UpgradeAccountActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "UpgradeAccountActivity";
    private RadioGroup rgp_upgrade;
    private RelativeLayout rl_wx_pay_way;
    private RelativeLayout rl_zfb_pay_way;
    private EditText et_enter_redeemcode;
    private Button btn_redeem_confirm;
    private TextView tv_how_to_get_redeemcode;
    private TextView tv_title;
    private LinearLayout ll_cancel;
    private String upgrade_money;
    private ChargeItem whichModularOpen;
    private RadioButton rbtn_buy_upgrade;
    private RadioButton rbtn_enter_redeemcode_upgrade;

    private LinearLayout ll_loading;
    private ProgressBar pb_loading;
    private TextView tv_loading;
    private ImageView iv_jiazai_filure;

    private TextView tv_price;

    private NetWorks netWorks = new NetWorks(this);
    private NetWorks.OnNetCallBack OnNetCallBack = new NetWorks.OnNetCallBack() {
        @Override
        public void onError(Exception e, int flag) {
            Utils.dismissWaittingDialog();
            LogUtils.e(TAG, "兑换码网络请求：Exception e=" + e);
            Utils.showToast(UpgradeAccountActivity.this, "网络请求失败，请重试！");
        }

        @Override
        public void onResonse(String response, int flag) {
            Utils.dismissWaittingDialog();
            LogUtils.e(TAG, "兑换码网络请求：response=" + response);
            boolean b = Utils.checkSaveToken(UpgradeAccountActivity.this, response);
            if (b) {
                parseJson(response);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_account);
        initView();
        initData();
        getPrice();
        setListener();
    }
    private void initData() {
        ChargeItem pay_item = (ChargeItem) getIntent().getSerializableExtra("pay_item");
        if(pay_item!=null){
            whichModularOpen = pay_item;
        }
        //设置标题，会员营销是升级，外卖是开通
        if("wm".equals(pay_item.getWhichModular())) {
            rbtn_buy_upgrade.setText("购买开通");
            rbtn_enter_redeemcode_upgrade.setText("兑换码开通");
        }else {
            tv_how_to_get_redeemcode.setVisibility(View.GONE);
        }
    }
    private void getPrice() {
        netWorks.getModulePrice(1,whichModularOpen.getWhichModular(), new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                loadFailure(R.string.get_moduleprice_fail);
                LogUtils.e(TAG,"获取版块开通价格异常："+e.toString());
            }

            @Override
            public void onResonse(String response, int flag) {
                ll_loading.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    long price = jsonObject.getJSONObject("msg").getLong("m");
                    if(price/1000>=0) {
                        upgrade_money=price/1000+"";
                        tv_price.setText("￥"+price/1000+"/年");
                    }
                } catch (JSONException e) {
                    tv_price.setText("数据异常，请联系您的销售人员");
                }
                LogUtils.e(TAG, "获取版块开通价格：" + response.toString());
            }
        });
    }

    /**
     * 联网获取数据失败
     */
    public void loadFailure(int stringID){//服务器异常
        iv_jiazai_filure.setVisibility(View.VISIBLE);
        iv_jiazai_filure.setImageResource(R.drawable.couldnot_get_data);
        tv_loading.setText(stringID);
        pb_loading.setVisibility(View.GONE);
    }

    private void initView() {
        ll_loading = (LinearLayout)findViewById(R.id.ll_loading);
        pb_loading = (ProgressBar)findViewById(R.id.pb_loading);
        tv_loading = (TextView)findViewById(R.id.tv_loading);
        iv_jiazai_filure = (ImageView)findViewById(R.id.iv_jiazai_filure);

        ll_cancel = (LinearLayout) findViewById(R.id.ll_cancel);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("开通方式");
        rgp_upgrade = (RadioGroup) findViewById(R.id.rgp_upgrade);
        rl_wx_pay_way = (RelativeLayout) findViewById(R.id.rl_wx_pay_way);
        rl_zfb_pay_way = (RelativeLayout) findViewById(R.id.rl_zfb_pay_way);

        et_enter_redeemcode = (EditText) findViewById(R.id.et_enter_redeemcode);
        btn_redeem_confirm = (Button) findViewById(R.id.btn_redeem_confirm);
        tv_how_to_get_redeemcode = (TextView) findViewById(R.id.tv_how_to_get_redeemcode);
        tv_how_to_get_redeemcode.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tv_how_to_get_redeemcode.getPaint().setAntiAlias(true);//抗锯齿
        rbtn_buy_upgrade = (RadioButton)findViewById(R.id.rbtn_buy_upgrade);
        rbtn_enter_redeemcode_upgrade = (RadioButton)findViewById(R.id.rbtn_enter_redeemcode_upgrade);

        tv_price = (TextView)findViewById(R.id.tv_price);
    }

    private void setListener() {

        rl_wx_pay_way.setOnClickListener(this);
        rl_zfb_pay_way.setOnClickListener(this);
        btn_redeem_confirm.setOnClickListener(this);
        tv_how_to_get_redeemcode.setOnClickListener(this);
        ll_cancel.setOnClickListener(this);

        rgp_upgrade.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_buy_upgrade://购买升级
                        buyToUpgrade();
                        break;
                    case R.id.rbtn_enter_redeemcode_upgrade://兑换码升级
                        enterRedeemcodeUpgrade();
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_wx_pay_way://微信支付
                getUpdateCode(R.id.rl_wx_pay_way);
                break;
            case R.id.rl_zfb_pay_way://支付宝支付
                getUpdateCode(R.id.rl_zfb_pay_way);
                break;
            case R.id.tv_how_to_get_redeemcode://怎么获取兑换码
                getUpdateCode(R.id.tv_how_to_get_redeemcode);
                break;
            case R.id.btn_redeem_confirm://点击兑换码兑换
                postToExchange();
                break;
            case R.id.ll_cancel://取消
                finish();
                break;
        }
    }

    private void postToExchange() {
        String openWhich = whichModularOpen.getWhichModular();
        LogUtils.Log("openWhich==" + openWhich);
        if (et_enter_redeemcode.getText() != null && !"".equals(et_enter_redeemcode.getText().toString().trim())) {
            Utils.showWaittingDialog(UpgradeAccountActivity.this,"兑换码验证中...");
            netWorks.redeemCodeToUprade(0, openWhich,et_enter_redeemcode.getText().toString().trim(), OnNetCallBack);
        } else {
            Utils.showToast(this, "请输入正确格式兑换码！");
        }
    }

    private void parseJson(String response) {
        LogUtils.Log("兑换返回json==" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            String code = jsonObject.getString("code");
            switch (code) {
                case "200":
                    Intent intent = new Intent(this, UpgradeAccountSuccessActivity.class);
                    int accty = jsonObject.getJSONObject("msg").getInt("accty");
                    String endtime = jsonObject.getJSONObject("msg").getString("endtime");
                    intent.putExtra("accty", accty);
                    intent.putExtra("endtime", endtime);
                    intent.putExtra("accname",jsonObject.getJSONObject("msg").getString("accname"));
                    startActivity(intent);
                    break;
                default:
                    Utils.showToast(UpgradeAccountActivity.this, jsonObject.getString("msg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getUpdateCode(int id) {
        Intent intent = new Intent(UpgradeAccountActivity.this, QRCodeActivity.class);
        intent.putExtra("upgrade_money", upgrade_money);
        intent.putExtra("ccy", whichModularOpen.getWhichModular());//升级模块的标识
        intent.putExtra("pay_item", whichModularOpen);//升级模块的标识
        switch (id) {
            case R.id.rl_wx_pay_way://微信
                intent.putExtra("payway", "wx");
                startActivity(intent);
                break;
            case R.id.rl_zfb_pay_way://支付宝
                intent.putExtra("payway", "ali");
                startActivity(intent);
                break;
            case R.id.tv_how_to_get_redeemcode:
                intent.putExtra("payway", "tbcode");
                startActivity(intent);
                break;
        }

    }

    private void buyToUpgrade() {
        tv_price.setVisibility(View.VISIBLE);
        rl_wx_pay_way.setVisibility(View.VISIBLE);
        rl_zfb_pay_way.setVisibility(View.VISIBLE);
//        rg_choose_fee_upgrade.setVisibility(View.VISIBLE);
        et_enter_redeemcode.setVisibility(View.GONE);
        btn_redeem_confirm.setVisibility(View.GONE);
        tv_how_to_get_redeemcode.setVisibility(View.GONE);
    }

    private void enterRedeemcodeUpgrade() {
        tv_price.setVisibility(View.GONE);
        rl_wx_pay_way.setVisibility(View.GONE);
        rl_zfb_pay_way.setVisibility(View.GONE);
//        rg_choose_fee_upgrade.setVisibility(View.GONE);
        et_enter_redeemcode.setVisibility(View.VISIBLE);
        btn_redeem_confirm.setVisibility(View.VISIBLE);

        if("wm".equals(whichModularOpen.getWhichModular())) {
            tv_how_to_get_redeemcode.setVisibility(View.VISIBLE);
        }
        //设置edittext自动获取光标
        et_enter_redeemcode.requestFocus();

    }
}
