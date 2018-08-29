package com.yongle.letuiweipad.domain.unpayorder;

import java.util.List;

/**
 * Created by 我是奋斗 on 2016/6/23.
 * 微信/e-mail:tt090423@126.com
 */
public class UnPayBean {


    /**
     * msg : [{"addtime":"2017-09-07 13:50:56","goods_num":"4.000","id":"5617","payment":"20.00","query_num":"A1","room_name":"","room_type":"0"},{"addtime":"2017-08-22 13:50:12","goods_num":"5.000","id":"5280","payment":"50000000.00","query_num":"A9","room_name":"","room_type":"0"},{"addtime":"2017-08-21 17:48:00","goods_num":"1024.000","id":"5259","payment":"902328.07","query_num":"A48","room_name":"","room_type":"0"},{"addtime":"2017-08-21 17:44:53","goods_num":"5869.000","id":"5258","payment":"5211672.00","query_num":"A47","room_name":"","room_type":"0"},{"addtime":"2017-08-07 11:20:53","goods_num":"7.000","id":"4939","payment":"207.61","query_num":"A1","room_name":"","room_type":"0"},{"addtime":"2017-08-02 16:26:41","goods_num":"1.000","id":"4864","payment":"120.00","query_num":"A8","room_name":"","room_type":"0"},{"addtime":"2017-07-08 10:28:32","goods_num":"4.000","id":"4374","payment":"0.04","query_num":"A1","room_name":"","room_type":"0"},{"addtime":"2017-07-07 14:53:25","goods_num":"21.000","id":"4305","payment":"482.10","query_num":"A20","room_name":"","room_type":"0"},{"addtime":"2017-03-30 18:44:02","goods_num":"2.000","id":"1898","payment":"20.00","query_num":"A41","room_name":"","room_type":"0"},{"addtime":"2017-03-30 18:34:59","goods_num":"2.000","id":"1893","payment":"20.00","query_num":"A40","room_name":"","room_type":"0"}]
     * num : 196
     * returnsys : dYddCC%wFFoFgLz2aYj1xWpBWntE
     */

    private String num;
    private List<MsgBean> msg;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
    public List<MsgBean> getMsg() {
        return msg;
    }

    public void setMsg(List<MsgBean> msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        /**
         * addtime : 2017-09-07 13:50:56
         * goods_num : 4.000
         * id : 5617
         * payment : 20.00
         * query_num : A1
         * room_name :
         * room_type : 0
         */

        private String addtime;
        private String goods_num;
        private String id;
        private String payment;
        private String query_num;
        private String room_name;
        private String room_type;
        private boolean selected=false;

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
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

        public String getPayment() {
            return payment;
        }

        public void setPayment(String payment) {
            this.payment = payment;
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

        public String getRoom_type() {
            return room_type;
        }

        public void setRoom_type(String room_type) {
            this.room_type = room_type;
        }

        @Override
        public String toString() {
            return "MsgBean{" +
                    "addtime='" + addtime + '\'' +
                    ", goods_num='" + goods_num + '\'' +
                    ", id='" + id + '\'' +
                    ", payment='" + payment + '\'' +
                    ", query_num='" + query_num + '\'' +
                    ", room_name='" + room_name + '\'' +
                    ", room_type='" + room_type + '\'' +
                    ", selected=" + selected +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UnPayBean{" +
                "num='" + num + '\'' +
                ", msg=" + msg +
                '}';
    }
}
