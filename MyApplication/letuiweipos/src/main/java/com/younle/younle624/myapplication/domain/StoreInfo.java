package com.younle.younle624.myapplication.domain;

/**
 * Created by 我是奋斗 on 2016/5/11.
 * 微信/e-mail:tt090423@126.com
 *
 * 添加店铺界面店铺信息实体类
 */
public class StoreInfo {
    private String storeName;
    private String storeAdd;
    private String storeNum;

    public StoreInfo() {
    }

    public StoreInfo(String storeName, String storeAdd, String storeNum) {
        this.storeName = storeName;
        this.storeAdd = storeAdd;
        this.storeNum = storeNum;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAdd() {
        return storeAdd;
    }

    public void setStoreAdd(String storeAdd) {
        this.storeAdd = storeAdd;
    }

    public String getStoreNum() {
        return storeNum;
    }

    public void setStoreNum(String storeNum) {
        this.storeNum = storeNum;
    }

    @Override
    public String toString() {
        return "StoreInfo{" +
                "storeName='" + storeName + '\'' +
                ", storeAdd='" + storeAdd + '\'' +
                ", storeNum='" + storeNum + '\'' +
                '}';
    }
}
