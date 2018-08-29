package com.younle.younle624.myapplication.domain.orderbean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 我是奋斗 on 2016/7/11.
 * 微信/e-mail:tt090423@126.com
 */
public class WaiterLeavelBean implements Serializable{
    private int leavelId;
    private String leavelName;
    private List<Waiter> waiterList;
    private boolean isChecked;

    public WaiterLeavelBean() {
    }

    public WaiterLeavelBean(int leavelId, String leavelName, List<Waiter> waiterList, boolean isChecked) {
        this.leavelId = leavelId;
        this.leavelName = leavelName;
        this.waiterList = waiterList;
        this.isChecked = isChecked;
    }

    public int getLeavelId() {
        return leavelId;
    }

    public void setLeavelId(int leavelId) {
        this.leavelId = leavelId;
    }

    public String getLeavelName() {
        return leavelName;
    }

    public void setLeavelName(String leavelName) {
        this.leavelName = leavelName;
    }

    public List<Waiter> getWaiterList() {
        return waiterList;
    }

    public void setWaiterList(List<Waiter> waiterList) {
        this.waiterList = waiterList;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    @Override
    public String toString() {
        return "WaiterLeavelBean{" +
                "leavelId=" + leavelId +
                ", leavelName='" + leavelName + '\'' +
                ", waiterList=" + waiterList +
                ", isChecked=" + isChecked +
                '}';
    }

    public static class Waiter implements Serializable{
        private int waiterId;
        private int count;//被选则的次数
        private String name;
        private String leavel;
        private boolean aviable;
        private String serviceItem;
        private String orderTime;
        private String serverId;//当前所服务的订单

        public String getServerId() {
            return serverId;
        }

        public void setServerId(String serverId) {
            this.serverId = serverId;
        }

        public Waiter() {
        }

        public Waiter(int waiterId, String name, String leavel, boolean aviable) {
            this.waiterId = waiterId;
            this.name = name;
            this.leavel = leavel;
            this.aviable = aviable;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public Waiter(int waiterId, String name, String leavel, boolean aviable, String serviceItem, String orderTime,String serverId) {
            this.waiterId = waiterId;
            this.name = name;
            this.leavel = leavel;
            this.aviable = aviable;
            this.serviceItem = serviceItem;
            this.orderTime = orderTime;
            this.serverId=serverId;
        }

        public int getWaiterId() {
            return waiterId;
        }

        public void setWaiterId(int waiterId) {
            this.waiterId = waiterId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLeavel() {
            return leavel;
        }

        public void setLeavel(String leavel) {
            this.leavel = leavel;
        }

        public boolean isAviable() {
            return aviable;
        }

        public void setAviable(boolean aviable) {
            this.aviable = aviable;
        }

        public String getServiceItem() {
            return serviceItem;
        }

        public void setServiceItem(String serviceItem) {
            this.serviceItem = serviceItem;
        }

        public String getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(String orderTime) {
            this.orderTime = orderTime;
        }

        @Override
        public String toString() {
            return "Waiter{" +
                    "waiterId=" + waiterId +
                    ", count=" + count +
                    ", name='" + name + '\'' +
                    ", leavel='" + leavel + '\'' +
                    ", aviable=" + aviable +
                    ", serviceItem='" + serviceItem + '\'' +
                    ", orderTime='" + orderTime + '\'' +
                    ", serverId='" + serverId + '\'' +
                    '}';
        }
    }

}
