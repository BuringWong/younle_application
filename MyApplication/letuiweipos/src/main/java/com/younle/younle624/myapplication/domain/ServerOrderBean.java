package com.younle.younle624.myapplication.domain;

import java.util.List;

/**
 * Created by 我是奋斗 on 2016/6/21.
 * 微信/e-mail:tt090423@126.com
 */
public class ServerOrderBean {


    /**
     * code : 200
     * msg : {"income":239.63,"saled":246,"list":[{"orderId":"7837","goodsName":"测试server","goodsPrice":"1.00","endTime":"2016-09-06 08:34:34","status":"未使用","date":"2016-09-06","income":2},{"orderId":"7089","goodsName":"测试server","goodsPrice":"1.00","endTime":"2016-09-14 04:39:47","status":"未使用","date":"2016-09-06","income":2},{"orderId":"1974","goodsName":"测试server","goodsPrice":"1.00","endTime":"2016-09-07 02:59:21","status":"已过期","date":"2016-09-05","income":16.78},{"orderId":"9076","goodsName":"测试server","goodsPrice":"1.00","endTime":"2016-09-07 19:03:28","status":"已核销（busy肯德基（崇文门店））","date":"2016-09-05","income":16.78},{"orderId":"6653","goodsName":"测试server","goodsPrice":"1.00","endTime":"2016-09-09 02:48:09","status":"已核销（busy肯德基（崇文门店））","date":"2016-09-05","income":16.78},{"orderId":"9306","goodsName":"测试2","goodsPrice":"1.00","endTime":"2016-09-08 16:56:06","status":"未使用","date":"2016-09-05","income":16.78},{"orderId":"1448","goodsName":"测试server","goodsPrice":"1.00","endTime":"2016-09-13 02:00:08","status":"未使用","date":"2016-09-05","income":16.78},{"orderId":"3995","goodsName":"测试6","goodsPrice":"0.80","endTime":"2016-09-09 09:12:37","status":"已核销（busy肯德基（崇文门店）","date":"2016-09-05","income":16.78},{"orderId":"7708","goodsName":"测试2","goodsPrice":"1.00","endTime":"2016-09-14 13:17:11","status":"未使用","date":"2016-09-05","income":16.78},{"orderId":"5686","goodsName":"测试server","goodsPrice":"1.00","endTime":"2016-09-06 03:38:58","status":"未使用","date":"2016-09-05","income":16.78},{"orderId":"80","goodsName":"测试server","goodsPrice":"1.00","endTime":"2016-09-05 09:43:05","status":"已核销（busy肯德基（崇文门店））","date":"2016-09-05","income":16.78},{"orderId":"2987","goodsName":"测试6","goodsPrice":"0.80","endTime":"2016-09-10 21:20:27","status":"已核销（busy肯德基（崇文门店）","date":"2016-09-05","income":16.78},{"orderId":"4045","goodsName":"测试server","goodsPrice":"1.00","endTime":"2016-09-06 06:51:10","status":"已核销（busy肯德基（崇文门店））","date":"2016-09-05","income":16.78},{"orderId":"2454","goodsName":"测试6","goodsPrice":"0.80","endTime":"2016-09-06 02:15:13","status":"已核销（busy肯德基（崇文门店）","date":"2016-09-05","income":16.78},{"orderId":"4219","goodsName":"测试6","goodsPrice":"0.80","endTime":"2016-09-10 05:51:32","status":"已核销（busy肯德基（崇文门店）","date":"2016-09-05","income":16.78}],"goodsName":[{"id":"0","name":"全部商品"},{"id":"150","name":"服务"}]}
     */

