package com.younle.younle624.myapplication.domain;

/**
 * Created by 我是奋斗 on 2016/6/24.
 * 微信/e-mail:tt090423@126.com
 */
public class OrderDetailBean  {
    private String bigName;
    private String smallName;
    private double price;
    private int num;
    private double totalCount;

    public OrderDetailBean() {
    }

    public OrderDetailBean(String bigName, String smallName, double price, int num, double totalCount) {
        this.bigName = bigName;
        this.smallName = smallName;
        this.price = price;
        this.num = num;
        this.totalCount = totalCount;
    }

    public String getBigName() {
        return bigName;
    }

    public void setBigName(String bigName) {
        this.bigName = bigName;
    }

    public String getSmallName() {
        return smallName;
    }

    public void setSmallName(String smallName) {
        this.smallName = smallName;
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

    public double getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(double totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public String toString() {
        return "OrderDetailBean{" +
                "bigName='" + bigName + '\'' +
                ", smallName='" + smallName + '\'' +
                ", price=" + price +
                ", num=" + num +
                ", totalCount=" + totalCount +
                '}';
    }
}
