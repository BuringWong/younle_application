package com.younle.younle624.myapplication.domain.setting;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 我是奋斗 on 2016/6/16.
 * 微信/e-mail:tt090423@126.com
 * 商品设置对应的实体类，要保存啊到数据库
 */
public class Goods implements Serializable {

    /**
     * code : 200
     * msg : [{"sortName":"饮料","sortGoods":[{"id":"1","name":"果粒橙","price":"3.50","usable":"0"},{"id":"2","name":"雪碧","price":"4.00","usable":"1"},{"id":"3","name":"百事达可乐","price":"2.50","usable":"1"}]},{"sortName":"盖饭","sortGoods":[{"id":"4","name":"鱼香肉丝盖饭","price":"17.00","usable":"1"},{"id":"5","name":"过油肉盖饭","price":"16.00","usable":"1"}]},{"sortName":"面食","sortGoods":[{"id":"6","name":"牛肉面","price":"15.00","usable":"1"},{"id":"7","name":"酸汤面","price":"18.00","usable":"1"}]}]
     */

    private int code;
    /**
     * sortName : 饮料
     * sortGoods : [{"id":"1","name":"果粒橙","price":"3.50","usable":"0"},{"id":"2","name":"雪碧","price":"4.00","usable":"1"},{"id":"3","name":"百事达可乐","price":"2.50","usable":"1"}]
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
         * id : 1
         * name : 果粒橙
         * price : 3.50
         * usable : 0
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

            public String getUsable() {
                return usable;
            }

            public void setUsable(String usable) {
                this.usable = usable;
            }
        }
    }
}
