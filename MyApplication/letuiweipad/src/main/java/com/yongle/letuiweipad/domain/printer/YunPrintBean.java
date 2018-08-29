package com.yongle.letuiweipad.domain.printer;

import java.io.Serializable;

/**
 * Created by bert_dong on 2017/8/8 0008.
 * 邮箱：18701038771@163.com
 */

public class YunPrintBean implements Serializable {

    private String partner;
    private String apiKey;
    private String machine_code;
    private String mKey;
    private String msg;

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getMachine_code() {
        return machine_code;
    }

    public void setMachine_code(String machine_code) {
        this.machine_code = machine_code;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "YunPrintBean{" +
                "partner='" + partner + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", machine_code='" + machine_code + '\'' +
                ", mKey='" + mKey + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
