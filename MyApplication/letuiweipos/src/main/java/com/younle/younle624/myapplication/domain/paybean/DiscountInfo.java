package com.younle.younle624.myapplication.domain.paybean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：Create by 我是奋斗 on2016/12/17 16:23
 * 邮箱：tt090423@126.com
 * 微信：17346512596
 * 会员折扣信息
 */
public class DiscountInfo implements Serializable {

    /**
     * balance_rules : [{"money":"100","song":"50","zong":"150"},{"money":"200","song":"100","zong":"300"},{"money":"300","song":"150","zong":"450"}]
     * discount_rules : [{"money":"120","rate":"1"}]
     * url : http://test.younle.com/tpl/Home/common/image/ban_5.jpg
     */

    private String url;
    /**
     * money : 100
     * song : 50
     * zong : 150
     */

    private List<BalanceRulesBean> balance_rules;
    /**
     * money : 120
     * rate : 1
     */

    private List<DiscountRulesBean> discount_rules;

    private double rechargeMoney;
    private String membershipNum;
    private String storeWechat;

    public double getRechargeMoney() {
        return rechargeMoney;
    }

    public void setRechargeMoney(double rechargeMoney) {
        this.rechargeMoney = rechargeMoney;
    }

    public String getMembershipNum() {
        return membershipNum;
    }

    public void setMembershipNum(String membershipNum) {
        this.membershipNum = membershipNum;
    }

    public String getStoreWechat() {
        return storeWechat;
    }

    public void setStoreWechat(String storeWechat) {
        this.storeWechat = storeWechat;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public static class BalanceRulesBean implements Serializable{
        private String money;
        private String song;
        private String zong;

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

        public String getZong() {
            return zong;
        }

        public void setZong(String zong) {
            this.zong = zong;
        }

        @Override
        public String toString() {
            return "BalanceRulesBean{" +
                    "money='" + money + '\'' +
                    ", song='" + song + '\'' +
                    ", zong='" + zong + '\'' +
                    '}';
        }
    }

    public static class DiscountRulesBean implements Serializable {
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

        @Override
        public String toString() {
            return "DiscountRulesBean{" +
                    "money='" + money + '\'' +
                    ", rate='" + rate + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "DiscountInfo{" +
                "url='" + url + '\'' +
                ", balance_rules=" + balance_rules +
                ", discount_rules=" + discount_rules +
                '}';
    }
}
