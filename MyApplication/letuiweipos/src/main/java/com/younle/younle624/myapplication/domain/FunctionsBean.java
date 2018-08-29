package com.younle.younle624.myapplication.domain;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Administrator on 2017/5/18.
 */

public class FunctionsBean {

    /**
     * authtxt : [{"name":"下单功能","wisdom":1,"xd":1},{"name":"商品管理及订单分析","wisdom":1,"xd":1},{"name":"库存管理","wisdom":1,"xd":1},{"name":"外接扫码枪","wisdom":1,"xd":1},{"name":"房间/桌台管理","wisdom":1,"xd":1},{"name":"房间/桌台计费","wisdom":1,"xd":1},{"name":"支付即会员","wisdom":1,"xd":1},{"name":"会员数据分析","wisdom":1,"xd":1},{"name":"会员折扣、储值","wisdom":1,"xd":1},{"name":"优惠券核销及营销数据","wisdom":1,"xd":1}]
     * m_mdp : 1440000
     * m_xd : 480000
     */

    private long m_mdp;
    private long m_xd;
    /**
     * name : 下单功能
     * wisdom : 1
     * xd : 1
     */

    private List<AuthtxtBean> authtxt;

    public static FunctionsBean objectFromData(String str) {

        return new Gson().fromJson(str, FunctionsBean.class);
    }

    public long getM_mdp() {
        return m_mdp;
    }

    public void setM_mdp(long m_mdp) {
        this.m_mdp = m_mdp;
    }

    public long getM_xd() {
        return m_xd;
    }

    public void setM_xd(long m_xd) {
        this.m_xd = m_xd;
    }

    public List<AuthtxtBean> getAuthtxt() {
        return authtxt;
    }

    public void setAuthtxt(List<AuthtxtBean> authtxt) {
        this.authtxt = authtxt;
    }

    public static class AuthtxtBean {
        private String name;
        private int wisdom;
        private int xd;

        public static AuthtxtBean objectFromData(String str) {

            return new Gson().fromJson(str, AuthtxtBean.class);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getWisdom() {
            return wisdom;
        }

        public void setWisdom(int wisdom) {
            this.wisdom = wisdom;
        }

        public int getXd() {
            return xd;
        }

        public void setXd(int xd) {
            this.xd = xd;
        }
    }
}
