package com.younle.younle624.myapplication.domain.printsetting;

import java.io.Serializable;

/**
 * Created by 我是奋斗 on 2016/8/3.
 * 微信/e-mail:tt090423@126.com
 */
public class PrintDevices implements Serializable {
    private String reMarkName;
    private String blueToothAdd;
    private String type;

    public PrintDevices() {
    }

    public PrintDevices(String reMarkName, String blueToothAdd, String type) {
        this.reMarkName = reMarkName;
        this.blueToothAdd = blueToothAdd;
        this.type = type;
    }

    public String getReMarkName() {
        return reMarkName;
    }

    public void setReMarkName(String reMarkName) {
        this.reMarkName = reMarkName;
    }

    public String getBlueToothAdd() {
        return blueToothAdd;
    }

    public void setBlueToothAdd(String blueToothAdd) {
        this.blueToothAdd = blueToothAdd;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PrintDevices{" +
                "reMarkName='" + reMarkName + '\'' +
                ", blueToothAdd='" + blueToothAdd + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
