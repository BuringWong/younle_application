package com.younle.younle624.myapplication.domain;

import java.io.Serializable;

/**
 * Created by 我是奋斗 on 2016/6/28.
 * 微信/e-mail:tt090423@126.com
 * 
 * 代金券核销，代金券的信息
 */
public class VoucherRead implements Serializable{


    /**
     * code : 200
     * msg : {"secmark":"2","server_name":"井格老灶火锅1（石景山店）","vhs_start":"2016年04月10日","vhs_end":"2017年01月01日","vhs_info":"&lt;p style=&quot;color:#AAA;&quot;&gt;如：xxxxxx料理1份，价值¥68&lt;/p&gt;","status":"核销"}
     */

    private int code;
    /**
     * secmark : 2
     * server_name : 井格老灶火锅1（石景山店）
     * vhs_start : 2016年04月10日
     * vhs_end : 2017年01月01日
     * vhs_info : &lt;p style=&quot;color:#AAA;&quot;&gt;如：xxxxxx料理1份，价值¥68&lt;/p&gt;
     * status : 核销
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

    public static class MsgBean implements Serializable {
        private String secmark;
        private String server_name;
        private String vhs_start;
        private String vhs_end;
        private String vhs_info;
        private String status;

        public String getSecmark() {
            return secmark;
        }

        public void setSecmark(String secmark) {
            this.secmark = secmark;
        }

        public String getServer_name() {
            return server_name;
        }

        public void setServer_name(String server_name) {
            this.server_name = server_name;
        }

        public String getVhs_start() {
            return vhs_start;
        }

        public void setVhs_start(String vhs_start) {
            this.vhs_start = vhs_start;
        }

        public String getVhs_end() {
            return vhs_end;
        }

        public void setVhs_end(String vhs_end) {
            this.vhs_end = vhs_end;
        }

        public String getVhs_info() {
            return vhs_info;
        }

        public void setVhs_info(String vhs_info) {
            this.vhs_info = vhs_info;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
