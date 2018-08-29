package com.yongle.letuiweipad.domain.printer;

/**
 * Created by Administrator on 2017/10/24.
 * 打印机分组打印的实体类，包含要打的内容，和要打的商品所属分组id
 */

public class YunPrintGroupBean {
    private String msg;
    private String groupId;

    public YunPrintGroupBean(String msg, String groupId) {
        this.msg = msg;
        this.groupId = groupId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "YunPrintGroupBean{" +
                "msg='" + msg + '\'' +
                ", groupId='" + groupId + '\'' +
                '}';
    }
}
