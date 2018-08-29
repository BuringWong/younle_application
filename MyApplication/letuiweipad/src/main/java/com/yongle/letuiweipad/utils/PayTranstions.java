package com.yongle.letuiweipad.utils;

import android.content.Context;

import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.domain.createorder.SavedFailOrder;

/**
 * Created by Administrator on 2017/9/23.
 */

public class PayTranstions {
    /**
     * 获取需要存储的对象
     *
     * @param trade_num
     * @param query_num
     * @param trade_time
     * @param money      total_fee,订单总金额
     * @param success    是否成功的标识,0是失败，1是成功
     * @param cancel     是否撤销 0是不需要撤销，1是需要撤销
     * @param payMent    实收金额
     * @param vipcard_id
     * @param fact_price 充的钱
     * @param give_price 送的钱
     */
    public static SavedFailOrder getSaveOrder(String ld_order_no, String trade_num, String order_id,
                                              String money, String query_num, String trade_time,
                                              String success, String cancel, String payMent,
                                              String vipcard_id, String fact_price, String give_price,
                                              String vipcreate_id, String vip_discount, String cardinfo) {
        SavedFailOrder cashOrder = new SavedFailOrder();
        cashOrder.setStoreid(Constant.STORE_ID);
        cashOrder.setAdv_id(Constant.ADV_ID);
        cashOrder.setDeviceid(Constant.DEVICE_IMEI);

        cashOrder.setPay_type(Constant.payway);
        cashOrder.setTotal_fee(money);//总金额
        cashOrder.setPayment(payMent);//实付金额
        if("0".equals(ld_order_no)||"".equals(ld_order_no)){
            cashOrder.setOrder_no(trade_num);
        }else{
            cashOrder.setOrder_no(ld_order_no);
        }

        cashOrder.setOrder_no_last(trade_num);
        cashOrder.setSuccess(success);
        cashOrder.setCancel(cancel);
        cashOrder.setType(Constant.UNKNOWN_COMMODITY);
        cashOrder.setQuery_num(query_num);
        cashOrder.setPaytime(trade_time);
        cashOrder.setAddtime(trade_time);
        cashOrder.setOrder_id(order_id);

        cashOrder.setVipcard_id(vipcard_id);
        cashOrder.setVipcreate_id(vipcreate_id);
        cashOrder.setVip_discount(String.valueOf(vip_discount));
//        cashOrder.setFact_price(fact_price);
        cashOrder.setGive_price(give_price);
        cashOrder.setCardinfo(cardinfo);

        return cashOrder;
    }
    private static String QUERY_DAY = "query_day";
    private static String QUERY_NUM = "query_num";


    /**
     * 获取查询编号
     *
     * @return
     */
    public static String getBQueryNum(Context context) {
        Integer return_num = null;
        //判断是否过了一天：存储时间戳 利用格式转换判断天数是否发生变化
        String storage_day = SpUtils.getInstance(context).getString(QUERY_DAY, "");
        if ("".equals(storage_day)) {
            SpUtils.getInstance(context).save(QUERY_DAY, Utils.getCurrentTime1());
            return_num = 1;
        } else {
            Long day = Long.valueOf(storage_day);
            String storage_day_time = Utils.getDatetimeStampToString(day);//"yyyy-MM-dd HH:mm:ss"
            LogUtils.Log("stroage_day_time==" + storage_day_time);
            String now_day_time = Utils.getDatetimeStampToString(Long.valueOf(Utils.getCurrentTime1()));

            String storage_this_day = storage_day_time.substring(0, 10);
            String now_this_day = now_day_time.substring(0, 10);

            if (storage_this_day.equals(now_this_day)) {
                //获取存储的序号加1
                String query_num = SpUtils.getInstance(context).getString(QUERY_NUM, "");
                if ("".equals(query_num)) {
                    return_num = 1;
                } else {
                    return_num = Integer.valueOf(query_num);
                    return_num += 1;
                }
            } else {
                //本地查询号变为1，更改存储的时间戳
                return_num = 1;
                SpUtils.getInstance(context).save(QUERY_DAY, Utils.getCurrentTime1());
            }
        }
        SpUtils.getInstance(context).save(QUERY_NUM, return_num + "");
        LogUtils.e("Utils", "本地存储订单：B" + Constant.DEVICE_ADV_NUM + return_num);
        return "B" + Constant.DEVICE_ADV_NUM + return_num;
    }
    /**
     * 返回false不可玩下进行
     * @return
     */
    public static boolean payIegal(Context context,String payment,boolean isCash){
        Double money=0.00;
        try {
            money = Double.valueOf(payment);
        }catch (Exception e){
            Utils.showToast(context,"输入金额不合法");
        }
        if (money > 999999.99) {
            Utils.showToast(context, context.getResources().getString(R.string.pay_money_max));
            return false;
        }
        if (money < 0.01) {
            Utils.showToast(context, context.getResources().getString(R.string.pay_money_min));
            return false;
        }
        if (isCash) {//现金收款不受网络限制
            return true;
        } else {//微信支付宝判断网络状态
            if (!NetworkUtils.isNetworkAvailable(context)) {
                Utils.showToast(context, context.getResources().getString(R.string.no_network_please_cash_card));
                return false;
            }
        }
        return true;
    }

}
