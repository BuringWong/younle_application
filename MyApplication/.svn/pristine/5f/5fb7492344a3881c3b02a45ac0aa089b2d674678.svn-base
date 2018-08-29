package com.younle.younle624.myapplication.pagers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.manager.orderpager.pos.PayQRCodeActivity;
import com.younle.younle624.myapplication.activity.manager.orderpager.pos.PosOrderActivity;
import com.younle.younle624.myapplication.activity.pos.CollectionFailActivity;
import com.younle.younle624.myapplication.activity.pos.CollectionSuccessActivity;
import com.younle.younle624.myapplication.basepager.BasePager;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.constant.UrlConstance;
import com.younle.younle624.myapplication.domain.Compute;
import com.younle.younle624.myapplication.domain.InputItem;
import com.younle.younle624.myapplication.domain.PayCardinfoBean;
import com.younle.younle624.myapplication.domain.ResultQueryBean;
import com.younle.younle624.myapplication.domain.SavedFailOrder;
import com.younle.younle624.myapplication.domain.orderbean.PosCashVoucherBean;
import com.younle.younle624.myapplication.domain.orderbean.PosDiscountVoucherBean;
import com.younle.younle624.myapplication.domain.orderbean.PosMemberBean;
import com.younle.younle624.myapplication.domain.paybean.PayParams;
import com.younle.younle624.myapplication.utils.CashCardPayUtils;
import com.younle.younle624.myapplication.utils.ClicKUtils;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.NetWorks;
import com.younle.younle624.myapplication.utils.NetworkUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.MD5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import zxing.activity.CaptureActivity;

/**
 * Created by 我是奋斗 on 2016/6/3.
 * 微信/e-mail:tt090423@126.com
 */

public class PosPager extends BasePager implements View.OnClickListener {

    private static final int RIGHT_CODE = 1;
    private static final int FAIL = 2;
    private static final int UNKNOW_PAY_RESULT = 3;
    private static final int CLEAR_ALL_MONEY = 100;
    private static final int PAY_MEMBER_BALANCE = 4;
    private static final int CUT_DOWN_TIMMER = 5;
    private static final int QUERY_ORDER_STATE = 6;
    private final int PAY_FLAG_QERY_PAY_STATE = 7;
    private Button mBtn0;
    private Button mBtn1;
    private Button mBtn2;
    private Button mBtn3;
    private Button mBtn4;
    private Button mBtn5;
    private Button mBtn6;
    private Button mBtn7;
    private Button mBtn8;
    private Button mBtn9;
    private Button mBtnAdd;
    private Button mBtnSubraction;
    private Button mMultip;
    private Button mBtnDiv;
    private Button mBtnSpot;
    private HashMap<View, String> map;
    private static final String TAG = PosPager.class.getSimpleName();
    private TextView mEtPrint;
    private TextView mCurrent;
    private ImageView mIvDelete;
    private Button mIvDeleteAll;
    private final double MAX_MONEY = 999999.99;
    private final double MIN_MONEY = 0.01;
    private final double MIN_ENTER_MONEY = -999999.99;
    private TextView tv_title;
    private int mLastInputstatus = INPUT_NUMBER; //记录上一次输入状态
    public static final int INPUT_NUMBER = 1;
    public static final int INPUT_POINT = 0;
    public static final int INPUT_OPERATOR = -1;
    public static final int END = -2;
    public static final int ERROR = -3;
    private List<InputItem> mInputList; //定义记录每次输入的数
    private String trade_num = "";  //订单号
    private String trade_time = ""; //交易时间
    private String failreason = mActivity.getResources().getString(R.string.network_abnormal);
    private String solveway = mActivity.getResources().getString(R.string.network_abnormal_solve_way);
    private String query_num = "0";
    private String freemoney = "0";//会员卡余额
    private String payment="0";
    private boolean isLimitzfbwx;
    private Button btn_start_pay;
    private PopupWindow popupWindow;
    //private View popView;
    private PosMemberBean memberBean;
    private PosDiscountVoucherBean discountVoucherBean;
    private PosCashVoucherBean cashVoucherBean;
    private int recLen = Constant.RECLEN_TIME;
    private TextView tv_cout_timmer;
    private PopupWindow waittingPop;
    private boolean isClickCancel;
    private boolean isTimeGone;
    private final int NO_PAY_RESULT = 0;
    private final int MAY_PAY_SUCCESS = 1;
    private String transaction_id = "";
    private String total_fee = "0.00";
    private String cardinfo_pay = "";

    public PosPager(Activity activity) {
        super(activity);
        EventBus.getDefault().register(this);
    }

    public static final PosPager newInstance(Activity activity) {
        PosPager fragment = new PosPager(activity);
        return fragment;
    }
    private NetWorks netWorks;
    private NetWorks.OnNetCallBack callBack = new NetWorks.OnNetCallBack() {
        @Override
        public void onError(Exception e, int flag) {
            switch (flag){
                case PAY_MEMBER_BALANCE://会员卡余额支付
                    dealpayError();
                    break;
                case PAY_FLAG_QERY_PAY_STATE:
                    dealQueryError();
                    break;
            }
        }

        @Override
        public void onResonse(String response, int flag) {
            switch (flag){
                case PAY_MEMBER_BALANCE://会员卡余额支付
                    dealpayRes(response);
                    break;
                case PAY_FLAG_QERY_PAY_STATE:
                    dealQueryRes(response);
                    break;
            }
        }
    };

