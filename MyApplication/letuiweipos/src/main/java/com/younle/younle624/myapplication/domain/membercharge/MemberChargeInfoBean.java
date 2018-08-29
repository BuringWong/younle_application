package com.younle.younle624.myapplication.domain.membercharge;

import java.util.List;

/**
 * Created by Administrator on 2016/11/23.
 */
public class MemberChargeInfoBean {

    /**
     * auth : ["1","3","5","6"]
     * code : 200
     * msg : {"headimgurl":"http://wx.qlogo.cn/mmopen/PiajxSqBRaELzrpMNccRGB9A62NwqLjNfrD2L3n78XWdBt892lR5D7zxPSWj44kzJV5HVysyv4QRUuDZsDppdGg/0","id":"1","money":201250,"name":"张开圆","nickname":"最爱喜洋洋","rules":[{"money":"100","song":"50","zong":"150"},{"money":"200","song":"100","zong":"300"},{"money":"300","song":"150","zong":"450"}],"supply":"true"}
     */

    private int code;
    /**
     * headimgurl : http://wx.qlogo.cn/mmopen/PiajxSqBRaELzrpMNccRGB9A62NwqLjNfrD2L3n78XWdBt892lR5D7zxPSWj44kzJV5HVysyv4QRUuDZsDppdGg/0
     * id : 1
     * money : 201250
     * name : 张开圆
     * nickname : 最爱喜洋洋
     * rules : [{"money":"100","song":"50","zong":"150"},{"money":"200","song":"100","zong":"300"},{"money":"300","song":"150","zong":"450"}]
     * supply : true
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
        private String headimgurl;
        private String id;
        private double money;
        private String name;
        private String nickname;
        private String supply;
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        /**
         * money : 100
         * song : 50
         * zong : 150
         */

        private List<RulesBean> rules;

        public String getHeadimgurl() {
            return headimgurl;
        }

        public void setHeadimgurl(String headimgurl) {
            this.headimgurl = headimgurl;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSupply() {
            return supply;
        }

        public void setSupply(String supply) {
            this.supply = supply;
        }

        public List<RulesBean> getRules() {
            return rules;
        }

        public void setRules(List<RulesBean> rules) {
            this.rules = rules;
        }

        public static class RulesBean {
            private String money;
            private String song;
            private String zong;

            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
            }

            public String getSong() {
                return song;
            }

            public void setSong(String song) {
                this.song = song;
            }

            public String getZong() {
                return zong;
            }

            public void setZong(String zong) {
                this.zong = zong;
            }
        }
    }
}
