package com.younle.younle624.myapplication.domain;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by bert_dong on 2016/8/10 0010.
 * 邮箱：18701038771@163.com
 */
@Table(name = "savedfailorder")
public class SavedFailOrder implements Serializable{

    @Column(name = "id",autoGen = true,isId = true)
    private String id;
    @Column(name = "order_no")
    private String order_no = "0";
    @Column(name = "query_num")//结果未知的时候的显示单号:随机5位
    private String query_num;
    @Column(name = "adv_id")
    private String adv_id = "0";
    @Column(name = "storeid")
    private String storeid = "0";
    @Column(name = "pay_type")
    private String pay_type;
    @Column(name = "success")
    private String success;
    @Column(name = "deviceid")
    private String deviceid = "0";
    @Column(name = "addtime")
    private String addtime = "0";
    @Column(name = "paytime")
    private String paytime = "0";
    @Column(name = "type")
    private String type = "5";
    @Column(name = "total_fee")
    private String total_fee;
    @Column(name = "payment")
    private String payment;
    @Column(name = "cancel")
    private String cancel = "0";//不设置的话就为0设置才为1

    //会员充值加
    @Column(name = "vipcard_id")
    private String vipcard_id;
   /* @Column(name = "fact_price")
    private String fact_price = "0.00";*/
    @Column(name = "give_price")
    private String give_price;
    @Column(name = "order_no_last")
    private String order_no_last;
    @Column(name = "order_id")
    private String order_id = "0";
    @Column(name = "vipcreate_id")
    private String vipcreate_id = "0";
    @Column(name = "vip_discount")
    private String vip_discount = "0";
    @Column(name = "cardinfo")
    private String cardinfo = "";
    @Column(name = "remars_info")
    private String remarsinfo = "";

    public String getRemarsinfo() {
        return remarsinfo;
    }
    public void setRemarsinfo(String remarsinfo) {
        this.remarsinfo = remarsinfo;
    }
    public String getCardinfo() {
        return cardinfo;
    }
    public void setCardinfo(String cardinfo) {
        this.cardinfo = cardinfo;
    }
    public String getVip_discount() {
        return vip_discount;
    }
    public void setVip_discount(String vip_discount) {
        this.vip_discount = vip_discount;
    }
    public String getVipcreate_id() {
        return vipcreate_id;
    }
    public void setVipcreate_id(String vipcreate_id) {
        this.vipcreate_id = vipcreate_id;
    }
    public String getOrder_id() {
        return order_id;
    }
    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
    public String getVipcard_id() {
        return vipcard_id;
    }
    public void setVipcard_id(String vipcard_id) {
        this.vipcard_id = vipcard_id;
    }
    public String getGive_price() {
        return give_price;
    }
    public void setGive_price(String give_price) {
        this.give_price = give_price;
    }
    public String getQuery_num() {
        return query_num;
    }
    public void setQuery_num(String query_num) {
        this.query_num = query_num;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTotal_fee() {
        return total_fee;
    }
    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }
    public String getAdv_id() {
        return adv_id;
    }
    public void setAdv_id(String adv_id) {
        this.adv_id = adv_id;
    }
    public String getStoreid() {
        return storeid;
    }
    public void setStoreid(String storeid) {
        this.storeid = storeid;
    }
    public String getPay_type() {
        return pay_type;
    }
    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }
    public String getSuccess() {
        return success;
    }
    public void setSuccess(String success) {
        this.success = success;
    }
    public String getDeviceid() {
        return deviceid;
    }
    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }
    public String getAddtime() {
        return addtime;
    }
    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }
    public String getPaytime() {
        return paytime;
    }
    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getOrder_no() {
        return order_no;
    }
    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }
    public String getCancel() {
        return cancel;
    }
    public void setCancel(String cancel) {
        this.cancel = cancel;
    }
    public String getPayment() {
        return payment;
    }
    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getOrder_no_last() {
        return order_no_last;
    }

    public void setOrder_no_last(String order_no_last) {
        this.order_no_last = order_no_last;
    }

    @Override
    public String toString() {
        return "SavedFailOrder{" +
                "id='" + id + '\'' +
                ", order_no='" + order_no + '\'' +
                ", query_num='" + query_num + '\'' +
                ", adv_id='" + adv_id + '\'' +
                ", storeid='" + storeid + '\'' +
                ", pay_type='" + pay_type + '\'' +
                ", success='" + success + '\'' +
                ", deviceid='" + deviceid + '\'' +
                ", addtime='" + addtime + '\'' +
                ", paytime='" + paytime + '\'' +
                ", type='" + type + '\'' +
                ", total_fee='" + total_fee + '\'' +
                ", payment='" + payment + '\'' +
                ", cancel='" + cancel + '\'' +
                ", vipcard_id='" + vipcard_id + '\'' +
                ", give_price='" + give_price + '\'' +
                ", old_order_no='" + order_no_last + '\'' +
                '}';
    }
}
