package com.yongle.letuiweipad.domain.createorder;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bert_dong on 2016/10/8 0008.
 * 邮箱：18701038771@163.com
 */
public class UnPayDetailsBean implements Serializable{
    @Override
    public String toString() {
        return "UnPayDetailsBean{" +
                "code=" + code +
                ", msg=" + msg +
                '}';
    }

    /**
     * code : 200
     * msg : {"addtime":1481801070,"order_entity":[{"entity_name":"果粒橙","entity_num":2,"entity_price":5,"id":"1","total_fee":10},{"entity_name":"雪碧","entity_num":2,"entity_price":4.5,"id":"2","total_fee":9}],"order_rooms":[{"end_time":"0","id":"54","is_billing":"0","roomname":"桌台9号","start_time":"1481800200","true_income":0,"use_timelength":"14分钟"}],"order_server":[{"id":"5","server_name":"2小时按摩","server_num":3,"server_price":188,"total_fee":564},{"id":"6","server_name":"1小时按摩","server_num":2,"server_price":99,"total_fee":198}],"orderid":23038,"payment":0,"query_num":34,"remark":"","total_fee":781,"vip":"0","vip_discount":"0","vipinfo":{"average":0,"headimgurl":"http://wx.qlogo.cn/mmopen/6FoiafBLkmYnFFlbVHa0xiauVob0fVDKWyuhzW1D77hFvT67VWF84bUpPAooPboaicamVRGdOOOQNp6uAaUGIvGep62DRlibkzsX/0","money":20,"name":"尚","nickname":"虾米","sex":"0","tags":"","vip_discount_rules":[{"money":120,"rate":"1"}],"vipcard_id":1,"vipcardid":"26","vipcreate_id":1}}
     */

    private int code;
    /**
     * addtime : 1481801070
     * order_entity : [{"entity_name":"果粒橙","entity_num":2,"entity_price":5,"id":"1","total_fee":10},{"entity_name":"雪碧","entity_num":2,"entity_price":4.5,"id":"2","total_fee":9}]
     * order_rooms : [{"end_time":"0","id":"54","is_billing":"0","roomname":"桌台9号","start_time":"1481800200","true_income":0,"use_timelength":"14分钟"}]
     * order_server : [{"id":"5","server_name":"2小时按摩","server_num":3,"server_price":188,"total_fee":564},{"id":"6","server_name":"1小时按摩","server_num":2,"server_price":99,"total_fee":198}]
     * orderid : 23038
     * payment : 0
     * query_num : 34
     * remark :
     * total_fee : 781.0
     * vip : 0
     * vip_discount : 0
     * vipinfo : {"average":0,"headimgurl":"http://wx.qlogo.cn/mmopen/6FoiafBLkmYnFFlbVHa0xiauVob0fVDKWyuhzW1D77hFvT67VWF84bUpPAooPboaicamVRGdOOOQNp6uAaUGIvGep62DRlibkzsX/0","money":20,"name":"尚","nickname":"虾米","sex":"0","tags":"","vip_discount_rules":[{"money":120,"rate":"1"}],"vipcard_id":1,"vipcardid":"26","vipcreate_id":1}
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

    public static class MsgBean implements Serializable{
        private String addtime;
        private int orderid;
        private double payment;
        private String query_num;
        private String remark;
        private double total_fee;
        private int vip;
        private String vip_discount;
        private String order_no;
        private double goods_num;
        private Roombean room;
        private double price;

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public Roombean getRoom() {
            return room;
        }

        public void setRoom(Roombean room) {
            this.room = room;
        }
        public double getGoods_num() {
            return goods_num;
        }

        public void setGoods_num(double goods_num) {
            this.goods_num = goods_num;
        }
        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        @Override
        public String toString() {
            return "MsgBean{" +
                    "addtime='" + addtime + '\'' +
                    ", orderid=" + orderid +
                    ", payment=" + payment +
                    ", query_num='" + query_num + '\'' +
                    ", remark='" + remark + '\'' +
                    ", total_fee=" + total_fee +
                    ", vip=" + vip +
                    ", vip_discount='" + vip_discount + '\'' +
                    ", order_no='" + order_no + '\'' +
                    ", goods_num=" + goods_num +
                    ", room=" + room +
                    ", vipinfo=" + vipinfo +
                    ", order_goods=" + order_goods +
                    ", group_info=" + group_info +
                    ", order_rooms=" + order_rooms +
                    '}';
        }

        /**
         * average : 0.0
         * headimgurl : http://wx.qlogo.cn/mmopen/6FoiafBLkmYnFFlbVHa0xiauVob0fVDKWyuhzW1D77hFvT67VWF84bUpPAooPboaicamVRGdOOOQNp6uAaUGIvGep62DRlibkzsX/0
         * money : 20.0
         * name : 尚
         * nickname : 虾米
         * sex : 0
         * tags :
         * vip_discount_rules : [{"money":120,"rate":"1"}]
         * vipcard_id : 1
         * vipcardid : 26
         * vipcreate_id : 1
         */

