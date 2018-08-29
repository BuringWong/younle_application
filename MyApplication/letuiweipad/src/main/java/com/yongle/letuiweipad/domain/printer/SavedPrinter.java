package com.yongle.letuiweipad.domain.printer;

import java.io.Serializable;

/**
 * Created by 我是奋斗 on 2016/8/4.
 * 微信/e-mail:tt090423@126.com
 */
public class SavedPrinter implements Serializable{
    private String printerId;
    private String printerKey;
    private String name;
    private String printGroupId;
    private String printGroupName;

    private String blueToothAdd;
    /**
     * 打印机类型 0：本地打印机 1：蓝牙打印机 2：云打印机
     */
    private int type;

    private String printPermission;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPrintPermission() {
        return printPermission;
    }

    public void setPrintPermission(String printPermission) {
        this.printPermission = printPermission;
    }

    public SavedPrinter() {
    }

    public SavedPrinter(String printerId, String printerKey, String name) {
        this.printerId = printerId;
        this.printerKey = printerKey;
        this.name = name;
    }

    public String getBlueToothAdd() {
        return blueToothAdd;
    }

    public void setBlueToothAdd(String blueToothAdd) {
        this.blueToothAdd = blueToothAdd;
    }

    public String getPrintGroupId() {
        return printGroupId;
    }

    public void setPrintGroupId(String printGroupId) {
        this.printGroupId = printGroupId;
    }

    public String getPrintGroupName() {
        return printGroupName;
    }

    public void setPrintGroupName(String printGroupName) {
        this.printGroupName = printGroupName;
    }

    public String getPrinterId() {
            return printerId;
        }

        public void setPrinterId(String printerId) {
            this.printerId = printerId;
        }

        public String getPrinterKey() {
            return printerKey;
        }

        public void setPrinterKey(String printerKey) {
            this.printerKey = printerKey;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    @Override
    public String toString() {
        return "SavedPrinter{" +
                "printerId='" + printerId + '\'' +
                ", printerKey='" + printerKey + '\'' +
                ", name='" + name + '\'' +
                ", printGroupId='" + printGroupId + '\'' +
                ", printGroupName='" + printGroupName + '\'' +
                ", blueToothAdd='" + blueToothAdd + '\'' +
                ", type='" + type + '\'' +
                ", printPermission='" + printPermission + '\'' +
                '}';
    }
}
