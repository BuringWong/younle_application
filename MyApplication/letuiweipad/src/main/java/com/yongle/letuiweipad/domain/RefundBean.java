package com.yongle.letuiweipad.domain;

/**
 * Created by BurNing.Wong on 2018/7/28 0028.
 * 邮箱：tt090423@126.com
 */

public class RefundBean {
    String name;
    double price;
    double num;

    public RefundBean(String name, double price, double num) {
        this.name = name;
        this.price = price;
        this.num = num;
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

    public double getNum() {
        return num;
    }

    public void setNum(double num) {
        this.num = num;
    }
}