        private VipinfoBean vipinfo;
        /**
         * entity_name : 果粒橙
         * entity_num : 2
         * entity_price : 5.0
         * id : 1
         * total_fee : 10.0
         */

        //private List<OrderEntityBean> order_entity;
        private voucherinfoBean voucherinfo;

        private List<OrderGoodsBean> order_goods;
        private List<GroupInfoBean> group_info;
        /**
         * end_time : 0
         * id : 54
         * is_billing : 0
         * roomname : 桌台9号
         * start_time : 1481800200
         * true_income : 0.0
         * use_timelength : 14分钟
         */

        private List<OrderRoomsBean> order_rooms;
        /**
         * id : 5
         * server_name : 2小时按摩
         * server_num : 3
         * server_price : 188.0
         * total_fee : 564.0
         */

        //private List<OrderServerBean> order_server;
        public voucherinfoBean getVoucherinfo() {
            return voucherinfo;
        }
        public void setVoucherinfo(voucherinfoBean voucherinfo) {
            this.voucherinfo = voucherinfo;
        }
        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
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

        public String getVip_discount() {
            return vip_discount;
        }

        public void setVip_discount(String vip_discount) {
            this.vip_discount = vip_discount;
        }

        public VipinfoBean getVipinfo() {
            return vipinfo;
        }

        public void setVipinfo(VipinfoBean vipinfo) {
            this.vipinfo = vipinfo;
        }

        public List<OrderRoomsBean> getOrder_rooms() {
            return order_rooms;
        }

        public void setOrder_rooms(List<OrderRoomsBean> order_rooms) {
            this.order_rooms = order_rooms;
        }

        public List<OrderGoodsBean> getOrder_goods() {
            return order_goods;
        }

        public void setOrder_goods(List<OrderGoodsBean> order_goods) {
            this.order_goods = order_goods;
        }

        public List<GroupInfoBean> getGroup_info() {
            return group_info;
        }

        public void setGroup_info(List<GroupInfoBean> group_info) {
            this.group_info = group_info;
        }

        /*public List<OrderServerBean> getOrder_server() {
            return order_server;
        }

        public void setOrder_server(List<OrderServerBean> order_server) {
            this.order_server = order_server;
        }*/

        public static class VipinfoBean implements Serializable{
            private double average;
            private String headimgurl;
            private double money;
            private String name;
            private String nickname;
            private String sex;
            private String[] tags;
            private int vipcard_id;
            private String vipcardid;
            private int vipcreate_id;
            private boolean is_set_vip_price;

            public boolean isIs_set_vip_price() {
                return is_set_vip_price;
            }

            public void setIs_set_vip_price(boolean is_set_vip_price) {
                this.is_set_vip_price = is_set_vip_price;
            }

            /**
             * money : 120.0
             * rate : 1
             */

            private List<VipDiscountRulesBean> vip_discount_rules;

            public double getAverage() {
                return average;
            }

            public void setAverage(double average) {
                this.average = average;
            }

            public String getHeadimgurl() {
                return headimgurl;
            }

            public void setHeadimgurl(String headimgurl) {
                this.headimgurl = headimgurl;
            }

            public double getMoney() {
                return money;
            }

