package com.younle.younle624.myapplication.domain;

import java.util.List;

/**
 * Created by 我是奋斗 on 2016/7/4.
 * 微信/e-mail:tt090423@126.com
 */
public class EntityOrderBean {


    /**
     * goodsName : [{"id":"0","name":"全部商品"},{"id":"12","name":"替换商品1"},{"id":"97","name":"123123123"},{"id":"99","name":"测试实物"},{"id":"114","name":"2323实物"},{"id":"124","name":"重写实物商品规格测试---别动"}]
      list : [{"buyNum":2,"date":"2016-10-21","status":"订单完成 ","orderAmount":210,"income":3240,"goodsName":"王麻子特制大麻子","orderId":"9"},{"buyNum":1,"date":"2016-10-21","status":"订单完成 ","orderAmount":1010,"income":3240,"goodsName":"2","orderId":"3"},{"buyNum":1,"date":"2016-10-21","status":"订单完成 ","orderAmount":1010,"income":3240,"goodsName":"2","orderId":"5"},{"buyNum":1,"date":"2016-10-21","status":"订单完成 ","orderAmount":1010,"income":3240,"goodsName":"三件套","orderId":"4"},{"buyNum":2,"date":"2016-10-05","status":"订单完成 ","orderAmount":7,"income":49,"goodsName":"Anne测试2","orderId":"15"},{"buyNum":2,"date":"2016-10-05","status":"订单完成 ","orderAmount":7,"income":49,"goodsName":"Anne测试1","orderId":"14"},{"buyNum":2,"date":"2016-10-05","status":"订单完成 ","orderAmount":7,"income":49,"goodsName":"Anne测试2","orderId":"13"},{"buyNum":2,"date":"2016-10-05","status":"订单完成 ","orderAmount":7,"income":49,"goodsName":"Anne测试1","orderId":"12"},{"buyNum":2,"date":"2016-10-05","status":"订单完成 ","orderAmount":7,"income":49,"goodsName":"Anne测试1","orderId":"11"},{"buyNum":2,"date":"2016-10-05","status":"订单完成 ","orderAmount":7,"income":49,"goodsName":"Anne测试1","orderId":"10"},{"buyNum":2,"date":"2016-10-05","status":"订单完成 ","orderAmount":7,"income":49,"goodsName":"Anne测试1","orderId":"16"}]
     * income : 3289
     * saled : 11
     */

    private double income;
    private int saled;
    /**
     * id : 0
     * name : 全部商品
     */

    private List<GoodsNameBean> goodsName;
    /**
     * buyNum : 2
     * date : 2016-10-21
     * status : 订单完成
     * orderAmount : 210
     * income : 3240
     * goodsName : 王麻子特制大麻子
     * orderId : 9
     */

    private List<ListBean> list;

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public int getSaled() {
        return saled;
    }

    public void setSaled(int saled) {
        this.saled = saled;
    }

    public List<GoodsNameBean> getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(List<GoodsNameBean> goodsName) {
        this.goodsName = goodsName;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class GoodsNameBean {
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

    public static class ListBean {
        private int buyNum;
        private String date;
        private String status;
        private double orderAmount;
        private double income;
        private String goodsName;
        private String orderId;

        public int getBuyNum() {
            return buyNum;
        }

        public void setBuyNum(int buyNum) {
            this.buyNum = buyNum;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public double getOrderAmount() {
            return orderAmount;
        }

        public void setOrderAmount(double orderAmount) {
            this.orderAmount = orderAmount;
        }

        public double getIncome() {
            return income;
        }

        public void setIncome(double income) {
            this.income = income;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }
    }
}
