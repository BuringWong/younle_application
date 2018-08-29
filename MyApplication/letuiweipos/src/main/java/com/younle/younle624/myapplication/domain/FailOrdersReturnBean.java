package com.younle.younle624.myapplication.domain;

import java.util.List;

/**
 * Created by bert_dong on 2016/8/18 0018.
 * 邮箱：18701038771@163.com
 */
public class FailOrdersReturnBean {

    /**
     * order_no : 3036003619715169489435648098
     */

    private List<FailOrdersBean> fail_orders;

    public List<FailOrdersBean> getFail_orders() {
        return fail_orders;
    }

    public void setFail_orders(List<FailOrdersBean> fail_orders) {
        this.fail_orders = fail_orders;
    }

    public static class FailOrdersBean {
        private String order_no;

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }
    }
}
