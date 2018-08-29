package com.younle.younle624.myapplication.domain;

import java.util.List;

/**
 * Created by bert_dong on 2018/4/2 0002.
 * 邮箱：18701038771@163.com
 */

public class WxAppletBean {


    private long requestTime;
    private List<AppLetReceiveBean> orderList;
    private int step=10;

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    public List<AppLetReceiveBean> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<AppLetReceiveBean> orderList) {
        this.orderList = orderList;
    }

    @Override
    public String toString() {
        return "WxAppletBean{" +
                "requestTime=" + requestTime +
                ", orderList=" + orderList +
                ", step=" + step +
                '}';
    }
}
