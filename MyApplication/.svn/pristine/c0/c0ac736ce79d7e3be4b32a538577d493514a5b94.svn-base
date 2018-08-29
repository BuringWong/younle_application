package com.yongle.letuiweipad.domain;

import com.younle.younle624.myapplication.domain.orderbean.RefundOrder;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bert_dong on 2016/7/30 0030.
 * 邮箱：18701038771@163.com
 */
public class PosPrintBean implements Serializable {
    /**
     * code : 200
     * msg : {"dayOrderNum":"15","discount":0.9,"factPayMoney":8.1,"goodsInfo":[{"end":"","name":"红塔山","num":"2","price":3,"start":"","type":"0"},{"end":"","name":"加多宝","num":"2","price":3,"start":"","type":"0"},{"end":"","name":"泰式按摩2钟","num":"2","price":100,"start":"","type":"0"},{"end":"2016-11-07 16:18:00","name":"总统房2","num":"1","price":322.98,"start":"2016-11-07 16:14:00","type":"1"},{"end":"2016-11-07 16:29:00","name":"总统房3","num":"1","price":988,"start":"2016-11-07 16:24:00","type":"1"}],"orderNo":"147886086429176","payType":"现金支付","remark":"","shouldPayMoney":9,"tradeTime":"2016-11-11 18:41:06"}
     */

    private int code;
    /**
     * dayOrderNum : 15
     * discount : 0.9
     * factPayMoney : 8.1
     * goodsInfo : [{"end":"","name":"红塔山","num":"2","price":3,"start":"","type":"0"},{"end":"","name":"加多宝","num":"2","price":3,"start":"","type":"0"},{"end":"","name":"泰式按摩2钟","num":"2","price":100,"start":"","type":"0"},{"end":"2016-11-07 16:18:00","name":"总统房2","num":"1","price":322.98,"start":"2016-11-07 16:14:00","type":"1"},{"end":"2016-11-07 16:29:00","name":"总统房3","num":"1","price":988,"start":"2016-11-07 16:24:00","type":"1"}]
     * orderNo : 147886086429176
     * payType : 现金支付
     * remark :
     * shouldPayMoney : 9
     * tradeTime : 2016-11-11 18:41:06
     */

