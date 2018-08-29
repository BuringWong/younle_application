package com.yongle.letuiweipad.domain.createorder;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 我是奋斗 on 2016/9/29.
 * 微信/e-mail:tt090423@126.com
 */
public class DetailMemberBean implements Serializable {

    /**
     * auth : ["1","2","3",""4,"5","6"]
     * code : 200
     * msg : {"average":3032.5,"headimgurl":"http://wx.qlogo.cn/mmopen/PiajxSqBRaELzrpMNccRGB9A62NwqLjNfrD2L3n78XWdBt892lR5D7zxPSWj44kzJV5HVysyv4QRUuDZsDppdGg/0","money":92080.85,"name":"张开圆","nickname":"最爱喜洋洋","sex":"1","tags":",3,8,","vip_discount_rules":[{"money":"120","rate":"1"}],"vipcardid":"1","vipcardid":"1","vipcreate_id":"1"}
     */

    private int code;
    /**
     * average : 3032.5
     * headimgurl : http://wx.qlogo.cn/mmopen/PiajxSqBRaELzrpMNccRGB9A62NwqLjNfrD2L3n78XWdBt892lR5D7zxPSWj44kzJV5HVysyv4QRUuDZsDppdGg/0
     * money : 92080.85
     * name : 张开圆
     * nickname : 最爱喜洋洋
     * sex : 1
     * tags : ,3,8,
     * vip_discount_rules : [{"money":"120","rate":"1"}]
     * vipcardid : 1
     * vipcardid : 1
     * vipcardid : 1
     * vipcreate_id : 1
     * vip_discount_rules : [{"money":"120","rate":"1"}]
     */

    private MsgBean msg;
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

    public List<String> getAuth() {
        return auth;
    }

    public void setAuth(List<String> auth) {
        this.auth = auth;
    }

    public static class MsgBean implements Serializable {
        private double average;
        private String headimgurl;
        private double money;
        private String name;
        private String nickname;
        private String sex;
        private String[] tags;
        private String vipcardid;
        private String vipcreate_id;
        private String auth_code;
        private String monthnum;
        private String top5;
        private boolean supply;
        private boolean is_set_vip_price;
        private List<RulesBean> rules;

        public List<RulesBean> getRules() {
            return rules;
        }

        public void setRules(List<RulesBean> rules) {
            this.rules = rules;
        }

        public boolean isIs_set_vip_price() {
            return is_set_vip_price;
        }

        public void setIs_set_vip_price(boolean is_set_vip_price) {
            this.is_set_vip_price = is_set_vip_price;
        }

        public String[] getTags() {
            return tags;
        }

        public void setTags(String[] tags) {
            this.tags = tags;
        }

        public boolean isSupply() {
            return supply;
        }

        public void setSupply(boolean supply) {
            this.supply = supply;
        }

        public String getTop5() {
            return top5;
        }

        public void setTop5(String top5) {
            this.top5 = top5;
        }

        public String getMonthnum() {
            return monthnum;
        }

        public void setMonthnum(String monthnum) {
            this.monthnum = monthnum;
        }

        public String getAuth_code() {
            return auth_code;
        }

        public void setAuth_code(String auth_code) {
            this.auth_code = auth_code;
        }

        /**
         * money : 120
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


        public String getVipcardid() {
            return vipcardid;
        }

        public void setVipcardid(String vipcardid) {
            this.vipcardid = vipcardid;
        }

        public String getVipcreate_id() {
            return vipcreate_id;
        }

        public void setVipcreate_id(String vipcreate_id) {
            this.vipcreate_id = vipcreate_id;
        }

        public List<VipDiscountRulesBean> getVip_discount_rules() {
            return vip_discount_rules;
        }

        public void setVip_discount_rules(List<VipDiscountRulesBean> vip_discount_rules) {
            this.vip_discount_rules = vip_discount_rules;
        }

        public static class VipDiscountRulesBean implements Serializable {
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
}

   /* *//**
     * code : 200
     * msg : {"average":5882.4,"headimgurl":"http://wx.qlogo.cn/mmopen/PiajxSqBRaELzrpMNccRGB9A62NwqLjNfrD2L3n78XWdBt892lR5D7zxPSWj44kzJV5HVysyv4QRUuDZsDppdGg/0","money":101251.13,"name":"张开圆","nickname":"最爱喜洋洋","sex":"1","tags":",3,8,","vip_discount_rules":[{"money":"120","rate":"1"}],"vipcardid":"1"}
     *//*

    private int code;
    *//**
     * average : 5882.4
     * headimgurl : http://wx.qlogo.cn/mmopen/PiajxSqBRaELzrpMNccRGB9A62NwqLjNfrD2L3n78XWdBt892lR5D7zxPSWj44kzJV5HVysyv4QRUuDZsDppdGg/0
     * money : 101251.13
     * name : 张开圆
     * nickname : 最爱喜洋洋
     * sex : 1
     * tags : ,3,8,
     * vip_discount_rules : [{"money":"120","rate":"1"}]
     * vipcardid : 1
     *//*

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
        private double average;
        private String headimgurl;
        private double money;
        private String name;
        private String nickname;
        private String sex;
        private String tags;
        private String vipcardid;
        *//**
         * money : 120
         * rate : 1
         *//*

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

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getVipcardid() {
            return vipcardid;
        }

        public void setVipcardid(String vipcardid) {
            this.vipcardid = vipcardid;
        }

        public List<VipDiscountRulesBean> getVip_discount_rules() {
            return vip_discount_rules;
        }

        public void setVip_discount_rules(List<VipDiscountRulesBean> vip_discount_rules) {
            this.vip_discount_rules = vip_discount_rules;
        }

        public static class VipDiscountRulesBean implements Serializable {
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
    }*/
