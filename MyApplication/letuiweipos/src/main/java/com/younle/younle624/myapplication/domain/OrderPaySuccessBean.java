package com.younle.younle624.myapplication.domain;

import java.util.List;

/**
 * Created by bert_dong on 2016/11/28 0028.
 * 邮箱：18701038771@163.com
 */
public class OrderPaySuccessBean {

    /**
     * auth : ["1","3","5"]
     * code : 200
     * code_des : 支付成功
     * query_num : 1
     * time_end : 2016-11-28 10:25:44
     * total_fee : 0.01
     */

    private String code;
    private String code_des;
    private int query_num;
    private String time_end;
    private double total_fee;
    private List<String> auth;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode_des() {
        return code_des;
    }

    public void setCode_des(String code_des) {
        this.code_des = code_des;
    }

    public int getQuery_num() {
        return query_num;
    }

    public void setQuery_num(int query_num) {
        this.query_num = query_num;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public double getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(double total_fee) {
        this.total_fee = total_fee;
    }

    public List<String> getAuth() {
        return auth;
    }

    public void setAuth(List<String> auth) {
        this.auth = auth;
    }
}
