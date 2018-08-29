package com.younle.younle624.myapplication.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.pos.CollectionSuccessActivity;
import com.younle.younle624.myapplication.application.MyApplication;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.SavedFailOrder;
import com.younle.younle624.myapplication.domain.UnPayDetailsBean;
import com.younle.younle624.myapplication.domain.paybean.DiscountInfo;
import com.younle.younle624.myapplication.domain.paybean.PayParams;
import com.younle.younle624.myapplication.myservice.FailOrderService;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

/**
 * 作者：Create by 我是奋斗 on2016/12/14 15:14
 * 邮箱：tt090423@126.com
 * 微信：17346512596
 */
public class CashCardPayUtils {

    private static CashCardPayUtils instance;
    private Activity activity;
    private Activity umActivity;
    private SavedFailOrder ucashOrder;
    private Handler uhandler;
    private AlertDialog alertDialog;
    private DiscountInfo discountInfo;

    public static CashCardPayUtils getInstance() {
        if (instance == null) {
            instance = new CashCardPayUtils();
        }
        return instance;
    }

    /**
     * 现金或者刷卡支付
     *
     * @param mActivity
     * @param orderBean
     * @param orderBean 下单未支付订单
     */
    public void cashCardPay(Activity mActivity, SavedFailOrder cashOrder, Handler handler,
                            UnPayDetailsBean orderBean) {
        umActivity = mActivity;
        ucashOrder = cashOrder;
        uhandler = handler;
        //LogUtils.e("测试流程时间", "时间1=" + Utils.getCurrentTime1());
        if (NetworkUtils.isNetworkAvailable(mActivity)) {
            //显示Dialog,如果已经开始联网支付，怎屏蔽后续的请求
            //LogUtils.e("测试流程时间","时间2="+Utils.getCurrentTime1());
            showDialog();
            //LogUtils.e("测试流程时间", "时间3=" + Utils.getCurrentTime1());
            updateOrderList(orderBean);
        } else {
           // LogUtils.e("测试流程时间","时间4="+Utils.getCurrentTime1());
            ucashOrder.setQuery_num(Utils.getBQueryNum(mActivity));
            saveCashCardOrder(orderBean);
        }
    }

    /**
     * 联网更新现金或者刷卡订单
     *
     * @param orderBean
     */
    private void updateOrderList(final UnPayDetailsBean orderBean) {

        NetWorks netWorks = new NetWorks(umActivity);
        netWorks.PayCard(getPayParams(orderBean), new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                LogUtils.Log("提交现金刷卡订单error:" + e.toString());
                cannotDisMiss();
                ucashOrder.setQuery_num(Utils.getBQueryNum(umActivity));
                saveCashCardOrder(orderBean);
            }

            @Override
            public void onResonse(String response, int flag) {
                LogUtils.Log("现金刷卡：onResponse():::" + response);
                cannotDisMiss();
                boolean toNextStep = Utils.checkSaveToken(umActivity, response);
                if (toNextStep) {
                    try {
                        JSONObject payResponse = new JSONObject(response);
                        String code = payResponse.getString("code");
                        if ("200".equals(code)) {
                            try {
                                //String QRCode_url = payResponse.getString("url");
                                String QRCode_url =  payResponse.getJSONObject("ticketInfo").getString("url");
                                LogUtils.e("现金刷卡：onResponse()","QRCode_url="+QRCode_url);
                                if(QRCode_url!=null&&!"".equals(QRCode_url)){
                                    Constant.query_member_left = QRCode_url;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            double payment = 0;
                            try {
                                payment = payResponse.getDouble("payment");
                            } catch (JSONException e) {
                                LogUtils.Log("现金刷卡JSONException e=" + e);
                                e.printStackTrace();
                            }
                            LogUtils.e("现金刷卡：onResponse():::","payment="+payment);
                            parsePayResultJson(payResponse, orderBean,payment);
                            if (uhandler != null) {
                                uhandler.sendEmptyMessageDelayed(100, 1000);//POS页面的清屏专用100
                                Constant.IS_CLOSE_DETAIL_ACTIVITY = true;
                            }
                        } else {
                            LogUtils.Log("现金刷卡：onResponse():code=" + code);
                            ucashOrder.setQuery_num(Utils.getBQueryNum(umActivity));
                            saveCashCardOrder(orderBean);
                        }
                    } catch (JSONException e) {
                        LogUtils.Log("现金刷卡JSONException e=" + e);
                        e.printStackTrace();
                    }
                }
            }
        }, 0);
    }

    private PayParams getPayParams(UnPayDetailsBean orderBean) {
        PayParams payParams = new PayParams();
        payParams.setType(Constant.UNKNOWN_COMMODITY);
        payParams.setPayWay(Constant.payway);
        payParams.setOrderNo(ucashOrder.getOrder_no_last());//2017.1.7 17:39改
        if(ucashOrder.getOrder_id()!=null){
            payParams.setOrderId(ucashOrder.getOrder_id());
        }else{
            payParams.setOrderId("0");
        }
        payParams.setLast_order_no(Constant.LAST_ORDER_NO);
        payParams.setPayMent(ucashOrder.getPayment());
        payParams.setTotal_fee(ucashOrder.getTotal_fee());
        payParams.setVipId(ucashOrder.getVipcard_id());
        payParams.setVip_discount(ucashOrder.getVip_discount());
        payParams.setCardId("0");
        payParams.setAuthCode("0");
        LogUtils.e("现金刷卡支付", "payParams.setVipcreate_id=" + ucashOrder.getVipcreate_id());
        payParams.setVipcreate_id(ucashOrder.getVipcreate_id());
        payParams.setCardinfo(ucashOrder.getCardinfo());
        if(orderBean!=null){
            if(orderBean.getMsg()!=null){
                if(orderBean.getMsg().getRoom()!=null){
                    payParams.setRoom(new Gson().toJson(orderBean.getMsg().getRoom()));
                }
            }
        }
        return payParams;
    }