    private void toCaptureActivity() {
        Constant.payway = "-1";
        Intent intent = new Intent(mActivity, CaptureActivity.class);
        intent.putExtra(Constant.FROME_WHERE, Constant.MEMBER_DIRECT_PAY);
        intent.putExtra("total_fee", total_fee);
        intent.putExtra("commit", "1");
        intent.putExtra("type_for_mc", "1");
        mActivity.startActivity(intent);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CLEAR_ALL_MONEY:
                    clearMoney();
                    break;
                case CUT_DOWN_TIMMER:
                    cutDownTimer();
                    break;
                case QUERY_ORDER_STATE:
                    queryOrderState();
                    break;
            }
        }
    };

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pos_count, null);
        btn_start_pay = (Button) view.findViewById(R.id.btn_start_pay);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText(mActivity.getResources().getString(R.string.enter_pay_money));
        mBtn0 = (Button) view.findViewById(R.id.btn0);
        mBtn1 = (Button) view.findViewById(R.id.btn1);
        mBtn2 = (Button) view.findViewById(R.id.btn2);
        mBtn3 = (Button) view.findViewById(R.id.btn3);
        mBtn4 = (Button) view.findViewById(R.id.btn4);
        mBtn5 = (Button) view.findViewById(R.id.btn5);
        mBtn6 = (Button) view.findViewById(R.id.btn6);
        mBtn7 = (Button) view.findViewById(R.id.btn7);
        mBtn8 = (Button) view.findViewById(R.id.btn8);
        mBtn9 = (Button) view.findViewById(R.id.btn9);
        mBtnAdd = (Button) view.findViewById(R.id.btn_Add);
        mBtnSubraction = (Button) view.findViewById(R.id.btn_subtraction);
        mMultip = (Button) view.findViewById(R.id.btn_multip);
        mBtnDiv = (Button) view.findViewById(R.id.btn_Div);
        mBtnSpot = (Button) view.findViewById(R.id.btn_Spot);
        mCurrent = (TextView) view.findViewById(R.id.et_current);
        mCurrent.setHorizontallyScrolling(true);
        mCurrent.setFocusable(true);
        mIvDelete = (ImageView) view.findViewById(R.id.iv_delet);
        mIvDeleteAll = (Button) view.findViewById(R.id.bt_delete_all);
        mEtPrint = (TextView) view.findViewById(R.id.et_print);
        setOnClickListener();//调用监听事件
        netWorks = new NetWorks(mActivity);
        return view;
    }

    private void setOnClickListener() {
        btn_start_pay.setOnClickListener(this);
        mIvDeleteAll.setOnClickListener(this);
        mIvDelete.setOnClickListener(this);
        mBtnAdd.setOnClickListener(this);
        mBtnSubraction.setOnClickListener(this);
        mMultip.setOnClickListener(this);
        mBtnDiv.setOnClickListener(this);
        mBtn0.setOnClickListener(this);
        mBtn1.setOnClickListener(this);
        mBtn2.setOnClickListener(this);
        mBtn3.setOnClickListener(this);
        mBtn4.setOnClickListener(this);
        mBtn5.setOnClickListener(this);
        mBtn6.setOnClickListener(this);
        mBtn7.setOnClickListener(this);
        mBtn8.setOnClickListener(this);
        mBtn9.setOnClickListener(this);
        mBtnSpot.setOnClickListener(this);
        MyTextWatcher myTextWatcher = new MyTextWatcher();
        mCurrent.addTextChangedListener(myTextWatcher);
    }

    @Override
    public void initData() {
        if (map == null) {
            map = new HashMap<>();
            map.put(mBtnAdd, mActivity.getResources().getString(R.string.add));
            map.put(mBtnSubraction, mActivity.getResources().getString(R.string.sub));
            map.put(mMultip, mActivity.getResources().getString(R.string.multply));
            map.put(mBtnDiv, mActivity.getResources().getString(R.string.divide));
            map.put(mBtn0, mActivity.getResources().getString(R.string.zero));
            map.put(mBtn1, mActivity.getResources().getString(R.string.one));
            map.put(mBtn2, mActivity.getResources().getString(R.string.two));
            map.put(mBtn3, mActivity.getResources().getString(R.string.three));
            map.put(mBtn4, mActivity.getResources().getString(R.string.four));
            map.put(mBtn5, mActivity.getResources().getString(R.string.five));
            map.put(mBtn6, mActivity.getResources().getString(R.string.six));
            map.put(mBtn7, mActivity.getResources().getString(R.string.seven));
            map.put(mBtn8, mActivity.getResources().getString(R.string.eight));
            map.put(mBtn9, mActivity.getResources().getString(R.string.nine));
            map.put(mBtnSpot, mActivity.getResources().getString(R.string.point));
            mInputList = new ArrayList<>();
            mEtPrint.setText(mActivity.getResources().getString(R.string.set_two_decimal_places_zero));
            clearInputScreen();
        }
    }

    /**
     * 显示支付Popwindow
     */
    private void showPayPopWindowForCardCoupons(int tag) {

        popupWindow = null;
        Constant.UNKNOWN_COMMODITY = "1";
        final LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popView = inflater.inflate(R.layout.pop_order_pay_way_for_cardcoupons, null);
        TextView tv_should_pay_money = (TextView) popView.findViewById(R.id.tv_should_pay_money);

        RelativeLayout rl_member_pay_money = (RelativeLayout) popView.findViewById(R.id.rl_member_pay_money);//会员卡余额收款
        RelativeLayout rl_member_name = (RelativeLayout) popView.findViewById(R.id.rl_member_name);
        RelativeLayout rl_member_balance = (RelativeLayout) popView.findViewById(R.id.rl_member_balance);
        RelativeLayout rl_member_discount = (RelativeLayout) popView.findViewById(R.id.rl_member_discount);
        LinearLayout ll_member_discount = (LinearLayout) popView.findViewById(R.id.ll_member_discount);
        TextView tv_member_name = (TextView) popView.findViewById(R.id.tv_member_name);
        TextView tv_member_balance = (TextView) popView.findViewById(R.id.tv_member_balance);
        TextView tv_member_discount = (TextView) popView.findViewById(R.id.tv_member_discount);
        ImageView iv_coupons_discount = (ImageView) popView.findViewById(R.id.iv_coupons_discount);
        TextView tv_member_discount_num = (TextView) popView.findViewById(R.id.tv_member_discount_num);
        TextView tv_reduce_money = (TextView) popView.findViewById(R.id.tv_reduce_money);
        TextView consume_explain = (TextView) popView.findViewById(R.id.consume_explain);
        TextView tv_distinguish_member = (TextView) popView.findViewById(R.id.tv_distinguish_member);
        RelativeLayout rl_wx_pay_money = (RelativeLayout) popView.findViewById(R.id.rl_wx_pay_money);
        RelativeLayout rl_zfb_pay_money = (RelativeLayout) popView.findViewById(R.id.rl_zfb_pay_money);
        RelativeLayout rl_card_pay_money = (RelativeLayout) popView.findViewById(R.id.rl_card_pay_money);
        RelativeLayout rl_cash_pay_money = (RelativeLayout) popView.findViewById(R.id.rl_cash_pay_money);
        TextView tv_micro_pay = (TextView) popView.findViewById(R.id.tv_micro_pay);
        TextView tv_ali_pay = (TextView) popView.findViewById(R.id.tv_ali_pay);
        total_fee = mEtPrint.getText().toString();

        //判断是否开通微信/支付宝权限
        if(Constant.OPEN_ALIPAY != 1){
            //显示为记账
            tv_ali_pay.setText("支付宝收款(记账)");
        }
        if(Constant.OPEN_WXPAY != 1){
            //显示为记账
            tv_micro_pay.setText("微信收款(记账)");
        }

        if(tag==1){
            if(memberBean.getMsg().isSupply()) {
                rl_member_pay_money.setVisibility(View.VISIBLE);//会员卡余额收款选项
                rl_member_balance.setVisibility(View.VISIBLE);//会员卡余额
            }
            rl_member_name.setVisibility(View.VISIBLE);//会员名
            tv_member_discount.setVisibility(View.VISIBLE);
            ll_member_discount.setVisibility(View.VISIBLE);//
            tv_member_name.setText(memberBean.getMsg().getName());
            tv_member_balance.setText(String.valueOf(memberBean.getMsg().getMoney()));

            if(memberBean.getMsg().getVip_discount()>0) {
                rl_member_discount.setVisibility(View.VISIBLE);//会员折扣
                tv_reduce_money.setVisibility(View.VISIBLE);//减少的钱数
                tv_member_discount_num.setVisibility(View.VISIBLE);//折扣数
                tv_member_discount_num.setText(String.valueOf(memberBean.getMsg().getVip_discount()) + "折");//需要增加字段
                tv_reduce_money.setText(Utils.keepTwoDecimal(String.valueOf(Double.valueOf(total_fee)-memberBean.getMsg().getPayment())));
            }else {
                rl_member_discount.setVisibility(View.GONE);//会员折扣
            }

            tv_should_pay_money.setText(Utils.keepTwoDecimal(String.valueOf(memberBean.getMsg().getPayment())));//设置收款金额
            payment = String.valueOf(memberBean.getMsg().getPayment());
            cardinfo_pay = "";
        }else if(tag==2){//折扣券
            rl_member_discount.setVisibility(View.VISIBLE);
            iv_coupons_discount.setVisibility(View.VISIBLE);
            tv_member_discount.setVisibility(View.VISIBLE);
            ll_member_discount.setVisibility(View.VISIBLE);//
            iv_coupons_discount.setImageResource(R.drawable.discount_pic);
            tv_reduce_money.setVisibility(View.VISIBLE);//减少的钱数
            tv_member_discount.setText(discountVoucherBean.getMsg().getInfo());
            String reduce_money = Utils.keepTwoDecimal(String.valueOf(Double.valueOf(total_fee) * (1 - discountVoucherBean.getMsg().getDiscount() * 0.1)));
            LogUtils.e(TAG,"reduce_money="+reduce_money);
            tv_reduce_money.setText(Utils.keepTwoDecimal(reduce_money));
            payment = String.valueOf(Double.valueOf(total_fee) - Double.valueOf(reduce_money));
            if(Double.valueOf(payment)>=0.01){
                tv_should_pay_money.setText(Utils.keepTwoDecimal(payment));
                cardinfo_pay = getCardinfoJson(2);
            }else{
                payment = "0.01";
                tv_should_pay_money.setText(Utils.keepTwoDecimal(payment));
                cardinfo_pay = getCardinfoJson(2);
            }
            if(discountVoucherBean.getMsg().getVipcardid()!=null&&!"".equals(discountVoucherBean.getMsg().getVipcardid())&&!"0".equals(discountVoucherBean.getMsg().getVipcardid())){
                consume_explain.setVisibility(View.VISIBLE);
                consume_explain.setText("该顾客为会员："+discountVoucherBean.getMsg().getVipinfo_name()+"，本单记录为会员消费");
            }else{
                consume_explain.setVisibility(View.GONE);
            }
        }else if(tag==3){//现金抵扣券
            rl_member_discount.setVisibility(View.VISIBLE);
            iv_coupons_discount.setVisibility(View.VISIBLE);
            tv_member_discount.setVisibility(View.VISIBLE);
            ll_member_discount.setVisibility(View.VISIBLE);//
            iv_coupons_discount.setImageResource(R.drawable.coupons_pic);
            tv_reduce_money.setVisibility(View.VISIBLE);//减少的钱数
            if(Double.valueOf(total_fee) >= cashVoucherBean.getMsg().getLeast_cost()){
                double pay_money = Double.valueOf(total_fee) - cashVoucherBean.getMsg().getReduce_cost();
                LogUtils.e(TAG,"pay_money=="+pay_money);
                if(pay_money > 0){
                    LogUtils.e(TAG,"pay_money > 0");
                    payment = Utils.keepTwoDecimal(String.valueOf(pay_money));
                    tv_should_pay_money.setText(Utils.keepTwoDecimal(String.valueOf(pay_money)));
                    tv_member_discount.setText(cashVoucherBean.getMsg().getInfo());
                    tv_reduce_money.setText(Utils.keepTwoDecimal(String.valueOf(cashVoucherBean.getMsg().getReduce_cost())));
                    cardinfo_pay = getCardinfoJson(3);
                }else{
                    LogUtils.e(TAG,"pay_money <= 0.01");
                    payment = "0.00";
                    tv_should_pay_money.setText(String.valueOf("0.00"));
                    tv_member_discount.setText(cashVoucherBean.getMsg().getInfo());
                    tv_reduce_money.setText(Utils.keepTwoDecimal(String.valueOf(cashVoucherBean.getMsg().getReduce_cost())));
                    cardinfo_pay = getCardinfoJson(3);
                }
            }else{
                payment = total_fee;
                tv_should_pay_money.setText(Utils.keepTwoDecimal(total_fee));
                tv_member_discount.setText(cashVoucherBean.getMsg().getInfo()+"\n(本次消费未满足本券消费条件暂不能使用。)");
                tv_reduce_money.setText(Utils.keepTwoDecimal(String.valueOf(cashVoucherBean.getMsg().getReduce_cost())));
                cardinfo_pay = "";
            }
            if(cashVoucherBean.getMsg().getVipcardid()!=null&&!"".equals(cashVoucherBean.getMsg().getVipcardid())&&!"0".equals(cashVoucherBean.getMsg().getVipcardid())){
                consume_explain.setVisibility(View.VISIBLE);
                consume_explain.setText("该顾客为会员："+cashVoucherBean.getMsg().getVipinfo_name()+"，本单记录为会员消费");
            }else{
                consume_explain.setVisibility(View.GONE);
            }
        }else{
            rl_member_pay_money.setVisibility(View.GONE);
            tv_should_pay_money.setText(Utils.keepTwoDecimal(total_fee));//设置收款金额
            payment = total_fee;
            cardinfo_pay = "";
            resetBean();
        }

        popView.requestFocus();
        popupWindow = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        setAlpha(0.6f);
        popupWindow.showAtLocation(mActivity.findViewById(R.id.all_pospager), Gravity.BOTTOM, 0, 0);
        popupWindow.setAnimationStyle(R.style.pop_animation);
        popupWindow.update();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAlpha(1);
            }
        });

        //识别会员
        tv_distinguish_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBean();
                //引导开通:跳转到充值
                    netWorks.UpDateAuth(new NetWorks.OnGetQueryResult() {
                        @Override
                        public void stateOpen() {
                            toCaptureActivity();
                        }

                        @Override
                        public void stateClose() {
                        }
                    }, "4");
            }
        });
        //微信付款
        rl_wx_pay_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(judgeAmount(payment)){
                    Constant.payway = "0";
                    toPay();
                }
            }
        });
        //支付宝付款
        rl_zfb_pay_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(judgeAmount(payment)){
                    Constant.payway = "1";
                    toPay();
                }
            }
        });
        //刷卡记账
        rl_card_pay_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(judgeAmount(payment)){
                    //Constant.payway = "2";
                    Constant.payway = "6";
                    toPay();
                }
            }
        });
        //现金记账
        rl_cash_pay_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(judgeAmount(payment)){
                    Constant.payway = "3";
                    toPay();
                }
            }
        });
        //会员卡余额支付
        rl_member_pay_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(judgeAmount(payment)){
                    Constant.payway = "4";
                    showWaittingPopWindow();//倒计时弹框
                    toPay();
                }
            }
        });
    }

    private void resetBean() {
        Constant.MEMBER_ID="";
        cardinfo_pay = "";
        memberBean = null;
        cashVoucherBean = null;
        discountVoucherBean = null;
    }

    /**
     * tag 2为折扣券 3为抵扣券
     * @param tag
     */
    private String getCardinfoJson(int tag) {
        PayCardinfoBean payCardinfoBean = new PayCardinfoBean();
        switch (tag){
            case 2://折扣券
                payCardinfoBean.setCode(discountVoucherBean.getMsg().getCode());
                payCardinfoBean.setCardid(discountVoucherBean.getMsg().getCardid());
                payCardinfoBean.setCardtype(discountVoucherBean.getMsg().getCard_type());
                payCardinfoBean.setDiscount(String.valueOf(discountVoucherBean.getMsg().getDiscount()));
                payCardinfoBean.setVipcardid(discountVoucherBean.getMsg().getVipcardid());
                payCardinfoBean.setInfo(discountVoucherBean.getMsg().getInfo());
                break;
            case 3://抵扣券
                payCardinfoBean.setCode(cashVoucherBean.getMsg().getCode());
                payCardinfoBean.setCardid(cashVoucherBean.getMsg().getCardid());
                payCardinfoBean.setCardtype(cashVoucherBean.getMsg().getCard_type());
                payCardinfoBean.setLeast_cost(String.valueOf(cashVoucherBean.getMsg().getLeast_cost()));
                payCardinfoBean.setReduce_cost(String.valueOf(cashVoucherBean.getMsg().getReduce_cost()));
                payCardinfoBean.setVipcardid(String.valueOf(cashVoucherBean.getMsg().getVipcardid()));
                payCardinfoBean.setInfo(cashVoucherBean.getMsg().getInfo());
                break;
        }
        return new Gson().toJson(payCardinfoBean);
    }

    /**
     * 去被扫码页面
     */
    private void toPayQRCodeActivity() {
        Constant.LAST_ORDER_NO = "";
        Intent intent = new Intent(mActivity, PayQRCodeActivity.class);
        intent.putExtra("total_fee", total_fee);
        intent.putExtra("payment", payment);
        intent.putExtra("cardinfo_pay",cardinfo_pay);
        if(memberBean!=null){//加上会员卡号：
            intent.putExtra("vip_card", memberBean.getMsg().getVipcardid());
            intent.putExtra("vipcreate_id", memberBean.getMsg().getVipcreate_id());
            intent.putExtra("vip_discount", String.valueOf(memberBean.getMsg().getVip_discount()));
        }
        mActivity.startActivity(intent);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        delayClearMoney();
    }

    /**
     * 显示等待的PopWindow
     */
    private void showWaittingPopWindow() {

        recLen = Constant.RECLEN_TIME;
        handler.sendEmptyMessage(CUT_DOWN_TIMMER);
        //增加PopWindow
        final LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_waitting_layout, null);
        tv_cout_timmer = (TextView) view.findViewById(R.id.tv_cout_timmer);
        LinearLayout ll_return_pre = (LinearLayout) view.findViewById(R.id.ll_return_pre);

        waittingPop = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        final WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = 0.6f;
        mActivity.getWindow().setAttributes(params);
        waittingPop.setBackgroundDrawable(new BitmapDrawable());
        waittingPop.setOutsideTouchable(false);
        waittingPop.setFocusable(true);
        waittingPop.showAtLocation(mActivity.findViewById(R.id.all_pospager), Gravity.CENTER_HORIZONTAL, 0, Utils.dip2px(mActivity, -100));//设置为取景框中间
        waittingPop.update();
        waittingPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1f;
                mActivity.getWindow().setAttributes(params);
            }
        });
        ll_return_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClickCancel = true;
                dismissWaittingPopWindow();
                showSeeResultPopWindow(MAY_PAY_SUCCESS);
            }
        });
    }

    /**
     * 支付返回error
     */
    private void dealpayError() {
        if(!isClickCancel&&!isTimeGone&&recLen<Constant.RECLEN_TIME) {
            if (1 < recLen) {
                handler.sendEmptyMessage(QUERY_ORDER_STATE);
            } else {
                dismissWaittingPopWindow();
                showSeeResultPopWindow(NO_PAY_RESULT);
            }
        }
    }

    private void queryOrderState() {
        LogUtils.e(TAG, "轮询：recLen:" + recLen +",trade_num:" + trade_num);
        if (!isClickCancel) {
            if (recLen < Constant.RECLEN_TIME&&1 < recLen) {
                netWorks.queryPayState("0", trade_num, callBack, PAY_FLAG_QERY_PAY_STATE);
            }
        }
    }

    private void dealpayRes(String response) {
        LogUtils.e(TAG, "dealpayRes()刷卡支付返回数据response=null：" + (response == null));
        LogUtils.e(TAG, "dealpayRes()刷卡支付返回数据：" + response);
        if (!isClickCancel && !isTimeGone) {
            dismissWaittingPopWindow();
            try {
                JSONObject payResponse = new JSONObject(response);
                int mCode = payResponse.getInt("code");
                if (30000 == mCode) {//支付失败
                    LogUtils.e(TAG, "支付失败mCode.equals(\"FAIL\")进入收款失败界面gfdsafasf");
                    failreason = payResponse.getString("code_des");
                    solveway = payResponse.getString("solution");
                    if(payResponse.getString("payment")!=null){
                        payment = payResponse.getString("payment");
                    }
                    dismissWaittingPopWindow();
                    toFailActivity();
                } else if (200 == mCode) {
                    //transaction_id = payResponse.getString("transaction_id");
                    LogUtils.e(TAG,"1="+(payResponse.getString("payment")!=null));
                    if(payResponse.getString("payment")!=null){
                        payment = payResponse.getString("payment");
                    }
                    LogUtils.e(TAG,"2="+(payResponse.getString("query_num")!=null));
                    if(payResponse.getString("query_num")!=null){
                        query_num = payResponse.getString("query_num");
                    }
                    LogUtils.e(TAG,"3="+(payResponse.getString("freemoney")!=null));
                    if(payResponse.getString("freemoney")!=null){
                        freemoney = payResponse.getString("freemoney");
                    }
                    LogUtils.e(TAG, "支付成功mCode.equals(\"SUCCESS\")");
                    toSuccessActivity();
                    delayClearMoney();
                }
            } catch (JSONException e) {
                LogUtils.e(TAG, e.toString());
                e.printStackTrace();
            }
        }
    }

    private void dealQueryRes(String response) {
        LogUtils.e(TAG, "轮询：response" + response);
        if (recLen < Constant.RECLEN_TIME&&1 < recLen&&!isClickCancel) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int code = jsonObject.getInt("code");
                if (code == 200) {//弹出成功页面
                    ResultQueryBean resultQueryBean = new Gson().fromJson(response, ResultQueryBean.class);
                    if ("0".equals(resultQueryBean.getMsg().getSuccess())) {//等待支付
                        handler.sendEmptyMessageDelayed(QUERY_ORDER_STATE, 2000);
                    } else if ("1".equals(resultQueryBean.getMsg().getSuccess())) {//确定成功
                        transaction_id = resultQueryBean.getMsg().getTransaction_id();
                        payment = String.valueOf(resultQueryBean.getMsg().getPayment());
                        dismissWaittingPopWindow();
                        toSuccessActivity();
                    } else if ("2".equals(resultQueryBean.getMsg().getSuccess())) {//确认失败
                        //弹出失败页面
                        dismissWaittingPopWindow();
                        toFailActivity();
                    }
                } else {
                    //轮训查直到到成功为止
                    handler.sendEmptyMessageDelayed(QUERY_ORDER_STATE, 2000);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void toSuccessActivity() {
        Intent intent;
        handler.removeCallbacksAndMessages(null);
        intent = new Intent(mActivity, CollectionSuccessActivity.class);
        intent.putExtra("tradenum", trade_num);
        intent.putExtra("payment", payment);
        intent.putExtra("total_fee", total_fee);
        intent.putExtra("freemoney", freemoney);
        intent.putExtra("tradetime", trade_time);
        intent.putExtra("querynum", query_num);
        intent.putExtra("transaction_id", transaction_id);
        if(memberBean!=null){
            intent.putExtra("vip_discount", memberBean.getMsg().getVip_discount());
        }
        mActivity.startActivity(intent);
    }

    private void toFailActivity() {
        Intent intent;
        removeHandlerMessages();
        intent = new Intent(mActivity, CollectionFailActivity.class);
        intent.putExtra("tradenum", trade_num);
        intent.putExtra("failreason", failreason);
        intent.putExtra("solveway", solveway);
        intent.putExtra("totalfee", total_fee);
        intent.putExtra("payment", payment);
        intent.putExtra("tradetime", trade_time);
        intent.putExtra("querynum", query_num);
        intent.putExtra("vip_card", memberBean.getMsg().getVipcardid());
        intent.putExtra("vipcreate_id", memberBean.getMsg().getVipcreate_id());
        LogUtils.e(TAG, "传递到四个页面的vipcreate_id" + memberBean.getMsg().getVipcreate_id());
        intent.putExtra(Constant.UNPAY_ORDER_ID, "0");
        if(memberBean!=null){
            intent.putExtra("vip_discount", memberBean.getMsg().getVip_discount());
        }
        mActivity.startActivity(intent);
        delayClearMoney();
    }

    private PayParams getPayParams() {
        PayParams payParams = new PayParams();
        payParams.setType(Constant.UNKNOWN_COMMODITY);
        payParams.setPayWay(Constant.payway);
        payParams.setOrderNo(trade_num);
        payParams.setLast_order_no(Constant.LAST_ORDER_NO);
        payParams.setAuthCode("0");
        payParams.setCardId("0");
        payParams.setOrderId("0");

        payParams.setTotal_fee(total_fee);
        if(memberBean!=null){
            payParams.setVipId(memberBean.getMsg().getVipcardid());
            payParams.setVipcreate_id(memberBean.getMsg().getVipcreate_id());
            payParams.setPayMent(String.valueOf(memberBean.getMsg().getPayment()));
            payParams.setVip_discount(String.valueOf(memberBean.getMsg().getVip_discount()));
        }else{
            payParams.setVipId("0");
            payParams.setVipcreate_id("0");
            payParams.setPayMent(total_fee);
        }
        return payParams;
    }

    private void dealQueryError() {
        if (recLen < Constant.RECLEN_TIME&&1 < recLen) {
            handler.sendEmptyMessageDelayed(QUERY_ORDER_STATE, 2000);
        }
    }

    private void cutDownTimer() {
        recLen--;
        tv_cout_timmer.setText("" + recLen);
        if (recLen < Constant.RECLEN_TIME&&0 < recLen) {
            Message message = handler.obtainMessage(CUT_DOWN_TIMMER);
            handler.sendMessageDelayed(message, 1000);      // send message
        } else {
            isTimeGone = true;
            dismissWaittingPopWindow();
            showSeeResultPopWindow(NO_PAY_RESULT);
        }
    }

    private void showSeeResultPopWindow(int tag) {
        final LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.unknow_pay_result, null);
        TextView tv_return = (TextView) view.findViewById(R.id.tv_return);
        TextView tv_see_orders = (TextView) view.findViewById(R.id.tv_see_orders);
        TextView tv_title_pay_result = (TextView) view.findViewById(R.id.tv_title_pay_result);
        switch (tag){
            case NO_PAY_RESULT:
                tv_title_pay_result.setText(R.string.no_pay_result);
                break;
            case MAY_PAY_SUCCESS:
                tv_title_pay_result.setText(R.string.may_pay_success);
                break;
        }
        final PopupWindow resultPop = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = 0.7f;
        mActivity.getWindow().setAttributes(params);
        resultPop.setOutsideTouchable(false);
        resultPop.setFocusable(true);
        resultPop.showAtLocation(mActivity.findViewById(R.id.all_pospager), Gravity.CENTER_HORIZONTAL, 0, Utils.dip2px(mActivity, -100));//设置为取景框中间

        tv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultPop.dismiss();
            }
        });
        tv_see_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(new Intent(mActivity, PosOrderActivity.class));
                resultPop.dismiss();
            }
        });
    }

    private void dismissWaittingPopWindow() {
        if (waittingPop != null) {
            handler.removeMessages(CUT_DOWN_TIMMER);
            waittingPop.dismiss();
            WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
            params.alpha = 1f;
            mActivity.getWindow().setAttributes(params);
        }
    }

    /**
     * 点击某一支付方式：
     */
    private void toPay() {
        Constant.LAST_ORDER_NO = "";
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        switch (Constant.payway) {
            case "0"://微信
                if(Constant.OPEN_WXPAY == 1){
                    if (!judgeNet()) {
                        return;
                    }
                    if(Constant.OPEN_WXPAY_MICRO==3){
                        toPayQRCodeActivity();
                    }else{
                        goToCaptureActivity();
                    }
                }else{
                    //没开通微信：点击标记为微信记账
                    Utils.judgeIsFirstRemarkPay(mActivity, new Utils.OnSureToNext() {
                        @Override
                        public void onSure() {
                            Constant.payway = "8";
                            cashCardPay();
                        }
                    });
                }
                break;
            case "1"://支付宝
                if(Constant.OPEN_ALIPAY == 1){
                    if (!judgeNet()) {
                        return;
                    }
                    goToCaptureActivity();
                }else{
                    //没开通支付宝：点击标记为支付宝记账
                    Utils.judgeIsFirstRemarkPay(mActivity, new Utils.OnSureToNext() {
                        @Override
                        public void onSure() {
                            Constant.payway = "9";
                            cashCardPay();
                        }
                    });
                }
                break;
            case "6"://刷卡 2->6
            case "3"://现金
                Utils.judgeIsFirstRemarkPay(mActivity, new Utils.OnSureToNext() {
                    @Override
                    public void onSure() {
                        cashCardPay();
                    }
                });
                break;
            case "4"://会员卡
                LogUtils.e(TAG,"直接收款 会员卡余额支付...");
                trade_time = Utils.getTradeTime();//秒
                trade_num = Utils.getTradeNum();
                netWorks.PayCard(getPayParams(), callBack, PAY_MEMBER_BALANCE);
                break;
        }
    }

    /**
     * 现金或者刷卡支付
     */
    private void cashCardPay() {
        LogUtils.e(TAG,"cashCardPay()...");
        trade_num = Utils.getTradeNum();
        trade_time = Utils.getTradeTime();//秒
        SavedFailOrder cardOrder;
        if(memberBean!=null){
            cardOrder = Utils.getSaveOrder("0", trade_num, "0",total_fee, query_num, trade_time, "1", "0", Utils.keepTwoDecimal(payment),
                    memberBean.getMsg().getVipcardid(), "0", "0",memberBean.getMsg().getVipcreate_id(),String.valueOf(memberBean.getMsg().getVip_discount()),cardinfo_pay);//type="1" 代表直接收银
        }else{
            cardOrder = Utils.getSaveOrder("0", trade_num, "0",total_fee, query_num, trade_time, "1", "0", Utils.keepTwoDecimal(payment), "0", "0", "0","0","0",cardinfo_pay);//type="1" 代表直接收银
        }
        LogUtils.e(TAG,"cardOrder.getCardinfo():"+cardOrder.getCardinfo());
        CashCardPayUtils.getInstance().cashCardPay(mActivity, cardOrder, handler, null);
    }
    /**
     * 到扫码页面:微信支付宝扫码支付去扫码页面
     */
    private void goToCaptureActivity() {
        Intent intent = new Intent(mActivity, CaptureActivity.class);
        intent.putExtra("total_fee", total_fee);
        intent.putExtra("payment", payment);
        intent.putExtra("cardinfo_pay",cardinfo_pay);
        //加上会员卡号：
        if(memberBean!=null){
            intent.putExtra("member_bean", memberBean);
            intent.putExtra("vip_card", memberBean.getMsg().getVipcardid());
            intent.putExtra("vipcreate_id", memberBean.getMsg().getVipcreate_id());
            intent.putExtra("vip_discount", String.valueOf(memberBean.getMsg().getVip_discount()));
        }
        mActivity.startActivity(intent);
        delayClearMoney();
    }

    /**
     * 扫描结束后的回调
     * @param memberBean
     */
    @Subscribe
    public void onEventMainThread(PosMemberBean memberBean) {
        LogUtils.e(TAG, "会员识别到信息返回到PosPager页面");
        if (memberBean != null) {
            this.memberBean = memberBean;
            LogUtils.e(TAG, "显示支付pop money="+(new Gson().toJson(memberBean)));
            showPayPopWindowForCardCoupons(1);
        } else {
            Toast.makeText(mActivity, "未检索到该会员信息！", Toast.LENGTH_SHORT).show();
        }
        //FIXME：这个可以保证只调用一次。
        EventBus.getDefault().cancelEventDelivery(memberBean);
    }

    /**
     * 扫描结束后的回调
     * @param discountVoucherBean
     */
    @Subscribe
    public void onEventMainThread(PosDiscountVoucherBean discountVoucherBean) {
        if (discountVoucherBean != null) {
            this.discountVoucherBean = discountVoucherBean;
            showPayPopWindowForCardCoupons(2);
        } else {
            Toast.makeText(mActivity, "未检索到该折扣券信息！", Toast.LENGTH_SHORT).show();
        }
        //FIXME：这个可以保证只调用一次。
        EventBus.getDefault().cancelEventDelivery(discountVoucherBean);
    }

    /**
     * 扫描结束后的回调
     * @param cashVoucherBean
     */
    @Subscribe
    public void onEventMainThread(PosCashVoucherBean cashVoucherBean) {
        LogUtils.e(TAG, "会员识别到抵扣信息返回到PosPager页面");
        if (cashVoucherBean != null) {
            this.cashVoucherBean = cashVoucherBean;
            showPayPopWindowForCardCoupons(3);
        } else {
            Toast.makeText(mActivity, "未检索到该抵扣券信息！", Toast.LENGTH_SHORT).show();
        }
        //FIXME：这个可以保证只调用一次。
        EventBus.getDefault().cancelEventDelivery(cashVoucherBean);
    }

    /**
     * 判断所有的支付条件
     * @return
     */
    private boolean judgeNet() {
        if (!NetworkUtils.isNetworkAvailable(mActivity)) {
            Utils.showToast(mActivity, mActivity.getResources().getString(R.string.no_network_please_cash_card));
            return false;
        } else {
            return true;
        }
    }

    private boolean judgeAmount(String moneyStr) {
        Double money = Double.valueOf(moneyStr);
        if (money > MAX_MONEY) {
            Utils.showToast(mActivity, mActivity.getResources().getString(R.string.pay_money_max));
            return false;
        } else if (money < MIN_MONEY) {
            Utils.showToast(mActivity, mActivity.getResources().getString(R.string.pay_money_min));
            return false;
        } else {
            return true;
        }
    }
    /**
     * 请求支付宝或者微信是否开通支付
     */
    private void requireForWXZFB(final int tag) {
        LogUtils.e(TAG, "请求支付宝(1)或者微信(0)是否开通支付：" + tag);
        LogUtils.e(TAG, "Constant.ADV_ID:" + Constant.ADV_ID);

        String currentTime = Utils.getCurrentTime();
        String token = Utils.getToken(currentTime, mActivity);
        OkHttpUtils.post()
                .url(UrlConstance.QUERY_PAY_STATUS)
                .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                .addParams(Constant.PARAMS_NEME_PASSDORD_MD5, MD5.md5(MD5.md5(Constant.PASSWORD)))//密码
                .addParams("adv_id", Constant.ADV_ID)
                .build()
                .writeTimeOut(5000)
                .readTimeOut(5000)
                .connTimeOut(5000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        LogUtils.Log("请求支付宝(1)或者微信(0)是否开通支付：0 e=="+e.toString());
                        isLimitzfbwx = false;
                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e(TAG, "response==" + response);
                        isLimitzfbwx = false;
                        boolean success = Utils.checkSaveToken(mActivity, response);
                        if (success) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int code = jsonObject.getInt("code");
                                if (code == 200) {//返回成功
                                    int certifite = jsonObject.getJSONObject("msg").getInt("certifite");
                                    int alipay = jsonObject.getJSONObject("msg").getInt("alipay");
                                    int micro = jsonObject.getJSONObject("msg").getInt("micro");
                                    Constant.OPEN_ALIPAY = alipay;
                                    Constant.OPEN_WXPAY = certifite;
                                    Constant.OPEN_WXPAY_MICRO = micro;
                                    if (tag == 1 && Constant.OPEN_ALIPAY == 1) {//支付宝
                                        goToCaptureActivity();
                                    } else if (tag == 0 && Constant.OPEN_WXPAY == 1) {//微信
                                        if(Constant.OPEN_WXPAY_MICRO==3){
                                            toPayQRCodeActivity();
                                        }else{
                                            goToCaptureActivity();
                                        }
                                    } else {
                                        showNoBandPay(tag);
                                    }
                                } else {
                                    showNoBandPay(tag);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    /**
     * 展示没有绑定提示的Dialog
     * @param tag
     */
    private void showNoBandPay(int tag) {
        String dialogMessage = "";
        if (tag == 1) {//支付宝
            dialogMessage = mActivity.getResources().getString(R.string.click_zfb_no_band);
        } else if (tag == 0) {//微信
            dialogMessage = mActivity.getResources().getString(R.string.click_wx_no_band);
        }
        AlertDialog errTokenDia;
        errTokenDia = new AlertDialog.Builder(mActivity)
                .setMessage(dialogMessage)
                .setPositiveButton(mActivity.getResources().getString(R.string.determine), null)
                .show();
        errTokenDia.setCancelable(false);
        errTokenDia.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
    }

    /**
     * 清除已经输入的金额
     */
    private void clearMoney() {
        mEtPrint.setText("0.00");
        clearInputScreen();
    }

    /**
     * 延迟清除金额
     */
    private void delayClearMoney() {
        handler.sendEmptyMessageDelayed(CLEAR_ALL_MONEY, 1000);
    }

    /**
     * 移除所有handler消息
     */
    private void removeHandlerMessages() {
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start_pay://展示收款popuwindow
                //LogUtils.e(TAG,"测试 px2dip："+Utils.px2dip(mActivity,18));
                if (ClicKUtils.isFastDoubleClick()) {
                    return;
                }
                //判断输入金额
                if (judgeAmount(mEtPrint.getText().toString())) {
                    showPayPopWindowForCardCoupons(0);
                }
                break;
            case R.id.bt_delete_all:
                clearInputScreen();
                mEtPrint.setText(mActivity.getResources().getString(R.string.set_two_decimal_places_zero));
                resetBean();
                break;
            case R.id.iv_delet:
                if (mCurrent.getText().toString().trim().length() > 2) {
                    back();
                    computation();
                } else {
                    mCurrent.setText(mActivity.getResources().getString(R.string.zero));
                    clearInputScreen();
                }
                break;
            case R.id.btn_Spot:
                inputPoint(view);
                break;
            case R.id.btn_Add:
            case R.id.btn_subtraction:
            case R.id.btn_multip:
            case R.id.btn_Div:
                inputOperatior(view);
                break;
            case R.id.btn0:
            case R.id.btn1:
            case R.id.btn2:
            case R.id.btn3:
            case R.id.btn4:
            case R.id.btn5:
            case R.id.btn6:
            case R.id.btn7:
            case R.id.btn8:
            case R.id.btn9:
                inputNumber(view);
                computation();
                break;
            default:
                inputNumber(view);
                break;
        }
    }

    private void setAlpha(float alpha) {
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = alpha;
        mActivity.getWindow().setAttributes(params);
    }

    //算式判断
    private void computation() {
        String s = mCurrent.getText().toString().trim();
        LogUtils.e(TAG, "computation()打印" + s);
        if (s.indexOf("+") >= 0 || s.indexOf("-") >= 0 || s.indexOf("×") >= 0 || s.indexOf("÷") >= 0) {
            String subStr = s.substring(s.length() - 1, s.length());
            if (subStr.equals("+") || subStr.equals("-") || subStr.equals("×") || subStr.equals("÷") || subStr.equals(".")) {
                s = s.substring(0, s.length() - 1);
            }
            s = Compute.count(s);//合法的算式

            //计算结果保留两位小数
            String twoDecimal = Utils.keepTwoDecimal(s);
            LogUtils.e(TAG, "打印结果？：" + twoDecimal);
            if ("meaningless".equals(twoDecimal)) {
                mEtPrint.setText("0.00");
                clearInputScreen();
                return;
            }
            if (MAX_MONEY < Double.valueOf(twoDecimal)) {
                //Toast.makeText(mActivity, mActivity.getResources().getString(R.string.pay_money_max), Toast.LENGTH_SHORT).show();
                Utils.showToast(mActivity,mActivity.getResources().getString(R.string.pay_money_max));
            } else if (MIN_ENTER_MONEY > Double.valueOf(twoDecimal)) {
                //Toast.makeText(mActivity, mActivity.getResources().getString(R.string.enter_money_min), Toast.LENGTH_SHORT).show();
                Utils.showToast(mActivity, mActivity.getResources().getString(R.string.enter_money_min));
            } else {
                mEtPrint.setText(twoDecimal);
            }
        } else {
            String enterNum = mCurrent.getText().toString().trim();
            if (enterNum != null) {
                if (!(enterNum.contains("+") || enterNum.contains("-") || enterNum.contains("×") || enterNum.contains("÷"))) {
                    String s1 = Utils.keepTwoDecimal(enterNum);
                    if (MAX_MONEY < Double.valueOf(s1)) {
                        //Toast.makeText(mActivity, mActivity.getResources().getString(R.string.pay_money_max), Toast.LENGTH_SHORT).show();
                        Utils.showToast(mActivity, mActivity.getResources().getString(R.string.pay_money_max));
                    } else if (MIN_ENTER_MONEY > Double.valueOf(s1)) {
                        //Toast.makeText(mActivity, mActivity.getResources().getString(R.string.enter_money_min), Toast.LENGTH_SHORT).show();
                        Utils.showToast(mActivity, mActivity.getResources().getString(R.string.enter_money_min));
                    } else {
                        mEtPrint.setText(s1);
                    }
                }
            }
        }
    }

    //输入点
    private void inputPoint(View view) {
        if (mLastInputstatus == INPUT_POINT) {
            return;
        }
        if (mLastInputstatus == END || mLastInputstatus == ERROR) {
            clearInputScreen();
        }
        String key = map.get(view);
        String input = mCurrent.getText().toString();
        //上一次输入状态为运算符
        if (mLastInputstatus == INPUT_OPERATOR) {
            input = input + mActivity.getResources().getString(R.string.zero);
        }
        mCurrent.setText(input + key);
        addInputList(INPUT_POINT, key);
    }

    //输入数字
    private void inputNumber(View view) {
        if (mLastInputstatus == END || mLastInputstatus == ERROR) {
            clearInputScreen();
        }
        String key = map.get(view);
        if (mActivity.getResources().getString(R.string.zero).equals(mCurrent.getText().toString())) {//第一次输入数字的时候
            mCurrent.setText(key);
        } else {
            mCurrent.setText(mCurrent.getText() + key);
        }
        addInputList(INPUT_NUMBER, key);
    }

    //输入运算符
    private void inputOperatior(View view) {
        if (mLastInputstatus == INPUT_OPERATOR || mLastInputstatus == ERROR) {
            return;
        }
        if (mLastInputstatus == END) {
            mLastInputstatus = INPUT_NUMBER;
        }

        String key = map.get(view);
        if (mActivity.getResources().getString(R.string.zero).equals(mCurrent.getText().toString())) {    //&& key.equals("-")
            mCurrent.setText(mActivity.getResources().getString(R.string.zero));
            mEtPrint.setText(mActivity.getResources().getString(R.string.set_two_decimal_places_zero));
            mInputList.set(0, new InputItem(mActivity.getResources().getString(R.string.zero), InputItem.InputType.INT_TYPE));
        } else {
            mCurrent.setText(mCurrent.getText() + key);
        }
        addInputList(INPUT_OPERATOR, key);
    }

    //回退操作
    private void back() {
        if (mLastInputstatus == ERROR) {
            clearInputScreen();
        }
        String str = mCurrent.getText().toString();
        if (str.length() >= 1) {
            mCurrent.setText(str.substring(0, str.length() - 1));
            //mEtPrint.setText(str.substring(0,str.length()-1));
            backList();
        } else {
            mCurrent.setText("");
            clearScreen(new InputItem("", InputItem.InputType.INT_TYPE));
        }
    }

    //回退InputList操作
    private void backList() {
        InputItem item = mInputList.get(mInputList.size() - 1);
        if (item.getType() == InputItem.InputType.INT_TYPE && item.getInput().length() >= 1) {
            //获取到最后一个item,并去掉最后一个字符
            String input = item.getInput().substring(0,
                    item.getInput().length() - 1);
            //如果截完了，则移除这个item，并将当前状态改为运算操作符
            if ("".equals(input)) {
                mInputList.remove(item);
                mLastInputstatus = INPUT_OPERATOR;
            } else {
                //否则设置item为截取完的字符串，并将当前状态改为number
                item.setInput(input);
                mLastInputstatus = INPUT_NUMBER;
            }
            //如果item是运算操作符 则移除。
        } else if (item.getType() == InputItem.InputType.OPERATOR_TYPE) {
            mInputList.remove(item);
            if (mInputList.get(mInputList.size() - 1).getType() == InputItem.InputType.INT_TYPE) {
                mLastInputstatus = INPUT_NUMBER;
            } else {
                mLastInputstatus = INPUT_POINT;
            }
            //如果当前item是小数
        } else {
            if (item.getInput().length() >= 1) {
                String input = item.getInput().substring(0, item.getInput().length() - 1);
                if ("".equals(input)) {
                    mInputList.remove(item);
                    mLastInputstatus = INPUT_OPERATOR;
                } else {
                    if (input.contains(".")) {
                        item.setInput(input);

                        mLastInputstatus = INPUT_POINT;
                    } else {
                        item.setInput(input);
                        mLastInputstatus = INPUT_NUMBER;
                    }
                }
            }
        }
    }

    //清零
    private void clearInputScreen() {
        mCurrent.setText(mActivity.getResources().getString(R.string.zero));
        mLastInputstatus = INPUT_NUMBER;
        mInputList.clear();
        mInputList.add(new InputItem("", InputItem.InputType.INT_TYPE));
    }

    //清屏
    private void clearScreen(InputItem item) {
        if (mLastInputstatus != ERROR) {
            mLastInputstatus = END;
        }
        mInputList.clear();
        mInputList.add(item);
    }

    //输入方法
    private void addInputList(int inputOperator, String key) {
        switch (inputOperator) {
            case INPUT_NUMBER://如果输入数字
                if (mLastInputstatus == INPUT_NUMBER) {//上一次输入状态等于数字
                    InputItem item = mInputList.get(mInputList.size() - 1);
                    item.setInput(item.getInput() + key);
                    item.setType(InputItem.InputType.INT_TYPE);
                    mLastInputstatus = INPUT_NUMBER;
                } else if (mLastInputstatus == INPUT_OPERATOR) {//上一次输入状态等于运算符
                    InputItem item = new InputItem(key, InputItem.InputType.INT_TYPE);
                    mInputList.add(item);
                    mLastInputstatus = INPUT_NUMBER;
                } else if (mLastInputstatus == INPUT_POINT) {//上一次输入状态是小数点
                    InputItem item = mInputList.get(mInputList.size() - 1);
                    item.setInput(item.getInput() + key);
                    item.setType(InputItem.InputType.DOUBLE_TYPE);
                    mLastInputstatus = INPUT_POINT;
                }
                break;
            case INPUT_OPERATOR://如果输入运算符
                InputItem item = new InputItem(key, InputItem.InputType.OPERATOR_TYPE);
                mInputList.add(item);
                mLastInputstatus = INPUT_OPERATOR;
                break;
            case INPUT_POINT://如果输入是小数点
                if (mLastInputstatus == INPUT_OPERATOR) {//上一次输入状态是运算符
                    InputItem item1 = new InputItem(mActivity.getResources().getString(R.string.zero) + key, InputItem.InputType.DOUBLE_TYPE);
                    mInputList.add(item1);
                    mLastInputstatus = INPUT_POINT;
                } else {
                    InputItem item1 = mInputList.get(mInputList.size() - 1);
                    item1.setInput(item1.getInput() + key);
                    item1.setType(InputItem.InputType.DOUBLE_TYPE);
                    mLastInputstatus = INPUT_POINT;
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        removeHandlerMessages();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    public void setResutl(int requestCode, int resultCode, Intent data) {
        LogUtils.Log("fragment中的result");
        switch (resultCode) {
            case Activity.RESULT_CANCELED:
                String reason = data.getStringExtra("reason");
                LogUtils.Log("reason==" + reason);
                if (reason != null) {
                    Toast.makeText(mActivity, reason, Toast.LENGTH_SHORT).show();
                }
                break;
            case Activity.RESULT_OK:
                Toast.makeText(mActivity, "收款成功", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 输入文本监听
     */
    class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mActivity.getResources().getString(R.string.zero).equals(mCurrent.getText().toString().trim())) {
                mEtPrint.setText(mActivity.getResources().getString(R.string.set_two_decimal_places_zero));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}
