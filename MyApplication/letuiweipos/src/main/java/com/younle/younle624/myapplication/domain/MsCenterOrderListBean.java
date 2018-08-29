package com.younle.younle624.myapplication.domain;

import java.util.List;

/**
 * Created by bert_dong on 2017/6/17 0017.
 * 邮箱：18701038771@163.com
 */

public class MsCenterOrderListBean {

    /**
     * code : 200
     * msg : {"orderList":[{"addtime":"2017-06-19 14:38:01","dayOrderNum":"A1","goodsNum":"1","orderNo":"14834237887591","orderNote":"","orderType":"2","roomName":"","success":"1","totalFee":"5.00"},{"addtime":"2017-06-19 15:26:25","dayOrderNum":"A8","goodsNum":"0","orderNo":"1497857185181132","orderNote":"","orderType":"0","roomName":"","success":"1","totalFee":"0.03"}]}
     * returnsys : d*I#wq#dwqKq1X15BN4dCbBAmFmJ
     */

    private int code;
    private MsgBean msg;
    private String returnsys;

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

    public String getReturnsys() {
        return returnsys;
    }

    public void setReturnsys(String returnsys) {
        this.returnsys = returnsys;
    }

    public static class MsgBean {
        /**
         * addtime : 2017-06-19 14:38:01
         * dayOrderNum : A1
         * goodsNum : 1
         * orderNo : 14834237887591
         * orderNote :
         * orderType : 2
         * roomName :
         * success : 1
         * totalFee : 5.00
         */

        private List<OrderListBean> orderList;

        public List<OrderListBean> getOrderList() {
            return orderList;
        }

        public void setOrderList(List<OrderListBean> orderList) {
            this.orderList = orderList;
        }

        public static class OrderListBean {
            private String addtime;
            private String dayOrderNum;
            private String goodsNum;
            private String orderNo;
            private String orderNote;
            private String orderType;
            private String roomName;
            private String success;
            private String totalFee;
            private String isRechargeLog;
            private String logId;
            private String delivery_status;//配送状态，0 带配送，1 已配送

            public String getDelivery_status() {
                return delivery_status;
            }

            public void setDelivery_status(String delivery_status) {
                this.delivery_status = delivery_status;
            }

            public String getLogId() {
                return logId;
            }

            public void setLogId(String logId) {
                this.logId = logId;
            }

            public String getIsRechargeLog() {
                return isRechargeLog;
            }

            public void setIsRechargeLog(String isRechargeLog) {
                this.isRechargeLog = isRechargeLog;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public String getDayOrderNum() {
                return dayOrderNum;
            }

            public void setDayOrderNum(String dayOrderNum) {
                this.dayOrderNum = dayOrderNum;
            }

            public String getGoodsNum() {
                return goodsNum;
            }

            public void setGoodsNum(String goodsNum) {
                this.goodsNum = goodsNum;
            }

            public String getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(String orderNo) {
                this.orderNo = orderNo;
            }

            public String getOrderNote() {
                return orderNote;
            }

            public void setOrderNote(String orderNote) {
                this.orderNote = orderNote;
            }

            public String getOrderType() {
                return orderType;
            }

            public void setOrderType(String orderType) {
                this.orderType = orderType;
            }

            public String getRoomName() {
                return roomName;
            }

            public void setRoomName(String roomName) {
                this.roomName = roomName;
            }

            public String getSuccess() {
                return success;
            }

            public void setSuccess(String success) {
                this.success = success;
            }

            public String getTotalFee() {
                return totalFee;
            }

            public void setTotalFee(String totalFee) {
                this.totalFee = totalFee;
            }
        }
    }
}