    private void parsePayResultJson(JSONObject payResponse, UnPayDetailsBean orderBean,double payment) throws JSONException {
        String query_num = "未获取";
        discountInfo=null;
        //非会员充值，有优惠信息的需要解析
        if (!Constant.UNKNOWN_COMMODITY.equals("2")) {
            query_num = payResponse.getString("query_num");
            String ticketInfo = payResponse.getString("ticketInfo");
            if (!TextUtils.isEmpty(ticketInfo)) {
                Gson gson = new Gson();
                discountInfo = gson.fromJson(ticketInfo, DiscountInfo.class);
                LogUtils.Log("现金支付：" + discountInfo.toString());
            }
        }
        toCSActivity(query_num, orderBean, payment);
    }

    /**
     * 存储现金刷卡订单到数据表中
     *
     * @param orderBean
     */
    private void saveCashCardOrder(UnPayDetailsBean orderBean) {
        MyApplication myAppinstance = MyApplication.getInstance();
        DbManager.DaoConfig daoConfig = myAppinstance.getDaoConfig();
        DbManager db = org.xutils.x.getDb(daoConfig);
        //LogUtils.e("测试流程时间", "时间5=" + Utils.getCurrentTime1());

        try {
            db.save(ucashOrder);
            //LogUtils.e("测试流程时间", "时间6=" + Utils.getCurrentTime1());
        } catch (DbException e) {
            e.printStackTrace();
        }
        //开始同步本地订单
        initService();
        toCSActivity(ucashOrder.getQuery_num(), orderBean, 0);
        //LogUtils.e("测试流程时间", "时间9=" + Utils.getCurrentTime1());
        currentDb(daoConfig);
        //LogUtils.e("测试流程时间", "时间10=" + Utils.getCurrentTime1());
        if (uhandler != null) {
            uhandler.sendEmptyMessageDelayed(100, 1000);//POS页面的清屏专用100
        }
        //LogUtils.e("测试流程时间", "时间11=" + Utils.getCurrentTime1());
    }

    /**
     * 跳转到收款成功界面
     */
    private void toCSActivity(String query_num, UnPayDetailsBean orderBean,double payment) {
        Intent intent = new Intent(umActivity, CollectionSuccessActivity.class);
        intent.putExtra("payway", ucashOrder.getPay_type());
        intent.putExtra("tradenum", ucashOrder.getOrder_no_last());//2016.01.07 17:53
        //intent.putExtra("payment", ucashOrder.getPayment());
        if(payment>0){
            intent.putExtra("payment", String.valueOf(payment));
        }else{
            intent.putExtra("payment", ucashOrder.getPayment());
        }
        intent.putExtra("total_fee", ucashOrder.getTotal_fee());
        intent.putExtra("tradetime", ucashOrder.getPaytime());
        intent.putExtra(Constant.ORDER_BEAN, orderBean);
        intent.putExtra(Constant.MEMBER_DISCOUNT_BEAN, discountInfo);
        intent.putExtra("querynum", query_num);
        intent.putExtra("cardinfo_pay",ucashOrder.getCardinfo());
        umActivity.startActivity(intent);
    }

    /**
     * 初始化服务
     */
    private void initService() {

        //net.loonggg.testbackstage.TestService
        boolean serviceWork = Utils.isServiceWork(umActivity, Constant.SERVICE_UPDATE_FAIL_OR_CASHCARD);
        LogUtils.Log("服务有没有在工作：" + serviceWork);
        LogUtils.e("测试流程时间", "时间7=" + Utils.getCurrentTime1());
        if (!serviceWork) {
            //在此开启服务，进行间隔轮询失败订单
            LogUtils.Log("服务之前未打开，新开服务...");
            Intent intent = new Intent(umActivity, FailOrderService.class);
            umActivity.startService(intent);
        }
        LogUtils.e("测试流程时间","时间8="+Utils.getCurrentTime1());
    }

    /**
     * 查看当前db文件
     *
     * @param daoConfig
     */
    private static void currentDb(DbManager.DaoConfig daoConfig) {
        try {
            List<SavedFailOrder> all = x.getDb(daoConfig).findAll(SavedFailOrder.class);
            if (all != null && all.size() > 0) {
                for (int i = 0; i < all.size(); i++) {
                    LogUtils.Log(all.get(i).toString());
                }
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void showDialog() {
        alertDialog = Utils.wybDialog(umActivity, false, R.layout.pos_dialog, 0, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, "");
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:
                        break;
                }
                return true;
            }
        });
    }

    private void cannotDisMiss() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

}
