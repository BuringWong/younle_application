package com.younle.younle624.myapplication.domain;

import java.util.List;

/**
 * Created by 我是奋斗 on 2016/6/21.
 * 微信/e-mail:tt090423@126.com
 */
public class VoucherInfo {

    /**
     * code : 200
     * msg : {"name":"井格老灶火锅1（石景山店）","detail":[{"val":"交易时间： 2016-06-20"},{"val":"交易编号： 20859428502903"},{"val":"订单类型： 即时到账"},{"val":"付款人： 不经历风雨怎能见彩虹(保密 北京 朝阳)"},{"val":"付款方式： 微信支付"},{"val":"收款账户： 商户自己账户"},{"val":"实付金额： 256.34"},{"val":"适用门店： 大望路店、也许会、也许会、也许会"},{"val":"是否核销： 否"},{"val":"是否退款： 否"},{"val":"代金券详情： &lt;ol class=&quot; list-paddingleft-2&quot; style=&quot;list-style-type: decimal;&quot;&gt;&lt;li&gt;&lt;p&gt;好&lt;/p&gt;&lt;/li&gt;&lt;li&gt;&lt;p&gt;好棒&lt;/p&gt;&lt;/li&gt;&lt;li&gt;&lt;p&gt;很好&lt;/p&gt;&lt;/li&gt;&lt;li&gt;&lt;p&gt;非常好&lt;br/&gt;&lt;/p&gt;&lt;/li&gt;&lt;/ol&gt;"}]}
     */

    private int code;
    /**
     * name : 井格老灶火锅1（石景山店）
     * detail : [{"val":"交易时间： 2016-06-20"},{"val":"交易编号： 20859428502903"},{"val":"订单类型： 即时到账"},{"val":"付款人： 不经历风雨怎能见彩虹(保密 北京 朝阳)"},{"val":"付款方式： 微信支付"},{"val":"收款账户： 商户自己账户"},{"val":"实付金额： 256.34"},{"val":"适用门店： 大望路店、也许会、也许会、也许会"},{"val":"是否核销： 否"},{"val":"是否退款： 否"},{"val":"代金券详情： &lt;ol class=&quot; list-paddingleft-2&quot; style=&quot;list-style-type: decimal;&quot;&gt;&lt;li&gt;&lt;p&gt;好&lt;/p&gt;&lt;/li&gt;&lt;li&gt;&lt;p&gt;好棒&lt;/p&gt;&lt;/li&gt;&lt;li&gt;&lt;p&gt;很好&lt;/p&gt;&lt;/li&gt;&lt;li&gt;&lt;p&gt;非常好&lt;br/&gt;&lt;/p&gt;&lt;/li&gt;&lt;/ol&gt;"}]
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

    public static class MsgBean {
        private String name;
        /**
         * val : 交易时间： 2016-06-20
         */

        private List<DetailBean> detail;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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
