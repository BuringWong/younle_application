package com.younle.younle624.myapplication.domain.orderbean;

import java.io.Serializable;

/**
 * Created by 我是奋斗 on 2016/7/8.
 * 微信/e-mail:tt090423@126.com
 *
 * 下单页面对应的房间的实体类
 */
public class Room implements Serializable{
    private int roomId;
    private String name;
    private int deposit;//押金
    private int minConsume;//最低消费
    private boolean isFree;//是否悠长使用
    private boolean aviable;//是否可用
    private String useingItem;//如果正在使用，使用中的项目
    private String orderTime;//正在使用中的项目的下单时间
    private String roomType;//房间类型
    private boolean isChecked;//是否被选择
    private long startTime;
    private long endTime;
    public Room() {
    }

    public Room(int roomId, String name, int deposit, int minConsume, boolean isFree, boolean aviable,boolean isChecked) {
        this.roomId = roomId;
        this.name = name;
        this.deposit = deposit;
        this.minConsume = minConsume;
        this.isFree = isFree;
        this.aviable = aviable;
        this.isChecked=isChecked;

    }

    public Room(int roomId, String name, int deposit, int minConsume, boolean isFree, boolean aviable, String roomType) {
        this.roomId = roomId;
        this.name = name;
        this.deposit = deposit;
        this.minConsume = minConsume;
        this.isFree = isFree;
        this.aviable = aviable;
        this.roomType = roomType;
    }

    public Room(int roomId, String name, int deposit, int minConsume, boolean isFree, boolean aviable, String useingItem, String orderTime, String roomType) {
        this.roomId = roomId;
        this.name = name;
        this.deposit = deposit;
        this.minConsume = minConsume;
        this.isFree = isFree;
        this.aviable = aviable;
        this.useingItem = useingItem;
        this.orderTime = orderTime;
        this.roomType = roomType;
    }

    public Room(int roomId, String name, int deposit, int minConsume, boolean isFree, boolean aviable, String useingItem, String orderTime) {
        this.roomId = roomId;
        this.name = name;
        this.deposit = deposit;
        this.minConsume = minConsume;
        this.isFree = isFree;
        this.aviable = aviable;
        this.useingItem = useingItem;
        this.orderTime = orderTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getUseingItem() {
        return useingItem;
    }

    public void setUseingItem(String useingItem) {
        this.useingItem = useingItem;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    public int getMinConsume() {
        return minConsume;
    }

    public void setMinConsume(int minConsume) {
        this.minConsume = minConsume;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setIsFree(boolean isFree) {
        this.isFree = isFree;
    }

    public boolean isAviable() {
        return aviable;
    }

    public void setAviable(boolean aviable) {
        this.aviable = aviable;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", name='" + name + '\'' +
                ", deposit=" + deposit +
                ", minConsume=" + minConsume +
                ", isFree=" + isFree +
                ", aviable=" + aviable +
                ", useingItem='" + useingItem + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", roomType='" + roomType + '\'' +
                ", isChecked=" + isChecked +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