    private MsgBean msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public static class MsgBean implements Serializable {
        private String dayOrderNum;
        private double discount;
        private double factPayMoney;
        private String orderNo;
        private String orderNoOri;
        private String transaction_id;
        private String payType;
        private String remark;
        private String orderNote;
        private double shouldPayMoney;
        private String addTime;
        private String payTime;

        private String roomName;
        private String isRefund;
        private double refundMoney;
        private String refundOperator;
        private int orderType;//0代表有商品且已经支付；1代表会员充值订单；2代表分订单
        private int resouceType;//0 pos机含商品订单,1 pos机未知商品订单(直接收银),2 二维码收款订单,3 小程序自助点单订单,4 小程序社区门店版订单

        private String orderid;
        private int isSetPwd;

        private String refundgoods;
        private String refundNo;
        private String outOrderNo;
        private List<RefundOrder> refundData;

        public int getResouceType() {
            return resouceType;
        }

        public void setResouceType(int resouceType) {
            this.resouceType = resouceType;
        }

        public List<RefundOrder> getRefundData() {
            return refundData;
        }

        public void setRefundData(List<RefundOrder> refundData) {
            this.refundData = refundData;
        }

        public String getRefundgoods() {
            return refundgoods;
        }

        public void setRefundgoods(String refundgoods) {
            this.refundgoods = refundgoods;
        }

        public String getRefundNo() {
            return refundNo;
        }

        public void setRefundNo(String refundNo) {
            this.refundNo = refundNo;
        }

        public String getOutOrderNo() {
            return outOrderNo;
        }

        public void setOutOrderNo(String outOrderNo) {
            this.outOrderNo = outOrderNo;
        }

        public int getIsSetPwd() {
            return isSetPwd;
        }

        public void setIsSetPwd(int isSetPwd) {
            this.isSetPwd = isSetPwd;
        }
        private String isWxapp;

        public String getIsWxapp() {
            return isWxapp;
        }

        public void setIsWxapp(String isWxapp) {
            this.isWxapp = isWxapp;
        }

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        public int getOrderType() {
            return orderType;
        }

        public void setOrderType(int orderType) {
            this.orderType = orderType;
        }

        private String refundTime;

        public String getOrderNote() {
            return orderNote;
        }

        public void setOrderNote(String orderNote) {
            this.orderNote = orderNote;
        }

        public String getOrderNoOri() {
            return orderNoOri;
        }

        public void setOrderNoOri(String orderNoOri) {
            this.orderNoOri = orderNoOri;
        }

        public String getRoomName() {
            return roomName;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
        }

        public String getIsRefund() {
            return isRefund;
        }

        public void setIsRefund(String isRefund) {
            this.isRefund = isRefund;
        }

        public double getRefundMoney() {
            return refundMoney;
        }

        public void setRefundMoney(double refundMoney) {
            this.refundMoney = refundMoney;
        }

        public String getRefundOperator() {
            return refundOperator;
        }

        public void setRefundOperator(String refundOperator) {
            this.refundOperator = refundOperator;
        }

        public String getRefundTime() {
            return refundTime;
        }

        public void setRefundTime(String refundTime) {
            this.refundTime = refundTime;
        }

        /**
         * end :
         * name : 红塔山
         * num : 2
         * price : 3
         * start :
         * type : 0
         */

        private List<GoodsInfoBean> goodsInfo;
        private List<GroupInfoBean> groupInfo;
        private VoucherInfoBean voucherInfo;

        public List<GroupInfoBean> getGroupInfo() {
            return groupInfo;
        }

        public void setGroupInfo(List<GroupInfoBean> groupInfo) {
            this.groupInfo = groupInfo;
        }

        public VoucherInfoBean getVoucherInfo() {
            return voucherInfo;
        }

        public void setVoucherInfo(VoucherInfoBean voucherInfo) {
            this.voucherInfo = voucherInfo;
        }

        public String getDayOrderNum() {
            return dayOrderNum;
        }

        public void setDayOrderNum(String dayOrderNum) {
            this.dayOrderNum = dayOrderNum;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public double getFactPayMoney() {
            return factPayMoney;
        }

        public void setFactPayMoney(double factPayMoney) {
            this.factPayMoney = factPayMoney;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }
        public String getTransaction_id() {
            return transaction_id;
        }

        public void setTransaction_id(String transaction_id) {
            this.transaction_id = transaction_id;
        }

        public String getPayType() {
            return payType;
        }

        public void setPayType(String payType) {
            this.payType = payType;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public double getShouldPayMoney() {
            return shouldPayMoney;
        }

        public void setShouldPayMoney(double shouldPayMoney) {
            this.shouldPayMoney = shouldPayMoney;
        }

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getPayTime() {
            return payTime;
        }

        public void setPayTime(String payTime) {
            this.payTime = payTime;
        }

        public List<GoodsInfoBean> getGoodsInfo() {
            return goodsInfo;
        }

        public void setGoodsInfo(List<GoodsInfoBean> goodsInfo) {
            this.goodsInfo = goodsInfo;
        }

        public static class GoodsInfoBean implements Serializable {
            private String end;
            private String name;
            private String num;
            private double price;
            private String start;
            private String type;
            private String group_id;
            private int is_vip;
            private String size_name;
            private double ori_price;

            private String id;
            private double refund_num=0;
            private double refund_price=0;
            private String size_id;
            private String main_id="0";
            private double left_num=0;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public double getRefund_num() {
                return refund_num;
            }

            public void setRefund_num(double refund_num) {
                this.refund_num = refund_num;
            }

            public double getRefund_price() {
                return refund_price;
            }

            public void setRefund_price(double refund_price) {
                this.refund_price = refund_price;
            }

            public String getSize_id() {
                return size_id;
            }

            public void setSize_id(String size_id) {
                this.size_id = size_id;
            }

            public String getMain_id() {
                return main_id;
            }

            public void setMain_id(String main_id) {
                this.main_id = main_id;
            }

            public double getLeft_num() {
                return left_num;
            }

            public void setLeft_num(double left_num) {
                this.left_num = left_num;
            }

            public double getOri_price() {
                return ori_price;
            }

            public void setOri_price(double ori_price) {
                this.ori_price = ori_price;
            }

            public String getSize_name() {
                return size_name;
            }

            public void setSize_name(String size_name) {
                this.size_name = size_name;
            }

            public int getIs_vip() {
                return is_vip;
            }

            public void setIs_vip(int is_vip) {
                this.is_vip = is_vip;
            }

            public String getGroup_id() {
                return group_id;
            }

            public void setGroup_id(String group_id) {
                this.group_id = group_id;
            }

            public String getEnd() {
                return end;
            }

            public void setEnd(String end) {
                this.end = end;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public String getStart() {
                return start;
            }

            public void setStart(String start) {
                this.start = start;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }

        public static class VoucherInfoBean implements Serializable{

            private String type;
            private String title;
            private String money;

            public String getType() {
                return type;
            }
            public void setType(String type) {
                this.type = type;
            }
            public String getTitle() {
                return title;
            }
            public void setTitle(String title) {
                this.title = title;
            }
            public String getMoney() {
                return money;
            }
            public void setMoney(String money) {
                this.money = money;
            }
        }

        public static class GroupInfoBean implements Serializable{

            private String id;
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