            public void setMoney(double money) {
                this.money = money;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String[] getTags() {
                return tags;
            }

            public void setTags(String[] tags) {
                this.tags = tags;
            }

            public int getVipcard_id() {
                return vipcard_id;
            }

            public void setVipcard_id(int vipcard_id) {
                this.vipcard_id = vipcard_id;
            }

            public String getVipcardid() {
                return vipcardid;
            }

            public void setVipcardid(String vipcardid) {
                this.vipcardid = vipcardid;
            }

            public int getVipcreate_id() {
                return vipcreate_id;
            }

            public void setVipcreate_id(int vipcreate_id) {
                this.vipcreate_id = vipcreate_id;
            }

            public List<VipDiscountRulesBean> getVip_discount_rules() {
                return vip_discount_rules;
            }

            public void setVip_discount_rules(List<VipDiscountRulesBean> vip_discount_rules) {
                this.vip_discount_rules = vip_discount_rules;
            }

            public static class VipDiscountRulesBean implements Serializable{
                private double money;
                private String rate;

                public double getMoney() {
                    return money;
                }

                public void setMoney(double money) {
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

        public static class voucherinfoBean implements Serializable{

            private String code;
            private String card_type;
            private String cardid;
            private double discount;
            private String errcode;
            private double least_cost;
            private double reduce_cost;
            private String info;
            private String usetime;
            private String vipcardid;
            private String vipinfo_name;
            private String couponid;

            public String getCode() {
                return code;
            }
            public void setCode(String code) {
                this.code = code;
            }
            public String getCouponid() {
                return couponid;
            }
            public void setCouponid(String couponid) {
                this.couponid = couponid;
            }
            public String getCard_type() {
                return card_type;
            }
            public void setCard_type(String card_type) {
                this.card_type = card_type;
            }
            public String getCardid() {
                return cardid;
            }
            public void setCardid(String cardid) {
                this.cardid = cardid;
            }
            public double getDiscount() {
                return discount;
            }
            public void setDiscount(double discount) {
                this.discount = discount;
            }
            public String getErrcode() {
                return errcode;
            }
            public void setErrcode(String errcode) {
                this.errcode = errcode;
            }
            public double getLeast_cost() {
                return least_cost;
            }
            public void setLeast_cost(double least_cost) {
                this.least_cost = least_cost;
            }
            public double getReduce_cost() {
                return reduce_cost;
            }
            public void setReduce_cost(double reduce_cost) {
                this.reduce_cost = reduce_cost;
            }
            public String getInfo() {
                return info;
            }
            public void setInfo(String info) {
                this.info = info;
            }
            public String getUsetime() {
                return usetime;
            }
            public void setUsetime(String usetime) {
                this.usetime = usetime;
            }
            public String getVipcardid() {
                return vipcardid;
            }
            public void setVipcardid(String vipcardid) {
                this.vipcardid = vipcardid;
            }
            public String getVipinfo_name() {
                return vipinfo_name;
            }
            public void setVipinfo_name(String vipinfo_name) {
                this.vipinfo_name = vipinfo_name;
            }
        }

        public static class OrderGoodsBean implements Serializable{
            private String goods_name;
            private double goods_num;
            private double goods_price;
            private double goods_ori_price;
            private String goods_unit;
            private String group_id;
            private String group_name;
            private String id;
            private String size_id;
            private String size_name;
            private double total_fee;
            private String is_weigh;//0：非称重 1：称重
            /**
             * 0：非会员价
             * 1: 会员价
             */
            private int is_vip;

            public int getIs_vip() {
                return is_vip;
            }

            public void setIs_vip(int is_vip) {
                this.is_vip = is_vip;
            }

            public String getIs_weigh() {
                return is_weigh;
            }

            public void setIs_weigh(String is_weigh) {
                this.is_weigh = is_weigh;
            }

            public String getSize_id() {
                return size_id;
            }

            public void setSize_id(String size_id) {
                this.size_id = size_id;
            }


            /*public String getSizeId() {
                return sizeId;
            }

            public void setSizeId(String sizeId) {
                this.sizeId = sizeId;
            }*/

            public String getSize_name() {
                return size_name;
            }

            public void setSize_name(String size_name) {
                this.size_name = size_name;
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

            public String getGoods_name() {
                return goods_name;
            }

            public void setGoods_name(String goods_name) {
                this.goods_name = goods_name;
            }

            public double getGoods_num() {
                return goods_num;
            }

            public double getGoods_ori_price() {
                return goods_ori_price;
            }

            public void setGoods_ori_price(double goods_ori_price) {
                this.goods_ori_price = goods_ori_price;
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

            public String getGoods_unit() {
                return goods_unit;
            }

            public void setGoods_unit(String goods_unit) {
                this.goods_unit = goods_unit;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public double getTotal_fee() {
                return total_fee;
            }

            public void setTotal_fee(double total_fee) {
                this.total_fee = total_fee;
            }

        }
        public static class GroupInfoBean implements Serializable{

            private String group_id;
            private String group_name;

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
        }
        public static class OrderRoomsBean implements Serializable{
            private String end_time;
            private String id;
            private String is_billing;
            private String roomname;
            private String start_time;
            private String true_income;
            private String use_timelength;
            private String room_id_room;
            private int is_vip;
            /**
             * 1:桌台
             * 0:房间
             */
            private String room_type;

            public String getRoom_type() {
                return room_type;
            }

            public int getIs_vip() {
                return is_vip;
            }

            public void setIs_vip(int is_vip) {
                this.is_vip = is_vip;
            }

            public void setRoom_type(String room_type) {
                this.room_type = room_type;
            }

            public String getRoom_id_room() {
                return room_id_room;
            }

            public void setRoom_id_room(String room_id_room) {
                this.room_id_room = room_id_room;
            }

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

    public static class Roombean implements Serializable{
        private String end_time;
        private String id;
        private String true_income;
        private String use_timelength;

        public Roombean() {
        }

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
