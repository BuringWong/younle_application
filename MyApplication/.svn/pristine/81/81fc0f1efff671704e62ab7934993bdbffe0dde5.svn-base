package com.younle.younle624.myapplication.domain;

import java.util.List;

/**
 * Created by 我是奋斗 on 2016/7/4.
 * 微信/e-mail:tt090423@126.com
 * 订单页面，实物类的打印详情页面
 */
public class EntityPrintBean {


    /**
     * goodsName : 南风实木家具
     * detail : [{"val":"交易时间：2016-06-28 14:01:40"},{"val":"交易编号：46000000468201606"},{"val":"购买数量：1"},{"val":"实付金额：5.00（5.00/个+运费）"},{"val":"订单状态：订单完成 "},{"val":"收货人：庞一鹤"},{"val":"收货人地址：北京市,北京市,东城区东打磨厂街7号 宝鼎中心C座 622"},{"val":"收货人手机：18210396402"},{"val":"邮编：725021"},{"val":"售出渠道：自营"}]
     */

    private MsgBean msg;

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        private String goodsName;
        /**
         * val : 交易时间：2016-06-28 14:01:40
         */

        private List<DetailBean> detail;

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public List<DetailBean> getDetail() {
            return detail;
        }

        public void setDetail(List<DetailBean> detail) {
            this.detail = detail;
        }

        public static class DetailBean {
            private String val;

            public String getVal() {
                return val;
            }

            public void setVal(String val) {
                this.val = val;
            }
        }
    }
}
