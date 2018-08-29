package com.younle.younle624.myapplication.domain;

import com.younle.younle624.myapplication.domain.orderbean.WaiterLeavelBean;

import java.util.List;

/**
 * Created by bert_dong on 2016/11/1 0001.
 * 邮箱：18701038771@163.com
 */
public class ServiceBean {
    private String serviceName;
    private String kinds;
    private double price;
    private int count;
    private int serviceId;
    private List<WaiterLeavelBean.Waiter> waiterList;
    private boolean bindSaleMan;
    public ServiceBean() {
    }

    public ServiceBean(String serviceName, String kinds, double price, int count, int serviceId,
                       List<WaiterLeavelBean.Waiter> waiterList, boolean bindSaleMan) {
        this.serviceName = serviceName;
        this.kinds = kinds;
        this.price = price;
        this.count = count;
        this.serviceId = serviceId;
        this.waiterList = waiterList;
        this.bindSaleMan=bindSaleMan;
    }

    public boolean isBindSaleMan() {
        return bindSaleMan;
    }

    public void setBindSaleMan(boolean bindSaleMan) {
        this.bindSaleMan = bindSaleMan;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public List<WaiterLeavelBean.Waiter> getWaiterList() {
        return waiterList;
    }

    public void setWaiterList(List<WaiterLeavelBean.Waiter> waiterList) {
        this.waiterList = waiterList;
    }

    public String getKinds() {
        return kinds;
    }

    public void setKinds(String kinds) {
        this.kinds = kinds;
    }

    @Override
    public String toString() {
        return "ServiceBean{" +
                "serviceName='" + serviceName + '\'' +
                ", kinds='" + kinds + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", serviceId=" + serviceId +
                ", waiterList=" + waiterList +
                ", bindSaleMan=" + bindSaleMan +
                '}';
    }
}
