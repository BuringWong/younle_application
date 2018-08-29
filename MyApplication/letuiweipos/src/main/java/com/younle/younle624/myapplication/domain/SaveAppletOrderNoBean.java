package com.younle.younle624.myapplication.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bert_dong on 2017/6/16 0016.
 * 邮箱：18701038771@163.com
 */
public class SaveAppletOrderNoBean implements Serializable{

    private MsgBean msg;

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        /**
         * time : 1000000000000000000
         * orderNo : 25
         */

        private List<SaveDataBean> saveData;

        public List<SaveDataBean> getSaveData() {
            return saveData;
        }

        public void setSaveData(List<SaveDataBean> saveData) {
            this.saveData = saveData;
        }

        public static class SaveDataBean {
            private long time;
            private String orderNo;
            private String unionid;

            public SaveDataBean(String orderNo,long time,String unionid) {
                this.time = time;
                this.orderNo = orderNo;
                this.unionid = unionid;
            }

            public String getUnionid() {
                return unionid;
            }

            public void setUnionid(String unionid) {
                this.unionid = unionid;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public String getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(String orderNo) {
                this.orderNo = orderNo;
            }
        }
    }
}
