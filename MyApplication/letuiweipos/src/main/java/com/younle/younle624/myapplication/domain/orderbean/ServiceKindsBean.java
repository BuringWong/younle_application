package com.younle.younle624.myapplication.domain.orderbean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 我是奋斗 on 2016/7/8.
 * 微信/e-mail:tt090423@126.com
 */
public class ServiceKindsBean {
    private int serviceKindId;
    private String serviceKindName;
    private List<ServiceBean> serviceBeanList;
    private boolean isChecked;

    public ServiceKindsBean() {
    }

    public ServiceKindsBean(int serviceKindId, String serviceKindName, List<ServiceBean> serviceBeanList, boolean isChecked) {
        this.serviceKindId = serviceKindId;
        this.serviceKindName = serviceKindName;
        this.serviceBeanList = serviceBeanList;
        this.isChecked = isChecked;
    }

    public int getServiceKindId() {
        return serviceKindId;
    }

    public void setServiceKindId(int serviceKindId) {
        this.serviceKindId = serviceKindId;
    }

    public String getServiceKindName() {
        return serviceKindName;
    }

    public void setServiceKindName(String serviceKindName) {
        this.serviceKindName = serviceKindName;
    }

    public List<ServiceBean> getServiceBeanList() {
        return serviceBeanList;
    }

    public void setServiceBeanList(List<ServiceBean> serviceBeanList) {
        this.serviceBeanList = serviceBeanList;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    @Override
    public String toString() {
        return "ServiceKindsBean{" +
                "serviceKindId=" + serviceKindId +
                ", serviceKindName='" + serviceKindName + '\'' +
                ", serviceBeanList=" + serviceBeanList +
                ", isChecked=" + isChecked +
                '}';
    }

    /**
     * Created by 我是奋斗 on 2016/6/30.
     * 微信/e-mail:tt090423@126.com
     *
     * 下单页面，服务类项目对应的实体类
     */
    public static class ServiceBean implements Serializable {
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
                           List<WaiterLeavelBean.Waiter> waiterList,boolean bindSaleMan) {
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
}
