package com.younle.younle624.myapplication.domain;

/**
 * Created by bert_dong on 2016/10/27 0027.
 * 邮箱：18701038771@163.com
 */
public class ResultQueryBean {

    /**
     * code : 200
     * msg : {"success":0}
     */

    private int code;
    /**
     * success : 0
     */

    private MsgBean msg;

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

    public static class MsgBean {
        private String success;
        private String transaction_id;
        private double payment;

        public double getPayment() {
            return payment;
        }

        public void setPayment(double payment) {
            this.payment = payment;
        }

        public String getTransaction_id() {
            return transaction_id;
        }

        public void setTransaction_id(String transaction_id) {
            this.transaction_id = transaction_id;
        }
        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }
    }
}
