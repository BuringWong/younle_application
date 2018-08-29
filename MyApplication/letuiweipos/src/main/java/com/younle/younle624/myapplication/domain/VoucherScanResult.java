package com.younle.younle624.myapplication.domain;

/**
 * Created by 我是奋斗 on 2016/6/15.
 * 微信/e-mail:tt090423@126.com
 */
public class VoucherScanResult {
    private String result;
    private String desc;

    public VoucherScanResult() {
    }

    public VoucherScanResult(String result, String desc) {
        this.result = result;
        this.desc = desc;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
