package com.younle.younle624.myapplication.domain;

import java.util.List;

/**
 * Created by bert_dong on 2017/5/8 0008.
 * 邮箱：18701038771@163.com
 */

public class UnpayDetailpagerBean {


    /**
     * auth : ["1","2","3","4","5","6","7"]
     * code : 200
     * msg : {"addtime":"1493974272","goods_num":"3.0","order_goods":[{"goods_name":"果粒橙","goods_num":4,"goods_unit":"瓶","group_id":"0","group_name":"","id":"1","total_fee":34,"size_list":[{"size_id":"2","size_name":"3L大瓶","size_num":1,"size_price":10},{"size_id":"3","size_name":"1L中瓶","size_num":3,"size_price":8}]}],"order_no":"149397427281034","order_rooms":[{"end_time":"0","id":"1","is_billing":"1","room_id_room":"740","roomname":"圆1","start_time":"1493974200","true_income":"2299.35","use_timelength":"64小时53分钟"}],"orderid":2780,"payment":35.5,"query_num":"A44","remark":"","room":{"end_time":1494207780,"id":"740","true_income":"1.5","use_timelength":1494207780},"total_fee":35.5,"vip":0}
     * returnsys : d*I*%F#w%*LCM5NbEmMxDqbrm6n5
     */

    private int code;
    /**
     * addtime : 1493974272
     * goods_num : 3.0
     * order_goods : [{"goods_name":"果粒橙","goods_num":4,"goods_unit":"瓶","group_id":"0","group_name":"","id":"1","total_fee":34,"size_list":[{"size_id":"2","size_name":"3L大瓶","size_num":1,"size_price":10},{"size_id":"3","size_name":"1L中瓶","size_num":3,"size_price":8}]}]
     * order_no : 149397427281034
     * order_rooms : [{"end_time":"0","id":"1","is_billing":"1","room_id_room":"740","roomname":"圆1","start_time":"1493974200","true_income":"2299.35","use_timelength":"64小时53分钟"}]
     * orderid : 2780
     * payment : 35.5
     * query_num : A44
     * remark :
     * room : {"end_time":1494207780,"id":"740","true_income":"1.5","use_timelength":1494207780}
     * total_fee : 35.5
     * vip : 0
     */

    private MsgBean msg;
    private String returnsys;
    private List<String> auth;

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

    public List<String> getAuth() {
        return auth;
    }

    public void setAuth(List<String> auth) {
        this.auth = auth;
    }

    public static class MsgBean {
        private String addtime;
        private String goods_num;
        private String order_no;
        private int orderid;
        private double payment;
        private String query_num;
        private String remark;
        /**
         * end_time : 1494207780
         * id : 740
         * true_income : 1.5
         * use_timelength : 1494207780
         */

        private RoomBean room;
        private double total_fee;
        private int vip;
        /**
         * goods_name : 果粒橙
         * goods_num : 4
         * goods_unit : 瓶
         * group_id : 0
         * group_name :
         * id : 1
         * total_fee : 34
         * size_list : [{"size_id":"2","size_name":"3L大瓶","size_num":1,"size_price":10},{"size_id":"3","size_name":"1L中瓶","size_num":3,"size_price":8}]
         */

        private List<OrderGoodsBean> order_goods;
        /**
         * end_time : 0
         * id : 1
         * is_billing : 1
         * room_id_room : 740
         * roomname : 圆1
         * start_time : 1493974200
         * true_income : 2299.35
         * use_timelength : 64小时53分钟
         */

        private List<OrderRoomsBean> order_rooms;

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

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        public int getOrderid() {
            return orderid;
        }

        public void setOrderid(int orderid) {
            this.orderid = orderid;
        }

        public double getPayment() {
            return payment;
        }

        public void setPayment(double payment) {
            this.payment = payment;
        }

        public String getQuery_num() {
            return query_num;
        }