    private int code;
    /**
     * income : 239.63
     * saled : 246
     * list : [{"orderId":"7837","goodsName":"测试server","goodsPrice":"1.00","endTime":"2016-09-06 08:34:34","status":"未使用","date":"2016-09-06","income":2},{"orderId":"7089","goodsName":"测试server","goodsPrice":"1.00","endTime":"2016-09-14 04:39:47","status":"未使用","date":"2016-09-06","income":2},{"orderId":"1974","goodsName":"测试server","goodsPrice":"1.00","endTime":"2016-09-07 02:59:21","status":"已过期","date":"2016-09-05","income":16.78},{"orderId":"9076","goodsName":"测试server","goodsPrice":"1.00","endTime":"2016-09-07 19:03:28","status":"已核销（busy肯德基（崇文门店））","date":"2016-09-05","income":16.78},{"orderId":"6653","goodsName":"测试server","goodsPrice":"1.00","endTime":"2016-09-09 02:48:09","status":"已核销（busy肯德基（崇文门店））","date":"2016-09-05","income":16.78},{"orderId":"9306","goodsName":"测试2","goodsPrice":"1.00","endTime":"2016-09-08 16:56:06","status":"未使用","date":"2016-09-05","income":16.78},{"orderId":"1448","goodsName":"测试server","goodsPrice":"1.00","endTime":"2016-09-13 02:00:08","status":"未使用","date":"2016-09-05","income":16.78},{"orderId":"3995","goodsName":"测试6","goodsPrice":"0.80","endTime":"2016-09-09 09:12:37","status":"已核销（busy肯德基（崇文门店）","date":"2016-09-05","income":16.78},{"orderId":"7708","goodsName":"测试2","goodsPrice":"1.00","endTime":"2016-09-14 13:17:11","status":"未使用","date":"2016-09-05","income":16.78},{"orderId":"5686","goodsName":"测试server","goodsPrice":"1.00","endTime":"2016-09-06 03:38:58","status":"未使用","date":"2016-09-05","income":16.78},{"orderId":"80","goodsName":"测试server","goodsPrice":"1.00","endTime":"2016-09-05 09:43:05","status":"已核销（busy肯德基（崇文门店））","date":"2016-09-05","income":16.78},{"orderId":"2987","goodsName":"测试6","goodsPrice":"0.80","endTime":"2016-09-10 21:20:27","status":"已核销（busy肯德基（崇文门店）","date":"2016-09-05","income":16.78},{"orderId":"4045","goodsName":"测试server","goodsPrice":"1.00","endTime":"2016-09-06 06:51:10","status":"已核销（busy肯德基（崇文门店））","date":"2016-09-05","income":16.78},{"orderId":"2454","goodsName":"测试6","goodsPrice":"0.80","endTime":"2016-09-06 02:15:13","status":"已核销（busy肯德基（崇文门店）","date":"2016-09-05","income":16.78},{"orderId":"4219","goodsName":"测试6","goodsPrice":"0.80","endTime":"2016-09-10 05:51:32","status":"已核销（busy肯德基（崇文门店）","date":"2016-09-05","income":16.78}]
     * goodsName : [{"id":"0","name":"全部商品"},{"id":"150","name":"服务"}]
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

    public static class MsgBean {
        private double income;
        private int saled;
        /**
         * orderId : 7837
         * goodsName : 测试server
         * goodsPrice : 1.00
         * endTime : 2016-09-06 08:34:34
         * status : 未使用
         * date : 2016-09-06
         * income : 2
         */

        private List<ListBean> list;
        /**
         * id : 0
         * name : 全部商品
         */

        private List<GoodsNameBean> goodsName;

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

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public List<GoodsNameBean> getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(List<GoodsNameBean> goodsName) {
            this.goodsName = goodsName;
        }

        public static class ListBean {
            private String orderId;
            private String goodsName;
            private String goodsPrice;
            private String endTime;
            private String status;
            private String date;
            private double income;

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public String getGoodsName() {
                return goodsName;
            }

            public void setGoodsName(String goodsName) {
                this.goodsName = goodsName;
            }

            public String getGoodsPrice() {
                return goodsPrice;
            }

            public void setGoodsPrice(String goodsPrice) {
                this.goodsPrice = goodsPrice;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public double getIncome() {
                return income;
            }

            public void setIncome(double income) {
                this.income = income;
            }
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
    }
}
