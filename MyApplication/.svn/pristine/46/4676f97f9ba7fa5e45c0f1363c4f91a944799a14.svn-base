package com.younle.younle624.myapplication.domain.setting;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 我是奋斗 on 2016/8/2.
 * 微信/e-mail:tt090423@126.com
 */
public class RoomSettingBean implements Serializable{


    /**
     * code : 200
     * msg : [{"sortName":"标准间","sortGoods":[{"id":"8","name":"301房间","price":"188.00","minConsume":"无","deposit":"100","usable":"1"},{"id":"9","name":"302房间","price":"267.00","minConsume":"50","deposit":"500","usable":"1"}]},{"sortName":"大床房","sortGoods":[{"id":"10","name":"303房间","price":"399.00","minConsume":"无","deposit":"300","usable":"1"},{"id":"11","name":"408房间","price":"399.00","minConsume":"无","deposit":"200","usable":"1"}]},{"sortName":"商务房","sortGoods":[{"id":"12","name":"409房间","price":"589.00","minConsume":"无","deposit":"无","usable":"1"}]}]
     */

    private int code;
    /**
     * sortName : 标准间
     * sortGoods : [{"id":"8","name":"301房间","price":"188.00","minConsume":"无","deposit":"100","usable":"1"},{"id":"9","name":"302房间","price":"267.00","minConsume":"50","deposit":"500","usable":"1"}]
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

    public static class MsgBean implements Serializable {
        private String sortName;
        /**
         * id : 8
         * name : 301房间
         * price : 188.00
         * minConsume : 无
         * deposit : 100
         * usable : 1
         */

        private List<SortGoodsBean> sortGoods;

        public String getSortName() {
            return sortName;
        }

        public void setSortName(String sortName) {
            this.sortName = sortName;
        }

        public List<SortGoodsBean> getSortGoods() {
            return sortGoods;
        }

        public void setSortGoods(List<SortGoodsBean> sortGoods) {
            this.sortGoods = sortGoods;
        }

        public static class SortGoodsBean implements Serializable {
            private String id;
            private String name;
            private String price;
            private String minConsume;
            private String deposit;
            private String usable;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getMinConsume() {
                return minConsume;
            }

            public void setMinConsume(String minConsume) {
                this.minConsume = minConsume;
            }

            public String getDeposit() {
                return deposit;
            }

            public void setDeposit(String deposit) {
                this.deposit = deposit;
            }

            public String getUsable() {
                return usable;
            }

            public void setUsable(String usable) {
                this.usable = usable;
            }
        }
    }
}
