package com.younle.younle624.myapplication.domain;

/**
 * Created by bert_dong on 2017/6/14 0014.
 * 邮箱：18701038771@163.com
 */

public class GetAppletDataBean {
    private int code;
    private AppLetReceiveBean msg;
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public AppLetReceiveBean getMsg() {
        return msg;
    }
    public void setMsg(AppLetReceiveBean msg) {
        this.msg = msg;
    }
}
