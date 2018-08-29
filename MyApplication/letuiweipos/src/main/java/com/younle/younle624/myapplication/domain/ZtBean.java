package com.younle.younle624.myapplication.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bert_dong on 2018/7/10 0010.
 * 邮箱：18701038771@163.com
 */

public class ZtBean implements Serializable {


        /**
         * goods_list : [{"addtime":"1531186029","cardprice":"31.20","cardtype":"MEMBER_CASH","goods_name":"绿豆糕","goods_num":"1.000","goods_price":"6.00","is_vip":"1","order_no":"1531186029869251","orderinfo":"","pay_type":"0","payment":"19.80","paytime":"0","query_num":"1","total_fee":"51.00","vip_discount":"0.00"},{"addtime":"1531186029","cardprice":"31.20","cardtype":"MEMBER_CASH","goods_name":"测试多规格123","goods_num":"2.000","goods_price":"6.00","is_vip":"1","order_no":"1531186029869251","orderinfo":"","pay_type":"0","payment":"19.80","paytime":"0","query_num":"1","total_fee":"51.00","vip_discount":"0.00"},{"addtime":"1531186029","cardprice":"31.20","cardtype":"MEMBER_CASH","goods_name":"可比克","goods_num":"1.000","goods_price":"1.20","is_vip":"1","order_no":"1531186029869251","orderinfo":"","pay_type":"0","payment":"19.80","paytime":"0","query_num":"1","total_fee":"51.00","vip_discount":"0.00"},{"addtime":"1531186029","cardprice":"31.20","cardtype":"MEMBER_CASH","goods_name":"测试4","goods_num":"1.000","goods_price":"0.60","is_vip":"1","order_no":"1531186029869251","orderinfo":"","pay_type":"0","payment":"19.80","paytime":"0","query_num":"1","total_fee":"51.00","vip_discount":"0.00"}]
         * money : {"payment":"19.80","price":"原价51.00元,会员优惠价:-¥31.20","total_fee":"51.00"}
         * order : {"addtime":"2018-07-10 09:27:09","order_no":"1531186029869251","pay_type":"微信支付","paytime":"1970-01-01 08:00:00"}
         * order_title : 订单C1
         * orderinfo :
         */
        private MoneyBean money;
        private OrderBean order;
        private String order_title;
        private String orderinfo;
        private List<GoodsListBean> goods_list;
        public MoneyBean getMoney() {
            return money;
        }

        public void setMoney(MoneyBean money) {
            this.money = money;
        }

        public OrderBean getOrder() {
            return order;
        }

        public void setOrder(OrderBean order) {
            this.order = order;
        }

        public String getOrder_title() {
            return order_title;
        }

        public void setOrder_title(String order_title) {
            this.order_title = order_title;
        }

        public String getOrderinfo() {
            return orderinfo;
        }

        public void setOrderinfo(String orderinfo) {
            this.orderinfo = orderinfo;
        }

        public List<GoodsListBean> getGoods_list() {
            return goods_list;
        }

        public void setGoods_list(List<GoodsListBean> goods_list) {
            this.goods_list = goods_list;
        }

        public static class MoneyBean implements Serializable{
            /**
             * payment : 19.80
             * price : 原价51.00元,会员优惠价:-¥31.20
             * total_fee : 51.00
             */

            private String payment;
            private String price;
            private String total_fee;

            public String getPayment() {
                return payment;
            }

            public void setPayment(String payment) {
                this.payment = payment;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getTotal_fee() {
                return total_fee;
            }

            public void setTotal_fee(String total_fee) {
                this.total_fee = total_fee;
            }

            @Override
            public String toString() {
                return "MoneyBean{" +
                        "payment='" + payment + '\'' +
                        ", price='" + price + '\'' +
                        ", total_fee='" + total_fee + '\'' +
                        '}';
            }
        }

        public static class OrderBean implements Serializable{
            /**
             * addtime : 2018-07-10 09:27:09
             * order_no : 1531186029869251
             * pay_type : 微信支付
             * paytime : 1970-01-01 08:00:00
             */

            private String addtime;
            private String order_no;
            private String pay_type;
            private String paytime;
            private int deliver_status;
            private int is_refund;
            private String outOrderNo;

            public String getOutOrderNo() {
                return outOrderNo;
            }

            public void setOutOrderNo(String outOrderNo) {
                this.outOrderNo = outOrderNo;
            }

            public int getDeliver_status() {
                return deliver_status;
            }

            public void setDeliver_status(int deliver_status) {
                this.deliver_status = deliver_status;
            }

            public int getIs_refund() {
                return is_refund;
            }

            public void setIs_refund(int is_refund) {
                this.is_refund = is_refund;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public String getOrder_no() {
                return order_no;
            }

            public void setOrder_no(String order_no) {
                this.order_no = order_no;
            }

            public String getPay_type() {
                return pay_type;
            }

            public void setPay_type(String pay_type) {
                this.pay_type = pay_type;
            }

            public String getPaytime() {
                return paytime;
            }

            public void setPaytime(String paytime) {
                this.paytime = paytime;
            }

            @Override
            public String toString() {
                return "OrderBean{" +
                        "addtime='" + addtime + '\'' +
                        ", order_no='" + order_no + '\'' +
                        ", pay_type='" + pay_type + '\'' +
                        ", paytime='" + paytime + '\'' +
                        '}';
            }
        }

        public static class GoodsListBean implements Serializable {
            /**
             * addtime : 1531186029
             * cardprice : 31.20
             * cardtype : MEMBER_CASH
             * goods_name : 绿豆糕
             * goods_num : 1.000
             * goods_price : 6.00
             * is_vip : 1
             * order_no : 1531186029869251
             * orderinfo :
             * pay_type : 0
             * payment : 19.80
             * paytime : 0
             * query_num : 1
             * total_fee : 51.00
             * vip_discount : 0.00
             */

            private String goods_name;
            private double goods_num;
            private double goods_price;
            private int is_vip;

            public String getGoods_name() {
                return goods_name;
            }

            public void setGoods_name(String goods_name) {
                this.goods_name = goods_name;
            }

            public double getGoods_num() {
                return goods_num;
            }

            public void setGoods_num(double goods_num) {
                this.goods_num = goods_num;
            }

            public double getGoods_price() {
                return goods_price;
            }

            public void setGoods_price(double goods_price) {
                this.goods_price = goods_price;
            }

            public int getIs_vip() {
                return is_vip;
            }

            public void setIs_vip(int is_vip) {
                this.is_vip = is_vip;
            }

            @Override
            public String toString() {
                return "GoodsListBean{" +
                        "goods_name='" + goods_name + '\'' +
                        ", goods_num='" + goods_num + '\'' +
                        ", goods_price='" + goods_price + '\'' +
                        ", is_vip='" + is_vip + '\'' +
                        '}';
            }
        }

    @Override
    public String toString() {
        return "ZtBean{" +
                "money=" + money +
                ", order=" + order +
                ", order_title='" + order_title + '\'' +
                ", orderinfo='" + orderinfo + '\'' +
                ", goods_list=" + goods_list +
                '}';
    }
}
