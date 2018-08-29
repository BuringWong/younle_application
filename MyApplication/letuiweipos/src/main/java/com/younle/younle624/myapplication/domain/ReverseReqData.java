package com.younle.younle624.myapplication.domain;

/**
 * Created by Administrator on 2016/7/12.
 */
public class ReverseReqData {
    private String transactionID;
    private String outTradeNo;

    public ReverseReqData(String transactionID, String outTradeNo) {
        this.transactionID = transactionID;
        this.outTradeNo = outTradeNo;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    @Override
    public String toString() {
        return "ReverseReqData{" +
                "transactionID='" + transactionID + '\'' +
                ", outTradeNo='" + outTradeNo + '\'' +
                '}';
    }
}
