package com.younle.younle624.myapplication.domain;

import java.util.List;

/**
 * Created by bert_dong on 2017/6/13 0013.
 * 邮箱：18701038771@163.com
 */

public class AppLetReceiveBean {

    /**
     * addTime : 2017-06-12 17:15:33
     * dayOrderNum : A3
     * discount : 0.1
     * factPayMoney : 1950.01
     * goodsInfo : [{"end":"","name":"拌黄瓜","num":"3.0","price":50.01,"size_name":"","start":"","type":"1"},{"end":"","name":"捷豹-低配","num":"3.0","price":120.01,"size_name":"低配","start":"","type":"1"},{"end":"","name":"捷豹-中配","num":"3.0","price":180.01,"size_name":"中配","start":"","type":"1"},{"end":"","name":"路虎-低配","num":"3.0","price":120.01,"size_name":"低配","start":"","type":"1"},{"end":"","name":"路虎-中配","num":"3.0","price":180.01,"size_name":"中配","start":"","type":"1"}]
     * isCancel : 0
     * isOutOfLimit : 2017-06-12 17:15:33
     * isRefund : 0
     * orderNo : 1497333479486113
     * orderNoOri : 149725893345635
     * orderNote : 无
     * orderType : 2017-06-12 17:15:33
     * payStatus : 2017-06-12 17:15:33
     * payTime : 2017-06-13 13:58:00
     * payType : 现金支付
     * refundMoney : 0.01
     * refundOperator :
     * refundTime : 1970-01-01 08:00:00
     * remark : 无
     * roomName : 桌号：A01 （这样的形式返回的）
     * shouldPayMoney : 1950.01
     * ticketInfo : {"balance_rules":[{"money":"0.01","song":"10","zong":110.01}],"discount_rules":[{"money":"1.01","rate":"8"}],"membershipNum":"","rechargeMoney":0.01,"storeWechat":""}
     * transaction_id :
     */

    private String addTime;
    private String dayOrderNum;
    private double discount;
    private double factPayMoney;
    private String isCancel;
    private String isOutOfLimit;
    private String isRefund;
    private String orderNo;
    private String orderNoOri;
    private String orderNote;
    private int orderType;
    private String payStatus;//0未支付 1支付
    private String payTime;
    private String payType;
    private double refundMoney;
    private String refundOperator;
    private String refundTime;
    private String remark;
    private String roomName;
    private double shouldPayMoney;
    private String unionid;
    private String deliveryTime;
    private String cneeName;
    private String cneePhone;
    private String cneeAddress;

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getCneeName() {
        return cneeName;
    }

    public void setCneeName(String cneeName) {
        this.cneeName = cneeName;
    }

    public String getCneePhone() {
        return cneePhone;
    }

    public void setCneePhone(String cneePhone) {
        this.cneePhone = cneePhone;
    }

    public String getCneeAddress() {
        return cneeAddress;
    }

    public void setCneeAddress(String cneeAddress) {
        this.cneeAddress = cneeAddress;
    }

    /**
     * balance_rules : [{"money":"0.01","song":"10","zong":110.01}]
     * discount_rules : [{"money":"1.01","rate":"8"}]
     * membershipNum :
     * rechargeMoney : 0.01
     * storeWechat :
     */

    private TicketInfoBean ticketInfo;
    private String transaction_id;
    private String deliveryCost;
    private int deliveryStatus;//0未配送，1已配送
    private String zt_code;

