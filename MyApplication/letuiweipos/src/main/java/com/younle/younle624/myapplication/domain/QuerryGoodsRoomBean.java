package com.younle.younle624.myapplication.domain;

import java.util.List;

/**
 * Created by bert_dong on 2016/11/5 0005.
 * 邮箱：18701038771@163.com
 */
public class QuerryGoodsRoomBean {


    /**
     * code : 200
     * auth : ["1","3","5"]
     * msg : {"flag":0,"no_fee":1,"hour_fee":0,"day_fee":0}
     */

    private int code;
    /**
     * flag : 0
     * no_fee : 1
     * hour_fee : 0
     * day_fee : 0
     */

    private MsgBean msg;
    private List<String> auth;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public List<String> getAuth() {
        return auth;
    }

    public void setAuth(List<String> auth) {
        this.auth = auth;
    }

    public static class MsgBean {
        private int flag;
        private int no_fee;
        private int hour_fee;
        private int day_fee;

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public int getNo_fee() {
            return no_fee;
        }

        public void setNo_fee(int no_fee) {
            this.no_fee = no_fee;
        }

        public int getHour_fee() {
            return hour_fee;
        }

        public void setHour_fee(int hour_fee) {
            this.hour_fee = hour_fee;
        }

        public int getDay_fee() {
            return day_fee;
        }

        public void setDay_fee(int day_fee) {
            this.day_fee = day_fee;
        }
    }
}
