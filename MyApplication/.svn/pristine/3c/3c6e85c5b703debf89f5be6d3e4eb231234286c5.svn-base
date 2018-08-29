package com.younle.younle624.myapplication.domain;

import java.util.List;

/**
 * Created by 我是奋斗 on 2016/6/23.
 * 微信/e-mail:tt090423@126.com
 */
public class UnPayBean {


    /**
     * auth : ["1","3","5"]
     * code : 200
     * msg : [{"addtime":"2016-11-21","orderlist":[{"addtime":"2016-11-21","goods_num":"0","id":"22237","order_no":"14797114550991","order_time":"14:35","query_num":"1","room_name":"房间206","total_fee":"0.00"},{"addtime":"2016-11-21","goods_num":"0","id":"22239","order_no":"14797178986871","order_time":"16:00","query_num":"2","room_name":"总-zx","total_fee":"0.00"},{"addtime":"2016-11-21","goods_num":"0","id":"22241","order_no":"14797209908351","order_time":"17:32","query_num":"3","room_name":"包间（101）","total_fee":"0.00"}]},{"addtime":"2016-11-18","orderlist":[{"addtime":"2016-11-18","goods_num":"0","id":"22232","order_no":"14794623189791","order_time":"17:19","query_num":"1","room_name":"1号桌","total_fee":"0.00"},{"addtime":"2016-11-18","goods_num":"0","id":"22233","order_no":"14794623816461","order_time":"17:22","query_num":"2","room_name":"标准","total_fee":"0.00"}]},{"addtime":"2016-11-14","orderlist":[{"addtime":"2016-11-14","goods_num":"0","id":"22211","order_no":"147909104570576","order_time":"10:27","query_num":"1","room_name":"1号桌","total_fee":"0.00"},{"addtime":"2016-11-14","goods_num":"0","id":"22212","order_no":"147909104921076","order_time":"10:29","query_num":"2","room_name":"1号桌","total_fee":"0.00"},{"addtime":"2016-11-14","goods_num":"0","id":"22213","order_no":"14791019386671","order_time":"13:59","query_num":"3","room_name":"1号桌","total_fee":"0.00"},{"addtime":"2016-11-14","goods_num":"0","id":"22214","order_no":"14791117231091","order_time":"16:03","query_num":"4","room_name":"1号桌","total_fee":"0.00"},{"addtime":"2016-11-14","goods_num":"0","id":"22216","order_no":"14791202526431","order_time":"18:13","query_num":"5","room_name":"1号桌","total_fee":"0.00"}]}]
     */

    private int code;
    private List<String> auth;
    /**
     * addtime : 2016-11-21
     * orderlist : [{"addtime":"2016-11-21","goods_num":"0","id":"22237","order_no":"14797114550991","order_time":"14:35","query_num":"1","room_name":"房间206","total_fee":"0.00"},{"addtime":"2016-11-21","goods_num":"0","id":"22239","order_no":"14797178986871","order_time":"16:00","query_num":"2","room_name":"总-zx","total_fee":"0.00"},{"addtime":"2016-11-21","goods_num":"0","id":"22241","order_no":"14797209908351","order_time":"17:32","query_num":"3","room_name":"包间（101）","total_fee":"0.00"}]
     */

    private List<MsgBean> msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<String> getAuth() {
        return auth;
    }

    public void setAuth(List<String> auth) {
        this.auth = auth;
    }

    public List<MsgBean> getMsg() {
        return msg;
    }

    public void setMsg(List<MsgBean> msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        private String addtime;
        /**
         * addtime : 2016-11-21
         * goods_num : 0
         * id : 22237
         * order_no : 14797114550991
         * order_time : 14:35
         * query_num : 1
         * room_name : 房间206
         * total_fee : 0.00
         */

        private List<OrderlistBean> orderlist;

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public List<OrderlistBean> getOrderlist() {
            return orderlist;
        }

        public void setOrderlist(List<OrderlistBean> orderlist) {
            this.orderlist = orderlist;
        }

        public static class OrderlistBean {
            private String addtime;
            private String goods_num;
            private String id;
            private String order_no;
            private String order_time;
            private String query_num;
            private String room_name;
            private String total_fee;
            private String payment;
            private String room_type;

            public String getRoom_type() {
                return room_type;
            }
            public void setRoom_type(String room_type) {
                this.room_type = room_type;
            }
            public String getAddtime() {
                return addtime;
            }
            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }
            public String getGoods_num() {
                return goods_num;
            }
            public void setGoods_num(String goods_num) {
                this.goods_num = goods_num;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getOrder_no() {
                return order_no;
            }

            public void setOrder_no(String order_no) {
                this.order_no = order_no;
            }

            public String getOrder_time() {
                return order_time;
            }

            public void setOrder_time(String order_time) {
                this.order_time = order_time;
            }

            public String getQuery_num() {
                return query_num;
            }

            public void setQuery_num(String query_num) {
                this.query_num = query_num;
            }

            public String getRoom_name() {
                return room_name;
            }

            public void setRoom_name(String room_name) {
                this.room_name = room_name;
            }

            public String getTotal_fee() {
                return total_fee;
            }

            public void setTotal_fee(String total_fee) {
                this.total_fee = total_fee;
            }

            public String getPayment() {
                return payment;
            }

            public void setPayment(String payment) {
                this.payment = payment;
            }
        }
    }
}
