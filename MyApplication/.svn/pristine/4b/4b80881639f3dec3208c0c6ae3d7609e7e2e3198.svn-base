package com.younle.younle624.myapplication.activity.pos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.manager.orderpager.pos.PayQRCodeActivity;
import com.younle.younle624.myapplication.activity.manager.orderpager.pos.PosOrderActivity;
import com.younle.younle624.myapplication.application.MyApplication;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.constant.UrlConstance;
import com.younle.younle624.myapplication.domain.ResultQueryBean;
import com.younle.younle624.myapplication.domain.SavedFailOrder;
import com.younle.younle624.myapplication.domain.UnPayDetailsBean;
import com.younle.younle624.myapplication.domain.orderbean.PosMemberBean;
import com.younle.younle624.myapplication.domain.paybean.DiscountInfo;
import com.younle.younle624.myapplication.domain.paybean.PayParams;
import com.younle.younle624.myapplication.myservice.FailOrderService;
import com.younle.younle624.myapplication.utils.CashCardPayUtils;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.NetWorks;
import com.younle.younle624.myapplication.utils.NetworkUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.utils.scanbar.ScanGunKeyEventHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.util.MD5;
import org.xutils.ex.DbException;

import okhttp3.Call;
import zxing.activity.CaptureActivity;

/**
 * Created by Administrator on 2016/6/17.
 */
public class CollectionFailActivity extends Activity {

