package com.younle.younle624.myapplication.domain.orderbean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 我是奋斗 on 2016/7/8.
 * 微信/e-mail:tt090423@126.com
 */
public class GoodKindsBean {
    private int goodKindId;
    private String goodKindName;
    private List<GoodBean> goodBeanList;
    private boolean isChecked;

    public GoodKindsBean() {
    }

    public GoodKindsBean(int goodKindId, String goodKindName, List<GoodBean> goodBeanList, boolean isChecked) {
        this.goodKindId = goodKindId;
        this.goodKindName = goodKindName;
        this.goodBeanList = goodBeanList;
        this.isChecked = isChecked;
    }

    public int getGoodKindId() {
        return goodKindId;
    }

    public void setGoodKindId(int goodKindId) {
        this.goodKindId = goodKindId;
    }

    public String getGoodKindName() {
        return goodKindName;
    }

    public void setGoodKindName(String goodKindName) {
        this.goodKindName = goodKindName;
    }

    public List<GoodBean> getGoodBeanList() {
        return goodBeanList;
    }

    public void setGoodBeanList(List<GoodBean> goodBeanList) {
        this.goodBeanList = goodBeanList;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    @Override
    public String toString() {
        return "GoodKindsBean{" +
                "goodKindId=" + goodKindId +
                ", goodKindName='" + goodKindName + '\'' +
                ", goodBeanList=" + goodBeanList +
                ", isChecked=" + isChecked +
                '}';
    }

    /**
     * Created by 我是奋斗 on 2016/7/8.
     * 微信/e-mail:tt090423@126.com
     *
     * 下单页面的商品对应的实体类
     *
     */

    public static class GoodBean implements Serializable {
        private int goodId;
        private String name;
        private double price;
        private int num;
        private String kinds;
        private boolean bindSaleMan;

        public GoodBean() {
        }

        public GoodBean(int goodId, String name, double price, int num, String kinds,boolean bindSaleMan) {
            this.goodId = goodId;
            this.name = name;
            this.price = price;
            this.num = num;
            this.kinds = kinds;
            this.bindSaleMan=bindSaleMan;
        }

        public boolean isBindSaleMan() {
            return bindSaleMan;
        }

        public void setBindSaleMan(boolean bindSaleMan) {
            this.bindSaleMan = bindSaleMan;
        }

        public int getGoodId() {
            return goodId;
        }

        public void setGoodId(int goodId) {
            this.goodId = goodId;
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

        public String getKinds() {
            return kinds;
        }

        public void setKinds(String kinds) {
            this.kinds = kinds;
        }

        @Override
        public String toString() {
            return "GoodBean{" +
                    "goodId=" + goodId +
                    ", name='" + name + '\'' +
                    ", price=" + price +
                    ", num=" + num +
                    ", kinds='" + kinds + '\'' +
                    ", bindSaleMan=" + bindSaleMan +
                    '}';
        }
    }
}
