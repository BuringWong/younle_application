package com.younle.younle624.myapplication.utils.createorder;

/**
 * 作者：Create by 我是奋斗 on2016/12/21 17:41
 * 邮箱：tt090423@126.com
 * 微信：17346512596
 * 总订单详情的工具方法
 */
public class OrderDetailUtils {

    public static OrderDetailUtils instance;
    public static OrderDetailUtils getInstance(){
        if(instance==null) {
            instance=new OrderDetailUtils();
        }
        return instance;
    }
    /**
     * 判断时间选择的合法性， 开始时间必须小于结束时间
     * 结束时间必须大于开始时间
     */
    public boolean isTimeChooseOk(long endMill,long startMill) {
        if (endMill == -1) {
            return true;
        } else {
            return startMill < endMill;
        }
    }
}
