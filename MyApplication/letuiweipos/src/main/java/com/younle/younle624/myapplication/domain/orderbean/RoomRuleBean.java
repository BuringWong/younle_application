package com.younle.younle624.myapplication.domain.orderbean;

import java.io.Serializable;

/**
 * Created by 我是奋斗 on 2016/7/8.
 * 微信/e-mail:tt090423@126.com
 *
 * 下单页面房间使用规则对应的实体类
 */
public class RoomRuleBean implements Serializable{
    private int ruleId;
    private String name;
    private double price;
    private int num;
    private boolean bindSaleMan;

    public RoomRuleBean() {
    }

    public RoomRuleBean(int ruleId, String name, double price, int num,boolean bindSaleMan) {
        this.ruleId = ruleId;
        this.name = name;
        this.price = price;
        this.num = num;
        this.bindSaleMan=bindSaleMan;
    }

    public boolean isBindSaleMan() {
        return bindSaleMan;
    }

    public void setBindSaleMan(boolean bindSaleMan) {
        this.bindSaleMan = bindSaleMan;
    }

    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "RoomRuleBean{" +
                "ruleId=" + ruleId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", num=" + num +
                ", bindSaleMan=" + bindSaleMan +
                '}';
    }
}
