package com.younle.younle624.myapplication.domain;

/**
 * Created by bert_dong on 2017/5/17 0017.
 * 邮箱：18701038771@163.com
 *
 * 传到支付接口 cardinfo字段里的 bean
 */
public class PayCardinfoBean {

    private String code;
    private String cardid;
    private String cardtype;
    private String vipcardid;
    private String least_cost;
    private String reduce_cost;
    private String discount;
    private String info;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

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

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }

    public String getVipcardid() {
        return vipcardid;
    }

    public void setVipcardid(String vipcardid) {
        this.vipcardid = vipcardid;
    }

    public String getLeast_cost() {
        return least_cost;
    }

    public void setLeast_cost(String least_cost) {
        this.least_cost = least_cost;
    }

    public String getReduce_cost() {
        return reduce_cost;
    }

    public void setReduce_cost(String reduce_cost) {
        this.reduce_cost = reduce_cost;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
