package com.yongle.letuiweipad.domain.printer;


import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/10/23.
 */

public class GroupInfoBean implements Serializable {

    /**
     * code : 200
     * msg : [{"group_name":"22","id":"19"},{"group_name":"凉菜","id":"40"},{"group_name":"热菜","id":"41"},{"group_name":"主食","id":"42"},{"group_name":"酒水","id":"43"},{"group_name":"水果","id":"44"}]
     */

    private int code;
    /**
     * group_name : 22
     * id : 19
     */

    private List<PrintGroupBean> msg;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<PrintGroupBean> getMsg() {
        return msg;
    }

    public void setMsg(List<PrintGroupBean> msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "GroupInfoBean{" +
                "code=" + code +
                ", msg=" + msg +
                '}';
    }
}