    private Button btn_trade_again;
    private TextView btn_trade_canale;
    private TextView tv_trade_num;
    private TextView tv_pay_way;
    private TextView tv_fail_reason;
    private TextView tv_solve_way;
    private LinearLayout ll_return_pre;
    private String tradenum = "0";
    private String failreason = "0";
    private String solveway = "0";
    private String totalfee = "0.00";
    private String payment = "0.00";
    private String tradetime = "0";
    private String order_no_for_order = "0";
    private PopupWindow popupWindow;
    private String TAG = "CollectionFailActivity";
    private String vip_card;
    private String cardinfo_pay = "";
    private String vipcreate_id = "0";
    private String vip_discount = "0";
    private UnPayDetailsBean orderBean;
    private PosMemberBean memberBean;
    private DiscountInfo discountBean;
    private String order_id;
    private int fromWhere;
    private final int MEMBER_PAY = 3;
    private final int PAY_FLAG_QERY_PAY_STATE = 14;
    private static final int CUT_DOWN_TIMMER = 7;
    private static final int QUERY_ORDER_STATE = 10;
    private String trade_num;
    private String query_num = "0";
    private String trade_time;
    private TextView tv_cout_timmer;
    private int recLen = Constant.RECLEN_TIME;
    private PopupWindow waittingPop;
    private NetWorks netWorks;
    private boolean isZFB = false;//和P8000不同，没有刷卡所以只有两个状态值
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CUT_DOWN_TIMMER:
                    cutDownTimer();
                    break;
                case QUERY_ORDER_STATE:
                    queryOrderState();
                    break;
            }
        }
    };
    private NetWorks.OnNetCallBack netCallBack = new NetWorks.OnNetCallBack() {
        @Override
        public void onError(Exception e, int flag) {
            switch (flag) {
                case MEMBER_PAY://会员支付
                    dealpayError(e);
                    break;
                case PAY_FLAG_QERY_PAY_STATE://查询订单状态
                    dealQueryError(e);
                    break;
            }
        }

        @Override
        public void onResonse(String response, int flag) {
            switch (flag) {
                case MEMBER_PAY://会员支付
                    dealpayRes(response);
                    break;
                case PAY_FLAG_QERY_PAY_STATE://查询订单状态
                    dealQueryRes(response);
                    break;
            }
        }
    };
    private boolean isTimeGone;
    private String song_money="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_fail);
        netWorks = new NetWorks(this);
        Utils.initToolBarState(this);
        initView();
        initData();
        setListener();
    }

    //初始化视图
    private void initView() {
        btn_trade_again = (Button) findViewById(R.id.btn_trade_again);
        btn_trade_canale = (TextView) findViewById(R.id.btn_trade_canale);
        tv_trade_num = (TextView) findViewById(R.id.tv_trade_num);
        tv_pay_way = (TextView) findViewById(R.id.tv_pay_way);
        tv_fail_reason = (TextView) findViewById(R.id.tv_fail_reason);
        tv_solve_way = (TextView) findViewById(R.id.tv_solve_way);
        ll_return_pre = (LinearLayout) findViewById(R.id.ll_return_pre);
    }

    //初始化数据
    private void initData() {
        //获取intent传来的数据
        orderBean = (UnPayDetailsBean) getIntent().getSerializableExtra(Constant.ORDER_BEAN);
        memberBean = (PosMemberBean) getIntent().getSerializableExtra("member_bean");
        discountBean = (DiscountInfo) getIntent().getSerializableExtra(Constant.MEMBER_DISCOUNT_BEAN);
        fromWhere = getIntent().getIntExtra(Constant.FROME_WHERE, -1);
        if(Constant.ORDER_PAGER_DETAIL==fromWhere){
            Constant.IS_CLOSE_DETAIL_ACTIVITY=false;
        }
        String tradenumExtra = getIntent().getStringExtra("tradenum");
        String failreasonExtra = getIntent().getStringExtra("failreason");
        String solvewayExtra = getIntent().getStringExtra("solveway");
        String totalfeeExtra = getIntent().getStringExtra("totalfee");
        String paymentExtra = getIntent().getStringExtra("payment");
        String tradetimeExtra = getIntent().getStringExtra("tradetime");
        String songMoneyExtra = getIntent().getStringExtra("song_money");
        String vip_cardExtra = getIntent().getStringExtra("vip_card");
        String vipcreate_idExtra = getIntent().getStringExtra("vipcreate_id");
        String vip_discountExtra = getIntent().getStringExtra("vip_discount");
        String unpay_order_idExtra = getIntent().getStringExtra(Constant.UNPAY_ORDER_ID);
        String order_no_for_orderExtra = getIntent().getStringExtra("order_no_for_order");
        if(getIntent().getStringExtra("cardinfo_pay")!=null){
            cardinfo_pay = getIntent().getStringExtra("cardinfo_pay");
        }
        if(vip_cardExtra!=null) {
            vip_card = vip_cardExtra;
        }
        if(vipcreate_idExtra!=null) {
            vipcreate_id = vipcreate_idExtra;
        }
        if(vip_discountExtra!=null) {
            vip_discount = vip_discountExtra;
        }
        if(unpay_order_idExtra!=null) {
            order_id = unpay_order_idExtra;
        }
        if(songMoneyExtra!=null) {
            song_money=solvewayExtra;
        }
        if (tradenumExtra != null) {
            tradenum = tradenumExtra;
        }
        if (failreasonExtra != null) {
            failreason = failreasonExtra;
        }
        if (solvewayExtra != null) {
            solveway = solvewayExtra;
        }
        if (totalfeeExtra != null) {
            totalfee = totalfeeExtra;
        }
        if (paymentExtra != null) {
            payment = paymentExtra;
        }
        if (tradetimeExtra != null) {
            tradetime = tradetimeExtra;
        }
        if (order_no_for_orderExtra != null) {
            order_no_for_order = order_no_for_orderExtra;
        }
        showData();
    }

    //设置数据的显示
    private void showData() {
        tv_trade_num.setText(tradenum);
        tv_fail_reason.setText(failreason);
        tv_solve_way.setText(solveway);
        String way = "未知";
        switch (Constant.payway) {
            case "0":
                way = "微信付款";
                break;
            case "1":
                isZFB = true;
                way = "支付宝付款";
                break;
            case "2":
                way = "刷卡付款";
                break;
            case "3":
                way = "现金付款(记账)";
                break;
            case "4":
                isZFB = true;
                way = "会员卡付款";
                break;
        }
        tv_pay_way.setText(way);

        if ("".equals(Constant.LAST_ORDER_NO)) {
            Constant.LAST_ORDER_NO = tradenum;
        }
    }

    //设置监听
    private void setListener() {

        //重新交易
        btn_trade_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("2".equals(Constant.UNKNOWN_COMMODITY)) {
                    if (!Utils.isNetworkAvailable(CollectionFailActivity.this)) {
                        Toast.makeText(CollectionFailActivity.this, "网络异常，请检查网络后再试!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                //重新发起交易：弹出popwindow
                if (popupWindow != null) {
                    if (popupWindow.isShowing()) {
                        WindowManager.LayoutParams params = getWindow().getAttributes();
                        params.alpha = 1f;
                        CollectionFailActivity.this.getWindow().setAttributes(params);
                        popupWindow.dismiss();
                    } else {
                        showPopWindow();
                    }
                } else {
                    showPopWindow();
                }
            }
        });
        //取消
        btn_trade_canale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(CollectionFailActivity.this)
                        .setTitle("取消订单后，订单内容将被删除，您确定要取消订单么？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //撤销订单
                                cancelThisOrder();
                                Constant.IS_CLOSE_DETAIL_ACTIVITY = true;
                                finish();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        //返回
        ll_return_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 取消本次订单
     */
    private void cancelThisOrder() {
        LogUtils.Log("FaileActivity  last_order_id=="+Constant.LAST_ORDER_NO);
        LogUtils.Log("order_id==" + order_id);
        String orderid="";
        if(orderBean!=null) {
           orderid = orderBean.getMsg().getOrderid()+"";
        }
        String currentTime = Utils.getCurrentTime();
        String token = Utils.getToken(currentTime, this);
        OkHttpUtils.post()
                .url(UrlConstance.CANCEL_THIS_ORDER)
                .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                .addParams(Constant.PARAMS_NEME_PASSDORD_MD5, MD5.md5(MD5.md5(Constant.PASSWORD)))
                .addParams("order_no", Constant.LAST_ORDER_NO)
                .addParams("order_id",orderid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        storeToDatabase(tradenum, totalfee, "0", tradetime,
                                Utils.getBQueryNum(CollectionFailActivity.this), "1");
                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e(TAG, "response======" + response);
                        boolean b = Utils.checkSaveToken(CollectionFailActivity.this, response);
                        if (b) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int code = jsonObject.getInt("code");
                                if (code != 200) {
                                    //存储首次失败订单
                                    storeToDatabase(tradenum, totalfee, "0", tradetime, Utils.getBQueryNum(CollectionFailActivity.this), "1");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    /**
     * 存储到数据库
     *
     * @param total_fee
     * @param success
     * @param addtime
     * @param query_num
     * @param cancel
     */
    private void storeToDatabase(String tradenum, String total_fee, String success, String addtime, String query_num, String cancel) {
        MyApplication myAppinstance = MyApplication.getInstance();
        DbManager.DaoConfig daoConfig = myAppinstance.getDaoConfig();
        DbManager db = org.xutils.x.getDb(daoConfig);
        SavedFailOrder savedFailOrder = Utils.getSaveOrder(Constant.LAST_ORDER_NO, tradenum, "0",
                total_fee, query_num, addtime, success, cancel, payment, vip_card, "0", song_money, vipcreate_id, vip_discount,cardinfo_pay);//type="1" 代表直接收银
        try {
            db.save(savedFailOrder);
        } catch (DbException de) {
            de.printStackTrace();
        }
    }

    /**
     * 显示选择交易框
     */
    private void showPopWindow() {

        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_choose_pay_way, null);

        RelativeLayout member_pay_money = (RelativeLayout) view.findViewById(R.id.member_pay_money);
        //判断当前账号是否有升级高级账号,并且不是来自会员充值,并且只用下单识别会员后才有可能出现
        /*if(Constant.OPENED_PERMISSIONS.contains("2")&&fromWhere!=Constant.MEMBER_CHARGE_PAY&&vip_card!=null&& !TextUtils.isEmpty(vip_card)) {
            member_pay_money.setVisibility(View.VISIBLE);
        }else {
            member_pay_money.setVisibility(View.GONE);
        }*/
        TextView tv_micro_pay = (TextView) view.findViewById(R.id.tv_micro_pay);
        TextView tv_ali_pay = (TextView) view.findViewById(R.id.tv_ali_pay);
        if(Constant.OPEN_ALIPAY != 1){
            tv_ali_pay.setText("支付宝收款(记账)");
        }
        if(Constant.OPEN_WXPAY != 1){
            tv_micro_pay.setText("微信收款(记账)");
        }
        popupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.7f;
        this.getWindow().setAttributes(params);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(CollectionFailActivity.this.findViewById(R.id.pay_fail), Gravity.CENTER, 0, 0);
        view.findViewById(R.id.tv_weixin_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constant.OPEN_WXPAY == 1){
                    if (!judgeNet()) {
                        return;
                    }
                    if(Constant.OPEN_WXPAY_MICRO==3){
                        toPayQRCodeActivity();
                    }else{
                        popupWindow.dismiss();
                        LogUtils.e(TAG, "微信支付：传到扫码页面的totalfee=" + totalfee);
                        Constant.payway = "0";
                        toCaptureActivity();
                        finish();
                    }
                }else{
                    //没开通微信：点击标记为微信记账
                   /* if(Utils.judgeIsFirstRemarkPay(CollectionFailActivity.this)){
                        Constant.payway = "8";
                        cashCardPay();
                    }*/
                   Utils.judgeIsFirstRemarkPay(CollectionFailActivity.this, new Utils.OnSureToNext() {
                       @Override
                       public void onSure() {
                           popupWindow.dismiss();
                           Constant.payway = "8";
                           cashCardPay();
                       }
                   });
                }
            }
        });
        view.findViewById(R.id.tv_zfb_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constant.OPEN_ALIPAY == 1){
                    if (!judgeNet()) {
                        return;
                    }
                    popupWindow.dismiss();
                    Constant.payway = "1";
                    toCaptureActivity();
                    finish();
                }else{
                    //没开通支付宝：点击标记为支付宝记账
                    /*if(Utils.judgeIsFirstRemarkPay(CollectionFailActivity.this)){
                        popupWindow.dismiss();
                        Constant.payway = "9";
                        cashCardPay();
                    }*/
                    Utils.judgeIsFirstRemarkPay(CollectionFailActivity.this, new Utils.OnSureToNext() {
                        @Override
                        public void onSure() {
                            popupWindow.dismiss();
                            Constant.payway = "9";
                            cashCardPay();
                        }
                    });
                }
            }
        });
        view.findViewById(R.id.tv_cash_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.judgeIsFirstRemarkPay(CollectionFailActivity.this, new Utils.OnSureToNext() {
                    @Override
                    public void onSure() {
                        popupWindow.dismiss();
                        Constant.payway = "3";
                        cashCardPay();
                    }
                });
            }
        });
        view.findViewById(R.id.tv_card_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.judgeIsFirstRemarkPay(CollectionFailActivity.this, new Utils.OnSureToNext() {
                    @Override
                    public void onSure() {
                        popupWindow.dismiss();
                        Constant.payway = "6";
                        cashCardPay();
                    }
                });
            }
        });

        member_pay_money.setOnClickListener(new View.OnClickListener() {//会员卡余额支付
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                startMemberPay();

               /* //携带金额到刷卡页面 测试写死
                Constant.payway = "4";
                Intent intent = new Intent(CollectionFailActivity.this, CaptureActivity.class);
                intent.putExtra(Constant.MONEY, payment);
                intent.putExtra("total_fee", totalfee);
                intent.putExtra("vip_card", vip_card);
                intent.putExtra(Constant.UNPAY_ORDER_ID,order_id);
                intent.putExtra(Constant.ORDER_BEAN, orderBean);
                intent.putExtra(Constant.FROME_WHERE, Constant.MEME_PAY_DIC);
                startActivity(intent);
                finish();*/
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1f;
                CollectionFailActivity.this.getWindow().setAttributes(params);
            }
        });
    }

    /**
     * 会员余额支付
     */
    private void startMemberPay() {
        trade_num = Utils.getTradeNum();
        //query_num = Utils.getBQueryNum(this);
        trade_time = Utils.getTradeTime();//秒
        showWaittingPopWindow();
        netWorks.PayCard(getParams(), netCallBack, MEMBER_PAY);
    }

    /**
     * vipcreate_id 获取折扣信息
     *
     * @return
     */
    private PayParams getParams() {
        String tradeNum = Utils.getTradeNum();

        PayParams paramsBean = new PayParams();
        paramsBean.setType(Constant.UNKNOWN_COMMODITY);
        paramsBean.setPayWay(Constant.payway);
        paramsBean.setOrderNo(tradeNum);
        paramsBean.setOrderId(orderBean.getMsg().getOrderid() + "");
        paramsBean.setPayMent(payment);
        paramsBean.setTotal_fee(orderBean.getMsg().getTotal_fee() + "");
        //paramsBean.setVipId(memberBean.getMsg().getVipcreate_id());
        paramsBean.setVipId(vip_card);
        paramsBean.setVipcreate_id(vipcreate_id);
        paramsBean.setCardId("0");
        paramsBean.setLast_order_no(Constant.LAST_ORDER_NO);
        if(orderBean!=null){
            if(orderBean.getMsg()!=null){
                if(orderBean.getMsg().getRoom()!=null){
                    paramsBean.setRoom(new Gson().toJson(orderBean.getMsg().getRoom()));
                }
            }
        }
        return paramsBean;
    }

    /**
     * 判断所有的支付条件
     * @return
     */
    private boolean judgeNet() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            Utils.showToast(this, getResources().getString(R.string.no_network_please_cash_card));
            return false;
        } else {
            return true;
        }
    }

    /**
     * 查询订单状态
     */
    private void queryOrderState() {
        LogUtils.e(TAG, "轮询：recLen:" + recLen);
        LogUtils.e(TAG, "轮询：trade_num:" + trade_num);
        if (recLen < Constant.RECLEN_TIME&&1 < recLen) {
            netWorks.queryPayState("0",trade_num, netCallBack, PAY_FLAG_QERY_PAY_STATE);
        }
    }
    /**
     * 显示等待的PopWindow
     */
    private void showWaittingPopWindow() {
        recLen = Constant.RECLEN_TIME;
        //增加PopWindow
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_waitting_layout, null);
        view.setBackgroundColor(Color.rgb(135, 206, 235));
        tv_cout_timmer = (TextView) view.findViewById(R.id.tv_cout_timmer);

        Button btn_cancel_this_order = (Button) view.findViewById(R.id.btn_cancel_this_order);
        waittingPop = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.7f;
        getWindow().setAttributes(params);
        //waittingPop.setBackgroundDrawable(new BitmapDrawable());
        waittingPop.setOutsideTouchable(false);
        waittingPop.setFocusable(true);
        waittingPop.showAtLocation(findViewById(R.id.arl_this_order_detail), Gravity.CENTER_HORIZONTAL, 0, Utils.dip2px(this, -100));//设置为取景框中间

        //设置倒计时
        handler.sendEmptyMessage(CUT_DOWN_TIMMER);
        btn_cancel_this_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelThisOrder();
            }
        });
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
            showSeeResultPopWindow();
        }
    }
    private void dismissWaittingPopWindow() {
        if (waittingPop != null) {
            handler.removeMessages(CUT_DOWN_TIMMER);
            waittingPop.dismiss();
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.alpha = 1f;
            getWindow().setAttributes(params);
        }
    }
    private void toCaptureActivity() {
        Intent intent = new Intent(CollectionFailActivity.this, CaptureActivity.class);
        //intent.putExtra(Constant.MONEY, payment);
        intent.putExtra("payment", payment);
        intent.putExtra("total_fee", totalfee);
        intent.putExtra("vip_card", vip_card);
        intent.putExtra("vipcreate_id", vipcreate_id);
        intent.putExtra("vip_discount", vip_discount);
        intent.putExtra("give_money", song_money);
        intent.putExtra(Constant.UNPAY_ORDER_ID,order_id);
        intent.putExtra(Constant.ORDER_BEAN, orderBean);
        intent.putExtra(Constant.FROME_WHERE,fromWhere);
        intent.putExtra("cardinfo_pay",cardinfo_pay);
        startActivity(intent);
    }

    private void toSuccessActivity() {
        Intent intent;
        handler.removeCallbacksAndMessages(null);
        intent = new Intent(CollectionFailActivity.this, CollectionSuccessActivity.class);
        if ("".equals(Constant.LAST_ORDER_NO)) {
            intent.putExtra("tradenum", trade_num);
        } else {
            intent.putExtra("tradenum", Constant.LAST_ORDER_NO);
        }
        LogUtils.e(TAG, "payment=======" + payment);
        intent.putExtra("payment", payment);
        intent.putExtra("total_fee", totalfee);
        intent.putExtra("tradetime", trade_time);
        intent.putExtra("querynum", query_num);
        intent.putExtra("vip_discount", vip_discount);
        intent.putExtra(Constant.ORDER_BEAN, orderBean);
        intent.putExtra(Constant.MEMBER_DISCOUNT_BEAN, discountBean);
        intent.putExtra("cardinfo_pay",cardinfo_pay);
        startActivity(intent);
        finish();
    }

    private void showSeeResultPopWindow() {
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.unknow_pay_result, null);
        TextView tv_return = (TextView) view.findViewById(R.id.tv_return);
        TextView tv_see_orders = (TextView) view.findViewById(R.id.tv_see_orders);

        waittingPop = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.7f;
        getWindow().setAttributes(params);
        waittingPop.setOutsideTouchable(false);
        waittingPop.setFocusable(true);
        waittingPop.showAtLocation(findViewById(R.id.capture_layout), Gravity.CENTER_HORIZONTAL, 0, Utils.dip2px(this, -100));//设置为取景框中间

        tv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_see_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CollectionFailActivity.this, PosOrderActivity.class));
                finish();
            }
        });
    }
    private void dealpayRes(String response) {
        LogUtils.e(TAG, "会员支付：" + response);
        if (!isTimeGone) {//未撤销并且，倒计时并未到时间
            dismissWaittingPopWindow();
            try {
                JSONObject payResponse = new JSONObject(response);
                int mCode = payResponse.getInt("code");
                if ("30000".equals(mCode)) {//支付失败
                    LogUtils.e(TAG, "支付失败mCode.equals(\"FAIL\")进入收款失败界面");
                    failreason = payResponse.getString("code_des");
                    solveway = payResponse.getString("solution");
                    initData();
                    return;
                } else if ("200".equals(mCode)) {
                    LogUtils.e(TAG, "支付成功mCode.equals(\"SUCCESS\")");
                    if(payResponse.getString("query_num")!=null){
                        query_num = payResponse.getString("query_num");
                    }
                    double money = payResponse.getDouble("payment");
                    payment = Utils.keepTwoDecimal(String.valueOf(money));
                    //payment = payResponse.getDouble("payment");
                    toSuccessActivity();
                }
                LogUtils.e(TAG,"dealpayRes----------");
            } catch (JSONException e) {
                LogUtils.e(TAG, e.toString());
                e.printStackTrace();
            }
        }
    }
    private void dealQueryRes(String response) {
        LogUtils.e(TAG, "轮询：response" + response);
        if (recLen > 1&&recLen<Constant.RECLEN_TIME) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int code = jsonObject.getInt("code");
                if (code == 200) {//弹出成功页面
                    ResultQueryBean resultQueryBean = new Gson().fromJson(response, ResultQueryBean.class);
                    if ("0".equals(resultQueryBean.getMsg().getSuccess())) {//等待支付
                        handler.sendEmptyMessageDelayed(QUERY_ORDER_STATE, 2000);
                    } else if ("1".equals(resultQueryBean.getMsg().getSuccess())) {//确定成功
                        dismissWaittingPopWindow();
                        toSuccessActivity();
                    } else if ("2".equals(resultQueryBean.getMsg().getSuccess())) {//确认失败
                        //弹出失败页面
                        dismissWaittingPopWindow();
                        initData();
                    }
                } else {
                    //轮训查直到到成功为止
                    handler.sendEmptyMessageDelayed(QUERY_ORDER_STATE, 2000);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }/* else {//2016 11.23加
            saveOrderToFail();
        }*/
    }
    /**
     * 支付异常
     * @param e
     */
    private void dealpayError(Exception e) {
        LogUtils.e(TAG, "dealpayError()-onError()===" + e + "----e.getMessage()===" + e.getMessage());
        if(!isTimeGone&&recLen<Constant.RECLEN_TIME){
            if (1 < recLen) {
                handler.sendEmptyMessage(QUERY_ORDER_STATE);
            } else {
                dismissWaittingPopWindow();
                showSeeResultPopWindow();
            }
        }
    }
    /**
     * 查询订单返回error
     * @param e
     */
    private void dealQueryError(Exception e) {
        //轮训查出来成功为止
        LogUtils.e(TAG, "轮询：Exception e=" + e);
        if (recLen < Constant.RECLEN_TIME&&1 < recLen) {
            handler.sendEmptyMessageDelayed(QUERY_ORDER_STATE, 2000);
        }
    }

    /**
     * 现金或者刷卡支付
     */
    private void cashCardPay() {

        String orderid="0";
        if(orderBean!=null) {
            orderid = orderBean.getMsg().getOrderid()+"";
        }

        LogUtils.e(TAG, "orderid---" + orderid);
        LogUtils.e(TAG, "tradetime---" + tradetime);
        LogUtils.e(TAG, "totalfee-----------" + totalfee);
        LogUtils.e(TAG, "payment------------" + payment);

        SavedFailOrder cardOrder = Utils.getSaveOrder("0", tradenum, orderid, totalfee, query_num,
                tradetime, "1", "0", payment, vip_card, "0", song_money,vipcreate_id,vip_discount,cardinfo_pay);//type="1" 代表直接收银
        CashCardPayUtils.getInstance().cashCardPay(this, cardOrder, null, orderBean);
        LogUtils.e(TAG, "left==" + Constant.currentLeft);
        Constant.IS_CLOSE_DETAIL_ACTIVITY=true;
        finish();
    }
    /**
     * 初始化服务
     */
    private void initService() {

        boolean serviceWork = Utils.isServiceWork(this, Constant.SERVICE_UPDATE_FAIL_OR_CASHCARD);
        LogUtils.e(TAG, "服务有没有在工作：" + serviceWork);
        if (!serviceWork) {
            //在此开启服务，进行间隔轮询失败订单
            LogUtils.e(TAG, "服务之前未打开，新开服务...");
            Intent intent = new Intent(this, FailOrderService.class);
            startService(intent);
        }
    }

    /**
     * 请求支付宝或者微信是否开通支付
     */
    private void requireForWXZFB(final int tag) {
        LogUtils.e(TAG, "请求支付宝(1)或者微信(0)是否开通支付：" + tag);
        LogUtils.e(TAG, "Constant.ADV_ID:" + Constant.ADV_ID);

        String currentTime = Utils.getCurrentTime();
        String token = Utils.getToken(currentTime, this);
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

                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e(TAG, "response==" + response);
                        boolean success = Utils.checkSaveToken(CollectionFailActivity.this, response);
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
                                        Constant.payway = getResources().getString(R.string.one);
                                        toCaptureActivity();
                                        finish();
                                    } else if (tag == 0 && Constant.OPEN_WXPAY == 1) {//微信
                                        Constant.payway = getResources().getString(R.string.zero);
                                        if (Constant.OPEN_WXPAY_MICRO == 3) {
                                            toPayQRCodeActivity();
                                        } else {
                                            toCaptureActivity();
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
     * 去被扫码页面
     */
    private void toPayQRCodeActivity() {
        Intent intent = new Intent(this, PayQRCodeActivity.class);
        intent.putExtra("total_fee", totalfee);
        intent.putExtra("is_zfb",isZFB);
        intent.putExtra(Constant.FROME_WHERE,fromWhere);
        intent.putExtra(Constant.UNPAY_ORDER_ID, order_id);
        intent.putExtra(Constant.ORDER_BEAN, orderBean);
        intent.putExtra("order_no_for_order",order_no_for_order);
        intent.putExtra("cardinfo_pay", cardinfo_pay);
        if(memberBean!=null){//加上会员卡号：
            intent.putExtra("vip_card", memberBean.getMsg().getVipcardid());
            intent.putExtra("vipcreate_id", memberBean.getMsg().getVipcreate_id());
            if(memberBean.getMsg().getVip_discount()>0){
                intent.putExtra("vip_discount", String.valueOf(memberBean.getMsg().getVip_discount()));
                intent.putExtra("payment", String.valueOf(memberBean.getMsg().getPayment()));
            }else{
                intent.putExtra("payment", payment);
            }
        }else{
            intent.putExtra("payment", payment);
        }

        LogUtils.e(TAG,"失败页面到小微页面");
        LogUtils.e(TAG,"total_fee="+totalfee+",payment="+payment);

        startActivity(intent);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }


        finish();
    }

    /**
     * 展示没有绑定提示的Dialog
     * @param tag
     */
    private void showNoBandPay(int tag) {
        String dialogMessage = "";
        if (tag == 1) {//支付宝
            dialogMessage = getResources().getString(R.string.click_zfb_no_band);
        } else if (tag == 0) {//微信
            dialogMessage = getResources().getString(R.string.click_wx_no_band);
        }
        AlertDialog errTokenDia;
        errTokenDia = new AlertDialog.Builder(this)
                .setMessage(dialogMessage)
                .setPositiveButton(getResources().getString(R.string.determine), null)
                .show();
        errTokenDia.setCancelable(false);
        errTokenDia.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
    }

    //back键屏蔽
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.e(TAG, "onDestroy()...");
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(ScanGunKeyEventHelper.isScanGunEvent(event)) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
