package com.younle.younle624.myapplication.domain.setting;

import java.util.List;

/**
 * Created by 我是奋斗 on 2016/8/3.
 * 微信/e-mail:tt090423@126.com
 */
public class SettingItemBean {

    /**
     * code : 200
     * msg : [{"type":1,"name":"实物商品设置"},{"type":2,"name":"服务商品设置"},{"type":3,"name":"房间设置"},{"type":4,"name":"人员设置"}]
     */

    private int code;
    /**
     * type : 1
     * name : 实物商品设置
     */

    private List<MsgBean> msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<MsgBean> getMsg() {
        return msg;
    }

    public void setMsg(List<MsgBean> msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        private int type;
        private String name;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