        public void setQuery_num(String query_num) {
            this.query_num = query_num;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public RoomBean getRoom() {
            return room;
        }

        public void setRoom(RoomBean room) {
            this.room = room;
        }

        public double getTotal_fee() {
            return total_fee;
        }

        public void setTotal_fee(double total_fee) {
            this.total_fee = total_fee;
        }

        public int getVip() {
            return vip;
        }

        public void setVip(int vip) {
            this.vip = vip;
        }

        public List<OrderGoodsBean> getOrder_goods() {
            return order_goods;
        }

        public void setOrder_goods(List<OrderGoodsBean> order_goods) {
            this.order_goods = order_goods;
        }

        public List<OrderRoomsBean> getOrder_rooms() {
            return order_rooms;
        }

        public void setOrder_rooms(List<OrderRoomsBean> order_rooms) {
            this.order_rooms = order_rooms;
        }

        public static class RoomBean {
            private int end_time;
            private String id;
            private String true_income;
            private int use_timelength;

            public int getEnd_time() {
                return end_time;
            }

            public void setEnd_time(int end_time) {
                this.end_time = end_time;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTrue_income() {
                return true_income;
            }

            public void setTrue_income(String true_income) {
                this.true_income = true_income;
            }

            public int getUse_timelength() {
                return use_timelength;
            }

            public void setUse_timelength(int use_timelength) {
                this.use_timelength = use_timelength;
            }
        }

        public static class OrderGoodsBean {
            private String goods_name;
            private int goods_num;
            private String goods_unit;
            private String group_id;
            private String group_name;
            private String id;
            private int total_fee;
            /**
             * size_id : 2
             * size_name : 3L大瓶
             * size_num : 1.0
             * size_price : 10
             */

            private List<SizeListBean> size_list;

            public String getGoods_name() {
                return goods_name;
            }

            public void setGoods_name(String goods_name) {
                this.goods_name = goods_name;
            }

            public int getGoods_num() {
                return goods_num;
            }

            public void setGoods_num(int goods_num) {
                this.goods_num = goods_num;
            }

            public String getGoods_unit() {
                return goods_unit;
            }

            public void setGoods_unit(String goods_unit) {
                this.goods_unit = goods_unit;
            }

            public String getGroup_id() {
                return group_id;
            }

            public void setGroup_id(String group_id) {
                this.group_id = group_id;
            }

            public String getGroup_name() {
                return group_name;
            }

            public void setGroup_name(String group_name) {
                this.group_name = group_name;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getTotal_fee() {
                return total_fee;
            }

            public void setTotal_fee(int total_fee) {
                this.total_fee = total_fee;
            }

            public List<SizeListBean> getSize_list() {
                return size_list;
            }

            public void setSize_list(List<SizeListBean> size_list) {
                this.size_list = size_list;
            }

            public static class SizeListBean {
                private String size_id;
                private String size_name;
                private double size_num;
                private double size_price;

                public String getSize_id() {
                    return size_id;
                }

                public void setSize_id(String size_id) {
                    this.size_id = size_id;
                }

                public String getSize_name() {
                    return size_name;
                }

                public void setSize_name(String size_name) {
                    this.size_name = size_name;
                }

                public double getSize_num() {
                    return size_num;
                }

                public void setSize_num(double size_num) {
                    this.size_num = size_num;
                }

                public double getSize_price() {
                    return size_price;
                }

                public void setSize_price(double size_price) {
                    this.size_price = size_price;
                }
            }
        }

        public static class OrderRoomsBean {
            private String end_time;
            private String id;
            private String is_billing;
            private String room_id_room;
            private String roomname;
            private String start_time;
            private String true_income;
            private String use_timelength;

            public String getEnd_time() {
                return end_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getIs_billing() {
                return is_billing;
            }

            public void setIs_billing(String is_billing) {
                this.is_billing = is_billing;
            }

            public String getRoom_id_room() {
                return room_id_room;
            }

            public void setRoom_id_room(String room_id_room) {
                this.room_id_room = room_id_room;
            }

            public String getRoomname() {
                return roomname;
            }

            public void setRoomname(String roomname) {
                this.roomname = roomname;
            }

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public String getTrue_income() {
                return true_income;
            }

            public void setTrue_income(String true_income) {
                this.true_income = true_income;
            }

            public String getUse_timelength() {
                return use_timelength;
            }

            public void setUse_timelength(String use_timelength) {
                this.use_timelength = use_timelength;
            }
        }
    }
}
