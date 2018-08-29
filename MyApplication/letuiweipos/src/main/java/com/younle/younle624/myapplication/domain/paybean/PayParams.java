package com.younle.younle624.myapplication.domain.paybean;

/**
 * 作者：Create by 我是奋斗 on2016/12/19 12:07
 * 邮箱：tt090423@126.com
 * 微信：17346512596
 *
 * 支付接口的所有参数的 bean
 */
public class PayParams {

    private String payWay = "0";//支付方式
    private String authCode = "0";//微信支付宝扫到的支付code
    private String orderNo = "0";//app端生成的trade_num
    private String orderId = "0";//订单id,直接收银时候是trade_num,订单收银时候是primarykeyid
    private String payMent= "0.00";//实际支付金额
    private String total_fee= "0.00";//订单金额
    private String vipId= "0";//支付时会员id
    private String cardId= "0";//会员充值时会员id
    private String vipcreate_id= "0";//会员充值时会员id
    private int outTime = 5000;//连接超时时间
    private String type = "1";//交易类型：直接收银 订单收银 会员充值
    private String last_order_no= "0";//支付失败时候第一次失败的trade_num
    private String room = "";
    private String micro = "0";
    private String vip_discount = "0";
    private String last_alipay = "0";
    private String cardinfo = "";

    public PayParams() {}

    public String getCardinfo() {
        return cardinfo;
    }

    public void setCardinfo(String cardinfo) {
        this.cardinfo = cardinfo;
    }

    public String getLast_alipay() {
        return last_alipay;
    }

    public void setLast_alipay(String last_alipay) {
        this.last_alipay = last_alipay;
    }

    public String getVip_discount() {
        return vip_discount;
    }

    public void setVip_discount(String vip_discount) {
        this.vip_discount = vip_discount;
    }

    public String getMicro() {
        return micro;
    }

    public void setMicro(String micro) {
        this.micro = micro;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getVipcreate_id() {
        return vipcreate_id;
    }

    public void setVipcreate_id(String vipcreate_id) {
        this.vipcreate_id = vipcreate_id;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getPayMent() {
        return payMent;
    }

    public void setPayMent(String payMent) {
        this.payMent = payMent;
    }

    public String getVipId() {
        return vipId;
    }

    public void setVipId(String vipId) {
        this.vipId = vipId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public int getOutTime() {
        return outTime;
    }

    public void setOutTime(int outTime) {
        this.outTime = outTime;
    }

    public String getLast_order_no() {
        return last_order_no;
    }

    public void setLast_order_no(String last_order_no) {
        this.last_order_no = last_order_no;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PayParams{" +
                "payWay='" + payWay + '\'' +
                ", authCode='" + authCode + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", orderId='" + orderId + '\'' +
                ", payMent='" + payMent + '\'' +
                ", total_fee='" + total_fee + '\'' +
                ", vipId='" + vipId + '\'' +
                ", cardId='" + cardId + '\'' +
                ", vipcreate_id='" + vipcreate_id + '\'' +
                ", outTime=" + outTime +
                ", type='" + type + '\'' +
                ", last_order_no='" + last_order_no + '\'' +
                ", room='" + room + '\'' +
                ", micro='" + micro + '\'' +
                ", vip_discount='" + vip_discount + '\'' +
                ", last_alipay='" + last_alipay + '\'' +
                '}';
    }
}
