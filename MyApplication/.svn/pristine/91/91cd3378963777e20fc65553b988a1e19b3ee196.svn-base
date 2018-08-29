package com.younle.younle624.myapplication.activity.pos;

import android.app.Activity;

public class CollectionUnknownActivity extends Activity {

    /*private static final String ORDER_FAIL = "0";           //订单失败
    private static final String ORDER_SUCCESS = "1";        //订单成功
    private static final String PAY_TYPE_POST_CARD = "2";   //刷卡支付
    private static final String PAY_TYPE_CASH = "3";        //现金支付
    private static final String IS_BACK_MONEY = "1";        //需要返款
    private Button btn_trade_pay_cash;
    private Button btn_trade_post_card;
    private TextView btn_trade_canale;
    private TextView tv_trade_num;
    private TextView tv_trade_time;
    private TextView tv_pay_way;
    private LinearLayout ll_return_pre;
    private String tradenum = "0";
    private String tradetime = "0";
    private String query_num = "0";
    *//**
     * 会员充值赠送的钱
     *//*
    private String give_money = "0";

    private String adv_id = Constant.ADV_ID;//品牌主ID (全局)
    private String storeid = Constant.STORE_ID;//门店ID  (全局)
    private String deviceid = Constant.DEVICE_IMEI;//设备ID (全局)
    *//**
     * 总金额
     *//*
    private String total_fee;//总费用
    *//**
     * 实际支付金额，改价后不同于total_fee
     *//*
    private String payment;//总费用
    private String TAG = "CollectionUnknownActivity";
    private int fromeWhere;
    private String vip_card="0";
    private UnPayDetailsBean orderBean;
    *//**
     * 不是会员达到会员领卡标准后的会员优惠信息
     *//*
    private DiscountInfo discountBean;
    *//**
     * 扫到的码
     *//*
    private String cardId;
    private String order_id="0";
    private String order_no="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_nuknown);
        Utils.initToolBarState(this);
        initView();
        initData();
        setlistener();
        storeToDatabase(tradenum, total_fee, "0", tradetime, query_num, "1");
    }

    *//**
     * 初始化视图
     *//*
    private void initView() {
        btn_trade_pay_cash = (Button) findViewById(R.id.btn_trade_pay_cash);
        btn_trade_post_card = (Button) findViewById(R.id.btn_trade_post_card);
        btn_trade_canale = (TextView) findViewById(R.id.btn_trade_canale);
        tv_trade_num = (TextView) findViewById(R.id.tv_trade_num);
        tv_trade_time = (TextView) findViewById(R.id.tv_trade_time);
        tv_pay_way = (TextView) findViewById(R.id.tv_pay_way);
        ll_return_pre = (LinearLayout) findViewById(R.id.ll_return_pre);
    }

    *//**
     * 初始化服务
     *//*
    private void initService() {

        //net.loonggg.testbackstage.TestService
        boolean serviceWork = Utils.isServiceWork(CollectionUnknownActivity.this, "com.younle.younle624.myapplication.myservice.FailOrderService");
        LogUtils.e(TAG,"服务有没有在工作："+serviceWork);
        if (!serviceWork){
            //在此开启服务，进行间隔轮询失败订单
            LogUtils.e(TAG,"服务之前未打开，新开服务...");
            Intent intent = new Intent(CollectionUnknownActivity.this, FailOrderService.class);
            startService(intent);
        }
    }

    *//**
     * 初始化数据
     *//*
    private void initData() {

        //获取数据
        String tradenumExtra = getIntent().getStringExtra("tradenum");
        String tradetimeExtra = getIntent().getStringExtra("tradetime");
        String total_feeExtra = getIntent().getStringExtra("total_fee");
        String paymentExtra = getIntent().getStringExtra("payment");
        String querynumExtra = getIntent().getStringExtra("querynum");
        String givemoneyExtra = getIntent().getStringExtra("give_money");
        cardId = getIntent().getStringExtra("auth_code");

        String vip_cardExtra = getIntent().getStringExtra("vip_card");
        if(vip_cardExtra!=null&&!TextUtils.isEmpty(vip_cardExtra)) {
            vip_card=vip_cardExtra;
        }
        fromeWhere = getIntent().getIntExtra(Constant.FROME_WHERE, -1);
        orderBean = (UnPayDetailsBean) getIntent().getSerializableExtra(Constant.ORDER_BEAN);
        if(orderBean!=null) {
            order_id = orderBean.getMsg().getOrderid()+"";
            order_no = orderBean.getMsg().getOrder_no();
        }
        discountBean = (DiscountInfo) getIntent().getSerializableExtra(Constant.MEMBER_DISCOUNT_BEAN);

        if (tradenumExtra != null){tradenum = tradenumExtra;}
        if (tradetimeExtra != null){tradetime = tradetimeExtra;}
        if (total_feeExtra != null){total_fee = total_feeExtra;}
        if (paymentExtra != null){payment = paymentExtra;}
        if (querynumExtra != null){query_num = querynumExtra;}
        if (givemoneyExtra != null){give_money = givemoneyExtra;}

        showData();
    }

    //设置数据的显示
    private void showData() {
        tv_trade_num.setText(tradenum);
        String way = "未知";
        switch (Constant.payway) {
            case "0" :
                way="微信支付";
                break;
            case "1" :
                way="支付宝支付";
                break;
            case "2" :
                way="刷卡支付";
                break;
            case "3" :
                way="现金支付";
                break;
            case "4" :
                way="会员卡支付";
                break;
        }
        tv_pay_way.setText(way);
        tv_trade_time.setText(Utils.getDatetimeStampToString(tradetime + "000"));
    }

    *//**
     * 设置监听
     *//*
    private void setlistener() {

        //返回
        ll_return_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //取消
        btn_trade_canale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(CollectionUnknownActivity.this)
                        .setTitle("取消订单后，订单内容将被删除，您确定要取消订单么？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setChargeResult(false);
                                //存储进数据库标记为撤销订单状态
                                updateDatabase(tradenum, Constant.payway, adv_id, storeid, total_fee, ORDER_FAIL,
                                        deviceid, tradetime, Utils.getTradeTime(), Constant.UNKNOWN_COMMODITY, "1");
                                Constant.IS_CLOSE_DETAIL_ACTIVITY=true;
                                finish();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        //标记为现金收款，存储到数据库
        btn_trade_pay_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChargeResult(true);
                finish();
                //存储进数据库标记为现金支付状态
                updateDatabase(tradenum, PAY_TYPE_CASH, adv_id, storeid, total_fee, ORDER_SUCCESS,
                        deviceid, tradetime, Utils.getTradeTime(), Constant.UNKNOWN_COMMODITY, "0");

                toCSActivity(PAY_TYPE_CASH);
            }
        });
        //刷卡支付
        btn_trade_post_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChargeResult(true);
                //存储进数据库标记为刷卡支付状态
                updateDatabase(tradenum, PAY_TYPE_POST_CARD, adv_id, storeid, total_fee, ORDER_SUCCESS,
                        deviceid, tradetime, Utils.getTradeTime(), Constant.UNKNOWN_COMMODITY, "0");
                toCSActivity(PAY_TYPE_POST_CARD);
            }
        });
    }

    *//**
     * 存储到数据库
     *
     * @param total_fee
     * @param success
     * @param addtime
     * @param query_num
     * @param cancel
     *//*
    private void storeToDatabase(String trade_num, String total_fee, String success, String addtime, String query_num, String cancel) {

        MyApplication myAppinstance = MyApplication.getInstance();
        DbManager.DaoConfig daoConfig = myAppinstance.getDaoConfig();
        DbManager db = org.xutils.x.getDb(daoConfig);
        SavedFailOrder savedFailOrder;
        if(order_no!=null&&!TextUtils.isEmpty(order_no)) {
             savedFailOrder = Utils.getSaveOrder(order_no,trade_num, "0",total_fee, query_num, addtime, success, cancel, payment, vip_card, total_fee, give_money);//type="1" 代表直接收银
        }else {
             savedFailOrder = Utils.getSaveOrder("0",trade_num, "0",total_fee, query_num, addtime, success, cancel, payment, vip_card, total_fee, give_money);//type="1" 代表直接收银
        }
        try {
            db.save(savedFailOrder);
        } catch (DbException de) {
            de.printStackTrace();
        }
    }

    *//**
     * 标记收款成功后跳转至收款成功界面
     * @param payway
     *//*
    private void toCSActivity(String payway){
        Intent intent = new Intent(CollectionUnknownActivity.this, CollectionSuccessActivity.class);
        Constant.payway = payway;
        intent.putExtra("tradenum", tradenum);
        intent.putExtra("totalfee", total_fee);
        intent.putExtra("tradetime", tradetime);
        LogUtils.Log("传递前：" + tradetime);
        intent.putExtra("querynum", query_num);
        intent.putExtra(Constant.ORDER_BEAN, orderBean);
        intent.putExtra(Constant.MEMBER_DISCOUNT_BEAN, discountBean);
        startActivity(intent);
        Constant.IS_CLOSE_DETAIL_ACTIVITY=true;
        finish();
    }

    *//**
     * 设置会员卡充值微信支付宝收款结果
     *//*
    private void setChargeResult(boolean result) {
        if (fromeWhere== Constant.MEMBER_CHARGE_PAY) {
//            Constant.CHARGE_SUCCESS=result;
        }
    }
    *//**
     * 更新数据库数据
     *
     * @param orderId
     * @param pay_type
     * @param adv_id
     * @param storeid
     * @param total_fee
     * @param success
     * @param deviceid
     * @param addtime
     * @param paytime
     * @param type
     *//*
    private void updateDatabase(String orderId, String pay_type, String adv_id, String storeid, String total_fee,
                                String success, String deviceid, String addtime, String paytime, String type,
                                String cancel){

        MyApplication myAppinstance = MyApplication.getInstance();
        DbManager.DaoConfig daoConfig = myAppinstance.getDaoConfig();
        DbManager db = org.xutils.x.getDb(daoConfig);

        SavedFailOrder savedFailOrder = new SavedFailOrder();

        if(orderBean!=null) {
            savedFailOrder.setOrder_no(order_no);
        }else {
            if(TextUtils.isEmpty(Constant.LAST_ORDER_NO)) {
                savedFailOrder.setOrder_no(orderId);
            }else {
                savedFailOrder.setOrder_no(Constant.LAST_ORDER_NO);
            }
        }

        savedFailOrder.setOrder_no_last(orderId);
        savedFailOrder.setPay_type(pay_type);
        savedFailOrder.setAdv_id(adv_id);
        savedFailOrder.setStoreid(storeid);
        savedFailOrder.setTotal_fee(total_fee);
        savedFailOrder.setSuccess(success);
        savedFailOrder.setDeviceid(deviceid);
        savedFailOrder.setAddtime(addtime);
        savedFailOrder.setPaytime(paytime);
        savedFailOrder.setType(type);
        savedFailOrder.setCancel(cancel);
//        savedFailOrder.setOrder_id(order_id);
        savedFailOrder.setPayment(payment);
        savedFailOrder.setQuery_num(query_num);
        savedFailOrder.setVipcard_id(vip_card);
        savedFailOrder.setGive_price(give_money);

        //进行比对更新信息
        try {
            List<SavedFailOrder> failOrders = db.findAll(SavedFailOrder.class);

            if(failOrders != null){
                int num = failOrders.size() - 1;
                if(num >= 0){
                    LogUtils.e(TAG,"failOrders.size()>0");
                    if(orderId.equals(failOrders.get(num).getOrder_no())){
                        LogUtils.e(TAG,"orderId.equals(failOrders.get(failOrders.size()-1).getOrder_no())==true");
                        failOrders.set(num, savedFailOrder);
                    }
                }
            }

            db.dropTable(SavedFailOrder.class);
            if(failOrders != null){
                for(int j=0;j<failOrders.size();j++){
                    db.save(failOrders.get(j));
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        LogUtils.e(TAG,"结果未知失败页面打开服务...");
        initService();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK :
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }*/
}
