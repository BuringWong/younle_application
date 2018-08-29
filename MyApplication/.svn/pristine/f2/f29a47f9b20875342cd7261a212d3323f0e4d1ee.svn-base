package com.younle.younle624.myapplication.domain.orderbean;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
public class DetailPosDiscountVoucherBean implements Serializable {


    /**
     * auth : ["1","2","3","4","5","6"]
     * code : 200
     * msg : {"card_type":"DISCOUNT","discount":8,"errcode":0,"info":"整单8折","usetime":"2017-05-02 00:00:00-2017-09-30 23:59:59","vipcardid":"100","vipinfo_name":""}
     * returnsys : d*I*Id***F27KB3jr7OE3iFjogxe
     */

    private int code;
    /**
     * card_type : DISCOUNT
     * discount : 8
     * errcode : 0
     * info : 整单8折
     * usetime : 2017-05-02 00:00:00-2017-09-30 23:59:59
     * vipcardid : 100
     * vipinfo_name :
     */

    private MsgBean msg;
    private String returnsys;
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

    public String getReturnsys() {
        return returnsys;
    }

    public void setReturnsys(String returnsys) {
        this.returnsys = returnsys;
    }

    public List<String> getAuth() {
        return auth;
    }

    public void setAuth(List<String> auth) {
        this.auth = auth;
    }

    public static class MsgBean implements Serializable {
        private String card_type;
        private double discount;
        private int errcode;
        private String info;
        private String usetime;
        private String vipcardid;
        private String vipinfo_name;
        private String cardid;
        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCardid() {
            return cardid;
        }

        public void setCardid(String cardid) {
            this.cardid = cardid;
        }

        public String getCard_type() {
            return card_type;
        }

        public void setCard_type(String card_type) {
            this.card_type = card_type;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public int getErrcode() {
            return errcode;
        }

        public void setErrcode(int errcode) {
            this.errcode = errcode;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getUsetime() {
            return usetime;
        }

        public void setUsetime(String usetime) {
            this.usetime = usetime;
        }

        public String getVipcardid() {
            return vipcardid;
        }

        public void setVipcardid(String vipcardid) {
            this.vipcardid = vipcardid;
        }

        public String getVipinfo_name() {
            return vipinfo_name;
        }

        public void setVipinfo_name(String vipinfo_name) {
            this.vipinfo_name = vipinfo_name;
        }
    }
}