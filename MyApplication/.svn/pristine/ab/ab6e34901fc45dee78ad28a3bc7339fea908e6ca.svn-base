package com.yongle.letuiweipad.domain.createorder;


import java.io.Serializable;
import java.util.List;

/**
 * Created by 我是奋斗 on 2016/7/8.
 * 微信/e-mail:tt090423@126.com
 * 每次创建订单都会创建的订单实体类
 */
public class OrderBean implements Serializable{

    private String tradeNum;
    private String primaryKeyId;
    private List<RoomRuleBean> roomRuleList;//可以绑定多种规则，每种规则又可以有多个
    private List<GoodBean> goodList;//可以绑定多个good，每个good又可以有多个
    private String currentStartTime;
    private String currentEndTime;
    private String currentRoomId;
    private String currentRoomName;
    private List<UsedRoom> UsedRoomList;
    private List<UsedTime> UsedTimeList;

    public OrderBean() {
    }

    public String getPrimaryKeyId() {
        return primaryKeyId;
    }

    public void setPrimaryKeyId(String primaryKeyId) {
        this.primaryKeyId = primaryKeyId;
    }

    public String getCurrentRoomId() {
        return currentRoomId;
    }

    public void setCurrentRoomId(String currentRoomId) {
        this.currentRoomId = currentRoomId;
    }

    public String getCurrentRoomName() {
        return currentRoomName;
    }

    public void setCurrentRoomName(String currentRoomName) {
        this.currentRoomName = currentRoomName;
    }

    public String getCurrentStartTime() {
        return currentStartTime;
    }

    public void setCurrentStartTime(String currentStartTime) {
        this.currentStartTime = currentStartTime;
    }

    public String getCurrentEndTime() {
        return currentEndTime;
    }

    public void setCurrentEndTime(String currentEndTime) {
        this.currentEndTime = currentEndTime;
    }

    public List<UsedRoom> getUsedRoomList() {
        return UsedRoomList;
    }

    public void setUsedRoomList(List<UsedRoom> usedRoomList) {
        UsedRoomList = usedRoomList;
    }

    public List<UsedTime> getUsedTimeList() {
        return UsedTimeList;
    }

    public void setUsedTimeList(List<UsedTime> usedTimeList) {
        UsedTimeList = usedTimeList;
    }

    public String getTradeNum() {
        return tradeNum;
    }

    public void setTradeNum(String tradeNum) {
        this.tradeNum = tradeNum;
    }

    public List<RoomRuleBean> getRoomRuleList() {
        return roomRuleList;
    }

    public void setRoomRuleList(List<RoomRuleBean> roomRuleList) {
        this.roomRuleList = roomRuleList;
    }

    public List<GoodBean> getGoodList() {
        return goodList;
    }

    public void setGoodList(List<GoodBean> goodList) {
        this.goodList = goodList;
    }

    @Override
    public String toString() {
        return "OrderBean{" +
                "orderId='" + tradeNum + '\'' +
                ", roomRuleList=" + roomRuleList +
                ", goodList=" + goodList +
                ", currentStartTime='" + currentStartTime + '\'' +
                ", currentEndTime='" + currentEndTime + '\'' +
                ", currentRoomId='" + currentRoomId + '\'' +
                ", currentRoomName='" + currentRoomName + '\'' +
                ", UsedRoomList=" + UsedRoomList +
                ", UsedTimeList=" + UsedTimeList +
                '}';
    }
}