    public int getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(int deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getZt_code() {
        return zt_code;
    }

    public void setZt_code(String zt_code) {
        this.zt_code = zt_code;
    }

    /**
     * end :
     * name : 拌黄瓜
     * num : 3.0
     * price : 50.01
     * size_name :
     * start :
     * type : 1
     */

    private List<GoodsInfoBean> goodsInfo;
    private List<groupInfoBean> groupInfo;

    public String getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(String deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public List<groupInfoBean> getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(List<groupInfoBean> groupInfo) {
        this.groupInfo = groupInfo;
    }

    private VoucherInfoBean voucherInfo;

    public VoucherInfoBean getVoucherInfo() {
        return voucherInfo;
    }

    public void setVoucherInfo(VoucherInfoBean voucherInfo) {
        this.voucherInfo = voucherInfo;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
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

    public String getIsCancel() {
        return isCancel;
    }

    public void setIsCancel(String isCancel) {
        this.isCancel = isCancel;
    }

    public String getIsOutOfLimit() {
        return isOutOfLimit;
    }

    public void setIsOutOfLimit(String isOutOfLimit) {
        this.isOutOfLimit = isOutOfLimit;
    }

    public String getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(String isRefund) {
        this.isRefund = isRefund;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderNoOri() {
        return orderNoOri;
    }

    public void setOrderNoOri(String orderNoOri) {
        this.orderNoOri = orderNoOri;
    }

    public String getOrderNote() {
        return orderNote;
    }

    public void setOrderNote(String orderNote) {
        this.orderNote = orderNote;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public double getShouldPayMoney() {
        return shouldPayMoney;
    }

    public void setShouldPayMoney(double shouldPayMoney) {
        this.shouldPayMoney = shouldPayMoney;
    }

    public TicketInfoBean getTicketInfo() {
        return ticketInfo;
    }

    public void setTicketInfo(TicketInfoBean ticketInfo) {
        this.ticketInfo = ticketInfo;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public List<GoodsInfoBean> getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(List<GoodsInfoBean> goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    public static class TicketInfoBean {
        private String membershipNum;
        private double rechargeMoney;
        private String storeWechat;
        private String url;

        /**
         * money : 0.01
         * song : 10
         * zong : 110.01
         */

        private List<BalanceRulesBean> balance_rules;
        /**
         * money : 1.01
         * rate : 8
         */

        private List<DiscountRulesBean> discount_rules;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMembershipNum() {
            return membershipNum;
        }

        public void setMembershipNum(String membershipNum) {
            this.membershipNum = membershipNum;
        }

        public double getRechargeMoney() {
            return rechargeMoney;
        }

        public void setRechargeMoney(double rechargeMoney) {
            this.rechargeMoney = rechargeMoney;
        }

        public String getStoreWechat() {
            return storeWechat;
        }

        public void setStoreWechat(String storeWechat) {
            this.storeWechat = storeWechat;
        }

        public List<BalanceRulesBean> getBalance_rules() {
            return balance_rules;
        }

        public void setBalance_rules(List<BalanceRulesBean> balance_rules) {
            this.balance_rules = balance_rules;
        }

        public List<DiscountRulesBean> getDiscount_rules() {
            return discount_rules;
        }

        public void setDiscount_rules(List<DiscountRulesBean> discount_rules) {
            this.discount_rules = discount_rules;
        }

        public static class BalanceRulesBean {
            private String money;
            private String song;
            private double zong;

            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
            }

            public String getSong() {
                return song;
            }

            public void setSong(String song) {
                this.song = song;
            }

            public double getZong() {
                return zong;
            }

            public void setZong(double zong) {
                this.zong = zong;
            }
        }

        public static class DiscountRulesBean {
            private String money;
            private String rate;

            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
            }

            public String getRate() {
                return rate;
            }

            public void setRate(String rate) {
                this.rate = rate;
            }
        }
    }

    public static class GoodsInfoBean {
        private String end;
        private String name;
        private String num;
        private double price;
        private String size_name;
        private String start;
        private String type;//区分未知商品，自助点单商品，房间
        private String group_id;
        private double ori_price;
        private int is_vip;//1.是会员价商品，0.不是
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public double getOri_price() {
            return ori_price;
        }

        public void setOri_price(double ori_price) {
            this.ori_price = ori_price;
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

        public String getSize_name() {
            return size_name;
        }

        public void setSize_name(String size_name) {
            this.size_name = size_name;
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

    public static class VoucherInfoBean{

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

    public static class groupInfoBean{

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
