package com.younle.younle624.myapplication.activity.manager.orderpager.pos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.pos.CollectionSuccessActivity;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.UnPayDetailsBean;
import com.younle.younle624.myapplication.domain.paybean.DiscountInfo;
import com.younle.younle624.myapplication.domain.paybean.PayParams;
import com.younle.younle624.myapplication.utils.ClicKUtils;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.MicroMerchantCodeUtils;
import com.younle.younle624.myapplication.utils.NetWorks;
import com.younle.younle624.myapplication.utils.NetworkUtils;
import com.younle.younle624.myapplication.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class PayQRCodeActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "PayQRCodeActivity";
    private LinearLayout ll_cancel;
    private RelativeLayout rl_show_code;//显示被扫二维码的布局
    private RelativeLayout rl_page_out_time;//超时展示的页面
    private RelativeLayout rl_page_out_of_quota;//超时展示的页面
    private LinearLayout ll_please_pay_in_time;//倒计时布局
    private LinearLayout ll_refresh_loading;
    private TextView tv_pay_money;
    private TextView tv_count_down_timer;
    private Button btn_page_out_of_quota;
    private Button btn_refresh_pager;
    private ImageView iv_qrcode;
    private ImageView iv_progress;
    private ImageView iv_refresh_loading;
    private String payment = "0.00";
    private String total_fee = "0.00";
    private String vip_card = "0";
    private String vipcreate_id = "0";
    private String order_no = "0";
    private String order_no_for_order = "0";
    private String type = "1";
    private String transaction_id = "0";
    private String trade_time = "0";
    private String query_num = "0";
    private NetWorks netWorks;
    private String unpay_order_id ="0";
    private String give_money ="0";//充值赠送的金额
    private String vip_discount ="0";//打折
    private int fromWhere = -1;
    private String cardinfo_pay = "";

    private boolean isRegetCode = false;
    private boolean isDestory = false;
    private boolean isSuccess = true;//防止得到两次查询结果弹出两次成功页面的标识
    private boolean isZFB = false;//和P8000不同，没有刷卡所以只有两个状态值
    private String this_time_before_order_no = "0";
    private UnPayDetailsBean orderBean;
    /**
     * 会员优惠信息
     */
    private DiscountInfo discountBean;
    private int COUNT_TIME = 120;
    private static final int GET_QR_CODE = 0;
    private static final int QUERY_PAY_RESULT = 1;
    private static final int QUERY_PAY_RESULT_ALWAYS = 2;
    private static final int HANDLER_QUERY_ONE_MIN = 100;
    private static final int HANDLER_RADUCE_TIME = 200;
    private static final int HANDLER_QUERY_LOOPER = 300;
    private static final int HANDLER_QUERY_ALWAYS_LOOPER = 400;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HANDLER_QUERY_ONE_MIN://查询两分钟付款结果
                    //开始倒计时
                    COUNT_TIME = 120;
                    tv_count_down_timer.setText(COUNT_TIME+"秒");
                    ll_please_pay_in_time.setVisibility(View.VISIBLE);
                    handler.sendEmptyMessageDelayed(HANDLER_RADUCE_TIME,1000);
                    LogUtils.e(TAG,"handler 查两分钟付款结果开始...");
                    handler.sendEmptyMessageDelayed(HANDLER_QUERY_LOOPER, 3000);
                    break;
                case HANDLER_RADUCE_TIME://查询两分钟付款结果
                    //开始倒计时
                    if(COUNT_TIME>0){
                        COUNT_TIME--;
                        tv_count_down_timer.setText(COUNT_TIME+"秒");
                        handler.sendEmptyMessageDelayed(HANDLER_RADUCE_TIME,1000);
                    }else{
                        LogUtils.e(TAG,"查询两分钟付款结果结束：COUNT_TIME=0");
                        COUNT_TIME=0;
                        ll_please_pay_in_time.setVisibility(View.INVISIBLE);
                        switchCodeFreshPager(1);//超两分钟弹出超时页面
                    }
                    break;
                case HANDLER_QUERY_LOOPER://每隔三秒轮询一次
                    if(!isDestory){
                        queryPayResult();
                    }else{
                        handler.removeCallbacksAndMessages(null);
                    }
                    break;
                case HANDLER_QUERY_ALWAYS_LOOPER://每隔三秒轮询一次 一直查
                    if(!isDestory){
                        queryPayResultUntilSuccess();
                    }else{
                        handler.removeCallbacksAndMessages(null);
                    }
                    break;
            }
        }
    };
    private NetWorks.OnNetCallBack callBack = new NetWorks.OnNetCallBack() {
        @Override
        public void onError(Exception e, int flag) {
            switch (flag){
                case GET_QR_CODE://获取验证码 onError
                    LogUtils.e(TAG, "e.getMessage()===" + e.getMessage());
                    Utils.showToast(PayQRCodeActivity.this,"生成二维码失败请重试！(error)");
                    pbAnimationStop(iv_progress,true);
                    notRegetCode();
                    break;
                case QUERY_PAY_RESULT://查询付款结果 onError:此时分点击查询还是轮询查询
                    LogUtils.e(TAG, "callBack onError QUERY_PAY_RESULT轮询查询");
                    if(!isDestory){
                        handler.sendEmptyMessageDelayed(HANDLER_QUERY_LOOPER, 3000);
                    }else{
                        handler.removeCallbacksAndMessages(null);
                    }
                    break;
                case QUERY_PAY_RESULT_ALWAYS://一直查 查询付款结果 onError:此时分点击查询还是轮询查询
                    LogUtils.e(TAG, "callBack onError QUERY_PAY_RESULT轮询查询");
                    if(!isDestory){
                        handler.sendEmptyMessageDelayed(HANDLER_QUERY_ALWAYS_LOOPER, 3000);
                    }else{
                        handler.removeCallbacksAndMessages(null);
                    }
                    break;
            }
        }

        @Override
        public void onResonse(String response, int flag) {
            switch (flag){
                case GET_QR_CODE://获取二维码 onResonse
                    LogUtils.e(TAG, "获取二维码response=" + response);
                    pbAnimationStop(iv_progress,true);
                    notRegetCode();
                    try {
                        JSONObject payResponse = new JSONObject(response);
                        int mCode = payResponse.getInt("code");
                        if(200==mCode){
                            String code_url = payResponse.getString("code_url");
                            MicroMerchantCodeUtils.getCode(iv_qrcode, code_url);
                            switchCodeFreshPager(0);
                            ll_refresh_loading.setVisibility(View.GONE);//刷新中...隐藏
                            pbAnimationStop(iv_refresh_loading, false);
                            btn_refresh_pager.setVisibility(View.VISIBLE);
                            handler.sendEmptyMessage(HANDLER_QUERY_ONE_MIN);
                        }else if(10000==mCode){//在请求码的时候顾客已经支付，通过轮询查出支付结果
                            handler.sendEmptyMessage(HANDLER_QUERY_ALWAYS_LOOPER);
                        }else if(20000==mCode){//在请求码的时候顾客正在支付中，通过轮询查出支付结果
                            handler.sendEmptyMessage(HANDLER_QUERY_ALWAYS_LOOPER);
                        }else if(30000==mCode){//返回30000吐司服务器返回的信息
                            //刷新中...隐藏
                            ll_refresh_loading.setVisibility(View.GONE);
                            pbAnimationStop(iv_refresh_loading,false);
                            btn_refresh_pager.setVisibility(View.VISIBLE);
                            String code_des = payResponse.getString("code_des");
                            if(code_des.contains("限额")){
                                switchCodeFreshPager(3);
                            }else{
                                Utils.showToast(PayQRCodeActivity.this, "" + code_des);
                            }
                        }else{
                            ll_refresh_loading.setVisibility(View.GONE);//刷新中...隐藏
                            pbAnimationStop(iv_refresh_loading,false);
                            btn_refresh_pager.setVisibility(View.VISIBLE);
                            Utils.showToast(PayQRCodeActivity.this, "生成二维码失败，请重试！(code="+mCode+")");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case QUERY_PAY_RESULT://查询付款结果 onResonse
                    LogUtils.e(TAG, "查两分钟的轮询结果response=" + response);
                    pollingPayResult(response);
                    break;
                case QUERY_PAY_RESULT_ALWAYS://一直查 查询付款结果 onResonse
                    LogUtils.e(TAG, "一直查的轮询结果response=" + response);
                    pollingPayResultUntilSuccess(response);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_qrcode);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        ll_cancel = (LinearLayout) findViewById(R.id.ll_cancel);
        tv_pay_money = (TextView) findViewById(R.id.tv_pay_money);
        tv_count_down_timer = (TextView) findViewById(R.id.tv_count_down_timer);
        btn_refresh_pager = (Button) findViewById(R.id.btn_refresh_pager);
        iv_qrcode = (ImageView) findViewById(R.id.iv_qrcode);
        iv_progress = (ImageView) findViewById(R.id.iv_progress);

        rl_show_code = (RelativeLayout) findViewById(R.id.rl_show_code);
        rl_page_out_time = (RelativeLayout) findViewById(R.id.rl_page_out_time);
        ll_please_pay_in_time = (LinearLayout) findViewById(R.id.ll_please_pay_in_time);
        ll_refresh_loading = (LinearLayout) findViewById(R.id.ll_refresh_loading);
        iv_refresh_loading = (ImageView) findViewById(R.id.iv_refresh_loading);

        btn_page_out_of_quota = (Button) findViewById(R.id.btn_page_out_of_quota);
        rl_page_out_of_quota = (RelativeLayout) findViewById(R.id.rl_page_out_of_quota);
    }

    private void setListener() {
        ll_cancel.setOnClickListener(this);
        iv_qrcode.setOnClickListener(this);
        btn_refresh_pager.setOnClickListener(this);
        btn_page_out_of_quota.setOnClickListener(this);
    }

    private void initData() {
        getIntentData();
        pbAnimation(iv_progress,true);
        this_time_before_order_no = Constant.LAST_ORDER_NO;
        tv_pay_money.setText(payment);
        netWorks = new NetWorks(this);
        netWorks.PayCard(getPayParams(), callBack, GET_QR_CODE);
    }

    private void getIntentData() {
        Constant.payway = "0";
        Intent intent = getIntent();
        String paymentExtra = intent.getStringExtra("payment");
        String total_feeExtra = intent.getStringExtra("total_fee");
        String vip_cardExtra = intent.getStringExtra("vip_card");
        String vipcreate_idExtra = intent.getStringExtra("vipcreate_id");
        String unpay_order_idExtra = intent.getStringExtra(Constant.UNPAY_ORDER_ID);
        String give_moneyExtra = intent.getStringExtra("give_money");
        String vip_discountExtra = intent.getStringExtra("vip_discount");
        String order_no_for_orderExtra = intent.getStringExtra("order_no_for_order");
        String cardinfo_payExtra = intent.getStringExtra("cardinfo_pay");
        if(paymentExtra!=null){payment = Utils.keepTwoDecimal(paymentExtra);}
        if(total_feeExtra!=null){total_fee = Utils.keepTwoDecimal(total_feeExtra);}
        if(vip_cardExtra!=null){vip_card = vip_cardExtra;}
        if(vipcreate_idExtra!=null){vipcreate_id = vipcreate_idExtra;}
        if(unpay_order_idExtra!=null){unpay_order_id = unpay_order_idExtra;}
        if(give_moneyExtra!=null){give_money = give_moneyExtra;}
        if(vip_discountExtra!=null){vip_discount = vip_discountExtra;}
        if(order_no_for_orderExtra!=null){order_no_for_order = order_no_for_orderExtra;}
        if(cardinfo_payExtra!=null){cardinfo_pay = cardinfo_payExtra;}
        fromWhere = intent.getIntExtra(Constant.FROME_WHERE,-1);
        if(fromWhere==Constant.MEMBER_CHARGE_PAY){type = "2";}
        orderBean = (UnPayDetailsBean) intent.getSerializableExtra(Constant.ORDER_BEAN);
        discountBean = (DiscountInfo) intent.getSerializableExtra(Constant.MEMBER_DISCOUNT_BEAN);
        isZFB = intent.getBooleanExtra("is_zfb",false);
    }

    /**
     * 轮询查询成功返回
     * @param response
     */
    private void pollingPayResult(String response) {

        try {
            JSONObject payResponse = new JSONObject(response);
            int mCode = payResponse.getInt("code");
            if(200==mCode){//跳转到成功页面
                String success = payResponse.getString("success");
                if(success!=null&&"1".equals(success)){
                    query_num = payResponse.getString("query_num");
                    transaction_id = payResponse.getString("transaction_id");
                    order_no = payResponse.getString("order_no");
                    LogUtils.e(TAG,"-----parsePayResultJson------");
                    parsePayResultJson(payResponse);
                    LogUtils.e(TAG, "-----parsePayResultJson------");
                    toSuccessActivity();//轮询查询成功跳转到成功页
                }else{
                    handler.sendEmptyMessageDelayed(HANDLER_QUERY_LOOPER, 3000);
                }
            }else{
                handler.sendEmptyMessageDelayed(HANDLER_QUERY_LOOPER, 3000);
            }
        } catch (JSONException e) {
            Utils.showToast(this,"解析数据错误");
            e.printStackTrace();
        }
    }

    /**
     * 轮询查询成功返回
     * @param response
     */
    private void pollingPayResultUntilSuccess(String response) {

        try {
            JSONObject payResponse = new JSONObject(response);
            int mCode = payResponse.getInt("code");
            if(200==mCode){//跳转到成功页面
                String success = payResponse.getString("success");
                if(success!=null&&"1".equals(success)){
                    query_num = payResponse.getString("query_num");
                    transaction_id = payResponse.getString("transaction_id");
                    order_no = payResponse.getString("order_no");
                    LogUtils.e(TAG, "-----pollingPayResultUntilSuccess------");
                    parsePayResultJson(payResponse);
                    LogUtils.e(TAG, "-----pollingPayResultUntilSuccess------");
                    toSuccessActivity();//轮询查询成功跳转到成功页
                }else{
                    handler.sendEmptyMessageDelayed(HANDLER_QUERY_ALWAYS_LOOPER, 3000);
                }
            }else{
                handler.sendEmptyMessageDelayed(HANDLER_QUERY_ALWAYS_LOOPER, 3000);
            }
        } catch (JSONException e) {
            Utils.showToast(this,"解析数据错误");
            e.printStackTrace();
        }
    }

    private void parsePayResultJson(JSONObject payResponse) throws JSONException {
        discountBean=null;
        LogUtils.e(TAG, "0");
        if (!Constant.UNKNOWN_COMMODITY.equals("2")) {
            String ticketInfo = payResponse.getString("ticketInfo");
            if (!TextUtils.isEmpty(ticketInfo)) {
                Gson gson = new Gson();
                discountBean = gson.fromJson(ticketInfo, DiscountInfo.class);
            }
        }
    }

    /**
     * 重新获取二维码
     */
    private void reGetQRCode() {
        if("".equals(Constant.LAST_ORDER_NO)){
            Constant.LAST_ORDER_NO = order_no;
        }
        this_time_before_order_no = order_no;//每一次记录上一次的 order_no
        isRegetCode = true;
        netWorks.PayCard(getPayParams(), callBack, GET_QR_CODE);
    }

    /**
     * 查询付款结果
     */
    private void queryPayResult() {
        LogUtils.e(TAG, "queryPayResult() COUNT_TIME=" + COUNT_TIME);
        if(COUNT_TIME>0){//判断是否达到了两分钟
            if(fromWhere==Constant.ORDER_PAGER_DETAIL){
                LogUtils.e(TAG,"order_no_for_order="+order_no_for_order);
                netWorks.queryPayState(type, order_no_for_order, callBack, QUERY_PAY_RESULT);
            }else{
                if(isRegetCode){
                    netWorks.queryPayState(type, Constant.LAST_ORDER_NO, callBack, QUERY_PAY_RESULT);
                }else{
                    if("".equals(Constant.LAST_ORDER_NO)||"0".equals(Constant.LAST_ORDER_NO)){
                        netWorks.queryPayState(type, order_no, callBack, QUERY_PAY_RESULT);
                    }else{
                        netWorks.queryPayState(type, Constant.LAST_ORDER_NO, callBack, QUERY_PAY_RESULT);
                    }
                }
            }
        }else{//跳转到重新请求码的页面
            switchCodeFreshPager(1);//超两分钟弹出超时页面
        }
    }

    /**
     * 查询付款结果 一直查
     */
    private void queryPayResultUntilSuccess() {
        if(fromWhere==Constant.ORDER_PAGER_DETAIL){
            LogUtils.e(TAG, "order_no_for_order=" + order_no_for_order);
            netWorks.queryPayState(type, order_no_for_order, callBack, QUERY_PAY_RESULT_ALWAYS);
        }else{
            if(isRegetCode){
                netWorks.queryPayState(type, Constant.LAST_ORDER_NO, callBack, QUERY_PAY_RESULT_ALWAYS);
            }else{
                netWorks.queryPayState(type, order_no, callBack, QUERY_PAY_RESULT_ALWAYS);
            }
        }
    }

    private void switchCodeFreshPager(int isOutTime) {
        if(isOutTime==1){//超两分钟弹出超时页面
            rl_show_code.setVisibility(View.GONE);
            rl_page_out_time.setVisibility(View.VISIBLE);
        }else if(isOutTime==0){//重新请求得到二维码进行展示
            rl_show_code.setVisibility(View.VISIBLE);
            rl_page_out_time.setVisibility(View.GONE);
        }else{
            rl_page_out_of_quota.setVisibility(View.VISIBLE);
            rl_show_code.setVisibility(View.GONE);
            rl_page_out_time.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_cancel://取消
                finish();
                break;
            case R.id.btn_refresh_pager://刷新页面：顾客上一个二维码没有支付的时候刷新二维码并开始轮询，顾客已经支付返回支付成功进行跳转
                if (!judgeNet()) {
                    return;
                }
                if(ClicKUtils.isFastDoubleClick()){
                    return;
                }
                //刷新中...
                ll_refresh_loading.setVisibility(View.VISIBLE);
                pbAnimation(iv_refresh_loading, true);
                btn_refresh_pager.setVisibility(View.GONE);
                reGetQRCode();
                break;
            case R.id.btn_page_out_of_quota://超出单日支付限额
                finish();
                break;
        }
    }

    private PayParams getPayParams() {
        order_no = Utils.getTradeNum();
        PayParams payParams = new PayParams();
        payParams.setPayWay("0");//小微商户只有微信，写死为0
        payParams.setAuthCode("0");
        payParams.setOrderNo(order_no);
        payParams.setOrderId(String.valueOf(unpay_order_id));
        payParams.setPayMent(payment);
        payParams.setTotal_fee(total_fee);
        payParams.setVipId(vip_card);
        payParams.setCardId("0");
        payParams.setVipcreate_id(vipcreate_id);
        payParams.setVip_discount(vip_discount);
        payParams.setLast_order_no(this_time_before_order_no);//每一次请求都传（本次请求之前一次的）order_no，第一次传0
        payParams.setMicro("1");
        payParams.setCardinfo(cardinfo_pay);
        payParams.setType(Constant.UNKNOWN_COMMODITY);
        if(isZFB){
            isZFB = false;
            payParams.setLast_alipay("1");
        }else{
            payParams.setLast_alipay("0");
        }
        if(orderBean!=null){
            if(orderBean.getMsg()!=null){
                if(orderBean.getMsg().getRoom()!=null){
                    payParams.setRoom(new Gson().toJson(orderBean.getMsg().getRoom()));
                }
            }
        }
        return payParams;
    }

    private void toSuccessActivity() {
        if(isSuccess){//防止得到两次查询结果弹出两次成功页面
            isSuccess = false;
            trade_time = Utils.getTradeTime();
            Intent intent;
            handler.removeCallbacksAndMessages(null);
            intent = new Intent(PayQRCodeActivity.this, CollectionSuccessActivity.class);
            intent.putExtra("tradenum", order_no);
            intent.putExtra("transaction_id", transaction_id);//"transaction_id"
            intent.putExtra("tradetime", trade_time);
            intent.putExtra("querynum", query_num);//如果有就传过去 如果没有就传0
            intent.putExtra("song_money", give_money);//赠送的钱
            intent.putExtra("cardinfo_pay", cardinfo_pay);
            if(orderBean!=null){
                intent.putExtra("totalfee", orderBean.getMsg().getTotal_fee());
                intent.putExtra("payment", orderBean.getMsg().getPayment());
                intent.putExtra(Constant.ORDER_BEAN, orderBean);
            }else{
                intent.putExtra("payment", payment);
                intent.putExtra("total_fee", total_fee);
            }
            if(discountBean!=null){
                intent.putExtra(Constant.MEMBER_DISCOUNT_BEAN, discountBean);//为空，需要从查询二维码接口(PosPay/pay)获取
            }
            startActivity(intent);
            finish();
        }
    }

    /**
     * 判断所有的支付条件
     * @return
     */
    private boolean judgeNet() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            Utils.showToastLong(this, "未联网，请您将收银机联网，再次点击刷新");
            return false;
        } else {
            return true;
        }
    }

    private void notRegetCode() {
        ll_refresh_loading.setVisibility(View.GONE);
        btn_refresh_pager.setVisibility(View.VISIBLE);
    }

    /**
     * 显示加载的动画
     */
    public void pbAnimation(ImageView iv,boolean isShow) {
        AnimationSet ra = (AnimationSet) AnimationUtils.loadAnimation(this, R.anim.loading_rotate);
        ra.setInterpolator(new LinearInterpolator());
        if(iv!=null){
            iv.startAnimation(ra);
            if(isShow){
                iv.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 停止动画的显示
     */
    public void pbAnimationStop(ImageView iv,boolean isGone) {
        if(iv!=null){
            iv.clearAnimation();
            if(isGone){
                iv.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestory = true;
        handler.removeCallbacksAndMessages(null);
    }
}
